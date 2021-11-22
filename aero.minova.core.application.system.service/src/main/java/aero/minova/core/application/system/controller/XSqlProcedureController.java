package aero.minova.core.application.system.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.CustomLogger;
import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.ProcedureException;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.SqlProcedureResult;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.XProcedureException;
import aero.minova.core.application.system.domain.XSqlProcedureResult;
import aero.minova.core.application.system.domain.XTable;
import aero.minova.core.application.system.service.SecurityService;
import aero.minova.core.application.system.sql.SystemDatabase;

@RestController
public class XSqlProcedureController {

	@Autowired
	SystemDatabase systemDatabase;

	@Autowired
	CustomLogger customLogger;

	@Autowired
	SecurityService securityService;

	@Autowired
	SqlProcedureController sqlProcedureController;

	@Autowired
	SqlViewController sqlViewController;

	/**
	 * Führt eine Liste von voneinander abhängenden SQL-Prozeduren aus. Die Prozeduren müssen in der richtigen Reihenfolge gesendet werden. Erst wenn alle
	 * Prozeduren erfolgreich ausgeführt wurden, wird ein Commit getätigt. Tritt ein Fehler auf, werden alle bereits ausgeführten Prozeduren ge-rollbacked.
	 *
	 * @param inputTables
	 *            Liste der Tables mit Namen der Prozeduren und deren Aufruf Parameter
	 * @return Ergebnis Liste der Ergebnisse der Prozeduren-Aufrufe
	 * @throws Exception
	 *             Fehler bei der Ausführung von einer der Prozeduren. Rollback.
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "data/x-procedure")
	public ResponseEntity executeProcedures(@RequestBody List<XTable> inputTables) throws Exception {

		customLogger.logUserRequest("data/x-procedure: ", inputTables);
		List<XSqlProcedureResult> resultSets = new ArrayList<>();

		SqlProcedureResult result = new SqlProcedureResult();
		StringBuffer sb = new StringBuffer();
		Connection connection = null;

		try {
			connection = systemDatabase.getConnection();
			for (XTable xt : inputTables) {
				Table filledTable = new Table();
				// Referenzen auf Ergebnisse bereits ausgeführter Prozeduren auflösen.
				filledTable = fillInDependencies(xt, resultSets);

				// Rechteprüfung
				final List<Row> privilegeRequest = new ArrayList<>();
				if (securityService.arePrivilegeStoresSetup()) {
					privilegeRequest.addAll(securityService.getPrivilegePermissions(filledTable.getName()));
					if (privilegeRequest.isEmpty()) {
						throw new ProcedureException("msg.PrivilegeError %" + filledTable.getName());
					}
				}
				sqlProcedureController.calculateSqlProcedureResult(filledTable, privilegeRequest, connection, result, sb);
				// SqlProcedureResult wird in Liste hinzugefügt, um dessen Werte später in andere Values schreiben zu können.
				resultSets.add(new XSqlProcedureResult(xt.getId(), result));

			}

			// TODO hier die Check-Methode ausführen

		} catch (Exception e) {
			customLogger.logError("XSqlProcedure could not be executed: " + sb.toString(), e);
			try {
				connection.rollback();
			} catch (Exception e1) {
				customLogger.logError("Couldn't roll back xSqlProcedure execution", e);
			}
			throw new XProcedureException(inputTables, resultSets, e);
		} finally {
			systemDatabase.freeUpConnection(connection);
		}
		return new ResponseEntity(resultSets, HttpStatus.ACCEPTED);
	}

	/**
	 * Falls es einen Verweis auf ein OutputParameter einer anderen Prozedur gibt, wird der Value hier ersetzt.
	 * 
	 * @param dependant
	 *            Die Table, welche den potentiell-zu-ersetzenden Value enthält.
	 * @param dependencies
	 *            Die Liste an bereits ausgeführten xSqlProcedureResults. Diese beinhalten die Referenz-Values.
	 * @return Eine Table, in welcher der Value bereits ersetzt wurde.
	 */
	Table fillInDependencies(XTable dependant, List<XSqlProcedureResult> dependencies) {

		Table workingTable = dependant.getTable();

		for (Row r : workingTable.getRows()) {
			for (int i = 0; i < r.getValues().size(); i++) {
				aero.minova.core.application.system.domain.Value v = r.getValues().get(i);
				// Die Referenz-Id steht in der Rule des Values. Im Value des Values steht, in welcher Column das der gewünschte Parameter steht.
				if (v != null && v.getRule() != null) {
					XSqlProcedureResult dependency = findxSqlResultSet(v.getRule(), dependencies);
					int position = 0;

					String stringValue = v.getStringValue();
					/*
					 * Bei mehreren Rows in einer Referenztabelle, wird mit - die Position angegeben, in der geschickten Table z.B. r-parent_call-0-KeyLong Der
					 * Value dazu würde hier folgendermaßen aussehen: Value("0-KeyLong","parent_call")
					 */
					if (stringValue.contains("-")) {
						String[] valueParts = stringValue.split("-");

						position = Integer.parseInt(valueParts[0]);
						stringValue = valueParts[1];
					}

					// Ohne Outputparameter kann man nichts referenzieren
					if (dependency.getResultSet().getOutputParameters() == null) {
						throw new RuntimeException("No output parameters for resultset with id " + dependency.getId());
					}

					aero.minova.core.application.system.domain.Value newValue = findValueInColumn(dependency.getResultSet(), stringValue, position);
					// Tausche Value mit dem Ergebnis aus einem der ResultSets aus.
					r.getValues().remove(i);
					r.getValues().add(i, newValue);
				}
			}
		}

		return workingTable;
	}

	/**
	 * Findet den Value anhand des Spaltennamens.
	 * 
	 * @param dependency
	 *            Das SqlProcedureResult, welches den gewünschten Value enthält.
	 * @param columnName
	 *            Der Spaltenname der Spalte, welche den gesuchten Value enthält.
	 * @return Der Value aus der Spalte mit dem gesuchten Spaltennamen.
	 */
	aero.minova.core.application.system.domain.Value findValueInColumn(SqlProcedureResult dependency, String columnName, int row) {
		int position = -1;
		for (int i = 0; i < dependency.getOutputParameters().getColumns().size(); i++) {
			if (dependency.getOutputParameters().getColumns().get(i).getName().equals(columnName)) {
				position = i;
				break;
			}
		}
		if (position > -1) {
			return dependency.getOutputParameters().getRows().get(row).getValues().get(position);
		} else {
			throw new RuntimeException("Cannot find Column with name " + columnName);
		}
	}

	/**
	 * Findet die gesuchte Referenz-Tabelle in den ResultSets.
	 * 
	 * @param idToFind
	 *            Die Id der Tabelle, welche zurückgegeben werden soll.
	 * @param resultsets
	 *            Die Liste an xSqlProcedureResults, welche zur Verfügung stehen.
	 * @return Das xSqlProcedureResult, welches die gewünschte Id hat.
	 */
	XSqlProcedureResult findxSqlResultSet(String idToFind, List<XSqlProcedureResult> resultsets) {
		for (XSqlProcedureResult xSqlResult : resultsets) {
			if (xSqlResult.getId().equals(idToFind)) {
				return xSqlResult;
			}
		}
		throw new RuntimeException("Cannot find SqlProcedureResult with Id " + idToFind);
	}

	private void checkFollowUpProcedures(List<XTable> inputTables) {
		for (XTable xTable : inputTables) {
			// Die bevorzugte Sprache aus der Datenbank holen.
			Table sqlRequest = new Table();
			sqlRequest.setName("vcasBirtInvoiceServiceContractContact");
			sqlRequest.addColumn(new Column("KeyLong", DataType.INTEGER));
			sqlRequest.addColumn(new Column("Language", DataType.STRING));
			{
				Row requestParam = new Row();
				sqlRequest.getRows().add(requestParam);
				requestParam.addValue(null);
				requestParam.addValue(null);
			}
			try {
				Table viewResult = securityService.getTableForSecurityCheck(sqlRequest);

				// Überprüfen, ob wir überhaupt einen Eintrag in der Datenbank dafür haben.
				if (viewResult.getRows().size() != 0 && viewResult.getRows().get(0).getValues().get(1).getValue() != null) {}

			} catch (Exception e) {
				throw new RuntimeException("Could not find Language for InvoiceKey ", e);
			}

		}
	}

}
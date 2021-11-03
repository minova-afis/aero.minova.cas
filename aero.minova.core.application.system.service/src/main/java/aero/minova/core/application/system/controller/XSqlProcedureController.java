package aero.minova.core.application.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import aero.minova.core.application.system.CustomLogger;
import aero.minova.core.application.system.domain.ProcedureException;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.SqlProcedureResult;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.XSqlProcedureResult;
import aero.minova.core.application.system.domain.XTable;
import aero.minova.core.application.system.service.SecurityService;
import aero.minova.core.application.system.sql.SystemDatabase;
import lombok.val;

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
	Gson gson;

	/**
	 * Führt eine Liste von voneinander abhängenden SQL-Prozeduren aus. Wir können davon ausgehen, dass die Liste der Prozeduren auch in der richtigen
	 * Reihenfolge sind.
	 *
	 * @param inputTables
	 *            Liste der Tables mit Namen der Prozeduren und deren Aufruf Parameter
	 * @return Ergebnis Liste der Ergebnisse der Prozeduren-Aufrufe
	 * @throws Exception
	 *             Fehler bei der Ausführung
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "data/x-procedure")
	public ResponseEntity executeProcedure(@RequestBody List<XTable> inputTables) throws Exception {
		customLogger.logUserRequest("data/x-procedure: " + gson.toJson(inputTables));
		List<XSqlProcedureResult> resultSets = new ArrayList<>();

		for (XTable xt : inputTables) {
			Table filledTable = new Table();

			try {
				// Referenzen auf Ergebnisse bereits ausgeführter Prozeduren auflösen.
				filledTable = fillInDependencies(xt, resultSets);

				// Rechteprüfung
				final List<Row> privilegeRequest = new ArrayList<>();
				if (sqlProcedureController.arePrivilegeStoresSetup()) {
					privilegeRequest.addAll(securityService.getPrivilegePermissions(filledTable.getName()));
					if (privilegeRequest.isEmpty()) {
						throw new ProcedureException("msg.PrivilegeError %" + filledTable.getName());
					}
				}

				val result = sqlProcedureController.calculateSqlProcedureResult(filledTable, privilegeRequest);
				// SqlProcedureResult wird in Liste hinzugefügt, um dessen Werte später in andere Values schreiben zu können.
				resultSets.add(new XSqlProcedureResult(xt.getId(), result));

			} catch (Exception e) {
				customLogger.logError("Error while trying to execute procedure: " + xt.getTable().getName(), e);
				throw e;
			}
		}

		return new ResponseEntity(resultSets, HttpStatus.ACCEPTED);
	}

	/**
	 * Falls es einen Verweis auf ein OutputParameter einer anderen Prozedur gibt, wird der Value hier ersetzt.
	 * 
	 * @param xtable
	 *            Die Table, welche den potentiell-zu-ersetzenden Value enthält.
	 * @param resultsets
	 *            Die Liste an bereits ausgeführten xSqlProcedureResults. Diese beinhalten die Referenz-Values.
	 * @return Eine Table, in welcher der Value bereits ersetzt wurde.
	 */
	private Table fillInDependencies(XTable xtable, List<XSqlProcedureResult> resultsets) {

		Table workingTable = xtable.getTable();

		for (Row r : workingTable.getRows()) {
			for (int i = 0; i < r.getValues().size(); i++) {
				aero.minova.core.application.system.domain.Value v = r.getValues().get(i);
				// Die Referenz-Id steht in der Rule des Values. Im Value des Values steht, in welcher Column das der gewünschte Parameter steht.
				if (v != null && v.getRule() != null) {
					XSqlProcedureResult dependency = findxSqlResultSet(v.getRule(), resultsets);
					aero.minova.core.application.system.domain.Value newValue = findValue(dependency.getResultSet(), v.getStringValue());
					// Tauche Value mit dem Ergebnis aus einem der ResultSets aus.
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
	private aero.minova.core.application.system.domain.Value findValue(SqlProcedureResult dependency, String columnName) {
		int position = -1;
		for (int i = 0; i < dependency.getOutputParameters().getColumns().size(); i++) {
			if (dependency.getOutputParameters().getColumns().get(i).getName().toLowerCase().equals(columnName.toLowerCase())) {
				position = i;
				break;
			}
		}
		if (position > -1) {
			return dependency.getOutputParameters().getRows().get(0).getValues().get(position);
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
	private XSqlProcedureResult findxSqlResultSet(String idToFind, List<XSqlProcedureResult> resultsets) {
		for (XSqlProcedureResult xSqlResult : resultsets) {
			if (xSqlResult.getId().equals(idToFind)) {
				return xSqlResult;
			}
		}
		throw new RuntimeException("Cannot find SqlProcedureResult with Id " + idToFind);
	}

}

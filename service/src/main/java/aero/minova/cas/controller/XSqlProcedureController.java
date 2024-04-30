package aero.minova.cas.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.ProcedureException;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.api.domain.XProcedureException;
import aero.minova.cas.api.domain.XSqlProcedureResult;
import aero.minova.cas.api.domain.XTable;
import aero.minova.cas.service.ProcedureService;
import aero.minova.cas.service.QueueService;
import aero.minova.cas.service.SecurityService;
import aero.minova.cas.sql.SystemDatabase;

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
	ProcedureService procedureService;

	@Autowired
	SqlViewController sqlViewController;

	@Autowired
	QueueService queueService;

	/**
	 * Das sind Registrierungen, die ausgeführt werden, wenn eine Prozedur in der Liste mit den Namen der Registrierung ausgeführt werden soll.
	 */
	private final Map<String, BiFunction<List<XTable>, Map<Table, List<SqlProcedureResult>>, List<XSqlProcedureResult>>> extensions = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);

	/**
	 * Hiermit lassen sich Erweiterungen registrieren. Hat eine Tabelle in der Transaktion den registrierten Namen, so wird die gesamte Transaktion an die
	 * Erweiterung weitergeleitet. Rollbacks oder Checks werden dann NICHT automatisch ausgeführt, dafür ist die Erweiterung selbst zuständig.
	 *
	 * @param name
	 *            der Name, auf den registriert werden soll (Case insensitive, nur eine Extension pro Name möglich)
	 * @param ext
	 *            die Extension. Es gibt zwei Inputs. Zum einen eine Liste aus XTables, die die eigentlichen Input-Daten darstellt. <br>
	 *            Zum Anderen eine Map aus Tables auf Listen von SqlProcedureResults. Diese ist zu Beginn leer und muss gefüllt werden, wenn Nachrichten über
	 *            den QueueService verschickt werden sollen. Ist dies nicht der Fall, kann die Map ignoriert werden. <br>
	 *            Die Extension muss eine Liste von XSqlProcedureResults zurückgeben, ein Result pro Input-Tabelle.
	 * @throws IllegalArgumentException
	 *             beim Versuch, einen bereits verwendeten Namen für eine andere Extension zu registrieren
	 */
	public void registerExtension(String name, BiFunction<List<XTable>, Map<Table, List<SqlProcedureResult>>, List<XSqlProcedureResult>> ext) {
		if (extensions.containsKey(name)) {
			customLogger.logSetup("Cannot register two extensions with the same name: " + name);
			throw new IllegalArgumentException(name);
		}
		extensions.put(name, ext);
	}

	/**
	 * Führt eine Liste von voneinander abhängenden SQL-Prozeduren aus. Die Prozeduren müssen in der richtigen Reihenfolge gesendet werden. <br>
	 * <br>
	 * Wurde eine Extension auf die Transaktion registriert, so wird diese statt den SQL-Aufrufen durchgeführt, Rollbacks oder Checks werden dann NICHT
	 * automatisch ausgeführt, dafür ist die Erweiterung selbst zuständig. <br>
	 * <br>
	 * Gibt es keine Extension, wird ein Commit erst getätigt, wenn alle Prozeduren - d.h. die Anfrage und die dazugehörigen Checks - erfolgreich ausgeführt
	 * wurden. Tritt ein Fehler auf, werden alle bereits ausgeführten Prozeduren zurückgerollt.<br>
	 * <br>
	 * In jedem Fall werden nach dem erfolgreichen Ausführen der Prozeduren oder der Erweiterungen Nachrichten über den QueueService verschickt.
	 *
	 * @param inputTables
	 *            Liste der Tables mit Namen der Prozeduren und deren Aufruf Parameter
	 * @return Ergebnis Liste der Ergebnisse der Prozeduren-Aufrufe
	 * @throws Exception
	 *             Fehler bei der Ausführung von einer der Prozeduren. Rollback, falls keine Extension für die gesamte Transaktion ausgeführt wurde
	 */
	@PostMapping(value = "data/x-procedure")
	public ResponseEntity<List<XSqlProcedureResult>> executeProcedures(@RequestBody List<XTable> inputTables) throws Exception {

		customLogger.logUserRequest("data/x-procedure: ", inputTables);
		List<XSqlProcedureResult> resultSets = new ArrayList<>();

		Connection connection = null;
		StringBuffer sb = new StringBuffer();
		try {

			Map<Table, List<SqlProcedureResult>> inputTablesWithResults = new HashMap<>();

			// Soll die Transaktion von einer Erweiterung bearbeitet werden?
			Optional<List<XSqlProcedureResult>> checkForExtensions = checkForExtensions(inputTables, inputTablesWithResults);
			if (checkForExtensions.isPresent()) {
				resultSets = checkForExtensions.get();

			} else { // Ansonsten die Prozeduren einzeln verarbeiten

				connection = systemDatabase.getConnection();
				resultSets = processXProcedures(inputTables, resultSets, sb, connection, inputTablesWithResults);
				// Hier werden die Checks nach der eigentlichen Anfrage ausgeführt.
				checkFollowUpProcedures(inputTables, resultSets, sb, connection, inputTablesWithResults);
				// Erst wenn auch die Checks erfolgreich waren, wird der Commit gesendet.
				connection.commit();
			}

			// Nachdem alle Prozeduren und Folgeprozeduren bzw. die Erweiterung erfolgreich durchgelaufen sind, kann man die Nachrichten über den QueueService
			// verschicken.
			for (Map.Entry<Table, List<SqlProcedureResult>> mapEntry : inputTablesWithResults.entrySet()) {
				for (SqlProcedureResult result : mapEntry.getValue()) {
					queueService.accept(mapEntry.getKey(), new ResponseEntity<>(result, HttpStatus.ACCEPTED));
				}
			}
		} catch (Throwable e) {
			customLogger.logError("XSqlProcedure could not be executed: " + sb, e);
			if (connection != null) {
				try {
					connection.rollback();
				} catch (Exception e1) {
					customLogger.logError("Couldn't roll back xSqlProcedure execution", e);
				}
			}
			throw new XProcedureException(inputTables, resultSets, e);
		} finally {
			systemDatabase.closeConnection(connection);
		}
		return new ResponseEntity<>(resultSets, HttpStatus.ACCEPTED);
	}

	/**
	 * Überprüft, ob es eine Erweiterung für die Transaktion gibt. Wenn ja, so werden die Privilegien für jede Prozedur der Transaktion geprüft. Wenn alle
	 * Privilegien geprüft sind, wird die Erweiterung ausgeführt und ihr Ergebnis zurückgegeben.
	 *
	 * @param inputTables
	 * @param inputTablesWithResults
	 * @return
	 * @throws ProcedureException
	 */
	private Optional<List<XSqlProcedureResult>> checkForExtensions(List<XTable> inputTables, Map<Table, List<SqlProcedureResult>> inputTablesWithResults)
			throws ProcedureException {

		List<XSqlProcedureResult> extResult = null;
		BiFunction<List<XTable>, Map<Table, List<SqlProcedureResult>>, List<XSqlProcedureResult>> extensionFunction = null;

		// Gibt es eine Extension für die Transaktion?
		for (XTable table : inputTables) {
			if (extensions.containsKey(table.getTable().getName())) {
				extensionFunction = extensions.get(table.getTable().getName());
				break;
			}
		}

		// Privilegien Prüfen und Extension ausführen
		if (extensionFunction != null) {
			for (XTable table : inputTables) {
				List<Row> privilegeRequest = new ArrayList<>();
				privilegeRequest.addAll(securityService.getPrivilegePermissions(table.getTable().getName()));
				if (privilegeRequest.isEmpty()) {
					throw new ProcedureException("msg.PrivilegeError %" + table.getTable());
				}
			}

			extResult = extensionFunction.apply(inputTables, inputTablesWithResults);
		}

		return Optional.ofNullable(extResult);
	}

	/**
	 * Überprüft, ob nötige Privilegien für jede XTable vorhanden sind und führt dann die entsprechende Prozedur aus. Hier wird noch KEIN Commit auf die
	 * Datenbank durchgeführt. Es wird ebenfalls überprüft, ob eine mögliche Extension vorhanden ist. Dient nur der Abwärtskompatibilität und ist deswegen
	 * deprecated.
	 *
	 * @param inputTables
	 *            Eine Liste an XTables, welche Prozeduren enthalten ausgeführt werden sollen.
	 * @param resultSets
	 *            Die bisher noch leere Liste an XSqlResultSets, welche in dieser Methode gefüllt weden.
	 * @param sb
	 *            Ein StringBuffer.
	 * @param connection
	 *            Eine Connection zur Datenbank. QueueService später benötigt.
	 * @return Gibt die zuvor leere Liste an XSqlProcedureResults zurück, welche nun gefüllt ist.
	 * @throws Exception
	 *             Wirft einen Fehler, falls das Privileg nicht vorhanden ist oder es einen Fehler bei der Ausführung der Prozedur gab.
	 */
	@Deprecated
	private List<XSqlProcedureResult> processXProcedures(List<XTable> inputTables, List<XSqlProcedureResult> resultSets, StringBuffer sb, Connection connection)
			throws Exception {
		return processXProcedures(inputTables, resultSets, sb, connection, null);
	}

	/**
	 * Überprüft, ob nötige Privilegien für jede XTable vorhanden sind und führt dann die entsprechende Prozedur aus. Hier wird noch KEIN Commit auf die
	 * Datenbank durchgeführt. Es wird ebenfalls überprüft, ob eine mögliche Extension vorhanden ist.
	 *
	 * @param inputTables
	 *            Eine Liste an XTables, welche Prozeduren enthalten ausgeführt werden sollen.
	 * @param resultSets
	 *            Die bisher noch leere Liste an XSqlResultSets, welche in dieser Methode gefüllt weden.
	 * @param sb
	 *            Ein StringBuffer für das Loggen der Prozedur-Aufrufe
	 * @param connection
	 *            Eine Connection zur Datenbank.
	 * @param inputTablesWithResults
	 *            Eine Map, in welcher die InputTables und deren SqlProcedureResults gespeichert werden. Wird zum Versenden von Nachrichten über den
	 *            QueueService später benötigt.
	 * @return Gibt die zuvor leere Liste an XSqlProcedureResults zurück, welche nun gefüllt ist.
	 * @throws Exception
	 *             Wirft einen Fehler, falls das Privileg nicht vorhanden ist oder es einen Fehler bei der Ausführung der Prozedur gab.
	 */
	private List<XSqlProcedureResult> processXProcedures(List<XTable> inputTables, List<XSqlProcedureResult> resultSets, StringBuffer sb, Connection connection,
			Map<Table, List<SqlProcedureResult>> inputTablesWithResults) throws Exception {
		for (XTable xt : inputTables) {
			SqlProcedureResult result = new SqlProcedureResult();
			// Referenzen auf Ergebnisse bereits ausgeführter Prozeduren auflösen.
			Table filledTable = fillInDependencies(xt, resultSets);

			// Rechteprüfung
			final List<Row> privilegeRequest = new ArrayList<>();
			if (securityService.arePrivilegeStoresSetup()) {
				privilegeRequest.addAll(securityService.getPrivilegePermissions(filledTable.getName()));
				if (privilegeRequest.isEmpty()) {
					throw new ProcedureException("msg.PrivilegeError %" + filledTable.getName());
				}
			}

			ResponseEntity extensionResult = sqlProcedureController.checkForExtension(filledTable).orElse(null);

			// Falls Extension gefunden wurde, Extension ausführen, falls keine gefunden wurde, normal ausführen.
			if (extensionResult != null) {
				result = (SqlProcedureResult) extensionResult.getBody();
				customLogger.logSql("Extension succesfully executed with name: " + filledTable.getName());
			} else {
				result = procedureService.calculateSqlProcedureResult(filledTable, privilegeRequest, connection, result, sb);
				customLogger.logSql("Procedure succesfully executed: " + sb.toString());
			}
			// Die erste if-Bedingung ist eigentlich nur für die Abwärtskompatibilität da, damit hier keine NullPointerException geworfen wird.
			if (inputTablesWithResults != null) {
				// InputTable und Ergebnis hinzufügen, damit später Nachrichten versand werden können.
				if (inputTablesWithResults.containsKey(filledTable)) {
					inputTablesWithResults.get(filledTable).add(result);
				} else {
					List<SqlProcedureResult> resultList = new ArrayList<>();
					resultList.add(result);
					inputTablesWithResults.put(filledTable, resultList);
				}
			}

			// SqlProcedureResult wird in Liste hinzugefügt, um dessen Werte später in andere Values schreiben zu können.
			resultSets.add(new XSqlProcedureResult(xt.getId(), result));

		}
		return resultSets;
	}

	/**
	 * Falls es einen Verweis auf ein OutputParameter einer anderen Prozedur gibt, wird der Value hier ersetzt.
	 *
	 * @param dependant
	 *            Die Table, welche den potentiell-zu-ersetzenden Value enthält.
	 * @param dependencies
	 *            Die Liste an bereits ausgeführten xSqlProcedureResults. Diese beinhalten die Referenz-Values.
	 * @return Eine Table, in welcher der Value bereits ersetzt wurde.
	 * @throws RuntimeException
	 *             wenn das ResultSet einer Dependency keinen Output-Parameter enthält
	 * @throws RuntimeException
	 *             wenn im ResultSet einer Dependency kein Reference-Value gefunden wird
	 */
	Table fillInDependencies(XTable dependant, List<XSqlProcedureResult> dependencies) {

		Table workingTable = dependant.getTable();

		for (Row r : workingTable.getRows()) {
			for (int i = 0; i < r.getValues().size(); i++) {
				Value v = r.getValues().get(i);
				// Die Referenz-Id steht in der Rule des Values. Im Value des Values steht, in welcher Column das der gewünschte Parameter steht.
				if (v != null && v.getRule() != null) {
					XSqlProcedureResult dependency = findxSqlResultSet(v.getRule(), dependencies);
					int position = 0;

					String stringValue = v.getStringValue();
					/*
					 * Bei mehreren Rows in einer Referenztabelle, wird mit - die Position angegeben, in der geschickten Table z.B. r-parent_call-0-KeyLong. Der
					 * Value dazu würde hier folgendermaßen aussehen: Value("0-KeyLong","parent_call")
					 */
					if (stringValue.contains("-")) {
						// Spaltennamen könnten auch '-' enthalten, deshalb kein Split.
						String positionString = stringValue.substring(0, stringValue.indexOf("-"));
						position = Integer.parseInt(positionString);
						stringValue = stringValue.substring(positionString.length() + 1);
					}

					// Ohne OutputParameter kann man nichts referenzieren.
					if (dependency.getResultSet().getOutputParameters() == null) {
						throw new RuntimeException("No output parameters for resultset with id " + dependency.getId());
					}

					Value newValue = findValueInColumn(dependency.getResultSet(), stringValue, position).orElse(null);
					if (newValue == null) {
						throw new RuntimeException("No reference value found for column " + stringValue + " in row " + position + " !");
					}
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
	 * @return Der Value aus der Spalte mit dem gesuchten Spaltennamen oder null, wenn die Spalte nicht gefunden werden kann.
	 */
	Optional<Value> findValueInColumn(SqlProcedureResult dependency, String columnName, int row) {
		for (int i = 0; i < dependency.getOutputParameters().getColumns().size(); i++) {
			if (dependency.getOutputParameters().getColumns().get(i).getName().equals(columnName)) {
				return Optional.ofNullable(dependency.getOutputParameters().getRows().get(row).getValues().get(i));
			}
		}
		return Optional.empty();
	}

	/**
	 * Findet die gesuchte Referenz-Tabelle in den ResultSets.
	 *
	 * @param idToFind
	 *            Die Id der Tabelle, welche zurückgegeben werden soll.
	 * @param resultSets
	 *            Die Liste an xSqlProcedureResults, welche zur Verfügung stehen.
	 * @return Das xSqlProcedureResult, welches die gewünschte Id hat.
	 * @throws RuntimeException
	 *             wenn der gesuchte idToFind nicht vorhanden ist
	 */
	XSqlProcedureResult findxSqlResultSet(String idToFind, List<XSqlProcedureResult> resultSets) {
		for (XSqlProcedureResult xSqlResult : resultSets) {
			if (xSqlResult.getId().equals(idToFind)) {
				return xSqlResult;
			}
		}
		throw new RuntimeException("Cannot find SqlProcedureResult with Id " + idToFind);
	}

	/**
	 * Findet die gesuchte Referenz-Tabelle in den ResultSets.
	 *
	 * @param tableName
	 *            Der Name der Tabelle, welche zurückgegeben werden soll.
	 * @param resultSets
	 *            Die Liste an xSqlProcedureResults, welche zur Verfügung stehen.
	 * @return Eine Liste an xSqlProcedureResults, welche den gewünschten Namen haben.
	 */
	List<XSqlProcedureResult> findxSqlResultSetByName(String tableName, List<XSqlProcedureResult> resultSets) {
		List<XSqlProcedureResult> resultsWithThatName = new ArrayList<>();
		for (XSqlProcedureResult xSqlResult : resultSets) {
			if (xSqlResult.getResultSet() != null && xSqlResult.getResultSet().getOutputParameters() != null
					&& xSqlResult.getResultSet().getOutputParameters().getName().equals(tableName)) {
				resultsWithThatName.add(xSqlResult);
			}
		}
		return resultsWithThatName;
	}

	/**
	 * Findet anhand der übergebenen Liste an XTables und über einen Aufruf der xtcasUserPrivilege-Tabelle heraus, welche Check-Prozeduren für die gerade
	 * ausgeführten XProzeduren durchgeführt werden müssen. In dieser Methode wird noch kein Commit an die Datenbank gesendet. Methode dient nur zur
	 * Abwärtskompatibilität und ist deswegen deprecated.
	 *
	 * @param inputTables
	 *            Die Original-Anfrage, welche an das CAS gesendet wurde.
	 * @param xsqlResults
	 *            Die XSqlProcedureResults, welche die ausgeführten Prozeduren geliefert haben.
	 * @param connection
	 *            Die Verbindung zur Datenbank.
	 * @param sb
	 *            Ein StringBuffer, welcher das Ausführen der Check-Prozeduren loggt.
	 */
	@Deprecated
	private void checkFollowUpProcedures(List<XTable> inputTables, List<XSqlProcedureResult> xsqlResults, StringBuffer sb, Connection connection) {
		checkFollowUpProcedures(inputTables, xsqlResults, sb, connection, null);
	}

	/**
	 * Findet anhand der übergebenen Liste an XTables und über einen Aufruf der xtcasUserPrivilege-Tabelle heraus, welche Check-Prozeduren für die gerade
	 * ausgeführten XProzeduren durchgeführt werden müssen. In dieser Methode wird noch kein Commit an die Datenbank gesendet.
	 *
	 * @param inputTables
	 *            Die Original-Anfrage, welche an das CAS gesendet wurde.
	 * @param xsqlResults
	 *            Die XSqlProcedureResults, welche die ausgeführten Prozeduren geliefert haben.
	 * @param connection
	 *            Die Verbindung zur Datenbank.
	 * @param sb
	 *            Ein StringBuffer, welcher das Ausführen der Check-Prozeduren loggt.
	 * @param inputTablesWithResults
	 *            Eine Map, in welcher die InputTables und deren SqlProcedureResults gespeichert werden. Wird zum Versenden von Nachrichten über den
	 *            QueueService später benötigt.
	 * @throws RuntimeException
	 *             "msg.PrivilegeError"
	 */
	private void checkFollowUpProcedures(List<XTable> inputTables, List<XSqlProcedureResult> xsqlResults, StringBuffer sb, Connection connection,
			Map<Table, List<SqlProcedureResult>> inputTablesWithResults) {
		// Die nötigen Check-Prozeduren aus der xtcasUserPrivilege-Tabelle auslesen.
		Table privilegeRequest = new Table();
		privilegeRequest.setName("xtcasUserPrivilege");
		privilegeRequest.addColumn(new Column("KeyText", DataType.STRING));
		privilegeRequest.addColumn(new Column("TransactionChecker", DataType.STRING));

		List<Row> inputRows = new ArrayList<>(); // ToDo - was passiert mit inputRows?
		for (XTable xTable : inputTables) {
			Row requestParam = new Row();
			privilegeRequest.getRows().add(requestParam);
			requestParam.addValue(new Value(xTable.getTable().getName(), null));
			requestParam.addValue(null);

			inputRows.add(requestParam);
		}
		try {
			customLogger.logInfo("Checking for follow.up procedures...");
			Table checksPerPrivilege = securityService.unsecurelyGetIndexView(privilegeRequest);

			// Wir müssen ja eigentlich einen Eintrag in der Datenbank dazu haben, sonst hätten wir sie bisher nicht ausführen können.
			if (checksPerPrivilege.getRows().isEmpty()) {
				throw new RuntimeException("msg.PrivilegeError");
			}

			// Neue Prozedur-Aufrufe für alle gefundenen TransactionChecker zusammenbauen.
			List<XTable> checksXtables = new ArrayList<>();
			for (Row privilegeCheck : checksPerPrivilege.getRows()) {
				if (privilegeCheck.getValues().size() >= 2 && privilegeCheck.getValues().get(1) != null) {
					String dependencyTableName = privilegeCheck.getValues().get(0).getStringValue();
					String transactionChecker = privilegeCheck.getValues().get(1).getStringValue();

					// Alle ResultSets mit diesem Namen (nicht ID) müssen gecheckt werden.
					List<XSqlProcedureResult> resultsToCheck = findxSqlResultSetByName(dependencyTableName, xsqlResults);

					// Falls keine passenden OutputParameter gefunden werden können, muss das ResultSet des Haupt-Aufrufs (der erste in der Transaktion)
					// verwendet werden.
					if (resultsToCheck.isEmpty()) {
						resultsToCheck.add(xsqlResults.get(0));
					}

					// Und von diesen muss jede Row geprüft werden. Dabei holen wir uns jedes Mal den KeyLong (siehe Doku).
					for (XSqlProcedureResult res : resultsToCheck) {
						XTable followUpCheck = new XTable();
						followUpCheck.setId(dependencyTableName + transactionChecker);
						Table innerTable = new Table();
						innerTable.setName(transactionChecker);
						innerTable.addColumn(new Column("KeyLong", DataType.INTEGER));
						List<Row> checkArguments = new ArrayList<>();

						if (res.getResultSet().getOutputParameters() != null && res.getResultSet().getOutputParameters().getRows() != null) {
							for (int i = 0; i < res.getResultSet().getOutputParameters().getRows().size(); i++) {
								Value keyLongOfRow = findValueInColumn(res.getResultSet(), "KeyLong", i).orElse(null);

								// Falls die Prozedur bzw. dessen ResultSet keinen KeyLong als Output hatte, greifen wir auf den KeyLong des Haupt-ResultsSets
								// zurück.
								if (keyLongOfRow == null) {
									keyLongOfRow = findValueInColumn(xsqlResults.get(0).getResultSet(), "KeyLong", 0).orElse(null);
								}
								Row innerRow = new Row();
								innerRow.addValue(keyLongOfRow);
								checkArguments.add(innerRow);
								innerTable.setRows(checkArguments);
							}
						}
						// Es werden XTables verwendet, da die Prozeduren von den Ergebnissen anderer Prozeduren abhängen und so die bereits vorhandenen
						// Methoden wiederverwendet werden können.
						followUpCheck.setTable(innerTable);
						checksXtables.add(followUpCheck);
					}

				}
			}
			processXProcedures(checksXtables, xsqlResults, sb, connection, inputTablesWithResults);
		} catch (Exception e) {
			throw new RuntimeException("Error while trying to find follow up procedures.", e);
		}
	}
}

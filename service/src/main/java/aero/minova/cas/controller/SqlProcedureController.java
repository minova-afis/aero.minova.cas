package aero.minova.cas.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.OutputType;
import aero.minova.cas.api.domain.ProcedureException;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.service.ProcedureService;
import aero.minova.cas.service.QueueService;
import aero.minova.cas.service.SecurityService;
import aero.minova.cas.sql.ExecuteStrategy;
import aero.minova.cas.sql.SystemDatabase;
import lombok.Setter;

@RestController
public class SqlProcedureController {

	@Autowired
	CustomLogger customLogger;

	@Autowired
	ProcedureService procedureService;

	@Autowired
	public SecurityService securityService;

	@Autowired
	public SystemDatabase database;

	@Setter
	QueueService queueService;

	final Object extensionSynchronizer = new Object();

	/**
	 * Das sind Registrierungen, die ausgeführt werden, wenn eine Prozedur mit den Namen der Registrierung ausgeführt werden soll.
	 */
	private final Map<String, Function<Table, ResponseEntity>> extensions = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	/**
	 * Wird nur verwendet, falls die Tabelle "xvcasUserSecurity" nicht vorhanden ist. In diesem Fall kann man annehmen, das die Datenbank nicht aufgesetzt ist.
	 */
	private final Map<String, Function<Table, Boolean>> extensionBootstrapChecks = new HashMap<>();

	/**
	 * Hiermit lassen sich Erweiterungen registrieren, die ausgeführt werden, wenn eine Prozedur mit der Namen der Registrierung ausgeführt werden soll.
	 *
	 * @param name
	 *            Name der Erweiterung
	 * @param ext
	 *            Erweiterung
	 */
	public void registerExtension(String name, Function<Table, ResponseEntity> ext) {
		if (extensions.containsKey(name)) {
			String errorMessage = "Cannot register two extensions with the same name: " + name;
			customLogger.logSetup(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
		extensions.put(name, ext);
	}

	/**
	 * Registriert eine alternative Privilegien-Prüfung für Erweiterungen. Diese wird nur verwendet, wenn {@link #arePrivilegeStoresSetup} gilt.
	 *
	 * @param name
	 *            Name der Erweiterung
	 * @param extCheck
	 *            Alternative Privilegien-Prüfung
	 */
	public void registerExtensionBootstrapCheck(String name, Function<Table, Boolean> extCheck) {
		if (extensionBootstrapChecks.containsKey(name)) {
			throw new IllegalArgumentException(name);
		}
		extensionBootstrapChecks.put(name, extCheck);
	}

	/**
	 * Hinterlegt bei der Installation der Extensions die Rechte in der xtcasUserPrivileges-Tabelle, ordnet diese allerdings noch keinem Nutzer zu. Außerdem
	 * werden die Extensions in die tVersion10-Tabelle eingetragen.
	 */
	public void setupExtensions() {
		Table extensionSetupTable = new Table();
		extensionSetupTable.setName("xpcasSetupInsertUserPrivilege");
		extensionSetupTable.addColumn(new Column("KeyLong", DataType.INTEGER, OutputType.OUTPUT));
		extensionSetupTable.addColumn(new Column("KeyText", DataType.STRING));
		extensionSetupTable.addColumn(new Column("Description", DataType.STRING));

		for (String extensionName : extensions.keySet()) {
			Row extensionSetupRows = new Row();
			extensionSetupRows.addValue(null);
			extensionSetupRows.addValue(new Value(extensionName, null));
			extensionSetupRows.addValue(null);

			extensionSetupTable.addRow(extensionSetupRows);
		}
		try {
			database.getConnection().createStatement().execute("set ANSI_WARNINGS off");
			procedureService.unsecurelyProcessProcedure(extensionSetupTable, true);
			database.getConnection().createStatement().execute("set ANSI_WARNINGS on");
		} catch (Exception e) {
			customLogger.logError("Error while trying to setup extension privileges!", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Erstellt eine Admin-Rolle erstellt, welcher alle Rechte zugewiesen werden. Wird nach dem erfolgreichen Setup ausgeführt.
	 */
	public void setupPrivileges() {
		try {
			Table adminPrivilegeTable = new Table();
			adminPrivilegeTable.setName("xpcasInsertAllPrivilegesToUserGroup");
			adminPrivilegeTable.addColumn(new Column("UserGroup", DataType.STRING));
			adminPrivilegeTable.addColumn(new Column("SecurityToken", DataType.STRING));

			// Eine Gruppe mit dem Namen 'admin' und dem SecurityToken 'admin' anlegen.
			Row adminSetupRow = new Row();
			adminSetupRow.addValue(new Value("admin", null));
			adminSetupRow.addValue(new Value("admin", null));

			adminPrivilegeTable.addRow(adminSetupRow);
			database.getConnection().createStatement().execute("set ANSI_WARNINGS off");
			procedureService.unsecurelyProcessProcedure(adminPrivilegeTable, true);
			database.getConnection().createStatement().execute("set ANSI_WARNINGS on");
		} catch (Exception e) {
			customLogger.logError("Error while trying to setup privileges for admin!", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Führt eine CAS-Erweiterung falls vorhanden oder eine SQL-Prozedur im anderen Fall aus. Falls {@link #arePrivilegeStoresSetup} nicht gilt und es für die
	 * Eingabe eine passende Prozedur gibt, wird geprüft, ob es für die Erweiterung eine passende alternative-Rechteprüfung gibt. Dieser Mechanismus wird
	 * verwendet, um das Initialisieren der Datenbank über das CAS zu triggern.
	 *
	 * @param inputTable
	 *            Name der Prozedur und Aufruf Parameter
	 * @return Ergebnis des Prozeduren-Aufrufs
	 * @throws Exception
	 *             Fehler bei der Ausführung
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "data/procedure")
	public ResponseEntity executeProcedure(@RequestBody Table inputTable) throws Exception {
		if (inputTable.getName().equals("setup")) {
			database.getConnection().createStatement().execute("set ANSI_WARNINGS off");
		}
		customLogger.logUserRequest("data/procedure: ", inputTable);
		try {
			final List<Row> privilegeRequest = checkForPrivilegeAndBootstrapExtension(inputTable);

			Optional<ResponseEntity> extensionResult = checkForExtension(inputTable);

			if (extensionResult.isPresent()) {
				return extensionResult.get();
			}

			ResponseEntity result = new ResponseEntity(processSqlProcedureRequest(inputTable, privilegeRequest), HttpStatus.ACCEPTED);
			queueService.accept(inputTable, result);
			if (inputTable.getName().equals("setup")) {
				database.getConnection().createStatement().execute("set ANSI_WARNINGS on");
			}
			return result;
		} catch (Throwable e) {
			customLogger.logError("Error while trying to execute procedure: " + inputTable.getName(), e);

			// Jede Exception, die irgendwo im Code geworfen wird, sollte am Ende als ProcedureException raus kommen.
			throw new ProcedureException(e);
		}
	}

	/**
	 * Überprüft, ob es für den Namen der übergebenen Table einen passenden Eintrag in den Extensions gibt und gibt das Ergebnis der ausgeführten Extension als
	 * Optional<ResponseEntity> zurück.
	 *
	 * @param inputTable
	 *            Eine Table. Muss einen Namen haben.
	 * @return Das Ergebnis der Extension als Optional mit der übergebenen Table als Input.
	 */
	Optional<ResponseEntity> checkForExtension(Table inputTable) {
		ResponseEntity extResult = null;
		synchronized (extensionSynchronizer) {
			if (extensions.containsKey(inputTable.getName())) {
				final var extension = extensions.get(inputTable.getName());
				extResult = extension.apply(inputTable);
				queueService.accept(inputTable, extResult);
				if (extResult == null) {
					customLogger.logError(
							"Extension " + extension
									+ " returned null. This is not allowed to happen, as otherwise the SQL method is executed after the extension as well.",
							new NullPointerException());
				}
				return Optional.of(extResult);
			} else {
				return Optional.empty();
			}
		}
	}

	/**
	 * Überprüft, ob es für den derzeitigen Nutzer und die übergebene Table Privilegien gibt und falls nicht, ob eine Extension vorhanden ist, welche keine
	 * Privilegien benötigt.
	 * 
	 * @param inputTable
	 *            Die Table, welche auf Privilegien geprüft werden muss.
	 * @return Eine Liste von Rows mit Privilegname, UserGroup-Name und RowLevelSecurity-Bit.
	 * @throws Exception
	 *             Falls keine Datenbankverbindung aufgebaut werden kann.
	 * @throws ProcedureException
	 *             Falls der Nutzer kein Recht hat, die Aktion durchzuführen.
	 */
	private List<Row> checkForPrivilegeAndBootstrapExtension(Table inputTable) throws Exception, ProcedureException {
		final List<Row> privilegeRequest = new ArrayList<>();
		if (securityService.arePrivilegeStoresSetup()) {
			privilegeRequest.addAll(securityService.getPrivilegePermissions(inputTable.getName()));
			if (privilegeRequest.isEmpty()) {
				throw new ProcedureException("msg.PrivilegeError %" + inputTable.getName());
			}
		} else {
			if (extensionBootstrapChecks.containsKey(inputTable.getName())) {
				if (!extensionBootstrapChecks.get(inputTable.getName()).apply(inputTable)) {
					throw new ProcedureException("msg.PrivilegeError %" + inputTable.getName());
				}
			} else {
				throw new ProcedureException("msg.PrivilegeError %" + inputTable.getName());
			}
		}
		return privilegeRequest;
	}

	/**
	 * Diese Methode ist nicht geschützt. Aufrufer sind für die Sicherheit verantwortlich. Führt eine Prozedur mit den übergebenen Parametern aus. Falls die
	 * Prozedur Output-Parameter zurückgibt, werden diese auch im SqlProcedureResult zurückgegeben.
	 *
	 * @param inputTable
	 *            Ausführungs-Parameter im Form einer Table
	 * @return SqlProcedureResult der Ausführung
	 * @throws Exception
	 *             Fehler beim Ausführen der Prozedur.
	 */
	@Deprecated
	public SqlProcedureResult unsecurelyProcessProcedure(Table inputTable) throws Exception {
		return procedureService.unsecurelyProcessProcedure(inputTable);
	}

	/**
	 * Diese Methode ist nicht geschützt. Aufrufer sind für die Sicherheit verantwortlich. Führt eine Prozedur mit den übergebenen Parametern aus. Falls die
	 * Prozedur Output-Parameter zurückgibt, werden diese auch im SqlProcedureResult zurückgegeben. Prüft auch, ob eine Extension statt einer Prozedur
	 * aufgerufen werden muss.
	 *
	 * @param inputTable
	 *            Ausführungs-Parameter im Form einer Table
	 * @return Optional einer SqlProcedureResult der Ausführung
	 * @throws Exception
	 *             Fehler beim Ausführen der Prozedur.
	 */
	public ResponseEntity<?> unsecurelyExecuteProcedure(Table inputTable) throws Exception {
		Optional<ResponseEntity> extensionResult = checkForExtension(inputTable);

		if (extensionResult.isPresent()) {
			return extensionResult.orElse(null);
		}
		// Hiermit wird der unsichere Zugriff ermöglicht.
		Row requestingAuthority = new Row();
		/*
		 * Diese drei Values werden benötigt, um unsicher die Sicherheitsabfrage ohne User durchführen zu können. Das wichtigste hierbei ist, dass der dritte
		 * Value auf Valse steht. Das Format der Row ist normalerweise (PrivilegName, UserSecurityToke, RowLevelSecurity-Bit)
		 */
		requestingAuthority.addValue(new Value(false, "1"));
		requestingAuthority.addValue(new Value(false, "2"));
		requestingAuthority.addValue(new Value(false, "3"));

		List<Row> authority = new ArrayList<>();
		authority.add(requestingAuthority);
		return new ResponseEntity(procedureService.processSqlProcedureRequest(inputTable, authority), HttpStatus.ACCEPTED);
	}

	/**
	 * Speichert im SQl-Session-Context unter `casUser` den Nutzer, der die Abfrage tätigt.
	 *
	 * @param connection
	 *            Das ist die Session.
	 * @throws SQLException
	 *             Fehler beim setzen des Kontextes für die connection.
	 */
	@Deprecated
	private void setUserContextFor(Connection connection) throws SQLException {
		procedureService.setUserContextFor(connection);
	}

	/**
	 * Führt eine Prozedur mit den übergebenen Parametern aus. Falls die Prozedur Output-Parameter zurückgibt, werden diese auch im SqlProcedureResult
	 * zurückgegeben.
	 *
	 * @param inputTable
	 *            Ausführungs-Parameter im Form einer Table
	 * @param privilegeRequest
	 *            eine Liste an Rows im Format (PrivilegName,UserSecurityToken,RowLevelSecurity-Bit)
	 * @return Resultat SqlProcedureResult der Ausführung
	 * @throws Exception
	 *             Fehler bei der Ausführung
	 */
	@Deprecated
	public SqlProcedureResult processSqlProcedureRequest(Table inputTable, List<Row> privilegeRequest) throws Exception {
		return procedureService.processSqlProcedureRequest(inputTable, privilegeRequest, false);
	}

	/**
	 * Führt eine SQL-Prozedur aus. Hier gibt es keinen Rollback oder Commit. Diese müssen selbst durchgeführt werden. Diese Methode ist public, weil diese von
	 * Erweiterungen genutzt werden, um bei Fehlern in komplexeren Prozessen alle Änderungen in der Datenbank rückgängig zu machen.
	 *
	 * @param inputTable
	 *            Die Table mit allen Werten zum Verarbeiten der Prozedur.
	 * @param privilegeRequest
	 *            Eine Liste an Rows im Format (PrivilegName,UserSecurityToken,RowLevelSecurity-Bit).
	 * @param connection
	 *            Die Connection zu der Datenbank, welche die Prozedur ausführen soll.
	 * @param result
	 *            Das SQL-Result, welches innerhalb der Methode verändert und dann returned wird.
	 * @param sb
	 *            Ein StringBuffer, welcher im Fehlerfall die Fehlermeldung bis zum endgültigen Throw als String aufnimmt.
	 * @return result Das veränderte SqlProcedureResult.
	 * @throws SQLException
	 *             Falls ein Fehler beim Ausführen der Prozedur auftritt.
	 * @throws ProcedureException
	 *             Falls generell ein Fehler geworfen wird, zum Beispiel beim Konvertieren der Typen.
	 */
	@Deprecated
	public SqlProcedureResult calculateSqlProcedureResult(Table inputTable, List<Row> privilegeRequest, final java.sql.Connection connection,
			SqlProcedureResult result, StringBuffer sb) throws SQLException, ProcedureException {
		return procedureService.calculateSqlProcedureResult(inputTable, privilegeRequest, connection, result, sb);
	}

	@Deprecated
	private void fillCallableSqlProcedureStatement(CallableStatement preparedStatement, Table inputTable, int parameterOffset, StringBuffer sb, int row) {
		procedureService.fillCallableSqlProcedureStatement(preparedStatement, inputTable, parameterOffset, sb, row);
	}

	@Deprecated
	String prepareProcedureString(Table params) {
		return prepareProcedureString(params, ExecuteStrategy.STANDARD);
	}

	/**
	 * Bereitet einen Prozedur-String vor
	 *
	 * @param params
	 *            SQL-Call-Parameter
	 * @param strategy
	 *            SQL-Execution-Strategie
	 * @return SQL-Code
	 * @throws IllegalArgumentException
	 *             Fehler, wenn die Daten in params nicht richtig sind.
	 */
	@Deprecated
	String prepareProcedureString(Table params, Set<ExecuteStrategy> strategy) throws IllegalArgumentException {
		return procedureService.prepareProcedureString(params, strategy);
	}
}

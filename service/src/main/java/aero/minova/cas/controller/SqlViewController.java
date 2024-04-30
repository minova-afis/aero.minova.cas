package aero.minova.cas.controller;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.OutputType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.TableException;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.service.ProcedureService;
import aero.minova.cas.service.SecurityService;
import aero.minova.cas.service.ViewService;

@RestController
public class SqlViewController {

	@Autowired
	private ViewService viewService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	CustomLogger customLogger;

	@Autowired
	ProcedureService procedureService;

	final Object extensionSynchronizer = new Object();

	/**
	 * Das sind Registrierungen, die ausgeführt werden, wenn eine View mit den Namen der Registrierung ausgeführt werden soll.
	 */
	private final Map<String, Function<Table, Table>> extensions = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	/**
	 * Hiermit lassen sich Erweiterungen für Views registrieren, die ausgeführt werden, wenn eine View mit der Namen der Registrierung ausgeführt werden soll.
	 *
	 * @param name
	 *            Name der Erweiterung
	 * @param ext
	 *            Erweiterung
	 */
	public void registerExtension(String name, Function<Table, Table> ext) {
		if (extensions.containsKey(name)) {
			String errorMessage = "Cannot register two extensions with the same name: " + name;
			customLogger.logSetup(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}
		extensions.put(name, ext);
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
			procedureService.unsecurelyProcessProcedure(extensionSetupTable, true);
		} catch (Exception e) {
			customLogger.logError("Error while trying to setup extension privileges!", e);
			throw new RuntimeException(e);
		}
	}

	@Deprecated
	@GetMapping(value = "data/index", produces = "application/json")
	public Table getIndexViewGet(@RequestBody Table inputTable) throws Exception {
		customLogger.logUserRequest(
				": WARNING! The Get-Mapping of data/index will be removed in 6 months on 1st of September! Please use the Post-Mapping instead.");
		return getIndexView(inputTable);
	}

	@PostMapping(value = "data/index", produces = "application/json")
	public Table getIndexView(@RequestBody Table inputTable) throws Exception {
		customLogger.logUserRequest(": data/view: ", inputTable);
		return getIndexView(inputTable, true);
	}

	public Table getIndexView(@RequestBody Table inputTable, boolean checkForExtension) throws Exception {
		// Die Privilegien-Abfrage muss vor allem Anderen passieren. Falls das Privileg nicht vorhanden ist MUSS eine TableException geworfen werden.
		List<Row> authoritiesForThisTable = securityService.getPrivilegePermissions(inputTable.getName());
		if (authoritiesForThisTable.isEmpty()) {
			throw new TableException(new RuntimeException("msg.PrivilegeError %" + inputTable.getName()));
		}
		if (checkForExtension && extensions.containsKey(inputTable.getName())) {
			synchronized (extensionSynchronizer) {
				return extensions.get(inputTable.getName()).apply(inputTable);
			}
		}
		return viewService.executeView(inputTable, authoritiesForThisTable);
	}

	/**
	 * das Prepared Statement wird mit den dafür vorgesehenen Parametern befüllt
	 *
	 * @param inputTable
	 *            die Table, welche vom getIndexView aufgerufen wurde
	 * @param preparedStatement
	 *            das Prepared Statement, welches nur noch befüllt werden muss
	 * @return das befüllte, ausführbare Prepared Statement
	 */
	@Deprecated
	public PreparedStatement fillPreparedViewString(Table inputTable, CallableStatement preparedStatement, String query, StringBuilder sb) {
		return viewService.fillPreparedViewString(inputTable, preparedStatement, query, sb);
	}

	@Deprecated
	public Table convertSqlResultToTable(Table inputTable, ResultSet sqlSet) {
		return viewService.convertSqlResultToTable(inputTable, sqlSet);
	}

	@Deprecated
	String prepareViewString(Table params, boolean autoLike, int maxRows, List<Row> authorities) throws IllegalArgumentException {
		return prepareViewString(params, autoLike, maxRows, false, authorities);
	}

	/**
	 * Diese Methode stammt ursprünglich aus "ch.minova.ncore.data.sql.SQLTools#prepareViewString". Bereitet einen View-String vor und berücksichtigt eine evtl.
	 * angegebene Maximalanzahl Ergebnisse
	 *
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @param maxRows
	 *            maximale Anzahl Ergebnisse (Zeilen), die die Abfrage liefern soll, 0 für unbegrenzt
	 * @param count
	 *            Gibt an ob nur die Anzahl der Ergebniss (Zeilen), gezählt werden sollen.
	 * @return Präparierter View-String, der ausgeführt werden kann
	 * @throws IllegalArgumentException
	 * @author wild
	 */
	@Deprecated
	public String prepareViewString(Table params, boolean autoLike, int maxRows, boolean count, List<Row> authorities) throws IllegalArgumentException {
		return viewService.prepareViewString(params, autoLike, maxRows, count, authorities);
	}

	/*
	 * Pagination nach der Seek-Methode; bessere Performance als Offset bei großen Datensätzen. Wird NICHT für den "normalen" Index-Aufruf verwendet, da immer
	 * davon ausgegangen wird, dass ein KeyLong in der View/Table vorhanden ist.
	 */
	@Deprecated
	public String pagingWithSeek(Table params, boolean autoLike, int maxRows, boolean count, int page, List<Row> authorities) {
		return viewService.pagingWithSeek(params, autoLike, maxRows, count, page, authorities);
	}

	/**
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @return die Where-Klausel für die angegebenen Parameter
	 * @author wild
	 */
	@Deprecated
	protected String prepareWhereClause(Table params, boolean autoLike) {
		return viewService.prepareWhereClause(params, autoLike);
	}
}
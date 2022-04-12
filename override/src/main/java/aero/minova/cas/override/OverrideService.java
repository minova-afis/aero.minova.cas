package aero.minova.cas.override;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.OutputType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.controller.SqlProcedureController;
import aero.minova.cas.service.SecurityService;

public class OverrideService {

	@Autowired
	SqlProcedureController spc;

	@Autowired
	CustomLogger logger;

	@Autowired
	SecurityService securityService;

	/**
	 * Enthält Tupel aus Prozeudrennamen und Dienstnamen. Wird eine der enthaltenen Prozeduren ausgeführt, muss der dazugehörige Dienst angetriggert werden.
	 */
	private final Map<String, String> overrides = new HashMap<>();

	/**
	 * Enthält Tupel aus Dienstnamen und Tabellennamen. Wird eine der enthaltenen Tabellen verändert, muss der dazugehörige Dienst angetriggert werden.
	 */
	private final Map<String, String> newsfeeds = new HashMap<>();

	@PostConstruct
	private void setup() {
		spc.registerExtension("registerService", inputTable -> {
			try {
				int keyLong = registerService(inputTable);
				return new ResponseEntity(keyLong, HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("unregisterService", inputTable -> {
			try {
				int keyLong = registerService(inputTable);
				return new ResponseEntity(keyLong, HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("registerProcedureNewsfeed", inputTable -> {
			try {
				return new ResponseEntity(HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * Registriert einen Dienst beim CAS und schreibt die dazugehörigen Daten in die xtcasCASServices Tabelle. Die übergebene Table muss dafür in der ersten
	 * Spalte den Namen des Dienstes beinhalten, in der Zweiten die ServiceURL und in der Dritten den Port.
	 * 
	 * @param inputTable
	 *            Die Table, welche in der ersten Spalte den Dienstnamen, in der Zweiten die ServiceURL und in der dritten Column den Port benötigt.
	 * @return Den KeyLong des Dienstes als int.
	 */
	public int registerService(Table inputTable) {
		SqlProcedureResult registerResult = new SqlProcedureResult();

		Table registerSerivceTable = new Table();
		registerSerivceTable.setName("xpcasInsertCASService");
		registerSerivceTable.addColumn(new Column("KeyLong", DataType.INTEGER, OutputType.OUTPUT));
		registerSerivceTable.addColumn(new Column("KeyText", DataType.STRING));
		registerSerivceTable.addColumn(new Column("ServiceURL", DataType.STRING));
		registerSerivceTable.addColumn(new Column("Port", DataType.INTEGER));

		Row registerRow = new Row();
		registerRow.addValue(null);
		registerRow.addValue(inputTable.getRows().get(0).getValues().get(0));
		registerRow.addValue(inputTable.getRows().get(0).getValues().get(1));
		registerRow.addValue(inputTable.getRows().get(0).getValues().get(2));

		registerSerivceTable.addRow(registerRow);
		try {
			registerResult = spc.unsecurelyProcessProcedure(registerSerivceTable);
		} catch (Exception e) {
			logger.logError("The service " + inputTable.getRows().get(0).getValues().get(0).getStringValue() + " could not be registered", e);
			throw new RuntimeException(e);
		}

		return registerResult.getOutputParameters().getRows().get(0).getValues().get(0).getIntegerValue();
	}

	/**
	 * Löscht den Eintrag des Dienstes aus der Datenbank und meldet ihn vom CAS ab.
	 * 
	 * @param inputTable
	 *            eine Table, welche in der ersten Spalte den Dienstnamen, in der Zweiten die ServiceURL und in der dritten Column den Port benötigt.
	 */
	public void unregisterService(Table inputTable) {
		Table unregisterSerivceTable = new Table();
		unregisterSerivceTable.setName("xpcasDeleteCASService");
		unregisterSerivceTable.addColumn(new Column("KeyLong", DataType.INTEGER, OutputType.OUTPUT));
		unregisterSerivceTable.addColumn(new Column("KeyText", DataType.STRING));
		unregisterSerivceTable.addColumn(new Column("ServiceURL", DataType.STRING));
		unregisterSerivceTable.addColumn(new Column("Port", DataType.INTEGER));

		Row unregisterRow = new Row();
		unregisterRow.addValue(null);
		unregisterRow.addValue(inputTable.getRows().get(0).getValues().get(0));
		unregisterRow.addValue(inputTable.getRows().get(0).getValues().get(1));
		unregisterRow.addValue(inputTable.getRows().get(0).getValues().get(2));

		unregisterSerivceTable.addRow(unregisterRow);
		try {
			spc.unsecurelyProcessProcedure(unregisterSerivceTable);
		} catch (Exception e) {
			logger.logError("The service " + inputTable.getRows().get(0).getValues().get(0).getStringValue() + " could not be unregistered!", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Wenn das CAS neu gestartet wird, müssen die Overrides wieder aus der Datenbank ausgelesen werden, da die Map sonst leer ist.
	 */
	@PostConstruct
	private void initializeOverrides() {
		try {
			if (securityService.areOverrideStoresSetup()) {
				Table overrideTable = new Table();
				overrideTable.setName("xvcasCASServices");
				overrideTable.addColumn(new Column("KeyText", DataType.STRING));
				overrideTable.addColumn(new Column("TableName", DataType.STRING));
				try {
					Table viewResult = securityService.unsecurelyGetIndexView(overrideTable);

					for (Row row : viewResult.getRows()) {
						registerOverrides(row.getValues().get(0).getStringValue(), row.getValues().get(1).getStringValue());
					}

				} catch (Exception e) {
					logger.logError("Error while trying to initialize overrides!", e);
					throw new RuntimeException(e);
				}
			}
		} catch (Exception e) {
			logger.logError("Overrides could not be initialized!", e);
		}
	}

	/**
	 * @return Eine Map der registrierten Dienste.
	 */
	public Map<String, String> getOverrides() {
		return overrides;
	}

	/**
	 * @return Eine Map der registrierten Prozeduren.
	 */
	public Map<String, String> getNewsfeeds() {
		return newsfeeds;
	}

	/**
	 * Mappt, welche Prozeduren welche Tabellen verändern.
	 * 
	 * @param procedureName
	 *            Der Name der Prozedur.
	 * @param tableName
	 *            Der Name der Tabelle, welche durch die Prozedur verändert werden soll.
	 */
	public void registerOverrides(String procedureName, String tableName) {
		if (!(overrides.containsKey(procedureName) && overrides.containsValue(tableName))) {
			overrides.put(procedureName, tableName);
		}
	}

	/**
	 * Entfernt den Eintrag einer Tabelle zu einer Prozedur aus der Map.
	 * 
	 * @param procedureName
	 *            Die Prozedur, zu welcher der Eintrag entfernt werden soll.
	 * @param tableName
	 *            Der Tabellenname, zu welchem die Prozedur entfernt werden soll.
	 */
	public void unregisterOverrides(String procedureName, String tableName) {
		if ((overrides.containsKey(procedureName) && overrides.containsValue(tableName))) {
			overrides.remove(procedureName, tableName);
		}
	}

	/**
	 * Mappt die Tabellen zu den Servicenamen und registriert somit, welche Services angetriggert werden müssen, wenn Änderungen auf bestimmten Tabellen
	 * stattfinden.
	 * 
	 * @param serviceName
	 *            Der Name des Dienstes
	 * @param tableName
	 *            Der Name der Tabelle, auf welche gehorcht werden soll
	 */
	public void registerNewsfeed(String serviceName, String tableName) {
		if (!(newsfeeds.containsKey(serviceName) && newsfeeds.containsValue(tableName))) {
			newsfeeds.put(serviceName, tableName);
		}
	}

	/**
	 * Entfernt den Eintrag einer Table zu einem Dienst aus der Map.
	 * 
	 * @param serviceName
	 *            Der Dienst, zu welchem der Eintrag entfernt werden soll.
	 * @param tableName
	 *            Der
	 */
	public void unregisterNewsfeed(String serviceName, String tableName) {
		if ((newsfeeds.containsKey(serviceName) && newsfeeds.containsValue(tableName))) {
			newsfeeds.remove(serviceName, tableName);
		}
	}

}

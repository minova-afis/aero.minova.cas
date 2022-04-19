package aero.minova.cas.servicenotifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.SqlProcedureController;
import aero.minova.cas.service.SecurityService;

public class ServiceNotifierService {

	@Autowired
	SqlProcedureController spc;

	@Autowired
	CustomLogger logger;

	@Autowired
	SecurityService securityService;

	/**
	 * Enthält Tupel aus Prozedurenamen und Tabellennamen. Wird eine der enthaltenen Prozeduren ausgeführt, muss der dazugehörige Dienst angetriggert werden.
	 */
	private final Map<String, List<String>> servicenotifier = new HashMap<>();

	/**
	 * Enthält Tupel aus Dienstnamen und Tabellennamen. Wird eine der enthaltenen Tabellen verändert, muss der dazugehörige Dienst angetriggert werden.
	 */
	private final Map<String, List<String>> newsfeeds = new HashMap<>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
				unregisterService(inputTable);
				return new ResponseEntity(HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("registerProcedureNewsfeed", inputTable -> {
			try {
				registerProcedureNewsfeed(inputTable);
				return new ResponseEntity(HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("unregisterProcedureNewsfeed", inputTable -> {
			try {
				unregisterProcedureNewsfeed(inputTable);
				return new ResponseEntity(HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("registerNewsfeedListener", inputTable -> {
			try {
				registerNewsfeedListener(inputTable);
				return new ResponseEntity(HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("unregisterNewsfeedListener", inputTable -> {
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
		unregisterSerivceTable.addColumn(new Column("KeyText", DataType.STRING));
		unregisterSerivceTable.addColumn(new Column("ServiceURL", DataType.STRING));
		unregisterSerivceTable.addColumn(new Column("Port", DataType.INTEGER));

		Row unregisterRow = new Row();
		unregisterRow.addValue(inputTable.getRows().get(0).getValues().get(0));
		unregisterRow.addValue(inputTable.getRows().get(0).getValues().get(1));
		unregisterRow.addValue(inputTable.getRows().get(0).getValues().get(2));

		unregisterSerivceTable.addRow(unregisterRow);

		String serviceName = inputTable.getRows().get(0).getValues().get(0).getStringValue();
		try {
			spc.unsecurelyProcessProcedure(unregisterSerivceTable);
		} catch (Exception e) {
			logger.logError("The service " + serviceName + " could not be unregistered!", e);
			throw new RuntimeException(e);
		}

		// Wenn ein Service abgemeldet wird, müssen auch die Verweise aus den Maps und den Tabellen entfernt werden.
		Table unregisterNewsfeedListenerTable = new Table();
		unregisterNewsfeedListenerTable.addColumn(new Column("CASServiceName", DataType.STRING));
		unregisterNewsfeedListenerTable.addColumn(new Column("TableName", DataType.STRING));

		// Alle Tabellennamen aus der newsfeed-Map holen und danach die Einträge dazu in den Tabellen löschen.
		Row unregisterNewsfeedRow = new Row();
		for (String entry : newsfeeds.get(serviceName)) {
			unregisterNewsfeedRow.addValue(new Value(serviceName, null));
			unregisterNewsfeedRow.addValue(new Value(entry, null));
		}

		// Den Dienst erst aus den Tabellen und zum Schluss aus der lokalen Map löschen.
		unregisterNewsfeedListener(unregisterNewsfeedListenerTable);
		newsfeeds.remove(serviceName);
	}

	/**
	 * Registriert auf welcher Tabelle eine Prozedur ein Update ausführt.
	 * 
	 * @param inputTable
	 *            Eine Table, welche den Prozedurnamen und den Tabellennamen enthält.
	 */
	public void registerProcedureNewsfeed(Table inputTable) {
		Table registerProcedureNewsfeedTable = new Table();
		registerProcedureNewsfeedTable.setName("xpcasInsertProcedureNewsfeed");
		registerProcedureNewsfeedTable.addColumn(new Column("KeyLong", DataType.INTEGER, OutputType.OUTPUT));
		registerProcedureNewsfeedTable.addColumn(new Column("KeyText", DataType.STRING));
		registerProcedureNewsfeedTable.addColumn(new Column("TableName", DataType.STRING));

		for (int i = 0; i < inputTable.getRows().size(); i++) {

			Row registerRow = new Row();
			registerRow.addValue(null);
			registerRow.addValue(inputTable.getRows().get(i).getValues().get(0));
			registerRow.addValue(inputTable.getRows().get(i).getValues().get(1));

			registerProcedureNewsfeedTable.addRow(registerRow);
			try {
				spc.unsecurelyProcessProcedure(registerProcedureNewsfeedTable);
			} catch (Exception e) {
				logger.logError("The procedure " + inputTable.getRows().get(i).getValues().get(0).getStringValue() + " could not be registered for table "
						+ inputTable.getRows().get(i).getValues().get(1).getStringValue(), e);
				throw new RuntimeException(e);
			}

			registerServicenotifier(inputTable.getRows().get(i).getValues().get(0).getStringValue(),
					inputTable.getRows().get(i).getValues().get(1).getStringValue());
		}
	}

	/**
	 * Löscht einen Eintrag aus der xtcasProcedureNewsfeed anhand der übergebenen Parameter. Dazu werden Prozedurname und Tabellenname benötigt.
	 * 
	 * @param inputTable
	 *            Eine Table mit dem Prozedurnamen und dem Tabellennamen als String-Value in einer Row.
	 */
	public void unregisterProcedureNewsfeed(Table inputTable) {
		Table unregisterProcedureNewsfeedTable = new Table();
		unregisterProcedureNewsfeedTable.setName("xpcasDeleteProcedureNewsfeed");
		unregisterProcedureNewsfeedTable.addColumn(new Column("KeyText", DataType.STRING));
		unregisterProcedureNewsfeedTable.addColumn(new Column("TableName", DataType.STRING));

		for (int i = 0; i < inputTable.getRows().size(); i++) {

			Row unregisterRow = new Row();
			unregisterRow.addValue(inputTable.getRows().get(i).getValues().get(0));
			unregisterRow.addValue(inputTable.getRows().get(i).getValues().get(1));

			unregisterProcedureNewsfeedTable.addRow(unregisterRow);
			try {
				spc.unsecurelyProcessProcedure(unregisterProcedureNewsfeedTable);
			} catch (Exception e) {
				logger.logError("The procedure " + inputTable.getRows().get(i).getValues().get(0).getStringValue() + " could not be unregistered for table "
						+ inputTable.getRows().get(0).getValues().get(1).getStringValue() + "!", e);
				throw new RuntimeException(e);
			}
			unregisterServicenotifier(inputTable.getRows().get(0).getValues().get(0).getStringValue(),
					inputTable.getRows().get(0).getValues().get(1).getStringValue());
		}
	}

	/**
	 * Registriert für welche Tabelle ein Dienst angetriggert werden möchte, wenn ein Update auf dieser durchgeführt wird.
	 * 
	 * @param inputTable
	 *            Eine Table mit den beiden Values CASServiceKey als int und dem Tabellennamen als String.
	 */
	public void registerNewsfeedListener(Table inputTable) {
		Table registerNewsfeedTable = new Table();
		registerNewsfeedTable.setName("xpcasInsertNewsfeedListener");
		registerNewsfeedTable.addColumn(new Column("KeyLong", DataType.INTEGER, OutputType.OUTPUT));
		registerNewsfeedTable.addColumn(new Column("KeyText", DataType.STRING));
		registerNewsfeedTable.addColumn(new Column("TableName", DataType.STRING));

		for (int i = 0; i < inputTable.getRows().size(); i++) {
			Row registerRow = new Row();
			registerRow.addValue(null);
			registerRow.addValue(inputTable.getRows().get(0).getValues().get(0));
			registerRow.addValue(inputTable.getRows().get(0).getValues().get(1));

			registerNewsfeedTable.addRow(registerRow);
			try {
				spc.unsecurelyProcessProcedure(registerNewsfeedTable);
			} catch (Exception e) {
				logger.logError("The newsfeed for service " + inputTable.getRows().get(0).getValues().get(0).getStringValue()
						+ " could not be registered for table " + inputTable.getRows().get(i).getValues().get(1), e);
				throw new RuntimeException(e);
			}
			registerNewsfeed(inputTable.getRows().get(0).getValues().get(0).getStringValue(), inputTable.getRows().get(0).getValues().get(1).getStringValue());
		}
	}

	/**
	 * Löscht einen Eintrag aus der xtcasNewsfeedListener anhand der übergebenen Parameter. Dazu werden CASServicename und Tabellenname benötigt.
	 * 
	 * @param inputTable
	 *            Eine Table mit dem CASServiceNamen und dem Tabellennamen als String-Value in einer Row.
	 */
	public void unregisterNewsfeedListener(Table inputTable) {
		Table unregisterNewsfeedTable = new Table();
		unregisterNewsfeedTable.setName("xpcasDeleteNewsfeedListener");
		unregisterNewsfeedTable.addColumn(new Column("KeyText", DataType.STRING));
		unregisterNewsfeedTable.addColumn(new Column("TableName", DataType.STRING));

		for (int i = 0; i < inputTable.getRows().size(); i++) {

			Row unregisterRow = new Row();
			unregisterRow.addValue(inputTable.getRows().get(i).getValues().get(0));
			unregisterRow.addValue(inputTable.getRows().get(i).getValues().get(1));

			unregisterNewsfeedTable.addRow(unregisterRow);
			try {
				spc.unsecurelyProcessProcedure(unregisterNewsfeedTable);
			} catch (Exception e) {
				logger.logError("The newsfeed for service " + inputTable.getRows().get(i).getValues().get(0).getStringValue()
						+ " could not be unregistered for table " + inputTable.getRows().get(0).getValues().get(1).getStringValue() + "!", e);
				throw new RuntimeException(e);
			}
			unregisterNewsfeed(inputTable.getRows().get(0).getValues().get(0).getStringValue(),
					inputTable.getRows().get(0).getValues().get(1).getStringValue());
		}
	}

	/**
	 * Wenn das CAS neu gestartet wird, müssen die Servicenotifier wieder aus der Datenbank ausgelesen werden, da die Map sonst leer ist.
	 */
	@PostConstruct
	private void initializeServicenotifiers() {
		try {
			if (securityService.areServiceNotifiersStoresSetup()) {
				Table servicenotifierTable = findViewEntry(null, null, null, null, null);
				for (Row row : servicenotifierTable.getRows()) {
					registerServicenotifier(row.getValues().get(4).getStringValue(), row.getValues().get(5).getStringValue());
				}
			}
		} catch (Exception e) {
			logger.logError("Error while trying to initialize servicenotifiers!", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Wenn das CAS neu gestartet wird, muss die Newsfeeds-Map wieder aus der Datenbank ausgelesen werden, da die Map sonst leer ist.
	 */
	@PostConstruct
	private void initializeNewsfeeds() {
		try {
			if (securityService.areServiceNotifiersStoresSetup()) {
				Table newsfeedsTable = findViewEntry(null, null, null, null, null);
				for (Row row : newsfeedsTable.getRows()) {
					registerNewsfeed(row.getValues().get(3).getStringValue(), row.getValues().get(5).getStringValue());
				}
			}

		} catch (Exception e) {
			logger.logError("Error while trying to initialize newsfeed!", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sucht in der xvCASServices die Einträge anhand der übergebenen Values heraus. Die Values müssen hierfür String-Values sein. Wichtig hierbei ist, dass
	 * nicht jeder Value übergeben werden muss. Falls man die gesamte View haben möchte, kann man auch einfach 'null' in allen drei Übergabeparametern
	 * übergeben.
	 * 
	 * @param casServiceName
	 *            Der Name des Dienstes als String-Value.
	 * @param procedureName
	 *            Der Name der Prozedur als String-Value.
	 * @param tableName
	 *            Der Name der Table als String-Value.
	 * @param serviceURL
	 *            Die URL eines Dienstes.
	 * @param port
	 *            Der Port eines Dienstes.
	 * @return Eine Table mit den jeweiligen gefilterten Einträgen. Die Reihenfolge der Values ist folgende: CASServiceKey, NewsfeedListenerKey,
	 *         ProcedureNewsfeedKey, CASServiceName, ProcedureName, TableName, ServiceURL, Port
	 */
	public Table findViewEntry(Value casServiceName, Value procedureName, Value tableName, Value serviceURL, Value port) {
		Table viewResult = new Table();

		Table viewTable = new Table();
		viewTable.setName("xvcasCASServices");
		viewTable.addColumn(new Column("CASServiceKey", DataType.STRING));
		viewTable.addColumn(new Column("NewsfeedListenerKey", DataType.STRING));
		viewTable.addColumn(new Column("ProcedureNewsfeedKey", DataType.STRING));
		viewTable.addColumn(new Column("CASServiceName", DataType.STRING));
		viewTable.addColumn(new Column("ProcedureName", DataType.STRING));
		viewTable.addColumn(new Column("TableName", DataType.STRING));
		viewTable.addColumn(new Column("ServiceURL", DataType.STRING));
		viewTable.addColumn(new Column("PORT", DataType.INTEGER));

		Row viewRow = new Row();
		viewRow.addValue(null);
		viewRow.addValue(null);
		viewRow.addValue(null);
		viewRow.addValue(casServiceName);
		viewRow.addValue(procedureName);
		viewRow.addValue(tableName);
		viewRow.addValue(serviceURL);
		viewRow.addValue(port);
		try {
			viewResult = securityService.unsecurelyGetIndexView(viewTable);
		} catch (Exception e) {
			logger.logError("Error while trying to access view xvcasCASServices!", e);
			throw new RuntimeException(e);
		}
		return viewResult;
	}

	/**
	 * @return Eine Map der registrierten Dienste mit den Tabellen, auf welche sie horchen.
	 */
	public Map<String, List<String>> getServicenotifier() {
		return servicenotifier;
	}

	/**
	 * @return Eine Map der registrierten Prozeduren mit den Tabellen, welche sie verändern/updaten.
	 */
	public Map<String, List<String>> getNewsfeeds() {
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
	public void registerServicenotifier(String procedureName, String tableName) {
		if (!servicenotifier.containsKey(procedureName)) {
			List<String> tables = new ArrayList<>();
			tables.add(tableName);
			servicenotifier.put(procedureName, tables);
		} else if (servicenotifier.containsKey(procedureName) && !servicenotifier.get(procedureName).contains(tableName)) {
			servicenotifier.get(procedureName).add(tableName);
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
	public void unregisterServicenotifier(String procedureName, String tableName) {
		if ((servicenotifier.containsKey(procedureName) && servicenotifier.get(procedureName).contains(tableName))) {
			servicenotifier.get(procedureName).remove(tableName);
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
		if (!newsfeeds.containsKey(serviceName)) {
			List<String> tables = new ArrayList<>();
			tables.add(tableName);
			newsfeeds.put(serviceName, tables);
		} else if (newsfeeds.containsKey(serviceName) && !newsfeeds.get(serviceName).contains(tableName)) {
			newsfeeds.get(serviceName).add(tableName);
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
		if ((newsfeeds.containsKey(serviceName) && newsfeeds.get(serviceName).contains(tableName))) {
			newsfeeds.get(serviceName).remove(tableName);
		}
	}

}
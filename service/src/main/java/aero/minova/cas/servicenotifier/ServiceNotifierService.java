package aero.minova.cas.servicenotifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.SqlProcedureController;
import aero.minova.cas.service.SecurityService;
import aero.minova.cas.service.model.CASServices;
import aero.minova.cas.service.model.NewsfeedListener;
import aero.minova.cas.service.model.ProcedureNewsfeed;
import aero.minova.cas.service.model.ServiceMessageReceiverLoginType;
import aero.minova.cas.service.repository.CASServicesRepository;
import aero.minova.cas.service.repository.NewsfeedListenerRepository;
import aero.minova.cas.service.repository.ProcedureNewsfeedRepository;
import aero.minova.cas.service.repository.ServiceMessageReceiverLoginTypeRepository;
import jakarta.annotation.PostConstruct;

@Service
public class ServiceNotifierService {

	@Autowired
	SqlProcedureController spc;

	@Autowired
	CustomLogger logger;

	@Autowired
	SecurityService securityService;

	@Autowired
	ServiceMessageReceiverLoginTypeRepository serviceMessageReceiverLoginTypeRepo;

	@Autowired
	CASServicesRepository casServiceRepo;

	@Autowired
	NewsfeedListenerRepository newsfeedListenerRepo;

	@Autowired
	ProcedureNewsfeedRepository procedureNewsfeedRepo;

	/**
	 * Enthält Tupel aus Prozedurenamen und Tabellennamen. Wird eine der enthaltenen Prozeduren ausgeführt, muss der dazugehörige Dienst angetriggert werden.
	 */
	protected Map<String, List<String>> servicenotifier = new HashMap<>();

	/**
	 * Enthält Tupel aus Dienstnamen und Topics. Wird eine der enthaltenen Topics verändert, muss der dazugehörige Dienst angetriggert werden.
	 */
	protected Map<String, List<String>> newsfeeds = new HashMap<>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostConstruct
	private void setup() {

		try {
			findOrCreateServiceMessageReceiverLoginType("None");
			findOrCreateServiceMessageReceiverLoginType("BasicAuth");
			findOrCreateServiceMessageReceiverLoginType("OAuth2");
		} catch (Exception e) {
			logger.logError("Standard values could not be inserted in table xtcasServiceMessageReceiverLoginType.", e);
		}

		spc.registerExtension("xpcasRegisterService", inputTable -> {
			try {
				int keyLong = registerService(inputTable);
				return new ResponseEntity(keyLong, HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("xpcasUnregisterService", inputTable -> {
			try {
				unregisterService(inputTable);
				return new ResponseEntity(HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("xpcasRegisterProcedureNewsfeed", inputTable -> {
			try {
				registerProcedureNewsfeed(inputTable);
				return new ResponseEntity(HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("xpcasUnregisterProcedureNewsfeed", inputTable -> {
			try {
				unregisterProcedureNewsfeed(inputTable);
				return new ResponseEntity(HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("xpcasUnregisterProcedureNewsfeedCompletely", inputTable -> {
			try {
				unregisterProcedureNewsfeedCompletely(inputTable);
				return new ResponseEntity(HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("xpcasRegisterNewsfeedListener", inputTable -> {
			try {
				registerNewsfeedListener(inputTable);
				return new ResponseEntity(HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		spc.registerExtension("xpcasUnregisterNewsfeedListener", inputTable -> {
			try {
				unregisterNewsfeedListener(inputTable);
				return new ResponseEntity(HttpStatus.ACCEPTED);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * Findet Einträge im ServiceMessageReceiverLoginTypeRepository oder legt sie an, falls diese nicht existieren.
	 * 
	 * @param loginType
	 *            Der gesuchte Name als String.
	 * @return ein ServiceMessageReceiverLoginType-Objekt.
	 */
	public ServiceMessageReceiverLoginType findOrCreateServiceMessageReceiverLoginType(String loginType) {
		return !serviceMessageReceiverLoginTypeRepo.findByKeyText(loginType).isEmpty() ? serviceMessageReceiverLoginTypeRepo.findByKeyText(loginType).get(0)
				: createNewServiceMessageReceiverLoginType(loginType);
	}

	private ServiceMessageReceiverLoginType createNewServiceMessageReceiverLoginType(String loginType) {
		ServiceMessageReceiverLoginType serviceMessageLoginType = new ServiceMessageReceiverLoginType();
		serviceMessageLoginType.setKeyText(loginType);
		return serviceMessageReceiverLoginTypeRepo.save(serviceMessageLoginType);
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
		try {

			CASServices newService = new CASServices();

			newService.setKeyText(inputTable.getRows().get(0).getValues().get(inputTable.findColumnPosition("KeyText")).getStringValue());
			newService.setServiceUrl(inputTable.getRows().get(0).getValues().get(inputTable.findColumnPosition("ServiceURL")).getStringValue());
			newService.setPort(inputTable.getRows().get(0).getValues().get(inputTable.findColumnPosition("Port")).getIntegerValue());

			int loginTypeKey = inputTable.getRows().get(0).getValues().get(inputTable.findColumnPosition("ServiceMessageReceiverLoginType")) != null
					? inputTable.getRows().get(0).getValues().get(inputTable.findColumnPosition("ServiceMessageReceiverLoginType")).getIntegerValue()
					: 0;
			ServiceMessageReceiverLoginType loginType = serviceMessageReceiverLoginTypeRepo.findByKeyLongAndLastActionGreaterThan(loginTypeKey, 0);
			newService.setReceiverLoginType(loginType);

			newService.setUsername(inputTable.getRows().get(0).getValues().get(inputTable.findColumnPosition("Username")).getStringValue());
			newService.setPassword(inputTable.getRows().get(0).getValues().get(inputTable.findColumnPosition("Password")).getStringValue());
			newService.setClientId(inputTable.getRows().get(0).getValues().get(inputTable.findColumnPosition("ClientID")).getStringValue());
			newService.setClientSecret(inputTable.getRows().get(0).getValues().get(inputTable.findColumnPosition("ClientSecret")).getStringValue());
			newService.setTokenURL(inputTable.getRows().get(0).getValues().get(inputTable.findColumnPosition("TokenURL")).getStringValue());

			casServiceRepo.saveAndFlush(newService);

			return newService.getKeyLong();

		} catch (Exception e) {
			logger.logError("The service " + inputTable.getRows().get(0).getValues().get(0).getStringValue() + " could not be registered.", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Löscht den Eintrag des Dienstes aus der Datenbank und meldet ihn vom CAS ab.
	 * 
	 * @param inputTable
	 *            eine Table, welche den Dienstnamen,die ServiceURL und den Port benötigt.
	 */
	public void unregisterService(Table inputTable) {

		String serviceName = inputTable.getRows().get(0).getValues().get(inputTable.findColumnPosition("KeyText")).getStringValue();
		try {

			// Für die Delete-Prozedur muss der KeyLong rausgefunden werden.
			CASServices toDelete = findServiceEntry(serviceName);

			toDelete.setLastAction(-1);
			casServiceRepo.saveAndFlush(toDelete);

			// Hier wird der Eintrag aus der Datenbank-Tabelle gelöscht.
		} catch (Exception e) {
			logger.logError("The service " + serviceName + " could not be unregistered!", e);
			throw new RuntimeException(e);
		}

		// Wenn ein Service abgemeldet wird, müssen auch die Verweise aus den Maps und den Tabellen entfernt werden.
		Table unregisterNewsfeedListenerTable = new Table();
		unregisterNewsfeedListenerTable.addColumn(new Column("CASServiceName", DataType.STRING));
		unregisterNewsfeedListenerTable.addColumn(new Column("Topic", DataType.STRING));

		// Alle Tabellennamen aus der newsfeed-Map holen und danach die Einträge dazu in den Tabellen löschen.
		Row unregisterNewsfeedRow = new Row();

		if (newsfeeds.keySet().contains(serviceName)) {
			for (String entry : newsfeeds.get(serviceName)) {
				unregisterNewsfeedRow.addValue(new Value(serviceName, null));
				unregisterNewsfeedRow.addValue(new Value(entry, null));
			}
		}

		// Den Dienst erst aus den Tabellen und zum Schluss aus der lokalen Map löschen.
		unregisterNewsfeedListener(unregisterNewsfeedListenerTable);
	}

	/**
	 * Registriert auf welcher Tabelle eine Prozedur ein Update ausführt.
	 * 
	 * @param inputTable
	 *            Eine Table, welche den Prozedurnamen und den Tabellennamen enthält.
	 */
	public void registerProcedureNewsfeed(Table inputTable) {
		try {
			for (Row inputRow : inputTable.getRows()) {

				String keyText = inputRow.getValues().get(inputTable.findColumnPosition("KeyText")).getStringValue();
				String topic = inputRow.getValues().get(inputTable.findColumnPosition("Topic")).getStringValue();

				List<ProcedureNewsfeed> existingNewsfeeds = procedureNewsfeedRepo.findAllByKeyTextAndTopicAndLastActionGreaterThan(keyText, topic, 0);

				// Diese Einträge sind bereits vorhanden. Wir wollen keine doppelten Einträge, also return.
				if (!existingNewsfeeds.isEmpty()) {
					return;
				}
				ProcedureNewsfeed newProcedureNewsfeed = new ProcedureNewsfeed();

				newProcedureNewsfeed.setKeyText(keyText);
				newProcedureNewsfeed.setTopic(topic);

				procedureNewsfeedRepo.saveAndFlush(newProcedureNewsfeed);

			}
		} catch (Exception e) {
			logger.logError("Error while trying to register procedures: ", e);
			throw new RuntimeException(e);
		}

		// Nach erfolgreichem Eintragen in der Datenbank werden die Einträge der Map hinzugefügt.
		for (Row row : inputTable.getRows()) {
			registerServicenotifier(row.getValues().get(0).getStringValue(), row.getValues().get(1).getStringValue());
		}
	}

	/**
	 * Löscht einen Eintrag aus der xtcasProcedureNewsfeed anhand der übergebenen Parameter. Dazu werden Prozedurname und Tabellenname benötigt.
	 * 
	 * @param inputTable
	 *            Eine Table mit dem Prozedurnamen und dem Tabellennamen als String-Value in einer Row.
	 */
	public void unregisterProcedureNewsfeed(Table inputTable) {

		try {
			for (Row inputRow : inputTable.getRows()) {

				// Für die Delete-Prozedur muss der KeyLong rausgefunden werden.
				List<ProcedureNewsfeed> procedureNewsfeedList = findProcedureEntry(inputRow.getValues().get(0), inputRow.getValues().get(1));

				if (procedureNewsfeedList.isEmpty()) {
					throw new RuntimeException("The combination of procedurename and topic could not be found! " + inputRow.getValues().get(0).getStringValue()
							+ " " + inputRow.getValues().get(1).getStringValue());
				}

				for (ProcedureNewsfeed toDelete : procedureNewsfeedList) {
					toDelete.setLastAction(-1);
					// Hier wird der Eintrag aus der Datenbank-Tabelle gelöscht.
					procedureNewsfeedRepo.saveAndFlush(toDelete);
				}
			}
		} catch (Exception e) {
			logger.logError("Error while trying to unregister procedures: ", e);
			throw new RuntimeException(e);
		}

		// Nach erfolgreichem Löschen der Einträge in der Datenbank werden diese auch in der Map gelöscht.
		for (Row row : inputTable.getRows()) {
			unregisterServicenotifier(row.getValues().get(0).getStringValue(), row.getValues().get(1).getStringValue());
		}
	}

	public void unregisterProcedureNewsfeedCompletely(Table inputTable) {
		try {
			for (Row inputRow : inputTable.getRows()) {
				// Für die Delete-Prozedur muss der KeyLong rausgefunden werden.
				List<ProcedureNewsfeed> procedureNewsfeeds = findProcedureEntry(inputRow.getValues().get(0), null);
				for (ProcedureNewsfeed toDelete : procedureNewsfeeds) {
					toDelete.setLastAction(-1);
					// Hier wird der Eintrag aus der Datenbank-Tabelle gelöscht.
					procedureNewsfeedRepo.saveAndFlush(toDelete);
				}
			}
		} catch (Exception e) {
			logger.logError("Error while trying to unregister procedures: ", e);
			throw new RuntimeException(e);
		}

		// Nach erfolgreichem Löschen der Einträge in der Datenbank wird der Key auch in der Map gelöscht.
		for (Row row : inputTable.getRows()) {
			servicenotifier.remove(row.getValues().get(0).getStringValue());
		}
	}

	/**
	 * Registriert für welche Tabelle ein Dienst angetriggert werden möchte, wenn ein Update auf dieser durchgeführt wird.
	 * 
	 * @param inputTable
	 *            Eine Table mit den beiden Values CASServiceKey als int und dem Tabellennamen als String.
	 */
	public void registerNewsfeedListener(Table inputTable) {

		try {
			// CASService finden und in neuen NewsfeedListener einfügen.
			CASServices serviceKey = findServiceEntry(inputTable.getRows().get(0).getValues().get(0).getStringValue());

			for (Row inputRow : inputTable.getRows()) {
				String topic = inputRow.getValues().get(inputTable.findColumnPosition("Topic")).getStringValue();

				List<NewsfeedListener> existingListeners = newsfeedListenerRepo.findAllByCasServiceAndTopicAndLastActionGreaterThan(serviceKey, topic, 0);

				// Diese Einträge sind bereits vorhanden. Wir wollen keine doppelten Einträge, also return.
				if (!existingListeners.isEmpty()) {
					return;
				}

				NewsfeedListener newListener = new NewsfeedListener();
				newListener.setCasService(serviceKey);
				newListener.setTopic(topic);

				newsfeedListenerRepo.saveAndFlush(newListener);
			}
		} catch (Exception e) {
			logger.logError("Error while trying to register a new newsfeed: ", e);
			throw new RuntimeException(e);
		}

		// Nach erfolgreichem Eintragen in der Datenbank werden die Einträge der Map hinzugefügt.
		for (Row row : inputTable.getRows()) {
			registerNewsfeed(row.getValues().get(0).getStringValue(), row.getValues().get(1).getStringValue());
		}
	}

	/**
	 * Löscht einen Eintrag aus der xtcasNewsfeedListener anhand der übergebenen Parameter. Dazu werden CASServicename und Tabellenname benötigt.
	 * 
	 * @param inputTable
	 *            Eine Table mit dem CASServiceNamen und dem Tabellennamen als String-Value in einer Row.
	 */
	public void unregisterNewsfeedListener(Table inputTable) {

		try {
			for (Row inputRow : inputTable.getRows()) {

				// Wir finden erst einmal heraus, welche NewsfeedListener mit dem Service in Verbindung stehen.
				List<NewsfeedListener> newsfeedListeners = findViewEntry(inputRow.getValues().get(0).getStringValue(),
						inputRow.getValues().get(1).getStringValue());

				for (NewsfeedListener toDelete : newsfeedListeners) {
					toDelete.setLastAction(-1);
					// Hier wird der Eintrag aus der Datenbank-Tabelle gelöscht. LastAction wird einfach nur auf -1 gesetzt.
					newsfeedListenerRepo.saveAndFlush(toDelete);
				}
			}
		} catch (Exception e) {
			logger.logError("Error while trying to unregister newsfeed: ", e);
			throw new RuntimeException(e);
		}
		// Nach erfolgreichem Löschen der Einträge in der Datenbank werden diese auch in der Map gelöscht.
		for (Row row : inputTable.getRows()) {
			unregisterNewsfeed(row.getValues().get(0).getStringValue(), row.getValues().get(1).getStringValue());
		}
	}

	/**
	 * Findet den KeyLong zu einem bestimmten CASServiceName heraus. Sucht dafür in der xtcasCASServices-Tabelle. Muss man statt findViewEntry verwenden, wenn
	 * der ServiceKey zum Registrieren benötigt wird.
	 * 
	 * @param casServiceName
	 *            Der ServiceName, zu welchem man den KeyLong finden möchte.
	 * @return Den passenden KeyLong zum casServiceName.
	 */
	private CASServices findServiceEntry(String casServiceName) {
		try {
			// Die ServiceNamen müssen eindeutig sein, deswegen nehmen wir hier einfach den Ersten, den wir finden.
			return casServiceRepo.findByKeyText(casServiceName).get(0);
		} catch (Exception e) {
			logger.logError("Error while trying to find service " + casServiceName + " in xtcasCASServices!", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sucht in der xvCASServices die Einträge anhand der übergebenen Values heraus. Die Values müssen hierfür String-Values sein. Wichtig hierbei ist, dass
	 * nicht jeder Value übergeben werden muss.
	 * 
	 * @param casServiceName
	 *            Der Name des Dienstes als String.
	 * @param procedureName
	 *            Der Name der Prozedur als String.
	 * @param topic
	 *            Der Name der Table als String.
	 * @param serviceURL
	 *            Die URL eines Dienstes.
	 * @param port
	 *            Der Port eines Dienstes.
	 * @return Eine Table mit den jeweiligen gefilterten Einträgen. Die Reihenfolge der Values ist folgende: CASServiceKey, NewsfeedListenerKey,
	 *         ProcedureNewsfeedKey, CASServiceName, ProcedureName, topic, ServiceURL, Port
	 */
	public List<NewsfeedListener> findViewEntry(String casServiceName, String topic) {
		try {

			if (topic.isBlank()) {
				if (casServiceName == null || casServiceName.isBlank()) {
					return newsfeedListenerRepo.findAllByLastActionGreaterThan(0);
				} else {
					CASServices findMe = casServiceRepo.findByKeyText(casServiceName).get(0);
					return newsfeedListenerRepo.findAllByCasService(findMe);
				}
			} else {
				if (casServiceName == null || casServiceName.isBlank()) {
					return newsfeedListenerRepo.findAllByTopicAndLastActionGreaterThan(topic, 0);
				} else {
					CASServices findMe = casServiceRepo.findByKeyText(casServiceName).get(0);
					return newsfeedListenerRepo.findAllByCasServiceAndTopicAndLastActionGreaterThan(findMe, topic, 0);
				}
			}
		} catch (Exception e) {
			logger.logError("Error while trying to access view xtcasCASServices!", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sucht in der xtcasProcedureNewsfeed die Einträge anhand der übergebenen Values heraus. Die Values müssen hierfür String-Values sein. Wichtig hierbei ist,
	 * dass nicht jeder Value übergeben werden muss. Falls man die gesamte Tabelle haben möchte, kann man auch einfach 'null' in allen Übergabeparametern
	 * übergeben. LastAction ist immer >=0.
	 * 
	 * @param procedureName
	 *            Der Name der Prozedur als String-Value.
	 * @param topic
	 *            Der Name der Table als String-Value.
	 * @return Eine Table mit den gültigen KeyLongs zu der übergebenen Kombination.
	 */
	public List<ProcedureNewsfeed> findProcedureEntry(Value procedureName, Value topic) {

		try {
			return procedureNewsfeedRepo.findAllByKeyTextAndTopicAndLastActionGreaterThan(procedureName.getStringValue(), topic.getStringValue(), 0);

		} catch (Exception e) {
			logger.logError("Error while trying to access view xtcasProcedureNewsfeed!", e);
			throw new RuntimeException(e);
		}
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
	 * @param topic
	 *            Der Name der Tabelle, welche durch die Prozedur verändert werden soll.
	 */
	public void registerServicenotifier(String procedureName, String topic) {
		if (!servicenotifier.containsKey(procedureName)) {
			List<String> tables = new ArrayList<>();
			tables.add(topic);
			servicenotifier.put(procedureName, tables);
		} else if (servicenotifier.containsKey(procedureName) && !servicenotifier.get(procedureName).contains(topic)) {
			servicenotifier.get(procedureName).add(topic);
		}
	}

	/**
	 * Entfernt den Eintrag einer Tabelle zu einer Prozedur aus der Map.
	 * 
	 * @param procedureName
	 *            Die Prozedur, zu welcher der Eintrag entfernt werden soll.
	 * @param topic
	 *            Der Tabellenname, zu welchem die Prozedur entfernt werden soll.
	 */
	public void unregisterServicenotifier(String procedureName, String topic) {
		if ((servicenotifier.containsKey(procedureName) && servicenotifier.get(procedureName).contains(topic))) {
			servicenotifier.get(procedureName).remove(topic);
		}
	}

	/**
	 * Mappt die Tabellen zu den Servicenamen und registriert somit, welche Services angetriggert werden müssen, wenn Änderungen auf bestimmten Tabellen
	 * stattfinden.
	 * 
	 * @param serviceName
	 *            Der Name des Dienstes
	 * @param topic
	 *            Der Name der Tabelle, auf welche gehorcht werden soll
	 */
	public void registerNewsfeed(String serviceName, String topic) {
		if (!newsfeeds.containsKey(serviceName)) {
			List<String> tables = new ArrayList<>();
			tables.add(topic);
			newsfeeds.put(serviceName, tables);
		} else if (newsfeeds.containsKey(serviceName) && !newsfeeds.get(serviceName).contains(topic)) {
			newsfeeds.get(serviceName).add(topic);
		}
	}

	/**
	 * Entfernt den Eintrag einer Table zu einem Dienst aus der Map.
	 * 
	 * @param serviceName
	 *            Der Dienst, zu welchem der Eintrag entfernt werden soll.
	 * @param topic
	 *            Der Name des zu löschenden Topics als String.
	 */
	public void unregisterNewsfeed(String serviceName, String topic) {
		if ((newsfeeds.containsKey(serviceName) && newsfeeds.get(serviceName).contains(topic))) {
			newsfeeds.get(serviceName).remove(topic);
		}
	}

}

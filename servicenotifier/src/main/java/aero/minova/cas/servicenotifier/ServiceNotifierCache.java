package aero.minova.cas.servicenotifier;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.service.SecurityService;

// @Component
@Deprecated
public class ServiceNotifierCache {

	@Autowired
	ServiceNotifierService service;

	@Autowired
	CustomLogger logger;

	@Autowired
	SecurityService securityService;

	@org.springframework.beans.factory.annotation.Value("${aero.minova.cas.postConstruct:true}")
	boolean runOnStartUp;

	/**
	 * Wenn das CAS neu gestartet wird, müssen die Servicenotifier wieder aus der Datenbank ausgelesen werden, da die Map sonst leer ist.
	 */
	@PostConstruct
	private void initializeServicenotifiers() {
		if (!runOnStartUp) {
			return;
		}
		try {
			if (areServiceNotifiersStoresSetup()) {
				service.servicenotifier = new HashMap<>();
				Table servicenotifierTable = service.findViewEntry(null, null, null, null, null);
				for (Row row : servicenotifierTable.getRows()) {
					if (row.getValues().get(4) != null && row.getValues().get(5) != null) {
						service.registerServicenotifier(row.getValues().get(4).getStringValue(), row.getValues().get(5).getStringValue());
					}
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
		if (!runOnStartUp) {
			return;
		}
		try {
			if (areServiceNotifiersStoresSetup()) {
				service.newsfeeds = new HashMap<>();
				Table newsfeedsTable = service.findViewEntry(null, null, null, null, null);
				for (Row row : newsfeedsTable.getRows()) {
					service.registerNewsfeed(row.getValues().get(3).getStringValue(), row.getValues().get(5).getStringValue());
				}
			}

		} catch (Exception e) {
			logger.logError("Error while trying to initialize newsfeed!", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Prüft, ob minimalen die Datenbank-Objekte notwendig für das Registrieren von Diensten und Newsfeeds vorhanden sind. Dazu prüft man, ob die
	 * `xvcasCASServices` vorhanden ist.
	 * 
	 * @return True, falls xvcascasservices vorhanden ist.
	 * @throws Exception
	 *             Fehler bei der Emittlung
	 */
	public boolean areServiceNotifiersStoresSetup() throws Exception {
		return securityService.isTablePresent("xvcascasservices");
	}

}

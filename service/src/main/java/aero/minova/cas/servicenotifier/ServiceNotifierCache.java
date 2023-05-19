package aero.minova.cas.servicenotifier;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.service.SecurityService;
import aero.minova.cas.service.model.NewsfeedListener;
import aero.minova.cas.service.model.ProcedureNewsfeed;
import aero.minova.cas.service.repository.ProcedureNewsfeedRepository;
import jakarta.annotation.PostConstruct;

// @Component - Der Cache wird bisher nicht verwendet, da er momentan auch noch nicht benötigt wird.
// Außerdem würde er, wenn er angeschalten ist, zu Testfehlern führen (aufgrund der PostConstruct-Annotation), welche schwierig zu lösen sind und wofür wir momentan keine Zeit haben.
@Deprecated
public class ServiceNotifierCache {

	@Autowired
	ServiceNotifierService service;

	@Autowired
	CustomLogger logger;

	@Autowired
	SecurityService securityService;

	ProcedureNewsfeedRepository procedureNewsfeedRepo;

	/**
	 * Wenn das CAS neu gestartet wird, müssen die Servicenotifier wieder aus der Datenbank ausgelesen werden, da die Map sonst leer ist.
	 */
	@PostConstruct
	private void initializeServicenotifiers() {
		try {
			if (areServiceNotifiersStoresSetup()) {
				service.servicenotifier = new HashMap<>();
				List<NewsfeedListener> servicenotifierTable = service.findViewEntry(null, null, null, null, null);
				for (NewsfeedListener newsfeedListener : servicenotifierTable) {
					List<ProcedureNewsfeed> procedureNewsfeeds = procedureNewsfeedRepo.findAllByTopicAndLastaction(null, 0);
					for (ProcedureNewsfeed newsfeed : procedureNewsfeeds) {
						service.registerServicenotifier(newsfeed.getKeytext(), newsfeedListener.getTopic());
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
		try {
			if (areServiceNotifiersStoresSetup()) {
				service.newsfeeds = new HashMap<>();
				List<NewsfeedListener> newsfeedsTable = service.findViewEntry(null, null, null, null, null);
				for (NewsfeedListener newsfeedListener : newsfeedsTable) {
					service.registerNewsfeed(newsfeedListener.getCasservice().getKeytext(), newsfeedListener.getTopic());
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

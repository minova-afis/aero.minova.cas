package aero.minova.cas;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import aero.minova.cas.api.restapi.ClientRestAPI;
import jakarta.annotation.PostConstruct;

@Component
public class CustomLogger {
	// Log für alle ausgeführten SQL Queries, außer die Privilegien
	public Logger logger = LoggerFactory.getLogger("SqlLogger");
	// Log für alle Privilegien Anfragen
	public Logger privilegeLogger = LoggerFactory.getLogger("PrivilegeLogger");
	// Log für Fehlermeldungen
	public Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	// Log für die Anfragen der User ohne SQL
	public Logger userLogger = LoggerFactory.getLogger("UserLogger");
	// Log für File Hashes und Zipps
	public Logger filesLogger = LoggerFactory.getLogger("FilesLogger");
	// Log für Setup
	public Logger setupLogger = LoggerFactory.getLogger("SetupLogger");
	// Log für QueueService
	public Logger queueServiceLog = LoggerFactory.getLogger("QueueServiceLog");
	// Log für allgemeine Infos, die nicht zu den obrigen Loggern passen. Z.B: Aufruf einer externen API.
	public Logger infoLogger = LoggerFactory.getLogger("InfoLog");

	private static final String CASUSER = "CAS: {}";

	private static final String LOGFORMAT = "{}: {}";

	private static final String ERRORLOGFORMAT = "{}: {}: \n{}";

	private static final String USERREQUESTLOGFORMAT = "{}: {} {}";

	@Autowired
	private ClientRestAPI crapi;

	private Gson gson;

	@PostConstruct
	private void init() {
		gson = crapi.getGson();
	}

	// Eclipse zeigt keinen Fehler, wenn Methode nicht vorhanden ist, sie wird aber benötigt, da sonst beim Loggen von Exceptions eine NoSuchMethodException
	// geworfen wird und der Code abbricht.
	public void logError(String logMessage, Exception e) {
		logError(logMessage, (Throwable) e);
	}

	public void logError(String logMessage, Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		errorLogger.error(ERRORLOGFORMAT, getUser(), logMessage, sw);
	}

	public void logError(Exception e) {
		logError(e.getMessage(), (Throwable) e);
	}

	public void logError(String logMessage) {
		errorLogger.error(LOGFORMAT, getUser(), logMessage);
	}

	public void logPrivilege(String logMessage) {
		privilegeLogger.info(LOGFORMAT, getUser(), logMessage);
	}

	public void logSql(String logMessage) {
		logger.info(LOGFORMAT, getUser(), logMessage);
	}

	public void logUserRequest(String logMessage) {
		userLogger.info(LOGFORMAT, getUser(), logMessage);
	}

	public void logUserRequest(String logMessage, Object gsonObject) {
		userLogger.info(USERREQUESTLOGFORMAT, getUser(), logMessage, gson.toJson(gsonObject));
	}

	public void logFiles(String logMessage) {
		filesLogger.info(CASUSER, logMessage);
	}

	public void logSetup(String logMessage) {
		setupLogger.info(CASUSER, logMessage);
	}

	public void logQueueService(String logMessage) {
		queueServiceLog.info(CASUSER, logMessage);
	}

	/**
	 * TODO Das loggen funktioniert zur Zeit nicht.
	 *
	 * @param event
	 *            Das Event, bei dem die Methode ausgeführt werden soll.
	 */
	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) {
		final Environment env = event.getApplicationContext().getEnvironment();
		final Logger initLogger = LoggerFactory.getLogger("Initialization");
		initLogger.info("Active profiles: {}", Arrays.toString(env.getActiveProfiles()));
		final MutablePropertySources sources = ((AbstractEnvironment) env).getPropertySources();
		StreamSupport.stream(sources.spliterator(), false)//
				.filter(EnumerablePropertySource.class::isInstance)//
				.map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())//
				.flatMap(Arrays::stream)//
				.distinct()//
				.filter(prop -> !(prop.contains("credentials") || prop.contains("password")))//
				.forEach(prop -> {
					try {
						initLogger.info("Property: {}={}", prop, env.getProperty(prop));
					} catch (IllegalArgumentException e) {
						initLogger.warn("Property: " + prop + " not resolvable: " + e.getMessage(), e);
					}
				});
	}

	public void logInfo(String logMessage) {
		infoLogger.info(LOGFORMAT, getUser(), logMessage);
	}

	private String getUser() {
		return SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : "CAS";
	}
}
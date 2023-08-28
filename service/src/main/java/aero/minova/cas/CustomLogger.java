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
	private static final Logger LOGGER = LoggerFactory.getLogger("SqlLogger");
	// Log für alle Privilegien Anfragen
	private static final Logger PRIVILEGELOGGER = LoggerFactory.getLogger("PrivilegeLogger");
	// Log für Fehlermeldungen
	private static final Logger ERRORLOGGER = LoggerFactory.getLogger("ErrorLogger");
	// Log für die Anfragen der User ohne SQL
	private static final Logger USERLOGGER = LoggerFactory.getLogger("UserLogger");
	// Log für File Hashes und Zipps
	private static final Logger FILESLOGGER = LoggerFactory.getLogger("FilesLogger");
	// Log für Setup
	private static final Logger SETUPLOGGER = LoggerFactory.getLogger("SetupLogger");
	// Log für QueueService
	private static final Logger QUEUESERVICELOGGER = LoggerFactory.getLogger("QueueServiceLog");
	// Log für allgemeine Infos, die nicht zu den obrigen Loggern passen. Z.B: Aufruf einer externen API.
	private static final Logger INFOLOGGER = LoggerFactory.getLogger("InfoLog");

	private static final String CASUSER = "CAS : {}";

	private static final String LOGFORMAT = "{} : {}";

	private static final String ERRORLOGFORMAT = "{} : {} : \n {}";

	private static final String USERREQUESTLOGFORMAT = "{} : {} {}";

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
		String user = SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : null;
		ERRORLOGGER.error(ERRORLOGFORMAT, user, logMessage, e.getMessage(), sw.toString());

	}

	public void logPrivilege(String logMessage) {
		String user = SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : null;
		PRIVILEGELOGGER.info(LOGFORMAT, user, logMessage);
	}

	public void logSql(String logMessage) {
		String user = SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : null;
		LOGGER.info(LOGFORMAT, user, logMessage);
	}

	public void logUserRequest(String logMessage) {
		String user = SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : null;
		USERLOGGER.info(LOGFORMAT, user, logMessage);
	}

	public void logUserRequest(String logMessage, Object gsonObject) {
		String user = SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : null;
		USERLOGGER.info(USERREQUESTLOGFORMAT, user, logMessage, gson.toJson(gsonObject));
	}

	public void logFiles(String logMessage) {
		FILESLOGGER.info(CASUSER, logMessage);
	}

	public void logSetup(String logMessage) {
		SETUPLOGGER.info(CASUSER, logMessage);
	}

	public void logQueueService(String logMessage) {
		QUEUESERVICELOGGER.info(CASUSER, logMessage);
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
				.filter(ps -> ps instanceof EnumerablePropertySource)//
				.map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())//
				.flatMap(Arrays::stream)//
				.distinct()//
				.filter(prop -> !(prop.contains("credentials") || prop.contains("password")))//
				.forEach(prop -> initLogger.info("Property: {}={}", prop, env.getProperty(prop)));
	}

	public void logInfo(String logMessage) {
		String user = SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : null;
		INFOLOGGER.info(LOGFORMAT, user, logMessage);
	}

	public Logger getErrorLogger() {
		return ERRORLOGGER;
	}

	public Logger getUserLogger() {
		return USERLOGGER;
	}
}
package aero.minova.cas;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Component
public class CustomLogger {
	// Log für alle ausgeführten SQL Queries, außer die Privilegien
	public Logger logger = LoggerFactory.getLogger("SqlLogger");
	// Log für alle Privilegien Anfragen
	public Logger privilegeLogger = LoggerFactory.getLogger("PrivilegeLogger");
	// Log für Fehlermeldungen
	Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
	// Log für die Anfragen der User ohne SQL
	public Logger userLogger = LoggerFactory.getLogger("UserLogger");
	// Log für File Hashes und Zipps
	public Logger filesLogger = LoggerFactory.getLogger("FilesLogger");
	// Log für Setup
	public Logger setupLogger = LoggerFactory.getLogger("SetupLogger");

	private ClientRestAPI crapi = new ClientRestAPI();

	Gson gson = crapi.gson();

	// Eclipse zeigt keinen Fehler, wenn Methode nicht vorhanden ist, sie wird aber benötigt, da sonst beim Loggen von Exceptions eine NoSuchMethodException
	// geworfen wird und der Code abbricht.
	public void logError(String logMessage, Exception e) {
		logError(logMessage, (Throwable) e);
	}

	public void logError(String logMessage, Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			errorLogger.error(null + ": " + logMessage + ": " + e.getMessage() + "\n" + sw.toString());
		} else {
			errorLogger
					.error(SecurityContextHolder.getContext().getAuthentication().getName() + ": " + logMessage + ": " + e.getMessage() + "\n" + sw.toString());
		}
	}

	public void logPrivilege(String logMessage) {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			privilegeLogger.info(null + ": " + logMessage);
		} else {
			privilegeLogger.info(SecurityContextHolder.getContext().getAuthentication().getName() + ": " + logMessage);
		}
	}

	public void logSql(String logMessage) {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			logger.info(null + ": " + logMessage);
		} else {
			logger.info(SecurityContextHolder.getContext().getAuthentication().getName() + ": " + logMessage);
		}
	}

	public void logUserRequest(String logMessage) {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			userLogger.info(null + ": " + logMessage);
		} else {
			userLogger.info(SecurityContextHolder.getContext().getAuthentication().getName() + ": " + logMessage);
		}
	}

	public void logUserRequest(String logMessage, Object gsonObject) {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			userLogger.info(null + ": " + logMessage + " " + gson.toJson(gsonObject));
		} else {
			userLogger.info(SecurityContextHolder.getContext().getAuthentication().getName() + ": " + logMessage + " " + gson.toJson(gsonObject));
		}
	}

	public void logFiles(String logMessage) {
		filesLogger.info("CAS " + logMessage);
	}

	public void logSetup(String logMessage) {
		setupLogger.info("CAS " + logMessage);
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
}

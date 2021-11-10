package aero.minova.core.application.system;

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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.stream.StreamSupport;

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

	public void logError(String logMessage, Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		errorLogger
				.error(SecurityContextHolder.getContext().getAuthentication().getName() + ": " + logMessage + ": " + e.getMessage() + "\n" + sw.toString());
	}

	public void logPrivilege(String logMessage) {
		privilegeLogger.info(SecurityContextHolder.getContext().getAuthentication().getName() + ": " + logMessage);
	}

	public void logSql(String logMessage) {
		logger.info(SecurityContextHolder.getContext().getAuthentication().getName() + ": " + logMessage);
	}

	public void logUserRequest(String logMessage) {
		userLogger.info(SecurityContextHolder.getContext().getAuthentication().getName() + ": " + logMessage);
	}

	public void logFiles(String logMessage) {
		filesLogger.info("CAS " + logMessage);
	}

	/**
	 * TODO Das loggen funktioniert zur Zeit nicht.
	 *
	 * @param event Das Event, bei dem die Methode ausgeführt werden soll.
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

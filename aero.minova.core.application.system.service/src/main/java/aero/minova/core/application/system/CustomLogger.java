package aero.minova.core.application.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
		errorLogger
				.error(SecurityContextHolder.getContext().getAuthentication().getName() + ": " + logMessage + ": " + e.getMessage() + "\n" + e.getStackTrace());
	}

	public void logPrivilege(String logMessage) {
		logger.info(SecurityContextHolder.getContext().getAuthentication().getName() + ": " + logMessage);
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
}

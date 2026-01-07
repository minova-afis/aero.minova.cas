package aero.minova.cas.service;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.controller.SqlProcedureController;
import aero.minova.cas.sql.SystemDatabase;
import jakarta.annotation.PostConstruct;

/**
 * Automatically runs database setup on application startup when enabled.
 * This eliminates the need for manual POST /setup calls in containerized environments.
 *
 * <p>Enable with: {@code ng.api.autosetup=true}</p>
 *
 * <p>The service checks if setup is needed by verifying the existence of core tables.
 * If tables don't exist or are empty, setup is executed automatically.</p>
 *
 * <p><strong>Backward Compatibility:</strong> Defaults to false, maintaining existing behavior.
 * Only activates when explicitly enabled.</p>
 *
 * @since 13.7.0
 */
@Service
@ConditionalOnProperty(name = "ng.api.autosetup", havingValue = "true", matchIfMissing = false)
public class AutoSetupService {

	@Autowired
	protected CustomLogger logger;

	@Autowired
	SystemDatabase database;

	@Autowired
	SqlProcedureController sqlProcedureController;

	@Value("${ng.api.autosetup.force:false}")
	private boolean forceSetup;

	/**
	 * Runs automatically after Spring context initialization.
	 * Checks if setup is needed and executes it if required.
	 */
	@PostConstruct
	public void autoSetup() {
		logger.logInfo("Auto-setup service enabled - checking if database setup is required...");

		try {
			if (forceSetup) {
				logger.logInfo("Force setup enabled - running setup unconditionally");
				executeSetup();
			} else if (isSetupNeeded()) {
				logger.logInfo("Database setup required - executing setup procedure");
				executeSetup();
			} else {
				logger.logInfo("Database already initialized - skipping setup");
			}
		} catch (Exception e) {
			logger.logError("Auto-setup failed! Application may not function correctly.", e);
			throw new RuntimeException("Auto-setup failed - see logs for details", e);
		}
	}

	/**
	 * Checks if database setup is needed by verifying core tables exist and have data.
	 *
	 * @return true if setup should be run
	 */
	private boolean isSetupNeeded() {
		try (Connection connection = database.getConnection()) {
			// Check if xtcasUsers table exists and has at least one user
			String query = database.isSQLDatabase()
				? "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'xtcasUsers'"
				: "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'xtcasUsers'";

			try (var statement = connection.createStatement();
			     var resultSet = statement.executeQuery(query)) {

				if (resultSet.next() && resultSet.getInt(1) == 0) {
					logger.logInfo("Core table xtcasUsers does not exist - setup needed");
					return true;
				}
			}

			// Table exists, check if it has data
			try (var statement = connection.createStatement();
			     var resultSet = statement.executeQuery("SELECT COUNT(*) FROM xtcasUsers")) {

				if (resultSet.next() && resultSet.getInt(1) == 0) {
					logger.logInfo("Core table xtcasUsers is empty - setup needed");
					return true;
				}
			}

			connection.commit();
			return false;

		} catch (Exception e) {
			// If we can't check, assume setup is needed
			logger.logInfo("Could not verify database state (likely not initialized) - setup needed ("+e.getMessage()+")");
			return true;
		}
	}

	/**
	 * Executes the setup procedure by calling the same logic as POST /setup endpoint.
	 * This ensures identical behavior whether setup runs automatically or manually.
	 */
	private void executeSetup() throws Exception {
		logger.logSetup("Starting automatic database setup...");

		Table setupTable = new Table();
		setupTable.setName("setup");

		try {
			// Execute setup using the same controller method as manual setup
			sqlProcedureController.executeProcedure(setupTable);
			logger.logSetup("Automatic database setup completed successfully");
		} catch (Exception e) {
			logger.logError("Automatic database setup failed!", e);
			throw e;
		}
	}
}

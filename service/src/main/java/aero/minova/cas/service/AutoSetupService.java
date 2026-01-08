package aero.minova.cas.service;

import java.sql.Connection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

	private boolean setupSucceeded = false;
	private Exception setupException = null;

	/**
	 * Runs automatically after Spring context initialization.
	 * Checks if setup is needed and executes it if required.
	 *
	 * <p>Creates a temporary security context with CAS_AUTOSETUP user to allow
	 * setup procedures to execute with elevated privileges. The context is
	 * cleared immediately after setup completes.</p>
	 *
	 * <p><strong>Important:</strong> This method will NOT prevent application startup on failure.
	 * If auto-setup fails, the error is logged prominently but the application continues.
	 * This prevents boot-loops in cloud environments. Check logs for setup errors.</p>
	 */
	@PostConstruct
	public void autoSetup() {
		logger.logInfo("Auto-setup service enabled - checking if database setup is required...");

		// Create temporary authentication context for auto-setup
		// This grants the setup process admin privileges without requiring login_dataSource=admin
		Authentication auth = new UsernamePasswordAuthenticationToken(
			"CAS_AUTOSETUP",
			null,
			Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
		);
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(auth);
		SecurityContextHolder.setContext(securityContext);

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
			setupSucceeded = true;
		} catch (Exception e) {
			setupException = e;
			setupSucceeded = false;

			// Log error prominently multiple times to ensure visibility in logs
			logger.logError("╔════════════════════════════════════════════════════════════════════╗", null);
			logger.logError("║                     AUTO-SETUP FAILED!                             ║", null);
			logger.logError("╠════════════════════════════════════════════════════════════════════╣", null);
			logger.logError("║  Database initialization failed but application will continue.     ║", null);
			logger.logError("║  The application may NOT function correctly!                       ║", null);
			logger.logError("║                                                                    ║", null);
			logger.logError("║  Possible causes:                                                  ║", null);
			logger.logError("║  - Database connection issues                                      ║", null);
			logger.logError("║  - Insufficient database permissions                               ║", null);
			logger.logError("║  - Incompatible database schema                                    ║", null);
			logger.logError("║  - SQL script errors                                               ║", null);
			logger.logError("║                                                                    ║", null);
			logger.logError("║  Check logs above for detailed error information.                  ║", null);
			logger.logError("║  You may need to run setup manually via POST /setup endpoint.      ║", null);
			logger.logError("╚════════════════════════════════════════════════════════════════════╝", null);
			logger.logError("Auto-setup exception details:", e);

			// DO NOT throw exception - allow application to start
			// This prevents boot-loops in Kubernetes/cloud environments
			// Operators can check logs and health endpoints to diagnose issues
		} finally {
			// Always clear the security context after setup completes
			// This ensures the temporary admin privileges don't persist
			SecurityContextHolder.clearContext();
			logger.logInfo("Auto-setup security context cleared");
		}
	}

	/**
	 * Returns whether auto-setup succeeded.
	 * Can be used by health checks or monitoring.
	 */
	public boolean isSetupSucceeded() {
		return setupSucceeded;
	}

	/**
	 * Returns the exception that caused setup failure, if any.
	 */
	public Exception getSetupException() {
		return setupException;
	}

	/**
	 * Checks if database setup is needed by verifying xtcasUsers table exists and has users.
	 *
	 * <p><strong>Critical Check:</strong> If xtcasUsers is absent OR empty, nobody can login
	 * when using login_dataSource=database. This check ensures the database is in a usable state.</p>
	 *
	 * <p>Setup is needed if:</p>
	 * <ul>
	 *   <li>xtcasUsers table does not exist</li>
	 *   <li>xtcasUsers table exists but is empty</li>
	 *   <li>Database state cannot be verified (connection issues)</li>
	 * </ul>
	 *
	 * @return true if setup should be run, false if database is already initialized
	 */
	private boolean isSetupNeeded() {
		try (Connection connection = database.getConnection()) {

			// Step 1: Check if xtcasUsers table exists
			logger.logInfo("Checking if xtcasUsers table exists...");
			String tableExistsQuery = database.isSQLDatabase()
				? "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'xtcasUsers'"
				: "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'xtcasUsers'";

			boolean tableExists = false;
			try (var statement = connection.createStatement();
			     var resultSet = statement.executeQuery(tableExistsQuery)) {

				if (resultSet.next()) {
					tableExists = resultSet.getInt(1) > 0;
				}
			}

			if (!tableExists) {
				logger.logInfo("> Table xtcasUsers does not exist - setup REQUIRED");
				logger.logInfo("  Reason: Without xtcasUsers, database authentication is impossible");
				return true;
			}
			logger.logInfo(" Table xtcasUsers exists");

			// Step 2: Check if xtcasUsers has at least one user
			logger.logInfo("Checking if xtcasUsers table has users...");
			int userCount = 0;
			try (var statement = connection.createStatement();
			     var resultSet = statement.executeQuery("SELECT COUNT(*) FROM xtcasUsers WHERE LastAction > 0")) {

				if (resultSet.next()) {
					userCount = resultSet.getInt(1);
				}
			}

			if (userCount == 0) {
				logger.logInfo("> Table xtcasUsers is empty - setup REQUIRED");
				logger.logInfo("  Reason: Without users, nobody can login");
				return true;
			}

			logger.logInfo("> Table xtcasUsers has " + userCount + " active user(s)");
			logger.logInfo("  Database appears initialized - skipping setup");

			connection.commit();
			return false;

		} catch (Exception e) {
			// If we can't verify database state, assume setup is needed
			// This is safe because setup is idempotent
			logger.logInfo("> Could not verify database state - assuming setup is needed");
			logger.logInfo("  Error: " + e.getMessage());
			logger.logInfo("  This is normal for first-time installations");
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
			sqlProcedureController.setupDefaultAdminUser();
			logger.logInfo("Automatic database setup completed successfully");
			logger.logSetup("Automatic database setup completed successfully");
		} catch (Exception e) {
			logger.logError("Automatic database setup failed!", e);
			throw e;
		}
	}
}

package aero.minova.cas.service;

import aero.minova.cas.sql.SystemDatabase;
import ch.minova.appmodel.log.Log;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;

@Service
public class SelfProbingService {

	@Autowired
	SystemDatabase database;

	private void systemExit() {
		try {
			// Wir schlafen etwas in der Hoffnung, dass die Log-Nachricht auch wirklich ausgegeben oder in eine Datei etc. ausgeschrieben wurde.
			Thread.sleep(10000);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		} finally {
			System.exit(1);
		}
	}

	/**
	 * <p>Setzt eine Behandlung von nicht behandelten Exceptions auf.
	 * Das sind Exceptions, die nicht einmal durch Springs default Catchern gefangen wurden.</p>
	 */
	@PostConstruct
	public void setup() {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			if (e instanceof OutOfMemoryError) {
				Log.err(this, "Not enough RAM is available.", e);
				systemExit();
			}
		});
	}

	@Scheduled(cron = "${self.probing.cron:0 * * * * *}")
	private void run() {
		try {
			if (database.isSQLDatabase()) {
				if (database.getConnection().prepareCall("select 1").execute()) {
					Log.debug(this, "The connection to the database is working.");
				} else {
					Log.debug(this, "The connection to the database failed.");
					systemExit();
				}
			} else {
				Log.debug(this, "Database connection is not probed.");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}

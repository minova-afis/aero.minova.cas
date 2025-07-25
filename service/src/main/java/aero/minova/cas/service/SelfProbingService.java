package aero.minova.cas.service;

import aero.minova.cas.CustomLogger;
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

/**
 * Prüft, ob die Verbindung zur Datenbank noch steht und beendet den Dienst,
 * wenn Probleme gefunden wurden.
 * Zudem werden alle SQL-Verbindungen geschlossen, um den Connection-Pool nicht zu überfüllen.
 *
 */
@Service
public class SelfProbingService {

	@Autowired
	protected CustomLogger logger;
	@Autowired
	SystemDatabase database;

	@Value("#{new Integer('${self.probing.max.time:60000}')}")
	private int probingMaxTime = 60_000;

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
				logger.logError("Not enough RAM is available.", e);
				systemExit();
			}
		});
	}

	@Scheduled(cron = "${self.probing.cron:-}")
	private void run() {
		final var probe = new Thread(() -> {
		try {
			/*
			 * Hiermit werden alle Verbindungen die zum Pool zurückgegeben geschlossen,
			 * statt diese in den Pool zu tun,
			 * um zu vermeiden, dass der Pool voll mit Verbindungen läuft,
			 * welche nicht funktionieren oder einen Fehler hatten,
			 * während dies Hikari nicht mitbekommt.
			 * Somit ist es unwahrscheinlicher, dass diese kaputte Verbindungen an den nächsten Nutzer weitergegeben werden.
			 */
			database.softEvictConnections();
			if (database.getConnection().prepareCall("select 1").execute()) {
				logger.logInfo("The connection to the database is working.");
			} else {
				logger.logInfo("The connection to the database failed.");
				systemExit();
			}
		} catch (SQLException e) {
			logger.logError("The connection to the database failed.", e);
			systemExit();
		}
		});
		probe.start();
		try {
			probe.join(probingMaxTime);
		} catch (InterruptedException e) {
			logger.logError("Interrupt during probe.", e);
		}
		if (probe.isAlive()) {
			logger.logInfo("Database connection probing should not take longer than " //
					+ (probingMaxTime / 1000) //
					+ " seconds.");
			systemExit();
		}
	}
}

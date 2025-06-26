package aero.minova.cas.service;

import ch.minova.appmodel.log.Log;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import org.hibernate.Session;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SelfProbingService {

	/**
	 * <p>Setzt eine Behandlung von nicht behandelten Exceptions auf.
	 * Das sind Exceptions, die nicht einmal durch Springs default Catchern gefangen wurden.</p>
	 */
	@PostConstruct
	public void setup() {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			if (e instanceof OutOfMemoryError) {
				Log.err(this, "Not enough RAM is available.", e);
				try {
					// Wir schlafen etwas in der Hoffnung, dass die Log-Nachricht auch wirklich ausgegeben oder in eine Datei etc. ausgeschrieben wurde.
					Thread.sleep(10000);
				} catch (InterruptedException ex) {
					throw new RuntimeException(ex);
				} finally {
					System.exit(1);
				}
			}
		});
	}

	@Scheduled(cron = "${self.probing.cron:0 * * * * *}")
	private void run() {
	}
}

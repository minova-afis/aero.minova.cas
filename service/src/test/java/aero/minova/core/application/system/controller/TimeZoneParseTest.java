package aero.minova.core.application.system.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TimeZoneParseTest extends BaseTest {

	// Die alte System-Zeitzone war bei mir z.b. 'Europe/Berlin'
	public static ZoneId zone = ZoneId.of("Europe/Berlin");

	@Test
	public void testParseWithWrongTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone(zone));
		Instant i = Instant.now();
		Timestamp oldTime = Timestamp.from(i);

		ZonedDateTime zdOld = i.atZone(zone);

		int instantHours = i.atZone(ZoneOffset.UTC).getHour();
		int zdHours = zdOld.getHour();
		int oldTimeHours = oldTime.getHours();

		// mit der alten Zeitzone passt die Uhrzeit nach dem Parsen nicht mehr
		assertThat(oldTimeHours).isNotEqualTo(instantHours);
		assertThat(instantHours).isNotEqualTo(zdHours);
	}

	@Test
	public void testParseWithUtcOnly() {
		// Setzten der UTC Zeitzone
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		Instant i = Instant.now();
		ZonedDateTime zdOld = i.atZone(zone);
		Timestamp newTime = Timestamp.from(i);
		ZonedDateTime zdNew = i.atZone(ZoneId.systemDefault());

		int instantHours = i.atZone(ZoneOffset.UTC).getHour();
		int zdHours = zdNew.getHour();
		int timestampHours = newTime.getHours();

		// mit der alten Zeitzone passt die Uhrzeit nach dem Parsen nicht mehr
		assertThat(zdOld.toString()).isNotEqualTo(zdNew.toString());

		// mit der neuen Zeitzone bleibt die Uhrzeit(In diesem Fall die Stunden) beim Parsen zwischen den Typen gleich
		assertThat(instantHours).isEqualTo(zdHours);
		assertThat(instantHours).isEqualTo(timestampHours);
	}

}
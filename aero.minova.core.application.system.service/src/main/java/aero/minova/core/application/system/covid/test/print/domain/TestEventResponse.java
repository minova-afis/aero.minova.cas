package aero.minova.core.application.system.covid.test.print.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class TestEventResponse {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private String description;
	private String testequipment;
	private String type;
	private String bookingdate;
	private String teststrecke;

	public TestEventResponse(String description, String testequipment, Instant bookingdate, String teststrecke) {
		if (bookingdate != null) {
			if (bookingdate.isAfter(Instant.now())) {
				this.setType("Termin");
				this.setDescription("Test ist noch ausstehend");
			} else {
				this.setType("Testergebnis");
				if (description == null) {
					this.setDescription("Ergebnis wird ausgewertet");
				} else {
					this.setDescription(description);
				}
			}
			if (testequipment != null) {
				this.setTestequipment(testequipment);
			} else {
				this.setTestequipment("Kein Testkit angegeben");
			}
			this.setBookingdate(LocalDateTime.ofInstant(bookingdate, ZoneId.systemDefault()).format(DATE_FORMATTER));
		} else {
			this.setType("");
			this.setDescription("keine Termine gefunden");
			this.setBookingdate("");
			this.setTestequipment("");
		}
		if (teststrecke != null && !teststrecke.isEmpty()) {
			this.setTeststrecke(teststrecke);
		} else {
			this.setTeststrecke("Fehler bei der Erfassung des Teststreckenstandorts.");
		}
	}

}

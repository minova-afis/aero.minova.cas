package aero.minova.cas.app.util;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Diese Klasse dient zum Wandeln von Temporal-Werten, die als Json-Payload
 * empfangen wurden. Anhand der String-Länge wird der Ziel-Typ festgelegt:
 * <ol>
 * <li>6 Zeichen : wird als HHMMSS interpretiert in {@link LocalTime}
 * gewandelt</li>
 * <li>8 Zeichen : wird als YYYYMMDD interpretiert in {@link LocalDate}
 * gewandelt</li>
 * <li>14 Zeichen : wird als YYMMDDHHMMSS interpretiert in {@link LocalDateTime}
 * gewandelt</li>
 * </ol>
 * <br>
 * <b>Achtung:</b> wenn eine Zeitangabe zwischendurch als Long gewandelt war,
 * kann es Probleme beim Zurückwandeln von Zeiten zwischen 0 und 9 Uhr in ein
 * LocalTime kommen.
 */
public class TemporalDeserializer implements JsonDeserializer<Temporal> {

	/**
	 * @param json    The Json data being deserialized
	 * @param typeOfT The type of the Object to deserialize to
	 * @param context nicht verwendet
	 * @return entsprechend dem Format ein LocalDate, LocalTime oder LocalDateTime
	 * @throws java.time.DateTimeException wenn alles Ziffern sind, aus denen aber
	 *                                     kein Temporal-Wert gebildet werden kann.
	 * @throws NumberFormatException       wenn der String außer Ziffern andere
	 *                                     Zeichen enthält.
	 * @throws JsonParseException
	 */
	@Override
	public Temporal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		String dateTime = json.getAsString();
		Temporal t;

		switch (dateTime.length()) {
		case 6:
			t = LocalTime.of(Integer.parseInt(dateTime.substring(0, 2)), Integer.parseInt(dateTime.substring(2, 4)),
					Integer.parseInt(dateTime.substring(4, 6)));
			break;
		case 8:
			t = LocalDate.of(Integer.parseInt(dateTime.substring(0, 4)), Integer.parseInt(dateTime.substring(4, 6)),
					Integer.parseInt(dateTime.substring(6, 8)));
			break;
		case 14:
			t = LocalDateTime.of(Integer.parseInt(dateTime.substring(0, 4)), Integer.parseInt(dateTime.substring(4, 6)),
					Integer.parseInt(dateTime.substring(6, 8)), Integer.parseInt(dateTime.substring(8, 10)),
					Integer.parseInt(dateTime.substring(10, 12)), Integer.parseInt(dateTime.substring(12, 14)));
			break;
		default:
			return null;
		}

		if (t.getClass() == typeOfT) {
			return t;
		}

		if (typeOfT == LocalDate.class && t instanceof LocalDateTime ldt) {
			return ldt.toLocalDate();
		} else if (typeOfT == LocalDateTime.class && t instanceof LocalDate ld) {
			return ld.atStartOfDay();
		} else if (typeOfT == LocalTime.class && t instanceof LocalDateTime ldt) {
			return ldt.toLocalTime();
		} else if (typeOfT == LocalDateTime.class && t instanceof LocalTime lt) {
			return lt.atDate(LocalDate.parse("1900-01-01"));
		}

		return null;
	}

}

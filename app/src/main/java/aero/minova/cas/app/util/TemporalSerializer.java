package aero.minova.cas.app.util;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Diese Klasse dient zum Wandeln von Werten, die von {@link Temporal}
 * abgeleitet sind
 * <ol>
 * <li>{@link LocalDate} wird gewandelt in Format YYYYMMDD, Länge 8 Zeichen</li>
 * <li>{@link LocalTime} wird gewandelt in Format HHMMSS, Länge 6 Zeichen</li>
 * <li>{@link LocalDateTime} wird gewandelt in Format YYYYMMDDHHMMSS, Länge 14
 * Zeichen</li>
 * </ol>
 * <br>
 * Der entstehende String wird als Long in ein JsonElement gesteckt.<br>
 * <b>ACHTUNG:</b> das Zurückwandeln klappt nicht für LocalDate in der Zeit
 * zwischen 0 und 9 Uhr, dann geht die führende 0 verloren und der
 * {@link TemporalDeserializer} kommt nicht klar.
 */
public class TemporalSerializer implements JsonSerializer<Temporal> {

	@Override
	public JsonElement serialize(Temporal src, Type typeOfSrc, JsonSerializationContext context) {
		StringBuilder sb = new StringBuilder();
		if (src.isSupported(ChronoField.YEAR)) {
			String year = "0000" + src.get(ChronoField.YEAR);
			String month = "00" + src.get(ChronoField.MONTH_OF_YEAR);
			String day = "00" + src.get(ChronoField.DAY_OF_MONTH);
			sb.append(year.substring(year.length() - 4));
			sb.append(month.substring(month.length() - 2));
			sb.append(day.substring(day.length() - 2));
		}
		if (src.isSupported(ChronoField.HOUR_OF_DAY)) {
			String hour = "00" + src.get(ChronoField.HOUR_OF_DAY);
			String minute = "00" + src.get(ChronoField.MINUTE_OF_HOUR);
			String second = "00" + src.get(ChronoField.SECOND_OF_MINUTE);
			sb.append(hour.substring(hour.length() - 2));
			sb.append(minute.substring(minute.length() - 2));
			sb.append(second.substring(second.length() - 2));
		}
		return new JsonPrimitive(Long.valueOf(sb.toString()));
	}

}

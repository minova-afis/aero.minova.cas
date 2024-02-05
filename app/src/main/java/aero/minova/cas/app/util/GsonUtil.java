package aero.minova.cas.app.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

	private GsonUtil() {
	}

	public static Gson getGson() {
		return getGsonBuilder().create();
	}

	public static GsonBuilder getGsonBuilder() {
		return new GsonBuilder()//
				.registerTypeAdapter(LocalDateTime.class, new TemporalSerializer()) //
				.registerTypeAdapter(LocalDateTime.class, new TemporalDeserializer()) //
				.registerTypeAdapter(LocalDate.class, new TemporalSerializer()) //
				.registerTypeAdapter(LocalDate.class, new TemporalDeserializer())//
				.registerTypeAdapter(LocalTime.class, new TemporalSerializer()) //
				.registerTypeAdapter(LocalTime.class, new TemporalDeserializer());
	}
}

package aero.minova.core.application.system.domain;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Locale;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class ValueDeserializer implements JsonDeserializer<Value> {
	public static final String SQL_IS_NULL = "is null";
	public static final String SQL_IS_NOT_NULL = "is not null";
	public static final String[] SQL_OPERATORS = { "<>", "<=", ">=", "<", ">", "=", "between()", "in()", "not like", "like", SQL_IS_NULL, SQL_IS_NOT_NULL };

	@Override
	public Value deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
		if (json == null || type == null) {
			return null;
		}
		String typeString = json.getAsString().substring(0, 1);
		String rule = json.getAsString().substring(1, 2);
		String value = json.getAsString().substring(2);

		if (rule.contains("#")) {
			rule = value.substring(0, getOperatorEndIndex(value));
			value = value.substring(getOperatorEndIndex(value) + 1, value.length());
		} else {
			rule = null;
		}

		switch (typeString) {
		case "n":
			return new Value(Integer.parseInt(value), rule);
		case "d":
			return new Value(Double.parseDouble(value), rule);
		case "s":
			return new Value(value, rule);
		case "i":
			return new Value(Instant.parse(value), rule);
		case "z":
			return new Value(ZonedDateTime.parse(value), rule);
		case "b":
			return new Value(Boolean.valueOf(value), rule);
		default:
			break;
		}
		return null;
	}

	/**
	 * Prüft, ob der String einen SQL Operator am Anfang hat
	 * 
	 * @param value
	 * @return
	 */
	protected static boolean hasOperator(String value) {
		return getOperatorEndIndex(value) != 0;
	}

	/**
	 * Wenn es einen Operator gibt, dann liefert die Funktion den Index bis zu dem sich der Operator erstreckt
	 * 
	 * @param value
	 * @return 0, wenn es keinen Operator gibt
	 */
	protected static int getOperatorEndIndex(String value) {
		if (value == null || value.length() == 0) {
			return 0;
		}
		// Wir simulieren einen ltrim, um die Anfangsposition des Operators festzustellen
		String tmp = (value + "_").trim();
		tmp = tmp.toLowerCase(Locale.ENGLISH).substring(0, tmp.length() - 1);
		final int shift = value.length() - tmp.length();
		for (final String sqlOperator : SQL_OPERATORS) {
			if (tmp.startsWith(sqlOperator)) {
				return shift + sqlOperator.length();
			}
		}
		return 0;
	}
}
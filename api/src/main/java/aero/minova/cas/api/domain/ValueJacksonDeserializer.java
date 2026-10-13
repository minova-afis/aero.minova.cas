package aero.minova.cas.api.domain;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Reiner Jackson-Deserializer für {@link Value} - ohne Gson-Abhängigkeit.
 *
 * <p>
 * Erwartet einen JSON-String im Format {@code "<typ>-<wert>"} bzw. {@code "f-<rule>-<typ>-<wert>"}
 * (siehe {@link ValueJacksonSerializer} für das Gegenstück).
 * </p>
 */

public class ValueJacksonDeserializer extends JsonDeserializer<Value> {

	public static final String SQL_IS_NULL = "null";
	public static final String SQL_IS_NOT_NULL = "!null";
	protected static final String[] SQL_OPERATORS = { "<>", "<=", ">=", "<", ">", "=", "between()", "in()", "!~", "~", SQL_IS_NULL, SQL_IS_NOT_NULL };

	@Override
	public Value deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonNode node = jsonParser.getCodec().readTree(jsonParser);

		if (node == null || node.isNull()) {
			return null;
		}

		return convertToValue(node.asText());
	}

	/**
	 * Ehemals der Gson-{@code deserialize(JsonElement, Type, JsonDeserializationContext)} - inhaltlich
	 * unverändert, arbeitet jetzt aber direkt auf dem rohen String statt auf einem {@code JsonElement}.
	 *
	 * @param raw
	 *            Der rohe (bereits von Anführungszeichen befreite) Wert aus dem JSON, z.B. {@code "n-42"}
	 *            oder {@code "f-=-n-5"}.
	 */
	private Value convertToValue(String raw) {
		if (raw == null) {
			return null;
		}

		String typeString = raw.substring(0, 1);
		String value = raw.substring(2);
		String rule;

		if (value == null) {
			throw new NullPointerException("No values found!");
		}

		if (typeString.equals("f")) {
			int operatorPos = getOperatorEndIndex(value);
			rule = value.substring(0, operatorPos).toLowerCase();
			// falls die Regel is null oder is not null ist, macht es keinen Sinn, dass ein Wert weiter gegeben würde
			if (rule.contains("null")) {
				value = "";
				typeString = "s";
			} else if (rule.contains("!~")) {
				rule = "not like";
				typeString = value.substring(operatorPos + 1, operatorPos + 2);
				value = value.substring(operatorPos + 3, value.length());
			} else if (rule.contains("~")) {
				rule = "like";
				typeString = value.substring(operatorPos + 1, operatorPos + 2);
				value = value.substring(operatorPos + 3, value.length());
			} else {
				typeString = value.substring(operatorPos + 1, operatorPos + 2);
				value = value.substring(operatorPos + 3, value.length());
			}
		} else if (typeString.equals("r")) {
			typeString = "s";
			rule = value.substring(0, value.indexOf("-"));
			value = value.substring(value.indexOf("-") + 1, value.length());
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
		case "m":
			return new Value(BigDecimal.valueOf(Double.parseDouble(value)), rule);
		case "l":
			return new Value(Long.parseLong(value), rule);
		case "x":
			// Decode Base64
			return new Value(value == null || value.isEmpty() ? null : Base64.getDecoder().decode(value), rule);
		default:
			break;
		}
		return null;
	}

	/**
	 * Wenn es einen Operator gibt, dann liefert die Funktion den Index bis zu dem sich der Operator erstreckt
	 *
	 * @param value
	 *            String mit SQL-Operator am Anfang
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
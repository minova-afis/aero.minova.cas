package aero.minova.cas.api.domain;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Reiner Jackson-Serializer für {@link Value} - ohne Gson-Abhängigkeit.
 *
 * <p>
 * Erzeugt einen JSON-String im Format {@code "<typ>-<wert>"} bzw. {@code "f-<rule>-<typ>-<wert>"}
 * (siehe {@link ValueDeserializer} für das Gegenstück).
 * </p>
 *
 * <p>
 * Wichtig: Der Wert wird über {@link JsonGenerator#writeString(String)} geschrieben, nicht über
 * {@code writeRawValue(...)} mit manuell angehängten Anführungszeichen. {@code writeString} übernimmt
 * das korrekte JSON-Escaping (Anführungszeichen, Backslashes, Steuerzeichen, Unicode) - bei String-Werten
 * (Typ {@code STRING}) kann der eigentliche Wert beliebige Zeichen enthalten, die sonst ungültiges JSON
 * erzeugen würden.
 * </p>
 */
public class ValueJacksonSerializer extends JsonSerializer<Value> {

	@Override
	public void serialize(Value value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		String converted = convertToJsonContent(value);

		if (converted == null) {
			jsonGenerator.writeNull();
			return;
		}

		// writeString() statt writeRawValue(): Jackson kümmert sich um korrektes JSON-Escaping
		// des Inhalts (z.B. wenn ein STRING-Wert Anführungszeichen oder Backslashes enthält).
		jsonGenerator.writeString(converted);
		
	}

	/**
	 * Ehemals der Gson-{@code serialize(Value, Type, JsonSerializationContext)} - inhaltlich unverändert,
	 * liefert jetzt aber den rohen (unquotierten) String statt eines Gson-{@code JsonPrimitive}. Das
	 * Escaping für die JSON-Ausgabe übernimmt {@link JsonGenerator#writeString(String)} im Aufrufer.
	 */
	private String convertToJsonContent(Value value) {
		if (value == null || value.getValue() == null || value.getType() == null) {
			return null;
		}

		String ruleString = "";
		if (value.getRule() != null) {
			String rule = value.getRule();
			if (rule.equals("like")) {
				rule = "~";
			} else if (rule.equals("not like")) {
				rule = "!~";
			}
			ruleString = "f-" + rule + "-";
		}

		switch (value.getType()) {
		case INTEGER:
			return ruleString + "n-" + value.getIntegerValue();
		case DOUBLE:
			return ruleString + "d-" + value.getDoubleValue();
		case STRING:
			return ruleString + "s-" + value.getStringValue();
		case INSTANT:
			return ruleString + "i-" + value.getInstantValue().toString();
		case ZONED:
			return ruleString + "z-" + value.getZonedDateTimeValue().toString();
		case BOOLEAN:
			return ruleString + "b-" + value.getBooleanValue().toString();
		case BIGDECIMAL:
			return ruleString + "m-" + value.getBigDecimalValue().toString();
		case LONG:
			return ruleString + "l-" + value.getLongValue().toString();
		case BINARY:
			return ruleString + "x-" + java.util.Base64.getEncoder().encodeToString(value.getBinaryValue());
		default:
			return null;
		}
	}
}

package aero.minova.cas.api.domain;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ValueGsonSerializer implements JsonSerializer<Value> {

	@Override
	public JsonElement serialize(Value value, Type type, JsonSerializationContext context) {
		if (value == null || type == null || value.getValue() == null || value.getType() == null) {
			return null;
		}
				
		return new JsonPrimitive(convertToJsonContent(value));
	}

	// AUS ValueJacksonSerializer. Kann später ins Value Objekt wandern.
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
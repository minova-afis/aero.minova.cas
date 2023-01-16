package aero.minova.cas.api.domain;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ValueSerializer extends com.fasterxml.jackson.databind.JsonSerializer<Value> implements JsonSerializer<Value> {
	@Autowired
	private Gson gson;

	@Override
	public void serialize(Value value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeRawValue(gson.toJson(value));
	}

	@Override
	public JsonElement serialize(Value value, Type type, JsonSerializationContext context) {
		if (value == null || type == null || value.getValue() == null || value.getType() == null) {
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
			return new JsonPrimitive(ruleString + "n-" + value.getIntegerValue());
		case DOUBLE:
			return new JsonPrimitive(ruleString + "d-" + value.getDoubleValue());
		case STRING:
			return new JsonPrimitive(ruleString + "s-" + value.getStringValue());
		case INSTANT:
			return new JsonPrimitive(ruleString + "i-" + value.getInstantValue().toString());
		case ZONED:
			return new JsonPrimitive(ruleString + "z-" + value.getZonedDateTimeValue().toString());
		case BOOLEAN:
			return new JsonPrimitive(ruleString + "b-" + value.getBooleanValue().toString());
		case BIGDECIMAL:
			return new JsonPrimitive(ruleString + "m-" + value.getBigDecimalValue().toString());
		case LONG:
			return new JsonPrimitive(ruleString + "l-" + value.getLongValue().toString());
		default:
			return null;
		}
	}
}
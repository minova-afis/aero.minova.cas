package aero.minova.cas.api.domain;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;

public class ValueSerializer extends com.fasterxml.jackson.databind.JsonSerializer<Value>  implements JsonSerializer<Value> {
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
		switch (value.getType()) {
		case INTEGER:
			return new JsonPrimitive("n-" + value.getIntegerValue());
		case DOUBLE:
			return new JsonPrimitive("d-" + value.getDoubleValue());
		case STRING:
			return new JsonPrimitive("s-" + value.getStringValue());
		case INSTANT:
			return new JsonPrimitive("i-" + value.getInstantValue().toString());
		case ZONED:
			return new JsonPrimitive("z-" + value.getZonedDateTimeValue().toString());
		case BOOLEAN:
			return new JsonPrimitive("b-" + value.getBooleanValue().toString());
		case BIGDECIMAL:
			return new JsonPrimitive("m-" + value.getBigDecimalValue().toString());
		default:
			return null;
		}
	}
}
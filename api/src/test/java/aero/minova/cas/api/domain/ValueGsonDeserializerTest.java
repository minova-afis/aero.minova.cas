package aero.minova.cas.api.domain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class ValueGsonDeserializerTest {

	 private static final Gson GSON = new GsonBuilder()
	    		.disableHtmlEscaping()
	            .registerTypeAdapter(Value.class, new ValueGsonDeserializer())
	            .registerTypeAdapter(Value.class, new ValueGsonSerializer())
	            .create();

	private ValueGsonDeserializer valueGsonDeserializer;

	@Mock
	private JsonDeserializationContext jsonDeserializationContext;
	
	@BeforeEach
	void setUp() {
		valueGsonDeserializer = new ValueGsonDeserializer();
	}

	// --- Null-Fälle -------------------------------------------------------

	@Test
	@DisplayName("JSON null -> deserialisiert zu null")
	void testDeserialize_jsonNull_returnsNull() throws Exception {
		Value actual = valueGsonDeserializer.deserialize(null, Value.class, jsonDeserializationContext);
		
		assertNull(actual);		
	}

	// --- Alle DataTypes ohne Rule ------------------------------------------
	@ParameterizedTest(name = "{0} -> {1}")
	@MethodSource("rawJsonAndExpected")
	@DisplayName("Deserialisiert jeden DataType korrekt (ohne Rule)")
	void testDeserialize_perType_noRule(JsonPrimitive rawJson, DataType expectedType, Object expectedValue) throws Exception {
	
		Value actual = valueGsonDeserializer.deserialize((JsonElement)rawJson, Value.class, jsonDeserializationContext);

		assertEquals(expectedType, actual.getType());
		if (expectedType == DataType.BINARY) {
			assertArrayEquals((byte[]) expectedValue, (byte[]) actual.getValue());
		} else {
			assertEquals(expectedValue, actual.getValue());
		}
		assertNull(actual.getRule());
	}

	private static Stream<Arguments> rawJsonAndExpected() {
		Instant instant = Instant.parse("2024-01-15T10:30:00Z");
		ZonedDateTime zoned = ZonedDateTime.of(2024, 1, 15, 10, 30, 0, 0, ZoneOffset.UTC);
		byte[] binary = { 1, 2, 3, 4 };

		return Stream.of(
				Arguments.of(new JsonPrimitive("n-42"), DataType.INTEGER, 42),
				Arguments.of(new JsonPrimitive("l-123456789"), DataType.LONG, 123456789L),
				Arguments.of(new JsonPrimitive("d-3.14"), DataType.DOUBLE, 3.14),
				Arguments.of(new JsonPrimitive("s-hello world"), DataType.STRING, "hello world"),
				Arguments.of(new JsonPrimitive("b-true"), DataType.BOOLEAN, Boolean.TRUE),
				Arguments.of(new JsonPrimitive("m-199.99"), DataType.BIGDECIMAL, BigDecimal.valueOf(199.99)),
				Arguments.of(new JsonPrimitive("i-" + instant + ""), DataType.INSTANT, instant),
				Arguments.of(new JsonPrimitive("z-" + zoned + ""), DataType.ZONED, zoned),
				Arguments.of(new JsonPrimitive("x-" + Base64.getEncoder().encodeToString(binary) + ""), DataType.BINARY, binary));
	}

	// --- Rule-Handling ------------------------------------------------------
	@Test
	@DisplayName("Rule '=' wird korrekt zurückgelesen")
	void testDeserialize_ruleEquals() throws Exception {
		JsonPrimitive jsonString = new JsonPrimitive("f-=-n-5");
		Value actual = valueGsonDeserializer.deserialize((JsonElement)jsonString, Value.class, jsonDeserializationContext);

		assertEquals(DataType.INTEGER, actual.getType());
		assertEquals(5, actual.getIntegerValue());
		assertEquals("=", actual.getRule());
	}

	@Test
	@DisplayName("~ wird zu Rule 'like' übersetzt")
	void testDeserialize_tildeIsTranslatedToLike() throws Exception {
		JsonPrimitive jsonString = new JsonPrimitive("f-~-s-abc");
		Value actual = valueGsonDeserializer.deserialize((JsonElement)jsonString, Value.class, jsonDeserializationContext);

		assertEquals(DataType.STRING, actual.getType());
		assertEquals("abc", actual.getStringValue());
		assertEquals("like", actual.getRule());
	}

	@Test
	@DisplayName("!~ wird zu Rule 'not like' übersetzt")
	void testDeserialize_notTildeIsTranslatedToNotLike() throws Exception {
		JsonPrimitive jsonString = new JsonPrimitive("f-!~-s-abc");
		Value actual = valueGsonDeserializer.deserialize((JsonElement)jsonString, Value.class, jsonDeserializationContext);

		assertEquals(DataType.STRING, actual.getType());
		assertEquals("abc", actual.getStringValue());
		assertEquals("not like", actual.getRule());
	}

	@Test
	@DisplayName("Rule ohne Sonderfall (z.B. 'between()') bleibt unverändert")
	void testDeserialize_ruleOtherPassesThrough() throws Exception {
		JsonPrimitive jsonString = new JsonPrimitive("f-between()-n-5");
		Value actual = valueGsonDeserializer.deserialize((JsonElement)jsonString, Value.class, jsonDeserializationContext);

		assertEquals(DataType.INTEGER, actual.getType());
		assertEquals(5, actual.getIntegerValue());
		assertEquals("between()", actual.getRule());
	}

	@Test
	@DisplayName("Rule 'null' -> Wert wird verworfen, Ergebnis ist leerer String")
	void testDeserialize_ruleNull_discardsValue() throws Exception {
		JsonPrimitive jsonString = new JsonPrimitive("f-null-x");
		Value actual = valueGsonDeserializer.deserialize((JsonElement)jsonString, Value.class, jsonDeserializationContext);

		assertEquals(DataType.STRING, actual.getType());
		assertEquals("", actual.getStringValue());
		assertEquals("null", actual.getRule());
	}

	@Test
	@DisplayName("Rule '!null' -> Wert wird verworfen, Ergebnis ist leerer String")
	void testDeserialize_ruleNotNull_discardsValue() throws Exception {
		JsonPrimitive jsonString = new JsonPrimitive("f-!null-x");
		Value actual = valueGsonDeserializer.deserialize((JsonElement)jsonString, Value.class, jsonDeserializationContext);

		assertEquals(DataType.STRING, actual.getType());
		assertEquals("", actual.getStringValue());
		assertEquals("!null", actual.getRule());
	}

	@Test
	@DisplayName("Präfix 'r' (Legacy-Format) wird korrekt in Rule + String-Wert zerlegt")
	void testDeserialize_legacyRPrefix() throws Exception {
		JsonPrimitive jsonString = new JsonPrimitive("r-customRule-myValue");
		Value actual = valueGsonDeserializer.deserialize((JsonElement)jsonString, Value.class, jsonDeserializationContext);

		assertEquals(DataType.STRING, actual.getType());
		assertEquals("myValue", actual.getStringValue());
		assertEquals("customRule", actual.getRule());
	}

	@Test
	@DisplayName("Unbekannter Typ-Präfix liefert null")
	void testDeserialize_unknownTypePrefix_returnsNull() throws Exception {
		JsonPrimitive jsonString = new JsonPrimitive("q-irgendwas");
		Value actual = valueGsonDeserializer.deserialize((JsonElement)jsonString, Value.class, jsonDeserializationContext);

		assertNull(actual);
	}

	// --- Roundtrip mit dem Serializer ---------------------------------------

	@Test
	@DisplayName("Roundtrip: Serializer -> Deserializer liefert äquivalenten Value")
	void testRoundtrip_withSerializer() throws Exception {

		Value original = new Value(BigDecimal.valueOf(42.5), "<=");
		
		String json = GSON.toJson(original);
		Value result = GSON.fromJson(json, Value.class);

		assertEquals(original.getType(), result.getType());
		assertEquals(original.getBigDecimalValue(), result.getBigDecimalValue());
		assertEquals(original.getRule(), result.getRule());
	}
}

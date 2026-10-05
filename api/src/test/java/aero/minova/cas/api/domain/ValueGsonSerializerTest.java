package aero.minova.cas.api.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

public class ValueGsonSerializerTest {

    private static final Gson GSON = new GsonBuilder()
    		.disableHtmlEscaping()
            .registerTypeAdapter(Value.class, new ValueGsonSerializer())
            .create();

    
	private ValueGsonSerializer serializer;
	
	@Mock
	private JsonSerializationContext jsonSerializationContext;
	
	@BeforeEach
	public void setup() {
		serializer = new ValueGsonSerializer();
	}
		
	// --------------------------------------------------------------------------------------
	@Test
	void serializesCustomObjectWithGsonContext() {
	    Value value = buildValue(DataType.STRING, "hallo", "=");
	    
	    JsonElement actual = serializer.serialize(
	    		value,
	    		Value.class,
	    		jsonSerializationContext
	        );
	    
	    JsonPrimitive expected = new JsonPrimitive("f-=-s-hallo");

	    assertEquals(expected, actual);
	}
	
	
	@Test
	@DisplayName("value == null -> schreibt null")
	void testSerialize_valueIsNull_writesNull() throws Exception {
		//JsonSerializationContext jsonSerializationContext = mock(JsonSerializationContext.class);
		Value value = null;
		
		JsonElement actual = serializer.serialize(
				value,
	    		Value.class,
	    		jsonSerializationContext
	        );
	    
	    assertNull(actual);		
	}

	@Test
	@DisplayName("Value.getValue() == null -> schreibt null")
	void testSerialize_innerValueIsNull_writesNull() throws Exception {
		Value value = mock(Value.class);
		when(value.getValue()).thenReturn(null);
		when(value.getType()).thenReturn(DataType.DOUBLE);

		JsonElement actual = serializer.serialize(
				value,
	    		Value.class,
	    		jsonSerializationContext
	        );
	    
	    assertNull(actual);		
	}

	@Test
	@DisplayName("Value.getType() == null -> schreibt null")
	void testSerialize_typeIsNull_writesNull() throws Exception {
		Value value = mock(Value.class);
		
		when(value.getValue()).thenReturn("irgendwas");
		when(value.getType()).thenReturn(null);
		
		JsonElement actual = serializer.serialize(
				value,
	    		Value.class,
	    		jsonSerializationContext
	        );

	    assertNull(actual);		
	}
	
	// --- Alle DataTypes ohne Rule ------------------------------------------

	@ParameterizedTest(name = "{0} -> {1}")
	@MethodSource("typeValueAndExpectedPrefix")
	@DisplayName("Serialisiert jeden DataType korrekt (ohne Rule)")
	void testSerialize_perType_noRule(DataType type, Object rawValue, JsonPrimitive expected) throws Exception {
		Value value = buildValue(type, rawValue, null);

		JsonElement actual = serializer.serialize(
				value,
	    		Value.class,
	    		jsonSerializationContext
	        );

		assertEquals(expected, actual);
	}

	private static Stream<Arguments> typeValueAndExpectedPrefix() {
		Instant instant = Instant.parse("2024-01-15T10:30:00Z");
		ZonedDateTime zoned = ZonedDateTime.of(2024, 1, 15, 10, 30, 0, 0, ZoneOffset.UTC);
		byte[] binary = { 1, 2, 3, 4 };

		return Stream.of(
				Arguments.of(DataType.INTEGER, 42, "n-42"),
				Arguments.of(DataType.LONG, 123456789L, "l-123456789"),
				Arguments.of(DataType.DOUBLE, 3.14, "d-3.14"),
				Arguments.of(DataType.STRING, "hello world", "s-hello world"),
				Arguments.of(DataType.BOOLEAN, Boolean.TRUE, "b-true"),
				Arguments.of(DataType.BIGDECIMAL, new BigDecimal("199.99"), "m-199.99"),
				Arguments.of(DataType.INSTANT, instant, "i-" + instant + ""),
				Arguments.of(DataType.ZONED, zoned, "z-" + zoned + ""),
				Arguments.of(DataType.BINARY, binary, "x-" + Base64.getEncoder().encodeToString(binary) + ""));
	}

	// --- Rule-Handling ------------------------------------------------------
	@Test
	@DisplayName("Rule '=' wird als f-=- Präfix übernommen")
	void testSerialize_ruleEquals() throws Exception {
		Value value = buildValue(DataType.INTEGER, 5, "=");

		JsonElement expected = new JsonPrimitive("f-=-n-5");

		JsonElement actual = serializer.serialize(
				value,
	    		Value.class,
	    		jsonSerializationContext
	        );
				
		assertEquals(expected, actual);
	}

	@Test
	@DisplayName("Rule 'like' wird zu ~ übersetzt")
	void testSerialize_ruleLikeIsTranslated() throws Exception {
		Value value = buildValue(DataType.STRING, "abc", "like");

		JsonElement expected = new JsonPrimitive("f-~-s-abc");

		JsonElement actual = serializer.serialize(
				value,
	    		Value.class,
	    		jsonSerializationContext
	        );
				
		assertEquals(expected, actual);
	}

	@Test
	@DisplayName("Rule 'not like' wird zu !~ übersetzt")
	void testSerialize_ruleNotLikeIsTranslated() throws Exception {
		Value value = buildValue(DataType.STRING, "abc", "not like");

		JsonElement expected = new JsonPrimitive("f-!~-s-abc");

		JsonElement actual = serializer.serialize(
				value,
	    		Value.class,
	    		jsonSerializationContext
	        );
				
		assertEquals(expected, actual);
	}

	@Test
	@DisplayName("Rule ohne Sonderfall (z.B. 'between()') bleibt unverändert")
	void testSerialize_ruleOtherPassesThrough() throws Exception {
		Value value = buildValue(DataType.INTEGER, 5, "between()");

		JsonElement expected = new JsonPrimitive("f-between()-n-5");

		JsonElement actual = serializer.serialize(
				value,
	    		Value.class,
	    		jsonSerializationContext
	        );
				
		assertEquals(expected, actual);
	}

	@Test
	@DisplayName("Keine Rule -> kein f-...- Präfix")
	void testSerialize_noRule_noPrefixWritten() throws Exception {
		Value value = buildValue(DataType.INTEGER, 5, null);

		JsonElement expected = new JsonPrimitive("n-5");

		JsonElement actual = serializer.serialize(
				value,
	    		Value.class,
	    		jsonSerializationContext
	        );
				
		assertEquals(expected, actual);
	}

	// --- Integrationstest über echten ObjectMapper ---------------------------

	@Test
	@DisplayName("Integration: Serialisierung über ObjectMapper liefert gültiges, erwartetes JSON")
	void testSerialize_viaObjectMapper_producesExpectedJson() throws Exception {

		Value value = buildValue(DataType.STRING, "hallo", "=");

		   // Act
        String actual = GSON.toJson(value);

		assertEquals("\"f-=-s-hallo\"", actual);
	}

	// --- Hilfsmethoden ---------------------------------------------------

	/**
	 * Baut ein {@link Value}-Mock-Objekt mit dem gewünschten Typ, Wert und Rule, ohne von konkreten
	 * Value-Konstruktoren (die evtl. nicht jeden DataType, z.B. BINARY, abdecken) abhängig zu sein.
	 */
	private Value buildValue(DataType type, Object rawValue, String rule) {
		Value value = mock(Value.class);
		org.mockito.Mockito.when(value.getType()).thenReturn(type);
		org.mockito.Mockito.when(value.getValue()).thenReturn(rawValue);
		org.mockito.Mockito.when(value.getRule()).thenReturn(rule);

		switch (type) {
			case INTEGER -> org.mockito.Mockito.when(value.getIntegerValue()).thenReturn((Integer) rawValue);
			case LONG -> org.mockito.Mockito.when(value.getLongValue()).thenReturn((Long) rawValue);
			case DOUBLE -> org.mockito.Mockito.when(value.getDoubleValue()).thenReturn((Double) rawValue);
			case STRING -> org.mockito.Mockito.when(value.getStringValue()).thenReturn((String) rawValue);
			case BOOLEAN -> org.mockito.Mockito.when(value.getBooleanValue()).thenReturn((Boolean) rawValue);
			case BIGDECIMAL -> org.mockito.Mockito.when(value.getBigDecimalValue()).thenReturn((BigDecimal) rawValue);
			case INSTANT -> org.mockito.Mockito.when(value.getInstantValue()).thenReturn((Instant) rawValue);
			case ZONED -> org.mockito.Mockito.when(value.getZonedDateTimeValue()).thenReturn((ZonedDateTime) rawValue);
			case BINARY -> org.mockito.Mockito.when(value.getBinaryValue()).thenReturn((byte[]) rawValue);
			default -> throw new IllegalArgumentException("Unbekannter DataType in Testaufbau: " + type);
		}
		return value;
	}

}

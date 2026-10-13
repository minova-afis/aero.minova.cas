package aero.minova.cas.api.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
import org.mockito.ArgumentCaptor;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ValueJacksonSerializerTest {

	private final ValueJacksonSerializer serializer = new ValueJacksonSerializer();
	
	@BeforeEach
	public void setup() {
	}
		
	// --------------------------------------------------------------------------------------

	@Test
	@DisplayName("value == null -> schreibt null")
	void testSerialize_valueIsNull_writesNull() throws Exception {
		JsonGenerator generator = mock(JsonGenerator.class);
		SerializerProvider provider = mock(SerializerProvider.class);

		serializer.serialize(null, generator, provider);

		verify(generator).writeNull();
	}

	@Test
	@DisplayName("Value.getValue() == null -> schreibt null")
	void testSerialize_innerValueIsNull_writesNull() throws Exception {
		Value value = mock(Value.class);
		JsonGenerator generator = mock(JsonGenerator.class);
		SerializerProvider provider = mock(SerializerProvider.class);

		serializer.serialize(value, generator, provider);

		verify(generator).writeNull();
	}

	@Test
	@DisplayName("Value.getType() == null -> schreibt null")
	void testSerialize_typeIsNull_writesNull() throws Exception {
		Value value = mock(Value.class);
		org.mockito.Mockito.when(value.getValue()).thenReturn("irgendwas");
		org.mockito.Mockito.when(value.getType()).thenReturn(null);
		JsonGenerator generator = mock(JsonGenerator.class);
		SerializerProvider provider = mock(SerializerProvider.class);

		serializer.serialize(value, generator, provider);

		verify(generator).writeNull();
	}

	// --- Alle DataTypes ohne Rule ------------------------------------------

	@ParameterizedTest(name = "{0} -> {1}")
	@MethodSource("typeValueAndExpectedPrefix")
	@DisplayName("Serialisiert jeden DataType korrekt (ohne Rule)")
	void testSerialize_perType_noRule(DataType type, Object rawValue, String expectedRawJson) throws Exception {
		Value value = buildValue(type, rawValue, null);

		JsonGenerator generator = mock(JsonGenerator.class);
		SerializerProvider provider = mock(SerializerProvider.class);

		serializer.serialize(value, generator, provider);

		verify(generator).writeString(expectedRawJson);
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

		JsonGenerator generator = mock(JsonGenerator.class);
		serializer.serialize(value, generator, mock(SerializerProvider.class));

		verify(generator).writeString("f-=-n-5");
	}

	@Test
	@DisplayName("Rule 'like' wird zu ~ übersetzt")
	void testSerialize_ruleLikeIsTranslated() throws Exception {
		Value value = buildValue(DataType.STRING, "abc", "like");

		JsonGenerator generator = mock(JsonGenerator.class);
		serializer.serialize(value, generator, mock(SerializerProvider.class));

		verify(generator).writeString("f-~-s-abc");
	}

	@Test
	@DisplayName("Rule 'not like' wird zu !~ übersetzt")
	void testSerialize_ruleNotLikeIsTranslated() throws Exception {
		Value value = buildValue(DataType.STRING, "abc", "not like");

		JsonGenerator generator = mock(JsonGenerator.class);
		serializer.serialize(value, generator, mock(SerializerProvider.class));

		verify(generator).writeString("f-!~-s-abc");
	}

	@Test
	@DisplayName("Rule ohne Sonderfall (z.B. 'between()') bleibt unverändert")
	void testSerialize_ruleOtherPassesThrough() throws Exception {
		Value value = buildValue(DataType.INTEGER, 5, "between()");

		JsonGenerator generator = mock(JsonGenerator.class);
		serializer.serialize(value, generator, mock(SerializerProvider.class));

		verify(generator).writeString("f-between()-n-5");
	}

	@Test
	@DisplayName("Keine Rule -> kein f-...- Präfix")
	void testSerialize_noRule_noPrefixWritten() throws Exception {
		Value value = buildValue(DataType.INTEGER, 5, null);

		JsonGenerator generator = mock(JsonGenerator.class);
		serializer.serialize(value, generator, mock(SerializerProvider.class));

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		verify(generator).writeString(captor.capture());
		assertEquals("n-5", captor.getValue());
	}

	// --- Integrationstest über echten ObjectMapper ---------------------------

	//@Disabled("Nur mit @JsonSerialize(using = ValueJacksonSerializer.class) in Value")
	@Test
	@DisplayName("Integration: Serialisierung über ObjectMapper liefert gültiges, erwartetes JSON")
	void testSerialize_viaObjectMapper_producesExpectedJson() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(Value.class, serializer);
		mapper.addMixIn(Value.class, ValueJacksonMixin.class);	// Replace @JsonDeserialize and @JsonSerialize in Value
		mapper.registerModule(module);

		Value value = buildValue(DataType.STRING, "hallo", "=");

		String json = mapper.writeValueAsString(value);

		assertEquals("\"f-=-s-hallo\"", json);
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

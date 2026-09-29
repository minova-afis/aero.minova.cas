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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Tests für {@link ValueDeserializer}.
 *
 * <p>
 * {@code ValueDeserializer} hat (nach der Umstellung von Gson auf reines Jackson) keine
 * {@code @Autowired}-Abhängigkeiten mehr, daher ist kein {@code @SpringBootTest} nötig - ein reiner
 * JUnit5-Test mit einem manuell konfigurierten {@link ObjectMapper} genügt.
 * </p>
 */
public class ValueJacksonDeserializerTest {

	private ObjectMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(Value.class, new ValueJacksonDeserializer());
		mapper.addMixIn(Value.class, ValueJacksonMixin.class);	// Replace @JsonDeserialize and @JsonSerialize in Value
		mapper.registerModule(module);	
	}

	// --- Null-Fälle -------------------------------------------------------

	@Test
	@DisplayName("JSON null -> deserialisiert zu null")
	void testDeserialize_jsonNull_returnsNull() throws Exception {
		Value result = mapper.readValue("null", Value.class);
		assertNull(result);
	}

	// --- Alle DataTypes ohne Rule ------------------------------------------

	@ParameterizedTest(name = "{0} -> {1}")
	@MethodSource("rawJsonAndExpected")
	@DisplayName("Deserialisiert jeden DataType korrekt (ohne Rule)")
	void testDeserialize_perType_noRule(String rawJson, DataType expectedType, Object expectedValue) throws Exception {
		Value result = mapper.readValue(rawJson, Value.class);

		assertEquals(expectedType, result.getType());
		if (expectedType == DataType.BINARY) {
			assertArrayEquals((byte[]) expectedValue, (byte[]) result.getValue());
		} else {
			assertEquals(expectedValue, result.getValue());
		}
		assertNull(result.getRule());
	}

	private static Stream<Arguments> rawJsonAndExpected() {
		Instant instant = Instant.parse("2024-01-15T10:30:00Z");
		ZonedDateTime zoned = ZonedDateTime.of(2024, 1, 15, 10, 30, 0, 0, ZoneOffset.UTC);
		byte[] binary = { 1, 2, 3, 4 };

		return Stream.of(
				Arguments.of("\"n-42\"", DataType.INTEGER, 42),
				Arguments.of("\"l-123456789\"", DataType.LONG, 123456789L),
				Arguments.of("\"d-3.14\"", DataType.DOUBLE, 3.14),
				Arguments.of("\"s-hello world\"", DataType.STRING, "hello world"),
				Arguments.of("\"b-true\"", DataType.BOOLEAN, Boolean.TRUE),
				Arguments.of("\"m-199.99\"", DataType.BIGDECIMAL, BigDecimal.valueOf(199.99)),
				Arguments.of("\"i-" + instant + "\"", DataType.INSTANT, instant),
				Arguments.of("\"z-" + zoned + "\"", DataType.ZONED, zoned),
				Arguments.of("\"x-" + Base64.getEncoder().encodeToString(binary) + "\"", DataType.BINARY, binary));
	}

	// --- Rule-Handling ------------------------------------------------------

	@Test
	@DisplayName("Rule '=' wird korrekt zurückgelesen")
	void testDeserialize_ruleEquals() throws Exception {
		Value result = mapper.readValue("\"f-=-n-5\"", Value.class);

		assertEquals(DataType.INTEGER, result.getType());
		assertEquals(5, result.getIntegerValue());
		assertEquals("=", result.getRule());
	}

	@Test
	@DisplayName("~ wird zu Rule 'like' übersetzt")
	void testDeserialize_tildeIsTranslatedToLike() throws Exception {
		Value result = mapper.readValue("\"f-~-s-abc\"", Value.class);

		assertEquals(DataType.STRING, result.getType());
		assertEquals("abc", result.getStringValue());
		assertEquals("like", result.getRule());
	}

	@Test
	@DisplayName("!~ wird zu Rule 'not like' übersetzt")
	void testDeserialize_notTildeIsTranslatedToNotLike() throws Exception {
		Value result = mapper.readValue("\"f-!~-s-abc\"", Value.class);

		assertEquals(DataType.STRING, result.getType());
		assertEquals("abc", result.getStringValue());
		assertEquals("not like", result.getRule());
	}

	@Test
	@DisplayName("Rule ohne Sonderfall (z.B. 'between()') bleibt unverändert")
	void testDeserialize_ruleOtherPassesThrough() throws Exception {
		Value result = mapper.readValue("\"f-between()-n-5\"", Value.class);

		assertEquals(DataType.INTEGER, result.getType());
		assertEquals(5, result.getIntegerValue());
		assertEquals("between()", result.getRule());
	}

	@Test
	@DisplayName("Rule 'null' -> Wert wird verworfen, Ergebnis ist leerer String")
	void testDeserialize_ruleNull_discardsValue() throws Exception {
		Value result = mapper.readValue("\"f-null-x\"", Value.class);

		assertEquals(DataType.STRING, result.getType());
		assertEquals("", result.getStringValue());
		assertEquals("null", result.getRule());
	}

	@Test
	@DisplayName("Rule '!null' -> Wert wird verworfen, Ergebnis ist leerer String")
	void testDeserialize_ruleNotNull_discardsValue() throws Exception {
		Value result = mapper.readValue("\"f-!null-x\"", Value.class);

		assertEquals(DataType.STRING, result.getType());
		assertEquals("", result.getStringValue());
		assertEquals("!null", result.getRule());
	}

	@Test
	@DisplayName("Präfix 'r' (Legacy-Format) wird korrekt in Rule + String-Wert zerlegt")
	void testDeserialize_legacyRPrefix() throws Exception {
		Value result = mapper.readValue("\"r-customRule-myValue\"", Value.class);

		assertEquals(DataType.STRING, result.getType());
		assertEquals("myValue", result.getStringValue());
		assertEquals("customRule", result.getRule());
	}

	@Test
	@DisplayName("Unbekannter Typ-Präfix liefert null")
	void testDeserialize_unknownTypePrefix_returnsNull() throws Exception {
		Value result = mapper.readValue("\"q-irgendwas\"", Value.class);

		assertNull(result);
	}

	// --- Roundtrip mit dem Serializer ---------------------------------------

	@Test
	@DisplayName("Roundtrip: Serializer -> Deserializer liefert äquivalenten Value")
	void testRoundtrip_withSerializer() throws Exception {
		ObjectMapper roundtripMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(Value.class, new ValueJacksonSerializer());
		module.addDeserializer(Value.class, new ValueJacksonDeserializer());
		roundtripMapper.addMixIn(Value.class, ValueJacksonMixin.class);	// Replace @JsonDeserialize and @JsonSerialize in Value
		roundtripMapper.registerModule(module);

		Value original = new Value(BigDecimal.valueOf(42.5), "<=");

		String json = roundtripMapper.writeValueAsString(original);
		Value result = roundtripMapper.readValue(json, Value.class);

		assertEquals(original.getType(), result.getType());
		assertEquals(original.getBigDecimalValue(), result.getBigDecimalValue());
		assertEquals(original.getRule(), result.getRule());
	}

}

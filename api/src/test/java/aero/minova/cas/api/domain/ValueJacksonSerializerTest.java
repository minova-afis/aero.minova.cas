package aero.minova.cas.api.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ValueJacksonSerializerTest {

	private ObjectMapper objectMapper;
	
	@BeforeEach
	public void setup() {
		objectMapper = new ObjectMapper();
	}
	
	@Test
	void testValueOfTypeInteger() throws IOException {
		// given
		Value value = new Value(123);
		
		// when 
		String actual = objectMapper.writeValueAsString(value);
		String expected = "\"n-123\"";
		
		// then
		assertEquals(expected, actual);
	}

	@Test
	void testValueOfTypeLong() throws IOException {
		// given
		Value value = new Value(123L);
		
		// when 
		String actual = objectMapper.writeValueAsString(value);
		String expected =  "\"l-123\"";
		
		// then
		assertEquals(expected, actual);
	}

}

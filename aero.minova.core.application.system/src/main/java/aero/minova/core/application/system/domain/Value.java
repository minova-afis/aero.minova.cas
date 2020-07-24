package aero.minova.core.application.system.domain;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Der Wert kann auch null sein. Wenn dass nicht so wäre, könnten wir optionale Werte in einer {@link Table} nicht darstellen.
 * 
 * @author avots
 */
public class Value {
	private DataType type;
	private Object value;

	public Value(Integer integerValue) {
		type = DataType.INTEGER;
		setValue(requireNonNull(integerValue));
	}

	public Value(Long longValue) {
		type = DataType.LONG;
		setValue(longValue);
	}

	public Value(Boolean booleanValue) {
		type = DataType.BOOLEAN;
		setValue(booleanValue);
	}

	public Value(Double doubleValue) {
		type = DataType.DOUBLE;
		setValue(doubleValue);
	}

	public Value(String stringValue) {
		type = DataType.STRING;
		setValue(stringValue);
	}

	public Value(Instant instantValue) {
		type = DataType.INSTANT;
		setValue(instantValue);
	}

	public Value(ZonedDateTime zonedDateTimeValue) {
		type = DataType.ZONED;
		setValue(zonedDateTimeValue);
	}

	public Value(LocalDate dateValue) {
		type = DataType.DATE;
		setValue(dateValue);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(Class<T> type) {
		return (T) value;
	}

	public DataType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Integer getIntegerValue() {
		return type == DataType.INTEGER ? (Integer) value : null;
	}

	public String getStringValue() {
		return type == DataType.STRING ? (String) value : null;
	}

	public Double getDoubleValue() {
		return type == DataType.DOUBLE ? (Double) value : null;
	}

	public Instant getInstantValue() {
		return type == DataType.INSTANT ? (Instant) value : null;
	}

	public ZonedDateTime getZonedDateTimeValue() {
		return type == DataType.ZONED ? (ZonedDateTime) value : null;
	}

	public Boolean getBooleanValue() {
		return type == DataType.BOOLEAN ? (Boolean) value : null;
	}
}
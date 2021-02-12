package aero.minova.core.application.system.domain;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Der Wert kann auch null sein. Wenn dass nicht so wäre, könnten wir optionale Werte in einer {@link Table} nicht darstellen.
 * 
 * @author avots
 */
public class Value {
	private DataType type;
	private Object value;
	private String rule;

	public Value(Integer integerValue) {
		type = DataType.INTEGER;
		rule = null;
		setValue(requireNonNull(integerValue));
	}

	public Value(Long longValue) {
		type = DataType.LONG;
		rule = null;
		setValue(longValue);
	}

	public Value(Boolean booleanValue) {
		type = DataType.BOOLEAN;
		rule = null;
		setValue(booleanValue);
	}

	public Value(Double doubleValue) {
		type = DataType.DOUBLE;
		rule = null;
		setValue(doubleValue);
	}

	public Value(String stringValue) {
		type = DataType.STRING;
		rule = null;
		setValue(stringValue);
	}

	public Value(Instant instantValue) {
		type = DataType.INSTANT;
		rule = null;
		setValue(instantValue);

	}

	public Value(ZonedDateTime zonedDateTimeValue) {
		type = DataType.ZONED;
		rule = null;
		setValue(zonedDateTimeValue);
	}

	public Value(Integer integerValue, String rule) {
		type = DataType.INTEGER;
		this.rule = rule;
		setValue(requireNonNull(integerValue));
	}

	public Value(Long longValue, String rule) {
		type = DataType.LONG;
		this.rule = rule;
		setValue(longValue);
	}

	public Value(Boolean booleanValue, String rule) {
		type = DataType.BOOLEAN;
		this.rule = rule;
		setValue(booleanValue);
	}

	public Value(Double doubleValue, String rule) {
		type = DataType.DOUBLE;
		this.rule = rule;
		setValue(doubleValue);
	}

	public Value(String stringValue, String rule) {
		type = DataType.STRING;
		this.rule = rule;
		setValue(stringValue);
	}

	public Value(Instant instantValue, String rule) {
		type = DataType.INSTANT;
		this.rule = rule;
		setValue(instantValue);
	}

	public Value(ZonedDateTime zonedDateTimeValue, String rule) {
		type = DataType.ZONED;
		this.rule = rule;
		setValue(zonedDateTimeValue);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(Class<T> type) {
		return (T) value;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
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

	public Long getLongValue() {
		return type == DataType.LONG ? (Long) value : null;
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
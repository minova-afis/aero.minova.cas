package aero.minova.cas.api.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Der Wert kann auch null sein. Wenn dass nicht so wäre, könnten wir optionale Werte in einer {@link Table} nicht darstellen.
 *
 * @author avots
 */
@JsonDeserialize(using = ValueDeserializer.class)
@JsonSerialize(using = ValueSerializer.class)
public class Value implements Serializable {
	private static final long serialVersionUID = 202106161639L;
	private DataType type;
	private Object value;
	private String rule;

	/**
	 * Der leere Konstruktor. Wird normalerweise nicht verwendet. Wird aber benötigt, damit die Hauptklasse von Serializable erben kann.
	 */
	public Value() {

	}

	/**
	 * Der Standard-Konstruktor für Integer-Values.
	 *
	 * @param integerValue
	 *            Der Wert als Integer.
	 * @param rule
	 *            Gültige Rules sind entweder null oder folgende Strings: "<>", "<=", ">=", "<", ">", "=", "between()", "in()", "!~", "~", "null", "!null"
	 */
	public Value(Integer integerValue, String rule) {
		type = DataType.INTEGER;
		this.rule = rule;
		setValue(integerValue);
	}

	/**
	 * Der Standard-Konstruktor für Integer-Values, ohne rule
	 *
	 * @param integerValue
	 *            Der Wert als Integer.
	 */
	public Value(Integer integerValue) {
		this(integerValue, null);
	}

	/**
	 * Der Standard-Konstruktor für Long-Values.
	 *
	 * @param longValue
	 *            Der Wert als Long.
	 * @param rule
	 *            Gültige Rules sind entweder null oder folgende Strings: "<>", "<=", ">=", "<", ">", "=", "between()", "in()", "!~", "~", "null", "!null"
	 */
	public Value(Long longValue, String rule) {
		type = DataType.LONG;
		this.rule = rule;
		setValue(longValue);
	}

	/**
	 * Der Standard-Konstruktor für Long-Values, ohne rule
	 *
	 * @param longValue
	 *            Der Wert als Long.
	 */
	public Value(Long longValue) {
		this(longValue, null);
	}

	/**
	 * @param booleanValue
	 * @param rule
	 *            Gültige Rules sind entweder null oder folgende Strings: "<>", "<=", ">=", "<", ">", "=", "between()", "in()", "!~", "~", "null", "!null"
	 */
	public Value(Boolean booleanValue, String rule) {
		type = DataType.BOOLEAN;
		this.rule = rule;
		setValue(booleanValue);
	}

	/**
	 * @param booleanValue
	 */
	public Value(Boolean booleanValue) {
		this(booleanValue, null);
	}

	/**
	 * Der Standard-Konstruktor für Double-Values.
	 *
	 * @param doubleValue
	 *            Der Wert als Double.
	 * @param rule
	 *            Gültige Rules sind entweder null oder folgende Strings: "<>", "<=", ">=", "<", ">", "=", "between()", "in()", "!~", "~", "null", "!null"
	 */
	public Value(Double doubleValue, String rule) {
		type = DataType.DOUBLE;
		this.rule = rule;
		setValue(doubleValue);
	}

	public Value(Double doubleValue) {
		this(doubleValue, null);
	}

	/**
	 * Der Standard-Konstruktor für String-Values.
	 *
	 * @param stringValue
	 *            Der Wert als String.
	 * @param rule
	 *            Gültige Rules sind entweder null oder folgende Strings: "<>", "<=", ">=", "<", ">", "=", "between()", "in()", "!~", "~", "null", "!null"
	 */
	public Value(String stringValue, String rule) {
		type = DataType.STRING;
		this.rule = rule;
		setValue(stringValue);
	}

	public Value(String stringValue) {
		this(stringValue, null);
	}

	/**
	 * Der Standard-Konstruktor für Instant-Values.
	 *
	 * @param instantValue
	 *            Der Wert als Instant.
	 * @param rule
	 *            Gültige Rules sind entweder null oder folgende Strings: "<>", "<=", ">=", "<", ">", "=", "between()", "in()", "!~", "~", "null", "!null"
	 */
	public Value(Instant instantValue, String rule) {
		type = DataType.INSTANT;
		this.rule = rule;
		setValue(instantValue);
	}

	public Value(Instant instantValue) {
		this(instantValue, null);
	}

	/**
	 * Der Standard-Konstruktor für ZonedDateTime-Values.
	 *
	 * @param zonedDateTimeValue
	 *            Der Wert als ZonedDateTime.
	 * @param rule
	 *            Gültige Rules sind entweder null oder folgende Strings: "<>", "<=", ">=", "<", ">", "=", "between()", "in()", "!~", "~", "null", "!null"
	 */
	public Value(ZonedDateTime zonedDateTimeValue, String rule) {
		type = DataType.ZONED;
		this.rule = rule;
		setValue(zonedDateTimeValue);
	}

	public Value(ZonedDateTime zonedDateTimeValue) {
		this(zonedDateTimeValue, null);
	}

	/**
	 * Der Standard-Konstruktor für BigDecimal-Values. Wird für Money verwendet.
	 *
	 * @param decimalValue
	 *            Der Wert als BigDecimal.
	 * @param rule
	 *            Gültige Rules sind entweder null oder folgende Strings: "<>", "<=", ">=", "<", ">", "=", "between()", "in()", "!~", "~", "null", "!null"
	 */
	public Value(BigDecimal decimalValue, String rule) {
		type = DataType.BIGDECIMAL;
		this.rule = rule;
		setValue(decimalValue);
	}

	public Value(BigDecimal decimalValue) {
		this(decimalValue, null);
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(Class<T> type) {
		return (T) value;
	}

	public DataType getType() {
		return type;
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

	public BigDecimal getBigDecimalValue() {
		return type == DataType.BIGDECIMAL ? (BigDecimal) value : null;
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

	@Override
	public String toString() {
		return "Value [type=" + type + ", value=" + value + ", rule=" + rule + "]";
	}

}

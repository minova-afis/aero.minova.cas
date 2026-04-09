package aero.minova.cas.api.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

public enum DataType {
	BOOLEAN(Boolean.class),
	DOUBLE(Double.class),
	INSTANT(Timestamp.class),
	INTEGER(Integer.class),
	LONG(Long.class),
	STRING(String.class),
	ZONED(Timestamp.class),
	BIGDECIMAL(BigDecimal.class),
	BINARY(byte[].class);
	
	/** Data type that is used to pass the value to JDBC driver
	 */
	public final Class<?> sqlDataType;
	
	private DataType(Class<?> sqlDataType) {
		this.sqlDataType = sqlDataType;
	}
}
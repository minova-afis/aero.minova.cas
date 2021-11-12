package aero.minova.core.application.system.sql;

import static java.time.ZoneId.systemDefault;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.slf4j.Logger;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.ProcedureException;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;

public class SqlUtils {

	private SqlUtils() {
		throw new UnsupportedOperationException();
	}

	public static String toSqlString(Value value) {
		if (value.getType() == DataType.BOOLEAN) {
			if (value.getBooleanValue()) {
				return "1";
			} else {
				return "0";
			}
		} else {
			return value.getValue().toString();
		}
	}

	public static Row convertSqlResultToRow(Table outputTable, ResultSet sqlSet, Logger logger, Object conversionUser) throws ProcedureException {
		try {
			Row row = new Row();
			for (Column column : outputTable.getColumns()) {
				Value value;
				if (column.getType() == DataType.STRING) {
					value = new Value(sqlSet.getString(column.getName()), null);
				} else if (column.getType() == DataType.INTEGER) {
					value = new Value(sqlSet.getInt(column.getName()), null);
				} else if (column.getType() == DataType.BOOLEAN) {
					value = new Value(sqlSet.getBoolean(column.getName()), null);
				} else if (column.getType() == DataType.BIGDECIMAL) {
					value = new Value(sqlSet.getBigDecimal(column.getName()), null);
				} else if (column.getType() == DataType.DOUBLE) {
					value = new Value(sqlSet.getDouble(column.getName()), null);
				} else if (column.getType() == DataType.INSTANT) {
					if (sqlSet.getTimestamp(column.getName()) == null) {
						value = new Value((Instant) null, null);
					} else {
						value = new Value(sqlSet.getTimestamp(column.getName()).toInstant(), null);
					}
				} else if (column.getType() == DataType.LONG) {
					value = new Value(sqlSet.getLong(column.getName()), null);
				} else if (column.getType() == DataType.ZONED) {
					if (sqlSet.getTimestamp(column.getName()) == null) {
						value = new Value((ZonedDateTime) null, null);
					} else {
						value = new Value(sqlSet.getTimestamp(column.getName()).toInstant().atZone(systemDefault()), null);
					}
				} else {
					logger.warn(conversionUser.getClass().getSimpleName() + ": Ausgabe-Typ wird nicht unterstützt. Er wird als String dargestellt: "
							+ column.getType());
					value = new Value(sqlSet.getString(column.getName()), null);
				}
				// getInt und getDouble geben bei NULL in der Datenbank einfach den Wert 0 zurück, was nicht richtig ist!
				if (sqlSet.wasNull()) {
					value = null;
				}
				row.addValue(value);
			}
			return row;
		} catch (Exception e) {
			throw new ProcedureException("msg.ConvertTableError");
		}
	}

	public static Value parseSqlParameter(CallableStatement statement, int index, Column column) {
		try {
			Value value;
			if (column.getType() == DataType.STRING) {
				value = new Value(statement.getString(index), null);
			} else if (column.getType() == DataType.INTEGER) {
				value = new Value(statement.getInt(index), null);
			} else if (column.getType() == DataType.BOOLEAN) {
				value = new Value(statement.getBoolean(index), null);
			} else if (column.getType() == DataType.DOUBLE) {
				value = new Value(statement.getDouble(index), null);
			} else if (column.getType() == DataType.BIGDECIMAL) {
				value = new Value(statement.getBigDecimal(index), null);
			} else if (column.getType() == DataType.INSTANT) {
				value = new Value(//
						Optional.ofNullable(statement.getTimestamp(index))//
								.map(e -> e.toInstant())//
								.orElse(null),
						null);
			} else if (column.getType() == DataType.LONG) {
				value = new Value(statement.getLong(index), null);
			} else if (column.getType() == DataType.ZONED) {
				value = new Value(//
						Optional.ofNullable(statement.getTimestamp(index))//
								.map(e -> e.toInstant().atZone(systemDefault()))//
								.orElse(null),
						null);
			} else {
				throw new UnsupportedOperationException();
			}

			// getInt und getDouble geben bei NULL in der Datenbank einfach den Wert 0 zurück, was nicht richtig ist!
			if (statement.wasNull()) {
				value = null;
			}
			return value;
		} catch (SQLException e) {
			try {
				throw new RuntimeException("msg.SqlParameterParseError %" + index + " %" + statement.getString(index));
			} catch (SQLException e2) {
				throw new RuntimeException(e);
			}
		}
	}

}

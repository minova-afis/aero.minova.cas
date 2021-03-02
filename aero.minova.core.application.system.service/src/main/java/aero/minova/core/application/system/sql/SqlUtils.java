package aero.minova.core.application.system.sql;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.slf4j.Logger;

import aero.minova.core.application.system.CoreApplicationSystemApplication;
import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
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

	public static Row convertSqlResultToRow(Table outputTable, ResultSet sqlSet, Logger logger, Object conversionUser) {
		try {
			Row row = new Row();
			for (Column column : outputTable.getColumns()) {
				if (column.getType() == DataType.STRING) {
					row.addValue(new Value(sqlSet.getString(column.getName()), null));
				} else if (column.getType() == DataType.INTEGER) {
					row.addValue(new Value(sqlSet.getInt(column.getName()), null));
				} else if (column.getType() == DataType.BOOLEAN) {
					row.addValue(new Value(sqlSet.getBoolean(column.getName()), null));
				} else if (column.getType() == DataType.DOUBLE) {
					row.addValue(new Value(sqlSet.getDouble(column.getName()), null));
				} else if (column.getType() == DataType.INSTANT) {
					if (sqlSet.getTimestamp(column.getName()) == null) {
						row.addValue(new Value((Instant) null, null));
					} else {
						row.addValue(new Value(sqlSet.getTimestamp(column.getName()).toInstant(), null));
					}
				} else if (column.getType() == DataType.LONG) {
					row.addValue(new Value(sqlSet.getLong(column.getName()), null));
				} else if (column.getType() == DataType.ZONED) {
					if (sqlSet.getTimestamp(column.getName()) == null) {
						row.addValue(new Value((ZonedDateTime) null, null));
					} else {
						row.addValue(new Value(sqlSet.getTimestamp(column.getName()).toInstant().atZone(CoreApplicationSystemApplication.DEFAULT_ZONE), null));
					}
				} else {
					logger.warn(conversionUser.getClass().getSimpleName() + ": Ausgabe-Typ wird nicht unterstützt. Er wird als String dargestellt: "
							+ column.getType());
					row.addValue(new Value(sqlSet.getString(column.getName()), null));
				}
			}
			return row;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static Value parseSqlParameter(CallableStatement statement, int index, Column column) {
		try {
			if (column.getType() == DataType.STRING) {
				return new Value(statement.getString(index), null);
			} else if (column.getType() == DataType.INTEGER) {
				return new Value(statement.getInt(index), null);
			} else if (column.getType() == DataType.BOOLEAN) {
				return new Value(statement.getBoolean(index), null);
			} else if (column.getType() == DataType.DOUBLE) {
				return new Value(statement.getDouble(index), null);
			} else if (column.getType() == DataType.INSTANT) {
				return new Value(//
						Optional.ofNullable(statement.getTimestamp(index))//
								.map(e -> e.toInstant())//
								.orElse(null),
						null);
			} else if (column.getType() == DataType.LONG) {
				return new Value(statement.getLong(index), null);
			} else if (column.getType() == DataType.ZONED) {
				return new Value(//
						Optional.ofNullable(statement.getTimestamp(index))//
								.map(e -> e.toInstant().atZone(CoreApplicationSystemApplication.DEFAULT_ZONE))//
								.orElse(null),
						null);
			} else {
				throw new UnsupportedOperationException();
			}
		} catch (SQLException e) {
			try {
				throw new RuntimeException("Could not parse SQL Parameter " + index + " with value: " + statement.getString(index), e);
			} catch (SQLException e2) {
				throw new RuntimeException(e);
			}
		}
	}

}

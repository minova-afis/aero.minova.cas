package aero.minova.core.application.system.sql;

import aero.minova.core.application.system.domain.*;
import org.slf4j.Logger;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;

import static java.time.ZoneId.systemDefault;

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
					row.addValue(new Value(sqlSet.getString(column.getName())));
				} else if (column.getType() == DataType.INTEGER) {
					row.addValue(new Value(sqlSet.getInt(column.getName())));
				} else if (column.getType() == DataType.BOOLEAN) {
					row.addValue(new Value(sqlSet.getBoolean(column.getName())));
				} else if (column.getType() == DataType.DOUBLE) {
					row.addValue(new Value(sqlSet.getDouble(column.getName())));
				} else if (column.getType() == DataType.INSTANT) {
					if (sqlSet.getTimestamp(column.getName()) == null) {
						row.addValue(new Value((Instant) null));
					} else {
						row.addValue(new Value(sqlSet.getTimestamp(column.getName()).toInstant()));
					}
				} else if (column.getType() == DataType.LONG) {
					row.addValue(new Value(sqlSet.getLong(column.getName())));
				} else if (column.getType() == DataType.ZONED) {
					if (sqlSet.getTimestamp(column.getName()) == null) {
						row.addValue(new Value((ZonedDateTime) null));
					} else {
						row.addValue(new Value(sqlSet.getTimestamp(column.getName()).toInstant().atZone(systemDefault())));
					}
				} else {
					logger.warn(conversionUser.getClass().getSimpleName() + ": Ausgabe-Typ wird nicht unterstützt. Er wird als String dargestellt: "
							+ column.getType());
					row.addValue(new Value(sqlSet.getString(column.getName())));
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
				return new Value(statement.getString(index));
			} else if (column.getType() == DataType.INTEGER) {
				return new Value(statement.getInt(index));
			} else if (column.getType() == DataType.BOOLEAN) {
				return new Value(statement.getBoolean(index));
			} else if (column.getType() == DataType.DOUBLE) {
				return new Value(statement.getDouble(index));
			} else if (column.getType() == DataType.INSTANT) {
				return new Value(//
						Optional.ofNullable(statement.getTimestamp(index))//
								.map(e -> e.toInstant())//
								.orElse(null));
			} else if (column.getType() == DataType.LONG) {
				return new Value(statement.getLong(index));
			} else if (column.getType() == DataType.ZONED) {
				return new Value(//
						Optional.ofNullable(statement.getTimestamp(index))//
								.map(e -> e.toInstant().atZone(systemDefault()))//
								.orElse(null));
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

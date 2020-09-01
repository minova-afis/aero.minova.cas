package aero.minova.core.application.system.sql;

import java.sql.ResultSet;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Predicate;

import org.slf4j.Logger;

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
		return convertSqlResultToRow(outputTable, sqlSet, logger, conversionUser, c -> true);
	}

	public static Row convertSqlResultToRow(Table outputTable, ResultSet sqlSet, Logger logger, Object conversionUser, Predicate<Column> columnSelector) {
		try {
			Row row = new Row();
			for (Column column : outputTable.getColumns()) {
				if (columnSelector.test(column)) {
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
							row.addValue(new Value(sqlSet.getTimestamp(column.getName()).toInstant().atZone(ZoneId.systemDefault())));
						}
					} else {
						logger.warn(conversionUser.getClass().getSimpleName() + ": Ausgabe-Typ wird nicht unterstützt. Er wird als String dargestellt: "
								+ column.getType());
						row.addValue(new Value(sqlSet.getString(column.getName())));
					}
				}
			}
			return row;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

}

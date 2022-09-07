package aero.minova.cas.sql;

import static java.time.ZoneId.systemDefault;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;

import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.ProcedureException;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import lombok.val;

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

	public static Table convertSqlResultToTable(Table inputTable, ResultSet sqlSet, Logger logger, Object conversionUser) {
		try {
			Table outputTable = new Table();
			outputTable.setName(inputTable.getName());
			outputTable.setColumns(//
					inputTable.getColumns().stream()//
							.filter(column -> !Objects.equals(column.getName(), Column.AND_FIELD_NAME))//
							.collect(Collectors.toList()));
			while (sqlSet.next()) {
				outputTable.addRow(SqlUtils.convertSqlResultToRow(outputTable, sqlSet, logger, conversionUser));
			}
			return outputTable;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Das Prepared Statement wird mit den dafür vorgesehenen Parametern befüllt. Diese werden aus der übergebenen inputTable gezogen.
	 *
	 * @param inputTable
	 *            die Table, welche vom getIndexView aufgerufen wurde
	 * @param preparedStatement
	 *            das Prepared Statement, welches nur noch befüllt werden muss
	 * @param query
	 *            Das bereits fertig aufgebaute Sql Statement, welches statt der Werte '?' enthält. Diese werden hier 'ersetzt'.
	 * @param sb
	 *            Ein StringBuilder zum Loggen der inputParameter.
	 * @param Logger
	 *            Ein Logger, welcher bei Fehlern die Exception loggen kann.
	 * @return das befüllte, ausführbare Prepared Statement
	 */
	public static PreparedStatement fillPreparedViewString(Table inputTable, CallableStatement preparedStatement, String query, StringBuilder sb,
			Logger logger) {
		int parameterOffset = 1;
		sb.append(query);

		List<Value> inputValues = new ArrayList<>();
		for (Row row : inputTable.getRows()) {
			for (int i = 0; i < row.getValues().size(); i++) {
				// nur die Values von den Spalten, welche nicht die AND_FIELD Spalte ist, interessiert uns
				if (!inputTable.getColumns().get(i).getName().equals(Column.AND_FIELD_NAME)) {
					inputValues.add(row.getValues().get(i));
				}
			}
		}
		for (int i = 0; i < inputValues.size(); i++) {
			try {
				val iVal = inputValues.get(i);
				if (iVal != null) {
					val rule = iVal.getRule();
					String stringValue = iVal.getValue() + "";
					if (rule == null) {
						if (!stringValue.trim().isEmpty()) {
							sb.append(" ; Position: " + (i + parameterOffset) + ", Value:" + stringValue);
							preparedStatement.setString(i + parameterOffset, stringValue);
						} else {
							// i tickt immer eins hoch, selbst wenn ein Value den Wert 'null', '' hat
							// damit die Position beim Einfügen also stimmt, muss parameterOffset um 1 verringert werden
							parameterOffset--;
						}
					} else if (rule.contains("in")) {
						List<String> inBetweenValues;
						inBetweenValues = Stream.of(iVal.getStringValue().split(","))//
								.collect(Collectors.toList());
						for (String string : inBetweenValues) {
							sb.append(" ; Position: " + (i + parameterOffset) + ", Value:" + string);
							preparedStatement.setString(i + parameterOffset, string);
							parameterOffset++;
						}
						// i zählt als nächstes hoch, deswegem muss parameterOffset wieder um 1 verringert werden
						parameterOffset--;
					} else if (rule.contains("between")) {
						List<String> inBetweenValues;
						inBetweenValues = Stream.of(iVal.getStringValue().split(","))//
								.collect(Collectors.toList());
						// bei between vertrauen wir nicht darauf, dass der Nutzer wirklich nur zwei Werte einträgt,
						// sondern nehmen den ersten und den letzten Wert
						sb.append(" ; Position: " + (i + parameterOffset) + ", Value:" + inBetweenValues.get(0));
						preparedStatement.setString(i + parameterOffset, inBetweenValues.get(0));
						parameterOffset++;
						sb.append(" ; Position: " + (i + parameterOffset) + ", Value:" + inBetweenValues.get(inBetweenValues.size() - 1));
						preparedStatement.setString(i + parameterOffset, inBetweenValues.get(inBetweenValues.size() - 1));
					} else {
						if (!stringValue.trim().isEmpty()) {
							sb.append(" ; Position: " + (i + parameterOffset) + ", Value:" + stringValue);
							preparedStatement.setString(i + parameterOffset, stringValue);
						} else {
							parameterOffset--;
						}
					}
				} else {
					parameterOffset--;
				}
			} catch (Exception e) {
				logger.error("Statement could not be filled: " + sb.toString(), e);
				throw new RuntimeException("msg.ParseError %" + (i + parameterOffset));
			}
		}
		sb.append("\n");
		return preparedStatement;
	}
}

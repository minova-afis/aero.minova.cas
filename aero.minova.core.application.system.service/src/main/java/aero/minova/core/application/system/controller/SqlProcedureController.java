package aero.minova.core.application.system.controller;

import static aero.minova.core.application.system.domain.OutputType.OUTPUT;
import static aero.minova.core.application.system.sql.SqlUtils.convertSqlResultToRow;
import static aero.minova.core.application.system.sql.SqlUtils.parseSqlParameter;
import static java.sql.Types.VARCHAR;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.SqlProcedureResult;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;
import aero.minova.core.application.system.sql.ExecuteStrategy;
import aero.minova.core.application.system.sql.SqlUtils;
import aero.minova.core.application.system.sql.SystemDatabase;
import lombok.val;

@RestController
public class SqlProcedureController {
	@Autowired
	SystemDatabase systemDatabase;
	Logger logger = LoggerFactory.getLogger(SqlViewController.class);
	private final ObjectMapper objectMapper = new ObjectMapper();

	@PostMapping(value = "data/procedure", produces = "application/json")
	public SqlProcedureResult executeProcedure(@RequestBody Table inputTable) {
		try {
			val preparedStatement = systemDatabase.connection().prepareCall(prepareProcedureString(inputTable));
			range(0, inputTable.getRows().get(0).getValues().size())//
					.forEach(i -> {
						try {
							val iVal = inputTable.getRows().get(0).getValues().get(i);
							val type = inputTable.getColumns().get(i).getType();
							if (iVal == null) {
								if (type == DataType.BOOLEAN) {
									preparedStatement.setObject(i + 1, null, Types.BOOLEAN);
								} else if (type == DataType.DOUBLE) {
									preparedStatement.setObject(i + 1, null, Types.DOUBLE);
								} else if (type == DataType.INSTANT) {
									preparedStatement.setObject(i + 1, null, Types.TIMESTAMP);
								} else if (type == DataType.INTEGER) {
									preparedStatement.setObject(i + 1, null, Types.INTEGER);
								} else if (type == DataType.LONG) {
									preparedStatement.setObject(i + 1, null, Types.DOUBLE);
								} else if (type == DataType.STRING) {
									preparedStatement.setObject(i + 1, null, Types.NVARCHAR);
								} else if (type == DataType.ZONED) {
									preparedStatement.setObject(i + 1, null, Types.TIMESTAMP);
								} else {
									throw new IllegalArgumentException("Unknown type: " + type.name());
								}
							} else {
								if (type == DataType.BOOLEAN) {
									preparedStatement.setBoolean(i + 1, iVal.getBooleanValue());
								} else if (type == DataType.DOUBLE) {
									preparedStatement.setDouble(i + 1, iVal.getDoubleValue());
								} else if (type == DataType.INSTANT) {
									preparedStatement.setTimestamp(i + 1, Timestamp.from(iVal.getInstantValue()));
								} else if (type == DataType.INTEGER) {
									preparedStatement.setInt(i + 1, iVal.getIntegerValue());
								} else if (type == DataType.LONG) {
									preparedStatement.setDouble(i + 1, Double.valueOf(iVal.getLongValue()));
								} else if (type == DataType.STRING) {
									preparedStatement.setString(i + 1, iVal.getStringValue());
								} else if (type == DataType.ZONED) {
									preparedStatement.setTimestamp(i + 1, Timestamp.from(iVal.getZonedDateTimeValue().toInstant()));
								} else {
									throw new IllegalArgumentException("Unknown type: " + type.name());
								}
							}
							if (inputTable.getColumns().get(i).getOutputType() == OUTPUT) {
								preparedStatement.registerOutParameter(i + 1, Types.VARCHAR);
							}
						} catch (Exception e) {
							throw new RuntimeException("Could not parse input parameter with index:" + i, e);
						}
					});
			val result = new SqlProcedureResult();
			val hasResultSet = preparedStatement.execute();
			if (hasResultSet) {
				val sqlResultSet = preparedStatement.getResultSet();
				val resultSet = new Table();
				result.setResultSet(resultSet);
				resultSet.setName("resultSet");
				val metaData = sqlResultSet.getMetaData();
				resultSet.setColumns(//
						range(0, metaData.getColumnCount()).mapToObj(i -> {
							try {
								val type = metaData.getColumnType(i);
								val name = metaData.getColumnName(i);
								if (type == Types.BOOLEAN) {
									return new Column(name, DataType.BOOLEAN);
								} else if (type == Types.DOUBLE) {
									return new Column(name, DataType.DOUBLE);
								} else if (type == Types.TIMESTAMP) {
									return new Column(name, DataType.INSTANT);
								} else if (type == Types.INTEGER) {
									return new Column(name, DataType.INTEGER);
								} else if (type == Types.DOUBLE) {
									return new Column(name, DataType.DOUBLE);
								} else if (type == Types.VARCHAR) {
									return new Column(name, DataType.STRING);
								} else {
									throw new UnsupportedOperationException("Unsupported result set type: " + i);
								}
							} catch (SQLException e) {
								throw new RuntimeException(e);
							}
						}).collect(toList()));
				while (sqlResultSet.next()) {
					resultSet.addRow(//
							convertSqlResultToRow(resultSet//
									, sqlResultSet//
									, logger//
									, this));
				}
			}
			val hasOutputParameters = inputTable//
					.getColumns()//
					.stream()//
					.anyMatch(c -> c.getOutputType() == OUTPUT);
			if (hasOutputParameters) {
				val outputParameters = new Table();
				result.setOutputParameters(outputParameters);
				outputParameters.setName("outputParameters");
				val outputColumnsMapping = inputTable//
						.getColumns()//
						.stream()//
						.map(c -> c.getOutputType() == OUTPUT)//
						.collect(toList());
				val outputValues = new Row();
				outputParameters.addRow(outputValues);
				range(1, inputTable.getColumns().size())//
						.forEach(i -> {
							if (outputColumnsMapping.get(i)) {
								outputValues.addValue(parseSqlParameter(preparedStatement, i + 1, inputTable.getColumns().get(i)));
							} else {
								outputValues.addValue(null);
							}
						});
			}
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sollte durch "data/procedure" ersetzt werden.
	 * 
	 * @param inputTable
	 * @return
	 */
	@Deprecated
	@PostMapping(value = "data/procedure-with-return-code", produces = "application/json")
	public Table executeProcedureWithReturnCode(@RequestBody Table inputTable) {
		try {
			Set<ExecuteStrategy> executeStrategies = new HashSet<>();
			executeStrategies.add(ExecuteStrategy.RETURN_CODE_IS_ERROR_IF_NOT_0);
			val preparedStatement = systemDatabase.connection().prepareCall(prepareProcedureString(inputTable, executeStrategies));
			range(0, inputTable.getRows().get(0).getValues().size())//
					.forEach(i -> {
						try {
							val iVal = inputTable.getRows().get(0).getValues().get(i);
							if (iVal == null) {
								// 2 Wird verwendet, da der erste Parameter der return code ist.
								preparedStatement.setNull(i + 2, VARCHAR);
							} else {
								preparedStatement.setString(i + 2, SqlUtils.toSqlString(iVal));
							}
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					});
			preparedStatement.registerOutParameter(1, Types.INTEGER);
			preparedStatement.execute();
			val returnCode = preparedStatement.getInt(1);

			val outputTable = new Table();
			outputTable.setName(inputTable.getName());
			outputTable.setColumns(Arrays.asList(new Column("ReturnCode", DataType.INTEGER)));

			Row row = new Row();
			row.addValue(new Value(returnCode));
			outputTable.addRow(row);
			return outputTable;
		} catch (SQLException e) {
			try {
				logger.error("Could not execute \"" + inputTable.getName() + "\" with argument: " + objectMapper.writeValueAsString(e), e);
			} catch (JsonProcessingException e1) {
				logger.error("Logging failed: ", e1);
			}
			val outputTable = new Table();
			outputTable.setName(inputTable.getName());
			outputTable.setColumns(Arrays.asList(new Column("ReturnCode", DataType.INTEGER)));

			Row row = new Row();
			row.addValue(new Value(0));
			outputTable.addRow(row);
			return outputTable;
		}
	}

	String prepareProcedureString(Table params) {
		return prepareProcedureString(params, ExecuteStrategy.STANDARD);
	}

	/**
	 * Bereitet einen Prozedur-String vor
	 * 
	 * @param name
	 * @param fds
	 * @param strategy
	 * @return
	 * @throws IllegalArgumentException
	 */
	String prepareProcedureString(Table params, Set<ExecuteStrategy> strategy) throws IllegalArgumentException {
		if (params.getName() == null || params.getName().trim().length() == 0) {
			throw new IllegalArgumentException("Cannot prepare procedure with NULL name");
		}
		final int paramCount = params.getColumns().size();
		final boolean returnRequired = ExecuteStrategy.returnRequired(strategy);

		final StringBuffer sb = new StringBuffer();
		sb.append('{').append(returnRequired ? "? = call " : "call ").append(params.getName()).append("(");
		for (int i = 0; i < paramCount; i++) {
			sb.append(i == 0 ? "?" : ",?");
		}
		sb.append(")}");
		return sb.toString();
	}

}

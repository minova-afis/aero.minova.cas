package aero.minova.core.application.system.controller;

import static aero.minova.core.application.system.domain.OutputType.OUTPUT;
import static aero.minova.core.application.system.sql.SqlUtils.convertSqlResultToRow;
import static aero.minova.core.application.system.sql.SqlUtils.parseSqlParameter;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.SqlProcedureResult;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.sql.ExecuteStrategy;
import aero.minova.core.application.system.sql.SystemDatabase;
import aero.minova.trac.integration.controller.TracController;
import lombok.val;

@RestController
public class SqlProcedureController {
	@Autowired
	SystemDatabase systemDatabase;
	Logger logger = LoggerFactory.getLogger(SqlViewController.class);

	@Autowired
	TracController trac;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@PostMapping(value = "data/procedure", produces = "application/json")
	public SqlProcedureResult executeProcedure(@RequestBody Table inputTable) throws SQLException {
		if ("Ticket".equals(inputTable.getName())) {
			val result = new SqlProcedureResult();
			result.setResultSet(trac.getTicket(inputTable.getRows().get(0).getValues().get(0).getStringValue()));
			return result;
		}

		val parameterOffset = 2;
		val resultSetOffset = 1;
		final val connection = systemDatabase.getConnection();
		try {
			Set<ExecuteStrategy> executeStrategies = new HashSet<>();
			executeStrategies.add(ExecuteStrategy.RETURN_CODE_IS_ERROR_IF_NOT_0);
			val preparedStatement = connection.prepareCall(prepareProcedureString(inputTable, executeStrategies));
			range(0, inputTable.getColumns().size())//
					.forEach(i -> {
						try {
							val iVal = inputTable.getRows().get(0).getValues().get(i);
							val type = inputTable.getColumns().get(i).getType();
							if (iVal == null) {
								if (type == DataType.BOOLEAN) {
									preparedStatement.setObject(i + parameterOffset, null, Types.BOOLEAN);
								} else if (type == DataType.DOUBLE) {
									preparedStatement.setObject(i + parameterOffset, null, Types.DOUBLE);
								} else if (type == DataType.INSTANT) {
									preparedStatement.setObject(i + parameterOffset, null, Types.TIMESTAMP);
								} else if (type == DataType.INTEGER) {
									preparedStatement.setObject(i + parameterOffset, null, Types.INTEGER);
								} else if (type == DataType.LONG) {
									preparedStatement.setObject(i + parameterOffset, null, Types.DOUBLE);
								} else if (type == DataType.STRING) {
									preparedStatement.setObject(i + parameterOffset, null, Types.NVARCHAR);
								} else if (type == DataType.ZONED) {
									preparedStatement.setObject(i + parameterOffset, null, Types.TIMESTAMP);
								} else {
									throw new IllegalArgumentException("Unknown type: " + type.name());
								}
							} else {
								if (type == DataType.BOOLEAN) {
									preparedStatement.setBoolean(i + parameterOffset, iVal.getBooleanValue());
								} else if (type == DataType.DOUBLE) {
									preparedStatement.setDouble(i + parameterOffset, iVal.getDoubleValue());
								} else if (type == DataType.INSTANT) {
									preparedStatement.setTimestamp(i + parameterOffset, Timestamp.from(iVal.getInstantValue()));
								} else if (type == DataType.INTEGER) {
									preparedStatement.setInt(i + parameterOffset, iVal.getIntegerValue());
								} else if (type == DataType.LONG) {
									preparedStatement.setDouble(i + parameterOffset, Double.valueOf(iVal.getLongValue()));
								} else if (type == DataType.STRING) {
									preparedStatement.setString(i + parameterOffset, iVal.getStringValue());
								} else if (type == DataType.ZONED) {
									preparedStatement.setTimestamp(i + parameterOffset, Timestamp.from(iVal.getZonedDateTimeValue().toInstant()));
								} else {
									throw new IllegalArgumentException("Unknown type: " + type.name());
								}
							}
							if (inputTable.getColumns().get(i).getOutputType() == OUTPUT) {
								if (type == DataType.BOOLEAN) {
									preparedStatement.registerOutParameter(i + parameterOffset, Types.BOOLEAN);
								} else if (type == DataType.DOUBLE) {
									preparedStatement.registerOutParameter(i + parameterOffset, Types.DOUBLE);
								} else if (type == DataType.INSTANT) {
									preparedStatement.registerOutParameter(i + parameterOffset, Types.TIMESTAMP);
								} else if (type == DataType.INTEGER) {
									preparedStatement.registerOutParameter(i + parameterOffset, Types.INTEGER);
								} else if (type == DataType.LONG) {
									preparedStatement.registerOutParameter(i + parameterOffset, Types.DOUBLE);
								} else if (type == DataType.STRING) {
									preparedStatement.registerOutParameter(i + parameterOffset, Types.NVARCHAR);
								} else if (type == DataType.ZONED) {
									preparedStatement.registerOutParameter(i + parameterOffset, Types.TIMESTAMP);
								} else {
									throw new IllegalArgumentException("Unknown type: " + type.name());
								}
							}
						} catch (Exception e) {
							throw new RuntimeException("Could not parse input parameter with index:" + i, e);
						}
					});
			preparedStatement.registerOutParameter(1, Types.INTEGER);
			val result = new SqlProcedureResult();
			preparedStatement.execute();
			if (null != preparedStatement.getResultSet()) {
				val sqlResultSet = preparedStatement.getResultSet();
				val resultSet = new Table();
				result.setResultSet(resultSet);
				resultSet.setName("resultSet");
				val metaData = sqlResultSet.getMetaData();
				resultSet.setColumns(//
						range(0, metaData.getColumnCount()).mapToObj(i -> {
							try {
								val type = metaData.getColumnType(i + resultSetOffset);
								val name = metaData.getColumnName(i + resultSetOffset);
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
								} else if (type == Types.NVARCHAR) {
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
			// Dies muss ausgelesen werden, nachdem die ResultSet ausgelesen wurde, da sonst diese nicht abrufbar ist.
			val returnCode = preparedStatement.getObject(1);
			if (returnCode != null) {
				result.setReturnCode(preparedStatement.getInt(1));
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
				outputParameters.setColumns(inputTable.getColumns());
				range(0, inputTable.getColumns().size())//
						.forEach(i -> {
							if (outputColumnsMapping.get(i)) {
								outputValues.addValue(parseSqlParameter(preparedStatement, i + parameterOffset, inputTable.getColumns().get(i)));
							} else {
								inputTable.getRows().get(0).getValues().get(i);
							}
						});
			}
			connection.commit();
			return result;
		} catch (SQLException e) {
			logger.error("Couldn't execute procedure: ", e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				logger.error("Couldn't roll back procedure execution: ", e1);
			}
			throw e;
		} finally {
			systemDatabase.freeUpConnection(connection);
		}
	}

	String prepareProcedureString(Table params) {
		return prepareProcedureString(params, ExecuteStrategy.STANDARD);
	}

	/**
	 * Bereitet einen Prozedur-String vor
	 *
	 * @param params
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
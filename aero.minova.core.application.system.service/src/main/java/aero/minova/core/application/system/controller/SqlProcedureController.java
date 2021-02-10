package aero.minova.core.application.system.controller;

import static aero.minova.core.application.system.domain.OutputType.OUTPUT;
import static aero.minova.core.application.system.sql.SqlUtils.convertSqlResultToRow;
import static aero.minova.core.application.system.sql.SqlUtils.parseSqlParameter;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.ErrorMessage;
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

	@Autowired
	SqlViewController svc;

	@PostMapping(value = "data/procedure", produces = "application/json")
	public SqlProcedureResult executeProcedure(@RequestBody Table inputTable) {
		if ("Ticket".equals(inputTable.getName())) {
			val result = new SqlProcedureResult();
			result.setResultSet(trac.getTicket(inputTable.getRows().get(0).getValues().get(0).getStringValue()));
			return result;
		}

		// bei Prozeduren ist es nur wichtig, dass es eine Erlaubnis gibt
		@SuppressWarnings("unchecked")
		List<GrantedAuthority> userAuthorities = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		if (svc.checkPrivilege(userAuthorities, inputTable.getName()).getRows().isEmpty()) {
			throw new RuntimeException("Insufficient Permission for " + inputTable.getName());
		}
		return calculateSqlProcedureResult(inputTable);
	}

	public SqlProcedureResult calculateSqlProcedureResult(Table inputTable) {
		val parameterOffset = 2;
		val resultSetOffset = 1;
		final val connection = systemDatabase.getConnection();
		val result = new SqlProcedureResult();

		try {
			final Set<ExecuteStrategy> executeStrategies = new HashSet<>();
			executeStrategies.add(ExecuteStrategy.RETURN_CODE_IS_ERROR_IF_NOT_0);
			final val procedureCall = prepareProcedureString(inputTable, executeStrategies);
			logger.info("Executing: " + procedureCall);
			final val preparedStatement = connection.prepareCall(procedureCall);
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
			preparedStatement.execute();
			if (null != preparedStatement.getResultSet()) {
				val sqlResultSet = preparedStatement.getResultSet();
				val resultSet = new Table();
				result.setResultSet(resultSet);
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
								} else if (type == Types.VARCHAR) {
									return new Column(name, DataType.STRING);
								} else if (type == Types.NVARCHAR) {
									return new Column(name, DataType.STRING);
								} else {
									throw new UnsupportedOperationException("Unsupported result set type: " + i);
								}
							} catch (Exception e) {
								throw new RuntimeException("Could not parse resultset: ", e);
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
								outputValues.addValue(inputTable.getRows().get(0).getValues().get(i));
							}
						});
			}
			connection.commit();
		} catch (Exception e) {
			Exception sqlE = new Exception("Couldn't execute procedure: ", e);
			logger.error(sqlE.getMessage());
			ErrorMessage error = new ErrorMessage();
			error.setErrorMessage(sqlE);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			sqlE.printStackTrace(pw);
			String messages = sw.toString();
			List<String> trace = Stream.of(messages.split("\n\tat|\n"))//
					.map(String::trim)//
					.collect(Collectors.toList());
			error.setTrace(trace);
			result.setReturnErrorMessage(error);
			result.setReturnCode(-1);
			try {
				connection.rollback();
			} catch (Exception e1) {
				Exception ex = new Exception("Couldn't roll back procedure execution: ", e);
				logger.error(ex.getMessage());
				error.setErrorMessage(ex);
				ex.printStackTrace(pw);
				messages = sw.toString();
				trace = Stream.of(messages.split("\n\tat|\n"))//
						.map(String::trim)//
						.collect(Collectors.toList());
				error.setTrace(trace);
				result.setReturnErrorMessage(error);
				result.setReturnCode(-2);
			}
		} finally {
			systemDatabase.freeUpConnection(connection);
		}
		return result;
	}

	String prepareProcedureString(Table params) {
		return prepareProcedureString(params, ExecuteStrategy.STANDARD);
	}

	/**
	 * Bereitet einen Prozedur-String vor
	 *
	 * @param params
	 *            SQL-Call-Parameter
	 * @param strategy
	 *            SQL-Execution-Strategie
	 * @return SQL-Code
	 * @throws IllegalArgumentException
	 *             Fehler, wenn die Daten in params nicht richtig sind.
	 */
	String prepareProcedureString(Table params, Set<ExecuteStrategy> strategy) throws IllegalArgumentException {
		if (params.getName() == null || params.getName().trim().length() == 0) {
			throw new IllegalArgumentException("Cannot prepare procedure with NULL name");
		}
		final int paramCount = params.getColumns().size();
		final boolean returnRequired = ExecuteStrategy.returnRequired(strategy);

		final StringBuilder sb = new StringBuilder();
		sb.append('{').append(returnRequired ? "? = call " : "call ").append(params.getName()).append("(");
		for (int i = 0; i < paramCount; i++) {
			sb.append(i == 0 ? "?" : ",?");
		}
		sb.append(")}");
		return sb.toString();
	}
}
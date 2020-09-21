package aero.minova.core.application.system.controller;

import static aero.minova.core.application.system.sql.SqlUtils.convertSqlResultToRow;
import static aero.minova.core.application.system.sql.SqlUtils.parseSqlParameter;
import static java.sql.Types.VARCHAR;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;

import java.sql.SQLException;
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
import aero.minova.core.application.system.domain.OutputType;
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
							if (iVal == null) {
								preparedStatement.setNull(i + 1, VARCHAR);
							} else {
								preparedStatement.setString(i + 1, SqlUtils.toSqlString(iVal));
							}
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					});
			val hasResultSet = preparedStatement.execute();
			if (hasResultSet) {
				val sqlResultSet = preparedStatement.getResultSet();
				val resultSet = new Table();
				resultSet.setName("resultSet");
				// TODO Determine output format via result set and not input format: https://docs.oracle.com/javase/7/docs/api/java/sql/ResultSetMetaData.html
				resultSet.setColumns(inputTable.getColumns()//
						.stream()//
						.collect(toList()));
				while (sqlResultSet.next()) {
					resultSet.addRow(//
							convertSqlResultToRow(resultSet//
									, sqlResultSet//
									, logger//
									, this//
									, c -> !Objects.equals(c.getName(), "FilterLastAction")));
				}
			}
			val hasOutputParameters = inputTable//
					.getColumns()//
					.stream()//
					.anyMatch(c -> c.getOutputType() == OutputType.OUTPUT);
			if (hasOutputParameters) {
				val outputParameters = new Table();
				outputParameters.setName("outputParameters");
				val outputColumnsMapping = inputTable//
						.getColumns()//
						.stream()//
						.map(c -> c.getOutputType() == OutputType.OUTPUT)//
						.collect(toList());
				val outputValues = new Row();
				outputParameters.addRow(outputValues);
				range(0, inputTable.getColumns().size())//
						.forEach(i -> {
							if (outputColumnsMapping.get(i)) {
								outputValues.addValue(parseSqlParameter(preparedStatement, i, inputTable.getColumns().get(i)));
							} else {
								outputValues.addValue(null);
							}
						});
			}
			val result = new SqlProcedureResult();
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
			val param = params.getColumns().get(i);
			if (param.getOutputType() == OutputType.OUTPUT) {
				sb.append(i == 0 ? "? output" : ",? output");
			} else {
				sb.append(i == 0 ? "?" : ",?");
			}
		}
		sb.append(")}");
		return sb.toString();
	}

}

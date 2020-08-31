package aero.minova.core.application.system.controller;

import static aero.minova.core.application.system.sql.SqlUtils.convertSqlResultToRow;
import static java.sql.Types.VARCHAR;
import static java.util.stream.IntStream.rangeClosed;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.Row;
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

	@PostMapping(value = "data/procedure-with-result-set", produces = "application/json")
	public Table executeProcedure(@RequestBody Table inputTable) {
		try {
			val preparedStatement = systemDatabase.connection().prepareCall(prepareProcedureString(inputTable));
			IntStream.range(0, inputTable.getRows().get(0).getValues().size())//
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
			val resultSet = preparedStatement.executeQuery();
			val outputTable = new Table();
			outputTable.setName(inputTable.getName());
			outputTable.setColumns(//
					inputTable.getColumns().stream()//
							.filter(column -> !Objects.equals(column.getName(), Column.AND_FIELD_NAME))//
							.filter(column -> !Objects.equals(column.getName(), "FilterLastAction"))//
							.collect(Collectors.toList()));
			while (resultSet.next()) {
				outputTable.addRow(convertSqlResultToRow(outputTable, resultSet, logger, this, c -> !Objects.equals(c.getName(), "FilterLastAction")));
			}
			return outputTable;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping(value = "data/procedure-with-return-code", produces = "application/json")
	public Table executeProcedureWithReturnCode(@RequestBody Table inputTable) {
		try {
			Set<ExecuteStrategy> executeStrategies = new HashSet<>();
			executeStrategies.add(ExecuteStrategy.RETURN_CODE_IS_ERROR_IF_NOT_0);
			val preparedStatement = systemDatabase.connection().prepareCall(prepareProcedureString(inputTable, executeStrategies));
			IntStream.range(0, inputTable.getRows().get(0).getValues().size())//
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
			val returnCode = 0;// preparedStatement.getInt(1);

			val outputTable = new Table();
			outputTable.setName(inputTable.getName());
			outputTable.setColumns(Arrays.asList(new Column("ReturnCode", DataType.INTEGER)));

			Row row = new Row();
			row.addValue(new Value(returnCode));
			outputTable.addRow(row);
			return outputTable;
		} catch (

		SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static String prepareProcedureString(Table params) {
		return prepareProcedureString(params, ExecuteStrategy.standard);
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
	private static String prepareProcedureString(Table params, Set<ExecuteStrategy> strategy) throws IllegalArgumentException {
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

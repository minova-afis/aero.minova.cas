package aero.minova.core.application.system.controller;

import static java.util.stream.IntStream.rangeClosed;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
	Connection sqlConnection;

	@PostMapping(value = "data/procedure-with-result-set", produces = "application/json")
	public Table executeProcedure(@RequestBody Table inputTable) {
		if (sqlConnection == null) {
			sqlConnection = SystemDatabase.connection();
		}
		try {
			val preparedStatement = sqlConnection.prepareCall(prepareProcedureString(inputTable));
			IntStream.range(0, inputTable.getRows().get(0).getValues().size())//
					.forEach(i -> {
						try {
							val iVal = inputTable.getRows().get(0).getValues().get(i);
							if (iVal == null) {
								preparedStatement.setString(i + 1, "null");
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
					rangeClosed(1, resultSet.getMetaData().getColumnCount())//
							.mapToObj(i -> {
								try {
									return resultSet.getMetaData().getColumnName(i);
								} catch (SQLException e1) {
									throw new RuntimeException(e1);
								}
							})//
							.map(columnName -> new Column(columnName, DataType.STRING))//
							.collect(Collectors.toList()));
			while (resultSet.next()) {
				Row row = new Row();
				rangeClosed(1, resultSet.getMetaData().getColumnCount()).forEach(i -> {
					// TODO Feld typisieren.
					try {
						row.addValue(new Value(resultSet.getString(i)));
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				});
				outputTable.addRow(row);
			}
			return outputTable;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping(value = "data/procedure-with-return-code", produces = "application/json")
	public Table executeProcedureWithReturnCode(@RequestBody Table inputTable) {
		if (sqlConnection == null) {
			sqlConnection = SystemDatabase.connection();
		}
		try {
			Set<ExecuteStrategy> executeStrategies = new HashSet<>();
			executeStrategies.add(ExecuteStrategy.RETURN_CODE_IS_ERROR_IF_NOT_0);
			val preparedStatement = sqlConnection.prepareCall(prepareProcedureString(inputTable, executeStrategies));
			IntStream.range(0, inputTable.getRows().get(0).getValues().size())//
					.forEach(i -> {
						try {
							val iVal = inputTable.getRows().get(0).getValues().get(i);
							if (iVal == null) {
								preparedStatement.setString(i + 2, "null");
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

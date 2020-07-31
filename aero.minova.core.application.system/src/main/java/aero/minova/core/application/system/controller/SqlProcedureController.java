package aero.minova.core.application.system.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
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
import aero.minova.core.application.system.sql.SystemDatabase;
import lombok.val;

@RestController
public class SqlProcedureController {
	Connection sqlConnection;

	@PostMapping(value = "data/procedure", produces = "application/json")
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
							if (iVal != null) {
								preparedStatement.setString(i + 1, iVal.getValue().toString());
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
							.collect(Collectors.toList()));
			while (resultSet.next()) {
				Row row = new Row();
				for (Column column : outputTable.getColumns()) {
					// TODO Feld typisieren.
					if (column.getType() == DataType.STRING) {
						row.addValue(new Value(resultSet.getString(column.getName())));
					} else if (column.getType() == DataType.INTEGER) {
						row.addValue(new Value(resultSet.getInt(column.getName())));
					} else {
						throw new UnsupportedOperationException("Der Typ " + column.getType() + " wird nicht unterstützt.");
					}
				}
				outputTable.addRow(row);
			}
			return outputTable;
		} catch (SQLException e) {
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

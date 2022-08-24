package aero.minova.cas.service;

import static aero.minova.cas.sql.SqlUtils.convertSqlResultToRow;
import static aero.minova.cas.sql.SqlUtils.parseSqlParameter;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.OutputType;
import aero.minova.cas.api.domain.ProcedureException;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.TableMetaData;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.sql.ExecuteStrategy;
import aero.minova.cas.sql.SystemDatabase;
import lombok.val;

@Service
public class ProcedureService {

	@Autowired
	CustomLogger customLogger;

	@Autowired
	SystemDatabase systemDatabase;

	@org.springframework.beans.factory.annotation.Value("${aero.minova.database.kind:mysql}")
	String databaseKind;

	@org.springframework.beans.factory.annotation.Value("${aero.minova.database.maxresultsetcount:512}")
	Integer maxResultSetCount;

	@Autowired
	SecurityService securityService;

	/**
	 * Speichert im SQl-Session-Context unter `casUser` den Nutzer, der die Abfrage tätigt.
	 *
	 * @param connection
	 *            Das ist die Session.
	 * @throws SQLException
	 *             Fehler beim setzen des Kontextes für die connection.
	 */
	public void setUserContextFor(Connection connection) throws SQLException {

		CallableStatement userContextSetter;
		if (databaseKind.equals("postgresql")) {
			userContextSetter = connection.prepareCall("SET my.app_user = ?;");
		} else {
			userContextSetter = connection.prepareCall("exec sys.sp_set_session_context N'casUser', ?;");
		}
		userContextSetter.setNString(1, SecurityContextHolder.getContext().getAuthentication().getName());
		userContextSetter.execute();
	}

	/**
	 * Führt eine Prozedur mit den übergebenen Parametern aus. Falls die Prozedur Output-Parameter zurückgibt, werden diese auch im SqlProcedureResult
	 * zurückgegeben.
	 *
	 * @param inputTable
	 *            Ausführungs-Parameter im Form einer Table
	 * @param privilegeRequest
	 *            eine Liste an Rows im Format (PrivilegName,UserSecurityToken,RowLevelSecurity-Bit)
	 * @return Resultat SqlProcedureResult der Ausführung
	 * @throws Exception
	 *             Fehler bei der Ausführung
	 */
	public SqlProcedureResult processSqlProcedureRequest(Table inputTable, List<Row> privilegeRequest) throws Exception {
		SqlProcedureResult result = new SqlProcedureResult();
		StringBuffer sb = new StringBuffer();
		Connection connection = null;

		try {
			connection = systemDatabase.getConnection();
			result = calculateSqlProcedureResult(inputTable, privilegeRequest, connection, result, sb);
			connection.commit();
			customLogger.logSql("Procedure succesfully executed: " + sb.toString());
		} catch (Exception e) {
			customLogger.logError("Procedure could not be executed: " + sb.toString(), e);
			try {
				connection.rollback();
				systemDatabase.freeUpConnection(connection);
			} catch (Exception e1) {
				customLogger.logError("Couldn't roll back procedure execution", e);
				connection.close();
			}
			throw new ProcedureException(e);
		} finally {
			systemDatabase.freeUpConnection(connection);
		}
		return result;
	}

	/**
	 * Führt eine SQL-Prozedur aus. Hier gibt es keinen Rollback oder Commit. Diese müssen selbst durchgeführt werden. Diese Methode ist public, weil diese von
	 * Erweiterungen genutzt werden, um bei Fehlern in komplexeren Prozessen alle Änderungen in der Datenbank rückgängig zu machen.
	 *
	 * @param inputTable
	 *            Die Table mit allen Werten zum Verarbeiten der Prozedur.
	 * @param privilegeRequest
	 *            Eine Liste an Rows im Format (PrivilegName,UserSecurityToken,RowLevelSecurity-Bit).
	 * @param connection
	 *            Die Connection zu der Datenbank, welche die Prozedur ausführen soll.
	 * @param result
	 *            Das SQL-Result, welches innerhalb der Methode verändert und dann returned wird.
	 * @param sb
	 *            Ein StringBuffer, welcher im Fehlerfall die Fehlermeldung bis zum endgültigen Throw als String aufnimmt.
	 * @return result Das veränderte SqlProcedureResult.
	 * @throws SQLException
	 *             Falls ein Fehler beim Ausführen der Prozedur auftritt.
	 * @throws ProcedureException
	 *             Falls generell ein Fehler geworfen wird, zum Beispiel beim Konvertieren der Typen.
	 */
	public SqlProcedureResult calculateSqlProcedureResult(Table inputTable, List<Row> privilegeRequest, final java.sql.Connection connection,
			SqlProcedureResult result, StringBuffer sb) throws SQLException, ProcedureException {
		List<String> userSecurityTokensToBeChecked = securityService.extractUserTokens(privilegeRequest);

		result.setReturnCodes(new ArrayList<Integer>());
		result.setReturnCode(0);
		val parameterOffset = 2;
		val resultSetOffset = 1;
		TableMetaData inputMetaData = inputTable.getMetaData();
		if (inputMetaData == null) {
			inputTable.setMetaData(new TableMetaData());
			inputMetaData = inputTable.getMetaData();
		}
		final int page;
		final int limit;
		// falls nichts als page angegeben wurde, wird angenommen, dass die erste Seite ausgegeben werden soll
		if (inputMetaData.getPage() == null) {
			page = 1;
		} else if (inputMetaData.getPage() <= 0) {
			throw new IllegalArgumentException("msg.PageError");
		} else {
			page = inputMetaData.getPage();
		}
		// falls nichts als Size/maxRows angegeben wurde, wird angenommen, dass alles ausgegeben werden soll; alles = 0
		if (inputMetaData.getLimited() == null) {
			limit = 0;
		} else if (inputMetaData.getLimited() < 0) {
			throw new IllegalArgumentException("msg.LimitError");
		} else {
			limit = inputMetaData.getLimited();
		}
		final Set<ExecuteStrategy> executeStrategies = new HashSet<>();
		executeStrategies.add(ExecuteStrategy.RETURN_CODE_IS_ERROR_IF_NOT_0);
		final val procedureCall = prepareProcedureString(inputTable, executeStrategies);
		sb.append(procedureCall);
		setUserContextFor(connection);

		final val preparedStatement = connection.prepareCall(procedureCall);

		// Jede Row ist eine Abfrage.
		for (int j = 0; j < inputTable.getRows().size(); j++) {
			SqlProcedureResult resultForThisRow = new SqlProcedureResult();

			fillCallableSqlProcedureStatement(preparedStatement, inputTable, parameterOffset, sb, j);

			preparedStatement.registerOutParameter(1, Types.INTEGER);
			preparedStatement.execute();
			{
				int i = 0;
				while (preparedStatement.getResultSet() == null) {
					if ((preparedStatement.getMoreResults() == false) && (preparedStatement.getUpdateCount() == -1)) {
						// Es gibt kein nächstes Result gibt.
						break;
					}
					if (++i >= maxResultSetCount) {
						customLogger.logSql(
								"Warning: too many result sets. Mabye there is a big report, which writes a lot of resultsets? Please increase the default value in application.properties (aero.minova.database.maxresultsetcount)");
						break;
					}
				}
			}
			if (null != preparedStatement.getResultSet() || (preparedStatement.getMoreResults() && null != preparedStatement.getResultSet())) {
				val sqlResultSet = preparedStatement.getResultSet();
				val resultSet = new Table();
				resultSet.setName(inputTable.getName());
				resultForThisRow.setResultSet(resultSet);
				val metaData = sqlResultSet.getMetaData();
				resultSet.setColumns(//
						range(0, metaData.getColumnCount()).mapToObj(i -> {
							try {
								val type = metaData.getColumnType(i + resultSetOffset);
								val name = metaData.getColumnName(i + resultSetOffset);
								if (type == Types.BOOLEAN || Types.BIT == type) {
									return new Column(name, DataType.BOOLEAN);
								} else if (type == Types.DOUBLE) {
									return new Column(name, DataType.DOUBLE);
								} else if (type == Types.TIMESTAMP) {
									return new Column(name, DataType.INSTANT);
								} else if (type == Types.INTEGER) {
									return new Column(name, DataType.INTEGER);
								} else if ((type == Types.VARCHAR) || (type == Types.NVARCHAR)) {
									return new Column(name, DataType.STRING);
								} else if (type == Types.DECIMAL) {
									return new Column(name, DataType.BIGDECIMAL);
								} else if (type == Types.BIGINT) {
									return new Column(name, DataType.LONG);
								} else {
									throw new UnsupportedOperationException("msg.UnsupportedResultSetError %" + i);
								}
							} catch (Exception e) {
								throw new RuntimeException("msg.ParseResultSetError");
							}
						}).collect(toList()));
				int totalResults = 0;

				int securityTokenInColumn = -1;
				if (!userSecurityTokensToBeChecked.isEmpty()) {
					securityTokenInColumn = securityService.findSecurityTokenColumn(resultSet);
				}
				resultSet.setMetaData(new TableMetaData());
				while (sqlResultSet.next()) {
					Row rowToBeAdded = null;
					if (limit > 0) {
						// nur die Menge an Rows, welche auf der gewünschten Page liegen
						if (sqlResultSet.getRow() > ((page - 1) * limit) && sqlResultSet.getRow() <= (page * limit)) {
							rowToBeAdded = convertSqlResultToRow(resultSet//
									, sqlResultSet//
									, customLogger.logger////
									, this);
						}
					} else {
						rowToBeAdded = convertSqlResultToRow(resultSet//
								, sqlResultSet//
								, customLogger.logger////
								, this);
					}

					/*
					 * Falls die SecurityToken-Prüfung nicht eingeschalten ist, wird einfach true zurückgegeben und die Row hinzugefügt.
					 */
					if (securityService.isRowAccessValid(userSecurityTokensToBeChecked, rowToBeAdded, securityTokenInColumn)) {
						resultSet.addRow(rowToBeAdded);
						totalResults++;
					}
					resultSet.fillMetaData(resultSet, limit, totalResults, page);
				}
			}

			val hasOutputParameters = inputTable//
					.getColumns()//
					.stream()//
					.anyMatch(c -> c.getOutputType() == OutputType.OUTPUT);
			if (hasOutputParameters) {
				val outputParameters = new Table();
				outputParameters.setName(inputTable.getName());
				resultForThisRow.setOutputParameters(outputParameters);
				val outputColumnsMapping = inputTable//
						.getColumns()//
						.stream()//
						.map(c -> c.getOutputType() == OutputType.OUTPUT)//
						.collect(toList());

				val outputValues = new Row();
				outputParameters.setColumns(inputTable.getColumns());
				range(0, inputTable.getColumns().size())//
						.forEach(i -> {
							if (outputColumnsMapping.get(i)) {
								outputValues.addValue(parseSqlParameter(preparedStatement, i + parameterOffset, inputTable.getColumns().get(i)));
							} else {
								outputValues.addValue(inputTable.getRows().get(0).getValues().get(i));
							}
						});
				int securityTokenInColumn = -1;
				if (!userSecurityTokensToBeChecked.isEmpty()) {
					securityTokenInColumn = securityService.findSecurityTokenColumn(inputTable);
				}
				Row resultRow = new Row();
				if (securityService.isRowAccessValid(userSecurityTokensToBeChecked, outputValues, securityTokenInColumn)) {
					resultRow = outputValues;
				} else {
					for (Value element : outputValues.getValues()) {
						resultRow.addValue(null);
					}
				}
				outputParameters.addRow(resultRow);
			}

			// Endresult-OutputParameter erweitern um RowResult-OutputParameter.
			if (resultForThisRow.getOutputParameters() != null && resultForThisRow.getOutputParameters().getRows() != null) {
				if (result.getOutputParameters() == null) {
					result.setOutputParameters(resultForThisRow.getOutputParameters());
				} else {
					result.getOutputParameters().getRows().addAll(resultForThisRow.getOutputParameters().getRows());
				}
			}

			// Endresult-ResultSet erweitern um RowResult-ResultSet.
			if (resultForThisRow.getResultSet() != null && resultForThisRow.getResultSet().getRows() != null) {
				if (result.getResultSet() == null) {
					result.setResultSet(resultForThisRow.getResultSet());
				} else {
					TableMetaData metaData = result.getResultSet().getMetaData();
					result.getResultSet().getRows().addAll(resultForThisRow.getResultSet().getRows());
					result.getResultSet().fillMetaData(inputTable, limit, metaData.getTotalResults() + resultForThisRow.getResultSet().getRows().size(), page);
				}
			}
			// Dies muss ausgelesen werden, nachdem die ResultSet ausgelesen wurde, da sonst diese nicht abrufbar ist.
			val returnCode = preparedStatement.getObject(1);
			if (returnCode != null) {
				int reCode = preparedStatement.getInt(1);
				resultForThisRow.setReturnCode(reCode);
				// Falls nicht alle ReturnCodes gleich sind, wird 1 als endgültiger ReturnCode rein geschrieben.
				if (!result.getReturnCodes().isEmpty() && !result.getReturnCodes().contains(reCode) && result.getReturnCode() != 1) {
					result.setReturnCode(1);
				}
				result.getReturnCodes().add(resultForThisRow.getReturnCode());
			}
		}
		return result;
	}

	public void fillCallableSqlProcedureStatement(CallableStatement preparedStatement, Table inputTable, int parameterOffset, StringBuffer sb, int row) {
		range(0, inputTable.getColumns().size())//
				.forEach(i -> {
					try {
						val iVal = inputTable.getRows().get(row).getValues().get(i);
						val type = inputTable.getColumns().get(i).getType();
						if (iVal == null) {
							sb.append(" ; Position: " + (i + parameterOffset) + ", Value: " + iVal);
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
							} else if (type == DataType.BIGDECIMAL) {
								preparedStatement.setObject(i + parameterOffset, null, Types.DECIMAL);
							} else if (type == DataType.LONG) {
								preparedStatement.setObject(i + parameterOffset, null, Types.BIGINT);
							} else {
								throw new IllegalArgumentException("msg.UnknownType %" + type.name());
							}
						} else {
							sb.append(" ; Position: " + (i + parameterOffset) + ", Value: " + iVal.getValue().toString());
							if (type == DataType.BOOLEAN) {
								preparedStatement.setBoolean(i + parameterOffset, iVal.getBooleanValue());
							} else if (type == DataType.DOUBLE) {
								preparedStatement.setDouble(i + parameterOffset, iVal.getDoubleValue());
							} else if (type == DataType.INSTANT) {
								preparedStatement.setTimestamp(i + parameterOffset, Timestamp.from(iVal.getInstantValue()));
							} else if (type == DataType.INTEGER) {
								preparedStatement.setInt(i + parameterOffset, iVal.getIntegerValue());
							} else if (type == DataType.STRING) {
								preparedStatement.setString(i + parameterOffset, iVal.getStringValue());
							} else if (type == DataType.ZONED) {
								preparedStatement.setTimestamp(i + parameterOffset, Timestamp.from(iVal.getZonedDateTimeValue().toInstant()));
							} else if (type == DataType.BIGDECIMAL) {
								preparedStatement.setBigDecimal(i + parameterOffset, iVal.getBigDecimalValue());
							} else if (type == DataType.LONG) {
								preparedStatement.setLong(i + parameterOffset, iVal.getLongValue());
							} else {
								throw new IllegalArgumentException("msg.UnknownType %" + type.name());
							}
						}
						if (inputTable.getColumns().get(i).getOutputType() == OutputType.OUTPUT) {
							if (type == DataType.BOOLEAN) {
								preparedStatement.registerOutParameter(i + parameterOffset, Types.BOOLEAN);
							} else if (type == DataType.DOUBLE) {
								preparedStatement.registerOutParameter(i + parameterOffset, Types.DOUBLE);
							} else if (type == DataType.INSTANT) {
								preparedStatement.registerOutParameter(i + parameterOffset, Types.TIMESTAMP);
							} else if (type == DataType.INTEGER) {
								preparedStatement.registerOutParameter(i + parameterOffset, Types.INTEGER);
							} else if (type == DataType.LONG) {
								preparedStatement.registerOutParameter(i + parameterOffset, Types.BIGINT);
							} else if (type == DataType.STRING) {
								preparedStatement.registerOutParameter(i + parameterOffset, Types.NVARCHAR);
							} else if (type == DataType.ZONED) {
								preparedStatement.registerOutParameter(i + parameterOffset, Types.TIMESTAMP);
							} else if (type == DataType.BIGDECIMAL) {
								preparedStatement.registerOutParameter(i + parameterOffset, Types.DECIMAL);
							} else {
								throw new IllegalArgumentException("msg.UnknownType %" + type.name());
							}
						}
					} catch (Exception e) {
						throw new RuntimeException("msg.ParseError %" + i, e);
					}
				});
	}

	public String prepareProcedureString(Table params) {
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
	public String prepareProcedureString(Table params, Set<ExecuteStrategy> strategy) throws IllegalArgumentException {
		if (params.getName() == null || params.getName().trim().length() == 0) {
			throw new IllegalArgumentException("msg.ProcedureNullName");
		}
		final int paramCount = params.getColumns().size();
		final boolean returnRequired = ExecuteStrategy.returnRequired(strategy);

		final StringBuilder sb = new StringBuilder();
		sb.append('{')//
				.append("")//
				.append(returnRequired ? "? = call " : "call ")//
				.append(params.getName())//
				.append("(");
		for (int i = 0; i < paramCount; i++) {
			sb.append(i == 0 ? "?" : ",?");
		}
		sb.append(")}");
		return sb.toString();
	}
}

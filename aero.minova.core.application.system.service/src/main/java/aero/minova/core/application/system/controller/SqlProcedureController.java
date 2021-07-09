package aero.minova.core.application.system.controller;

import static aero.minova.core.application.system.domain.OutputType.OUTPUT;
import static aero.minova.core.application.system.sql.SqlUtils.convertSqlResultToRow;
import static aero.minova.core.application.system.sql.SqlUtils.parseSqlParameter;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import aero.minova.core.application.system.CustomLogger;
import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.ProcedureException;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.SqlProcedureResult;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.TableMetaData;
import aero.minova.core.application.system.sql.ExecuteStrategy;
import aero.minova.core.application.system.sql.SystemDatabase;
import lombok.val;

@RestController
public class SqlProcedureController {
	@Autowired
	SystemDatabase systemDatabase;
	CustomLogger customLogger = new CustomLogger();

	@Autowired
	SqlViewController svc;

	@Autowired
	Gson gson;

	private Map<String, Function<Table, ResponseEntity>> extension = new HashMap<>();

	public void registerExctension(String name, Function<Table, ResponseEntity> ext) {
		if (extension.containsKey(name)) {
			throw new IllegalArgumentException(name);
		}
		extension.put(name, ext);
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "data/procedure")
	public ResponseEntity executeProcedure(@RequestBody Table inputTable) throws Exception {
		customLogger.logUserRequest("data/procedure: " + gson.toJson(inputTable));
		if (extension.containsKey(inputTable.getName())) {
			try {
				return extension.get(inputTable.getName()).apply(inputTable);
			} catch (Exception e) {
				throw new ProcedureException(e.getMessage());
			}
		}
		try {
			List<Row> privilegeRequest = svc.getPrivilegePermissions(inputTable.getName()).getRows();
			if (privilegeRequest.isEmpty()) {
				throw new ProcedureException("msg.PrivilegeError %" + inputTable.getName());
			}
			val result = calculateSqlProcedureResult(inputTable, privilegeRequest);
			return new ResponseEntity(result, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			customLogger.logError("Error while trying to execute procedure: " + inputTable.getName(), e);
			throw e;
		}
	}

	/**
	 * Diese Methode ist nicht geschützt. Aufrufer sind für die Sicherheit verantwortlich.
	 *
	 * @param inputTable
	 *            Ausfüjhrungs-Parameter
	 * @param privilegeRequest
	 * @return Resultat der Ausführung
	 * @throws Exception
	 *             Fehler bei der Ausführung
	 */
	public SqlProcedureResult calculateSqlProcedureResult(Table inputTable, List<Row> privilegeRequest) throws Exception {
		List<String> userSecurityTokensToBeChecked = svc.extractUserTokens(privilegeRequest);
		val parameterOffset = 2;
		val resultSetOffset = 1;
		final val connection = systemDatabase.getConnection();
		val result = new SqlProcedureResult();
		StringBuffer sb = new StringBuffer();

		try {
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
			final val preparedStatement = connection.prepareCall(procedureCall);
			range(0, inputTable.getColumns().size())//
					.forEach(i -> {
						try {
							val iVal = inputTable.getRows().get(0).getValues().get(i);
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
								} else if (type == DataType.LONG) {
									preparedStatement.setDouble(i + parameterOffset, Double.valueOf(iVal.getLongValue()));
								} else if (type == DataType.STRING) {
									preparedStatement.setString(i + parameterOffset, iVal.getStringValue());
								} else if (type == DataType.ZONED) {
									preparedStatement.setTimestamp(i + parameterOffset, Timestamp.from(iVal.getZonedDateTimeValue().toInstant()));
								} else if (type == DataType.BIGDECIMAL) {
									preparedStatement.setBigDecimal(i + parameterOffset, iVal.getBigDecimalValue());
								} else {
									throw new IllegalArgumentException("msg.UnknownType %" + type.name());
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
			preparedStatement.registerOutParameter(1, Types.INTEGER);
			preparedStatement.execute();
			if (null != preparedStatement.getResultSet() || (preparedStatement.getMoreResults() && null != preparedStatement.getResultSet())) {
				val sqlResultSet = preparedStatement.getResultSet();
				val resultSet = new Table();
				resultSet.setName(inputTable.getName());
				result.setResultSet(resultSet);
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
								} else if (type == Types.VARCHAR) {
									return new Column(name, DataType.STRING);
								} else if (type == Types.NVARCHAR) {
									return new Column(name, DataType.STRING);
								} else if (type == Types.DECIMAL) {
									return new Column(name, DataType.BIGDECIMAL);
								} else {
									throw new UnsupportedOperationException("msg.UnsupportedResultSetError %" + i);
								}
							} catch (Exception e) {
								throw new RuntimeException("msg.ParseResultSetError");
							}
						}).collect(toList()));
				int totalResults = 0;

				int securityTokenInColumn = findSecurityTokenColumn(resultSet);
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
					if (checkRowForValidSecurityToken(userSecurityTokensToBeChecked, rowToBeAdded, securityTokenInColumn)) {
						resultSet.addRow(rowToBeAdded);
						totalResults++;
					}
					resultSet.fillMetaData(resultSet, limit, totalResults, page);
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
				outputParameters.setColumns(inputTable.getColumns());
				range(0, inputTable.getColumns().size())//
						.forEach(i -> {
							if (outputColumnsMapping.get(i)) {
								outputValues.addValue(parseSqlParameter(preparedStatement, i + parameterOffset, inputTable.getColumns().get(i)));
							} else {
								outputValues.addValue(inputTable.getRows().get(0).getValues().get(i));
							}
						});
				int securityTokenInColumn = findSecurityTokenColumn(inputTable);

				Row resultRow = new Row();
				if (checkRowForValidSecurityToken(userSecurityTokensToBeChecked, outputValues, securityTokenInColumn)) {
					resultRow = outputValues;
				} else {
					for (int i = 0; i < outputValues.getValues().size(); i++) {
						resultRow.addValue(null);
					}
				}
				outputParameters.addRow(resultRow);
			}
			connection.commit();
			customLogger.logSql("Procedure succesfully executed: " + sb.toString());
		} catch (Exception e) {
			customLogger.logError("Procedure could not be executed: " + sb.toString(), e);
			try {
				connection.rollback();
			} catch (Exception e1) {
				customLogger.logError("Couldn't roll back procedure execution", e);
			}
			throw new ProcedureException(e);
		} finally {
			systemDatabase.freeUpConnection(connection);
		}
		return result;
	}

	/*
	 * Findet die SecurityToken-Spalte der übergebenen Table.
	 */
	int findSecurityTokenColumn(Table inputTable) {
		int securityTokenInColumn = 0;
		// Herausfinden an welcher Stelle die Spalte mit den SecurityTokens ist
		for (int i = 0; i < inputTable.getColumns().size(); i++) {
			if (inputTable.getColumns().get(i).getName().equals("SecurityToken")) {
				securityTokenInColumn = i;
			}
		}
		return securityTokenInColumn;
	}

	String prepareProcedureString(Table params) {
		return prepareProcedureString(params, ExecuteStrategy.STANDARD);
	}

	/*
	 * Falls die Row-Level-Security für die Prozedur eingeschalten ist (Einträge in der Liste vorhanden), sollten die Rows nach dem Ausführen der Prozedur
	 * gefiltert werden. Überprüft, ob der SecurityToken der rowToBeChecked mit mind. 1 SecurityToken des Users übereinstimmt.
	 */
	boolean checkRowForValidSecurityToken(List<String> userSecurityTokens, Row rowToBeChecked, int securityTokenInColumn) {
		if (!userSecurityTokens.isEmpty()) {
			String securityTokenValue = rowToBeChecked.getValues().get(securityTokenInColumn).getStringValue();
			if (securityTokenValue == null || userSecurityTokens.contains(securityTokenValue.toLowerCase())) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
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
			throw new IllegalArgumentException("msg.ProcedureNullName");
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

	/*
	 * Updatet die Rollen, welche momentan im SecurityContext für den eingeloggten User hinterlegt sind
	 */
	public List<GrantedAuthority> loadPrivileges(String username, List<GrantedAuthority> authorities) {
		Table tUser = new Table();
		tUser.setName("xtcasUser");
		List<Column> columns = new ArrayList<>();
		columns.add(new Column("KeyText", DataType.STRING));
		columns.add(new Column("UserSecurityToken", DataType.STRING));
		columns.add(new Column("Memberships", DataType.STRING));
		tUser.setColumns(columns);
		Row userEntry = new Row();
		userEntry.setValues(Arrays.asList(new aero.minova.core.application.system.domain.Value(username, null),
				new aero.minova.core.application.system.domain.Value("", null), new aero.minova.core.application.system.domain.Value("", null)));
		tUser.addRow(userEntry);

		// dabei sollte nur eine ROW rauskommen, da jeder User eindeutig sein müsste
		Table membershipsFromUser = svc.getTableForSecurityCheck(tUser);
		List<String> userSecurityTokens = new ArrayList<>();

		if (membershipsFromUser.getRows().size() > 0) {
			String result = membershipsFromUser.getRows().get(0).getValues().get(2).getStringValue();

			// alle SecurityTokens werden in der Datenbank mit Leerzeile und Raute voneinander getrennt
			userSecurityTokens = Stream.of(result.split("#"))//
					.map(String::trim)//
					.collect(Collectors.toList());

			// überprüfen, ob der einzigartige userSecurityToken bereits in der Liste der Memberships vorhanden war, wenn nicht, dann hinzufügen
			String uniqueUserToken = membershipsFromUser.getRows().get(0).getValues().get(1).getStringValue().replace("#", "").trim();
			if (!userSecurityTokens.contains(uniqueUserToken))
				userSecurityTokens.add(uniqueUserToken);
		} else {
			// falls der User nicht in der Datenbank gefunden wurde, wird sein Benutzername als einzigartiger userSecurityToken verwendet
			userSecurityTokens.add(username);
		}

		// füge die authorities hinzu, welche aus dem Active Directory kommen
		for (GrantedAuthority ga : authorities) {
			userSecurityTokens.add(ga.getAuthority());
		}

		// die Berechtigungen der Gruppen noch herausfinden
		Table groups = new Table();
		groups.setName("xtcasUserGroup");
		List<Column> groupcolumns = new ArrayList<>();
		groupcolumns.add(new Column("KeyText", DataType.STRING));
		groupcolumns.add(new Column("SecurityToken", DataType.STRING));
		groups.setColumns(groupcolumns);
		for (String s : userSecurityTokens) {
			if (!s.trim().equals("")) {
				Row tokens = new Row();
				tokens.setValues(Arrays.asList(new aero.minova.core.application.system.domain.Value(s.trim(), null),
						new aero.minova.core.application.system.domain.Value("", "!null")));
				groups.addRow(tokens);
			}
		}
		if (groups.getRows().size() > 0) {
			List<Row> groupTokens = svc.getTableForSecurityCheck(groups).getRows();
			List<String> groupSecurityTokens = new ArrayList<>();
			for (Row r : groupTokens) {
				String memberships = r.getValues().get(1).getStringValue();
				// alle SecurityToken einer Gruppe der Liste hinzufügen
				val membershipsAsList = Stream.of(memberships.split("#"))//
						.map(String::trim)//
						.collect(Collectors.toList());
				groupSecurityTokens.addAll(membershipsAsList);
			}

			// verschiedene Rollen/Gruppen können dieselbe Berechtigung haben, deshalb rausfiltern
			for (String string : groupSecurityTokens) {
				if (!userSecurityTokens.contains(string))
					userSecurityTokens.add(string);
			}
		}

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (String string : userSecurityTokens) {
			if (!string.equals(""))
				grantedAuthorities.add(new SimpleGrantedAuthority(string));
		}

		return grantedAuthorities;
	}
}

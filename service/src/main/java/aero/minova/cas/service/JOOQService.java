package aero.minova.cas.service;

import static java.util.Arrays.asList;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.jooq.Query;
import org.jooq.SelectField;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.TableException;
import aero.minova.cas.api.domain.TableMetaData;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.sql.SqlUtils;
import aero.minova.cas.sql.SystemDatabase;
import lombok.val;

@Service
public class JOOQService {

	@Autowired
	SystemDatabase systemDatabase;

	@Autowired
	CustomLogger customLogger;

	@Autowired
	SecurityService securityService;

	public Table executeView(Table inputTable, List<Row> authoritiesForThisTable) throws TableException {
		final val connection = systemDatabase.getConnection();
		Table result = new Table();
		StringBuilder sb = new StringBuilder();
		try {
			inputTable = columnSecurity(inputTable, authoritiesForThisTable);
			TableMetaData inputMetaData = inputTable.getMetaData();
			if (inputTable.getMetaData() == null) {
				inputMetaData = new TableMetaData();
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

			Query query = prepareViewString(inputTable, false, limit, false, authoritiesForThisTable);

			String sql = query.getSQL();
			val preparedStatement = connection.prepareCall(sql);
			val preparedViewStatement = fillPreparedViewString(inputTable, preparedStatement, sql, sb);
			customLogger.logSql("Executing statements: " + sb.toString());
			ResultSet resultSet = preparedViewStatement.executeQuery();

			result = convertSqlResultToTable(inputTable, resultSet);

			int totalResults = 0;
			if (!result.getRows().isEmpty()) {
				totalResults = result.getRows().size();
			}

			// Falls es ein Limit gibt, müssen die auszugebenden Rows begrenzt werden.
			if (limit > 0) {
				List<Row> resultRows = new ArrayList<>();
				for (int i = 0; i < limit; i++) {
					int rowPointer = i + (limit * (page - 1));
					if (rowPointer < result.getRows().size()) {
						resultRows.add(result.getRows().get(rowPointer));
					}
				}
				result.setRows(resultRows);
			}

			result.fillMetaData(result, limit, totalResults, page);

		} catch (Throwable e) {
			customLogger.logError("Statement could not be executed: " + sb.toString(), e);
			throw new TableException(e);
		} finally {
			systemDatabase.freeUpConnection(connection);
		}
		return result;
	}

	/**
	 * das Prepared Statement wird mit den dafür vorgesehenen Parametern befüllt
	 *
	 * @param inputTable
	 *            die Table, welche vom getIndexView aufgerufen wurde
	 * @param preparedStatement
	 *            das Prepared Statement, welches nur noch befüllt werden muss
	 * @return das befüllte, ausführbare Prepared Statement
	 */
	public PreparedStatement fillPreparedViewString(Table inputTable, CallableStatement preparedStatement, String query, StringBuilder sb) {
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
				customLogger.logError("Statement could not be filled: " + sb.toString(), e);
				throw new RuntimeException("msg.ParseError %" + (i + parameterOffset));
			}
		}
		sb.append("\n");
		return preparedStatement;
	}

	public Table convertSqlResultToTable(Table inputTable, ResultSet sqlSet) {
		try {
			Table outputTable = new Table();
			outputTable.setName(inputTable.getName());
			outputTable.setColumns(//
					inputTable.getColumns().stream()//
							.filter(column -> !Objects.equals(column.getName(), Column.AND_FIELD_NAME))//
							.collect(Collectors.toList()));
			while (sqlSet.next()) {
				outputTable.addRow(SqlUtils.convertSqlResultToRow(outputTable, sqlSet, customLogger.getUserLogger(), this));
			}
			return outputTable;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public Query prepareViewString(Table params, boolean autoLike, int maxRows, List<Row> authorities) throws IllegalArgumentException {
		return prepareViewString(params, autoLike, maxRows, false, authorities);
	}

	/**
	 * Diese Methode stammt ursprünglich aus "ch.minova.ncore.data.sql.SQLTools#prepareViewString". Bereitet einen View-String vor und berücksichtigt eine evtl.
	 * angegebene Maximalanzahl Ergebnisse
	 *
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @param maxRows
	 *            maximale Anzahl Ergebnisse (Zeilen), die die Abfrage liefern soll, 0 für unbegrenzt
	 * @param count
	 *            Gibt an ob nur die Anzahl der Ergebniss (Zeilen), gezählt werden sollen.
	 * @return Präparierter View-String, der ausgeführt werden kann
	 * @throws IllegalArgumentException
	 * @author wild
	 */
	public Query prepareViewString(Table params, boolean autoLike, int maxRows, boolean count, List<Row> authorities) throws IllegalArgumentException {

		if (params.getName() == null || params.getName().trim().length() == 0) {
			throw new IllegalArgumentException("msg.ViewNullName");
		}

		// Hier wird auch schon die RowLevelSecurity mit rein gepackt.
		Condition condition = prepareWhereClause(params, autoLike, authorities);

		List<SelectField> fields = new ArrayList();

		for (Column column : params.getColumns()) {
			if (!column.getName().equals(Column.AND_FIELD_NAME)) {
				fields.add(DSL.field(column.getName()));
			}
		}

		Query query;

		if (count) {
			if (condition != null) {
				query = DSL.selectCount().from(params.getName()).where(condition);
			} else {
				query = DSL.selectCount().from(params.getName());
			}
		} else {
			if (condition != null) {
				query = DSL.select(fields).from(params.getName()).where(condition);
			} else {
				query = DSL.select(fields).from(params.getName());
			}
		}

		return query;
	}

	/**
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @return die Where-Klausel für die angegebenen Parameter
	 * @author wild
	 */
	public Condition prepareWhereClause(Table params, boolean autoLike, List<Row> authorities) {

		Condition condition = DSL.noCondition();

		for (Row r : params.getRows()) {
			for (int i = 0; i < r.getValues().size(); i++) {
				Value value = r.getValues().get(i);
				if (value != null && !value.getValue().toString().isBlank() && !params.getColumns().get(i).getName().equals(Column.AND_FIELD_NAME)) {
					String rule = value.getRule();
					if (autoLike && params.getColumns().get(i).getType().equals(DataType.STRING) && (!value.getStringValue().contains("%"))) {
						value = new Value(value.getStringValue() + "%", rule);
					}
					if (rule == null) {
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).eq(r.getValues().get(i).getValue().toString()));
					} else if (rule.equals("~")) {
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).like(value.getValue().toString()));
					} else if (rule.equals("!~")) {
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).notLike(value.getValue().toString()));
					} else if (rule.equals(">")) {
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).gt(r.getValues().get(i).getValue().toString()));
					} else if (rule.equals(">=")) {
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).ge(r.getValues().get(i).getValue().toString()));
					} else if (rule.equals("<")) {
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).lt(r.getValues().get(i).getValue().toString()));
					} else if (rule.equals("<=")) {
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).le(r.getValues().get(i).getValue().toString()));
					} else if (rule.equals("<>")) {
						condition.and(DSL.field(params.getColumns().get(i).getName()).ne(r.getValues().get(i).getValue().toString()));
					} else if (rule.equals("between()")) {
						int separator = value.getValue().toString().indexOf(",");
						String valueA = value.getValue().toString().substring(0, separator - 1);
						String valueB = value.getValue().toString().substring(separator + 1);
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).between(valueA, valueB));
					} else if (rule.equals("in()")) {
						List<String> inValues = Stream.of(value.getValue().toString().split(",")).collect(Collectors.toList());

						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).in(inValues));
					} else if (rule.equals("null")) {
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).isNull());
					} else if (rule.equals("!null")) {
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).isNotNull());
					} else {
						throw new IllegalArgumentException("Invalid rule " + rule + "for value" + value.getValue().toString() + " !");
					}
				}
			}
		}

		// Hier passiert die RowLevelSecurity.
		if (!authorities.isEmpty()) {
			Condition userCondition = rowLevelSecurity(authorities);
			if (userCondition != null) {
				condition = condition.and(userCondition);
			}
		}

		return condition;
	}

	/**
	 * Wie {@link #getIndexView(Table)}, nur ohne die erste Sicherheits-Abfrage, um die maximale Länge zu erhalten Ist nur für die Sicherheitsabfragen gedacht,
	 * um nicht zu viele unnötige SQL-Abfrgane zu machen.
	 *
	 * @param inputTable
	 *            Die Parameter, der SQL-Anfrage die ohne Sicherheitsprüfung durchgeführt werden soll.
	 * @return Das Ergebnis der Abfrage.
	 */
	public Table unsecurelyGetIndexView(Table inputTable) {
		StringBuilder sb = new StringBuilder();
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		Table result = new Table();
		final val connection = systemDatabase.getConnection();
		try {
			final String viewQuery = prepareViewString(inputTable, false, -1, false, userGroups).getSQL();
			val preparedStatement = connection.prepareCall(viewQuery);
			val preparedViewStatement = fillPreparedViewString(inputTable, preparedStatement, viewQuery, sb);
			customLogger.logPrivilege("Executing statement: " + sb.toString());
			ResultSet resultSet = preparedViewStatement.executeQuery();
			result = convertSqlResultToTable(inputTable, resultSet);
		} catch (Exception e) {
			customLogger.logError("Statement could not be executed: " + sb.toString(), e);
			throw new RuntimeException(e);
		} finally {
			systemDatabase.freeUpConnection(connection);
		}
		return result;
	}

	/**
	 * Entfernt alle Spalten der Eingabe-Tabelle, auf die der Nutzer keinen Zugriff hat. Die Sql-Abfrage hat folgendes Format: select TableName, ColumnName,
	 * SecurityToken from xtcasColumnSecurity where (TableName = inputTableName and SecurityToken = UserSecurityToken1) or (TableName = inputTableName and
	 * SecurityToken = UserSecurityToken2) or ... Die Rows der zurückgelieferten Table haben folgendes Format: Row r = [Tabellenname,ColumnName,SecurityToken],
	 * Beispiel: Row r = ["tTestTabelle","Spalte1","User1"] Row r = ["tTestTabelle","Spalte2","User1"]
	 *
	 * @param inputTable
	 *            Enthält den Tabellen-Namen und die Spalten, welche von einem Nutzer angefragt werden.
	 * @param userGroups
	 *            Die Nutzer-Gruppen/Rollen, welche Zugriff auf die Tabelle haben wollen.
	 * @return Diese Tabelle enhtält die Spalten, welche für die Index-View von diesem User verwendet werden dürfen.
	 * @author weber
	 */
	public Table columnSecurity(Table params, List<Row> userGroups) {

		Table inputTable = new Table();

		inputTable.setName(params.getName());
		inputTable.setColumns(params.getColumns());
		inputTable.setRows(params.getRows());

		Table columnSec = new Table();
		columnSec.setName("xtcasColumnSecurity");
		List<Column> columns = new ArrayList<>();
		columns.add(new Column("TableName", DataType.STRING));
		columns.add(new Column("ColumnName", DataType.STRING));
		columns.add(new Column("SecurityToken", DataType.STRING));
		columnSec.setColumns(columns);

		List<Row> columnRestrictionsForThisUserAndThisTable = new ArrayList<>();

		for (Row row : userGroups) {
			if (row.getValues().get(0).getStringValue().equalsIgnoreCase(inputTable.getName())) {

				Row bar = new Row();
				bar.setValues(asList(new Value(inputTable.getName(), null), new Value("", null), new Value(row.getValues().get(1).getStringValue(), null)));
				List<Row> checkRow = new ArrayList<>();
				checkRow.add(bar);
				columnSec.setRows(checkRow);

				List<Row> tokenSpecificAuthorities = unsecurelyGetIndexView(columnSec).getRows();
				// wenn es in der tColumnSecurity keinen Eintrag für diese Tabelle gibt, dann darf der User jede Spalte ansehen
				if (tokenSpecificAuthorities.isEmpty())
					return inputTable;
				columnRestrictionsForThisUserAndThisTable.addAll(tokenSpecificAuthorities);
			}
		}
		List<String> grantedColumns = new ArrayList<String>();
		// die Spaltennamen, welche wir durch den Select erhalten haben in eine List packen, dabei darauf achten,
		// dass verschiedene SecurityTokens dieselbe Erlaubnis haben können, deshalb Doppelte rausfiltern
		for (Row row : columnRestrictionsForThisUserAndThisTable) {
			String grantedColumnFromtColumnSecurity = row.getValues().get(1).getStringValue();
			if (!grantedColumns.contains(grantedColumnFromtColumnSecurity)) {
				grantedColumns.add(grantedColumnFromtColumnSecurity);
			}
		}

		// wenn SELECT *, dann ist wantedColumns leer
		List<Column> wantedColumns = new ArrayList<Column>(inputTable.getColumns());
		if (wantedColumns.isEmpty())
			for (String s : grantedColumns) {
				inputTable.addColumn(new Column(s, DataType.STRING));
			}

		// Hier wird herausgefiltert, welche der angeforderten Spalten(wantedColumns) genehmigt werden können(grantedColumns)
		for (Column column : wantedColumns) {
			if (!grantedColumns.contains(column.getName())) {
				for (Row r : inputTable.getRows()) {
					r.getValues().remove(inputTable.getColumns().indexOf(column));
				}
				inputTable.getColumns().remove(column);
			}
		}

		// falls die Spalten der inputTable danach leer sind, darf wohl keine Spalte gesehen werden
		if (inputTable.getColumns().isEmpty()) {
			RuntimeException exception = new RuntimeException(
					"msg.ColumnSecurityError %" + SecurityContextHolder.getContext().getAuthentication().getName() + " %" + inputTable.getName());
			customLogger.logError("No columns available for this user", exception);
			throw exception;
		}
		return inputTable;
	}

	/**
	 * Fügt an das Ende der Where-Klausel die Abfrage nach den SecurityTokens des momentan eingeloggten Users und dessen Gruppen an Der resultierende String hat
	 * dann folgendes Format: [and/where] ((SecurityToken IS NULL) or (SecurityToken IN (UserSecurityToken1, UserSecurityToken2, ...))
	 * 
	 * @param requestingAtuhorities
	 *            Die Rollen des Nutzers, welche ein Recht auf einen Zugriff haben.
	 * @return eine Condition, der entweder an das Ende der vorhandenen Where-Klausel angefügt wird oder die Where-Klausel selbst ist
	 */
	public Condition rowLevelSecurity(List<Row> requestingAtuhorities) {
		List<String> requestingRoles = new ArrayList<>();
		if (!requestingAtuhorities.isEmpty()) {
			requestingRoles = securityService.extractUserTokens(requestingAtuhorities);
			// Falls die Liste leer ist, darf der User alle Spalten sehen.
			if (requestingRoles.isEmpty()) {
				return null;
			}

			Condition userCondition = DSL.noCondition();

			// Wenn SecurityToken null, dann darf jeder User die Spalte sehen.
			userCondition = userCondition.and(DSL.field("SecurityToken").isNull());

			// Nach allen relevanten SecurityTokens suchen.
			userCondition = userCondition.and(DSL.field("SecurityToken").in(requestingRoles));

			return userCondition;
		} else {
			return null;
		}
	}

	/**
	 * Überprüft, ob es in der xvcasUserPrivileges mindestens einen Eintrag für die User Group des momentan eingeloggten Users gibt. Die Abfrage sieht
	 * folgendermaßen aus: select PrivilegeKeyText,KeyText,RowLevelSecurity from xvcasUserPrivileges where (PrivilegeKeyText = privilegeName and KeyText =
	 * UserSecurityToken1) or (PrivilegeKeyText = privilegeName and KeyText = UserSecurityToken2) or ... Die erzeugten Rows haben folgendes Format: Row r =
	 * [Tabellenname,UserSecurityToken,RowLevelSecurity], Beispiel: Row r = ["tTestTabelle","User1",1], Row r2 = ["tTestTabelle","User2",0]
	 *
	 * @param privilegeName
	 *            Das Privilege, für das ein Recht eingefordert wird.
	 * @return Enthält alle Gruppen, die Ein Recht auf das Privileg haben.
	 **/
	public List<Row> getPrivilegePermissions(String privilegeName) {
		@SuppressWarnings("unchecked")
		List<GrantedAuthority> allUserAuthorities = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		Table userPrivileges = new Table();
		userPrivileges.setName("xvcasUserPrivileges");
		List<Column> columns = new ArrayList<>();
		columns.add(new Column("PrivilegeKeyText", DataType.STRING));
		columns.add(new Column("KeyText", DataType.STRING));
		columns.add(new Column("RowLevelSecurity", DataType.BOOLEAN));
		columns.add(Column.AND_FIELD);
		userPrivileges.setColumns(columns);

		for (GrantedAuthority ga : allUserAuthorities) {
			Row tableNameAndUserToken = new Row();
			tableNameAndUserToken
					.setValues(asList(new Value(privilegeName, null), new Value(ga.getAuthority(), null), new Value("", null), new Value(false, null)));
			userPrivileges.addRow(tableNameAndUserToken);
		}
		return unsecurelyGetIndexView(userPrivileges).getRows();
	}
}

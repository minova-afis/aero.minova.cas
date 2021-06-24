package aero.minova.core.application.system.controller;

import static java.util.Arrays.asList;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.TableException;
import aero.minova.core.application.system.domain.TableMetaData;
import aero.minova.core.application.system.domain.Value;
import aero.minova.core.application.system.sql.SqlUtils;
import aero.minova.core.application.system.sql.SystemDatabase;
import lombok.val;

@RestController
public class SqlViewController {

	@Autowired
	SystemDatabase systemDatabase;
	Logger logger = LoggerFactory.getLogger(SqlViewController.class);

	@Autowired
	Gson gson;

	@GetMapping(value = "data/index", produces = "application/json")
	public Table getIndexView(@RequestBody Table inputTable) throws Exception {
		logger.info("data/view: " + gson.toJson(inputTable));
		final val connection = systemDatabase.getConnection();
		Table result = new Table();
		StringBuilder sb = new StringBuilder();
		try {
			@SuppressWarnings("unchecked")
			List<GrantedAuthority> allUserAuthorities = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			List<Row> authoritiesForThisTable = getPrivilegePermissions(allUserAuthorities, inputTable.getName()).getRows();
			if (authoritiesForThisTable.isEmpty()) {
				throw new RuntimeException("msg.PrivilegeError %" + inputTable.getName());
			}
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
			val viewQuery = pagingWithSeek(inputTable, false, limit, false, page, authoritiesForThisTable);
			val preparedStatement = connection.prepareCall(viewQuery);
			val preparedViewStatement = fillPreparedViewString(inputTable, preparedStatement, viewQuery, sb);
			logger.info("Executing statements: " + sb.toString());
			ResultSet resultSet = preparedViewStatement.executeQuery();

			result = convertSqlResultToTable(inputTable, resultSet);
			int viewCount = 0;
			if (result.getRows().size() > 0) {
				viewCount = result.getRows().size();
			}
			result.fillMetaData(result, limit, viewCount, page);

		} catch (Exception e) {
			logger.error("Statement could not be executed: " + e.getMessage());
			throw new TableException(e);
		} finally {
			systemDatabase.freeUpConnection(connection);
		}
		return result;
	}

	public Table unsecurelyGetIndexView(@RequestBody Table inputTable, List<Row> requestingAuthorities) throws Exception {
		final val connection = systemDatabase.getConnection();
		Table result = new Table();
		StringBuilder sb = new StringBuilder();
		try {
			val viewQuery = pagingWithSeek(inputTable, false, -1, false, 1, requestingAuthorities);
			val preparedStatement = connection.prepareCall(viewQuery);
			val preparedViewStatement = fillPreparedViewString(inputTable, preparedStatement, viewQuery, sb);
			logger.info("Executing statements: " + sb.toString());
			ResultSet resultSet = preparedViewStatement.executeQuery();

			result = convertSqlResultToTable(inputTable, resultSet);
		} catch (Exception e) {
			logger.error("Statement could not be executed: " + e.getMessage(), e);
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
	private PreparedStatement fillPreparedViewString(Table inputTable, CallableStatement preparedStatement, String query, StringBuilder sb) {
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
				if (!(iVal == null)) {
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
						List<String> inBetweenValues = new ArrayList<>();
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
						List<String> inBetweenValues = new ArrayList<>();
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
				logger.error("Statement could not be filled: " + sb.toString(), e);
				throw new RuntimeException("msg.ParseError %" + (i + parameterOffset));
			}
		}
		sb.append("\n");
		return preparedStatement;
	}

	/**
	 * Überprüft, ob es in der vCASUserPrivileges mindestens einen Eintrag für die User Group des momentan eingeloggten Users gibt.
	 *
	 * @param securityTokens
	 *            Die Gruppen, die dem anfordenden gehören.
	 * @param privilegeName
	 *            Das Privilege, für das ein Recht eingefordert wird.
	 * @return Enthält alle Gruppen, die Ein Recht auf das Privileg haben.
	 **/
	public Table getPrivilegePermissions(List<GrantedAuthority> securityTokens, String privilegeName) {
		Table userPrivileges = new Table();
		userPrivileges.setName("xvcasUserPrivileges");
		List<Column> columns = new ArrayList<>();
		columns.add(new Column("PrivilegeKeyText", DataType.STRING));
		columns.add(new Column("KeyText", DataType.STRING));
		columns.add(new Column("RowLevelSecurity", DataType.BOOLEAN));
		columns.add(Column.AND_FIELD);
		userPrivileges.setColumns(columns);

		List<String> userTokens = new ArrayList<>();
		for (GrantedAuthority ga : securityTokens) {
			userTokens.add(ga.getAuthority());
		}

		for (String s : userTokens) {
			Row tableNameAndUserToken = new Row();
			tableNameAndUserToken.setValues(asList(new Value(privilegeName, null), new Value(s, null), new Value("", null), new Value(false, null)));
			userPrivileges.addRow(tableNameAndUserToken);
		}
		return getTableForSecurityCheck(userPrivileges);
	}

	/**
	 * Wie {@link #getIndexView(Table)}, nur ohne die erste Sicherheits-Abfrage, um die maximale Länge zu erhalten Ist nur für die Sicherheitsabfragen gedacht,
	 * um nicht zu viele unnötige SQL-Abfrgane zu machen.
	 *
	 * @param inputTable
	 *            Die Parameter, der SQL-Anfrage die ohne Sicherheitsprüfung durchgeführt werden soll.
	 * @return Das Ergebnis der Abfrage.
	 */
	public Table getTableForSecurityCheck(Table inputTable) {
		StringBuilder sb = new StringBuilder();
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		final val connection = systemDatabase.getConnection();
		try {
			final val viewQuery = prepareViewString(inputTable, false, 1000, false, userGroups);
			val preparedStatement = connection.prepareCall(viewQuery);
			val preparedViewStatement = fillPreparedViewString(inputTable, preparedStatement, viewQuery, sb);
			logger.info("Executing statement: " + sb.toString());
			ResultSet resultSet = preparedViewStatement.executeQuery();
			val result = convertSqlResultToTable(inputTable, resultSet);
			return result;
		} catch (Exception e) {
			logger.error("Statement could not be executed: " + sb.toString(), e);
			throw new RuntimeException(e);
		}
	}

	protected Table convertSqlResultToTable(Table inputTable, ResultSet sqlSet) {
		try {
			Table outputTable = new Table();
			outputTable.setName(inputTable.getName());
			outputTable.setColumns(//
					inputTable.getColumns().stream()//
							.filter(column -> !Objects.equals(column.getName(), Column.AND_FIELD_NAME))//
							.collect(Collectors.toList()));
			while (sqlSet.next()) {
				outputTable.addRow(SqlUtils.convertSqlResultToRow(outputTable, sqlSet, logger, this));
			}
			return outputTable;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	String prepareViewString(Table params, boolean autoLike, int maxRows, List<Row> authorities) throws IllegalArgumentException {
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
	String prepareViewString(Table params, boolean autoLike, int maxRows, boolean count, List<Row> authorities) throws IllegalArgumentException {
		final StringBuffer sb = new StringBuffer();
		if (params.getName() == null || params.getName().trim().length() == 0) {
			throw new IllegalArgumentException("msg.ViewNullName");
		}

		if (count) {
			sb.append("select count(1) from ");
		} else {
			if (maxRows > 0) {
				sb.append("select top ").append(maxRows).append(" ");
			}
			val outputFormat = params.getColumns().stream()//
					.filter(c -> !Objects.equals(c.getName(), Column.AND_FIELD_NAME))//
					.collect(Collectors.toList());
			if (outputFormat.isEmpty()) {
				sb.append("* from ");
			} else {
				sb.append(//
						outputFormat.stream()//
								.map(Column::getName)//
								.collect(Collectors.joining(", ")));
				sb.append(" from ");
			}
		}
		sb.append(params.getName());
		boolean whereClauseExists = false;
		if (params.getColumns().size() > 0 && params.getRows().size() > 0) {
			final String where = prepareWhereClause(params, autoLike);
			sb.append(where);
			if (!where.trim().equals("")) {
				whereClauseExists = true;
				sb.append(")");
			}
		}

		final String onlyAuthorizedRows = rowLevelSecurity(whereClauseExists, authorities);
		sb.append(onlyAuthorizedRows);

		return sb.toString();
	}

	/*
	 * Pagination nach der Seek-Methode; bessere Performance als Offset bei großen Datensätzen
	 */
	public String pagingWithSeek(Table params, boolean autoLike, int maxRows, boolean count, int page, List<Row> authorities) {
		final StringBuffer sb = new StringBuffer();
		if (params.getName() == null || params.getName().trim().length() == 0) {
			throw new IllegalArgumentException("msg.ViewNullName");
		}
		sb.append("select ");
		val outputFormat = params.getColumns().stream()//
				.filter(c -> !Objects.equals(c.getName(), Column.AND_FIELD_NAME))//
				.collect(Collectors.toList());
		if (outputFormat.isEmpty()) {
			sb.append("* from ");
		} else {
			sb.append(//
					outputFormat.stream()//
							.map(Column::getName)//
							.collect(Collectors.joining(", ")));
			sb.append(" from ");
		}

		sb.append("( select Row_Number() over (order by KeyLong) as RowNum, * from ").append(params.getName());
		boolean whereClauseExists = false;
		if (params.getColumns().size() > 0 && params.getRows().size() > 0) {
			final String where = prepareWhereClause(params, autoLike);
			sb.append(where);
			if (!where.trim().equals("")) {
				whereClauseExists = true;
				sb.append(")");
			}
		}
		final String onlyAuthorizedRows = rowLevelSecurity(whereClauseExists, authorities);
		sb.append(onlyAuthorizedRows);
		sb.append(" ) as RowConstraintResult");

		if (page > 0) {
			sb.append("\r\nwhere RowNum > " + ((page - 1) * maxRows));
			// bei 0 sollen einfach alle Ergebnisse ausgegeben werden
			if (maxRows > 0) {
				sb.append("\r\nand RowNum <= " + (page * maxRows) + " order by RowNum");
			}
		}
		return sb.toString();
	}

	/**
	 * Entfernt alle spalten der Eingabe-Tabelle, auf die der Nutzer keinen Zugriff hat.
	 * <p>
	 * TODO Idee: Mann sollte eine neue Tabelle erstellen, statt die eingabe abzuändern, da die Methoden-Signature impliziert, dass die InputTable nicht
	 * geändert wird.
	 *
	 * @param inputTable
	 *            Enthält den Tabellen-Namen und die Spalten, welche von einem Nutzer angefragt werden.
	 * @param userGroups
	 *            Die Nutzer-Gruppen/Rollen, welche Zugriff auf die Tabelle haben wollen.
	 * @return Diese Tabelle enhtält die Spalten, welche für die Index-View von diesem User verwendet werden dürfen.
	 * @author weber
	 */
	public Table columnSecurity(Table inputTable, List<Row> userGroups) {
		Table columnSec = new Table();
		columnSec.setName("xtcasColumnSecurity");
		List<Column> columns = new ArrayList<>();
		columns.add(new Column("TableName", DataType.STRING));
		columns.add(new Column("ColumnName", DataType.STRING));
		columns.add(new Column("SecurityToken", DataType.STRING));
		columnSec.setColumns(columns);

		List<Row> result = new ArrayList<>();
		for (Row row : userGroups) {
			if (row.getValues().get(0).getStringValue().equals(inputTable.getName())) {
				Row bar = new Row();
				bar.setValues(asList(new Value(inputTable.getName(), null), new Value("", null), new Value(row.getValues().get(1).getStringValue(), null)));
				List<Row> checkRow = new ArrayList<>();
				checkRow.add(bar);
				columnSec.setRows(checkRow);
				List<Row> tokenSpecificAuthorities = getTableForSecurityCheck(columnSec).getRows();
				// wenn es in der tColumnSecurity keinen Eintrag für diese Tabelle gibt, dann darf der User jede Spalte ansehen
				if (tokenSpecificAuthorities.isEmpty())
					return inputTable;
				result.addAll(tokenSpecificAuthorities);
			}
		}
		List<String> grantedColumns = new ArrayList<String>();
		// die Spaltennamen, welche wir durch den Select erhalten haben in eine List packen, dabei darauf achten,
		// dass verschiedene SecurityTokens dieselbe Erlaubnis haben können, deshalb Doppelte rausfiltern
		for (Row row : result) {
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
			throw new RuntimeException(
					"msg.ColumnSecurityError %" + SecurityContextHolder.getContext().getAuthentication().getName() + " %" + inputTable.getName());
		}
		return inputTable;
	}

	/**
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @return die Where-Klausel für die angegebenen Parameter
	 * @author wild
	 */
	protected String prepareWhereClause(Table params, boolean autoLike) {
		final StringBuffer where = new StringBuffer();
		final boolean hasAndClause;
		// TODO Check size
		val andFields = params.getColumns().stream()//
				.filter(c -> Objects.equals(c.getName(), Column.AND_FIELD_NAME))//
				.collect(Collectors.toList());
		final Column andField;
		if (andFields.isEmpty()) {
			hasAndClause = false;
			andField = null;
		} else {
			hasAndClause = true;
			andField = andFields.get(0);
		}
		val andFieldIndex = params.getColumns().indexOf(andField);
		for (int rowI = 0; rowI < params.getRows().size(); rowI++) {
			final Row r = params.getRows().get(rowI);
			// TODO Nicht annehmen, dass die spezielle &-Spalte die letzte Spalte ist.
			final boolean and;
			if (hasAndClause) {
				and = r.getValues().get(andFieldIndex).getBooleanValue();
			} else {
				and = false;
			}

			// Eine where Zeile aufbauen
			final StringBuffer clause = new StringBuffer();
			COLS: for (int colI = 0; colI < r.getValues().size(); ++colI) {
				val def = r.getValues().get(colI);
				val col = params.getColumns().get(colI);
				if (Column.AND_FIELD_NAME.equalsIgnoreCase(col.getName())) {
					continue COLS;
				}
				if (r.getValues().get(colI) == null) {
					continue COLS;
				}

				final Object valObj = r.getValues().get(colI).getValue();
				String strValue = valObj.toString().trim();
				String ruleValue = r.getValues().get(colI).getRule();
				if (strValue != null && strValue.length() != 0) {
					if (clause.length() > 0) {
						clause.append(" and ");
					}
					clause.append(col.getName());

					if (ruleValue != null && ruleValue.length() != 0) {
						if (ruleValue.contains("in")) {
							clause.append(" in(");

							// für jeden der Komma-getrennten Werte muss ein Fragezeichen da sein
							String valuesSeperatedByString = Stream.of(strValue.split(",")) //
									.map(s -> "?").collect(Collectors.joining(", "));

							clause.append(valuesSeperatedByString).append(")");
						} else if (ruleValue.contains("between")) {
							clause.append(" between ? and ?");
						} else {
							clause.append(" ").append(ruleValue).append(' ').append("?");
						}
					} else {
						if (autoLike && valObj instanceof String && def.getType() == DataType.STRING && (!strValue.contains("%"))) {
							strValue += "%";
							params.getRows().get(rowI).getValues().get(colI).setValue(strValue);
						}
						if (def.getType() == DataType.STRING && (strValue.contains("%") || strValue.contains("_"))) {
							clause.append(" like");
						} else {
							clause.append(" =");
						}
						clause.append(' ').append("?");
					}
					// falls im Wert-Feld nichts steht, könnte immer noch die Regel is null oder is not null angefragt werden
				} else if (ruleValue != null) {
					if (ruleValue.contains("!null")) {
						if (clause.length() > 0) {
							clause.append(" and ");
						}
						clause.append(col.getName()).append(" is not null");
					} else if (ruleValue.contains("null")) {
						if (clause.length() > 0) {
							clause.append(" and ");
						}
						clause.append(col.getName()).append(" is null");
					}
				}
			}

			// Wenn es etwas gab, dann fügen wir diese Zeile der kompletten WHERE-clause hinzu
			if (clause.length() > 0) {
				if (where.length() == 0) {
					where.append("\r\nwhere (");
				} else {
					where.append(and ? "\r\n  and " : "\r\n   or ");
				}
				where.append('(').append(clause.toString()).append(')');
			}
		}
		return where.toString();
	}

	protected List<String> checkUserTokens(List<Row> requestingAtuhorities) {
		List<String> requestingRoles = new ArrayList<>();
		boolean checkNeeded = true;

		for (Row authority : requestingAtuhorities) {
			// falls auch nur einmal false in der RowLevelSecurity-Spalte vorkommt, darf der User die komplette Tabelle sehen
			if (!authority.getValues().get(2).getBooleanValue()) {
				checkNeeded = false;
			}
			if (checkNeeded) {
				// hier sind die Rollen/UserSecurityToken, welche authorisiert sind, auf die Tabelle zuzugreifen
				String value = authority.getValues().get(1).getStringValue().trim();
				if ((!value.equals("")) && (!requestingRoles.contains(value))) {
					requestingRoles.add(authority.getValues().get(1).getStringValue());
				}
			}
		}
		return requestingRoles;
	}

	/**
	 * Fügt an das Ende der Where-Klausel die Abfrage nach den SecurityTokens des momentan eingeloggten Users und dessen Gruppen an
	 *
	 * @param isFirstWhereClause
	 *            Abhängig davon, ob bereits eine where-Klausel besteht oder nicht, muss 'where' oder 'and' vorne angefügt werden
	 * @param requestingAtuhorities
	 *            Die Rollen des Nutzers, welche ein Recht auf einen Zugriff haben.
	 * @return einen String, der entweder an das Ende der vorhandenen Where-Klausel angefügt wird oder die Where-Klausel selbst ist
	 */
	protected String rowLevelSecurity(boolean isFirstWhereClause, List<Row> requestingAtuhorities) {

		List<String> requestingRoles = checkUserTokens(requestingAtuhorities);
		// falls die Liste leer ist, darf der User alle Spalten sehen
		if (requestingRoles.isEmpty()) {
			return "";
		}

		final StringBuffer rowSec = new StringBuffer();
		// Falls where-Klausel bereits vorhanden 'and' anfügen, wenn nicht, dann 'where'
		if (isFirstWhereClause) {
			rowSec.append("\r\nand (");
		} else {
			rowSec.append("\r\nwhere (");
		}
		// Wenn SecurityToken null, dann darf jeder User die Spalte sehen
		rowSec.append(" ( SecurityToken IS NULL )");

		if (requestingRoles.size() > 0) {
			rowSec.append("\r\nor ( SecurityToken IN (");
			for (String r : requestingRoles) {
				rowSec.append("'").append(r.trim()).append("',");
			}
			rowSec.deleteCharAt(rowSec.length() - 1);
			rowSec.append(") )");
		}
		rowSec.append(" )");
		return rowSec.toString();
	}
}
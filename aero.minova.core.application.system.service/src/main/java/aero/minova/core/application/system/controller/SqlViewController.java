package aero.minova.core.application.system.controller;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.TableMetaData;
import aero.minova.core.application.system.domain.Value;
import aero.minova.core.application.system.sql.SqlUtils;
import aero.minova.core.application.system.sql.SystemDatabase;
import lombok.val;

@RestController
public class SqlViewController {
	public static final String SQL_IS_NULL = "is null";
	public static final String SQL_IS_NOT_NULL = "is not null";
	public static final String[] SQL_OPERATORS = { "<>", "<=", ">=", "<", ">", "=", "between(", "in(", "not like", "like", SQL_IS_NULL, SQL_IS_NOT_NULL };

	@Autowired
	SystemDatabase systemDatabase;
	Logger logger = LoggerFactory.getLogger(SqlViewController.class);

	@GetMapping(value = "data/index", produces = "application/json")
	public Table getIndexView(@RequestBody Table inputTable) {
		@SuppressWarnings("unchecked")
		List<GrantedAuthority> allUserAuthorities = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		List<Row> authoritiesForThisTable = checkPrivilege(allUserAuthorities, inputTable.getName()).getRows();
		if (authoritiesForThisTable.isEmpty())
			throw new RuntimeException("Insufficient Permission for " + inputTable.getName());
		try {
			Table accessableTable = columnSecurity(inputTable, authoritiesForThisTable);
			inputTable = accessableTable;
			val countQuery = prepareViewString(inputTable, false, 1000, true, authoritiesForThisTable);
			logger.info("Executing: " + countQuery);
			val viewCounter = systemDatabase//
					.connection()//
					.prepareCall(countQuery)//
					.executeQuery();
			viewCounter.next();
			val viewCount = viewCounter.getInt(1);
			val limit = Optional.ofNullable(inputTable.getMetaData())//
					.map(TableMetaData::getLimited)//
					.orElse(Integer.MAX_VALUE);
			val viewQuery = prepareViewString(inputTable, false, limit, false, authoritiesForThisTable);
			logger.info("Executing: " + viewQuery);
			ResultSet resultSet = systemDatabase.connection()//
					.prepareCall(viewQuery)//
					.executeQuery();
			val result = convertSqlResultToTable(inputTable, resultSet);
			if (limit < viewCount) {
				if (result.getMetaData() == null) {
					result.setMetaData(new TableMetaData());
				}
				result.getMetaData().setLimited(limit);
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Überprüft, ob es in der vCASUserPrivileges mindestens einen Eintrag für die User Group des momentan eingeloggten Users gibt
	 **/
	public Table checkPrivilege(List<GrantedAuthority> securityToken, String privilegeName) {
		Table userPrivileges = new Table();
		userPrivileges.setName("vCASUserPrivileges");
		List<Column> columns = new ArrayList<>();
		columns.add(new Column("PrivilegeKeyText", DataType.STRING));
		columns.add(new Column("KeyText", DataType.STRING));
		columns.add(new Column("RowLevelSecurity", DataType.BOOLEAN));
		columns.add(Column.AND_FIELD);
		userPrivileges.setColumns(columns);

		List<String> userTokens = new ArrayList<>();
		for (GrantedAuthority ga : securityToken) {
			userTokens.add(ga.getAuthority());
		}

		for (String s : userTokens) {
			Row tableNameAndUserToken = new Row();
			tableNameAndUserToken.setValues(Arrays.asList(new Value(privilegeName), new Value(s), new Value(""), new Value(false)));
			userPrivileges.addRow(tableNameAndUserToken);
		}
		return getTableForSecurityCheck(userPrivileges);
	}

	/*
	 * Wie indexView, nur ohne die erste Abfrage, um die maximale Länge zu erhalten Ist nur für die Sicherheitsabfragen gedacht, um nicht zu viele unnötige
	 * SQL-Abfrgane zu machen
	 */
	public Table getTableForSecurityCheck(Table inputTable) {
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value(""));
		inputRow.addValue(new Value(""));
		inputRow.addValue(new Value(false));
		userGroups.add(inputRow);
		try {

			// TODO: Änderung aus Issue #8 (SQL Injection verhindern) auch für die Methode aktivieren
//			final val viewQuery = prepareViewString(inputTable, false, limit, false);
//			logger.info("Executing: " + viewQuery);
//			val preparedStatement = connection.prepareCall(viewQuery);
//			val preparedViewStatement = fillPreparedViewString(inputTable, preparedStatement);
//			ResultSet resultSet = preparedViewStatement.executeQuery();

			val viewQuery = prepareViewString(inputTable, false, 1000, false, userGroups);
			logger.info("Executing: " + viewQuery);
			ResultSet resultSet = systemDatabase.connection()//
					.prepareCall(viewQuery)//
					.executeQuery();
			val result = convertSqlResultToTable(inputTable, resultSet);
			return result;
		} catch (Exception e) {
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
	 * @author wild
	 * @throws IllegalArgumentException
	 */
	String prepareViewString(Table params, boolean autoLike, int maxRows, boolean count, List<Row> authorities) throws IllegalArgumentException {
		final StringBuffer sb = new StringBuffer();
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
			if (!where.trim().equals(""))
				whereClauseExists = true;
		}

		final String onlyAuthorizedRows = rowLevelSecurity(whereClauseExists, authorities);
		sb.append(onlyAuthorizedRows);

		return sb.toString();
	}

	/**
	 * @param inputTable
	 *            die Tabelle mit den Spalten, welche angefragt werden
	 * @return Tabelle mit bereits konfigurierten Spalten, welche für die Index-View von diesem User verwendet werden dürfen
	 * @author weber
	 */
	public Table columnSecurity(Table inputTable, List<Row> userGroups) {
		Table foo = new Table();
		foo.setName("tColumnSecurity");
		List<Column> columns = new ArrayList<>();
		columns.add(new Column("TableName", DataType.STRING));
		columns.add(new Column("ColumnName", DataType.STRING));
		columns.add(new Column("SecurityToken", DataType.STRING));
		foo.setColumns(columns);

		List<Row> result = new ArrayList<>();
		for (Row row : userGroups) {
			if (row.getValues().get(0).getStringValue().equals(inputTable.getName())) {
				Row bar = new Row();
				bar.setValues(Arrays.asList(new Value(inputTable.getName()), new Value(""), new Value(row.getValues().get(1).getStringValue())));
				List<Row> checkRow = new ArrayList<>();
				checkRow.add(bar);
				foo.setRows(checkRow);
				List<Row> tokenSpecificAuthorities = getTableForSecurityCheck(foo).getRows();
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
			throw new RuntimeException("Insufficient Permission for " + inputTable.getName() + "; User with Username '"
					+ SecurityContextHolder.getContext().getAuthentication().getName() + "' is not allowed to see the selected columns of this table");
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
				if (strValue != null && strValue.length() != 0) {
					if (clause.length() > 0) {
						clause.append(" and ");
					}
					clause.append(col.getName());

					// #13193
					if (strValue.equalsIgnoreCase("null") || strValue.equalsIgnoreCase("not null")) {
						strValue = "is " + strValue;
					}
					if (!hasOperator(strValue)) {
						if (autoLike && valObj instanceof String && def.getType() == DataType.STRING && !strValue.contains("%")) {
							strValue += "%";
						}

						if (def.getType() == DataType.STRING && (strValue.contains("%") || strValue.contains("_"))) {
							clause.append(" like");
						} else {
							clause.append(" =");
						}
					}

					strValue = encloseInCommasIfRequired(def, strValue);
					clause.append(' ').append(strValue);
				}
			}

			// Wenn es etwas gab, dann fügen wir diese Zeile der kompletten WHERE-clause hinzu
			if (clause.length() > 0) {
				if (where.length() == 0) {
					where.append("\r\nwhere ");
				} else {
					where.append(and ? "\r\n  and " : "\r\n   or ");
				}
				where.append('(').append(clause.toString()).append(')');
			}
		}

		return where.toString();
	}

	/**
	 * Fügt an das Ende der Where-Klausel die Abfrage nach den SecurityTokens des momentan eingeloggten Users und dessen Gruppen an
	 * 
	 * @param boolean
	 *            Abhängig davon, ob bereits eine where-Klausel besteht oder nicht, muss 'where' oder 'and' vorne angefügt werden
	 * @return einen String, der entweder an das Ende der vorhandenen Where-Klausel angefügt wird oder die Where-Klausel selbst ist
	 */
	protected String rowLevelSecurity(boolean where, List<Row> authorities) {

		List<String> roles = new ArrayList<>();

		for (Row row : authorities) {
			if (!row.getValues().get(2).getBooleanValue())
				return "";
			String value = row.getValues().get(1).getStringValue().trim();
			if ((!value.equals("")) && (!roles.contains(value)))
				roles.add(row.getValues().get(1).getStringValue());
		}

		final StringBuffer rowSec = new StringBuffer();
		// Falls where-Klausel bereits vorhanden 'and' anfügen, wenn nicht, dann 'where'
		if (where) {
			rowSec.append("\r\nand (");
		} else {
			rowSec.append("\r\nwhere (");
		}
		// Wenn SecurityToken null, dann darf jeder User die Spalte sehen
		rowSec.append(" ( SecurityToken IS NULL )");

		@SuppressWarnings("unchecked")
		List<GrantedAuthority> allUserAuthorities = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

		if (allUserAuthorities.size() > 0) {
			rowSec.append("\r\nor ( SecurityToken IN (");
			for (GrantedAuthority ga : allUserAuthorities) {
				rowSec.append("'").append(ga.getAuthority().trim()).append("',");
			}
			rowSec.deleteCharAt(rowSec.length() - 1);
			rowSec.append(") )");
		}
		rowSec.append(" )");
		return rowSec.toString();
	}

	/**
	 * Abhängig von dem Feld-Typ, wird der Wert von Kommas umgeben oder nicht
	 */
	protected static String encloseInCommasIfRequired(Value vd, String value) {
		if (vd == null || value == null) {
			return value;
		}

		if (SQL_IS_NULL.equalsIgnoreCase(value.trim()) || SQL_IS_NOT_NULL.equalsIgnoreCase(value.trim())) {
			return value;
		} else if (vd.getType() == DataType.STRING || vd.getType().equals(DataType.INSTANT) || vd.getType().equals(DataType.ZONED)) {
			final int i = getOperatorEndIndex(value);
			if (i > 0) {
				return value.substring(0, i) + " '" + value.substring(i).trim() + "'";
			} else {
				return "'" + value.substring(i).trim() + "'";
			}
		}
		return value;
	}

	/**
	 * Prüft, ob der String einen SQL Operator am Anfang hat
	 * 
	 * @param value
	 * @return
	 */
	protected static boolean hasOperator(String value) {
		return getOperatorEndIndex(value) != 0;
	}

	/**
	 * Wenn es einen Operator gibt, dann liefert die Funktion den Index bis zu dem sich der Operator erstreckt
	 * 
	 * @param value
	 * @return 0, wenn es keinen Operator gibt
	 */
	protected static int getOperatorEndIndex(String value) {
		if (value == null || value.length() == 0) {
			return 0;
		}
		// Wir simulieren einen ltrim, um die Anfangsposition des Operators festzustellen
		String tmp = (value + "_").trim();
		tmp = tmp.toLowerCase(Locale.ENGLISH).substring(0, tmp.length() - 1);
		final int shift = value.length() - tmp.length();
		for (final String sqlOperator : SQL_OPERATORS) {
			if (tmp.startsWith(sqlOperator)) {
				return shift + sqlOperator.length();
			}
		}
		return 0;
	}
}
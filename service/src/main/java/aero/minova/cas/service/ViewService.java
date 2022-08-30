package aero.minova.cas.service;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
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
public class ViewService {

	@Autowired
	SystemDatabase systemDatabase;

	@Autowired
	CustomLogger customLogger;
	@Autowired
	private SecurityService securityService;

	public Table executeView(Table inputTable, List<Row> authoritiesForThisTable) throws TableException {
		final val connection = systemDatabase.getConnection();
		Table result = new Table();
		StringBuilder sb = new StringBuilder();
		try {
			inputTable = securityService.columnSecurity(inputTable, authoritiesForThisTable);
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

			// POSTGRE SQL verwendet RowCount als Funktion, wesewegen es nicht so genutzt werden kann, wie wir es bei der pagingWithSeek-Methode verwenden.
			// Deshalb verwenden wir stattdessen die prepareViewString-Methode, welche minimal langsamer ist.
			// Die pagingWithSeek-Methode benötigt immer einen KeyLong in der Anfrage. Es gibt allerdings auch einige Anfragen, die keinen KeyLong benötigen,
			// weswegen dann Fehlermeldungen geworfen werden. Deshalb wird ab jetzt einfach die prepareViewString-Methode verwendet.
			String viewQuery = prepareViewString(inputTable, false, 0, authoritiesForThisTable);
			val preparedStatement = connection.prepareCall(viewQuery);
			val preparedViewStatement = fillPreparedViewString(inputTable, preparedStatement, viewQuery, sb);
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

	public String prepareViewString(Table params, boolean autoLike, int maxRows, List<Row> authorities) throws IllegalArgumentException {
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
	public String prepareViewString(Table params, boolean autoLike, int maxRows, boolean count, List<Row> authorities) throws IllegalArgumentException {
		final StringBuffer sb = new StringBuffer();
		if (params.getName() == null || params.getName().trim().length() == 0) {
			throw new IllegalArgumentException("msg.ViewNullName");
		}

		if (count) {
			sb.append("select count(1) from ");
		} else {
			if (maxRows > 0) {
				sb.append("select top ").append(maxRows).append(" ");
			} else {
				sb.append("select ");
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
		if (!params.getColumns().isEmpty() && !params.getRows().isEmpty()) {
			final String where = prepareWhereClause(params, autoLike);
			sb.append(where);
			if (!where.trim().equals("")) {
				whereClauseExists = true;
				sb.append(")");
			}
		}

		final String onlyAuthorizedRows = securityService.rowLevelSecurity(whereClauseExists, authorities);
		sb.append(onlyAuthorizedRows);

		return sb.toString();
	}

	/*
	 * Pagination nach der Seek-Methode; bessere Performance als Offset bei großen Datensätzen. Wird NICHT für den "normalen" Index-Aufruf verwendet, da immer
	 * davon ausgegangen wird, dass ein KeyLong in der View/Table vorhanden ist.
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
		if (!params.getColumns().isEmpty() && !params.getRows().isEmpty()) {
			final String where = prepareWhereClause(params, autoLike);
			sb.append(where);
			if (!where.trim().equals("")) {
				whereClauseExists = true;
				sb.append(")");
			}
		}
		final String onlyAuthorizedRows = securityService.rowLevelSecurity(whereClauseExists, authorities);
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
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @return die Where-Klausel für die angegebenen Parameter
	 * @author wild
	 */
	public String prepareWhereClause(Table params, boolean autoLike) {
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

			// Wenn es etwas gab, dann fügen wir diese Zeile der kompletten WHERE-clause hinzu.
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
}

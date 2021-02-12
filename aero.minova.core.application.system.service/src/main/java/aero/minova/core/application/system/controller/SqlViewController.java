package aero.minova.core.application.system.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.ErrorMessage;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
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

	@GetMapping(value = "data/index", produces = "application/json")
	public Table getIndexView(@RequestBody Table inputTable) {
		final val connection = systemDatabase.getConnection();
		Table result = new Table();
		try {
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
				throw new IllegalArgumentException("Page must be higher than 0");
			} else {
				page = inputMetaData.getPage();
			}
			// falls nichts als Size/maxRows angegeben wurde, wird angenommen, dass alles ausgegeben werden soll; alles = 0
			if (inputMetaData.getLimited() == null) {
				limit = 0;
			} else if (inputMetaData.getLimited() < 0) {
				throw new IllegalArgumentException("Limited must be higher or equal to 0");
			} else {
				limit = inputMetaData.getLimited();
			}
			final val countQuery = prepareViewString(inputTable, false, 1, true);
			logger.info("Executing: " + countQuery);
			val preparedCountStatement = connection.prepareCall(countQuery);
			PreparedStatement callableCountStatement = fillPreparedViewString(inputTable, preparedCountStatement);
			ResultSet viewCounter = callableCountStatement.executeQuery();
			viewCounter.next();
			val viewCount = viewCounter.getInt(1);

			final val viewQuery = pagingWithSeek(inputTable, false, limit, false, page);
			logger.info("Executing: " + viewQuery);
			val preparedStatement = connection.prepareCall(viewQuery);
			val preparedViewStatement = fillPreparedViewString(inputTable, preparedStatement);
			ResultSet resultSet = preparedViewStatement.executeQuery();

			result = convertSqlResultToTable(inputTable, resultSet);
			result.fillMetaData(result, limit, viewCount, page);

		} catch (Exception e) {
			Exception sqlE = new Exception("Couldn't execute query: ", e);
			ErrorMessage error = new ErrorMessage();
			error.setErrorMessage(sqlE);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String messages = sw.toString();
			List<String> trace = Stream.of(messages.split("\n\tat|\n"))//
					.map(String::trim)//
					.collect(Collectors.toList());
			error.setTrace(trace);
			result.setReturnErrorMessage(error);
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
	private PreparedStatement fillPreparedViewString(Table inputTable, CallableStatement preparedStatement) {
		int parameterOffset = 1;

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
						preparedStatement.setString(i + parameterOffset, inBetweenValues.get(0));
						parameterOffset++;
						preparedStatement.setString(i + parameterOffset, inBetweenValues.get(inBetweenValues.size() - 1));
					} else {
						if (!stringValue.trim().isEmpty()) {
							preparedStatement.setString(i + parameterOffset, stringValue);
						} else {
							parameterOffset--;
						}
					}
				} else {
					parameterOffset--;
				}
			} catch (Exception e) {
				throw new RuntimeException("Could not parse input parameter with index:" + i, e);
			}
		}
		return preparedStatement;
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

	String prepareViewString(Table params, boolean autoLike, int maxRows) throws IllegalArgumentException {
		return prepareViewString(params, autoLike, maxRows, false);
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
	String prepareViewString(Table params, boolean autoLike, int maxRows, boolean count) throws IllegalArgumentException {
		final StringBuffer sb = new StringBuffer();
		if (params.getName() == null || params.getName().trim().length() == 0) {
			throw new IllegalArgumentException("Cannot prepare statement with NULL name");
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
		if (params.getColumns().size() > 0 && params.getRows().size() > 0) {
			final String where = prepareWhereClause(params, autoLike);
			sb.append(where);
		}

		return sb.toString();
	}

	/*
	 * Pagination nach der Seek-Methode; bessere Performance als Offset bei großen Datensätzen
	 */
	public String pagingWithSeek(Table params, boolean autoLike, int maxRows, boolean count, int page) {
		final StringBuffer sb = new StringBuffer();
		if (params.getName() == null || params.getName().trim().length() == 0) {
			throw new IllegalArgumentException("Cannot prepare statement with NULL name");
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
		if (params.getColumns().size() > 0 && params.getRows().size() > 0) {
			final String where = prepareWhereClause(params, autoLike);
			sb.append(where);
		}
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

							List<String> inBetweenValues = new ArrayList<>();
							inBetweenValues = Stream.of(strValue.split(","))//
									.collect(Collectors.toList());

							String comma = "";
							// für jeden der Komma-getrennten Werte muss ein Fragezeichen da sein
							for (int i = 0; i < inBetweenValues.size(); i++) {
								clause.append(comma).append(" ? ");
								comma = ",";
							}

							clause.append(")");
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
					if (ruleValue.contains("not null")) {
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
					where.append("\r\nwhere ");
				} else {
					where.append(and ? "\r\n  and " : "\r\n   or ");
				}
				where.append('(').append(clause.toString()).append(')');
			}
		}

		return where.toString();
	}

}
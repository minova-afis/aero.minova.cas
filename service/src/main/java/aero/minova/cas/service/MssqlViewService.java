package aero.minova.cas.service;

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
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.sql.SqlUtils;
import aero.minova.cas.sql.SystemDatabase;
import lombok.val;

@Service
public class MssqlViewService implements ViewServiceInterface {

	@Autowired
	SystemDatabase systemDatabase;

	@Autowired
	CustomLogger customLogger;

	@Autowired
	private SecurityService securityService;

	public MssqlViewService(SystemDatabase systemDatabase, CustomLogger customLogger, SecurityService securityService) {
		this.systemDatabase = systemDatabase;
		this.customLogger = customLogger;
		this.securityService = securityService;
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
			final val viewQuery = prepareViewString(inputTable, false, -1, false, userGroups);
			val preparedStatement = connection.prepareCall(viewQuery);
			val preparedViewStatement = SqlUtils.fillPreparedViewString(inputTable, preparedStatement, viewQuery, sb, customLogger.getErrorLogger());
			customLogger.logPrivilege("Executing statement: " + sb.toString());
			ResultSet resultSet = preparedViewStatement.executeQuery();
			result = SqlUtils.convertSqlResultToTable(inputTable, resultSet, customLogger.getUserLogger(), this);
		} catch (Exception e) {
			customLogger.logError("Statement could not be executed: " + sb.toString(), e);
			throw new RuntimeException(e);
		} finally {
			systemDatabase.freeUpConnection(connection);
		}
		return result;
	}

	public String prepareViewString(Table params, boolean autoLike, int maxRows, List<Row> authorities) throws IllegalArgumentException {
		return prepareViewString(params, autoLike, maxRows, false, authorities);
	}

	/**
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @param maxRows
	 *            maximale Anzahl Ergebnisse (Zeilen), die die Abfrage liefern soll, 0 für unbegrenzt
	 * @param count
	 *            Gibt an ob nur die Anzahl der Ergebniss (Zeilen), gezählt werden sollen.
	 * @param authorities
	 *            Eine Liste an autorisierten UserGruppen. Wird für die RowLevelSecurity benötigt.
	 * @return Präparierter View-String, der ausgeführt werden kann
	 * @throws IllegalArgumentException
	 * @author wild
	 */
	public String prepareViewString(Table params, boolean autoLike, int maxRows, boolean count, List<Row> authorities) throws IllegalArgumentException {
		final StringBuilder sb = new StringBuilder();
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
				if (Column.AND_FIELD_NAME.equalsIgnoreCase(col.getName()) || r.getValues().get(colI) == null) {
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

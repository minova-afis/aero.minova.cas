package aero.minova.cas.service;

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

	public Table unsecurelyGetIndexView(Table inputTable) {
		StringBuilder sb = new StringBuilder();
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		Table result = new Table();
		val connection = systemDatabase.getConnection();
		try {
			val viewQuery = prepareViewString(inputTable, false, IF_LESS_THAN_ZERO_THEN_MAX_ROWS, false, userGroups);
			val preparedStatement = connection.prepareCall(viewQuery);
			try (PreparedStatement preparedViewStatement = SqlUtils.fillPreparedViewString(inputTable, preparedStatement, viewQuery, sb,
					customLogger.errorLogger)) {
				customLogger.logPrivilege("Executing SQL-statement for view:  " + sb);
				try (ResultSet resultSet = preparedViewStatement.executeQuery()) {
					result = SqlUtils.convertSqlResultToTable(inputTable, resultSet, customLogger.userLogger, this);
				}
			}
		} catch (Exception e) {
			customLogger.logError("Statement could not be executed: " + sb, e);
			throw new RuntimeException(e);
		} finally {
			systemDatabase.closeConnection(connection);
		}
		return result;
	}

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
					.toList();
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

	public String prepareWhereClause(Table params, boolean autoLike) {
		final StringBuilder where = new StringBuilder();
		final boolean hasAndClause;
		// TODO Check size
		val andFields = params.getColumns().stream()//
				.filter(c -> Objects.equals(c.getName(), Column.AND_FIELD_NAME))//
				.toList();
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
			if (hasAndClause && r.getValues().get(andFieldIndex) != null && r.getValues().get(andFieldIndex).getBooleanValue() != null) {
				and = r.getValues().get(andFieldIndex).getBooleanValue();
			} else {
				and = false;
			}

			// Eine where Zeile aufbauen
			final StringBuilder clause = new StringBuilder();
			COLS: for (int colI = 0; colI < r.getValues().size(); ++colI) {
				val def = r.getValues().get(colI);
				val col = params.getColumns().get(colI);
				if (Column.AND_FIELD_NAME.equalsIgnoreCase(col.getName()) || r.getValues().get(colI) == null) {
					continue COLS;
				}

				final Object valObj = r.getValues().get(colI).getValue();
				String strValue = valObj.toString().trim();
				String ruleValue = r.getValues().get(colI).getRule();
				if (Objects.equals(ruleValue, "~")) {
					ruleValue = "like";
				} else if (Objects.equals(ruleValue, "!~")) {
					ruleValue = "not like";
				}

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
				where.append('(').append(clause).append(')');
			}
		}
		return where.toString();
	}

	/*
	 * Pagination nach der Seek-Methode; bessere Performance als Offset bei großen Datensätzen. Wird NICHT für den "normalen" Index-Aufruf verwendet, da immer
	 * davon ausgegangen wird, dass ein KeyLong in der View/Table vorhanden ist.
	 */
	public String pagingWithSeek(Table params, boolean autoLike, int maxRows, boolean count, int page, List<Row> authorities) {
		final StringBuilder sb = new StringBuilder();
		if (params.getName() == null || params.getName().trim().length() == 0) {
			throw new IllegalArgumentException("msg.ViewNullName");
		}
		sb.append("select ");
		val outputFormat = params.getColumns().stream()//
				.filter(c -> !Objects.equals(c.getName(), Column.AND_FIELD_NAME))//
				.toList();
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
}

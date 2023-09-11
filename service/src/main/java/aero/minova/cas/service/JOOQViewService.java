package aero.minova.cas.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.jooq.Query;
import org.jooq.SelectField;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.sql.SqlUtils;
import aero.minova.cas.sql.SystemDatabase;
import lombok.RequiredArgsConstructor;
import lombok.val;

@RequiredArgsConstructor
@Service
public class JOOQViewService implements ViewServiceInterface {
	private final SystemDatabase systemDatabase;
	private final CustomLogger customLogger;
	private final SecurityService securityService;

	public String prepareViewString(Table params, boolean autoLike, int maxRows, boolean isCounting, List<Row> authorities) throws IllegalArgumentException {

		if (params.getName() == null || params.getName().trim().length() == 0) {
			throw new IllegalArgumentException("msg.ViewNullName");
		}

		// Hier wird auch schon die RowLevelSecurity mit rein gepackt.
		Condition condition = prepareWhereClauseGetCondition(params, autoLike);

		// Hier passiert die RowLevelSecurity.
		Condition userCondition = rowLevelSecurity(authorities);
		if (userCondition != null) {
			condition = condition.and(userCondition);
		}

		List<SelectField<Object>> fields = new ArrayList<>();

		for (Column column : params.getColumns()) {
			if (!column.getName().equals(Column.AND_FIELD_NAME)) {
				fields.add(DSL.field(column.getName()));
			}
		}

		Query query;

		// Hier wird nur unterschieden, ob die Einträge gezählt werden sollen oder nicht.
		if (isCounting) {
			query = DSL.selectCount().from(params.getName()).where(condition);
		} else if (maxRows > 0) {
			query = DSL.select(fields).from(params.getName()).where(condition).limit(maxRows);
		} else {
			query = DSL.select(fields).from(params.getName()).where(condition);
		}

		return query.getSQL();
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
		final val connection = systemDatabase.getConnection();
		try {
			final String viewQuery = prepareViewString(inputTable, false, IF_LESS_THAN_ZERO_THEN_MAX_ROWS, false, userGroups);
			val preparedStatement = connection.prepareCall(viewQuery);
			try (PreparedStatement preparedViewStatement = SqlUtils.fillPreparedViewString(inputTable, preparedStatement, viewQuery, sb,
					customLogger.errorLogger)) {
				customLogger.logPrivilege("Executing SQL-statement for view: " + sb);
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

	public Condition rowLevelSecurity(List<Row> requestingAuthorities) {
		if (requestingAuthorities.isEmpty()) {
			return null;
		}

		List<String> requestingRoles = securityService.extractUserTokens(requestingAuthorities);
		// Falls die Liste leer ist, darf der User alle Spalten sehen.
		if (requestingRoles.isEmpty()) {
			return null;
		}

		Condition userCondition = DSL.noCondition();

		// Wenn SecurityToken null, dann darf jeder User die Spalte sehen.
		userCondition = userCondition.and(DSL.field("SecurityToken").isNull());

		// Nach allen relevanten SecurityTokens suchen.
		userCondition = userCondition.or(DSL.field("SecurityToken").in(requestingRoles));

		return userCondition;
	}

	@Override
	public String prepareWhereClause(Table params, boolean autoLike) {
		return prepareWhereClauseGetCondition(params, autoLike).toString();
	}

	private Condition prepareWhereClauseGetCondition(Table params, boolean autoLike) {
		List<Condition> whereConditions = new ArrayList<>();

		Condition returnCondition = DSL.noCondition();

		int columnAndField = params.findColumnPosition(Column.AND_FIELD_NAME);
		for (Row r : params.getRows()) {

			Condition condition = DSL.noCondition();

			boolean hasAndValue = false;
			if (columnAndField >= 0) {
				hasAndValue = r.getValues().get(columnAndField) != null && //
						r.getValues().get(columnAndField).getBooleanValue() != null && //
						r.getValues().get(columnAndField).getBooleanValue();
			}

			for (int i = 0; i < r.getValues().size(); i++) {

				Value value = r.getValues().get(i);

				if (value != null && !params.getColumns().get(i).getName().equals(Column.AND_FIELD_NAME)) {
					String rule = value.getRule();
					String valueString = value.getValue().toString();
					String columnName = params.getColumns().get(i).getName();

					// Falls rule null ist und der value auch null ist ... dann ist doch gar nichts zu machen.
					if (rule == null && (valueString == null || valueString.isBlank())) {
						continue;
					}

					// Is Null und is not Null muss zuerst geprüft werden, da es egal ist, ob etwas im Value steht.
					if (rule != null && rule.contains("!null")) {
						condition = condition.and(DSL.field(columnName).isNotNull());
						continue;
					} else if (rule != null && rule.contains("null")) {
						condition = condition.and(DSL.field(columnName).isNull());
						continue;
					}

					if (rule == null || rule.isBlank()) {
						if ((autoLike || valueString.contains("%") || valueString.contains("_"))
								&& params.getColumns().get(i).getType().equals(DataType.STRING)) {
							rule = "~";
						} else {
							rule = "=";
						}
					}

					// Bei autoLike ggf. ein % anhängen
					if (autoLike && //
							params.getColumns().get(i).getType().equals(DataType.STRING) && //
							(!value.getStringValue().contains("%"))) {
						value = new Value(value.getStringValue() + "%", rule);
						valueString = value.getValue().toString();
					}

					if (rule.equals("~") || rule.equals("like")) {
						condition = condition.and(DSL.field(columnName).likeIgnoreCase(valueString));
					} else if (rule.equals("!~") || rule.equals("not like")) {
						condition = condition.and(DSL.field(columnName).notLikeIgnoreCase(valueString));
					} else if (rule.equals("=")) {
						if (value.getType().equals(DataType.STRING)) {
							condition = condition.and(DSL.field(columnName).equalIgnoreCase(valueString));
						} else {
							condition = condition.and(DSL.field(columnName).eq(valueString));
						}
					} else if (rule.equals(">")) {
						condition = condition.and(DSL.field(columnName).gt(valueString));
					} else if (rule.equals(">=")) {
						condition = condition.and(DSL.field(columnName).ge(valueString));
					} else if (rule.equals("<")) {
						condition = condition.and(DSL.field(columnName).lt(valueString));
					} else if (rule.equals("<=")) {
						condition = condition.and(DSL.field(columnName).le(valueString));
					} else if (rule.equals("<>")) {
						if (value.getType().equals(DataType.STRING)) {
							condition = condition.and(DSL.field(columnName).notEqualIgnoreCase(valueString));
						} else {
							condition = condition.and(DSL.field(columnName).ne(valueString));
						}
					} else if (rule.equals("between()")) {
						List<String> betweenValues = Stream.of(valueString.split(",")).toList();
						condition = condition.and(DSL.field(columnName).between(betweenValues.get(0), betweenValues.get(1)));
					} else if (rule.equals("in()")) {
						List<String> inValues = Stream.of(valueString.split(",")).toList();
						condition = condition.and(DSL.field(columnName).in(inValues));
					} else {
						throw new IllegalArgumentException("Invalid rule " + rule + " for value " + valueString + "!");
					}

				}

			}
			if (hasAndValue) {
				returnCondition = returnCondition.and(condition);
			} else {
				returnCondition = returnCondition.or(condition);
			}
			whereConditions.add(condition);
		}

		return returnCondition;
	}

	@Override
	public String pagingWithSeek(Table params, boolean autoLike, int maxRows, boolean count, int page, List<Row> authorities) {
		return "Not implemented yet";
	}
}

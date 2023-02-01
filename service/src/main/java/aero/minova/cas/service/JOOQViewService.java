package aero.minova.cas.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.jooq.Query;
import org.jooq.SelectField;
import org.jooq.impl.DSL;
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
public class JOOQViewService implements ViewServiceInterface {

	@Autowired
	SystemDatabase systemDatabase;

	@Autowired
	CustomLogger customLogger;

	@Autowired
	private SecurityService securityService;

	public JOOQViewService(SystemDatabase systemDatabase, CustomLogger customLogger, SecurityService securityService) {
		this.systemDatabase = systemDatabase;
		this.customLogger = customLogger;
		this.securityService = securityService;
	}

	public String prepareViewString(Table params, boolean autoLike, int maxRows, boolean isCounting, List<Row> authorities) throws IllegalArgumentException {

		if (params.getName() == null || params.getName().trim().length() == 0) {
			throw new IllegalArgumentException("msg.ViewNullName");
		}

		// Hier wird auch schon die RowLevelSecurity mit rein gepackt.
		String whereClause = prepareWhereClause(params, autoLike);

		Condition condition = DSL.condition(whereClause);

		// Hier passiert die RowLevelSecurity.
		if (!authorities.isEmpty()) {
			Condition userCondition = rowLevelSecurity(authorities);
			if (userCondition != null) {
				condition = condition.and(userCondition);
			}
		}

		List<SelectField> fields = new ArrayList();

		for (Column column : params.getColumns()) {
			if (!column.getName().equals(Column.AND_FIELD_NAME)) {
				fields.add(DSL.field(column.getName()));
			}
		}

		Query query;

		// Hier wird nur unterschieden, ob die Einträge gezählt werden sollen oder nicht.
		// Wenn eine condition leer ist und die toString()-Methode auf diese angewandt wird, kommt ein "(true)" dabei raus. Das wollen wir nicht in unserer
		// Abfrage haben.
		if (isCounting) {
			if (condition != null && !condition.toString().equals("(true)")) {
				query = DSL.selectCount().from(params.getName()).where(condition);
			} else {
				query = DSL.selectCount().from(params.getName());
			}
		} else if (maxRows > 0) {
			if (condition != null && !condition.toString().equals("(true)")) {
				query = DSL.select(fields).from(params.getName()).where(condition).limit(maxRows);
			} else {
				query = DSL.select(fields).from(params.getName()).limit(maxRows);
			}
		} else {
			if (condition != null && !condition.toString().equals("(true)")) {
				query = DSL.select(fields).from(params.getName()).where(condition);
			} else {
				query = DSL.select(fields).from(params.getName());
			}
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
			final String viewQuery = prepareViewString(inputTable, false, -1, false, userGroups);
			val preparedStatement = connection.prepareCall(viewQuery);
			val preparedViewStatement = SqlUtils.fillPreparedViewString(inputTable, preparedStatement, viewQuery, sb, customLogger.getErrorLogger());
			customLogger.logPrivilege("Executing SQL-statement for view: " + sb.toString());
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

	public Condition rowLevelSecurity(List<Row> requestingAuthorities) {
		List<String> requestingRoles = new ArrayList<>();
		if (!requestingAuthorities.isEmpty()) {
			requestingRoles = securityService.extractUserTokens(requestingAuthorities);
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

	@Override
	public String prepareWhereClause(Table params, boolean autoLike) {
		Condition condition = DSL.noCondition();

		for (Row r : params.getRows()) {
			for (int i = 0; i < r.getValues().size(); i++) {
				Value value = r.getValues().get(i);

				if (value != null && !params.getColumns().get(i).getName().equals(Column.AND_FIELD_NAME)) {
					String rule = (r.getValues().get(i).getRule() != null ? r.getValues().get(i).getRule() : null);

					// Is Null und is not Null muss zuerst geprüft werden, da es egal ist, ob etwas im Value steht.
					if (rule != null && rule.contains("!null")) {
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).isNotNull());
						continue;
					} else if (rule != null && rule.contains("null")) {
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).isNull());
						continue;
					}

					if (autoLike && params.getColumns().get(i).getType().equals(DataType.STRING) && !value.getValue().toString().isBlank()
							&& (!value.getStringValue().contains("%"))) {
						value = new Value(value.getStringValue() + "%", rule);
					}

					if (rule == null && !value.getValue().toString().contains("%")) {
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).eq(r.getValues().get(i).getValue().toString()));
					} else if (value.getValue().toString().contains("%") || rule.equals("~")) {
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
						List<String> betweenValues = Stream.of(value.getValue().toString().split(",")).collect(Collectors.toList());
						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).between(betweenValues.get(0), betweenValues.get(1)));
					} else if (rule.equals("in()")) {
						List<String> inValues = Stream.of(value.getValue().toString().split(",")).collect(Collectors.toList());

						condition = condition.and(DSL.field(params.getColumns().get(i).getName()).in(inValues));
					} else {
						throw new IllegalArgumentException("Invalid rule " + rule + " for value" + value.getValue().toString() + " !");
					}
				}
			}
		}
		return condition.toString();
	}
}

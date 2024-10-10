package aero.minova.cas.service;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.TableException;
import aero.minova.cas.api.domain.TableMetaData;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.sql.SqlUtils;
import aero.minova.cas.sql.SystemDatabase;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.val;

@Service
public class ViewService {

	ViewServiceInterface viewService;

	@Autowired
	SystemDatabase systemDatabase;

	@Autowired
	CustomLogger customLogger;

	@Autowired
	private SecurityService securityService;

	@PersistenceContext
	private EntityManager entityManager;

	@PostConstruct
	private void init() {
		if (systemDatabase.isSQLDatabase()) {
			viewService = new MssqlViewService(systemDatabase, customLogger);
		} else {
			viewService = new JOOQViewService(systemDatabase, customLogger);
		}
		securityService.setViewService(this);
	}

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
			String viewQuery = viewService.prepareViewString(inputTable, false, 0, authoritiesForThisTable);
			val preparedStatement = connection.prepareCall(viewQuery);
			try (PreparedStatement preparedViewStatement = fillPreparedViewString(inputTable, preparedStatement, viewQuery, sb)) {
				customLogger.logSql("Executing statements: " + sb);
				try (ResultSet resultSet = preparedViewStatement.executeQuery()) {

					result = SqlUtils.convertSqlResultToTable(inputTable, resultSet, customLogger.userLogger, this);

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
				}
			} finally {
				preparedStatement.close();
			}
		} catch (Throwable e) {
			customLogger.logError("Statement could not be executed: " + sb, e);
			throw new TableException(e);
		} finally {
			systemDatabase.closeConnection(connection);
		}
		return result;
	}

	/**
	 * Das Prepared Statement wird mit den dafür vorgesehenen Parametern befüllt. Diese werden aus der übergebenen inputTable gezogen.
	 *
	 * @param inputTable
	 *            die Table, welche vom getIndexView aufgerufen wurde
	 * @param preparedStatement
	 *            das Prepared Statement, welches nur noch befüllt werden muss
	 * @param query
	 *            Das bereits fertig aufgebaute Sql Statement, welches statt der Werte '?' enthält. Diese werden hier 'ersetzt'.
	 * @param sb
	 *            Ein StringBuilder zum Loggen der inputParameter.
	 */
	public PreparedStatement fillPreparedViewString(Table inputTable, CallableStatement preparedStatement, String query, StringBuilder sb) {
		return SqlUtils.fillPreparedViewString(inputTable, preparedStatement, query, sb, customLogger.errorLogger);

	}

	@Deprecated
	public Table convertSqlResultToTable(Table inputTable, ResultSet sqlSet) {
		return SqlUtils.convertSqlResultToTable(inputTable, sqlSet, customLogger.userLogger, this);
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
		return viewService.prepareWhereClause(params, autoLike);
	}

	String prepareViewString(Table params, boolean autoLike, int maxRows, List<Row> authorities) throws IllegalArgumentException {
		return viewService.prepareViewString(params, autoLike, maxRows, false, authorities);
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
	public String prepareViewString(Table params, boolean autoLike, int maxRows, boolean count, List<Row> authorities) {
		return viewService.prepareViewString(params, autoLike, maxRows, count, authorities);
	}

	/**
	 * Wie {@link #getIndexView(Table)}, nur ohne die erste Sicherheits-Abfrage und ohne die maximale Anzahl der ausgegebenen Zeilen zu beschränken. Ist nur für
	 * die Sicherheitsabfragen gedacht, um nicht zu viele unnötige SQL-Abfrage zu machen.
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
		final String viewQuery = prepareViewString(inputTable, false, ViewServiceInterface.IF_LESS_THAN_ZERO_THEN_MAX_ROWS, false, userGroups);
		try (final var preparedStatement = connection.prepareCall(viewQuery)) {
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

	/**
	 * Ist nur im MssqlViewService implementiert.
	 * 
	 * @param params
	 *            Suchzeilen (z.B. Suchparameter), wobei auch ein Spezialfeld mit dem Namen 'AND' genutzt werden kann, um die Kriterien zu verknüpfen
	 * @param autoLike
	 *            wenn true, dann werden alle String-Parameter, die noch kein % haben, mit einem '%' am Ende versehen
	 * @param maxRows
	 *            maximale Anzahl Ergebnisse (Zeilen), die die Abfrage liefern soll, 0 für unbegrenzt
	 * @param count
	 *            Gibt an ob nur die Anzahl der Ergebniss (Zeilen), gezählt werden sollen.
	 * @param page
	 *            Die Seite, welche man zurückerhalten möchte.
	 * @param authorities
	 * @return
	 */
	public String pagingWithSeek(Table params, boolean autoLike, int maxRows, boolean count, int page, List<Row> authorities) {
		return viewService.pagingWithSeek(params, autoLike, maxRows, count, page, authorities);
	}
}

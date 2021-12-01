package aero.minova.core.application.system.service;

import static java.util.Arrays.asList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import aero.minova.core.application.system.CustomLogger;
import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.ProcedureException;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;
import aero.minova.core.application.system.sql.SystemDatabase;
import lombok.val;

@Service
public class SecurityService {

	@Autowired
	SystemDatabase systemDatabase;

	@Autowired
	SqlViewController svc;

	@Autowired
	public CustomLogger customLogger;

	/**
	 * Prüft, ob die minimal notwendigen Datenbank-Objekte für die Privileg-Prüfung in der Datenbank aufgesetzt wurden. Dazu prüft man, ob die
	 * `xvcasUserPrivileges` vorhanden ist.
	 *
	 * @return Dies ist wahr, wenn die Privilegien eines Nutzers anhand der Datenbank geprüft werden können.
	 * @throws Exception
	 *             Fehler bei der Ermittelung
	 */
	public boolean arePrivilegeStoresSetup() throws Exception {
		return isTablePresent("xvcasUserPrivileges");
	}

	private boolean isTablePresent(String tableName) throws Exception {
		try (final Connection connection = systemDatabase.getConnection()) {
			return connection.getMetaData()//
					.getTables(null, null, tableName, null)//
					.next();
		}
	}

	/**
	 * Überprüft, ob es in der vCASUserPrivileges mindestens einen Eintrag für die User Group des momentan eingeloggten Users gibt. Die Abfrage sieht
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
		return getTableForSecurityCheck(userPrivileges).getRows();
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
		Table result = new Table();
		final val connection = systemDatabase.getConnection();
		try {
			final val viewQuery = svc.prepareViewString(inputTable, false, -1, false, userGroups);
			val preparedStatement = connection.prepareCall(viewQuery);
			val preparedViewStatement = svc.fillPreparedViewString(inputTable, preparedStatement, viewQuery, sb);
			customLogger.logPrivilege("Executing statement: " + sb.toString());
			ResultSet resultSet = preparedViewStatement.executeQuery();
			result = svc.convertSqlResultToTable(inputTable, resultSet);
		} catch (Exception e) {
			customLogger.logError("Statement could not be executed: " + sb.toString(), e);
			throw new RuntimeException(e);
		} finally {
			systemDatabase.freeUpConnection(connection);
		}
		return result;
	}

	/**
	 * Entfernt alle Spalten der Eingabe-Tabelle, auf die der Nutzer keinen Zugriff hat.
	 * <p>
	 * TODO Idee: Mann sollte eine neue Tabelle erstellen, statt die eingabe abzuändern, da die Methoden-Signature impliziert, dass die InputTable nicht
	 * geändert wird. Die Sql-Abfrage hat folgendes Format: select TableName, ColumnName, SecurityToken from xtcasColumnSecurity where (TableName =
	 * inputTableName and SecurityToken = UserSecurityToken1) or (TableName = inputTableName and SecurityToken = UserSecurityToken2) or ... Die Rows der
	 * zurückgelieferten Table haben folgendes Format: Row r = [Tabellenname,ColumnName,SecurityToken], Beispiel: Row r = ["tTestTabelle","Spalte1","User1"] Row
	 * r = ["tTestTabelle","Spalte2","User1"]
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

		List<Row> columnRestrictionsForThisUserAndThisTable = new ArrayList<>();
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
	 * @param isFirstWhereClause
	 *            Abhängig davon, ob bereits eine where-Klausel besteht oder nicht, muss 'where' oder 'and' vorne angefügt werden
	 * @param requestingAtuhorities
	 *            Die Rollen des Nutzers, welche ein Recht auf einen Zugriff haben.
	 * @return einen String, der entweder an das Ende der vorhandenen Where-Klausel angefügt wird oder die Where-Klausel selbst ist
	 */
	public String rowLevelSecurity(boolean isFirstWhereClause, List<Row> requestingAtuhorities) {
		List<String> requestingRoles = new ArrayList<>();
		if (!requestingAtuhorities.isEmpty()) {
			requestingRoles = extractUserTokens(requestingAtuhorities);
			// falls die Liste leer ist, darf der User alle Spalten sehen
			if (requestingRoles.isEmpty()) {
				return "";
			}
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

	/**
	 * @param requestingAuthorities
	 *            eine Liste an Rows im Format: eine Row = ("ProzedurName","UserSecurityToken","RowLevelSecurity-Bit").
	 * @return Eine Liste an Strings, welche alle relevanten UserSecurityTokens beinhaltet oder eine leere Liste, falls ein SecurityToken die Berechtigung hat
	 *         alle Rows zu sehen.
	 * @author weber
	 */
	public List<String> extractUserTokens(List<Row> requestingAuthorities) {
		List<String> requestingRoles = new ArrayList<>();

		for (Row authority : requestingAuthorities) {
			/*
			 * Falls auch nur einmal false in der RowLevelSecurity-Spalte vorkommt, darf der User die komplette Tabelle sehen. Ist dies der Fall, können wir
			 * ruhig eine leere Liste zurückgeben, da deren Inhalt die UserSecurityTokens wären, nach welchen gefiltert werden würde.
			 */
			if (!authority.getValues().get(2).getBooleanValue()) {
				return new ArrayList<>();
			}
			// Hier sind die Rollen/UserSecurityToken, welche authorisiert sind, auf die Tabelle zuzugreifen.
			String value = authority.getValues().get(1).getStringValue().trim().toLowerCase();
			if (!value.equals("") && !requestingRoles.contains(value)) {
				requestingRoles.add(authority.getValues().get(1).getStringValue().toLowerCase());
			}
		}
		return requestingRoles;
	}

	/**
	 * Findet die SecurityToken-Spalte der übergebenen Table.
	 * 
	 * @param inputTable
	 *            Die Table, in welcher nach der SecurityToken-Spalte gesucht werden soll.
	 * @return Der int-Wert der Spalte, in welcher der SecurityToken ist.
	 * @throws ProcedureException
	 *             Falls keine SecurityToken-Spalte gefunden werden kann.
	 */
	public int findSecurityTokenColumn(Table inputTable) throws ProcedureException {
		int securityTokenInColumn = -1;
		// Herausfinden an welcher Stelle die Spalte mit den SecurityTokens ist
		for (int i = 0; i < inputTable.getColumns().size(); i++) {
			if (inputTable.getColumns().get(i).getName().equals("SecurityToken")) {
				securityTokenInColumn = i;
			}
		}
		if (securityTokenInColumn < 0) {
			throw new ProcedureException("msg.MissingSecurityTokenColumn");
		}
		return securityTokenInColumn;
	}

	/**
	 * Falls die Row-Level-Security für die Prozedur eingeschalten ist (Einträge in der Liste vorhanden), sollten die Rows nach dem Ausführen der Prozedur
	 * gefiltert werden. Überprüft, ob der SecurityToken der rowToBeChecked mit mind. 1 SecurityToken des Users übereinstimmt.
	 * 
	 * @param userSecurityTokens
	 *            Eine Liste an Strings von UserSecurityTokens.
	 * @param rowToBeChecked
	 *            Die Row aus dem SqlProcedureResult, welche überprüft werden muss.
	 * @param securityTokenInColumn
	 *            Die Spalte als int, in welcher der SecurityToken liegt.
	 * @return True, falls einer der SecurityTokens übereinstimmt, andernfalls False.
	 */
	public boolean isRowAccessValid(List<String> userSecurityTokens, Row rowToBeChecked, int securityTokenInColumn) {
		if (!userSecurityTokens.isEmpty()) {
			String securityTokenValue = rowToBeChecked.getValues().get(securityTokenInColumn).getStringValue();
			if (securityTokenValue == null || userSecurityTokens.contains(securityTokenValue.toLowerCase())) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	/**
	 * Updatet die Rollen, welche momentan im SecurityContext für den eingeloggten User hinterlegt sind, anhand folgender Abfrage: select
	 * KeyText,UserSecurityToken,Memberships from xtcasUser where KeyText = username
	 * 
	 * @param username
	 *            Der Username dessen Rollen geladen werden sollen.
	 * @param authorities
	 *            Die Liste an GrantedAuthorities, die der User bereits besitzt.
	 * @return Die Liste an bereits vorhandenen GrantedAuthorities vereint mit den neuen Authorities.
	 */
	public List<GrantedAuthority> loadPrivileges(String username, List<GrantedAuthority> authorities) {
		Table tUser = new Table();
		tUser.setName("xtcasUser");
		List<Column> columns = new ArrayList<>();
		columns.add(new Column("KeyText", DataType.STRING));
		columns.add(new Column("UserSecurityToken", DataType.STRING));
		columns.add(new Column("Memberships", DataType.STRING));
		tUser.setColumns(columns);
		Row userEntry = new Row();
		userEntry.setValues(Arrays.asList(new aero.minova.core.application.system.domain.Value(username, null),
				new aero.minova.core.application.system.domain.Value("", null), new aero.minova.core.application.system.domain.Value("", null)));
		tUser.addRow(userEntry);

		// dabei sollte nur eine ROW rauskommen, da jeder User eindeutig sein müsste
		Table membershipsFromUser = getTableForSecurityCheck(tUser);
		List<String> userSecurityTokens = new ArrayList<>();

		if (membershipsFromUser.getRows().size() > 0) {
			String result = membershipsFromUser.getRows().get(0).getValues().get(2).getStringValue();

			// alle SecurityTokens werden in der Datenbank mit Leerzeile und Raute voneinander getrennt
			userSecurityTokens = Stream.of(result.split("#"))//
					.map(String::trim)//
					.collect(Collectors.toList());

			// überprüfen, ob der einzigartige userSecurityToken bereits in der Liste der Memberships vorhanden war, wenn nicht, dann hinzufügen
			String uniqueUserToken = membershipsFromUser.getRows().get(0).getValues().get(1).getStringValue().replace("#", "").trim();
			if (!userSecurityTokens.contains(uniqueUserToken))
				userSecurityTokens.add(uniqueUserToken);
		} else {
			// falls der User nicht in der Datenbank gefunden wurde, wird sein Benutzername als einzigartiger userSecurityToken verwendet
			userSecurityTokens.add(username);
		}

		// füge die authorities hinzu, welche aus dem Active Directory kommen
		for (GrantedAuthority ga : authorities) {
			userSecurityTokens.add(ga.getAuthority());
		}

		// die Berechtigungen der Gruppen noch herausfinden
		Table groups = new Table();
		groups.setName("xtcasUserGroup");
		List<Column> groupcolumns = new ArrayList<>();
		groupcolumns.add(new Column("KeyText", DataType.STRING));
		groupcolumns.add(new Column("SecurityToken", DataType.STRING));
		groups.setColumns(groupcolumns);
		for (String s : userSecurityTokens) {
			if (!s.trim().equals("")) {
				Row tokens = new Row();
				tokens.setValues(Arrays.asList(new aero.minova.core.application.system.domain.Value(s.trim(), null),
						new aero.minova.core.application.system.domain.Value("", "!null")));
				groups.addRow(tokens);
			}
		}
		if (groups.getRows().size() > 0) {
			List<Row> groupTokens = getTableForSecurityCheck(groups).getRows();
			List<String> groupSecurityTokens = new ArrayList<>();
			for (Row r : groupTokens) {
				String memberships = r.getValues().get(1).getStringValue();
				// alle SecurityToken einer Gruppe der Liste hinzufügen
				val membershipsAsList = Stream.of(memberships.split("#"))//
						.map(String::trim)//
						.collect(Collectors.toList());
				groupSecurityTokens.addAll(membershipsAsList);
			}

			// verschiedene Rollen/Gruppen können dieselbe Berechtigung haben, deshalb rausfiltern
			for (String string : groupSecurityTokens) {
				if (!userSecurityTokens.contains(string))
					userSecurityTokens.add(string);
			}
		}

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (String string : userSecurityTokens) {
			if (!string.equals(""))
				grantedAuthorities.add(new SimpleGrantedAuthority(string));
		}

		return grantedAuthorities;
	}
}

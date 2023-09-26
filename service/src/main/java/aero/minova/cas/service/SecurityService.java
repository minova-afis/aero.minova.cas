package aero.minova.cas.service;

import static java.util.Arrays.asList;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.ProcedureException;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.sql.SystemDatabase;
import lombok.Setter;
import lombok.val;

@Service
@Setter
public class SecurityService {

	ViewServiceInterface viewService;

	@Autowired
	SystemDatabase systemDatabase;

	@Autowired
	public CustomLogger customLogger;

	@org.springframework.beans.factory.annotation.Value("${login_dataSource:}")
	private String dataSource;

	/**
	 * Prüft, ob die minimal notwendigen Datenbank-Objekte für die Privileg-Prüfung in der Datenbank aufgesetzt wurden. Dazu prüft man, ob die
	 * `xvcasUserSecurity` vorhanden ist.
	 *
	 * @return <code>true</code>, wenn die Privilegien eines Nutzers anhand der Datenbank geprüft werden können.
	 * @throws Exception
	 *             Fehler bei der Ermittelung
	 */
	public boolean arePrivilegeStoresSetup() throws Exception {
		return isTablePresent("xvcasusersecurity");
	}

	public boolean isTablePresent(String tableName) throws Exception {
		try (final Connection connection = systemDatabase.getConnection()) {
			return connection.getMetaData()//
					.getTables(null, null, tableName, null)//
					.next();
		}
	}

	/**
	 * Überprüft, ob es in der vCASUserPrivileges mindestens einen Eintrag für die User Group des momentan eingeloggten Users gibt. Die Abfrage sieht
	 * folgendermaßen aus: select PrivilegeKeyText,KeyText,RowLevelSecurity from xvcasUserSecurity where (PrivilegeKeyText = privilegeName and SecurityToken =
	 * UserSecurityToken1) or (PrivilegeKeyText = privilegeName and SecurityToken = UserSecurityToken2) or ... Die erzeugten Rows haben folgendes Format: Row r
	 * = [Tabellenname,UserSecurityToken,RowLevelSecurity], Beispiel: Row r = ["tTestTabelle","User1",1], Row r2 = ["tTestTabelle","User2",0]
	 *
	 * @param privilegeName
	 *            Das Privilege, für das ein Recht eingefordert wird.
	 * @return Enthält alle Gruppen, die ein Recht auf das Privileg haben.
	 **/
	public List<Row> getPrivilegePermissions(String privilegeName) {
		loadAllPrivileges();
		@SuppressWarnings("unchecked")
		List<GrantedAuthority> allUserAuthorities = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		Table userPrivileges = new Table();
		userPrivileges.setName("xvcasUserSecurity");
		List<Column> columns = new ArrayList<>();
		columns.add(new Column("PrivilegeKeyText", DataType.STRING));
		columns.add(new Column("SecurityToken", DataType.STRING));
		columns.add(new Column("RowLevelSecurity", DataType.BOOLEAN));
		columns.add(Column.AND_FIELD);
		userPrivileges.setColumns(columns);

		for (GrantedAuthority ga : allUserAuthorities) {

			// Überprüfen, ob der SecurityToken an irgendeiner Position in der SecurityToken-Spalte auftaucht.
			Row tableNameAndUserToken = new Row();

			// Token ist in der Mitte des Strings.
			tableNameAndUserToken.setValues(
					asList(new Value(privilegeName, null), new Value("%#" + ga.getAuthority() + "#%", null), new Value("", null), new Value(false, null)));
			// Token ist am Ende des Strings
			tableNameAndUserToken
					.setValues(asList(new Value(privilegeName, null), new Value("%#" + ga.getAuthority(), null), new Value("", null), new Value(false, null)));
			userPrivileges.addRow(tableNameAndUserToken);
		}

		// Hier bekommen wir Rows zurück, die in irgendeiner Weise unsere Tokens enthalten.
		List<Row> queryResult = unsecurelyGetIndexView(userPrivileges).getRows();

		// Hier filtern wir noch alle SecurityToken raus, die nicht unserem Benutzer gehören.
		List<Row> returnRows = new ArrayList<>();
		for (Row r : queryResult) {
			List<String> resultAuthorities = Stream.of(r.getValues().get(1).getStringValue().split("#"))//
					.map(String::new)//
					.collect(Collectors.toList());

			for (String authority : resultAuthorities) {
				if (authority != null && !authority.isBlank() && allUserAuthorities.contains(new SimpleGrantedAuthority(authority))) {
					r.getValues().set(1, new Value(authority, null));
					Row newRow = new Row();
					newRow.setValues(asList(r.getValues().get(0), new Value(authority, null), r.getValues().get(2)));
					returnRows.add(newRow);
				}
			}

		}

		return returnRows;
	}

	/**
	 * Lädt für LDAP- und Datenbankbankbenutzer die Rollen der User Groups in den SecurityContext.
	 */
	public void loadAllPrivileges() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				List<String> userSecurityTokens = new ArrayList<>();

				// Je nachdem, ob per LDAP oder Database autorisiert wird, wird entweder auf der xtcasUser abgefragt oder auf der xtcasUsers.
				if (dataSource.equalsIgnoreCase("ldap")) {
					userSecurityTokens = loadLDAPUserTokens(authentication.getName());
				} else if (dataSource.equalsIgnoreCase("database")) {
					userSecurityTokens = loadDatabaseUserTokens(authentication.getName());
				}

				List<GrantedAuthority> oldAuthorities = (List<GrantedAuthority>) authentication.getAuthorities();

				// Hier werden die SecurityTokens der Gruppen ausgelesen.
				List<GrantedAuthority> grantedAuthorities = loadUserGroupPrivileges(authentication.getName(), userSecurityTokens, oldAuthorities);

				// GrantedAuthorities zu SimpleGrantedAuthorities ummappen, da Typecasting in diesem Fall nicht funktioniert.
				List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<>();
				for (GrantedAuthority ga : grantedAuthorities) {
					SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ga.getAuthority());
					updatedAuthorities.add(authority);
				}

				// Neue Authentication mit den alten Logindaten erstellen und in den Context setzen.
				Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(),
						updatedAuthorities);

				SecurityContextHolder.getContext().setAuthentication(newAuth);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}

	}

	/**
	 * Wie {@link #getIndexView(Table)}, nur ohne die erste Sicherheits-Abfrage, um die maximale Länge zu erhalten. Ist nur für die Sicherheitsabfragen gedacht,
	 * um nicht zu viele unnötige SQL-Abfragen zu machen.
	 *
	 * @param inputTable
	 *            Die Parameter, der SQL-Anfrage die ohne Sicherheitsprüfung durchgeführt werden soll.
	 * @return Das Ergebnis der Abfrage.
	 */
	public Table unsecurelyGetIndexView(Table inputTable) {
		return viewService.unsecurelyGetIndexView(inputTable);

	}

	/**
	 * Entfernt alle Spalten der Eingabe-Tabelle, auf die der Nutzer keinen Zugriff hat.
	 * <p>
	 * TODO Idee: Man sollte eine neue Tabelle erstellen, statt die Eingabe abzuändern, da die Methoden-Signature impliziert, dass die InputTable nicht geändert
	 * wird.<br>
	 * Die Sql-Abfrage hat folgendes Format:<br>
	 * <code>
	 * select TableName, ColumnName, SecurityToken from xtcasColumnSecurity where (TableName =
	 * inputTableName and SecurityToken = UserSecurityToken1) or (TableName = inputTableName and SecurityToken = UserSecurityToken2) or ...
	 * </code> <br>
	 * ToDo: kann man nicht schreiben: Select ... from where tableName = inputTableName and SecurityToken in ( token1, token2, ... ) <br>
	 * Die Rows der zurückgelieferten Table haben folgendes Format:<br>
	 * Row r = [Tabellenname,ColumnName,SecurityToken],<br>
	 * Beispiel: Row r = ["tTestTabelle","Spalte1","User1"]<br>
	 * Row r = ["tTestTabelle","Spalte2","User1"]
	 *
	 * @param inputTable
	 *            Enthält den Tabellen-Namen und die Spalten, welche von einem Nutzer angefragt werden.
	 * @param userGroups
	 *            Die Nutzer-Gruppen/Rollen, welche Zugriff auf die Tabelle haben wollen.
	 * @return Diese Tabelle enthält die Spalten, welche für die Index-View von diesem User verwendet werden dürfen.
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
			// Hier wird darauf abgefragt, welche UserGruppen für die angeforderte Tabelle autorisiert sind. Hierbei sollte die Schreibweise der Tabelle/View
			// egal sein.
			if (row.getValues().get(0).getStringValue().equalsIgnoreCase(inputTable.getName())) {
				Row bar = new Row();
				bar.setValues(asList(new Value(inputTable.getName(), null), new Value("", null), new Value(row.getValues().get(1).getStringValue(), null)));
				List<Row> checkRow = new ArrayList<>();
				checkRow.add(bar);
				columnSec.setRows(checkRow);
				List<Row> tokenSpecificAuthorities = unsecurelyGetIndexView(columnSec).getRows();
				// wenn es in der tColumnSecurity keinen Eintrag für diese Tabelle gibt, dann darf der User jede Spalte ansehen
				if (tokenSpecificAuthorities.isEmpty())
					return inputTable;
				columnRestrictionsForThisUserAndThisTable.addAll(tokenSpecificAuthorities);
			}
		}
		List<String> grantedColumns = new ArrayList<>();
		// die Spaltennamen, welche wir durch den Select erhalten haben, in eine List packen, dabei darauf achten,
		// dass verschiedene SecurityTokens dieselbe Erlaubnis haben können, deshalb Doppelte rausfiltern
		for (Row row : columnRestrictionsForThisUserAndThisTable) {
			String grantedColumnFromtColumnSecurity = row.getValues().get(1).getStringValue();
			if (!grantedColumns.contains(grantedColumnFromtColumnSecurity)) {
				grantedColumns.add(grantedColumnFromtColumnSecurity);
			}
		}

		// wenn SELECT *, dann ist wantedColumns leer
		List<Column> wantedColumns = new ArrayList<>(inputTable.getColumns());
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
	 * @param requestingAuthorities
	 *            Die Rollen des Nutzers, welche ein Recht auf einen Zugriff haben.
	 * @return einen String, der entweder an das Ende der vorhandenen Where-Klausel angefügt wird oder die Where-Klausel selbst ist
	 */
	public String rowLevelSecurity(boolean isFirstWhereClause, List<Row> requestingAuthorities) {
		List<String> requestingRoles = new ArrayList<>();
		if (!requestingAuthorities.isEmpty()) {
			requestingRoles = extractUserTokens(requestingAuthorities);
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

		if (!requestingRoles.isEmpty()) {
			rowSec.append("\r\nor ( SecurityToken IN (");
			for (String r : requestingRoles) {
				rowSec.append("'").append(r.replace('#', ' ').trim()).append("',");
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
			if (!Boolean.TRUE.equals(authority.getValues().get(2).getBooleanValue())) {
				return new ArrayList<>();
			}
			// Hier sind die Rollen/UserSecurityToken, welche autorisiert sind, auf die Tabelle zuzugreifen.
			String value = authority.getValues().get(1).getStringValue().trim().toLowerCase();
			if (!value.isEmpty() && !requestingRoles.contains(value)) {
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
	 * Falls die Row-Level-Security für die Prozedur eingeschaltet ist (Einträge in der Liste vorhanden), sollten die Rows nach dem Ausführen der Prozedur
	 * gefiltert werden. Überprüft, ob der SecurityToken der rowToBeChecked mit mind. 1 SecurityToken des Users übereinstimmt.
	 *
	 * @param userSecurityTokens
	 *            Eine Liste an Strings von UserSecurityTokens.
	 * @param rowToBeChecked
	 *            Die Row aus dem SqlProcedureResult, welche überprüft werden muss.
	 * @param securityTokenInColumn
	 *            Die Spalte als int, in welcher der SecurityToken liegt.
	 * @return True, falls einer der SecurityTokens übereinstimmt (oder die Liste <code>userSecurityTokens</code> leer ist), andernfalls False.
	 */
	public boolean isRowAccessValid(List<String> userSecurityTokens, Row rowToBeChecked, int securityTokenInColumn) {
		if (!userSecurityTokens.isEmpty()) {
			String securityTokenValue = rowToBeChecked.getValues().get(securityTokenInColumn).getStringValue();
			return securityTokenValue == null || userSecurityTokens.contains(securityTokenValue.toLowerCase());
		} else {
			return true;
		}
	}

	/**
	 * Updatet die Rollen, welche momentan im SecurityContext für den eingeloggten User hinterlegt sind, anhand folgender Abfrage:<br>
	 * <code>select
	 * KeyText,UserSecurityToken,Memberships from xtcasUser where KeyText = username
	 * </code>
	 * 
	 * @param username
	 *            Der Username dessen Rollen geladen werden sollen.
	 * @param userSecurityTokens
	 * @param authorities
	 *            Die Liste an GrantedAuthorities, die der User bereits besitzt.
	 * @return Die Liste an bereits vorhandenen GrantedAuthorities vereint mit den neuen Authorities.
	 */
	public List<GrantedAuthority> loadUserGroupPrivileges(String username, List<String> userSecurityTokens, List<GrantedAuthority> authorities) {
		// Füge der Liste der ausgelesenen Authorities aus der Datenbank die Authorities hinzu, welche bereits vorhanden waren.
		if (authorities != null) {
			for (GrantedAuthority ga : authorities) {
				if (!userSecurityTokens.contains(ga.getAuthority())) {
					userSecurityTokens.add(ga.getAuthority());
				}
			}
		}

		// Hier werden die Berechtigungen der Gruppen noch herausgesucht anhand der userSecurityToken-Liste.
		Table groups = new Table();
		groups.setName("xtcasUserGroup");
		List<Column> groupColumns = new ArrayList<>();
		groupColumns.add(new Column("KeyText", DataType.STRING));
		groupColumns.add(new Column("SecurityToken", DataType.STRING));
		groups.setColumns(groupColumns);
		for (String s : userSecurityTokens) {
			if (!s.trim().isEmpty()) {
				Row tokens = new Row();
				tokens.setValues(Arrays.asList(new Value(s.trim(), null), new Value("", "!null")));
				groups.addRow(tokens);
			}
		}
		if (!groups.getRows().isEmpty()) {
			List<Row> groupTokens = unsecurelyGetIndexView(groups).getRows();
			List<String> groupSecurityTokens = new ArrayList<>();
			for (Row r : groupTokens) {
				String memberships = r.getValues().get(1).getStringValue();
				// Die Memberships-Spalte in der xtcasUserGroup ist ein langer String. Hier wird der String beim Zeichen '#' getrennt und dann werden alle
				// SecurityToken einer Gruppe der Liste hinzufügen
				val membershipsAsList = Stream.of(memberships.split("#"))//
						.map(String::trim)//
						.collect(Collectors.toList());
				groupSecurityTokens.addAll(membershipsAsList);
			}

			// Verschiedene Rollen/Gruppen können dieselbe Berechtigung haben, deshalb rausfiltern. Wir wollen keine Einträge doppelt haben.
			for (String string : groupSecurityTokens) {
				if (!userSecurityTokens.contains(string))
					userSecurityTokens.add(string);
			}
		}

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (String string : userSecurityTokens) {
			if (!string.isEmpty())
				grantedAuthorities.add(new SimpleGrantedAuthority(string));
		}

		return grantedAuthorities;
	}

	/**
	 * @param username
	 * @return
	 */
	public List<String> loadDatabaseUserTokens(String username) {
		Table dataBaseTable = new Table();
		dataBaseTable.setName("xtcasAuthorities");
		List<Column> columns = new ArrayList<>();
		columns.add(new Column("Username", DataType.STRING));
		columns.add(new Column("Authority", DataType.STRING));
		dataBaseTable.setColumns(columns);
		Row userEntry = new Row();
		userEntry.setValues(Arrays.asList(new Value(username, null), null));
		dataBaseTable.addRow(userEntry);

		// dabei sollte nur eine ROW rauskommen, da jeder User eindeutig sein müsste
		Table membershipsFromUser = unsecurelyGetIndexView(dataBaseTable);
		List<String> userSecurityTokens = new ArrayList<>();

		if (!membershipsFromUser.getRows().isEmpty()) {
			for (Row userGroupRow : membershipsFromUser.getRows()) {
				Value userGroupName = userGroupRow.getValues().get(1);
				if (userGroupName != null && !userGroupName.getStringValue().strip().isBlank()) {
					userSecurityTokens.add(userGroupName.getStringValue().strip());
				}
			}

			// überprüfen, ob der einzigartige userSecurityToken bereits in der Liste der Memberships vorhanden war, wenn nicht, dann hinzufügen
			String uniqueUserToken = membershipsFromUser.getRows().get(0).getValues().get(1).getStringValue().replace("#", "").trim();
			if (!userSecurityTokens.contains(uniqueUserToken))
				userSecurityTokens.add(uniqueUserToken);
		} else {
			// falls der User nicht in der Datenbank gefunden wurde, wird sein Benutzername als einzigartiger userSecurityToken verwendet
			userSecurityTokens.add(username);
		}
		return userSecurityTokens;
	}

	/**
	 * @param username
	 * @return
	 */
	public List<String> loadLDAPUserTokens(String username) {
		Table tUser = new Table();
		tUser.setName("xtcasUser");
		List<Column> columns = new ArrayList<>();
		columns.add(new Column("KeyText", DataType.STRING));
		columns.add(new Column("UserSecurityToken", DataType.STRING));
		columns.add(new Column("Memberships", DataType.STRING));
		tUser.setColumns(columns);
		Row userEntry = new Row();
		userEntry.setValues(Arrays.asList(new Value(username, null), new Value("", null), new Value("", null)));
		tUser.addRow(userEntry);

		// dabei sollte nur eine ROW rauskommen, da jeder User eindeutig sein müsste
		Table membershipsFromUser = unsecurelyGetIndexView(tUser);
		List<String> userSecurityTokens = new ArrayList<>();

		if (!membershipsFromUser.getRows().isEmpty()) {
			String result = membershipsFromUser.getRows().get(0).getValues().get(2) != null
					? membershipsFromUser.getRows().get(0).getValues().get(2).getStringValue()
					: "";

			// alle SecurityTokens werden in der Datenbank mit Leerzeile und Raute voneinander getrennt
			userSecurityTokens = Stream.of(result.split("#"))//
					.map(String::trim)//
					.collect(Collectors.toList());

			// überprüfen, ob der einzigartige userSecurityToken bereits in der Liste der Memberships vorhanden war, wenn nicht, dann hinzufügen
			String uniqueUserToken = membershipsFromUser.getRows().get(0).getValues().get(1) != null
					? membershipsFromUser.getRows().get(0).getValues().get(1).getStringValue().replace("#", "").trim()
					: "";
			if (!userSecurityTokens.contains(uniqueUserToken))
				userSecurityTokens.add(uniqueUserToken);
		} else {
			// falls der User nicht in der Datenbank gefunden wurde, wird sein Benutzername als einzigartiger userSecurityToken verwendet
			userSecurityTokens.add(username);
		}
		return userSecurityTokens;
	}
}

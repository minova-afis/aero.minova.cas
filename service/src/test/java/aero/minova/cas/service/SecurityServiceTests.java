package aero.minova.cas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.ProcedureException;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import lombok.val;

//benötigt, damit JUnit-Tests nicht abbrechen
@SpringBootTest(classes = CoreApplicationSystemApplication.class, properties = { "application.runner.enabled=false" })
class SecurityServiceTests {
	@Autowired
	private SecurityService securityService;

	@Spy
	private SecurityService spySecurityService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		spySecurityService = spy(securityService);
	}

	@DisplayName("Row-Level-Security ohne Rollen")
	@WithMockUser(username = "user", roles = {})
	@Test
	void test_rowLevelSecurityWithNoRoles() {
		List<Row> userGroups = new ArrayList<>();

		assertThat(SecurityService.rowLevelSecurity(false, userGroups))//
				.isEqualTo("\r\nand ( ( SecurityToken IS NULL ) )");
		assertThat(SecurityService.rowLevelSecurity(true, userGroups))//
				.isEqualTo("\r\nwhere ( ( SecurityToken IS NULL ) )");
	}

	@DisplayName("Row-Level-Security mit mehreren Rollen")
	@WithMockUser(username = "user", roles = { "user", "dispatcher", "codemonkey" })
	@Test
	void test_rowLevelSecurityMultipleRoles() {
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("user", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);

		inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("dispatcher", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);

		inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("codemonkey", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);
		assertThat(SecurityService.rowLevelSecurity(false, userGroups))//
				.isEqualTo("\r\nand ( ( SecurityToken IS NULL )" + "\r\nor ( SecurityToken IN ('user','dispatcher','codemonkey') ) )");
		assertThat(SecurityService.rowLevelSecurity(true, userGroups))//
				.isEqualTo("\r\nwhere ( ( SecurityToken IS NULL )" + "\r\nor ( SecurityToken IN ('user','dispatcher','codemonkey') ) )");
	}

	@DisplayName("Row-Level-Security mit mehreren Rollen, aber eine darf alle Spalten sehen")
	@WithMockUser(username = "user", roles = { "user", "dispatcher", "codemonkey" })
	@Test
	void test_rowLevelSecurityWithOneAuthenticatedRole() {
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("user", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);

		inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("dispatcher", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);

		inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("codemonkey", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		assertThat(SecurityService.rowLevelSecurity(false, userGroups))//
				.isEmpty();
	}

	@DisplayName("Frage nach mehreren Spalten, bekomme alle zurück.")
	@WithMockUser(username = "user", roles = { "dispatcher" })
	@Test
	void test_ViewStringWithAuthenticatedUser() {
		val inputTable = new Table();
		inputTable.setName("vJournalIndexTest");
		inputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		inputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		inputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		inputTable.addColumn(new Column("&", DataType.BOOLEAN));
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("vJournalIndexTest", null));
		inputRow.addValue(new Value("dispatcher", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);

		List<Column> resultColumns = new ArrayList<>();
		resultColumns.add(new Column("OrderReceiverKey", DataType.INTEGER));
		resultColumns.add(new Column("ServiceKey", DataType.STRING));
		resultColumns.add(new Column("ChargedQuantity", DataType.STRING));

		Table result = securityService.columnSecurity(inputTable, userGroups);
		assertThat(result.getColumns().equals(resultColumns));
	}

	@DisplayName("Frage nach mehreren Spalten, bekomme aber nur die mit Berechtigung zurück.")
	@WithMockUser(username = "admin")
	@Test
	void test_ViewStringWithAuthenticatedUserButOneBlockedColumn() {
		val inputTable = new Table();
		inputTable.setName("vJournalIndexTest");
		inputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		inputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		inputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		inputTable.addColumn(new Column("&", DataType.BOOLEAN));
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("vJournalIndexTest", null));
		inputRow.addValue(new Value("admin", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);

		List<Column> resultColumns = new ArrayList<>();
		resultColumns.add(new Column("OrderReceiverKey", DataType.INTEGER));
		resultColumns.add(new Column("ChargedQuantity", DataType.STRING));

		Table result = securityService.columnSecurity(inputTable, userGroups);
		assertThat(result.getColumns().equals(resultColumns));
	}

	@DisplayName("Mehrere Rollen mit Einschränkungen")
	@WithMockUser(username = "admin", roles = { "admin", "dispo" })
	@Test
	void test_ViewStringWithAuthenticatedUserButMultipleBlockedColumn() {
		val inputTable = new Table();
		inputTable.setName("vJournalIndexTest");
		inputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		inputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		inputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		inputTable.addColumn(new Column("&", DataType.BOOLEAN));
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("vJournalIndexTest", null));
		inputRow.addValue(new Value("admin", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		inputRow = new Row();
		inputRow.addValue(new Value("vJournalIndexTest", null));
		inputRow.addValue(new Value("dispo", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);

		List<Column> resultColumns = new ArrayList<>();
		resultColumns.add(new Column("OrderReceiverKey", DataType.INTEGER));
		resultColumns.add(new Column("ServiceKey", DataType.STRING));
		resultColumns.add(new Column("ChargedQuantity", DataType.STRING));

		Table result = securityService.columnSecurity(inputTable, userGroups);
		assertThat(result.getColumns().equals(resultColumns));
	}

	@DisplayName("Frage nach geblockter Spalte mit Wert, bekomme nur sichtbare Spalten ohne Abfrage auf Wert")
	@WithMockUser(username = "admin", roles = { "admin" })
	@Test
	void test_ViewStringWithAuthenticatedUserButBlockedWhereClause() {
		val inputTable = new Table();
		inputTable.setName("vJournalIndexTest");
		inputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		inputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		inputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		inputTable.addColumn(new Column("&", DataType.BOOLEAN));
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("", null));
			inputRow.addValue(new Value("3", ">"));
			inputRow.addValue(new Value("", null));
			inputRow.addValue(new Value(false, null));
			inputTable.addRow(inputRow);
		}
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("vJournalIndexTest", null));
		inputRow.addValue(new Value("admin", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);

		List<Column> resultColumns = new ArrayList<>();
		resultColumns.add(new Column("OrderReceiverKey", DataType.INTEGER));
		resultColumns.add(new Column("ChargedQuantity", DataType.STRING));

		Table result = securityService.columnSecurity(inputTable, userGroups);
		assertThat(result.getColumns().equals(resultColumns));
	}

	@DisplayName("User hat keine Berechtigung, um auf Tabelle zuzugreifen, aber hat einen Eintrag in der tColumnSecurity")
	@WithMockUser(username = "admin", roles = { "afis" })
	@Test
	void test_ViewStringWithUnauthenticatedUserWithBlockedColumns() throws Exception {
		val inputTable = new Table();
		inputTable.setName("vJournalIndexTest");
		inputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		inputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		inputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		inputTable.addColumn(new Column("&", DataType.BOOLEAN));
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("0", ">"));
			inputRow.addValue(new Value("3", ">"));
			inputRow.addValue(new Value("5", ">"));
			inputRow.addValue(new Value(false, null));
			inputTable.addRow(inputRow);
		}
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("vWorkingTimeIndex2", null));
		inputRow.addValue(new Value("afis", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		Table mockResult = new Table();

		CustomLogger logger = Mockito.mock(CustomLogger.class);
		spySecurityService.customLogger = logger;

		doReturn(mockResult).when(spySecurityService).unsecurelyGetIndexView(Mockito.any());

		Throwable exception = assertThrows(RuntimeException.class, () -> spySecurityService.columnSecurity(inputTable, userGroups));
		assertThat(exception).hasMessage("msg.ColumnSecurityError %admin %vJournalIndexTest");

	}

	@DisplayName("Frage nach Tabelle (*), bekomme berechitgte Spalten zurück.")
	@WithMockUser(username = "admin", roles = { "admin" })
	@Test
	void test_ViewStringWithAuthenticatedUserWithNoHead() {
		val inputTable = new Table();
		inputTable.setName("vJournalIndexTest");
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("vJournalIndexTest", null));
		inputRow.addValue(new Value("admin", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);

		List<Column> resultColumns = new ArrayList<>();
		resultColumns.add(new Column("OrderReceiverKey", DataType.INTEGER));
		resultColumns.add(new Column("ChargedQuantity", DataType.STRING));

		Table result = securityService.columnSecurity(inputTable, userGroups);
		assertThat(result.getColumns().equals(resultColumns));
	}

	@DisplayName("Frage nach mehreren Spalten mit bestimmten Werten, bekomme alle zurück, da berechtigt, aber eine Rolle hätte keine Berechtigung.")
	@WithMockUser(username = "admin", roles = { "dispatcher", "user" })
	@Test
	void test_ViewStringWithOneAuthenticatedUserWithNoBlockedColumns() {
		val inputTable = new Table();
		inputTable.setName("vJournalIndexTest");
		inputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		inputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		inputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		inputTable.addColumn(new Column("&", DataType.BOOLEAN));
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("0", ">"));
			inputRow.addValue(new Value("3", ">"));
			inputRow.addValue(new Value("5", ">"));
			inputRow.addValue(new Value(false, null));
			inputTable.addRow(inputRow);
		}
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("vJournalIndexTest", null));
		inputRow.addValue(new Value("dispatcher", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		inputRow.addValue(new Value("vJournalIndexTest", null));
		inputRow.addValue(new Value("user", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);

		List<Column> resultColumns = new ArrayList<>();
		resultColumns.add(new Column("OrderReceiverKey", DataType.INTEGER));
		resultColumns.add(new Column("ServiceKey", DataType.STRING));
		resultColumns.add(new Column("ChargedQuantity", DataType.STRING));

		Table result = securityService.columnSecurity(inputTable, userGroups);
		assertThat(result.getColumns().equals(resultColumns));
	}

	@DisplayName("Frage nach mehreren Spalten mit bestimmten Werten, bekomme alle zurück, da eine Rolle für die gesamte Table berechtigt ist.")
	@WithMockUser(username = "admin", roles = { "admin", "dispatcher", "user" })
	@Test
	void test_ViewStringWithMultipleAuthenticatedUserWithNoBlockedColumns() {
		val inputTable = new Table();
		inputTable.setName("vJournalIndexTest");
		inputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		inputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		inputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		inputTable.addColumn(new Column("&", DataType.BOOLEAN));

		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("vJournalIndexTest", null));
		inputRow.addValue(new Value("admin", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		inputRow = new Row();
		inputRow.addValue(new Value("vJournalIndexTest", null));
		inputRow.addValue(new Value("dispatcher", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		inputRow.addValue(new Value("vJournalIndexTest", null));
		inputRow.addValue(new Value("user", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);

		List<Column> resultColumns = new ArrayList<>();
		resultColumns.add(new Column("OrderReceiverKey", DataType.INTEGER));
		resultColumns.add(new Column("ServiceKey", DataType.STRING));
		resultColumns.add(new Column("ChargedQuantity", DataType.STRING));
		Table mockResult = new Table();
		mockResult.addColumns(resultColumns);

		Table result = securityService.columnSecurity(inputTable, userGroups);
		assertThat(result.getColumns().equals(resultColumns));
	}

	@DisplayName("ExtractUserTokens keine Ausnahmen")
	@WithMockUser(username = "user", roles = {})
	@Test
	void test_extractUserTokens() {
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("user", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);

		inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("dispatcher", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);

		inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("codemonkey", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);
		List<String> resultList = SecurityService.extractUserTokens(userGroups);
		assertThat(resultList).hasSize(3).containsExactly("user", "dispatcher", "codemonkey");
	}

	@DisplayName("ExtractUserTokens eine Ausnahmen")
	@WithMockUser(username = "user", roles = { "user", "dispatcher", "codemonkey" })
	@Test
	void test_extractUserTokensGetEmptyStringBack() {
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("user", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);

		inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("dispatcher", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);

		inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("codemonkey", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);
		List<String> resultList = SecurityService.extractUserTokens(userGroups);

		assertThat(resultList).isEmpty();
	}

	@DisplayName("getPrivilegePermission-Rows überprüfen, ob alle Privilegien übernommen werden")
	@WithMockUser(username = "user", roles = { "user", "dispatcher", "codemonkey" })
	@Test
	void test_getPrivilegePermission() {

		List<Row> mockResult = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("test", null));
		inputRow.addValue(new Value("ROLE_user", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		mockResult.add(inputRow);

		inputRow = new Row();
		inputRow.addValue(new Value("test", null));
		inputRow.addValue(new Value("ROLE_dispatcher", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(true, null));
		mockResult.add(inputRow);

		inputRow = new Row();
		inputRow.addValue(new Value("test", null));
		inputRow.addValue(new Value("ROLE_codemonkey", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(true, null));
		mockResult.add(inputRow);

		Mockito.doAnswer(returnsFirstArg()).when(spySecurityService).unsecurelyGetIndexView(Mockito.any());
		Mockito.doNothing().when(spySecurityService).loadAllPrivileges();

		List<Row> result = spySecurityService.getPrivilegePermissions("test");
		assertThat(result).hasSize(3);
		assertThat(result.get(2).getValues().get(1).getStringValue()).isEqualTo(mockResult.get(0).getValues().get(1).getStringValue());
		assertThat(result.get(1).getValues().get(1).getStringValue()).isEqualTo(mockResult.get(1).getValues().get(1).getStringValue());
		assertThat(result.get(0).getValues().get(1).getStringValue()).isEqualTo(mockResult.get(2).getValues().get(1).getStringValue());
	}

	@DisplayName("getPrivilegePermission-Rows überprüfen, aber es gibt keine UserGruppen")
	@WithMockUser(username = "user", roles = {})
	@Test
	void test_getPrivilegePermissionNoPermissions() {

		Mockito.doAnswer(returnsFirstArg()).when(spySecurityService).unsecurelyGetIndexView(Mockito.any());
		Mockito.doNothing().when(spySecurityService).loadAllPrivileges();

		List<Row> result = spySecurityService.getPrivilegePermissions("test");
		assertThat(result).isEmpty();
	}

	@DisplayName("Finde Spalte mit SecurityToken per findSecurityTokenColumn")
	@WithMockUser(username = "user", roles = {})
	@Test
	void test_findSecurityTokenColumn() throws ProcedureException {
		val inputTable = new Table();
		inputTable.setName("spTest");
		inputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		inputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		inputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		inputTable.addColumn(new Column("SecurityToken", DataType.STRING));
		inputTable.addColumn(new Column("&", DataType.BOOLEAN));

		int result = spySecurityService.findSecurityTokenColumn(inputTable);
		assertThat(result).isEqualTo(3);
	}

	@DisplayName("Wirf ProcedureException, wenn SecurityTokenSpalte nicht vorhanden")
	@WithMockUser(username = "user", roles = {})
	@Test
	void test_findSecurityTokenColumnNoSecurityTokenColumn() {
		val inputTable = new Table();
		inputTable.setName("spTest");
		inputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		inputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		inputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		inputTable.addColumn(new Column("&", DataType.BOOLEAN));

		CustomLogger logger = Mockito.mock(CustomLogger.class);
		spySecurityService.customLogger = logger;

		Throwable exception = assertThrows(ProcedureException.class, () -> spySecurityService.findSecurityTokenColumn(inputTable));
		assertThat(exception).hasMessage("msg.MissingSecurityTokenColumn");
	}

	@DisplayName("Überprüfe, ob SecurityToken in Row übereinstimmt mit vorhandenen SecurityTokens")
	@WithMockUser(username = "user", roles = {})
	@Test
	void test_checkRowForValidSecurityToken() {

		List<String> userGroups = new ArrayList<>();
		userGroups.add("tester");
		userGroups.add("user");
		userGroups.add("dispatcher");
		userGroups.add("codemonkey");

		Row rowToBeChecked = new Row();
		rowToBeChecked.addValue(new Value("", null));
		rowToBeChecked.addValue(new Value("dispatcher", null));
		rowToBeChecked.addValue(new Value("", null));
		rowToBeChecked.addValue(new Value(true, null));

		assertTrue(spySecurityService.isRowAccessValid(userGroups, rowToBeChecked, 1));
	}

	@DisplayName("Überprüfe, ob SecurityToken in Row nicht übereinstimmt mit vorhandenen SecurityTokens")
	@WithMockUser(username = "user", roles = {})
	@Test
	void test_checkRowForValidSecurityTokenNoMatch() {

		List<String> userGroups = new ArrayList<>();
		userGroups.add("tester");
		userGroups.add("user");
		userGroups.add("dispatcher");
		userGroups.add("codemonkey");

		Row rowToBeChecked = new Row();
		rowToBeChecked.addValue(new Value("", null));
		rowToBeChecked.addValue(new Value("admin", null));
		rowToBeChecked.addValue(new Value("", null));
		rowToBeChecked.addValue(new Value(true, null));

		assertFalse(spySecurityService.isRowAccessValid(userGroups, rowToBeChecked, 1));
	}
}
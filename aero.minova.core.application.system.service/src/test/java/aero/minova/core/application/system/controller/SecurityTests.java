package aero.minova.core.application.system.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;
import lombok.val;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
class SecurityTests {

	@Autowired
	SqlViewController testSubject;

	@Spy
	SqlViewController spyController;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		spyController = spy(testSubject);
	}

	@Test
	void test_checkPrivilege() {
		String tableName = "tEmployee";
		List<GrantedAuthority> ga = new ArrayList<>();
		ga.add(new SimpleGrantedAuthority("admin"));
		Table user = new Table();
		user.addColumn(new Column("", DataType.STRING));
		Row r = new Row();
		r.addValue(new Value("admin", null));
		user.addRow(r);

		doReturn(user).when(spyController).getTableForSecurityCheck(Mockito.any());

		assertThat(spyController.getPrivilegePermissions(ga, tableName).getRows().isEmpty())//
				.isEqualTo(false);
		ga.clear();
		ga.add(new SimpleGrantedAuthority("dispatcher"));

		doReturn(new Table()).when(spyController).getTableForSecurityCheck(Mockito.any());

		assertThat(spyController.getPrivilegePermissions(ga, tableName).getRows().isEmpty())//
				.isEqualTo(true);
	}

	@DisplayName("Row-Level-Security ohne Rollen")
	@WithMockUser(username = "user", roles = {})
	@Test
	void test_rowLevelSecurityWithNoRoles() {
		Row inputRow = new Row();
		List<Row> userGroups = new ArrayList<>();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);

		assertThat(testSubject.rowLevelSecurity(false, userGroups))//
				.isEqualTo("\r\nwhere ( ( SecurityToken IS NULL ) )");
		assertThat(testSubject.rowLevelSecurity(true, userGroups))//
				.isEqualTo("\r\nand ( ( SecurityToken IS NULL ) )");
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
		assertThat(testSubject.rowLevelSecurity(false, userGroups))//
				.isEqualTo("\r\nwhere ( ( SecurityToken IS NULL )" + "\r\nor ( SecurityToken IN ('ROLE_codemonkey','ROLE_dispatcher','ROLE_user') ) )");
		assertThat(testSubject.rowLevelSecurity(true, userGroups))//
				.isEqualTo("\r\nand ( ( SecurityToken IS NULL )" + "\r\nor ( SecurityToken IN ('ROLE_codemonkey','ROLE_dispatcher','ROLE_user') ) )");
	}

	@DisplayName("Row-Level-Security mit mehreren Rollen, aber nur 2 von 3 dürften auf die Tabelle zugreifen (man soll trotzdem im WHERE 3 Rollen haben")
	@WithMockUser(username = "user", roles = { "user", "dispatcher", "codemonkey" })
	@Test
	void test_rowLevelSecurityMultipleRolesAndOneExtra() {
		List<Row> userGroups = new ArrayList<>();
		Row inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("user", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);

		inputRow = new Row();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("codemonkey", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);
		assertThat(testSubject.rowLevelSecurity(false, userGroups))//
				.isEqualTo("\r\nwhere ( ( SecurityToken IS NULL )" + "\r\nor ( SecurityToken IN ('ROLE_codemonkey','ROLE_dispatcher','ROLE_user') ) )");
		assertThat(testSubject.rowLevelSecurity(true, userGroups))//
				.isEqualTo("\r\nand ( ( SecurityToken IS NULL )" + "\r\nor ( SecurityToken IN ('ROLE_codemonkey','ROLE_dispatcher','ROLE_user') ) )");
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
		assertThat(testSubject.rowLevelSecurity(false, userGroups))//
				.isEqualTo("");
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

		doReturn(inputTable).when(spyController).getTableForSecurityCheck(Mockito.any());

		Table result = spyController.columnSecurity(inputTable, userGroups);
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
		Table mockResult = new Table();
		mockResult.addColumns(resultColumns);

		doReturn(mockResult).when(spyController).getTableForSecurityCheck(Mockito.any());

		Table result = spyController.columnSecurity(inputTable, userGroups);
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

		Table mockResult = new Table();
		mockResult.addColumns(resultColumns);

		doReturn(mockResult).when(spyController).getTableForSecurityCheck(Mockito.any());

		Table result = spyController.columnSecurity(inputTable, userGroups);
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

		Table mockResult = new Table();
		mockResult.addColumns(resultColumns);

		doReturn(mockResult).when(spyController).getTableForSecurityCheck(Mockito.any());

		Table result = spyController.columnSecurity(inputTable, userGroups);
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

		doReturn(mockResult).when(spyController).getTableForSecurityCheck(Mockito.any());

		Throwable exception = assertThrows(RuntimeException.class, () -> spyController.columnSecurity(inputTable, userGroups));
		thrown.expect(RuntimeException.class);
		assertEquals("Insufficient Permission for vJournalIndexTest; User with Username 'admin'" + " is not allowed to see the selected columns of this table",
				exception.getMessage());

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

		Table mockResult = new Table();
		mockResult.addColumns(resultColumns);

		doReturn(mockResult).when(spyController).getTableForSecurityCheck(Mockito.any());

		Table result = spyController.columnSecurity(inputTable, userGroups);
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

		Table mockResult = new Table();
		mockResult.addColumns(resultColumns);

		doReturn(mockResult).when(spyController).getTableForSecurityCheck(Mockito.any());

		Table result = spyController.columnSecurity(inputTable, userGroups);
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

		doReturn(mockResult).when(spyController).getTableForSecurityCheck(Mockito.any());

		Table result = spyController.columnSecurity(inputTable, userGroups);
		assertThat(result.getColumns().equals(resultColumns));

	}

}
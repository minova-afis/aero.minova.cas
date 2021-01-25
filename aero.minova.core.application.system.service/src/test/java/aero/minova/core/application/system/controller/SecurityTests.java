package aero.minova.core.application.system.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test
	void test_checkPrivilege() {
		String tableName = "tEmployee";
		String groupName = "admin";
		String secondGroup = "dispatcher";
		assertThat(testSubject.checkPrivilege(groupName,tableName))//
			.isEqualTo(true);
		assertThat(testSubject.checkPrivilege(secondGroup, tableName))//
			.isEqualTo(false);
	}
	
	@DisplayName("Row-Level-Security ohne Rollen")
	@WithMockUser(username="user", roles = {})
	@Test
	void test_rowLevelSecurityWithNoUser() {
		assertThat(testSubject.rowLevelSecurity(false))//
			.isEqualTo("\r\nwhere ( ( SecurityToken IS NULL ) )");
		assertThat(testSubject.rowLevelSecurity(true))//
		.isEqualTo("\r\nand ( ( SecurityToken IS NULL ) )");
	}
	
	@DisplayName("Row-Level-Security mit mehreren Rollen")
	@WithMockUser(username="user", roles = {"user", "dispatcher" ,"codemonkey"})
	@Test
	void test_rowLevelSecurityMultipleRoles() {
		assertThat(testSubject.rowLevelSecurity(false))//
			.isEqualTo("\r\nwhere ( ( SecurityToken IS NULL )"
					+ "\r\nor ( SecurityToken IN ('codemonkey','dispatcher','user') ) )");
		assertThat(testSubject.rowLevelSecurity(true))//
			.isEqualTo("\r\nand ( ( SecurityToken IS NULL )"
					+ "\r\nor ( SecurityToken IN ('codemonkey','dispatcher','user') ) )");
	}
	
	@DisplayName("Frage nach mehreren Spalten, bekomme alle zurück.")
	@WithMockUser(username="user", roles = {"dispatcher"})
	@Test
	void test_ViewStringWithAuthenticatedUser() {
		val intputTable = new Table();
		intputTable.setName("vJournalIndexTest");
		intputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		intputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		intputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		intputTable.addColumn(new Column("&", DataType.BOOLEAN));
		assertThat(testSubject.prepareViewString(intputTable, true, 1000))//
			.isEqualTo("select top 1000 OrderReceiverKey, ServiceKey, ChargedQuantity from vJournalIndexTest"
					+ "\r\nwhere ( ( SecurityToken IS NULL )"
					+ "\r\nor ( SecurityToken IN ('dispatcher') ) )");
	}
	
	@DisplayName("Frage nach mehreren Spalten, bekomme aber nur die mit Berechtigung zurück.")
	@WithMockUser(username="admin", roles = {"admin"})
	@Test
	void test_ViewStringWithAuthenticatedUserButOneBlockedColumn() {
		val intputTable = new Table();
		intputTable.setName("vJournalIndexTest");
		intputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		intputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		intputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		intputTable.addColumn(new Column("&", DataType.BOOLEAN));
		assertThat(testSubject.prepareViewString(intputTable, true, 1000))//
			.isEqualTo("select top 1000 OrderReceiverKey, ChargedQuantity from vJournalIndexTest"
					+ "\r\nwhere ( ( SecurityToken IS NULL )"
					+ "\r\nor ( SecurityToken IN ('admin') ) )");
	}
	
	@DisplayName("Mehrere Rollen mit Einschränkungen")
	@WithMockUser(username="admin", roles = {"admin","dispo"})
	@Test
	void test_ViewStringWithAuthenticatedUserButMultipleBlockedColumn() {
		val intputTable = new Table();
		intputTable.setName("vJournalIndexTest");
		intputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		intputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		intputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		intputTable.addColumn(new Column("&", DataType.BOOLEAN));
		assertThat(testSubject.prepareViewString(intputTable, true, 1000))//
			.isEqualTo("select top 1000 OrderReceiverKey, ServiceKey, ChargedQuantity from vJournalIndexTest"
					+ "\r\nwhere ( ( SecurityToken IS NULL )"
					+ "\r\nor ( SecurityToken IN ('admin','dispo') ) )");
	}
	
	@DisplayName("Frage nach geblockter Spalte mit Wert, bekomme nur sichtbare Spalten ohne Abfrage auf Wert")
	@WithMockUser(username="admin", roles = {"admin"})
	@Test
	void test_ViewStringWithAuthenticatedUserButBlockedWhereClause() {
		val intputTable = new Table();
		intputTable.setName("vJournalIndexTest");
		intputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		intputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		intputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		intputTable.addColumn(new Column("&", DataType.BOOLEAN));
		{
		Row inputRow = new Row();
		inputRow.addValue(new Value(""));
		inputRow.addValue(new Value(">"+"3"));
		inputRow.addValue(new Value(""));
		inputRow.addValue(new Value(false));
		intputTable.addRow(inputRow);
		}
		assertThat(testSubject.prepareViewString(intputTable, true, 1000))//
			.isEqualTo("select top 1000 OrderReceiverKey, ChargedQuantity from vJournalIndexTest"
					+ "\r\nwhere ( ( SecurityToken IS NULL )"
					+ "\r\nor ( SecurityToken IN ('admin') ) )");
	}
	
	@DisplayName("Frage nach mehreren Spalten mit bestimmten Werten, aber eine Spalte mit angefragtem Wert ist blockiert.")
	@WithMockUser(username="admin", roles = {"admin"})
	@Test
	void test_ViewStringWithAuthenticatedUserButBlockedColumnAndBlockedWhereClause() {
		val intputTable = new Table();
		intputTable.setName("vJournalIndexTest");
		intputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		intputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		intputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		intputTable.addColumn(new Column("&", DataType.BOOLEAN));
		{
		Row inputRow = new Row();
		inputRow.addValue(new Value(">"+"0"));
		inputRow.addValue(new Value(">"+"3"));
		inputRow.addValue(new Value(">"+"5"));
		inputRow.addValue(new Value(false));
		intputTable.addRow(inputRow);
		}
		assertThat(testSubject.prepareViewString(intputTable, true, 1000))//
			.isEqualTo("select top 1000 OrderReceiverKey, ChargedQuantity from vJournalIndexTest"
					+ "\r\nwhere (OrderReceiverKey > '0' and ChargedQuantity > '5')"
					+ "\r\nand ( ( SecurityToken IS NULL )"
					+ "\r\nor ( SecurityToken IN ('admin') ) )");
	
	}
	
	@DisplayName("Frage nach mehreren Spalten mit bestimmten Werten, bekomme alle zurück, da berechtigt.")
	@WithMockUser(username="admin", roles = {"dispatcher"})
	@Test
	void test_ViewStringWithAuthenticatedUserWithNoBlockedColumns() {
		val intputTable = new Table();
		intputTable.setName("vJournalIndexTest");
		intputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		intputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		intputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		intputTable.addColumn(new Column("&", DataType.BOOLEAN));
		{
		Row inputRow = new Row();
		inputRow.addValue(new Value(">"+"0"));
		inputRow.addValue(new Value(">"+"3"));
		inputRow.addValue(new Value(">"+"5"));
		inputRow.addValue(new Value(false));
		intputTable.addRow(inputRow);
		}
		assertThat(testSubject.prepareViewString(intputTable, true, 1000))//
			.isEqualTo("select top 1000 OrderReceiverKey, ServiceKey, ChargedQuantity from vJournalIndexTest"
					+ "\r\nwhere (OrderReceiverKey > '0' and ServiceKey > '3' and ChargedQuantity > '5')"
					+ "\r\nand ( ( SecurityToken IS NULL )"
					+ "\r\nor ( SecurityToken IN ('dispatcher') ) )");
	
	}
	
	@DisplayName("User hat keine  Berechtigung, um auf Tabelle zuzugreifen, aber hat einen Eintrag in der tColumnSecurity")
	@WithMockUser(username="admin", roles = {"afis"})
	@Test
	void test_ViewStringWithUnauthenticatedUserWithBlockedColumns() throws Exception {
		val intputTable = new Table();
		intputTable.setName("vJournalIndexTest");
		intputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		intputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		intputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		intputTable.addColumn(new Column("&", DataType.BOOLEAN));
		{
		Row inputRow = new Row();
		inputRow.addValue(new Value(">"+"0"));
		inputRow.addValue(new Value(">"+"3"));
		inputRow.addValue(new Value(">"+"5"));
		inputRow.addValue(new Value(false));
		intputTable.addRow(inputRow);
		}
		Throwable exception = assertThrows(RuntimeException.class, () -> testSubject.getIndexViewUnsecure(intputTable));
		thrown.expect(RuntimeException.class);
		assertEquals("java.lang.RuntimeException: Insufficient Permission for vJournalIndexTest; User with Username 'admin'"
				+ " is not allowed to see the selected columns of this table", exception.getMessage());
	
	}
	
	@DisplayName("Frage nach Tabelle (*), bekomme berechitgte Spalten zurück.")
	@WithMockUser(username="admin", roles = {"admin"})
	@Test
	void test_ViewStringWithAuthenticatedUserWithNoHead() {
		val intputTable = new Table();
		intputTable.setName("vJournalIndexTest");
		assertThat(testSubject.prepareViewString(intputTable, true, 1000))//
			.isEqualTo("select top 1000 OrderReceiverKey, ChargedQuantity from vJournalIndexTest"
					+ "\r\nwhere ( ( SecurityToken IS NULL )"
					+ "\r\nor ( SecurityToken IN ('admin') ) )");
	
	}
	
	@DisplayName("Frage nach mehreren Spalten mit bestimmten Werten, bekomme alle zurück, da berechtigt, aber eine Rolle hätte keine Berechtigung.")
	@WithMockUser(username="admin", roles = {"dispatcher", "user"})
	@Test
	void test_ViewStringWithOneAuthenticatedUserWithNoBlockedColumns() {
		val intputTable = new Table();
		intputTable.setName("vJournalIndexTest");
		intputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		intputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		intputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		intputTable.addColumn(new Column("&", DataType.BOOLEAN));
		{
		Row inputRow = new Row();
		inputRow.addValue(new Value(">"+"0"));
		inputRow.addValue(new Value(">"+"3"));
		inputRow.addValue(new Value(">"+"5"));
		inputRow.addValue(new Value(false));
		intputTable.addRow(inputRow);
		}
		assertThat(testSubject.prepareViewString(intputTable, true, 1000))//
			.isEqualTo("select top 1000 OrderReceiverKey, ServiceKey, ChargedQuantity from vJournalIndexTest"
					+ "\r\nwhere (OrderReceiverKey > '0' and ServiceKey > '3' and ChargedQuantity > '5')"
					+ "\r\nand ( ( SecurityToken IS NULL )"
					+ "\r\nor ( SecurityToken IN ('dispatcher','user') ) )");
	
	}
	
	@DisplayName("Frage nach mehreren Spalten mit bestimmten Werten, bekomme alle zurück, da eine Rolle für die gesamte Table berechtigt ist.")
	@WithMockUser(username="admin", roles = {"admin","dispatcher","user"})
	@Test
	void test_ViewStringWithMultipleAuthenticatedUserWithNoBlockedColumns() {
		val intputTable = new Table();
		intputTable.setName("vJournalIndexTest");
		intputTable.addColumn(new Column("OrderReceiverKey", DataType.INTEGER));
		intputTable.addColumn(new Column("ServiceKey", DataType.STRING));
		intputTable.addColumn(new Column("ChargedQuantity", DataType.STRING));
		intputTable.addColumn(new Column("&", DataType.BOOLEAN));
		{
		Row inputRow = new Row();
		inputRow.addValue(new Value(">"+"0"));
		inputRow.addValue(new Value(">"+"3"));
		inputRow.addValue(new Value(">"+"5"));
		inputRow.addValue(new Value(false));
		intputTable.addRow(inputRow);
		}
		assertThat(testSubject.prepareViewString(intputTable, true, 1000))//
			.isEqualTo("select top 1000 OrderReceiverKey, ServiceKey, ChargedQuantity from vJournalIndexTest"
					+ "\r\nwhere (OrderReceiverKey > '0' and ServiceKey > '3' and ChargedQuantity > '5')"
					+ "\r\nand ( ( SecurityToken IS NULL )"
					+ "\r\nor ( SecurityToken IN ('admin','dispatcher','user') ) )");
	
	}
	
}
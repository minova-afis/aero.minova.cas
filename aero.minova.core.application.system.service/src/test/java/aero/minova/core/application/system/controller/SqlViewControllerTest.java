package aero.minova.core.application.system.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.slf4j.helpers.NOPLogger.NOP_LOGGER;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.ProcedureException;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;
import aero.minova.core.application.system.sql.SqlUtils;
import lombok.val;

//benötigt, damit JUnit-Tests nicht abbrechen
@SpringBootTest(properties = { "application.runner.enabled=false" })
class SqlViewControllerTest {

	@Autowired
	SqlViewController testSubject;

	@DisplayName("Wähle Einträge ohne Einschränkungen aus.")
	@Test
	void testPrepareViewString_withStarSelect() {
		Table inputTable = new Table();
		inputTable.setName("vWorkingTimeIndex2");
		Row inputRow = new Row();
		List<Row> userGroups = new ArrayList<>();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		assertThat(testSubject.prepareViewString(inputTable, true, 1000, userGroups))//
				.isEqualTo("select top 1000 * from vWorkingTimeIndex2");
	}

	@DisplayName("Wähle alle Einträge mit einem bestimmten Wert eines Feldes.")
	@Test
	void testPrepareViewString_withSelectByOneAttributeValue() {
		Table inputTable = new Table();
		inputTable.setName("vWorkingTimeIndex2");
		inputTable.addColumn(new Column("EmployeeText", DataType.STRING));
		inputTable.addColumn(Column.AND_FIELD);
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("AVM", null));
			inputRow.addValue(new Value(false, null));
			inputTable.addRow(inputRow);
		}
		Row inputRow = new Row();
		List<Row> userGroups = new ArrayList<>();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		assertThat(testSubject.prepareViewString(inputTable, true, 1000, userGroups))//
				.isEqualTo("select top 1000 EmployeeText from vWorkingTimeIndex2\r\nwhere ((EmployeeText like ?) )");
		assertThat(inputTable.getRows().get(0).getValues().get(0).getStringValue()).isEqualTo("AVM%");
	}

	@DisplayName("Wähle alle Einträge mit jeweils einen bestimmten Werten in zwei Feldern.")
	@Test
	void testPrepareViewString_withSelectByMultipleAttributeValue() {
		Table inputTable = new Table();
		inputTable.setName("vWorkingTimeIndex2");
		inputTable.addColumn(new Column("EmployeeText", DataType.STRING));
		inputTable.addColumn(new Column("CustomerText", DataType.STRING));
		inputTable.addColumn(Column.AND_FIELD);
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("AVM", null));
			inputRow.addValue(new Value("MIN", null));
			inputRow.addValue(new Value(false, null));
			inputTable.addRow(inputRow);
		}
		Row inputRow = new Row();
		List<Row> userGroups = new ArrayList<>();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		assertThat(testSubject.prepareViewString(inputTable, true, 1000, userGroups))//
				.isEqualTo("select top 1000 EmployeeText, CustomerText from vWorkingTimeIndex2\r\nwhere ((EmployeeText like ? and CustomerText like ?) )");
	}

	@DisplayName("Wähle alle Einträge eines Datumsbereiches.")
	@Test
	void testPrepareViewString_withSelectByMultipleConditionsOnSameAttribute() {
		Table inputTable = new Table();
		inputTable.setName("vWorkingTimeIndex2");
		inputTable.addColumn(new Column("BookingDate", DataType.STRING));
		inputTable.addColumn(Column.AND_FIELD);
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value(LocalDate.of(2020, 7, 31).toString(), "<="));
			inputRow.addValue(new Value(false, null));
			inputTable.addRow(inputRow);
		}
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value(LocalDate.of(2020, 7, 29).toString(), ">"));
			inputRow.addValue(new Value(true, null));
			inputTable.addRow(inputRow);
		}
		Row inputRow = new Row();
		List<Row> userGroups = new ArrayList<>();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		assertThat(testSubject.prepareViewString(inputTable, true, 1000, userGroups))//
				.isEqualTo("select top 1000 BookingDate from vWorkingTimeIndex2\r\n" //
						+ "where ((BookingDate <= ?)\r\n"//
						+ "  and (BookingDate > ?) )");
	}

	@DisplayName("Wähle all Einträge von 2 Mitarbeitern aus.")
	@Test
	void testPrepareViewString_withSelectByMultipleOptionalRules() {
		Table inputTable = new Table();
		inputTable.setName("vWorkingTimeIndex2");
		inputTable.addColumn(new Column("EmployeeText", DataType.STRING));
		inputTable.addColumn(Column.AND_FIELD);
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("AVM", null));
			inputRow.addValue(new Value(false, null));
			inputTable.addRow(inputRow);
		}
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("WIS", null));
			inputRow.addValue(new Value(false, null));
			inputTable.addRow(inputRow);
		}
		Row inputRow = new Row();
		List<Row> userGroups = new ArrayList<>();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		assertThat(testSubject.prepareViewString(inputTable, true, 1000, userGroups))//
				.isEqualTo("select top 1000 EmployeeText from vWorkingTimeIndex2\r\n" //
						+ "where ((EmployeeText like ?)\r\n"//
						+ "   or (EmployeeText like ?) )");
	}

	@Test
	void testConvertSqlResultToRow_UnsupportedTypes() throws SQLException {
		val outputTable = new Table();
		outputTable.setName("vWorkingTimeIndex2");
		outputTable.addColumn(new Column("LastDate", null));
		val sqlSet = Mockito.mock(ResultSet.class);
		val time = Instant.ofEpochMilli(1598613904487L).toString();
		when(sqlSet.getString("LastDate")).thenReturn(time);
		@val
		aero.minova.core.application.system.domain.Row testResult = null;
		try {
			testResult = SqlUtils.convertSqlResultToRow(outputTable, sqlSet, NOP_LOGGER, this);
		} catch (ProcedureException e) {}
		assertThat(testResult.getValues()).hasSize(1);
		assertThat(testResult.getValues().get(0).getStringValue()).isEqualTo(time);
	}

	@DisplayName("Wähle alle Einträge mit einem bestimmten Wert eines Feldes.")
	@Test
	void testPrepareViewString_withSelectByAttributesWithoutConstraint() {
		Table inputTable = new Table();
		inputTable.setName("vWorkingTimeIndex2");
		inputTable.addColumn(new Column("EmployeeText", DataType.STRING));
		inputTable.addColumn(new Column("CustomerText", DataType.STRING));
		inputTable.addColumn(Column.AND_FIELD);
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("AVM", null));
			inputRow.addValue(null);
			inputRow.addValue(new Value(false, null));
			inputTable.addRow(inputRow);
		}
		Row inputRow = new Row();
		List<Row> userGroups = new ArrayList<>();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		assertThat(testSubject.prepareViewString(inputTable, true, 1000, userGroups))//
				.isEqualTo("select top 1000 EmployeeText, CustomerText from vWorkingTimeIndex2\r\nwhere ((EmployeeText like ?) )");
	}

	@Test
	void testAllDataTypes() throws SQLException {
		val outputTable = new Table();
		outputTable.setName("vWorkingTimeIndex2");
		outputTable.addColumn(new Column("INSTANT", DataType.INSTANT));
		outputTable.addColumn(new Column("BOOLEAN", DataType.BOOLEAN));
		outputTable.addColumn(new Column("DOUBLE", DataType.DOUBLE));
		outputTable.addColumn(new Column("INTEGER", DataType.INTEGER));
		outputTable.addColumn(new Column("LONG", DataType.LONG));
		outputTable.addColumn(new Column("STRING", DataType.STRING));
		outputTable.addColumn(new Column("ZONED", DataType.ZONED));
		val sqlSet = Mockito.mock(ResultSet.class);
		val time = Instant.ofEpochMilli(1598613904487L);
		when(sqlSet.getTimestamp("INSTANT")).thenReturn(Timestamp.from(time));
		when(sqlSet.getBoolean("BOOLEAN")).thenReturn(true);
		when(sqlSet.getDouble("DOUBLE")).thenReturn(3d);
		when(sqlSet.getInt("INTEGER")).thenReturn(5);
		when(sqlSet.getLong("LONG")).thenReturn(7L);
		when(sqlSet.getString("STRING")).thenReturn("string");
		when(sqlSet.getTimestamp("ZONED")).thenReturn(Timestamp.from(time));
		@val
		aero.minova.core.application.system.domain.Row testResult = null;
		try {
			testResult = SqlUtils.convertSqlResultToRow(outputTable, sqlSet, NOP_LOGGER, this);
		} catch (ProcedureException e) {}
		assertThat(testResult.getValues().get(0).getInstantValue()).isEqualTo(time);
		assertThat(testResult.getValues().get(1).getBooleanValue()).isEqualTo(true);
		assertThat(testResult.getValues().get(2).getDoubleValue()).isEqualTo(3d);
		assertThat(testResult.getValues().get(3).getIntegerValue()).isEqualTo(5);
		assertThat(testResult.getValues().get(4).getLongValue()).isEqualTo(7L);
		assertThat(testResult.getValues().get(5).getStringValue()).isEqualTo("string");
		assertThat(testResult.getValues().get(6).getZonedDateTimeValue()).isEqualTo(time.atZone(ZoneId.systemDefault()));
	}

	@Test
	void test_prepareWhereClause_AndColumnNotAtEnd() {
		val intputTable = new Table();
		intputTable.setName("vWorkingTimeIndex2");
		intputTable.addColumn(new Column("INSTANT", DataType.INSTANT));
		intputTable.addColumn(new Column("&", DataType.BOOLEAN));
		intputTable.addColumn(new Column("BOOLEAN", DataType.BOOLEAN));
		testSubject.prepareWhereClause(intputTable, true);
	}

	@Test
	void test_SearchViaString() {
		val intputTable = new Table();
		intputTable.setName("vWorkingTimeIndex2");
		intputTable.addColumn(new Column("KeyLong", DataType.INTEGER));
		val row = new Row();
		row.addValue(new Value("1", null));
		intputTable.getRows().add(row);
		assertThat(testSubject.prepareWhereClause(intputTable, true)).isEqualTo("\r\nwhere ((KeyLong like ?) )");
	}

	@Test
	void test_whereWithIn() {
		val intputTable = new Table();
		intputTable.setName("vWorkingTimeIndex2");
		intputTable.addColumn(new Column("KeyLong", DataType.INTEGER));
		val row = new Row();
		row.addValue(new Value("1,2,3", "in()"));
		intputTable.getRows().add(row);
		assertThat(testSubject.prepareWhereClause(intputTable, true)).isEqualTo("\r\nwhere ((KeyLong in(?, ?, ?)) )");
	}

	@Test
	void test_whereWithBetween() {
		val intputTable = new Table();
		intputTable.setName("vWorkingTimeIndex2");
		intputTable.addColumn(new Column("KeyLong", DataType.INTEGER));
		val row = new Row();
		row.addValue(new Value("1,2,3", "between()"));
		intputTable.getRows().add(row);
		assertThat(testSubject.prepareWhereClause(intputTable, true)).isEqualTo("\r\nwhere ((KeyLong between ? and ?) )");
	}

	@Test
	void test_whereNULL() {
		val intputTable = new Table();
		intputTable.setName("vWorkingTimeIndex2");
		intputTable.addColumn(new Column("KeyLong", DataType.INTEGER));
		intputTable.addColumn(new Column("KeyText", DataType.STRING));
		val row = new Row();
		row.addValue(new Value("", "is !null"));
		row.addValue(new Value("", "is null"));
		intputTable.getRows().add(row);
		assertThat(testSubject.prepareWhereClause(intputTable, true)).isEqualTo("\r\nwhere ((KeyLong is not null and KeyText is null) )");
	}

	@DisplayName("Zeige erste Seite.")
	@Test
	void testPaging() {
		Table inputTable = new Table();
		inputTable.setName("vWorkingTimeIndex2");
		inputTable.addColumn(new Column("EmployeeText", DataType.STRING));
		inputTable.addColumn(new Column("CustomerText", DataType.STRING));
		inputTable.addColumn(Column.AND_FIELD);
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("AVM", null));
			inputRow.addValue(new Value("MIN", null));
			inputRow.addValue(new Value(false, null));
			inputTable.addRow(inputRow);
		}
		Row inputRow = new Row();
		List<Row> userGroups = new ArrayList<>();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		assertThat(testSubject.pagingWithSeek(inputTable, true, 3, false, 1, userGroups))//
				.isEqualTo("select EmployeeText, CustomerText from ( select Row_Number() over (order by KeyLong) as RowNum, * from vWorkingTimeIndex2"
						+ "\r\nwhere ((EmployeeText like ? and CustomerText like ?) ) ) as RowConstraintResult" + "\r\nwhere RowNum > 0"
						+ "\r\nand RowNum <= 3 order by RowNum");
	}

	@DisplayName("Zeige alles auf erster Seite.")
	@Test
	void testPagingWithNoAttributes() {
		Table inputTable = new Table();

		inputTable.setName("vWorkingTimeIndex2");
		Row inputRow = new Row();
		List<Row> userGroups = new ArrayList<>();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		assertThat(testSubject.pagingWithSeek(inputTable, true, 3, false, 1, userGroups))//
				.isEqualTo("select * from ( select Row_Number() over (order by KeyLong) as RowNum, * from vWorkingTimeIndex2" + " ) as RowConstraintResult"
						+ "\r\nwhere RowNum > 0" + "\r\nand RowNum <= 3 order by RowNum");
	}

	@DisplayName("Zeige Einträge auf höherer Page.")
	@Test
	void testPagingOnHighPage() {
		Table inputTable = new Table();
		inputTable.setName("vWorkingTimeIndex2");
		inputTable.addColumn(new Column("EmployeeText", DataType.STRING));
		inputTable.addColumn(new Column("CustomerText", DataType.STRING));
		inputTable.addColumn(Column.AND_FIELD);
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("AVM", null));
			inputRow.addValue(new Value("MIN", null));
			inputRow.addValue(new Value(false, null));
			inputTable.addRow(inputRow);
		}
		Row inputRow = new Row();
		List<Row> userGroups = new ArrayList<>();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		assertThat(testSubject.pagingWithSeek(inputTable, true, 3, false, 5, userGroups))//
				.isEqualTo("select EmployeeText, CustomerText from ( select Row_Number() over (order by KeyLong) as RowNum, * from vWorkingTimeIndex2"
						+ "\r\nwhere ((EmployeeText like ? and CustomerText like ?) ) ) as RowConstraintResult" + "\r\nwhere RowNum > 12"
						+ "\r\nand RowNum <= 15 order by RowNum");
	}

	@DisplayName("Zeige alle Einträge (auch wenn Page eingestellt ist).")
	@Test
	void testPagingAllEntries() {
		Table inputTable = new Table();
		inputTable.setName("vWorkingTimeIndex2");
		inputTable.addColumn(new Column("EmployeeText", DataType.STRING));
		inputTable.addColumn(new Column("CustomerText", DataType.STRING));
		inputTable.addColumn(Column.AND_FIELD);
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("AVM", null));
			inputRow.addValue(new Value("MIN", null));
			inputRow.addValue(new Value(false, null));
			inputTable.addRow(inputRow);
		}
		Row inputRow = new Row();
		List<Row> userGroups = new ArrayList<>();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(false, null));
		userGroups.add(inputRow);
		assertThat(testSubject.pagingWithSeek(inputTable, true, 0, false, 5, userGroups))//
				.isEqualTo("select EmployeeText, CustomerText from ( select Row_Number() over (order by KeyLong) as RowNum, * from vWorkingTimeIndex2"
						+ "\r\nwhere ((EmployeeText like ? and CustomerText like ?) ) ) as RowConstraintResult" + "\r\nwhere RowNum > 0");
	}
}
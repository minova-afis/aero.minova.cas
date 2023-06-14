package aero.minova.cas.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.slf4j.helpers.NOPLogger.NOP_LOGGER;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.jooq.impl.DSL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.ProcedureException;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.BaseTest;
import aero.minova.cas.sql.SqlUtils;
import lombok.val;

//benötigt, damit JUnit-Tests nicht abbrechen
@SpringBootTest(classes = CoreApplicationSystemApplication.class, properties = { "application.runner.enabled=false", "spring.jooq.sql-dialect:POSTGRES" })
public class JOOQViewServiceTest extends BaseTest {

	@Autowired
	JOOQViewService testSubject;

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
				.isEqualTo("select * from vWorkingTimeIndex2 limit ?");
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
				.isEqualTo("select EmployeeText from vWorkingTimeIndex2 where EmployeeText like ? limit ?");
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
			inputRow.addValue(new Value(true, null));
			inputTable.addRow(inputRow);
		}
		Row inputRow = new Row();
		List<Row> userGroups = new ArrayList<>();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);
		assertThat(testSubject.prepareViewString(inputTable, true, 1000, userGroups))//
				.isEqualTo("select EmployeeText, CustomerText from vWorkingTimeIndex2 where (EmployeeText like ? and CustomerText like ?) limit ?");
	}

	@DisplayName("Wähle alle Einträge eines Datumsbereiches.")
	@Test
	void testPrepareViewString_withSelectByMultipleConditionsOnSameAttribute() {
		Table inputTable = new Table();
		inputTable.setName("vWorkingTimeIndex2");
		inputTable.addColumn(new Column("BookingDate", DataType.INSTANT));
		inputTable.addColumn(Column.AND_FIELD);
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value(Instant.parse("2020-07-31T00:00:00.00Z"), "<="));
			inputRow.addValue(new Value(false, null));
			inputTable.addRow(inputRow);
		}
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value(Instant.parse("2020-07-29T00:00:00.00Z"), ">"));
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
				.isEqualTo("select BookingDate from vWorkingTimeIndex2 where (BookingDate <= ? and BookingDate > ?) limit ?");
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
			inputRow.addValue(new Value(true, null));
			inputTable.addRow(inputRow);
		}
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("WIS", null));
			inputRow.addValue(new Value(true, null));
			inputTable.addRow(inputRow);
		}
		Row inputRow = new Row();
		List<Row> userGroups = new ArrayList<>();
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value("", null));
		inputRow.addValue(new Value(true, null));
		userGroups.add(inputRow);
		assertThat(testSubject.prepareViewString(inputTable, true, 1000, userGroups))//
				.isEqualTo("select EmployeeText from vWorkingTimeIndex2 where (EmployeeText like ? and EmployeeText like ?) limit ?");
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
		aero.minova.cas.api.domain.Row testResult = null;
		try {
			testResult = SqlUtils.convertSqlResultToRow(outputTable, sqlSet, NOP_LOGGER, this);
		} catch (ProcedureException e) {
			throw new RuntimeException(e);
		}
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
				.isEqualTo("select EmployeeText, CustomerText from vWorkingTimeIndex2 where EmployeeText like ? limit ?");
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
		aero.minova.cas.api.domain.Row testResult = null;
		try {
			testResult = SqlUtils.convertSqlResultToRow(outputTable, sqlSet, NOP_LOGGER, this);
		} catch (ProcedureException e) {
			throw new RuntimeException(e);
		}
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
		assertThat(testSubject.prepareWhereClause(intputTable, true)).isEqualTo(DSL.noCondition().toString());
	}

	@Test
	void test_SearchViaString() {
		val intputTable = new Table();
		intputTable.setName("vWorkingTimeIndex2");
		intputTable.addColumn(new Column("KeyLong", DataType.STRING));
		val row = new Row();
		row.addValue(new Value("1", ""));
		intputTable.getRows().add(row);
		assertThat(testSubject.prepareWhereClause(intputTable, true).toString()).isEqualTo("KeyLong like '1%'");
	}

	@Test
	void test_whereWithIn() {
		val intputTable = new Table();
		intputTable.setName("vWorkingTimeIndex2");
		intputTable.addColumn(new Column("KeyLong", DataType.INTEGER));
		val row = new Row();
		row.addValue(new Value("1,2,3", "in()"));
		intputTable.getRows().add(row);
		assertThat(testSubject.prepareWhereClause(intputTable, true).toString()).isEqualTo("KeyLong in (\n  '1', '2', '3'\n)");
	}

	@Test
	void test_whereWithBetween() {
		val intputTable = new Table();
		intputTable.setName("vWorkingTimeIndex2");
		intputTable.addColumn(new Column("KeyLong", DataType.INTEGER));
		val row = new Row();
		row.addValue(new Value("1,2,3", "between()"));
		intputTable.getRows().add(row);
		assertThat(testSubject.prepareWhereClause(intputTable, true).toString()).isEqualTo("KeyLong between '1' and '2'");
	}

	@Test
	void test_whereNULL() {
		val intputTable = new Table();
		intputTable.setName("vWorkingTimeIndex2");
		intputTable.addColumn(new Column("KeyLong", DataType.INTEGER));
		intputTable.addColumn(new Column("KeyText", DataType.STRING));
		intputTable.addColumn(Column.AND_FIELD);
		val row = new Row();
		row.addValue(new Value("", "is !null"));
		row.addValue(new Value("", "is null"));
		row.addValue(new Value(true, null));
		intputTable.getRows().add(row);
		assertThat(testSubject.prepareWhereClause(intputTable, true).toString().strip()).isEqualTo("(\n  KeyLong is not null\n  and KeyText is null\n)");
	}

	@Test
	void test_whereOr() {
		val intputTable = new Table();
		intputTable.setName("vWorkingTimeIndex2");
		intputTable.addColumn(new Column("KeyLong", DataType.INTEGER));
		intputTable.addColumn(new Column("KeyText", DataType.STRING));
		intputTable.addColumn(Column.AND_FIELD);
		val row = new Row();
		row.addValue(new Value("", "is !null"));
		row.addValue(new Value("", "is null"));
		row.addValue(new Value(false, null));
		intputTable.getRows().add(row);
		assertThat(testSubject.prepareWhereClause(intputTable, true).toString().strip()).isEqualTo("(\n  KeyLong is not null\n  or KeyText is null\n)");
	}
}

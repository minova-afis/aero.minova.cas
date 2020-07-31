package aero.minova.core.application.system.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;

@SpringBootTest
class SqlViewControllerTest {

	@Autowired
	SqlViewController testSubject;

	@DisplayName("Wähle Einträge ohne Einschränkungen aus.")
	@Test
	void testPrepareViewString_withStarSelect() {
		Table inputTable = new Table();
		inputTable.setName("vWorkingTimeIndex2");
		assertThat(testSubject.prepareViewString(inputTable, true, 1000))//
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
			inputRow.addValue(new Value("AVM"));
			inputRow.addValue(new Value(false));
			inputTable.addRow(inputRow);
		}
		assertThat(testSubject.prepareViewString(inputTable, true, 1000))//
				.isEqualTo("select top 1000 * from vWorkingTimeIndex2\r\nwhere (EmployeeText like 'AVM%')");
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
			inputRow.addValue(new Value("AVM"));
			inputRow.addValue(new Value("MIN"));
			inputRow.addValue(new Value(false));
			inputTable.addRow(inputRow);
		}
		assertThat(testSubject.prepareViewString(inputTable, true, 1000))//
				.isEqualTo("select top 1000 * from vWorkingTimeIndex2\r\nwhere (EmployeeText like 'AVM%' and CustomerText like 'MIN%')");
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
			inputRow.addValue(new Value("<=" + LocalDate.of(2020, 7, 31).toString()));
			inputRow.addValue(new Value(false));
			inputTable.addRow(inputRow);
		}
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value(">" + LocalDate.of(2020, 7, 29).toString()));
			inputRow.addValue(new Value(true));
			inputTable.addRow(inputRow);
		}
		assertThat(testSubject.prepareViewString(inputTable, true, 1000))//
				.isEqualTo("select top 1000 * from vWorkingTimeIndex2\r\n" //
						+ "where (BookingDate <= '2020-07-31')\r\n"//
						+ "  and (BookingDate > '2020-07-29')");
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
			inputRow.addValue(new Value("AVM"));
			inputRow.addValue(new Value(false));
			inputTable.addRow(inputRow);
		}
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("WIS"));
			inputRow.addValue(new Value(false));
			inputTable.addRow(inputRow);
		}
		assertThat(testSubject.prepareViewString(inputTable, true, 1000))//
				.isEqualTo("select top 1000 * from vWorkingTimeIndex2\r\n" //
						+ "where (EmployeeText like 'AVM%')\r\n"//
						+ "   or (EmployeeText like 'WIS%')");
	}
}
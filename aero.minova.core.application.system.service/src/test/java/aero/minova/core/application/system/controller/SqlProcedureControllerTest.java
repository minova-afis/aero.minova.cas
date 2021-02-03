package aero.minova.core.application.system.controller;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.OutputType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.SqlProcedureResult;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;
import lombok.val;

@SpringBootTest
public class SqlProcedureControllerTest {
	@Autowired
	SqlProcedureController testSubject;

	@Test
	public void test_prepareProcedureString() {
		val testParameter = new Table();
		testParameter.setName("testProcedure");
		testParameter.setColumns(//
				asList(//
						new Column("A", DataType.INTEGER)//
						, new Column("B", DataType.INTEGER, OutputType.INPUT)//
						, new Column("B", DataType.INTEGER, OutputType.OUTPUT)));
		val testProduct = testSubject.prepareProcedureString(testParameter);
		assertThat(testProduct).isEqualTo("{call testProcedure(?,?,?)}");
	}

	@Test
	public void test_prepareProcedureParsingErrorHandling() {
		val testParameter = new Table();
		testParameter.setName("spInsertWorkingTime");
		testParameter.setColumns(//
				asList(//
						new Column("KeyLong", DataType.INTEGER)//
						, new Column("EmployeeKey", DataType.INTEGER)//
						, new Column("ServiceContractKey", DataType.INTEGER)//
						, new Column("OrderReceiverrKey", DataType.INTEGER)//
						, new Column("ServiceObjectKey", DataType.INTEGER)//
						, new Column("ServiceKey", DataType.INTEGER)//
						, new Column("BookingDate", DataType.INSTANT)//
						, new Column("StartDate", DataType.INSTANT)//
						, new Column("EndDate", DataType.INSTANT)//
						, new Column("RenderedQuantity", DataType.DOUBLE)//
						, new Column("ChargedQuantity", DataType.INTEGER)//
						, new Column("Description", DataType.STRING)//
						, new Column("Spelling", DataType.BOOLEAN)));
		{
			Row inputRow = new Row();
			inputRow.addValue(null);
			inputRow.addValue(new Value(1));
			inputRow.addValue(new Value(1));
			inputRow.addValue(new Value(1));
			inputRow.addValue(new Value(1));
			inputRow.addValue(new Value(2));
			inputRow.addValue(new Value("2021-10-06T00:00:00.00Z"));
			inputRow.addValue(new Value("2021-10-06T00:00:00.00Z"));
			inputRow.addValue(new Value("2021-10-06T00:00:00.00Z"));
			inputRow.addValue(new Value(1.0));
			inputRow.addValue(new Value(5));
			inputRow.addValue(new Value("Test"));
			inputRow.addValue(new Value(false));
			testParameter.addRow(inputRow);
		}
		SqlProcedureResult result = testSubject.executeProcedure(testParameter);
		assertThat(result).isNotEqualTo(null);
		assertThat(result.getReturnErrorMessage().getCause()).isEqualTo("java.lang.RuntimeException: Could not parse input parameter with index:6");
	}

}
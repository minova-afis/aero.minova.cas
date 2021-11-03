package aero.minova.core.application.system.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.SqlProcedureResult;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;
import aero.minova.core.application.system.domain.XSqlProcedureResult;
import aero.minova.core.application.system.domain.XTable;

//benötigt, damit JUnit-Tests nicht abbrechen
@SpringBootTest(properties = { "application.runner.enabled=false" })
public class XSqlProcedureControllerTest extends BaseTest {
	@Autowired
	XSqlProcedureController testSubject;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testFillDependency() {
		List<XSqlProcedureResult> results = new ArrayList<>();

		Table inputTable = new Table();
		inputTable.setName("spTest");
		inputTable.addColumn(new Column("TestText", DataType.STRING));
		{
			Row inputRow = new Row();
			inputRow.addValue(null);
			inputTable.addRow(inputRow);
		}
		SqlProcedureResult sqlres = new SqlProcedureResult();
		sqlres.setOutputParameters(inputTable);
		XSqlProcedureResult xRes = new XSqlProcedureResult("test", sqlres);
		results.add(xRes);

		sqlres.setOutputParameters(inputTable);
		xRes = new XSqlProcedureResult("test3", sqlres);
		results.add(xRes);

		// In test2 steht der Value, den wir dann haben wollen.
		Table testTable = new Table();
		testTable.setName("xpTest");
		testTable.addColumn(new Column("TestText", DataType.STRING));
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("TestString", null));
			testTable.addRow(inputRow);
		}
		sqlres.setOutputParameters(testTable);
		xRes = new XSqlProcedureResult("test2", sqlres);
		results.add(xRes);

		// XTable zum Aufrufen der Methode
		Row inputRow = new Row();
		inputRow.addValue(new Value("TestText", "test2"));
		List<Row> rows = new ArrayList<>();
		rows.add(inputRow);
		inputTable.setRows(rows);

		XTable xtable = new XTable();
		xtable.setId("TestTable");
		xtable.setTable(inputTable);

		Table resTable = testSubject.fillInDependencies(xtable, results);

		assertThat(resTable.getRows().get(0).getValues().get(0).getStringValue()).isEqualTo("TestString");
	}

	@Test
	public void testFindxSqlResultSetValid() {
		List<XSqlProcedureResult> results = new ArrayList<>();

		Table inputTable = new Table();
		inputTable.setName("spTest");
		inputTable.addColumn(new Column("TestText", DataType.STRING));
		{
			Row inputRow = new Row();
			inputRow.addValue(null);
			inputTable.addRow(inputRow);
		}
		SqlProcedureResult sqlres = new SqlProcedureResult();
		sqlres.setOutputParameters(inputTable);
		XSqlProcedureResult xRes = new XSqlProcedureResult("test", sqlres);
		results.add(xRes);
		xRes = new XSqlProcedureResult("test2", sqlres);
		results.add(xRes);
		xRes = new XSqlProcedureResult("test3", sqlres);
		results.add(xRes);
		XSqlProcedureResult testres = testSubject.findxSqlResultSet("test2", results);

		assertThat(testres.getId()).isEqualTo("test2");
	}

	@Test
	public void testFindxSqlResultSetInvalid() {
		List<XSqlProcedureResult> results = new ArrayList<>();

		Table inputTable = new Table();
		inputTable.setName("spTest");
		inputTable.addColumn(new Column("TestText", DataType.STRING));
		{
			Row inputRow = new Row();
			inputRow.addValue(null);
			inputTable.addRow(inputRow);
		}
		SqlProcedureResult sqlres = new SqlProcedureResult();
		sqlres.setOutputParameters(inputTable);
		XSqlProcedureResult xRes = new XSqlProcedureResult("test", sqlres);
		results.add(xRes);
		xRes = new XSqlProcedureResult("test2", sqlres);
		results.add(xRes);
		xRes = new XSqlProcedureResult("test3", sqlres);
		results.add(xRes);

		Throwable exception = assertThrows(RuntimeException.class, () -> testSubject.findxSqlResultSet("test100", results));
		thrown.expect(RuntimeException.class);
		assertEquals("Cannot find SqlProcedureResult with Id test100", exception.getMessage());
	}

	@Test
	public void testFindValueValid() {
		Table inputTable = new Table();
		inputTable.setName("spTest");
		inputTable.addColumn(new Column("TestText", DataType.STRING));
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("Test", null));
			inputTable.addRow(inputRow);
		}
		SqlProcedureResult sqlres = new SqlProcedureResult();
		sqlres.setOutputParameters(inputTable);

		Value resval = testSubject.findValue(sqlres, "TestText");
		assertThat(resval.getStringValue()).isEqualTo("Test");
	}

	@Test
	public void testFindValueInvalid() {
		Table inputTable = new Table();
		inputTable.setName("spTest");
		inputTable.addColumn(new Column("Test", DataType.STRING));
		{
			Row inputRow = new Row();
			inputRow.addValue(new Value("Test", null));
			inputTable.addRow(inputRow);
		}
		SqlProcedureResult sqlres = new SqlProcedureResult();
		sqlres.setOutputParameters(inputTable);

		Throwable exception = assertThrows(RuntimeException.class, () -> testSubject.findValue(sqlres, "TestText"));
		thrown.expect(RuntimeException.class);
		assertEquals("Cannot find Column with name TestText", exception.getMessage());
	}
}

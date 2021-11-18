package aero.minova.core.application.system.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

	@Autowired
	Gson gson;

	@Test
	public void testFillInDependencies() {

		Type xSqlProcedureResultType = new TypeToken<ArrayList<XSqlProcedureResult>>() {}.getType();
		final List<XSqlProcedureResult> xSqlProcedureResults = gson.fromJson(new Scanner(getClass()//
				.getClassLoader()//
				.getResourceAsStream("xprocedureExample.json"), "UTF-8")//
						.useDelimiter("\\A")//
						.next()//
				, xSqlProcedureResultType);

		// XTable zum Aufrufen der Methode
		Table inputTable = new Table();
		Row inputRow = new Row();
		inputRow.addValue(new Value("TestText", "test2"));
		List<Row> rows = new ArrayList<>();
		rows.add(inputRow);
		inputTable.setRows(rows);

		XTable xtable = new XTable();
		xtable.setId("TestTable");
		xtable.setTable(inputTable);

		Table resTable = testSubject.fillInDependencies(xtable, xSqlProcedureResults);

		assertThat(resTable.getRows().get(0).getValues().get(0).getStringValue()).isEqualTo("TestString");
	}

	@Test
	public void testFindxsqlResultSetValid() {
		List<XSqlProcedureResult> results = new ArrayList<>();

		Table inputTable = new Table();
		inputTable.setName("spTest");
		inputTable.addColumn(new Column("TestText", DataType.STRING));
		{
			Row inputRow = new Row();
			inputRow.addValue(null);
			inputTable.addRow(inputRow);
		}
		SqlProcedureResult sqlRes = new SqlProcedureResult();
		sqlRes.setOutputParameters(inputTable);
		XSqlProcedureResult xRes = new XSqlProcedureResult("test", sqlRes);
		results.add(xRes);
		xRes = new XSqlProcedureResult("test2", sqlRes);
		results.add(xRes);
		xRes = new XSqlProcedureResult("test3", sqlRes);
		results.add(xRes);
		XSqlProcedureResult testres = testSubject.findxSqlResultSet("test2", results);

		assertThat(testres.getId()).isEqualTo("test2");
	}

	@Test
	public void testFindxsqlResultSetInvalid() {
		List<XSqlProcedureResult> results = new ArrayList<>();

		Table inputTable = new Table();
		inputTable.setName("spTest");
		inputTable.addColumn(new Column("TestText", DataType.STRING));
		{
			Row inputRow = new Row();
			inputRow.addValue(null);
			inputTable.addRow(inputRow);
		}
		SqlProcedureResult sqlRes = new SqlProcedureResult();
		sqlRes.setOutputParameters(inputTable);
		XSqlProcedureResult xRes = new XSqlProcedureResult("test", sqlRes);
		results.add(xRes);
		xRes = new XSqlProcedureResult("test2", sqlRes);
		results.add(xRes);
		xRes = new XSqlProcedureResult("test3", sqlRes);
		results.add(xRes);

		assertThat(catchThrowable(() -> testSubject.findxSqlResultSet("test100", results))).isInstanceOf(RuntimeException.class)
				.hasMessage("Cannot find SqlProcedureResult with Id test100");
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
		SqlProcedureResult sqlRes = new SqlProcedureResult();
		sqlRes.setOutputParameters(inputTable);

		Value resval = testSubject.findValueInColumn(sqlRes, "TestText", 0);
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
		SqlProcedureResult sqlRes = new SqlProcedureResult();
		sqlRes.setOutputParameters(inputTable);

		assertThat(catchThrowable(() -> testSubject.findValueInColumn(sqlRes, "TestText", 0))).isInstanceOf(RuntimeException.class)
				.hasMessage("Cannot find Column with name TestText");
	}
}

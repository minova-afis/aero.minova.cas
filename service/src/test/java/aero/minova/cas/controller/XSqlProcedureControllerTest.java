package aero.minova.cas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import aero.minova.cas.BaseTest;
import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.api.domain.XSqlProcedureResult;
import aero.minova.cas.api.domain.XTable;
import aero.minova.cas.sql.SystemDatabase;

//benötigt, damit JUnit-Tests nicht abbrechen
@SpringBootTest(classes = CoreApplicationSystemApplication.class, properties = { "application.runner.enabled=false" })
class XSqlProcedureControllerTest extends BaseTest {
	@Autowired
	XSqlProcedureController testSubject;

	@Autowired
	Gson gson;

	@Autowired
	SystemDatabase systemDatabase;

	@Test
	void checkAutoCommitDisabled() throws SQLException {
		// Autocommit muss per Default ausgeschaltet sein
		assertTrue(!systemDatabase.getConnection().getAutoCommit());
	}

	@Test
	void testFillInDependencies() {

		Type xSqlProcedureResultType = new TypeToken<ArrayList<XSqlProcedureResult>>() {}.getType();
		final List<XSqlProcedureResult> xSqlProcedureResults = gson.fromJson(new Scanner(getClass()//
				.getClassLoader()//
				.getResourceAsStream("jsons/xprocedureExample.json"), "UTF-8")//
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
	void testFindxsqlResultSetValid() {
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
	void testFindxsqlResultSetInvalid() {
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
	void testFindxsqlResultSetByNameValid() {
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

		Table secondTable = new Table();
		secondTable.setName("Test Test");
		secondTable.addColumn(new Column("TestText", DataType.STRING));
		{
			Row inputRow = new Row();
			inputRow.addValue(null);
			secondTable.addRow(inputRow);
		}

		SqlProcedureResult sqlRes2 = new SqlProcedureResult();
		sqlRes2.setOutputParameters(secondTable);
		xRes = new XSqlProcedureResult("test2", sqlRes2);
		results.add(xRes);
		xRes = new XSqlProcedureResult("test3", sqlRes2);
		results.add(xRes);
		List<XSqlProcedureResult> testres = testSubject.findxSqlResultSetByName("Test Test", results);

		assertThat(testres).hasSize(2);
	}

	@Test
	void testFindValueValid() {
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

		Value resval = testSubject.findValueInColumn(sqlRes, "TestText", 0).orElse(null);
		assertThat(resval.getStringValue()).isEqualTo("Test");
	}
}

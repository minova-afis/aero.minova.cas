package aero.minova.cas.service;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import aero.minova.cas.CoreApplicationSystemApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import aero.minova.cas.api.domain.Column;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.OutputType;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.controller.BaseTest;
import lombok.val;

//benötigt, damit JUnit-Tests nicht abbrechen
@SpringBootTest(classes = CoreApplicationSystemApplication.class, properties = { "application.runner.enabled=false" })
class ProcedureServiceTest extends BaseTest {
	@Autowired
	ProcedureService testSubject;

	@Test
	void test_prepareProcedureString() {
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
}
package aero.minova.cas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import aero.minova.cas.CoreApplicationSystemApplication;

//benötigt, damit JUnit-Tests nicht abbrechen
@SpringBootTest(classes = CoreApplicationSystemApplication.class, properties = { "application.runner.enabled=false" })
class SQLProcedureControllerTest extends BaseTest {

	@Autowired
	SqlProcedureController spc;

	@DisplayName("Keine doppelten Extensionnamen erlauben.")
	@Test
	void testDoubleExtensionWithSameName() throws Exception {

		spc.registerExtension("test", null);

		Throwable exception = assertThrows(IllegalArgumentException.class, () -> spc.registerExtension("test", null));
		assertThat(exception).hasMessage("Cannot register two extensions with the same name: test");

	}
}

package aero.minova.cas.extension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.api.domain.DataType;
import aero.minova.cas.api.domain.Row;
import aero.minova.cas.api.domain.SqlProcedureResult;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.domain.Value;
import aero.minova.cas.controller.BaseTest;
import aero.minova.cas.controller.SqlProcedureController;
import aero.minova.cas.service.SecurityService;

@SpringBootTest(classes = CoreApplicationSystemApplication.class, properties = { "application.runner.enabled=false", "spring.jooq.sql-dialect:POSTGRES" })

public class PasswordEncoderTest extends BaseTest {

	@Autowired
	SqlProcedureController spc;

	@Autowired
	PasswordEncoder passwordEncoder;

	@BeforeEach
	public void mockSecurityService() throws Exception {

		List<Row> permissions = new ArrayList<>();

		Row inputRow = new Row();
		inputRow.addValue(new Value("admin", null));
		inputRow.addValue(new Value("xpcasEncodePassword", null));
		inputRow.addValue(new Value(false, null));
		permissions.add(inputRow);

		SecurityService security = Mockito.mock(SecurityService.class);
		doReturn(true).when(security).arePrivilegeStoresSetup();
		doReturn(permissions).when(security).getPrivilegePermissions(Mockito.any());

		spc.securityService = security;

	}

	@DisplayName("Teste erfolgreiche Verschlüsselung.")
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testEncodePassword() throws Exception {
		Table test = readTableFromExampleJson("jsons/PasswordTest");

		SqlProcedureResult result = (SqlProcedureResult) spc.executeProcedure(test).getBody();

		assertTrue(passwordEncoder.matches("TEST", result.getResultSet().getRows().get(0).getValues().get(1).getStringValue()));

	}

	@DisplayName("Password-Spalte fehlt.")
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testEncodePasswordMissingColumnPassword() throws Exception {
		Table test = readTableFromExampleJson("jsons/PasswordTest2");

		Throwable exception = assertThrows(aero.minova.cas.api.domain.ProcedureException.class, () -> spc.executeProcedure(test));
		assertThat(exception).hasMessage("java.lang.IllegalArgumentException: Column `Password` is missing.");

	}

	@DisplayName("Encoded-Password-Spalte fehlt.")
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testEncodePasswordMissingColumnEncodedPassword() throws Exception {
		Table test = readTableFromExampleJson("jsons/PasswordTest3");

		Throwable exception = assertThrows(aero.minova.cas.api.domain.ProcedureException.class, () -> spc.executeProcedure(test));
		assertThat(exception).hasMessage("java.lang.IllegalArgumentException: Column `EncodedPassword` is missing.");

	}

	@DisplayName("Inhalt/Value der Password-Spalte fehlt.")
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testEncodePasswordMissingPassword() throws Exception {
		Table test = readTableFromExampleJson("jsons/PasswordTest4");

		Throwable exception = assertThrows(aero.minova.cas.api.domain.ProcedureException.class, () -> spc.executeProcedure(test));
		assertThat(exception).hasMessage("java.lang.IllegalArgumentException: Column `Password` has no value.");

	}

	@DisplayName("Falsche Spaltentypen.")
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void testEncodePasswordWrongColumnTypes() throws Exception {
		Table test = readTableFromExampleJson("jsons/PasswordTest");

		test.getColumns().get(0).setType(DataType.BOOLEAN);
		test.getColumns().get(1).setType(DataType.BOOLEAN);

		Throwable exception = assertThrows(aero.minova.cas.api.domain.ProcedureException.class, () -> spc.executeProcedure(test));
		assertThat(exception).hasMessage("java.lang.IllegalArgumentException: Column `Password` has to be of type String but is `BOOLEAN`.");

		test.getColumns().get(0).setType(DataType.STRING);

		Throwable exception2 = assertThrows(aero.minova.cas.api.domain.ProcedureException.class, () -> spc.executeProcedure(test));
		assertThat(exception2).hasMessage("java.lang.IllegalArgumentException: Column `EncodedPassword` has to be of type String but is `BOOLEAN`.");

	}

}

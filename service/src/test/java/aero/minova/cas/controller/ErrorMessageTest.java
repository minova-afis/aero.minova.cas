package aero.minova.cas.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.google.gson.Gson;

import aero.minova.cas.ControllerExceptionHandler;
import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.api.restapi.ClientRestAPI;

@SpringBootTest(classes = CoreApplicationSystemApplication.class)
@ActiveProfiles("test")
class ErrorMessageTest extends BaseTest {

	Gson gson;

	@Autowired
	ClientRestAPI clientRestAPI;

	@Autowired
	ControllerExceptionHandler testSubject;

	@Spy
	ControllerExceptionHandler spySubject;

	@Mock
	ControllerExceptionHandler mockSubject;

	@SuppressWarnings("deprecation")
	@BeforeEach
	public void setUp() {
		gson = clientRestAPI.getGson();

		MockitoAnnotations.initMocks(this);
		spySubject = spy(testSubject);
		mockSubject = mock(ControllerExceptionHandler.class);

		doCallRealMethod().when(mockSubject).prepareExceptionReturnTable(any(Exception.class));
		doCallRealMethod().when(mockSubject).handleSqlErrorMessage(any(Table.class), any(String.class));
	}

	@Test
	void testSqlErrorMessage1() {
		Exception e = new Exception("ADO | 25 | msg.sql.51103 @p tUnit.Description.16 @s kg | Beipieltext");

		Table exceptionTable = mockSubject.prepareExceptionReturnTable(e);

		Table sqlError1 = readTableFromExampleJson("jsons/SqlError1");

		assertThat(sqlError1.getColumns()).hasSameSizeAs(exceptionTable.getColumns());
		assertThat(sqlError1.getRows()).hasSameSizeAs(exceptionTable.getRows());
		assertThat(sqlError1.getRows().get(0).getValues().get(0).getStringValue().trim())
				.isEqualTo(exceptionTable.getRows().get(0).getValues().get(0).getStringValue().trim());
		assertThat(sqlError1.getRows().get(0).getValues().get(1).getStringValue().trim())
				.isEqualTo(exceptionTable.getRows().get(0).getValues().get(1).getStringValue().trim());
		assertThat(sqlError1.getRows().get(0).getValues().get(2).getStringValue().trim())
				.isEqualTo(exceptionTable.getRows().get(0).getValues().get(2).getStringValue());
		assertThat(sqlError1.getRows().get(0).getValues().get(3).getStringValue().trim())
				.isEqualTo(exceptionTable.getRows().get(0).getValues().get(3).getStringValue().trim());
	}

	@Test
	void testSqlErrorMessage2() {
		Exception e = new Exception("ADO | 30 | delaycode.description.comma | Commas are not allowed in the description.");

		Table exceptionTable = mockSubject.prepareExceptionReturnTable(e);

		Table sqlError1 = readTableFromExampleJson("jsons/SqlError2");

		assertThat(sqlError1.getColumns()).hasSameSizeAs(exceptionTable.getColumns());
		assertThat(sqlError1.getRows()).hasSameSizeAs(exceptionTable.getRows());
		assertThat(sqlError1.getRows().get(0).getValues().get(0).getStringValue().trim())
				.isEqualTo(exceptionTable.getRows().get(0).getValues().get(0).getStringValue().trim());
		assertThat(sqlError1.getRows().get(0).getValues().get(1).getStringValue().trim())
				.isEqualTo(exceptionTable.getRows().get(0).getValues().get(1).getStringValue().trim());
	}

	@Test
	void testSqlErrorMessage3() {
		Exception e = new Exception("ADO | 25 | msg.sql.51103");

		Table exceptionTable = mockSubject.prepareExceptionReturnTable(e);

		Table sqlError1 = readTableFromExampleJson("jsons/SqlError3");

		assertThat(sqlError1.getColumns()).hasSameSizeAs(exceptionTable.getColumns());
		assertThat(sqlError1.getRows()).hasSameSizeAs(exceptionTable.getRows());
		assertThat(sqlError1.getRows().get(0).getValues().get(0).getStringValue().trim())
				.isEqualTo(exceptionTable.getRows().get(0).getValues().get(0).getStringValue().trim());
		assertThat(sqlError1.getRows().get(0).getValues().get(1).getStringValue().trim())
				.isEqualTo(exceptionTable.getRows().get(0).getValues().get(1).getStringValue().trim());
	}

	@Test
	void testProgramMessage() {
		Exception e = new Exception("msg.PrivilegeError");

		Table exceptionTable = mockSubject.prepareExceptionReturnTable(e);

		Table sqlError1 = readTableFromExampleJson("jsons/ProgramError");

		assertThat(sqlError1.getColumns()).hasSameSizeAs(exceptionTable.getColumns());
		assertThat(sqlError1.getRows()).hasSameSizeAs(exceptionTable.getRows());
		assertThat(sqlError1.getRows().get(0).getValues().get(0).getStringValue().trim())
				.isEqualTo(exceptionTable.getRows().get(0).getValues().get(0).getStringValue().trim());
	}
}

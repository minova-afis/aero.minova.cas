package aero.minova.cas.controller;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.ProcedureException;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.service.SecurityService;
import cas.openapi.api.server.model.PingEntry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommunicationControllerTest {
	private static final MockHttpServletRequest REQUEST = new MockHttpServletRequest();

	@Mock
	private SqlProcedureController sqlProcedureControllerMock;
	@Mock
	private SecurityService securityServiceMock;
	@Mock
	private CustomLogger customLoggerMock;

	@InjectMocks
	private CommunicationController communicationController;

	@BeforeAll
	static void init() {
		REQUEST.setScheme("https");
		REQUEST.setServerPort(8443);
		REQUEST.setRequestURI("/cas");

		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(REQUEST));
	}

	@Test
	void pingTest() {
		assertThat(communicationController.ping())
				.isNotNull()
				.hasFieldOrPropertyWithValue("statusCode", HttpStatus.OK)
				.hasFieldOrPropertyWithValue("body", new PingEntry().returnCode(0));
	}

	@Test
	void loadPrivilegesTest() {
		doNothing().when(securityServiceMock).loadAllPrivileges();

		assertThat(communicationController.loadPrivileges())
				.isNotNull()
				.hasFieldOrPropertyWithValue("statusCode", HttpStatus.NO_CONTENT)
				.hasFieldOrPropertyWithValue("body", null);

		verify(securityServiceMock).loadAllPrivileges();
	}

	@java.lang.SuppressWarnings("java:S5778")
	@Test
	void loadPrivilegesFailedThrowsException() {
		Exception mockedException = new IllegalArgumentException("TEST: loadPrivileges failed");

		doNothing().when(customLoggerMock).logError(endsWith(mockedException.getMessage()), ArgumentMatchers.eq(mockedException));
		doThrow(new RuntimeException("TEST: loadPrivileges() failed")).when(securityServiceMock).loadAllPrivileges();

		assertThatThrownBy(() -> communicationController.loadPrivileges())
				.isInstanceOf(RuntimeException.class);

		verify(customLoggerMock).logError(anyString(), any(Exception.class));
		verify(securityServiceMock).loadAllPrivileges();
	}

	@Test
	void setupSuccessTest() throws Exception {
		Mockito.when(sqlProcedureControllerMock.executeProcedure(any(Table.class)))
				.thenReturn(ResponseEntity.noContent().build());

		assertSetupResponse(communicationController.setup(), "setupSuccess");

		verify(sqlProcedureControllerMock).executeProcedure(any(Table.class));
	}

	@Test
	void setupErrorTest() throws Exception {
		doThrow(new ProcedureException("TEST: setup() failed"))
				.when(sqlProcedureControllerMock).executeProcedure(any(Table.class));

		assertSetupResponse(communicationController.setup(), "setupError");

		verify(sqlProcedureControllerMock).executeProcedure(any(Table.class));
	}

	private void assertSetupResponse(ResponseEntity<Void> response, String locationSuffix) {
		assertThat(response)
				.isNotNull()
				.hasFieldOrPropertyWithValue("statusCode", HttpStatus.FOUND)
				.hasFieldOrPropertyWithValue("body", null)
				.extracting(ResponseEntity::getHeaders)
				.isNotNull()
				.extracting("Location")
				.asString()
				.isEqualTo("https://localhost:8443/" + locationSuffix);
	}
}

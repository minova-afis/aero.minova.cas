package aero.minova.cas.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import aero.minova.cas.CoreApplicationSystemApplication;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootTest(classes = CoreApplicationSystemApplication.class, properties = { "application.runner.enabled=false" })
@ActiveProfiles("test")
public class CommunicationControllerTest extends BaseTest {

	@Autowired
	SqlProcedureController sqlController;

	@Autowired
	CommunicationController communicationController;

	@Test
	public void testSetupSuccess() throws Exception {
		sqlController = spy(sqlController);
		communicationController.spc = sqlController;
		ResponseEntity res = new ResponseEntity(HttpStatus.ACCEPTED);
		doReturn(res).when(sqlController).executeProcedure(Mockito.any());

		HttpServletResponse httpServlet = new MockHttpServletResponse();
		communicationController.setup(httpServlet);
		assertEquals(302, httpServlet.getStatus());
		assertEquals("/setupSuccess", httpServlet.getHeader("Location"));
	}

	@Test
	public void testSetupError() throws Exception {
		HttpServletResponse httpServlet = new MockHttpServletResponse();
		// Da in den Tests die Hibernate Datenbank verwendet wird, wird im sqlController ein Fehler geworfen. Das soll hier aber genauso sein, damit wir den
		// Fehlerfall testen können.
		communicationController.setup(httpServlet);
		assertEquals(302, httpServlet.getStatus());
		assertEquals("/setupError", httpServlet.getHeader("Location"));
	}
}

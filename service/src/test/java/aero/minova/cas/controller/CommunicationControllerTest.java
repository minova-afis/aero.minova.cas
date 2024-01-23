package aero.minova.cas.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.service.model.Authorities;
import aero.minova.cas.service.model.UserGroup;
import aero.minova.cas.service.repository.AuthoritiesRepository;
import aero.minova.cas.service.repository.UserGroupRepository;
import aero.minova.cas.service.repository.UsersRepository;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootTest(classes = CoreApplicationSystemApplication.class, properties = { "application.runner.enabled=false", "login_dataSource=database" })
@ActiveProfiles("test")
@WithMockUser(username = "admin", password = "rqgzxTf71EAx8chvchMi", authorities = { "admin" }) // Wichtig ist "authorities" statt "roles" zu nutzen, da die
// Authority ansonsten "ROLE_admin" heißt

public class CommunicationControllerTest extends BaseTest {

	@Autowired
	SqlProcedureController sqlController;

	@Autowired
	CommunicationController communicationController;

	@Autowired
	AuthoritiesRepository authoritiesRepository;

	@Autowired
	UserGroupRepository userGroupRepository;

	@Autowired
	UsersRepository usersRepository;

	@Test
	void testSetupSuccess() throws Exception {
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
	void testSetupError() throws Exception {
		HttpServletResponse httpServlet = new MockHttpServletResponse();
		// Da in den Tests die Hibernate Datenbank verwendet wird, wird im sqlController ein Fehler geworfen. Das soll hier aber genauso sein, damit wir den
		// Fehlerfall testen können.
		communicationController.setup(httpServlet);
		assertEquals(302, httpServlet.getStatus());
		assertEquals("/setupError", httpServlet.getHeader("Location"));
	}

	@Test
	void testLoadPrivileges() throws Exception {
		List<GrantedAuthority> oldAuthorities = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

		assertEquals(1, oldAuthorities.size());
		assertEquals("admin", oldAuthorities.get(0).getAuthority());

		UserGroup group = new UserGroup();
		group.setKeyText("test");
		group.setSecurityToken("#test");
		userGroupRepository.save(group);

		Authorities authority = new Authorities();
		authority.setAuthority("test");
		authority.setUsername(oldAuthorities.get(0).getAuthority());
		authoritiesRepository.save(authority);

		communicationController.loadPrivileges();
		List<GrantedAuthority> newAuthorities = (List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

		assertEquals(2, newAuthorities.size());
		assertEquals("test", newAuthorities.get(0).getAuthority());
		assertEquals("admin", newAuthorities.get(1).getAuthority());
	}
}

package aero.minova.cas.controller;

import java.util.Collection;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.data.ldap.AutoConfigureDataLdap;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;

import aero.minova.cas.SecurityConfig;

@SpringBootTest
@Import(SecurityConfig.class)
@AutoConfigureDataLdap
public class AuthenticationTest extends BaseTest {

	@Autowired
	@Qualifier("ldapUser")
	private LdapUserDetailsMapper userDetailsContextMapper;

	@Autowired
	SqlViewController testSubject;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	class MockAuthenticator implements LdapAuthenticator {

		@Override
		public DirContextOperations authenticate(Authentication authentication) {
			DirContextAdapter ctx = new DirContextAdapter();
			ctx.setAttributeValue("ou", "FROM_ENTRY");
			String username = authentication.getName();
			String password = (String) authentication.getCredentials();
			if (username.equals("admin") && password.equals("rqgzxTf71EAx8chvchMi")) {
				ctx.setDn(new DistinguishedName("cn=admin,ou=people,dc=springframework,dc=org"));
				ctx.setAttributeValue("userPassword", "{SHA}rqgzxTf71EAx8chvchMi");
				return ctx;
			} else if (username.equals("user") && password.equals("password")) {
				ctx.setDn(new DistinguishedName("cn=user,ou=people,dc=springframework,dc=org"));
				return ctx;
			} else if (username.equals("test") && password.equals("password")) {
				ctx.setDn(new DistinguishedName("cn=user,ou=people,dc=springframework,dc=org"));
				return ctx;
			} else if (username.equals("codemonkey") && password.equals("password")) {
				ctx.setDn(new DistinguishedName("cn=user,ou=people,dc=springframework,dc=org"));
				return ctx;
			}
			throw new BadCredentialsException("Authentication failed.");
		}

	}

	class MockAuthoritiesPopulator implements LdapAuthoritiesPopulator {

		String username;

		@Override
		public Collection<GrantedAuthority> getGrantedAuthorities(DirContextOperations userCtx, String username) {
			this.username = username;
			return AuthorityUtils.createAuthorityList("ROLE_user", "ROLE_test");

		}

		String getRequestedUsername() {
			return this.username;
		}

	}

//	@DisplayName("Überprüfen, ob Rollen richtig übernommen werden")
//	@Test
//	public void testUserDetailsMapper() {
//		final LdapAuthenticationProvider ldapProvider = new LdapAuthenticationProvider(new MockAuthenticator());
//		ldapProvider.setUserDetailsContextMapper(userDetailsContextMapper);
//		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("admin", "rqgzxTf71EAx8chvchMi");
//		Object authDetails = new Object();
//		authRequest.setDetails(authDetails);
//		Authentication authResult = ldapProvider.authenticate(authRequest);
//		UserDetails user = (UserDetails) authResult.getPrincipal();
//		assertThat(user.getAuthorities()).hasSize(4);
//	}
//
//	@DisplayName("Überprüfen, ob Rollen richtig übernommen werden in den Methoden")
//	@Test
//	public void testUserDetailsMapperWithMethod() {
//		final LdapAuthenticationProvider ldapProvider = new LdapAuthenticationProvider(new MockAuthenticator());
//		ldapProvider.setUserDetailsContextMapper(userDetailsContextMapper);
//		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("admin", "rqgzxTf71EAx8chvchMi");
//		Object authDetails = new Object();
//		authRequest.setDetails(authDetails);
//		Authentication authResult = ldapProvider.authenticate(authRequest);
//		UserDetails user = (UserDetails) authResult.getPrincipal();
//		assertThat(user.getAuthorities()).hasSize(4);
//
//		SecurityContext sc = SecurityContextHolder.getContext();
//		sc.setAuthentication(authResult);
//
//		List<Row> userGroups = new ArrayList<>();
//		Row inputRow = new Row();
//		inputRow.addValue(new Value(""));
//		inputRow.addValue(new Value("admin"));
//		inputRow.addValue(new Value(true));
//		userGroups.add(inputRow);
//		assertThat(testSubject.rowLevelSecurity(false, userGroups))//
//				.isEqualTo("\r\nwhere ( ( SecurityToken IS NULL )" + "\r\nor ( SecurityToken IN ('useradmin','admin','useruser','userbeispiel') ) )");
//
//		String tableName = "tEmployee";
//		List<GrantedAuthority> ga = (List<GrantedAuthority>) sc.getAuthentication().getAuthorities();
//		Table test = testSubject.checkPrivilege(ga, tableName);
//		assertThat(test.getRows().isEmpty())//
//				.isEqualTo(false);
//		assertThat(test.getRows()).hasSize(1);
//
//	}
//
//	@DisplayName("Überprüfen, ob Rollen aus mehreren Gruppen richtig übernommen werden")
//	@Test
//	public void testUserDetailsMapperForMultipleGroups() {
//		final LdapAuthenticationProvider ldapProvider = new LdapAuthenticationProvider(new MockAuthenticator());
//		ldapProvider.setUserDetailsContextMapper(userDetailsContextMapper);
//		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("user", "password");
//		Object authDetails = new Object();
//		authRequest.setDetails(authDetails);
//		Authentication authResult = ldapProvider.authenticate(authRequest);
//		UserDetails user = (UserDetails) authResult.getPrincipal();
//		assertThat(user.getAuthorities()).hasSize(12);
//	}
//
//	@DisplayName("User ohne Eintrag im tUser")
//	@Test
//	public void testUserDetailsMapperForUnknownUser() {
//		final LdapAuthenticationProvider ldapProvider = new LdapAuthenticationProvider(new MockAuthenticator());
//		ldapProvider.setUserDetailsContextMapper(userDetailsContextMapper);
//		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("test", "password");
//		Object authDetails = new Object();
//		authRequest.setDetails(authDetails);
//		Authentication authResult = ldapProvider.authenticate(authRequest);
//		UserDetails user = (UserDetails) authResult.getPrincipal();
//		assertThat(user.getAuthorities()).hasSize(1);
//	}
//
//	@DisplayName("User ohne Gruppen")
//	@Test
//	public void testUserDetailsMapperForZeroGroups() {
//		final LdapAuthenticationProvider ldapProvider = new LdapAuthenticationProvider(new MockAuthenticator());
//		ldapProvider.setUserDetailsContextMapper(userDetailsContextMapper);
//		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("codemonkey", "password");
//		Object authDetails = new Object();
//		authRequest.setDetails(authDetails);
//		Authentication authResult = ldapProvider.authenticate(authRequest);
//		UserDetails user = (UserDetails) authResult.getPrincipal();
//		assertThat(user.getAuthorities()).hasSize(1);
//	}
//
//	@DisplayName("User mit AD Gruppen und Datenbank Gruppen")
//	@Test
//	public void testUserDetailsMapperForADAndDaoGroups() {
//		final LdapAuthenticationProvider ldapProvider = new LdapAuthenticationProvider(new MockAuthenticator(), new MockAuthoritiesPopulator());
//		ldapProvider.setUserDetailsContextMapper(userDetailsContextMapper);
//		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("admin", "rqgzxTf71EAx8chvchMi");
//		Object authDetails = new Object();
//		authRequest.setDetails(authDetails);
//		Authentication authResult = ldapProvider.authenticate(authRequest);
//		UserDetails user = (UserDetails) authResult.getPrincipal();
//		assertThat(user.getAuthorities()).hasSize(6);
//	}

}

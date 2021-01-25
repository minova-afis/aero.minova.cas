package aero.minova.core.application.system.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.data.ldap.AutoConfigureDataLdap;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import aero.minova.core.application.system.SecurityConfig;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Import(SecurityConfig.class)
@AutoConfigureDataLdap
public class AuthenticationTest {
	
	@Autowired
	@Qualifier("ldapUser")
	private LdapUserDetailsMapper userDetailsContextMapper ;
	
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
			}else if (username.equals("user") && password.equals("password")) {
				ctx.setDn(new DistinguishedName("cn=user,ou=people,dc=springframework,dc=org"));
				return ctx;
			}else if (username.equals("test") && password.equals("password")) {
				ctx.setDn(new DistinguishedName("cn=user,ou=people,dc=springframework,dc=org"));
				return ctx;
			}else if (username.equals("codemonkey") && password.equals("password")) {
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
			return AuthorityUtils.createAuthorityList("ROLE_user","ROLE_test");

		}

		String getRequestedUsername() {
			return this.username;
		}

	}

	@DisplayName("Überprüfen, ob Rollen richtig übernommen werden")
	@Test
	public void testUserDetailsMapper() {
		final LdapAuthenticationProvider ldapProvider = new LdapAuthenticationProvider(new MockAuthenticator());
		ldapProvider.setUserDetailsContextMapper(userDetailsContextMapper);
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("admin",
				"rqgzxTf71EAx8chvchMi");
		Object authDetails = new Object();
		authRequest.setDetails(authDetails);
		Authentication authResult = ldapProvider.authenticate(authRequest);
		UserDetails user = (UserDetails) authResult.getPrincipal();
		assertThat(user.getAuthorities()).hasSize(4);
	}
	
	@DisplayName("Überprüfen, ob Rollen aus mehreren Gruppen richtig übernommen werden")
	@Test
	public void testUserDetailsMapperForMultipleGroups() {
		final LdapAuthenticationProvider ldapProvider = new LdapAuthenticationProvider(new MockAuthenticator());
		ldapProvider.setUserDetailsContextMapper(userDetailsContextMapper);
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("user",
				"password");
		Object authDetails = new Object();
		authRequest.setDetails(authDetails);
		Authentication authResult = ldapProvider.authenticate(authRequest);
		UserDetails user = (UserDetails) authResult.getPrincipal();
		assertThat(user.getAuthorities()).hasSize(12);
	}
	
	@DisplayName("User ohne Eintrag im tUser")
	@Test
	public void testUserDetailsMapperForUnknownUser() {
		final LdapAuthenticationProvider ldapProvider = new LdapAuthenticationProvider(new MockAuthenticator());
		ldapProvider.setUserDetailsContextMapper(userDetailsContextMapper);
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("test",
				"password");
		Object authDetails = new Object();
		authRequest.setDetails(authDetails);
		assertThatExceptionOfType(RuntimeException.class)//
			.isThrownBy(() -> ldapProvider.authenticate(authRequest))
			.withMessageContaining("User with username " + authRequest.getName() + " is not registered in the data repository");
	}
	
	@DisplayName("User ohne Gruppen")
	@Test
	public void testUserDetailsMapperForZeroGroups() {
		final LdapAuthenticationProvider ldapProvider = new LdapAuthenticationProvider(new MockAuthenticator());
		ldapProvider.setUserDetailsContextMapper(userDetailsContextMapper);
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("codemonkey",
				"password");
		Object authDetails = new Object();
		authRequest.setDetails(authDetails);
		Authentication authResult = ldapProvider.authenticate(authRequest);
		UserDetails user = (UserDetails) authResult.getPrincipal();
		assertThat(user.getAuthorities()).hasSize(1);
	}
	
	@DisplayName("User mit AD Gruppen und Datenbank Gruppen")
	@Test
	public void testUserDetailsMapperForADAndDaoGroups() {
		final LdapAuthenticationProvider ldapProvider = new LdapAuthenticationProvider(new MockAuthenticator(),new MockAuthoritiesPopulator());
		ldapProvider.setUserDetailsContextMapper(userDetailsContextMapper);
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("admin",
				"rqgzxTf71EAx8chvchMi");
		Object authDetails = new Object();
		authRequest.setDetails(authDetails);
		Authentication authResult = ldapProvider.authenticate(authRequest);
		UserDetails user = (UserDetails) authResult.getPrincipal();
		assertThat(user.getAuthorities()).hasSize(6);
	}

}


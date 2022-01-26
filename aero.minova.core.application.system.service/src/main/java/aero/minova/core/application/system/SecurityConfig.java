package aero.minova.core.application.system;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import aero.minova.core.application.system.service.SecurityService;
import aero.minova.core.application.system.sql.SystemDatabase;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${security_ldap_domain:minova.com}")
	private String domain;

	@Value("${security_ldap_address:ldap://mindcsrv.minova.com:3268/}")
	private String ldapServerAddress;

	// Hier ist kein Default gesetzt, damit man bewusst den Admin-Nutzer deaktivieren muss.
	@Value("${login_dataSource:}")
	private String dataSource;

	@Value("${server.port:8084}")
	private String serverPort;

	@Autowired
	SecurityService securityService;

	@Autowired
	SystemDatabase systemDatabase;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/public/**", "/img/**", "/js/**", "/theme/**", "/index", "/login", "/layout").permitAll();
		http.authorizeRequests().anyRequest().fullyAuthenticated();
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/");
		http.formLogin()//
				.loginPage("/login")//
				.defaultSuccessUrl("/")//
				.permitAll();
		http.httpBasic();
		http.csrf().disable(); // TODO Entferne dies. Vereinfacht zur Zeit die Loginseite.
		http.logout().permitAll();
	}

	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		if (dataSource.equals("ldap")) {
			ActiveDirectoryLdapAuthenticationProvider acldap = new ActiveDirectoryLdapAuthenticationProvider(domain, ldapServerAddress);
			acldap.setUserDetailsContextMapper(this.userDetailsContextMapper());
			auth.authenticationProvider(acldap);
		} else if (dataSource.equals("database")) {
			auth.jdbcAuthentication()//
					.dataSource(systemDatabase.getDataSource())//
					.usersByUsernameQuery("select Username,Password,LastAction from xtcasUsers where Username = ?")//
					.authoritiesByUsernameQuery("select Username,Authority from xtcasAuthorities where Username = ?");
		} else if (dataSource.equals("admin")) {
			auth.inMemoryAuthentication()//
					.withUser("admin").password(passwordEncoder().encode("rqgzxTf71EAx8chvchMi")).authorities("admin");
		}
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean("ldapUser")
	public UserDetailsContextMapper userDetailsContextMapper() throws RuntimeException {
		return new LdapUserDetailsMapper() {
			@SuppressWarnings("unchecked")
			@Override
			public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities)
					throws RuntimeException {
				List<GrantedAuthority> grantedAuthorities = securityService.loadPrivileges(username, (List<GrantedAuthority>) authorities);
				return super.mapUserFromContext(ctx, username, grantedAuthorities);
			}
		};
	}
}
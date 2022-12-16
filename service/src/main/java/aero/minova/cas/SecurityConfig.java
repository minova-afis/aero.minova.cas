package aero.minova.cas;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import aero.minova.cas.service.SecurityService;
import aero.minova.cas.sql.SystemDatabase;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Value("${security_ldap_domain:minova.com}")
	private String domain;

	@Value("${security_ldap_address:ldap://mindcsrv.minova.com:3268/}")
	private String ldapServerAddress;

	@Value("${login_dataSource:}")
	private String dataSource;

	@Value("${server.port:8084}")
	private String serverPort;

	@Autowired
	SecurityService securityService;

	@Autowired
	SystemDatabase systemDatabase;

	private static final String ADMIN = "admin";

//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
//		if (dataSource.equals("ldap")) {
//			ActiveDirectoryLdapAuthenticationProvider acldap = new ActiveDirectoryLdapAuthenticationProvider(domain, ldapServerAddress);
//			acldap.setConvertSubErrorCodesToExceptions(true);
//			acldap.setUserDetailsContextMapper(this.userDetailsContextMapper());
//
//			return auth.authenticationProvider(acldap).build();
//		} else if (dataSource.equals("database")) {
//			return auth.jdbcAuthentication()//
//					.dataSource(systemDatabase.getDataSource())//
//					.usersByUsernameQuery("select Username,Password,LastAction from xtcasUsers where Username = ?")//
//					.authoritiesByUsernameQuery("select Username,Authority from xtcasAuthorities where Username = ?")
//					.and().build();
//		} else if (dataSource.equals(ADMIN)) {
//			auth.inMemoryAuthentication()//
//					.withUser(ADMIN)
//					.password(passwordEncoder().encode("rqgzxTf71EAx8chvchMi"))
//					.authorities(ADMIN);
//		}
//		throw new RuntimeException("dataSource '" + dataSource + "' is unknown");
//	}

	public void configureHttpSecStandard(HttpSecurity http) throws Exception {
		http.authorizeRequests().requestMatchers("/actuator/**").permitAll();

		http.authorizeRequests().requestMatchers("/", "/public/**", "/img/**", "/js/**", "/theme/**", "/index", "/login", "/layout").permitAll();
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

	@Order(1)
	@Configuration
	@Profile("dev")
	public static class DevSecurityConfig {
		@Autowired
		SecurityConfig securityConfig;

		@Autowired
		CustomLogger customLogger;

		@PostConstruct
		public void devWarning() {
			customLogger.logError("Never use dev-profile in production!", new Exception());
		}

		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			securityConfig.configureHttpSecStandard(http);

			// Enables CorsConfigurationSource to be used
			return http
					.cors()
					.and()
					.build();
		}

		@Bean
		CorsConfigurationSource corsConfigurationSource() {
			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			CorsConfiguration corsConfiguration = new CorsConfiguration();
			corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
			corsConfiguration.setAllowedMethods(Arrays.asList("*"));
			corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
			// Register mapping(s) to be added to cors whitelist e.g /cas/ping or /**
			source.registerCorsConfiguration("/**", corsConfiguration);
			return source;
		}
	}

	@Order(2)
	@Configuration
	public static class StandardConfig {
		@Autowired
		SecurityConfig securityConfig;

		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			securityConfig.configureHttpSecStandard(http);

			return http.build();
		}
	}

	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
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
				List<String> userSecurityTokens = securityService.loadLDAPUserTokens(username);
				List<GrantedAuthority> grantedAuthorities = securityService.loadUserGroupPrivileges(username, userSecurityTokens,
						(List<GrantedAuthority>) authorities);
				return super.mapUserFromContext(ctx, username, grantedAuthorities);
			}
		};
	}
}

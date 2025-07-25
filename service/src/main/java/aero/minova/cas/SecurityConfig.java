package aero.minova.cas;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

import aero.minova.cas.ldap.MultipleLdapDomainsAuthenticationProvider;
import aero.minova.cas.ldap.MultipleLdapServerAddressesUserDetailsManager;
import aero.minova.cas.service.SecurityService;
import aero.minova.cas.sql.SystemDatabase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	private static final String ADMIN = "admin";

	private static final String MULTIPLE_LDAP_CONFIGURATIONS_SEPERATOR = ";";

	@Value("${security_ldap_domain:minova.com}")
	private String domain;

	@Value("${security_ldap_address:ldap://mindcsrv.minova.com:3268/}")
	private String ldapServerAddress;

	@Value("${login_dataSource:}")
	private String loginDataSource;

	@Value("${server.port:8084}")
	private String serverPort;

	@Value("${cors.allowed.origins:http://localhost:8100,https://localhost:8100}")
	private String allowedOrigins;

	@Autowired
	SystemDatabase systemDatabase;

	private final DataSource dataSource;

	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}

	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedMethods(Arrays.asList("*"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
		corsConfiguration.setExposedHeaders(Arrays.asList("*"));

		corsConfiguration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);

		return source;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(requests -> requests
				.requestMatchers("/actuator/**").permitAll()
				.requestMatchers("/", "/public/**", "/img/**", "/js/**", "/theme/**", "/index", "/login", "/layout")
				.permitAll().anyRequest().fullyAuthenticated())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"))
				.formLogin(form -> form.loginPage("/login")//
						.defaultSuccessUrl("/")//
						.permitAll())
				.httpBasic(Customizer.withDefaults())
				.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
						.configurationSource(corsConfigurationSource()))

				// scj: CSRF Should only be enabled if basic auth is replaced by a modern
				// method.
				.csrf((csrf) -> csrf.disable()); // TODO: Reconsider this, as disabling CSRF can lead to security
													// vulnerabilities.
		return http.build();
	}

	@Bean
	public UserDetailsManager userDetailsManager() {
		if ("ldap".equals(loginDataSource)) {
			return new MultipleLdapServerAddressesUserDetailsManager(
					Arrays.asList(ldapServerAddress.split(SecurityConfig.MULTIPLE_LDAP_CONFIGURATIONS_SEPERATOR)));
		} else if ("database".equals(loginDataSource)) {
			JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

			if (systemDatabase.isSQLDatabase()) {
				jdbcUserDetailsManager.setUsersByUsernameQuery(
						"select Username,Password,LastAction as enabled  from xtcasUsers where Username = ?");
			} else {
				jdbcUserDetailsManager.setUsersByUsernameQuery(
						"select Username,Password,LastAction>0 as enabled  from xtcasUsers where Username = ?");
			}

			jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
					"select Username,Authority from xtcasAuthorities where Username = ?");

			return jdbcUserDetailsManager;
		} else if (ADMIN.equals(loginDataSource)) {
			UserDetails user = User //
					.withUsername(ADMIN) //
					.password(passwordEncoder().encode("rqgzxTf71EAx8chvchMi")) //
					.roles(ADMIN) //
					.authorities(ADMIN) //
					.build();
			return new InMemoryUserDetailsManager(user);
		}
		throw new IllegalArgumentException("dataSource contains unknown parameter '" + loginDataSource + "'");
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@ConditionalOnProperty(value = "login_dataSource", havingValue = "ldap")
	AuthenticationProvider activeDirectoryLdapAuthenticationProvider(
			UserDetailsContextMapper userDetailsContextMapper) {
		return new MultipleLdapDomainsAuthenticationProvider(//
				Arrays.asList(domain.split(SecurityConfig.MULTIPLE_LDAP_CONFIGURATIONS_SEPERATOR)), //
				Arrays.asList(ldapServerAddress.split(SecurityConfig.MULTIPLE_LDAP_CONFIGURATIONS_SEPERATOR)), //
				userDetailsContextMapper);
	}

	@Bean("ldapUser")
	@ConditionalOnProperty(value = "login_dataSource", havingValue = "ldap")
	UserDetailsContextMapper userDetailsContextMapper(SecurityService securityService) throws RuntimeException {
		return new LdapUserDetailsMapper() {
			@SuppressWarnings("unchecked")
			@Override
			public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
					Collection<? extends GrantedAuthority> authorities)
					throws RuntimeException {
				List<String> userSecurityTokens = securityService.loadLDAPUserTokens(username);
				List<GrantedAuthority> grantedAuthorities = securityService.loadUserGroupPrivileges(username,
						userSecurityTokens,
						(List<GrantedAuthority>) authorities);
				return super.mapUserFromContext(ctx, username, grantedAuthorities);
			}
		};
	}
}

package aero.minova.cas;

import aero.minova.cas.service.SecurityService;
import aero.minova.cas.sql.SystemDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;
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

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
	private static final String ADMIN = "admin";

	@Value("${security_ldap_domain:minova.com}")
	private String domain;

	@Value("${security_ldap_address:ldap://mindcsrv.minova.com:3268/}")
	private String ldapServerAddress;

	@Value("${login_dataSource:}")
	private String loginDataSource;

	@Value("${server.port:8084}")
	private String serverPort;

	private final Environment environment;
	private final CustomLogger customLogger;
	private final DataSource dataSource;

	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// scj: Should only be enabled if basic auth is replaced by a modern method.
		http.cors(Customizer.withDefaults())
				.csrf((csrf)-> csrf.disable()); // TODO: Reconsider this, as disabling CSRF can lead to security vulnerabilities.


		// Log a warning if the "dev" profile is active
		if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
			customLogger.logError("Never use profile 'dev' in production!", new Exception());
		}

		return http.build();
	}


	@Bean
	public UserDetailsManager userDetailsManager() {
		if ("ldap".equals(loginDataSource)) {
			DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(ldapServerAddress);
			contextSource.afterPropertiesSet();

			return new LdapUserDetailsManager(contextSource);
		} else if ("database".equals(loginDataSource)) {
			JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
			jdbcUserDetailsManager.setUsersByUsernameQuery("select Username,Password,LastAction from xtcasUsers where Username = ?");
			jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select Username,Authority from xtcasAuthorities where Username = ?");

			return jdbcUserDetailsManager;
		} else if (ADMIN.equals(loginDataSource)) {
			UserDetails user = User
					.withUsername(ADMIN)
					.password(passwordEncoder().encode("rqgzxTf71EAx8chvchMi"))
					.roles(ADMIN)
					.authorities(ADMIN)
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
	AuthenticationProvider activeDirectoryLdapAuthenticationProvider(UserDetailsContextMapper userDetailsContextMapper) {
		ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(domain, ldapServerAddress);
		provider.setConvertSubErrorCodesToExceptions(true);
		provider.setUserDetailsContextMapper(userDetailsContextMapper);

		return provider;
	}

	@Bean("ldapUser")
	@ConditionalOnProperty(value = "login_dataSource", havingValue = "ldap")
	UserDetailsContextMapper userDetailsContextMapper(SecurityService securityService) throws RuntimeException {
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

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();

		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("*"));

		if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
			corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8100", "https://saas-app-dev.minova.com"));
		} else if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
			corsConfiguration.setAllowedOrigins(Arrays.asList("https://saas-app.minova.com"));
		}

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);

		return source;
	}
}

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

import java.sql.SQLException;
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
	private String dataSource;

	@Value("${server.port:8084}")
	private String serverPort;

	private final Environment environment;
	private final CustomLogger customLogger;

	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(requests -> requests
				.requestMatchers("/actuator/**").permitAll()
				.requestMatchers("/", "/public/**", "/img/**", "/js/**", "/theme/**", "/index", "/login", "/layout").permitAll()
				.anyRequest().fullyAuthenticated()
		);

		http.logout().logoutUrl("/logout").logoutSuccessUrl("/");
		http.formLogin()//
				.loginPage("/login")//
				.defaultSuccessUrl("/")//
				.permitAll();
		http.httpBasic();
		http.csrf().disable(); // TODO Entferne dies. Vereinfacht zur Zeit die Loginseite.
		http.logout().permitAll();

		Arrays.stream(environment.getActiveProfiles())
				.filter("dev"::equals)
				.forEach(profile -> {
					customLogger.logError("Never use profile '" + profile + "' in production!", new Exception());

					try {
						// Enables CorsConfigurationSource to be used
						http.cors();
					} catch (Exception e) {
						customLogger.logError(e.getMessage(), e);
						throw new RuntimeException(e);
					}
				});
		return http.build();
	}

	@Bean
	public UserDetailsManager userDetailsManager(SystemDatabase systemDatabase) throws SQLException {
		if ("ldap".equals(dataSource)) {
			DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(ldapServerAddress);
			contextSource.afterPropertiesSet();

			return new LdapUserDetailsManager(contextSource);
		} else if ("database".equals(dataSource)) {
			JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(systemDatabase.getDataSource());
			jdbcUserDetailsManager.setUsersByUsernameQuery("select Username,Password,LastAction from xtcasUsers where Username = ?");
			jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select Username,Authority from xtcasAuthorities where Username = ?");

			return jdbcUserDetailsManager;
		} else if (ADMIN.equals(dataSource)) {
			UserDetails user = User
					.withUsername(ADMIN)
					.password(passwordEncoder().encode("rqgzxTf71EAx8chvchMi"))
					.roles(ADMIN)
					.authorities(ADMIN)
					.build();
			return new InMemoryUserDetailsManager(user);
		}
		throw new IllegalArgumentException("dataSource contains unknown parameter '" + dataSource + "'");
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
		corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
		corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
		corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// Register mapping(s) to be added to cors whitelist e.g /cas/ping or /**
		source.registerCorsConfiguration("/**", corsConfiguration);

		return source;
	}
}

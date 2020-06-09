package ch.minova.service.core.application.system.configuration.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@Profile("production")
public class ProductionWebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${security.ldap.domain:minova.com}")
	private String domain;

	@Value("${security.ldap.address:ldap://mindcsrv.minova.com/}")
	private String ldapServerAddress;

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests().antMatchers("/root/**").fullyAuthenticated();
		httpSecurity.logout().logoutUrl("/root/logout").logoutSuccessUrl("/");
		httpSecurity.formLogin()//
				.loginPage("/root/login.html")//
				.defaultSuccessUrl("/root/index.html")//
				.permitAll();
		httpSecurity.csrf().disable(); // TODO enable
		httpSecurity.logout().permitAll();
	}

	@Bean
	public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		return new ActiveDirectoryLdapAuthenticationProvider(domain, ldapServerAddress);
	}

}

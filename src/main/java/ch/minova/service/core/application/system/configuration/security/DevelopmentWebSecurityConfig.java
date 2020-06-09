package ch.minova.service.core.application.system.configuration.security;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@Profile("development")
public class DevelopmentWebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests().anyRequest().permitAll();
		// Wird für die H2 Konsole benötigt.
		httpSecurity.csrf().disable();
		httpSecurity.headers().frameOptions().disable();
		httpSecurity.logout().logoutUrl("/root/logout").logoutSuccessUrl("/");
		httpSecurity.formLogin()//
				.loginPage("/root/login.html")//
				.defaultSuccessUrl("/root/index.html")//
				.permitAll();
	}
}

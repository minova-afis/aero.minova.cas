package ch.minova.service.core.application.system.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

/**
 * This Configuration is applied during all profiles.
 * 
 * @author avots
 */
@Configuration
public class WebSecurityConfig {
	@Bean
	public SpringSecurityDialect springSecurityDialect() {
		return new SpringSecurityDialect();
	}
}

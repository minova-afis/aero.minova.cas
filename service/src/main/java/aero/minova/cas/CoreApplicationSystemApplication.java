package aero.minova.cas;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

// NOTE on the @EnableJpaRepositories excludeFilter: ch.minova.foundation.rest.auth's Grants persistence package
// (ch.minova.foundation.rest.auth.grants.persistence -- renamed from .claims.persistence, see the Claims->Grants
// pivot) already self-registers its own repositories via GrantsAutoConfiguration's scoped, conditional
// @EnableJpaRepositories(basePackageClasses = ...). Reaching it a second time from here collides on bean name
// (e.g. "groupRepository") and fails hard with BeanDefinitionOverrideException - see aero.minova.cas#1497.
// @EntityScan doesn't have this problem (Spring Boot's EntityScanPackages is explicitly additive across
// multiple @EntityScan declarations, by design - no
// collision risk), so it's left as a plain blanket scan. If another ch.minova.foundation.rest.* library is added
// later that ALSO self-registers its own repositories the way foundation.rest.auth's Grants layer does, add its
// persistence package to the exclude pattern below too.
@SpringBootApplication
@ComponentScan({ "aero.minova", "com.minova", "ch.minova.foundation.rest" })
@EntityScan({ "aero.minova", "com.minova", "ch.minova.foundation.rest" })
@EnableJpaRepositories(value = { "aero.minova", "com.minova", "ch.minova.foundation.rest" },
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
                pattern = "ch\\.minova\\.foundation\\.rest\\.auth\\.grants\\.persistence\\..*"))
@Configuration
@EnableScheduling
public class CoreApplicationSystemApplication {

	@Autowired
	static CustomLogger logger;

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		SpringApplication.run(CoreApplicationSystemApplication.class, args);

		try {
			logger = new CustomLogger();
			logger.logInfo(VersionUtil.getVersionString());
		} catch (Exception e) {
			logger.logError("Could not read CAS Version.", e);
		}
	}

}
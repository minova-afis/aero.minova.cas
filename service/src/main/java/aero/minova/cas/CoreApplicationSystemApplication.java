package aero.minova.cas;

import java.io.IOException;
import java.util.Properties;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import aero.minova.cas.service.mdi.Main;

@SpringBootApplication
@ComponentScan({ "aero.minova", "com.minova" })
@EntityScan({ "aero.minova", "com.minova" })
@EnableJpaRepositories({ "aero.minova", "com.minova" })
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
			logger.logInfo(getVersion());
		} catch (Exception e) {
			logger.logError("Could not read CAS Version.", e);
		}
	}

	public static String getVersion() throws IOException {
		Properties properties = new Properties();
		properties.load(Main.class.getResourceAsStream("/pom.properties"));
		return properties.getProperty("groupId") + "." + properties.getProperty("artifactId") + "-" + properties.getProperty("version");
	}
}
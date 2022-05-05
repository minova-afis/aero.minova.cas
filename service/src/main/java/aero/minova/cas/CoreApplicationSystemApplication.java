package aero.minova.cas;

import java.io.IOException;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

// Der ComponentScan wird verwendet, damit alle Minova Komponenten im classpath geladen werden.
@SpringBootApplication
@ComponentScan({ "aero.minova", "com.minova" })
@EntityScan("aero.minova")
@EnableJpaRepositories("aero.minova")
@Configuration
@EnableScheduling
public class CoreApplicationSystemApplication {
	public static void main(String[] args) throws IOException {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		SpringApplication.run(CoreApplicationSystemApplication.class, args);
	}
}
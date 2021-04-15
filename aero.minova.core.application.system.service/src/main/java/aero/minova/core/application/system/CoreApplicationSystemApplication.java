package aero.minova.core.application.system;

import java.io.IOException;
import java.time.ZoneId;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import aero.minova.core.application.system.domain.Value;
import aero.minova.core.application.system.domain.ValueDeserializer;
import aero.minova.core.application.system.domain.ValueSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

// Der ComponentScan wird verwendet, damit alle Minova Komponenten im classpath geladen werden.
@SpringBootApplication
@ComponentScan("aero.minova")
@Configuration
@EnableScheduling
public class CoreApplicationSystemApplication {
	public static void main(String[] args) throws IOException {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(CoreApplicationSystemApplication.class, args);
	}

	@Bean
	public Gson gson() {
		return new GsonBuilder() //
				.registerTypeAdapter(Value.class, new ValueSerializer()) //
				.registerTypeAdapter(Value.class, new ValueDeserializer()) //
				.create();
	}
}
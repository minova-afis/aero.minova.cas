package aero.minova.core.application.system;

import aero.minova.core.application.system.domain.Value;
import aero.minova.core.application.system.domain.ValueDeserializer;
import aero.minova.core.application.system.domain.ValueSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

// Der ComponentScan wird verwendet, damit alle Minova Komponenten im classpath geladen werden.
@SpringBootApplication
@ComponentScan("aero.minova")
@Configuration
public class CoreApplicationSystemApplication {
	public static void main(String[] args) throws IOException {
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
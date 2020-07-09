package aero.minova.core.application.system;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// Der CompponentScan wird verwendet, damit alle Minova Komponenten im classpath geladen werden.
@ComponentScan("ch.minova")
@SpringBootApplication
public class CoreApplicationSystemApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(CoreApplicationSystemApplication.class, args);
	}
}

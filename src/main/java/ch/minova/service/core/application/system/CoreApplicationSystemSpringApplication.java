package ch.minova.service.core.application.system;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

// Der CompponentScan wird verwendet, damit alle Minova Komponenten im classpath geladen werden.
@ComponentScan("ch.minova")
@SpringBootApplication
@EnableAdminServer
public class CoreApplicationSystemSpringApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(CoreApplicationSystemSpringApplication.class, args);
		//openIndexPage();
	}

//	/**
//	 * Source https://stackoverflow.com/questions/27378292/launch-browser-automatically-after-spring-boot-webapp-is-ready
//	 * 
//	 * @throws IOException
//	 */
//	private static void openIndexPage() {
//		try {
//			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost");
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

}

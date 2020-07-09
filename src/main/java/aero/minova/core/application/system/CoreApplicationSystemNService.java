package aero.minova.core.application.system;

import java.io.IOException;

import org.springframework.boot.SpringApplication;

import ch.minova.nservice.managed.Configuration;
import ch.minova.nservice.managed.service.ManagedService;
import ch.minova.nservice.managed.service.backend.ServiceConfiguration;
import ch.minova.nservice.managed.service.backend.ServiceMethods;

/**
 * TODO Zu einem installierbaren Modul machen. Dazu muss man das Parent Pom anpassen,
 * eine Lizenz und die "Setup.xml" erstellen.
 * 
 * @author avots
 *
 */
public class CoreApplicationSystemNService implements ManagedService {

	private final String[] args;

	public CoreApplicationSystemNService(String[] args) {
		this.args = args;
	}

	public static void main(String[] args) throws IOException {
		ManagedService.execute(new CoreApplicationSystemNService(args), args);
	}

	@Override
	public void loadService(ServiceConfiguration serviceConf, Configuration systemConf) throws Throwable {}

	@Override
	public void executeService(ServiceMethods serviceMethods) throws Throwable {
		SpringApplication.run(CoreApplicationSystemApplication.class, args);
	}

}

package aero.minova.cas.app.extension;

import org.springframework.stereotype.Component;

import aero.minova.cas.service.model.ServiceProperties;
import jakarta.annotation.PostConstruct;

@Component
public class ServicePropertiesExtension extends BaseExtension<ServiceProperties> {

	@PostConstruct
	public void setup() {
		allowDuplicateMatchcodes = true;
		super.setup(ServiceProperties.class);
	}
}
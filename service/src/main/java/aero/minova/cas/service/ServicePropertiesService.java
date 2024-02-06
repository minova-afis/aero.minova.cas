package aero.minova.cas.service;

import org.springframework.stereotype.Service;

import aero.minova.cas.service.model.ServiceProperties;
import jakarta.annotation.PostConstruct;

@Service
public class ServicePropertiesService extends BaseService<ServiceProperties> {
	@PostConstruct
	public void setup() {
		allowDuplicateMatchcodes = true;
	}
}

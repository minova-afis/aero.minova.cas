package aero.minova.cas.service;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.service.repository.ServiceMessageReceiverLoginTypeRepository;
import aero.minova.cas.servicenotifier.ServiceNotifierService;

//benötigt, damit JUnit-Tests nicht abbrechen
@SpringBootTest(classes = CoreApplicationSystemApplication.class, properties = { "application.runner.enabled=false" })
class ServiceNotifierServiceTest {

	@Autowired
	ServiceNotifierService serviceNotifierService;

	@Autowired
	ServiceMessageReceiverLoginTypeRepository serviceMessageReceiverLoginTypeRepository;

	@Test
	void assureLoginTypesAreCreated() {
		assertTrue(serviceMessageReceiverLoginTypeRepository.findByKeyText("None").isPresent());
		assertTrue(serviceMessageReceiverLoginTypeRepository.findByKeyText("BasicAuth").isPresent());
		assertTrue(serviceMessageReceiverLoginTypeRepository.findByKeyText("OAuth2").isPresent());
	}

}

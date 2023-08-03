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
		assertTrue(serviceMessageReceiverLoginTypeRepository.findByKeytextAndLastActionGreaterThan("None", 0).isPresent());
		assertTrue(serviceMessageReceiverLoginTypeRepository.findByKeytextAndLastActionGreaterThan("BasicAuth", 0).isPresent());
		assertTrue(serviceMessageReceiverLoginTypeRepository.findByKeytextAndLastActionGreaterThan("OAuth2", 0).isPresent());
	}

}

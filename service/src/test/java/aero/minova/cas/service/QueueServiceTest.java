package aero.minova.cas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.service.model.CASServices;
import aero.minova.cas.service.repository.CASServicesRepository;
import aero.minova.cas.service.repository.NewsfeedListenerRepository;
import aero.minova.cas.service.repository.ProcedureNewsfeedRepository;
import aero.minova.cas.service.repository.ServiceMessageReceiverLoginTypeRepository;
import aero.minova.cas.servicenotifier.ServiceNotifierRegistrationAPI;

@SpringBootTest(classes = CoreApplicationSystemApplication.class)
@ActiveProfiles("test")
public class QueueServiceTest {

	@Autowired
	ServiceNotifierRegistrationAPI serviceNotifierRegistration;

	@Autowired
	QueueService queueService;

	@Autowired
	CASServicesRepository serviceRepo;

	@Autowired
	NewsfeedListenerRepository newsfeedListenerRepo;

	@Autowired
	ProcedureNewsfeedRepository procedureNewsfeedRepo;

	@Autowired
	ServiceMessageReceiverLoginTypeRepository serviceMessageReceiverLoginTypeRepo;

	@Test
	public void testRegisterAll() {
		serviceNotifierRegistration.registerService("Service", "localhost", 0, 1, "test", "abc1234", null, null, null);

		// Service mit Topics verbinden
		serviceNotifierRegistration.registerNewsfeedListener("tShipment", "Service");
		serviceNotifierRegistration.registerNewsfeedListener("tFlightSchedule", "Service");

		// Prozedur mit Topic verbinden
		serviceNotifierRegistration.registerProcedureNewsfeed("test", "tShipment");
		serviceNotifierRegistration.registerProcedureNewsfeed("test", "tFlightSchedule");

		final CASServices testService = serviceRepo.findByKeyLong(1).get();

		assertEquals(1, serviceRepo.findAll().size());
		assertEquals("Service", testService.getKeyText());
		assertEquals("test", testService.getUsername());
		assertEquals("abc1234", testService.getPassword());
		assertEquals(0, testService.getPort());
		assertEquals("localhost", testService.getServiceUrl());
		assertEquals(2, newsfeedListenerRepo.findAll().size());
		assertEquals("tShipment", newsfeedListenerRepo.findByKeyLong(1).get().getTopic());
		assertEquals("tFlightSchedule", newsfeedListenerRepo.findByKeyLong(2).get().getTopic());
		assertEquals(2, procedureNewsfeedRepo.findAll().size());
		assertEquals("test", procedureNewsfeedRepo.findByKeyLong(1).get().getKeyText());
		assertEquals("tShipment", procedureNewsfeedRepo.findByKeyLong(1).get().getTopic());
		assertEquals("test", procedureNewsfeedRepo.findByKeyLong(2).get().getKeyText());
		assertEquals("tFlightSchedule", procedureNewsfeedRepo.findByKeyLong(2).get().getTopic());
	}

}

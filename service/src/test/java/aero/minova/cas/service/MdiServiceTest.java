package aero.minova.cas.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.service.model.Mdi;
import aero.minova.cas.service.repository.MdiRepository;
import aero.minova.cas.service.repository.MdiTypeRepository;
import jakarta.persistence.EntityNotFoundException;

@SpringBootTest(classes = CoreApplicationSystemApplication.class)
@ActiveProfiles("test")
class MdiServiceTest {

	@Autowired
	MdiTypeRepository mdiTypeRepository;

	@Autowired
	MdiRepository mdiRepository;

	@Autowired
	MdiService mdiService;

	@Test
	void testSetupMdiTypes() {

		// Erster Aufruf legt 3 Typen an
		mdiService.setupMdiTypes();
		assertEquals(3, mdiTypeRepository.findByLastActionGreaterThan(0).size());
		assertEquals(mdiTypeRepository.findByKeyText("form").get(0), mdiService.getMdiTypeByKeyLong(1));
		assertEquals(mdiTypeRepository.findByKeyText("menu").get(0), mdiService.getMdiTypeByKeyLong(2));
		assertEquals(mdiTypeRepository.findByKeyText("application").get(0), mdiService.getMdiTypeByKeyLong(3));

		// Zweiter Aufruf legt nicht doppelt an
		mdiService.setupMdiTypes();
		assertEquals(3, mdiTypeRepository.findByLastActionGreaterThan(0).size());

		// Gibt keinen MdiTyp mit KeyLong 4
		assertThrows(EntityNotFoundException.class, () -> mdiService.getMdiTypeByKeyLong(4));
	}

	@Test
	void testSetupCASMdi() {
		mdiService.setupCASMdi();

		// Zehn Einträge insgesamt, 1 General Info, 2 Menüs, 7 Masken
		assertEquals(10, mdiRepository.findByLastActionGreaterThan(0).size());
		assertEquals(1, mdiRepository.findByMdiTypeKeyLongAndLastActionGreaterThan(3, 0).size());
		assertEquals(2, mdiRepository.findByMdiTypeKeyLongAndLastActionGreaterThan(2, 0).size());
		assertEquals(7, mdiRepository.findByMdiTypeKeyLongAndLastActionGreaterThan(1, 0).size());
	}

	@Test
	void testSaveMdi() {
		mdiService.setupCASMdi(); // Zum Testdaten anlegen

		// Es darf kein zweites General Info angelegt werden
		Mdi mdi1 = new Mdi("XX", "@XXX", null, 0, mdiService.getMdiTypeByKeyLong(3), "aero.minova.cas", null);
		assertThrows(RuntimeException.class, () -> mdiService.save(mdi1));

		// Doppelter KeyText darf nicht angelegt werden
		Mdi mdi2 = new Mdi("DBUser", "@xtcasUsers", "CAS", 16, mdiService.getMdiTypeByKeyLong(1), "aero.minova.cas", "admin");
		assertThrows(RuntimeException.class, () -> mdiService.save(mdi2));
	}
}

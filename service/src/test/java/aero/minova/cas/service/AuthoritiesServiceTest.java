package aero.minova.cas.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.service.model.Authorities;
import aero.minova.cas.service.repository.AuthoritiesRepository;

@SpringBootTest(classes = CoreApplicationSystemApplication.class)
@ActiveProfiles("test")
class AuthoritiesServiceTest {

	@Autowired
	AuthoritiesService authoritiesService;

	@Autowired
	AuthoritiesRepository authoritiesRepository;

	@Test
	void testFunctions() {

		// Einfaches Speichern
		Authorities auth = new Authorities();
		auth.setUsername("testuser");
		auth.setAuthority("testAuth");
		authoritiesService.save(auth);

		Authorities auth2 = new Authorities();
		auth2.setUsername("user2");
		auth2.setAuthority("testAuth");
		auth2 = authoritiesService.save(auth2);

		// Speichern mit gleichem Username und Authority nicht erlaubt
		Authorities auth3 = new Authorities();
		auth3.setUsername("testuser");
		auth3.setAuthority("testAuth");
		assertThrows(RuntimeException.class, () -> authoritiesService.save(auth3));

		// Suchen nach Username
		List<Authorities> findByUsername = authoritiesService.findByUsername("testuser");
		assertEquals(1, findByUsername.size());
		assertDoesNotThrow(() -> authoritiesService.save(findByUsername.get(0)));

		// Suchen nach Authority
		List<Authorities> findByAuthority = authoritiesService.findByAuthority("testAuth");
		assertEquals(2, findByAuthority.size());

		// Löschen -> Wirklich in Datenbank entfernt
		authoritiesService.deleteByUsernameAndAuthority("testuser", "testAuth");
		assertTrue(authoritiesRepository.findById(findByUsername.get(0).getKeyLong()).isEmpty());

		// Löschen von nicht existenten Datensätzen -> Exceptions
		assertThrows(RuntimeException.class, () -> authoritiesService.deleteByUsernameAndAuthority("gibts", "nicht"));
		assertThrows(RuntimeException.class, () -> authoritiesService.deleteById(5000));

	}
}

package aero.minova.cas.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.service.model.UserGroup;
import aero.minova.cas.service.model.Users;

@SpringBootTest(classes = CoreApplicationSystemApplication.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class UsersServiceTest {

	@Autowired
	UsersService usersService;

	@Autowired
	UserGroupService userGroupService;

	@BeforeEach
	void setup() {
		UserGroup group = new UserGroup();
		group.setKeyText("Minova");
		group.setSecurityToken("token");
		userGroupService.save(group);

		UserGroup group2 = new UserGroup();
		group2.setKeyText("MinovaAdmin");
		group2.setSecurityToken("token");
		userGroupService.save(group2);

		UserGroup group3 = new UserGroup();
		group3.setKeyText("MinovaTTA");
		group3.setSecurityToken("token");
		userGroupService.save(group3);

		Users user1 = new Users();
		user1.setUsername("user1");
		user1.setPassword("$2a$10$.PKTRwGwfIDHy0Nw5nj.CezXtt7I4KrV4WpRYQxHdI/6ex85M0KMK");
		usersService.save(user1);

		Users user2 = new Users();
		user2.setUsername("user2");
		user2.setPassword("$2a$10$.PKTRwGwfIDHy0Nw5nj.CezXtt7I4KrV4WpRYQxHdI/6ex85M0KMK");
		usersService.save(user2);

		Users user3 = new Users();
		user3.setUsername("user3");
		user3.setPassword("$2a$10$.PKTRwGwfIDHy0Nw5nj.CezXtt7I4KrV4WpRYQxHdI/6ex85M0KMK");
		usersService.save(user3);

		Users user4 = new Users();
		user4.setUsername("user4");
		user4.setPassword("$2a$10$.PKTRwGwfIDHy0Nw5nj.CezXtt7I4KrV4WpRYQxHdI/6ex85M0KMK");
		usersService.save(user4);

		Users user5 = new Users();
		user5.setUsername("user5");
		user5.setPassword("$2a$10$.PKTRwGwfIDHy0Nw5nj.CezXtt7I4KrV4WpRYQxHdI/6ex85M0KMK");
		usersService.save(user5);
	}

	@Test
	void testGroup() {
		UserGroup minovaGroup = userGroupService.findEntitiesByKeyText("Minova").get(0);
		Users user1 = usersService.findByUsername("user1");
		Users user4 = usersService.findByUsername("user4");

		usersService.addUserToUserGroup(user1.getKeyLong(), minovaGroup.getKeyLong());
		usersService.addUserToUserGroup(user4.getKeyLong(), minovaGroup.getKeyLong());

		assertEquals(2, usersService.findUsersInUserGroup(minovaGroup.getKeyLong()).size());

		usersService.removeUserFromUserGroup(user1.getKeyLong(), minovaGroup.getKeyLong());

		assertEquals(1, usersService.findUsersInUserGroup(minovaGroup.getKeyLong()).size());
	}

	@Test
	void testSave() {

		// Doppelter Username nicht erlaubt
		Users user1 = new Users();
		user1.setUsername("user1");
		assertThrows(RuntimeException.class, () -> usersService.save(user1));

		// Unverschlüsseltes Passwort nicht erlaubt
		Users user6 = new Users();
		user6.setUsername("user6");
		user6.setPassword("notEncrypted");
		assertThrows(RuntimeException.class, () -> usersService.save(user6));
		user6.setPassword("notEncryptednotEncryptednotEncryptednotEncryptednotEncryptednotEncrypted");
		assertThrows(RuntimeException.class, () -> usersService.save(user6));

		// Verschlüssteltes Passwort funktioniert
		user6.setPassword("$2a$10$lP4pfPhCXu9Oukr.oLK9hOYpwpT3O7EC2dEE2d3HSwBySwYnNVojG");
		assertDoesNotThrow(() -> usersService.save(user6));

		// Wird Passwort mit null gespeichert bleibt altes
		Users userFromService = usersService.findByUsername("user6");
		userFromService.setPassword(null);
		usersService.save(userFromService);
		Users userFromService2 = usersService.findByUsername("user6");
		assertEquals("$2a$10$lP4pfPhCXu9Oukr.oLK9hOYpwpT3O7EC2dEE2d3HSwBySwYnNVojG", userFromService2.getPassword());

		// Bei echtem Passwort wird geändert
		Users userFromService3 = usersService.findByUsername("user6");
		userFromService3.setPassword("$2a$10$XHHIEJR8h3K2E/teoo6xTOvopt79hbBU4xsEVLEQ0Pu/8MPbO6W/e");
		usersService.save(userFromService3);
		Users userFromService4 = usersService.findByUsername("user6");
		assertEquals("$2a$10$XHHIEJR8h3K2E/teoo6xTOvopt79hbBU4xsEVLEQ0Pu/8MPbO6W/e", userFromService4.getPassword());
	}

	@Test
	void testBaseService() {
		assertEquals(5, usersService.getAllEntities().size());

		Users u = usersService.findByUsername("user1");
		usersService.deleteById(u.getKeyLong());
		assertEquals(4, usersService.getAllEntities().size());
		Users deleted = usersService.findEntityById(u.getKeyLong());
		assertEquals(-1, deleted.getLastAction());

		assertThrows(RuntimeException.class, () -> usersService.deleteById(50000));
		assertThrows(RuntimeException.class, () -> usersService.findEntityById(50000));
		assertThrows(RuntimeException.class, () -> usersService.findEntitiesByKeyText("Does Not Exist"));

	}

}

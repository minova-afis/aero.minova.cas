package aero.minova.cas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.service.model.User;
import aero.minova.cas.service.model.UserGroup;

@SpringBootTest(classes = CoreApplicationSystemApplication.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

	@Autowired
	UserService userService;

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

		User user1 = new User();
		user1.setKeyText("user1");
		user1.setUserSecurityToken("quatsch");
		user1.setMemberships("Minova#MinovaAdmin");
		userService.save(user1);

		User user2 = new User();
		user2.setKeyText("user2");
		user2.setUserSecurityToken("quatsch");
		user2.setMemberships("MinovaTTA");
		userService.save(user2);

		User user3 = new User();
		user3.setKeyText("user3");
		user3.setUserSecurityToken("quatsch");
		user3.setMemberships("Minova");
		userService.save(user3);

		User user4 = new User();
		user4.setKeyText("user4");
		user4.setUserSecurityToken("quatsch");
		userService.save(user4);

		User user5 = new User();
		user5.setKeyText("user5");
		user5.setUserSecurityToken("quatsch");
		user5.setMemberships("");
		userService.save(user5);
	}

	@Test
	void testFindInGroup() {
		int minovaKey = userGroupService.findEntitiesByKeyText("Minova").get(0).getKeyLong();
		assertEquals(2, userService.findUsersInUserGroup(minovaKey).size());

		int minovaAdminKey = userGroupService.findEntitiesByKeyText("MinovaAdmin").get(0).getKeyLong();
		assertEquals(1, userService.findUsersInUserGroup(minovaAdminKey).size());

		int minovaTTAKey = userGroupService.findEntitiesByKeyText("MinovaTTA").get(0).getKeyLong();
		assertEquals(1, userService.findUsersInUserGroup(minovaTTAKey).size());
	}

	@Test
	void addUserToGroup() {

		UserGroup minovaGroup = userGroupService.findEntitiesByKeyText("Minova").get(0);
		User user1 = userService.findEntitiesByKeyText("user1").get(0);
		User user4 = userService.findEntitiesByKeyText("user4").get(0);

		// Zuerst 2 Members
		assertEquals(2, userService.findUsersInUserGroup(minovaGroup.getKeyLong()).size());

		// Dann 3
		userService.addUserToUserGroup(user4.getKeyLong(), minovaGroup.getKeyLong());
		assertEquals(3, userService.findUsersInUserGroup(minovaGroup.getKeyLong()).size());

		// User wird nicht doppelt hinzugefügt
		userService.addUserToUserGroup(user1.getKeyLong(), minovaGroup.getKeyLong());
		assertEquals(3, userService.findUsersInUserGroup(minovaGroup.getKeyLong()).size());
	}

	@Test
	void removeUserFromGroup() {
		UserGroup minovaGroup = userGroupService.findEntitiesByKeyText("Minova").get(0);
		User user1 = userService.findEntitiesByKeyText("user1").get(0);

		// Zuerst 2 Members
		assertEquals(2, userService.findUsersInUserGroup(minovaGroup.getKeyLong()).size());

		// Dann 1
		userService.removeUserFromUserGroup(user1.getKeyLong(), minovaGroup.getKeyLong());
		assertEquals(1, userService.findUsersInUserGroup(minovaGroup.getKeyLong()).size());
	}

}

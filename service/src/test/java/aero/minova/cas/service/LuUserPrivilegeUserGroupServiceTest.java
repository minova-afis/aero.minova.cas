package aero.minova.cas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import aero.minova.cas.CoreApplicationSystemApplication;
import aero.minova.cas.service.model.LuUserPrivilegeUserGroup;
import aero.minova.cas.service.model.UserGroup;
import aero.minova.cas.service.model.UserPrivilege;

@SpringBootTest(classes = CoreApplicationSystemApplication.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class LuUserPrivilegeUserGroupServiceTest {

	@Autowired
	UserGroupService userGroupService;

	@Autowired
	UserPrivilegeService userPrivilegeService;

	@Autowired
	LuUserPrivilegeUserGroupService luUserPrivilegeUserGroupService;

	@Test
	void testSave() {
		UserGroup group = new UserGroup();
		group.setKeyText("Minova");
		group.setSecurityToken("Minova");
		group = userGroupService.save(group);

		UserPrivilege up1 = new UserPrivilege();
		up1.setKeyText("priv1");
		up1 = userPrivilegeService.save(up1);

		UserPrivilege up2 = new UserPrivilege();
		up2.setKeyText("priv2");
		up2 = userPrivilegeService.save(up2);

		// Speichern erlaubt
		LuUserPrivilegeUserGroup lu = new LuUserPrivilegeUserGroup();
		lu.setUserGroup(group);
		lu.setUserPrivilege(up2);
		luUserPrivilegeUserGroupService.save(lu);

		LuUserPrivilegeUserGroup lu3 = new LuUserPrivilegeUserGroup();
		lu3.setUserGroup(group);
		lu3.setUserPrivilege(up1);
		luUserPrivilegeUserGroupService.save(lu3);

		// Gleiche Kombo nur einmal erlaubt
		LuUserPrivilegeUserGroup lu2 = new LuUserPrivilegeUserGroup();
		lu2.setUserGroup(group);
		lu2.setUserPrivilege(up2);
		assertThrows(RuntimeException.class, () -> luUserPrivilegeUserGroupService.save(lu2));

		// Suchen Testen
		assertEquals(2, luUserPrivilegeUserGroupService.findByUserGroupKey(group.getKeyLong()).size());
		assertEquals(1, luUserPrivilegeUserGroupService.findByUserPrivilegeKey(up1.getKeyLong()).size());

	}

}

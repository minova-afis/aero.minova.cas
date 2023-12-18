package aero.minova.cas.app.extension;

import org.springframework.stereotype.Component;

import aero.minova.cas.service.model.UserGroup;
import jakarta.annotation.PostConstruct;

@Component
public class UserGroupExtension extends BaseExtension<UserGroup> {

	@PostConstruct
	public void setup() {
		// Berechtigung für Lookups
		authorizationService.findOrCreateUserPrivilege("xvcasUserIndex2");
		authorizationService.findOrCreateUserPrivilege("xvcasUsersIndex2");

		super.setup(UserGroup.class);
	}
}
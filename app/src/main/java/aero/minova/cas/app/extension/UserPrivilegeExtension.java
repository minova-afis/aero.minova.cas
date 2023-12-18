package aero.minova.cas.app.extension;

import org.springframework.stereotype.Component;

import aero.minova.cas.service.model.UserPrivilege;
import jakarta.annotation.PostConstruct;

@Component
public class UserPrivilegeExtension extends BaseExtension<UserPrivilege> {

	@PostConstruct
	public void setup() {
		super.setup(UserPrivilege.class);
	}
}
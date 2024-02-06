package aero.minova.cas.app.extension;

import org.springframework.stereotype.Component;

import aero.minova.cas.service.model.UserPrivilege;
import jakarta.annotation.PostConstruct;

@Component
public class UserPrivilegeExtension extends BaseExtension<UserPrivilege> {

	@PostConstruct
	void setPrefix() {
		viewPrefix = "xvcas";
		procedurePrefix = "xpcas";
		tablePrefix = "xtcas";
		super.basicSetup();
	}
}
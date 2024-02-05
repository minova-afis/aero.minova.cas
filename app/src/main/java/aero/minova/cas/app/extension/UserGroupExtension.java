package aero.minova.cas.app.extension;

import org.springframework.stereotype.Component;

import aero.minova.cas.service.model.UserGroup;
import jakarta.annotation.PostConstruct;

@Component
public class UserGroupExtension extends BaseExtension<UserGroup> {

	@PostConstruct
	void setPrefix() {
		viewPrefix = "xvcas";
		procedurePrefix = "xpcas";
		tablePrefix = "xtcas";
		super.basicSetup();
	}
}

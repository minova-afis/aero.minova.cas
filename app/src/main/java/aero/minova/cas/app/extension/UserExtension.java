package aero.minova.cas.app.extension;

import org.springframework.stereotype.Component;

import aero.minova.cas.service.model.User;
import jakarta.annotation.PostConstruct;

@Component
public class UserExtension extends BaseExtension<User> {

	@PostConstruct
	void setPrefix() {
		viewPrefix = "xvcas";
		procedurePrefix = "xpcas";
		tablePrefix = "xtcas";
		super.basicSetup();
	}
}
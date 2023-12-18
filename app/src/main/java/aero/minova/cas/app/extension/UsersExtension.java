package aero.minova.cas.app.extension;

import org.springframework.stereotype.Component;

import aero.minova.cas.service.model.Users;
import jakarta.annotation.PostConstruct;

@Component
public class UsersExtension extends BaseExtension<Users> {

	@PostConstruct
	public void setup() {
		allowDuplicateMatchcodes = true;
		super.setup(Users.class);
	}

	// TODO: Doppelten Nutzernamen vermeiden
}
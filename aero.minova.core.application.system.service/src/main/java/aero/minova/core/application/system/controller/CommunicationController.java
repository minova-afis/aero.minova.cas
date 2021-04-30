package aero.minova.core.application.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.domain.PingResponse;

@RestController
public class CommunicationController {

	@Autowired
	SqlProcedureController procedureController;

	/**
	 * Hiermit kann geprüft werden, ob die Kommunikation mit und die Anmeldung an den CAS funktioniert.
	 * 
	 * @return PingResponse Diese Antwort signalisiert, dass es funktioniert hat.
	 */
	@GetMapping(value = "ping", produces = "application/json")
	public PingResponse executePing() {
		return new PingResponse();
	}

	@PostMapping(value = "loadPrivileges")
	public void loadPrivileges() throws Exception {
		// Abfrage mur für jUnit-Tests, da dabei Authentication = null
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			procedureController.loadPrivileges(authentication.getName(), (List<GrantedAuthority>) authentication.getAuthorities());
		} else {
			throw new IllegalArgumentException("No User found, please login");
		}
	}
}

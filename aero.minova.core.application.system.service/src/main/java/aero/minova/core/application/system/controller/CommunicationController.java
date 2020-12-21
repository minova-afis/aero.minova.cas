package aero.minova.core.application.system.controller;

import aero.minova.core.application.system.domain.PingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommunicationController {
	/**
	 * Hiermit kann geprüft werden, ob die Kommunikation mit und die Anmeldung an den CAS funktioniert.
	 */
	@GetMapping(value = "ping", produces = "application/json")
	public PingResponse executePing() {
		return new PingResponse();
	}
}

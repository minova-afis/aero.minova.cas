package aero.minova.core.application.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.domain.PingResponse;

@RestController
public class CommunicationController {
	/**
	 * Hiermit kann geprüft werden, ob die Kommunikation mit und die Anmeldung an den CAS funktioniert.
	 * 
	 * @return PingResponse Diese Antwort signalisiert, dass es funktioniert hat.
	 */
	@GetMapping(value = "ping", produces = "application/json")
	public PingResponse executePing() {
		return new PingResponse();
	}
}

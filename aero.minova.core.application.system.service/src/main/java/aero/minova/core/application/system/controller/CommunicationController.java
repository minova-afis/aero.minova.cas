package aero.minova.core.application.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.service.SecurityService;
import domain.PingResponse;
import domain.Table;

@RestController
public class CommunicationController {

	@Value("${server.servlet.context-path:}")
	String homePath;

	@Autowired
	SecurityService securityService;

	@Autowired
	SqlProcedureController spc;

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
		try {
			// Abfrage mur für jUnit-Tests, da dabei Authentication = null
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null) {
				securityService.loadPrivileges(authentication.getName(), (List<GrantedAuthority>) authentication.getAuthorities());
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("No User found, please login");
		}
	}

	/**
	 * Empfängt das Signal von der Web-Oberfläche und übersetzt es in die Setup-Table. Kehrt auf die Hauptseite zurück, wenn das Setup durchgelaufen ist.
	 * 
	 * @throws Exception
	 *             Wenn beim Setup ein Fehler auftritt, zum Beispiel, wenn eine Prozedur fehlerhaft war.
	 */
	@RequestMapping(value = "/setup", method = RequestMethod.POST)
	public void setup(HttpServletResponse httpServletResponse) throws Exception {
		Table setupTable = new Table();
		setupTable.setName("setup");
		try {
			spc.executeProcedure(setupTable);
		} catch (Exception e) {
			httpServletResponse.setHeader("Location", homePath + "/setupError");
			httpServletResponse.setStatus(302);
		}
		httpServletResponse.setHeader("Location", homePath + "/setupSuccess");
		httpServletResponse.setStatus(302);
	}
}

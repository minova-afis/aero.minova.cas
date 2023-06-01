package aero.minova.cas.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.PingResponse;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.service.SecurityService;
import aero.minova.cas.sql.SystemDatabase;

@RestController
public class CommunicationController {

	@Value("${server.servlet.context-path:}")
	String homePath;

	@Autowired
	SqlProcedureController spc;

	@Autowired
	SecurityService securityService;

	@Autowired
	CustomLogger customLogger;
	
	@Autowired
	public SystemDatabase database;

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
			securityService.loadAllPrivileges();
		} catch (Exception e) {
			customLogger.logError("Erorr while trying to load privileges!", e);
			throw new RuntimeException(e);
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
		boolean success = true;
		try {
			database.getConnection().createStatement().execute("set ANSI_WARNINGS off");
			spc.executeProcedure(setupTable);
			database.getConnection().createStatement().execute("set ANSI_WARNINGS on");
		} catch (Exception e) {
			success = false;
		}
		if (success) {
			httpServletResponse.setHeader("Location", homePath + "/setupSuccess");
			httpServletResponse.setStatus(302);
		} else {
			httpServletResponse.setHeader("Location", homePath + "/setupError");
			httpServletResponse.setStatus(302);
		}
	}
}

package aero.minova.cas.controller;

import cas.openapi.api.server.api.LoadPrivilegesApi;
import cas.openapi.api.server.api.PingApi;
import cas.openapi.api.server.api.SetupApi;
import cas.openapi.api.server.model.PingEntry;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.cas.CustomLogger;
import aero.minova.cas.api.domain.Table;
import aero.minova.cas.service.SecurityService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CommunicationController implements PingApi, LoadPrivilegesApi, SetupApi {
	private final SqlProcedureController spc;
	private final SecurityService securityService;
	private final CustomLogger customLogger;

	/**
	 * Hiermit kann geprüft werden, ob die Kommunikation mit und die Anmeldung an den CAS funktioniert.
	 *
	 * @return PingResponse Diese Antwort signalisiert, dass es funktioniert hat.
	 */
	@Override
	public ResponseEntity<PingEntry> ping() {
		return ResponseEntity.ok(new PingEntry().returnCode(0));
	}

	@Override
	public ResponseEntity<Void> loadPrivileges() {
		try {
			securityService.loadAllPrivileges();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			customLogger.logError("Error while trying to load privileges!", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Empfängt das Signal von der Web-Oberfläche und übersetzt es in die Setup-Table. Kehrt auf die Hauptseite zurück, wenn das Setup durchgelaufen ist.
	 */
	@Override
	public ResponseEntity<Void> setup() {
		Table setupTable = new Table();
		setupTable.setName("setup");

		try {
			spc.executeProcedure(setupTable);
			return ResponseEntity.status(HttpStatus.FOUND).location(generateResourceUriWithLastSegmentReplacedSuffix("setupSuccess")).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.FOUND).location(generateResourceUriWithLastSegmentReplacedSuffix("setupError")).build();
		}
	}

	private URI generateResourceUriWithLastSegmentReplacedSuffix(String pathSegment) {
		return ServletUriComponentsBuilder //
				.fromCurrentRequest() //
				.replaceQuery("") //
				.path("/../{pathSegment}") //
				.buildAndExpand(pathSegment) //
				.normalize() //
				.toUri();
	}
}

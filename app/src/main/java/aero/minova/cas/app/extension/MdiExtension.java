package aero.minova.cas.app.extension;

import org.springframework.stereotype.Component;

import aero.minova.cas.service.model.Mdi;
import jakarta.annotation.PostConstruct;

@Component
public class MdiExtension extends BaseExtension<Mdi> {

	@PostConstruct
	public void setupPrivileges() {
		authorizationService.findOrCreateUserPrivilege("xtcasMdiType"); // Berechtigung für Lookup

		viewPrefix = "xvcas";
		procedurePrefix = "xpcas";
		tablePrefix = "xtcas";
		super.basicSetup();
	}
}
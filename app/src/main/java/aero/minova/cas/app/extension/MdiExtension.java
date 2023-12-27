package aero.minova.cas.app.extension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aero.minova.cas.service.MdiService;
import aero.minova.cas.service.model.Mdi;
import jakarta.annotation.PostConstruct;

@Component
public class MdiExtension extends BaseExtension<Mdi> {

	@Autowired
	MdiService mdiService;

	@PostConstruct
	public void setup() {
		authorizationService.findOrCreateUserPrivilege("xtcasMdiType"); // Berechtigung für Lookup
	}

}
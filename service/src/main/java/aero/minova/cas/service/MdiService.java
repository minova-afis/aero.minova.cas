package aero.minova.cas.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aero.minova.cas.service.model.Mdi;
import aero.minova.cas.service.model.MdiType;
import aero.minova.cas.service.repository.MdiRepository;
import aero.minova.cas.service.repository.MdiTypeRepository;

@Service
public class MdiService extends BaseService<Mdi> {

	@Autowired
	MdiTypeRepository mdiTypeRepository;

	@Autowired
	MdiRepository mdiRepository;

	@Autowired
	AuthorizationService authorizationService;

	/**
	 * Äquivalent zum Einspielen von xtcasMdiType.form.xml über setup, dort werden die 3 Typen auch angelegt
	 */
	public void setupMdiTypes() {

		// Zugriff auf xtcasMdi muss erlaubt sein
		authorizationService.findOrCreateUserPrivilege("xtcasMdi");

		if (!mdiTypeRepository.findByLastActionGreaterThan(0).isEmpty()) {
			// Wenn es schon MdiTypes gibt muss nichts getan werden
			return;
		}

		MdiType mdiType = new MdiType();
		mdiType.setKeyLong(1);
		mdiType.setKeyText("form");
		mdiType.setDescription("Form of Menu");
		mdiTypeRepository.save(mdiType);

		mdiType = new MdiType();
		mdiType.setKeyLong(2);
		mdiType.setKeyText("menu");
		mdiType.setDescription("Menu of WFC");
		mdiTypeRepository.save(mdiType);

		mdiType = new MdiType();
		mdiType.setKeyLong(3);
		mdiType.setKeyText("application");
		mdiType.setDescription("General Application Info");
		mdiTypeRepository.save(mdiType);
	}

	/**
	 * Äquivalent zu initMdi.sql, das bei Setup eingespielt wird.
	 */
	public void setupCASMdi() {

		// Zuerst muss sichergestellt sein, dass die MdiTypes angelegt sind
		setupMdiTypes();

		// General Application Info
		Mdi mdi = new Mdi("CAS", //
				"@CAS", //
				null, //
				0, //
				getMdiTypeByKeyLong(3), //
				"aero.minova.cas", //
				null);
		mdi.setKeyText(null);
		saveMdi(mdi);

		// Config Menu
		mdi = new Mdi(null, //
				"@cas.config", //
				null, //
				10, //
				getMdiTypeByKeyLong(2), //
				"aero.minova.cas", //
				"admin");
		mdi.setKeyText("config");
		saveMdi(mdi);

		mdi = new Mdi("ServiceProperties", //
				"@xtcasServiceProperties", //
				"config", //
				10, //
				getMdiTypeByKeyLong(1), //
				"aero.minova.cas", //
				"admin");
		mdi.setKeyText("ServiceProperties");
		saveMdi(mdi);

		// CAS Menu
		mdi = new Mdi(null, //
				"@CAS", //
				null, //
				20, //
				getMdiTypeByKeyLong(2), //
				"aero.minova.cas", //
				"admin");
		mdi.setKeyText("CAS");
		saveMdi(mdi);

		mdi = new Mdi("ColumnSecurity", //
				"@xtcasColumnSecurity", //
				"CAS", //
				11, //
				getMdiTypeByKeyLong(1), //
				"aero.minova.cas", //
				"admin");
		mdi.setKeyText("ColumnSecurity");
		saveMdi(mdi);

		mdi = new Mdi("UserGroup", //
				"@xtcasUserGroup", //
				"CAS", //
				12, //
				getMdiTypeByKeyLong(1), //
				"aero.minova.cas", //
				"admin");
		mdi.setKeyText("UserGroup");
		saveMdi(mdi);

		mdi = new Mdi("UserPrivilege", //
				"@xtcasUserPrivilege", //
				"CAS", //
				13, //
				getMdiTypeByKeyLong(1), //
				"aero.minova.cas", //
				"admin");
		mdi.setKeyText("UserPrivilege");
		saveMdi(mdi);

		mdi = new Mdi("Menu", //
				"@xtcasMdi", //
				"CAS", //
				14, //
				getMdiTypeByKeyLong(1), //
				"aero.minova.cas", //
				"admin");
		mdi.setKeyText("Mdi");
		saveMdi(mdi);

		mdi = new Mdi("LDAPUser", //
				"@xtcasUser", //
				"CAS", //
				15, //
				getMdiTypeByKeyLong(1), //
				"aero.minova.cas", //
				"admin");
		mdi.setKeyText("User");
		saveMdi(mdi);

		mdi = new Mdi("DBUser", //
				"@xtcasUsers", //
				"CAS", //
				16, //
				getMdiTypeByKeyLong(1), //
				"aero.minova.cas", //
				"admin");
		mdi.setKeyText("Users");
		saveMdi(mdi);
	}

	public MdiType getMdiTypeByKeyLong(int id) {
		Optional<MdiType> findById = mdiTypeRepository.findById(id);
		if (findById.isPresent()) {
			return findById.get();
		}
		return null;
	}

	/**
	 * Speichert die Mdi wenn möglich und gibt sie zurück. Wenn nicht gespeichert werden konnte wird null zurückgegeben
	 * 
	 * @param mdi
	 * @return
	 */
	public Mdi saveMdi(Mdi mdi) {
		try {
			return save(mdi);
		} catch (RuntimeException e) {
			// Wenn nicht gespeichert werden konnte null zurückgeben
		}
		return null;
	}

	@Override
	public Mdi save(Mdi mdi) {

		// Es darf nur genau einen Mdi Eintrag mit Typ 3 (Generelle Information) geben
		if (mdi.getMdiType().getKeyLong() == 3 && !mdiRepository.findByMdiTypeKeyLongAndLastActionGreaterThan(3, 0).isEmpty()) {
			throw new RuntimeException("@msg.OnlyOneGeneralInformationInMDI");
		}

		if (mdi.getMdiType().getKeyLong() != 1) {
			// Nur die Masken selbst sollen SecurityTokens haben, vgl xpcasInitMdi
			mdi.setSecurityToken(null);
		}

		return super.save(mdi);
	}

}

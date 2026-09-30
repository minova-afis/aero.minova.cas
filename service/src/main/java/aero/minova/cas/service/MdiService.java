package aero.minova.cas.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aero.minova.cas.service.model.Mdi;
import aero.minova.cas.service.model.MdiType;
import aero.minova.cas.service.repository.MdiRepository;
import aero.minova.cas.service.repository.MdiTypeRepository;
import aero.minova.cas.sql.SystemDatabase;
import jakarta.persistence.EntityNotFoundException;

@Service
public class MdiService extends BaseService<Mdi> {

	@Autowired
	MdiTypeRepository mdiTypeRepository;

	@Autowired
	MdiRepository mdiRepository;

	@Autowired
	AuthorizationService authorizationService;

	@Autowired
	SystemDatabase systemDatabase;

	/**
	 * Äquivalent zum Einspielen von xtcasMdiType.table.xml über setup, dort werden die 3 Typen auch angelegt.
	 *
	 * KeyLong wird hier bewusst fest vorgegeben, genau wie im &lt;values&gt;-Block von xtcasMdiType.table.xml. Da die
	 * Spalte trotzdem als IDENTITY definiert ist, lehnen sowohl Hibernates persist() als auch merge() eine bereits
	 * vorgegebene ID auf einer IDENTITY-Spalte ab; die Zeilen müssen daher per direktem Insert angelegt werden, bei
	 * SQL Server zusätzlich umrahmt von SET IDENTITY_INSERT (analog zu XmlDatabaseTable#generateUpdateValues, das
	 * genau dies beim Einspielen von xtcasMdiType.table.xml über setup macht).
	 */
	public void setupMdiTypes() {

		// Zugriff auf xtcasMdi muss erlaubt sein
		authorizationService.findOrCreateUserPrivilege("xtcasMdi");

		if (!mdiTypeRepository.findByLastActionGreaterThan(0).isEmpty()) {
			// Wenn es schon MdiTypes gibt muss nichts getan werden
			return;
		}

		boolean isSQLServer = systemDatabase.isSQLDatabase();
		try (Connection connection = systemDatabase.getConnection()) {
			if (isSQLServer) {
				connection.createStatement().execute("SET IDENTITY_INSERT xtcasMdiType ON");
			}
			insertMdiType(connection, 1, "form", "Form of Menu");
			insertMdiType(connection, 2, "menu", "Menu of WFC");
			insertMdiType(connection, 3, "application", "General Application Info");
			if (isSQLServer) {
				connection.createStatement().execute("SET IDENTITY_INSERT xtcasMdiType OFF");
			}
			connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void insertMdiType(Connection connection, int keyLong, String keyText, String description) throws SQLException {
		String sql = "INSERT INTO xtcasMdiType (KeyLong, KeyText, Description, LastUser, LastDate, LastAction) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, keyLong);
			statement.setString(2, keyText);
			statement.setString(3, description);
			statement.setString(4, BaseService.getCurrentUser());
			statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
			statement.setInt(6, 1);
			statement.executeUpdate();
		}
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
		throw new EntityNotFoundException("@msg.EntityNotFound");
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

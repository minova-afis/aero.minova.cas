package aero.minova.cas.service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import aero.minova.cas.service.model.Authorities;
import aero.minova.cas.service.model.LuUserPrivilegeUserGroup;
import aero.minova.cas.service.model.UserGroup;
import aero.minova.cas.service.model.UserPrivilege;
import aero.minova.cas.service.model.Users;
import aero.minova.cas.service.repository.AuthoritiesRepository;
import aero.minova.cas.service.repository.LuUserPrivilegeUserGroupRepository;
import aero.minova.cas.service.repository.UserGroupRepository;
import aero.minova.cas.service.repository.UserPrivilegeRepository;
import aero.minova.cas.service.repository.UsersRepository;

@Controller
public class AuthorizationService {

	@Autowired
	AuthoritiesRepository authoritiesRepository;

	@Autowired
	LuUserPrivilegeUserGroupRepository luUserPrivilegeUserGroupRepository;

	@Autowired
	UserGroupRepository userGroupRepository;

	@Autowired
	UserPrivilegeRepository userPrivilegeRepository;

	@Autowired
	UsersRepository usersRepository;

	/**
	 * Erstellt die Insert/Update/Read/Delete Prozedur-Berechtigungen und die Index-View
	 * 
	 * @param maskname
	 * @param procedurePrefix
	 * @param viewPrefix
	 */
	public void createDefaultPrivilegesForMask(String maskname, String procedurePrefix, String viewPrefix) {
		findOrCreateUserPrivilege(procedurePrefix + "Insert" + maskname);
		findOrCreateUserPrivilege(procedurePrefix + "Update" + maskname);
		findOrCreateUserPrivilege(procedurePrefix + "Read" + maskname);
		findOrCreateUserPrivilege(procedurePrefix + "Delete" + maskname);

		findOrCreateUserPrivilege(viewPrefix + maskname + "Index");
	}

	/**
	 * Trägt die Berechtigung in die Tabelle xtCasUserPrivilege ein, wenn sie noch nicht existiert. Z.B. "xpcorInsertMovement", "xvcorMovementIndex"
	 * 
	 * @param privilegeName
	 */
	public UserPrivilege findOrCreateUserPrivilege(String privilegeName) {
		return userPrivilegeRepository.findByKeyText(privilegeName).orElseGet(() -> {
			UserPrivilege privilege = new UserPrivilege();
			privilege.setKeyText(privilegeName);
			userPrivilegeRepository.save(privilege);
			return privilege;
		});
	}

	/**
	 * Erstellt einen Nutzer, der alle Berechtigungen hat (oder updated die Berechtigungen). Die Berechtigungen müssen bereits in der Tabelle xtCasUserPrivilege
	 * eingetragen sein (Methode {@link #findOrCreateUserPrivilege(String privilegeName)}). Existiert der Benutzer bereits, werden alle fehlenden Berechtigungen
	 * erteilt, das Passwort wird NICHT aktualisiert
	 * 
	 * @param username
	 * @param encryptedPassword
	 */
	public void createOrUpdateAdminUser(String username, String encryptedPassword) {

		findOrCreateUser(username, encryptedPassword);

		UserGroup userGroup = createOrUpdateUserGroup("admin", "#admin");

		findOrCreateAuthority(username, "admin");

		for (UserPrivilege priv : userPrivilegeRepository.findAllWithLastActionGreaterZero()) {
			findOrCreateLuUserPrivilegeUserGroup(userGroup, priv);
		}
	}

	/**
	 * Erstellt einen neuen Benutzer (für datasource="database") wenn noch kein Nutzer mit dem Namen existiert. Ansonsten wird das Passwort auch NICHT
	 * aktualisiert.
	 * 
	 * @param username
	 * @param encryptedPassword
	 * @return
	 */
	public Users findOrCreateUser(String username, String encryptedPassword) {
		return usersRepository.findByUsername(username).orElseGet(() -> {
			Users newUser = new Users();
			newUser.setUsername(username);
			newUser.setPassword(encryptedPassword);
			usersRepository.save(newUser);
			return newUser;
		});
	}

	/**
	 * Aktualisiert das Passwort des Benutzers (für datasource="database")
	 * 
	 * @param username
	 * @param encryptedPassword
	 * @return
	 * @throws NoSuchElementException
	 *             wenn der Benutzer nicht existiert
	 */
	public Users updateUserPassword(String username, String encryptedPassword) throws NoSuchElementException {
		Users user = usersRepository.findByUsername(username).get();

		user.setPassword(encryptedPassword);
		usersRepository.save(user);

		return user;
	}

	/**
	 * Erstellt eine neue UserGroup, wenn noch keine mit diesem KeyText existiert. Die securitytokens (getrennt mit #) werden an evtl. bestehende angehängt
	 * 
	 * @param keyText
	 * @param securitytoken
	 * @return
	 */
	public UserGroup createOrUpdateUserGroup(String keyText, String securitytoken) {
		UserGroup usergroup = userGroupRepository.findByKeyText(keyText).orElseGet(() -> {
			UserGroup group = new UserGroup();
			group.setSecuritytoken("");
			group.setKeyText(keyText);
			return group;
		});

		// Neue Tokens anhängen, dabei aber keine duplikate erstellen
		List<String> newTokens = Arrays.asList(securitytoken.split("#"));
		List<String> oldTokens = Arrays.asList(usergroup.getSecuritytoken().split("#"));
		for (String newToken : newTokens) {
			if (!oldTokens.contains(newToken)) {
				usergroup.setSecuritytoken(usergroup.getSecuritytoken() + "#" + newToken);
			}
		}

		userGroupRepository.save(usergroup);
		return usergroup;
	}

	/**
	 * Erstellt eine Authority (Zuorndung zwischen Benutzer und BenutzerGruppe), wenn diese noch nicht existiert
	 * 
	 * @param username
	 * @param authority
	 * @return
	 */
	public Authorities findOrCreateAuthority(String username, String authorityName) {
		return authoritiesRepository.findByUsernameAndAuthority(username, authorityName).orElseGet(() -> {
			Authorities authority = new Authorities();
			authority.setUsername(username);
			authority.setAuthority(authorityName);
			authoritiesRepository.save(authority);
			return authority;
		});
	}

	/**
	 * Erstellt einen Eintrag in xtcasLuUserPrivilegeUser, damit die Gruppe Rechte auf das Privileg (Prozedur, View, ...) hat, wenn noch nicht vorhanden
	 * 
	 * @param userGroup
	 * @param priv
	 */
	public LuUserPrivilegeUserGroup findOrCreateLuUserPrivilegeUserGroup(UserGroup userGroup, UserPrivilege priv) {
		return luUserPrivilegeUserGroupRepository.findByPrivilegeAndGroup(priv.getKeyLong(), userGroup.getKeyLong()).orElseGet(() -> {
			LuUserPrivilegeUserGroup lu = new LuUserPrivilegeUserGroup();
			lu.setUsergroup(userGroup);
			lu.setUserprivilege(priv);
			luUserPrivilegeUserGroupRepository.save(lu);
			return lu;
		});
	}
}

package aero.minova.cas.controller;

import java.util.Arrays;
import java.util.List;

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
public class AuthorizationController {

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
	 * Trägt die Berechtigung in die Tabelle xtCasUserPrivilege ein, wenn sie noch nicht existiert. Z.B. "xpcorInsertMovement", "xvcorMovementIndex"
	 * 
	 * @param privilegeName
	 */
	public UserPrivilege createUserPrivilege(String privilegeName) {
		UserPrivilege privilege = userPrivilegeRepository.findByKeyText(privilegeName);

		if (privilege == null) {
			privilege = new UserPrivilege();
			privilege.setKeyText(privilegeName);
			userPrivilegeRepository.save(privilege);
		}

		return privilege;
	}

	/**
	 * Erstellt einen Nutzer, der alle Berechtigungen hat (oder updated die Berechtigungen). Die Berechtigungen müssen bereits in der Tabelle xtCasUserPrivilege
	 * eingetragen sein (Methode {@link #createUserPrivilege(String privilegeName)}). Existiert der Benutzer bereits, werden alle fehlenden Berechtigungen
	 * erteilt
	 * 
	 * @param username
	 * @param encryptedPassword
	 */
	public void createAdminUser(String username, String encryptedPassword) {

		// 1. Neuen "Users" erstellen
		createUser(username, encryptedPassword);

		// 2. Admin-Gruppe erstellen
		UserGroup userGroup = createUserGroup("admin", "#admin");

		// 3. Neuen Nutzer der Admin-Gruppe zuweisen
		createAuthority(username, "admin");

		// 4: Admin-Gruppe alle Rechte zuweisen
		for (UserPrivilege priv : userPrivilegeRepository.findAllWithLastActionGreaterZero()) {
			createLuUserPrivilegeUserGroup(userGroup, priv);
		}
	}

	/**
	 * Erstellt einen neuen Benutzer (für datasource="database") wenn noch kein Nutzer mit dem Namen existiert. Ansonsten wird das Passwort aktualisiert.
	 * 
	 * @param username
	 * @param encryptedPassword
	 * @return
	 */
	public Users createUser(String username, String encryptedPassword) {
		Users newUser = usersRepository.findByUsername(username);

		if (newUser == null) {
			newUser = new Users();
			newUser.setUsername(username);
		}

		newUser.setPassword(encryptedPassword);
		usersRepository.save(newUser);
		return newUser;
	}

	/**
	 * Erstellt eine neue UserGroup, wenn noch keine mit diesem KeyText existiert. Die securitytokens (getrennt mit #) werden an evtl. bestehende angehängt
	 * 
	 * @param keyText
	 * @param securitytoken
	 * @return
	 */
	public UserGroup createUserGroup(String keyText, String securitytoken) {
		UserGroup usergroup = userGroupRepository.findByKeyText(keyText);

		if (usergroup == null) {
			usergroup = new UserGroup();
			usergroup.setSecuritytoken("");
			usergroup.setKeyText(keyText);
		}

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
	public Authorities createAuthority(String username, String authorityName) {
		Authorities authority = authoritiesRepository.findByUsernameAndAuthority(username, authorityName);

		if (authority == null) {
			authority = new Authorities();
			authority.setUsername(username);
			authority.setAuthority(authorityName);
			authoritiesRepository.save(authority);
		}

		return authority;
	}

	/**
	 * Erstellt einen Eintrag in xtcasLuUserPrivilegeUser, damit die Gruppe Rechte auf das Privileg (Prozedur, View, ...) hat, wenn noch nicht vorhanden
	 * 
	 * @param userGroup
	 * @param priv
	 */
	public LuUserPrivilegeUserGroup createLuUserPrivilegeUserGroup(UserGroup userGroup, UserPrivilege priv) {
		LuUserPrivilegeUserGroup lu = luUserPrivilegeUserGroupRepository.findByPrivilegeAndGroup(priv.getKeyLong(), userGroup.getKeyLong());

		if (lu == null) {
			lu = new LuUserPrivilegeUserGroup();
			lu.setUsergroup(userGroup);
			lu.setUserprivilege(priv);
			luUserPrivilegeUserGroupRepository.save(lu);
		}

		return lu;
	}
}

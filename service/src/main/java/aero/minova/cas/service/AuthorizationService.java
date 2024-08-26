package aero.minova.cas.service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;

import aero.minova.cas.CustomLogger;
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

@Service
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

	@Autowired
	UsersService usersService;

	@Autowired
	protected CustomLogger logger;

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
	 * Gibt die Insert/Update/Read/Delete Prozedur-Berechtigungen und die Index-View zu der gegebenen Gruppe. Existieren die Berechtigungen noch nicht werden
	 * sie angelegt.
	 * 
	 * @param maskname
	 * @param procedurePrefix
	 * @param viewPrefix
	 */
	public void giveDefaultPrivilegesForMask(UserGroup group, String maskname, String procedurePrefix, String viewPrefix) {
		findOrCreateLuUserPrivilegeUserGroup(group, findOrCreateUserPrivilege(procedurePrefix + "Insert" + maskname));
		findOrCreateLuUserPrivilegeUserGroup(group, findOrCreateUserPrivilege(procedurePrefix + "Update" + maskname));
		findOrCreateLuUserPrivilegeUserGroup(group, findOrCreateUserPrivilege(procedurePrefix + "Read" + maskname));
		findOrCreateLuUserPrivilegeUserGroup(group, findOrCreateUserPrivilege(procedurePrefix + "Delete" + maskname));

		findOrCreateLuUserPrivilegeUserGroup(group, findOrCreateUserPrivilege(viewPrefix + maskname + "Index"));
	}

	/**
	 * Trägt die Berechtigung in die Tabelle xtCasUserPrivilege ein, wenn sie noch nicht existiert. Z.B. "xpcorInsertMovement", "xvcorMovementIndex"
	 * 
	 * @param privilegeName
	 */
	public UserPrivilege findOrCreateUserPrivilege(String privilegeName) {
		try {
			return !userPrivilegeRepository.findByKeyTextAndLastActionGreaterThan(privilegeName, 0).isEmpty()
					? userPrivilegeRepository.findByKeyTextAndLastActionGreaterThan(privilegeName, 0).get(0)
					: createUserPrivilege(privilegeName);
		} catch (InvalidDataAccessResourceUsageException e) {
			logger.logError("Error while trying to set up privilege " + privilegeName
					+ ". Consider executing setup or enabling spring.jpa.hibernate.ddl-auto=update in the properties", e);
		}
		return null;
	}

	private UserPrivilege createUserPrivilege(String privilegeName) {
		UserPrivilege privilege = new UserPrivilege();
		privilege.setKeyText(privilegeName);
		return userPrivilegeRepository.save(privilege);
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

		for (UserPrivilege priv : userPrivilegeRepository.findByLastActionGreaterThan(0)) {
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
		return usersRepository.findByUsernameAndLastActionGreaterThan(username, 0).orElseGet(() -> {
			Users newUser = new Users();
			newUser.setUsername(username);
			newUser.setPassword(encryptedPassword);
			newUser = usersService.save(newUser);
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
		Users user = usersRepository.findByUsernameAndLastActionGreaterThan(username, 0).get();

		user.setPassword(encryptedPassword);
		user = usersRepository.save(user);

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
		UserGroup usergroup = !userGroupRepository.findByKeyTextAndLastActionGreaterThan(keyText, 0).isEmpty()
				? userGroupRepository.findByKeyTextAndLastActionGreaterThan(keyText, 0).get(0)
				: createUserGroup(keyText);

		// Neue Tokens anhängen, dabei aber keine duplikate erstellen
		List<String> newTokens = Arrays.asList(securitytoken.split("#"));
		List<String> oldTokens = Arrays.asList(usergroup.getSecurityToken().split("#"));
		for (String newToken : newTokens) {
			if (!oldTokens.contains(newToken)) {
				usergroup.setSecurityToken(usergroup.getSecurityToken() + "#" + newToken);
			}
		}

		return userGroupRepository.save(usergroup);
	}

	private UserGroup createUserGroup(String keyText) {
		UserGroup group = new UserGroup();
		group.setSecurityToken("");
		group.setKeyText(keyText);
		return group;
	}

	/**
	 * Erstellt eine Authority (Zuorndung zwischen Benutzer und BenutzerGruppe), wenn diese noch nicht existiert
	 * 
	 * @param username
	 * @param authority
	 * @return
	 */
	public Authorities findOrCreateAuthority(String username, String authorityName) {
		return authoritiesRepository.findByUsernameAndAuthorityAndLastActionGreaterThan(username, authorityName, 0).orElseGet(() -> {
			Authorities authority = new Authorities();
			authority.setUsername(username);
			authority.setAuthority(authorityName);
			authority = authoritiesRepository.save(authority);
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
		return luUserPrivilegeUserGroupRepository
				.findByUserPrivilegeKeyLongAndUserGroupKeyLongAndLastActionGreaterThan(priv.getKeyLong(), userGroup.getKeyLong(), 0).orElseGet(() -> {
					LuUserPrivilegeUserGroup lu = new LuUserPrivilegeUserGroup();
					lu.setUserGroup(userGroup);
					lu.setUserPrivilege(priv);
					lu = luUserPrivilegeUserGroupRepository.save(lu);
					return lu;
				});
	}
}

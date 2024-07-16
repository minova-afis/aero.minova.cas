package aero.minova.cas.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aero.minova.cas.service.model.Authorities;
import aero.minova.cas.service.model.UserGroup;
import aero.minova.cas.service.model.Users;
import aero.minova.cas.service.repository.UsersRepository;
import jakarta.annotation.PostConstruct;

@Service
public class UsersService extends BaseService<Users> {

	@Autowired
	AuthoritiesService authoritiesService;

	@Autowired
	UserGroupService userGroupService;

	@PostConstruct
	public void setup() {
		allowDuplicateMatchcodes = true;
	}

	public Users findByUsername(String username) {
		Optional<Users> findByUsername = ((UsersRepository) repository).findByUsername(username);
		if (findByUsername.isEmpty()) {
			throw new RuntimeException("@msg.EntityNotFound");
		}

		return findByUsername.get();
	}

	@Override
	public Users save(Users entity) {

		// Username darf nur einmal vorkommen
		Optional<Users> findByUsername = ((UsersRepository) repository).findByUsername(entity.getUsername());
		if (findByUsername.isPresent() && !Objects.equals(findByUsername.get().getKeyLong(), entity.getKeyLong())) {
			throw new RuntimeException("@msg.DuplicateMatchcodeNotAllowed");
		}

		// Passwort muss verschlüsselt sein
		String newPassword = entity.getPassword();
		if (newPassword != null && (newPassword.length() < 60 || !newPassword.startsWith("$2"))) {
			throw new RuntimeException("@msg.sql.PasswordNotEncrypted");
		}

		// Ggf altes Passwort auslesen und weiterverwenden
		if (newPassword == null && entity.getKeyLong() != null) {
			Users fromDatabase = repository.findById(entity.getKeyLong()).get();
			entity.setPassword(fromDatabase.getPassword());
		}

		return super.save(entity);
	}

	public List<Users> findUsersInUserGroup(int userGroupKey) {
		return findUsersInUserGroup(userGroupService.findEntityById(userGroupKey));
	}

	public List<Users> findUsersInUserGroup(UserGroup userGroup) {

		List<Authorities> authorities = authoritiesService.findByAuthority(userGroup.getKeyText());

		List<Users> res = new ArrayList<>();

		for (Authorities a : authorities) {
			res.add(findByUsername(a.getUsername()));
		}

		return res;
	}

	public List<UserGroup> findUserGroupsOfUser(int usersKey) {
		return findUserGroupsOfUser(findEntityById(usersKey));
	}

	public List<UserGroup> findUserGroupsOfUser(Users user) {

		List<Authorities> authorities = authoritiesService.findByUsername(user.getUsername());

		List<UserGroup> res = new ArrayList<>();

		for (Authorities a : authorities) {
			res.addAll(userGroupService.findEntitiesByKeyText(a.getAuthority()));
		}

		return res;
	}

	public void addUserToUserGroup(int usersKey, int userGroupKey) {
		addUserToUserGroup(findEntityById(usersKey), userGroupService.findEntityById(userGroupKey));
	}

	public void addUserToUserGroup(Users user, UserGroup userGroup) {
		String username = user.getUsername();
		String authority = userGroup.getKeyText();

		// Nur anlegen, wenn User nicht eh schon in der Gruppe ist
		if (!authoritiesService.findByUsernameAndAuthority(username, authority).isPresent()) {
			Authorities a = new Authorities();
			a.setAuthority(authority);
			a.setUsername(username);
			authoritiesService.save(a);
		}

	}

	public void removeUserFromUserGroup(int usersKey, int userGroupKey) {
		String username = findEntityById(usersKey).getUsername();
		String authority = userGroupService.findEntityById(userGroupKey).getKeyText();
		authoritiesService.deleteByUsernameAndAuthority(username, authority);
	}

}

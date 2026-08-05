package aero.minova.cas.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aero.minova.cas.service.model.UserGroup;
import aero.minova.cas.service.repository.UserGroupRepository;

/**
 * Auto-provisions {@code xtcasUserGroup} entries for OIDC roles the first time they're seen, without hitting the
 * database on every request: known role names are cached after boot, and only a cache miss triggers a fresh reload
 * (to pick up groups another instance may have already created) followed by creation if it's still genuinely new.
 */
@Service
public class OidcRoleProvisioningService {

	@Autowired
	UserGroupRepository userGroupRepository;

	@Autowired
	AuthorizationService authorizationService;

	private volatile Set<String> knownRoleGroups;

	public void ensureRolesProvisioned(Collection<String> roleNames) {
		for (String roleName : roleNames) {
			if (roleName == null || roleName.isBlank() || getKnownRoleGroups().contains(roleName)) {
				continue;
			}
			// Reload before creating: another instance may have already created this role's group.
			synchronized (this) {
				Set<String> reloaded = loadKnownRoleGroups();
				if (!reloaded.contains(roleName)) {
					authorizationService.ensureRoleGroupExists(roleName);
					Set<String> updated = new HashSet<>(reloaded);
					updated.add(roleName);
					knownRoleGroups = updated;
				}
			}
		}
	}

	private Set<String> getKnownRoleGroups() {
		Set<String> current = knownRoleGroups;
		return current != null ? current : loadKnownRoleGroups();
	}

	private Set<String> loadKnownRoleGroups() {
		Set<String> groups = userGroupRepository.findByLastActionGreaterThan(0).stream()
				.map(UserGroup::getKeyText).collect(Collectors.toSet());
		knownRoleGroups = groups;
		return groups;
	}
}

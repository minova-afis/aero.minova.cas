package aero.minova.cas.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import aero.minova.cas.service.model.LuUserPrivilegeUserGroup;
import aero.minova.cas.service.repository.LuUserPrivilegeUserGroupRepository;
import jakarta.annotation.PostConstruct;

@Service
public class LuUserPrivilegeUserGroupService extends BaseService<LuUserPrivilegeUserGroup> {

	@PostConstruct
	public void setup() {
		allowDuplicateMatchcodes = true;
	}

	public List<LuUserPrivilegeUserGroup> findByUserPrivilegeKey(int userPrivilegeKey) {
		return ((LuUserPrivilegeUserGroupRepository) repository).findByUserPrivilegeKeyLongAndLastActionGreaterThan(userPrivilegeKey, 0);
	}

	public List<LuUserPrivilegeUserGroup> findByUserGroupKey(int userGroupKey) {
		return ((LuUserPrivilegeUserGroupRepository) repository).findByUserGroupKeyLongAndLastActionGreaterThan(userGroupKey, 0);
	}

	@Override
	public LuUserPrivilegeUserGroup save(LuUserPrivilegeUserGroup entity) {

		// Kombination UserPrivilege/UserGroup darf nur einmal vorkommen
		Optional<LuUserPrivilegeUserGroup> find = ((LuUserPrivilegeUserGroupRepository) repository)
				.findByUserPrivilegeKeyLongAndUserGroupKeyLongAndLastActionGreaterThan(entity.getUserPrivilege().getKeyLong(),
						entity.getUserGroup().getKeyLong(), 0);
		if (find.isPresent() && !Objects.equals(find.get().getKeyLong(), entity.getKeyLong())) {
			throw new RuntimeException("@msg.DuplicateMatchcodeNotAllowed");
		}

		return super.save(entity);
	}
}

package aero.minova.cas.service.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.LuUserPrivilegeUserGroup;

@Repository
public interface LuUserPrivilegeUserGroupRepository extends DataEntityRepository<LuUserPrivilegeUserGroup> {

	public Optional<LuUserPrivilegeUserGroup> findByUserPrivilegeKeyLongAndUserGroupKeyLongAndLastActionGreaterThan(long userprivilegekey, long usergroupkey,
			int lastAction);

}

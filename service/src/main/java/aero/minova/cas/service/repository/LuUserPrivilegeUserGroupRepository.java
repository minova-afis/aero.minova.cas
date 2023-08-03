package aero.minova.cas.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.LuUserPrivilegeUserGroup;

@Repository
public interface LuUserPrivilegeUserGroupRepository extends JpaRepository<LuUserPrivilegeUserGroup, Long> {

	@Query("select lu from LuUserPrivilegeUserGroup lu where lu.lastAction > 0")
	public List<LuUserPrivilegeUserGroup> findAllWithLastActionGreaterZero();

	@Query("select lu from LuUserPrivilegeUserGroup lu where lu.userPrivilege.keyLong = :userprivilegekey and lu.userGroup.keyLong = :usergroupkey and lu.lastAction > 0")
	public Optional<LuUserPrivilegeUserGroup> findByPrivilegeAndGroup(long userprivilegekey, long usergroupkey);

}

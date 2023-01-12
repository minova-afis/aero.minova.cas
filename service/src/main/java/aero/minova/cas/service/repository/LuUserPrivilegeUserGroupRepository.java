package aero.minova.cas.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import aero.minova.cas.service.model.LuUserPrivilegeUserGroup;

public interface LuUserPrivilegeUserGroupRepository extends CrudRepository<LuUserPrivilegeUserGroup, Long> {

	@Query("select lu from LuUserPrivilegeUserGroup lu where lu.lastaction > 0")
	public List<LuUserPrivilegeUserGroup> findAllWithLastActionGreaterZero();

	@Query("select lu from LuUserPrivilegeUserGroup lu where lu.userprivilege.keylong = :userprivilegekey and lu.usergroup.keylong = :usergroupkey and lu.lastaction > 0")
	public Optional<LuUserPrivilegeUserGroup> findByPrivilegeAndGroup(long userprivilegekey, long usergroupkey);

}

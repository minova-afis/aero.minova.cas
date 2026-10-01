package aero.minova.cas.service.repository;

import java.util.List;
import java.util.Optional;

import aero.minova.cas.service.model.LuUserPrivilegeUserGroup;
import org.springframework.stereotype.Repository;

@Repository
public interface LuUserPrivilegeUserGroupRepository extends DataEntityRepository<LuUserPrivilegeUserGroup> {

    public Optional<LuUserPrivilegeUserGroup> findByUserPrivilegeKeyLongAndUserGroupKeyLongAndLastActionGreaterThan(
            long userprivilegekey, long usergroupkey, int lastAction);

    public List<LuUserPrivilegeUserGroup> findByUserPrivilegeKeyLongAndLastActionGreaterThan(
            long userprivilegekey, int lastAction);

    public List<LuUserPrivilegeUserGroup> findByUserGroupKeyLongAndLastActionGreaterThan(
            long usergroupkey, int lastAction);
}

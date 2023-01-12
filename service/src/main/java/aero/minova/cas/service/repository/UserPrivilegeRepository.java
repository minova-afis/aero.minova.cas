package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import aero.minova.cas.service.model.UserPrivilege;

public interface UserPrivilegeRepository extends CrudRepository<UserPrivilege, Long> {

	@Query("select u from UserPrivilege u where u.lastAction > 0")
	public List<UserPrivilege> findAllWithLastActionGreaterZero();

	@Query("select u from UserPrivilege u where u.keyText = :keyText and u.lastAction > 0")
	public UserPrivilege findByKeyText(String keyText);

}

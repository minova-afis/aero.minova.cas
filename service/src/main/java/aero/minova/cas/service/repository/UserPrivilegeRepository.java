package aero.minova.cas.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import aero.minova.cas.service.model.UserPrivilege;

public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, Long> {

	@Query("select u from UserPrivilege u where u.lastAction > 0")
	public List<UserPrivilege> findAllWithLastActionGreaterZero();

	@Query("select u from UserPrivilege u where u.keyText = :keytext and u.lastAction > 0")
	public Optional<UserPrivilege> findByKeyText(String keytext);

}

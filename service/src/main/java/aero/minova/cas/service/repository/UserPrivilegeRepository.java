package aero.minova.cas.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.UserPrivilege;

@Repository
public interface UserPrivilegeRepository extends JpaRepository<UserPrivilege, Long> {

	@Query("select u from UserPrivilege u where u.lastAction > 0")
	public List<UserPrivilege> findAllWithLastActionGreaterZero();

	@Query("select u from UserPrivilege u where u.keyText = :keyText and u.lastAction > 0")
	public Optional<UserPrivilege> findByKeyText(String keyText);

}

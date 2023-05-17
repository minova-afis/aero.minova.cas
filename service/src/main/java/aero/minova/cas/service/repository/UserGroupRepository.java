package aero.minova.cas.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.UserGroup;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

	@Query("select u from UserGroup u where u.lastAction > 0")
	public List<UserGroup> findAllWithLastActionGreaterZero();

	@Query("select u from UserGroup u where u.keyText =:keytext and u.lastAction > 0")
	public Optional<UserGroup> findByKeyText(String keytext);

}

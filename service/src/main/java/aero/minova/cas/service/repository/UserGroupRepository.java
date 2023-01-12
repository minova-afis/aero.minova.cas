package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import aero.minova.cas.service.model.UserGroup;

public interface UserGroupRepository extends CrudRepository<UserGroup, Long> {

	@Query("select u from UserGroup u where u.lastAction > 0")
	public List<UserGroup> findAllWithLastActionGreaterZero();

	@Query("select u from UserGroup u where u.keyText =:keyText and u.lastAction > 0")
	public UserGroup findByKeyText(String keyText);

}

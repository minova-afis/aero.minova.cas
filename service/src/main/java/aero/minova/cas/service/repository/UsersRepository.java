package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import aero.minova.cas.service.model.Users;

public interface UsersRepository extends CrudRepository<Users, Long> {

	@Query("select u from Users u where u.lastaction > 0")
	public List<Users> findAllWithLastActionGreaterZero();

	@Query("select u from Users u where u.username = :username and u.lastaction > 0")
	public Users findByUsername(String username);

}

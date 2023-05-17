package aero.minova.cas.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

	@Query("select u from Users u where u.lastaction > 0")
	public List<Users> findAllWithLastActionGreaterZero();

	@Query("select u from Users u where u.username = :username and u.lastaction > 0")
	public Optional<Users> findByUsername(String username);

}

package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import aero.minova.cas.service.model.Authorities;

public interface AuthoritiesRepository extends CrudRepository<Authorities, Long> {

	@Query("select a from Authorities a where a.lastaction > 0")
	public List<Authorities> findAllWithLastActionGreaterZero();

	@Query("select a from Authorities a where a.username = :username and a.authority = :authority and a.lastaction > 0")
	public Authorities findByUsernameAndAuthority(String username, String authority);

}

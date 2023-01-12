package aero.minova.cas.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.Authorities;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Long> {

	@Query("select a from Authorities a where a.lastaction > 0")
	public List<Authorities> findAllWithLastActionGreaterZero();

	@Query("select a from Authorities a where a.username = :username and a.authority = :authority and a.lastaction > 0")
	public Optional<Authorities> findByUsernameAndAuthority(String username, String authority);

}

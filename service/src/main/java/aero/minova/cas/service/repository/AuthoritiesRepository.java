package aero.minova.cas.service.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.Authorities;

@Repository
public interface AuthoritiesRepository extends DataEntityRepository<Authorities> {

	public Optional<Authorities> findByUsernameAndAuthorityAndLastActionGreaterThan(String username, String authority, int lastAction);

	public default Optional<Authorities> findByUsernameAndAuthority(String username, String authority) {
		return findByUsernameAndAuthorityAndLastActionGreaterThan(username, authority, 0);
	}

}

package aero.minova.cas.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.Authorities;

@Repository
public interface AuthoritiesRepository extends DataEntityRepository<Authorities> {

	public Optional<Authorities> findByUsernameAndAuthorityAndLastActionGreaterThan(String username, String authority, int lastAction);

	public default Optional<Authorities> findByUsernameAndAuthority(String username, String authority) {
		return findByUsernameAndAuthorityAndLastActionGreaterThan(username, authority, 0);
	}

	public List<Authorities> findByUsernameAndLastActionGreaterThan(String username, int lastAction);

	public default List<Authorities> findByUsername(String username) {
		return findByUsernameAndLastActionGreaterThan(username, 0);
	}

	public List<Authorities> findByAuthorityAndLastActionGreaterThan(String authority, int lastAction);

	public default List<Authorities> findByAuthority(String authority) {
		return findByAuthorityAndLastActionGreaterThan(authority, 0);
	}
}

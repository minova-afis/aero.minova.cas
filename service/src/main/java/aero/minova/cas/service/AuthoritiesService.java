package aero.minova.cas.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import aero.minova.cas.service.model.Authorities;
import aero.minova.cas.service.repository.AuthoritiesRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;

@Service
public class AuthoritiesService extends BaseService<Authorities> {

	@PostConstruct
	public void setup() {
		allowDuplicateMatchcodes = true;
	}

	@Override
	public void deleteById(int id) {

		Optional<Authorities> optional = repository.findById(id);
		if (optional.isEmpty()) {
			throw new EntityNotFoundException("@msg.EntityNotFound");
		}

		repository.delete(optional.get());
	}

	@Override
	public Authorities save(Authorities entity) {

		// Kombination aus Username und Authority/Gruppenname darf es nur einmal geben
		Optional<Authorities> authority = findByUsernameAndAuthority(entity.getUsername(), entity.getAuthority());

		if (authority.isPresent() && !Objects.equals(authority.get().getKeyLong(), entity.getKeyLong())) {
			throw new RuntimeException("msg.DuplicateMatchcodeNotAllowed");
		}

		return super.save(entity);
	}

	public Optional<Authorities> findByUsernameAndAuthority(String username, String authority) {
		return ((AuthoritiesRepository) repository).findByUsernameAndAuthority(username, authority);
	}

	public List<Authorities> findByUsername(String username) {
		return ((AuthoritiesRepository) repository).findByUsername(username);
	}

	public List<Authorities> findByAuthority(String authority) {
		return ((AuthoritiesRepository) repository).findByAuthority(authority);
	}

	public void deleteByUsernameAndAuthority(String username, String authority) {
		Optional<Authorities> findByUsernameAndAuthority = ((AuthoritiesRepository) repository).findByUsernameAndAuthority(username, authority);
		if (findByUsernameAndAuthority.isPresent()) {
			deleteById(findByUsernameAndAuthority.get().getKeyLong());
		} else {
			throw new EntityNotFoundException("@msg.EntityNotFound");
		}
	}

}

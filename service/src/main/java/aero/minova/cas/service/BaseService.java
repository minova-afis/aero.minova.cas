package aero.minova.cas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.security.core.context.SecurityContextHolder;

import aero.minova.cas.service.model.DataEntity;
import aero.minova.cas.service.repository.DataEntityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

public abstract class BaseService<E extends DataEntity> {

	@Autowired
	protected DataEntityRepository<E> repository;

	@Autowired
	protected DataSource dataSource;

	protected final Class<E> entityClass;

	@PersistenceContext
	protected EntityManager entityManager;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected boolean allowDuplicateMatchcodes = false;

	@SuppressWarnings("unchecked")
	public BaseService() {
		Class<?>[] resolveTypeArguments = GenericTypeResolver.resolveTypeArguments(getClass(), BaseService.class);
		if (resolveTypeArguments != null) {
			this.entityClass = (Class<E>) resolveTypeArguments[0];
		} else {
			throw new RuntimeException("No TypeArguments found");
		}
	}

	public E save(E entity) {

		if (!allowDuplicateMatchcodes) {// Gibt es diesen KeyText schon (nicht gelöscht)?
			List<E> findByKeyText = repository.findByKeyText(entity.getKeyText());
			for (E other : findByKeyText) {
				if (!Objects.equals(other.getKeyLong(), entity.getKeyLong())) {
					throw new RuntimeException("@msg.DuplicateMatchcodeNotAllowed");
				}
			}
		}

		if (entity.getKeyLong() != null) {
			entity.setLastAction(2);
		}

		entity.setLastDate(LocalDateTime.now());
		entity.setLastUser(getCurrentUser());
		entity = repository.save(entity);

		return entity;
	}

	public void deleteById(int id) {

		Optional<E> optional = repository.findById(id);
		if (optional.isEmpty()) {
			throw new EntityNotFoundException("@msg.EntityNotFound");
		}

		E entity = optional.get();

		entity.setLastAction(-1);
		entity.setLastUser(getCurrentUser());
		entity.setLastDate(LocalDateTime.now());
		repository.save(entity);
	}

	public E findEntityById(int id) {
		Optional<E> entity = repository.findById(id);
		if (entity.isEmpty()) {
			throw new EntityNotFoundException("@msg.EntityNotFound");
		}

		return entity.get();
	}

	public List<E> findEntitiesByKeyText(String keyText) {
		List<E> entity = repository.findByKeyText(keyText);
		if (entity.isEmpty()) {
			throw new EntityNotFoundException("@msg.EntityNotFound");
		}

		return entity;
	}

	public List<E> getAllEntities() {
		return repository.findAllWithLastActionGreaterZero();
	}

	/**
	 * Liefert die Authorities (AD-Gruppen, Memberships, Rollen) des angemeldeten Benutzers als String im Format <code>#a#b#c#</code>. Das Format entspricht
	 * den SecurityTokens in der Datenbank, sodass SQL-Code mit <code>like '%#a#%'</code> prüfen kann.
	 *
	 * @param maxLength
	 *            Maximale Länge des Strings. Längere Listen werden an einer Trennstelle abgeschnitten.
	 * @return Leerer String <code>#</code>, wenn kein Benutzer angemeldet ist oder er keine Authorities hat.
	 */
	public static String getCurrentAuthorities(int maxLength) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		StringBuilder sb = new StringBuilder("#");
		if (authentication != null) {
			for (var authority : authentication.getAuthorities()) {
				String name = authority.getAuthority() == null ? "" : authority.getAuthority().replace("#", "").trim();
				if (name.isEmpty() || sb.indexOf("#" + name + "#") >= 0) {
					continue;
				}
				if (sb.length() + name.length() + 1 > maxLength) {
					break;
				}
				sb.append(name).append('#');
			}
		}
		return sb.toString();
	}

	public static String getCurrentUser() {
		return SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : "CAS_JPA";
	}
}

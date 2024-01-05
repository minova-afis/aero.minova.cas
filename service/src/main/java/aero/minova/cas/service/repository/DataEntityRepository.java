package aero.minova.cas.service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import aero.minova.cas.service.model.DataEntity;

@NoRepositoryBean
public interface DataEntityRepository<E extends DataEntity> extends JpaRepository<E, Integer> {

	public List<E> findByLastActionGreaterThan(int lastAction);

	public Optional<E> findByKeyLong(int keyLong);

	public Optional<E> findByKeyTextAndLastActionGreaterThan(String keyText, int lastAction);

	public default Optional<E> findByKeyText(String keyText) {
		return findByKeyTextAndLastActionGreaterThan(keyText, 0);
	}
}

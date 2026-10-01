package aero.minova.cas.service.repository;

import java.util.List;
import java.util.Optional;

import aero.minova.cas.service.model.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DataEntityRepository<E extends DataEntity> extends JpaRepository<E, Integer> {

    public List<E> findByLastActionGreaterThan(int lastAction);

    public default List<E> findAllWithLastActionGreaterZero() {
        return findByLastActionGreaterThan(0);
    }

    public Optional<E> findByKeyLong(int keyLong);

    // Es gibt Tabellen in denen KeyText mehrmals vorkommen darf -> Rückgabetyp muss Liste sein, nicht Optional
    public List<E> findByKeyTextAndLastActionGreaterThan(String keyText, int lastAction);

    public default List<E> findByKeyText(String keyText) {
        return findByKeyTextAndLastActionGreaterThan(keyText, 0);
    }
}

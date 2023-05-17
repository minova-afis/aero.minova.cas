package aero.minova.cas.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.CASServices;

@Repository
public interface CASServicesRepository extends JpaRepository<CASServices, Long> {

	public CASServices findByKeyLong(int keyLong);

}

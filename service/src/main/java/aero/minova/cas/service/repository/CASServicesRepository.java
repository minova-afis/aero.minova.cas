package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.CASServices;

@Repository
public interface CASServicesRepository extends JpaRepository<CASServices, Long> {

	public CASServices findByKeylong(int keyLong);

	public List<CASServices> findByKeytext(String casServiceName);

}

package aero.minova.cas.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.ServiceProperties;

@Repository
public interface ServicePropertiesRepository extends JpaRepository<ServiceProperties, Long> {

	public ServiceProperties findByServiceAndPropertyAndLastActionGreaterThan(String service, String property, int lastAction);

}

package aero.minova.cas.service.repository;

import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.ServiceProperties;

@Repository
public interface ServicePropertiesRepository extends DataEntityRepository<ServiceProperties> {

	public ServiceProperties findByServiceAndPropertyAndLastActionGreaterThan(String service, String property, int lastAction);

}

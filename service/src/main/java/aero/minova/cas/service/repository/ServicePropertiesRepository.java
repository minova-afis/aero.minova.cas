package aero.minova.cas.service.repository;

import aero.minova.cas.service.model.ServiceProperties;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicePropertiesRepository extends DataEntityRepository<ServiceProperties> {

    public ServiceProperties findByServiceAndPropertyAndLastActionGreaterThan(
            String service, String property, int lastAction);
}

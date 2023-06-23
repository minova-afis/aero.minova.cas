package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.ServiceMessage;
import aero.minova.cas.service.model.ServiceProperties;

@Repository
public interface ServicePropertiesRepository extends JpaRepository<ServiceProperties, Long> {

	public List<ServiceMessage> findAllByServiceAndLastactionGreaterThan(String service, int lastaction);

}

package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.ServiceMessage;
import aero.minova.cas.service.model.ServiceProperties;

@Repository
public interface ServicePropertiesRepo extends JpaRepository<ServiceProperties, Long> {

	public List<ServiceMessage> findAllByKeytextAndLastactionGreaterThan(String keytext, int lastaction);

}

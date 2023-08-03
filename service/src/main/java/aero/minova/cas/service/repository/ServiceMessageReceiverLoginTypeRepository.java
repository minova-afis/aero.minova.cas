package aero.minova.cas.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.ServiceMessageReceiverLoginType;

@Repository
public interface ServiceMessageReceiverLoginTypeRepository extends JpaRepository<ServiceMessageReceiverLoginType, Long> {

	public Optional<ServiceMessageReceiverLoginType> findByKeytextAndLastActionGreaterThan(String keytext, int lastAction);

	public ServiceMessageReceiverLoginType findByKeyLongAndLastActionGreaterThan(int keyLong, int lastAction);

}

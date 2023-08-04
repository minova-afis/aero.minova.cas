package aero.minova.cas.service.repository;

import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.ServiceMessageReceiverLoginType;

@Repository
public interface ServiceMessageReceiverLoginTypeRepository extends DataEntityRepository<ServiceMessageReceiverLoginType> {

	public ServiceMessageReceiverLoginType findByKeyLongAndLastActionGreaterThan(int keyLong, int lastAction);

}

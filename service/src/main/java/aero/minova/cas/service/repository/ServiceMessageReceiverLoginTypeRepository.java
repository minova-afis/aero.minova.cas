package aero.minova.cas.service.repository;

import aero.minova.cas.service.model.ServiceMessageReceiverLoginType;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceMessageReceiverLoginTypeRepository
        extends DataEntityRepository<ServiceMessageReceiverLoginType> {

    public ServiceMessageReceiverLoginType findByKeyLongAndLastActionGreaterThan(int keyLong, int lastAction);
}

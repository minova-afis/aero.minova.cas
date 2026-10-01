package aero.minova.cas.service.repository;

import java.util.List;

import aero.minova.cas.service.model.ServiceMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceMessageRepository extends DataEntityRepository<ServiceMessage> {

    public List<ServiceMessage> findAllByIsSentFalseAndFailedFalseAndLastActionGreaterThan(int lastAction);
}

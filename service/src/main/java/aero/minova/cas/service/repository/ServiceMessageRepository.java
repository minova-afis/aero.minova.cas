package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.ServiceMessage;

@Repository
public interface ServiceMessageRepository extends DataEntityRepository<ServiceMessage> {

	public List<ServiceMessage> findAllByIsSentFalseAndFailedFalseAndLastActionGreaterThan(int lastAction);
}

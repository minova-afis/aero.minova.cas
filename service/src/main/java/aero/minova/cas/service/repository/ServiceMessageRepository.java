package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.ServiceMessage;

@Repository
public interface ServiceMessageRepository extends JpaRepository<ServiceMessage, Long> {

	public List<ServiceMessage> findAllByIsSentFalseAndFailedFalseAndLastActionGreaterThan(int lastAction);
}

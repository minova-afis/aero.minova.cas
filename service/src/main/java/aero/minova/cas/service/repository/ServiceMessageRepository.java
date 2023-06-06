package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.ServiceMessage;

@Repository
public interface ServiceMessageRepository extends JpaRepository<ServiceMessage, Long> {

	@Query("select u from ServiceMessage u where u.issent = false and failed=false and u.lastaction > :lastaction")
	public List<ServiceMessage> findAllByIssentFalseAndFailedFalseAndLastactionGreaterThan(int lastaction);
}

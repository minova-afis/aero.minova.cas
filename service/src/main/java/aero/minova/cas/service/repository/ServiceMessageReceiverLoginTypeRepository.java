package aero.minova.cas.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import aero.minova.cas.service.model.ServiceMessageReceiverLoginType;

public interface ServiceMessageReceiverLoginTypeRepository extends JpaRepository<ServiceMessageReceiverLoginType, Long> {

	@Query("select u from ServiceMessageReceiverLoginType u where u.keyText = :keytext and u.lastaction > 0")
	public Optional<ServiceMessageReceiverLoginType> findByKeyText(String keytext);

}

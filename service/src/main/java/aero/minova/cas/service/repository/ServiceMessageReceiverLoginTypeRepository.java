package aero.minova.cas.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.ServiceMessageReceiverLoginType;

@Repository
public interface ServiceMessageReceiverLoginTypeRepository extends JpaRepository<ServiceMessageReceiverLoginType, Long> {

	@Query("select u from ServiceMessageReceiverLoginType u where u.keytext = :keytext and u.lastAction > 0")
	public Optional<ServiceMessageReceiverLoginType> findByKeyText(String keytext);

	@Query("select u from ServiceMessageReceiverLoginType u where u.keyLong = :keylong and u.lastAction > 0")
	public ServiceMessageReceiverLoginType findByKeylong(int keylong);

}

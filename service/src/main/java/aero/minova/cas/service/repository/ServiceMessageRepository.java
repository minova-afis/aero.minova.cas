package aero.minova.cas.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aero.minova.cas.service.model.ServiceMessage;

public interface ServiceMessageRepository extends JpaRepository<ServiceMessage, Long> {

}

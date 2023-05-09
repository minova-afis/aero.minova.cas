package aero.minova.cas.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aero.minova.cas.service.model.NewsfeedListener;

public interface NewsfeedListenerRepository extends JpaRepository<NewsfeedListener, Long> {

}

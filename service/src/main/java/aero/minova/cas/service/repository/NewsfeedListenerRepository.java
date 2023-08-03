package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.CASServices;
import aero.minova.cas.service.model.NewsfeedListener;

@Repository
public interface NewsfeedListenerRepository extends JpaRepository<NewsfeedListener, Long> {

	public List<NewsfeedListener> findAllByLastActionGreaterThan(int lastAction);

	public List<NewsfeedListener> findAllByTopicAndLastActionGreaterThan(String topic, int lastAction);

	// @Query("select nl from NewsfeedListener nl where nl.CASServices = :casservicekey and nl.Topic = :topic and nl.lastaction > 0")
	public List<NewsfeedListener> findAllByCasServiceAndTopicAndLastActionGreaterThan(CASServices casService, String topic, int lastAction);

	public List<NewsfeedListener> findAllByCasService(CASServices casService);

}

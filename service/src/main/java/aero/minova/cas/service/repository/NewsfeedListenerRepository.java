package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.CASServices;
import aero.minova.cas.service.model.NewsfeedListener;

@Repository
public interface NewsfeedListenerRepository extends JpaRepository<NewsfeedListener, Long> {

	public List<NewsfeedListener> findAllByLastaction(int lastaction);

	public List<NewsfeedListener> findAllByTopicAndLastactionGreaterThan(String topic, int lastaction);

	// @Query("select nl from NewsfeedListener nl where nl.CASServices = :casservicekey and nl.Topic = :topic and nl.lastaction > 0")
	public List<NewsfeedListener> findAllByCasserviceAndTopicAndLastactionGreaterThan(CASServices casservice, String topic, int lastaction);

	public List<NewsfeedListener> findAllByCasservice(CASServices casservice);

}

package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.CASServices;
import aero.minova.cas.service.model.NewsfeedListener;

@Repository
public interface NewsfeedListenerRepository extends DataEntityRepository<NewsfeedListener> {

	public List<NewsfeedListener> findAllByTopicAndLastActionGreaterThan(String topic, int lastAction);

	public List<NewsfeedListener> findAllByCasServiceAndTopicAndLastActionGreaterThan(CASServices casService, String topic, int lastAction);

	public List<NewsfeedListener> findAllByCasService(CASServices casService);

	public List<NewsfeedListener> findAllByLastActionGreaterThan(int lastAction);

}

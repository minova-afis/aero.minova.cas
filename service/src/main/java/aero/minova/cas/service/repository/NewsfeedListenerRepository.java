package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.NewsfeedListener;

@Repository
public interface NewsfeedListenerRepository extends JpaRepository<NewsfeedListener, Long> {

	public List<NewsfeedListener> findAllByKeytextAndProcedurenameAndTopicAndServiceurlAndPortAndLastaction(String keytext, String procedurename, String topic,
			String serviceurl, Integer port, int lastaction);

}

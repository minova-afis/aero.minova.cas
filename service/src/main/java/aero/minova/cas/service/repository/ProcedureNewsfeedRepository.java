package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.ProcedureNewsfeed;

@Repository
public interface ProcedureNewsfeedRepository extends JpaRepository<ProcedureNewsfeed, Long> {

	List<ProcedureNewsfeed> findAllByKeyTextAndTopicAndLastActionGreaterThan(String keyText, String topic, int lastaction);

	List<ProcedureNewsfeed> findAllByTopicAndLastAction(String topic, int lastAction);

	List<ProcedureNewsfeed> findAllByLastAction(int lastAction);

}

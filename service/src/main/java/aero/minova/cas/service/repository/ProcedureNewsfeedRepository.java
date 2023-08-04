package aero.minova.cas.service.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import aero.minova.cas.service.model.ProcedureNewsfeed;

@Repository
public interface ProcedureNewsfeedRepository extends DataEntityRepository<ProcedureNewsfeed> {

	List<ProcedureNewsfeed> findAllByKeyTextAndTopicAndLastActionGreaterThan(String keyText, String topic, int lastaction);

	List<ProcedureNewsfeed> findAllByTopicAndLastAction(String topic, int lastAction);

}

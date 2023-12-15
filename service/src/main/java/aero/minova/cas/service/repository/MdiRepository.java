package aero.minova.cas.service.repository;

import java.util.List;

import aero.minova.cas.service.model.Mdi;

public interface MdiRepository extends DataEntityRepository<Mdi> {

	public List<Mdi> findByMdiTypeKeyLongAndLastActionGreaterThan(int mdiTypeKeyLong, int lastAction);
}

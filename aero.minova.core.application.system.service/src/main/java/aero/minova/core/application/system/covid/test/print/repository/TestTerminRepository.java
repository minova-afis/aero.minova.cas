package aero.minova.core.application.system.covid.test.print.repository;

import java.time.Instant;
import java.util.List;

import aero.minova.core.application.system.covid.test.print.domain.TestTermin;
import org.springframework.data.repository.CrudRepository;

public interface TestTerminRepository extends CrudRepository<TestTermin, Long> {
	List<TestTermin> findByLastActionGreaterThanAndStarttimeGreaterThanAndStarttimeLessThan(Integer lastAction, Instant mintime,
			Instant maxtime);
}

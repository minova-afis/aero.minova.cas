package aero.minova.core.application.system.covid.test.print.controller;

import static java.util.Arrays.asList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import aero.minova.core.application.system.covid.test.print.repository.TestTerminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.covid.test.print.domain.DateRequest;
import aero.minova.core.application.system.covid.test.print.domain.TimeLocation;
import aero.minova.core.application.system.covid.test.print.domain.Timeslot;
import aero.minova.core.application.system.sql.SystemDatabase;
import lombok.val;

@CrossOrigin
@RestController
public class MeetingController {

	Logger logger = LoggerFactory.getLogger(MeetingController.class);

	@PostMapping(value = "meeting/time/available", produces = "application/json")
	public List<Timeslot> getAvailableTimeslot(@RequestBody TimeLocation timeLocation) throws Exception {
		return asList(new Timeslot(LocalDateTime.of(2021, 4, 1, 9, 0), LocalDateTime.of(2021, 4, 1, 9, 30)), //
				new Timeslot(LocalDateTime.of(2021, 4, 1, 9, 30), LocalDateTime.of(2021, 4, 1, 10, 0)), //
				new Timeslot(LocalDateTime.of(2021, 4, 1, 10, 0), LocalDateTime.of(2021, 4, 1, 10, 30)), //
				new Timeslot(LocalDateTime.of(2021, 4, 1, 10, 30), LocalDateTime.of(2021, 4, 1, 11, 0)), //
				new Timeslot(LocalDateTime.of(2021, 4, 1, 11, 0), LocalDateTime.of(2021, 4, 1, 11, 30)), //
				new Timeslot(LocalDateTime.of(2021, 4, 1, 11, 30), LocalDateTime.of(2021, 4, 1, 12, 0)), //
				new Timeslot(LocalDateTime.of(2021, 4, 1, 12, 0), LocalDateTime.of(2021, 4, 1, 12, 30)), //
				new Timeslot(LocalDateTime.of(2021, 4, 1, 12, 30), LocalDateTime.of(2021, 4, 1, 13, 0)), //
				new Timeslot(LocalDateTime.of(2021, 4, 1, 12, 0), LocalDateTime.of(2021, 4, 1, 13, 30))//
		);
	}

	@PostMapping(value = "meeting/date/available", produces = "application/json")
	public List<LocalDate> getAvailableTimeslot(@RequestBody DateRequest testStreckenKeyText) throws Exception {
		return asList(LocalDate.of(2021, 4, 1));
	}
}

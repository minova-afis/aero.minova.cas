package aero.minova.covid.test.print;

import static java.util.Arrays.asList;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeetingController {
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
}

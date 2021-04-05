package aero.minova.core.application.system.covid.test.print.controller;

import static java.util.Arrays.asList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import aero.minova.core.application.system.covid.test.print.domain.AvailableTestsPerDate;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.covid.test.print.domain.DateRequest;
import aero.minova.core.application.system.covid.test.print.domain.TimeLocationRequest;
import aero.minova.core.application.system.covid.test.print.domain.Timeslot;

@CrossOrigin
@RestController
public class MeetingController {

    Logger logger = LoggerFactory.getLogger(MeetingController.class);

    @PostMapping(value = "meeting/time/available", produces = "application/json")
    public List<Timeslot> getAvailableTimeslot(@RequestBody TimeLocationRequest timeLocationRequest) throws Exception {
        return asList(new Timeslot(LocalDateTime.of(2021, 4, 1, 9, 0), LocalDateTime.of(2021, 4, 1, 9, 30), 1), //
                new Timeslot(LocalDateTime.of(2021, 4, 1, 9, 30), LocalDateTime.of(2021, 4, 1, 10, 0), 2), //
                new Timeslot(LocalDateTime.of(2021, 4, 1, 10, 0), LocalDateTime.of(2021, 4, 1, 10, 30), 3), //
                new Timeslot(LocalDateTime.of(2021, 4, 1, 10, 30), LocalDateTime.of(2021, 4, 1, 11, 0), 2), //
                new Timeslot(LocalDateTime.of(2021, 4, 1, 11, 0), LocalDateTime.of(2021, 4, 1, 11, 30), 0), //
                new Timeslot(LocalDateTime.of(2021, 4, 1, 11, 30), LocalDateTime.of(2021, 4, 1, 12, 0), 3), //
                new Timeslot(LocalDateTime.of(2021, 4, 1, 12, 0), LocalDateTime.of(2021, 4, 1, 12, 30), 0), //
                new Timeslot(LocalDateTime.of(2021, 4, 1, 12, 30), LocalDateTime.of(2021, 4, 1, 13, 0), 0), //
                new Timeslot(LocalDateTime.of(2021, 4, 1, 12, 0), LocalDateTime.of(2021, 4, 1, 13, 30), 2)//
        );
    }

    @PostMapping(value = "meeting/date/available", produces = "application/json")
    public List<AvailableTestsPerDate> getAvailableTimeslot(@RequestBody DateRequest testStreckenKeyText) throws Exception {
        val testResult = new AvailableTestsPerDate();
        testResult.setDate(LocalDate.of(2021, 4, 1));
        testResult.setAvailableTests(3);
        testResult.setTestStreckKeyText("Sommerhausen");
        return asList(testResult);
    }
}

package aero.minova.core.application.system.covid.test.print.controller;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;

import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.covid.test.print.domain.AvailableTestsPerDate;
import aero.minova.core.application.system.domain.*;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.covid.test.print.domain.AvailableDateRequest;
import aero.minova.core.application.system.covid.test.print.domain.TimeLocationRequest;
import aero.minova.core.application.system.covid.test.print.domain.Timeslot;

@CrossOrigin
@RestController
public class MeetingController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("dd.MM.yyyy")
            .withZone(ZoneId.systemDefault());

    Logger logger = LoggerFactory.getLogger(MeetingController.class);
    @Autowired
    SqlViewController sqlViewController;

    @PostMapping(value = "meeting/time/available", produces = "application/json")
    public List<Timeslot> getAvailableTimeslot(@RequestBody TimeLocationRequest timeLocationRequest) throws Exception {
        val sqlRequest = new Table();
        sqlRequest.setName("xvctsTestTerminBelegung");
        sqlRequest.addColumn(new Column("KeyLong", DataType.INTEGER, OutputType.OUTPUT));
        sqlRequest.addColumn(new Column("Starttime", DataType.ZONED, OutputType.INPUT));
        sqlRequest.addColumn(new Column("Startdate", DataType.INSTANT, OutputType.INPUT));
        sqlRequest.addColumn(new Column("FreeCapacity", DataType.INTEGER, OutputType.OUTPUT));
        {
            val firstRequestParams = new Row();
            sqlRequest.getRows().add(firstRequestParams);
            firstRequestParams.addValue(new Value(timeLocationRequest.getTestStreckKeyLong(), null));
            firstRequestParams.addValue(null);
            firstRequestParams.addValue(new Value(timeLocationRequest
                    .getDate()
                    .atStartOfDay(ZoneId.of("UTC"))
                    .toInstant()
                    , null));
            firstRequestParams.addValue(null);
        }
        // Hiermit wird der unsichere Zugriff ermöglicht.
        val requestingAuthority = new Row();
        requestingAuthority.addValue(new Value(false, "1"));
        requestingAuthority.addValue(new Value(false, "2"));
        requestingAuthority.addValue(new Value(false, "3"));


        return sqlViewController.unsecurelyGetIndexView(sqlRequest, Arrays.asList(requestingAuthority))
                .getRows()
                .stream()
                .map(row -> new Timeslot(row.getValues().get(1).getZonedDateTimeValue()
                        , null
                        , row.getValues().get(3).getIntegerValue())
                ).collect(toList());
    }

    @PostMapping(value = "meeting/date/available", produces = "application/json")
    public List<AvailableTestsPerDate> getAvailableTimeslot(@RequestBody AvailableDateRequest availableDateRequest) throws Exception {
        val sqlRequest = new Table();
        sqlRequest.setName("xvctsTestTagBelegung");
        sqlRequest.addColumn(new Column("KeyLong", DataType.INTEGER, OutputType.OUTPUT));
        sqlRequest.addColumn(new Column("StartDate", DataType.INSTANT, OutputType.INPUT));
        sqlRequest.addColumn(new Column("FreeCapacity", DataType.INTEGER, OutputType.OUTPUT));
        {
            val firstRequestParams = new Row();
            sqlRequest.getRows().add(firstRequestParams);
            firstRequestParams.addValue(new Value(availableDateRequest.getTestStreckKeyLong(), null));
            firstRequestParams.addValue(null);
            firstRequestParams.addValue(null);
        }
        // Hiermit wird der unsichere Zugriff ermöglicht.
        val requestingAuthority = new Row();
        requestingAuthority.addValue(new Value(false, "1"));
        requestingAuthority.addValue(new Value(false, "2"));
        requestingAuthority.addValue(new Value(false, "3"));


        return sqlViewController.unsecurelyGetIndexView(sqlRequest, Arrays.asList(requestingAuthority))
                .getRows()
                .stream()
                .map(row -> {
                    val testResult = new AvailableTestsPerDate();
                    testResult.setDate(row
                            .getValues()
                            .get(1)
                            .getInstantValue()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                    testResult.setAvailableTests(row.getValues().get(2).getIntegerValue());
                    testResult.setTestStreckKeyText(row.getValues().get(0).getStringValue());
                    return testResult;
                }).collect(toList());
    }
}

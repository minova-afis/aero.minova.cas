package aero.minova.core.application.system.covid.test.print.controller;

import static java.util.stream.Collectors.toList;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.controller.SqlProcedureController;
import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.covid.test.print.domain.MeetingFormInformation;
import aero.minova.core.application.system.covid.test.print.domain.TestEventResponse;
import aero.minova.core.application.system.covid.test.print.domain.TestPersonKey;
import aero.minova.core.application.system.covid.test.print.domain.TestTermin;
import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.OutputType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;
import lombok.val;

@RestController
public class MeetingFormController {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	Logger logger = LoggerFactory.getLogger(MeetingController.class);
	@Autowired
	SqlViewController sqlViewController;

	@Autowired
	SqlProcedureController sqlProcedureController;

	@PostMapping(value = "public/meeting/book", produces = "application/json")
	public TestTermin bookMeeting(@RequestBody MeetingFormInformation input) throws Exception {

		// Überprüfen, ob der Termin auch in der Zukunft liegt
		LocalDateTime datetime = null;
		if (!input.getStarttime().isEmpty()) {
			datetime = LocalDateTime.parse(input.getStarttime(), DATE_FORMATTER);
			if (Instant.now().isAfter(datetime.toInstant(ZoneOffset.UTC))) {
				throw new RuntimeException("Der gewünschte Termin liegt bereits in der Vergangenheit!");
			}
		} else {
			throw new RuntimeException("Bitte geben Sie Ihren gewünschten Termin an.");
		}

		val sqlRequest = new Table();
		sqlRequest.setName("xvctsTestTerminBuchung");
		sqlRequest.addColumn(new Column("KeyLong", DataType.LONG, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("ctsTestStreckeKey", DataType.LONG, OutputType.INPUT));
		sqlRequest.addColumn(new Column("Starttime", DataType.INSTANT, OutputType.INPUT));
		sqlRequest.addColumn(new Column("SecurityToken", DataType.STRING, OutputType.INPUT));
		{
			val firstRequestParams = new Row();
			sqlRequest.getRows().add(firstRequestParams);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(new Value(input.getTestStreckKeyLong(), null));
			firstRequestParams.addValue(new Value((datetime.toInstant(ZoneOffset.UTC)), null));
			firstRequestParams.addValue(null);
		}
		// Hiermit wird der unsichere Zugriff ermöglicht.
		val requestingAuthority = new Row();
		requestingAuthority.addValue(new Value(false, "1"));
		requestingAuthority.addValue(new Value(false, "2"));
		requestingAuthority.addValue(new Value(false, "3"));

		List<Row> viewOutput = sqlViewController.unsecurelyGetIndexView(sqlRequest, Arrays.asList(requestingAuthority)).getRows();

		// Falls der gewünschte Termin in der Zwischenzeit doch belegt wurde, ist die Liste leer
		if (viewOutput.isEmpty()) {
			throw new RuntimeException("Der gewünschte Zeitslot ist leider bereits belegt!");
		}

		// Falls der Termin noch frei ist, muss er nun mit der xpctsUpdateTestTermin Prozedur geändert werden
		Table procedureInput = new Table();
		procedureInput.setName("xpctsUpdateTestTermin");
		// Die Input-Variablen sind : @KeyLong des Termins, @CTSTestStreckeKey, @CTSTestPersonKey, @Starttime, @SecurityToken
		procedureInput.addColumn(new Column("KeyLong", DataType.LONG, OutputType.INPUT));
		procedureInput.addColumn(new Column("CTSTestStreckeKey", DataType.LONG, OutputType.INPUT));
		procedureInput.addColumn(new Column("Starttime", DataType.INSTANT, OutputType.INPUT));
		procedureInput.addColumn(new Column("SecurityToken", DataType.STRING, OutputType.INPUT));
		procedureInput.addColumn(new Column("CTSTestPersonKey", DataType.LONG, OutputType.INPUT));
		// TeststreckenKey, Starttime und PersonenKey kommen aus dem GET.
		// KeyLong des Testtermins und SecurityToken kommen aus der aufgerufenen View.
		{
			val secondRequestParams = new Row();
			procedureInput.getRows().add(secondRequestParams);
			secondRequestParams.addValue(viewOutput.get(0).getValues().get(0));
			secondRequestParams.addValue(new Value(input.getTestStreckKeyLong(), null));
			secondRequestParams.addValue(new Value((datetime.toInstant(ZoneOffset.UTC)), null));
			secondRequestParams.addValue(viewOutput.get(0).getValues().get(3));
			secondRequestParams.addValue(new Value(input.getTestPersonKeyLong(), null));
		}
		sqlProcedureController.calculateSqlProcedureResult(procedureInput);

		TestTermin termin = new TestTermin();
		termin.setKeyLong(viewOutput.get(0).getValues().get(0).getLongValue());
		termin.setCTSTeststreckeKey(input.getTestStreckKeyLong());
		termin.setCTSTestpersonKey(input.getTestPersonKeyLong());
		termin.setStarttime(input.getStarttime());
		return termin;
	}

	@PostMapping(value = "public/meeting/event", produces = "application/json")
	public List<TestEventResponse> getTestEvent(@RequestBody TestPersonKey key) throws Exception {
		val sqlRequest = new Table();
		sqlRequest.setName("xvctsTestTerminErgebnis");
		sqlRequest.addColumn(new Column("KeyLong", DataType.LONG, OutputType.INPUT));
		sqlRequest.addColumn(new Column("CTSTestPersonKey", DataType.LONG, OutputType.INPUT));
		sqlRequest.addColumn(new Column("StartTime", DataType.INSTANT, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("Ergebnis", DataType.STRING, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("Kit", DataType.STRING, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("Description", DataType.STRING, OutputType.OUTPUT));
		{
			val firstRequestParams = new Row();
			sqlRequest.getRows().add(firstRequestParams);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(new Value(key.getCTSTestPersonKey(), null));
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(null);
		}
		// Hiermit wird der unsichere Zugriff ermöglicht.
		val requestingAuthority = new Row();
		requestingAuthority.addValue(new Value(false, "1"));
		requestingAuthority.addValue(new Value(false, "2"));
		requestingAuthority.addValue(new Value(false, "3"));

		return sqlViewController.unsecurelyGetIndexView(sqlRequest, Arrays.asList(requestingAuthority)).getRows().stream()//
				.map(row -> new TestEventResponse(row.getValues().get(3).getStringValue(), row.getValues().get(4).getStringValue(),
						row.getValues().get(2).getInstantValue(), row.getValues().get(5).getStringValue()))
				.collect(toList());

	}

}

package aero.minova.core.application.system.covid.test.print.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.controller.SqlProcedureController;
import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.covid.test.print.domain.TestPersonMeetingInformation;
import aero.minova.core.application.system.covid.test.print.domain.TestTermin;
import aero.minova.core.application.system.covid.test.print.service.TestCertificateMailService;
import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.CovidException;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.OutputType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.SqlProcedureResult;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;
import lombok.val;

@RestController
public class TestPersonMeetingController {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private final Pattern emailpattern = Pattern.compile("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
	private final Pattern postalcodepattern = Pattern.compile("^0[1-9]\\d\\d(?<!0100)0|0[1-9]\\d\\d[1-9]|[1-9]\\d{3}[0-8]|[1-9]\\d{3}(?<!9999)9$");

	Logger logger = LoggerFactory.getLogger(MeetingController.class);
	@Autowired
	SqlViewController sqlViewController;

	@Autowired
	SqlProcedureController sqlProcedureController;

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private TestCertificateMailService mailService;

	@PostMapping(value = "public/meeting/noaccount/book", produces = "application/json")
	public TestTermin bookMeeting(@RequestBody TestPersonMeetingInformation input) throws Exception {

		logger.info("Trying to book a meeting for unregistered testperson with input: " + input.toString());

		// Überprüfen, ob der Termin auch in der Zukunft liegt
		LocalDateTime datetime = null;
		if (!input.getStarttime().isEmpty()) {
			datetime = LocalDateTime.parse(input.getStarttime(), DATE_FORMATTER);
			if (Instant.now().isAfter(datetime.toInstant(ZoneOffset.UTC))) {
				throw new CovidException("Der gewünschte Termin liegt bereits in der Vergangenheit!");
			}
		} else {
			throw new CovidException("Bitte geben Sie Ihren gewünschten Termin an.");
		}

		// Hier bekommen wir den KeyLong her, hier wird die Person entweder neu angelegt oder raus gesucht
		Long testPersonKey = checkTestPerson(input);

		val sqlRequest = new Table();
		sqlRequest.setName("xvctsTestTerminBuchung");
		sqlRequest.addColumn(new Column("KeyLong", DataType.LONG, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("ctsTestStreckeKey", DataType.LONG, OutputType.INPUT));
		sqlRequest.addColumn(new Column("Starttime", DataType.INSTANT, OutputType.INPUT));
		sqlRequest.addColumn(new Column("SecurityToken", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("Teststrecke", DataType.STRING, OutputType.INPUT));
		{
			val firstRequestParams = new Row();
			sqlRequest.getRows().add(firstRequestParams);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(new Value(input.getTestStreckKeyLong(), null));
			firstRequestParams.addValue(new Value((datetime.toInstant(ZoneOffset.UTC)), null));
			firstRequestParams.addValue(null);
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
			throw new CovidException("Der gewünschte Termin ist leider bereits belegt!");
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

		// TeststreckenKey und Starttime kommen aus dem GET.
		// TestPersonKey wird oben heraus gelesen oder neu generiert
		// KeyLong des Testtermins und SecurityToken kommen aus der aufgerufenen View.
		{
			val secondRequestParams = new Row();
			procedureInput.getRows().add(secondRequestParams);
			secondRequestParams.addValue(viewOutput.get(0).getValues().get(0));
			secondRequestParams.addValue(new Value(input.getTestStreckKeyLong(), null));
			secondRequestParams.addValue(new Value((datetime.toInstant(ZoneOffset.UTC)), null));
			secondRequestParams.addValue(viewOutput.get(0).getValues().get(3));
			secondRequestParams.addValue(new Value(testPersonKey, null));
		}
		try {
			sqlProcedureController.calculateSqlProcedureResult(procedureInput);

			try {
				val message = mailSender.createMimeMessage();
				{
					val helper = new MimeMessageHelper(message, true);
					helper.setTo(input.getEmail());
					helper.setFrom(mailService.mailAddress);
					helper.setSubject("COVID-Test-Terminbestätigung");
					helper.setText("Guten Tag " + input.getFirstname() + " " + input.getLastname() + ", " + " Sie haben erfolgreich ihren Terminen am "
							+ input.getStarttime() + " in " + viewOutput.get(0).getValues().get(4).getStringValue() + " gebucht.", true);
				}
				mailSender.send(message);
			} catch (Exception e) {
				logger.error("Could not send booking email.", e);
			}

			TestTermin termin = new TestTermin();
			termin.setKeyLong(viewOutput.get(0).getValues().get(0).getLongValue());
			termin.setCTSTeststreckeKey(input.getTestStreckKeyLong());
			termin.setCTSTestpersonKey(testPersonKey);
			termin.setStarttime(input.getStarttime());
			return termin;
		} catch (Exception e) {
			throw new CovidException(e.getMessage());
		}
	}

	private Long checkTestPerson(TestPersonMeetingInformation input) throws Exception {
		// Überprüft die Angaben auf Format und Länge
		checkUserInputNoRegister(input);

		// Überprüfen, ob die Person bereits im System registriert ist
		Table sqlRequest = new Table();
		sqlRequest.setName("xvctsTestPersonIndex");
		sqlRequest.addColumn(new Column("KeyLong", DataType.LONG, OutputType.INPUT));
		sqlRequest.addColumn(new Column("FirstName", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("LastName", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("Birthdate", DataType.INSTANT, OutputType.INPUT));
		sqlRequest.addColumn(new Column("Email", DataType.STRING, OutputType.INPUT));
		{
			val firstRequestParams = new Row();
			sqlRequest.getRows().add(firstRequestParams);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(new Value(input.getFirstname(), null));
			firstRequestParams.addValue(new Value(input.getLastname(), null));
			firstRequestParams.addValue(new Value((input.getBirthdate()).atStartOfDay(ZoneId.systemDefault()).toInstant(), null));
			firstRequestParams.addValue(new Value(input.getEmail(), null));
		}
		// Hiermit wird der unsichere Zugriff ermöglicht.
		val requestingAuthority = new Row();
		requestingAuthority.addValue(new Value(false, "1"));
		requestingAuthority.addValue(new Value(false, "2"));
		requestingAuthority.addValue(new Value(false, "3"));

		Long tpk;

		// Überprüfen, ob der Benutzer bereits exisitert, da keine Doppelteneiträge vorhanden sein sollten
		List<Row> viewOutput = sqlViewController.unsecurelyGetIndexView(sqlRequest, Arrays.asList(requestingAuthority)).getRows();
		if (!viewOutput.isEmpty()) {
			tpk = viewOutput.get(0).getValues().get(0).getLongValue();
		} else {

			Table sqlInsertRequest = new Table();
			sqlInsertRequest.setName("xpctsInsertTestPerson");
			sqlInsertRequest.addColumn(new Column("KeyLong", DataType.LONG, OutputType.OUTPUT));
			sqlInsertRequest.addColumn(new Column("FirstName", DataType.STRING, OutputType.INPUT));
			sqlInsertRequest.addColumn(new Column("LastName", DataType.STRING, OutputType.INPUT));
			sqlInsertRequest.addColumn(new Column("Birthdate", DataType.INSTANT, OutputType.INPUT));
			sqlInsertRequest.addColumn(new Column("Street", DataType.STRING, OutputType.INPUT));
			sqlInsertRequest.addColumn(new Column("PostalCode", DataType.STRING, OutputType.INPUT));
			sqlInsertRequest.addColumn(new Column("City", DataType.STRING, OutputType.INPUT));
			sqlInsertRequest.addColumn(new Column("Phone", DataType.STRING, OutputType.INPUT));
			sqlInsertRequest.addColumn(new Column("Phone2", DataType.STRING, OutputType.INPUT));
			sqlInsertRequest.addColumn(new Column("Email", DataType.STRING, OutputType.INPUT));
			sqlInsertRequest.addColumn(new Column("Password", DataType.STRING, OutputType.INPUT));
			{
				val secondRequestParams = new Row();
				sqlInsertRequest.getRows().add(secondRequestParams);
				secondRequestParams.addValue(null);
				secondRequestParams.addValue(new Value(input.getFirstname(), null));
				secondRequestParams.addValue(new Value(input.getLastname(), null));
				secondRequestParams.addValue(new Value((input.getBirthdate()).atStartOfDay(ZoneId.systemDefault()).toInstant(), null));
				secondRequestParams.addValue(new Value(input.getStreet(), null));
				secondRequestParams.addValue(new Value(input.getPostalcode(), null));
				secondRequestParams.addValue(new Value(input.getCity(), null));
				secondRequestParams.addValue(new Value(input.getPhonenumber(), null));
				secondRequestParams.addValue(null);
				secondRequestParams.addValue(new Value(input.getEmail(), null));
				secondRequestParams.addValue(null);
			}

			SqlProcedureResult result;
			try {
				result = sqlProcedureController.calculateSqlProcedureResult(sqlInsertRequest);
			} catch (Exception e) {
				throw new CovidException(e.getMessage());
			}
			tpk = result.getOutputParameters().getRows().get(0).getValues().get(0).getLongValue();
		}

		return tpk;

	}

	private void checkUserInputNoRegister(TestPersonMeetingInformation input) throws Exception {

		if (input.getFirstname() == null || input.getLastname() == null || input.getFirstname().isEmpty() || input.getLastname().isEmpty()) {
			throw new CovidException("Bitte geben Sie Ihren Vor- und Nachnamen an.");
		} else if (input.getFirstname().length() > 50) {
			throw new CovidException("Der angegebene Vorname übersteigt das Zeichenlimit!");
		} else if (input.getLastname().length() > 50) {
			throw new CovidException("Der angegebene Nachname übersteigt das Zeichenlimit!");
		}

		if (input.getStreet() == null || input.getStreet().isEmpty()) {
			throw new CovidException("Bitte geben Sie Ihre Adresse an.");
		}
		if (input.getStreet().length() > 50) {
			throw new CovidException("Die angegebene Straße übersteigt das Zeichenlimit!");
		}

		if (input.getCity() == null || input.getCity().isEmpty()) {
			throw new CovidException("Bitte geben Sie Ihren Wohnort an.");
		} else if (input.getCity().length() > 20) {
			throw new CovidException("Der angegebene Wohnort übersteigt das Zeichenlimit!");
		}

		if (input.getPostalcode() != null && !input.getPostalcode().isEmpty()) {
			Matcher matcher = postalcodepattern.matcher(input.getPostalcode());
			if (!matcher.find()) {
				throw new CovidException("Die angegebene Postleitzahl hat kein gültiges Format!");
			}
		} else {
			throw new CovidException("Bitte geben Sie Ihre Postleitzahl an.");
		}

		if (input.getBirthdate() != null) {
			if (Instant.now().isBefore((input.getBirthdate()).atStartOfDay(ZoneId.systemDefault()).toInstant())) {
				throw new CovidException("Der angegebene Geburtstag ist ungültig!");
			}
		} else {
			throw new CovidException("Bitte geben Sie Ihren Geburtstag an.");
		}

		if (input.getPhonenumber() != null && !input.getPhonenumber().isEmpty()) {
			if (!input.getPhonenumber().matches("[0-9]+")) {
				throw new CovidException("Die angegebene Telefonnummer ist nicht gültig!");
			} else if (input.getPhonenumber().length() > 20) {
				throw new CovidException("Die angegebene Telefonnummer übersteigt das Zeichenlimit!");
			}
		} else {
			throw new CovidException("Bitte geben Sie Ihre Telefonnummer an.");
		}

		if (input.getEmail() != null && !input.getEmail().isEmpty()) {
			Matcher matcher = emailpattern.matcher(input.getEmail());
			if (!matcher.find()) {
				throw new CovidException("Die angegebene E-Mail Adresse hat kein gültiges Format!");
			} else {
				if (input.getEmail().length() > 50) {
					throw new CovidException("Die angegebene Email-Adresse übersteigt das Zeichenlimit!");
				}
			}
		} else {
			throw new CovidException("Bitte geben Sie Ihre E-Mailadresse an.");
		}
	}
}

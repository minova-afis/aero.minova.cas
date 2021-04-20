package aero.minova.core.application.system.covid.test.print.controller;

import java.time.Instant;
import java.time.ZoneId;
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
import aero.minova.core.application.system.covid.test.print.domain.TestPersonInformation;
import aero.minova.core.application.system.covid.test.print.domain.TestPersonKey;
import aero.minova.core.application.system.covid.test.print.domain.UserInfo;
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
public class TestPersonController {

	@Autowired
	SqlViewController sqlViewController;

	@Autowired
	SqlProcedureController sqlProcedureController;

	final Logger logger = LoggerFactory.getLogger(TestCertificateMailService.class);

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private TestCertificateMailService mailService;

	private final Pattern emailpattern = Pattern.compile("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
	private final Pattern postalcodepattern = Pattern.compile("^0[1-9]\\d\\d(?<!0100)0|0[1-9]\\d\\d[1-9]|[1-9]\\d{3}[0-8]|[1-9]\\d{3}(?<!9999)9$");

	@PostMapping(value = "public/testPerson/register")
	public TestPersonKey registerTestPerson(@RequestBody TestPersonInformation input) throws Exception {

		// Überprüft die Angaben auf Format und Länge
		checkUserInput(input);

		// Überprüfen, ob die Person bereits im System registriert ist
		Table sqlRequest = new Table();
		sqlRequest.setName("xvctsMobileTestPersonIndex");
		sqlRequest.addColumn(new Column("Email", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("Password", DataType.STRING, OutputType.INPUT));
		{
			val firstRequestParams = new Row();
			sqlRequest.getRows().add(firstRequestParams);
			firstRequestParams.addValue(new Value(input.getEmail(), null));
			firstRequestParams.addValue(new Value("", "!null"));
		}
		// Hiermit wird der unsichere Zugriff ermöglicht.
		val requestingAuthority = new Row();
		requestingAuthority.addValue(new Value(false, "1"));
		requestingAuthority.addValue(new Value(false, "2"));
		requestingAuthority.addValue(new Value(false, "3"));

		// Überprüfen, ob der Benutzer bereits exisitert, da keine Doppelteneiträge vorhanden sein sollten
		List<Row> viewOutput = sqlViewController.unsecurelyGetIndexView(sqlRequest, Arrays.asList(requestingAuthority)).getRows();
		if (!viewOutput.isEmpty()) {
			throw new CovidException("Ein Benutzer mit dieser Emailadresse existiert bereits!");
		}

		Table sqlInsertRequest = new Table();
		sqlInsertRequest.setName("xpctsInsertTestPersonApp");
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
			secondRequestParams.addValue(new Value(input.getPhonenumber2(), null));
			secondRequestParams.addValue(new Value(input.getEmail(), null));
			secondRequestParams.addValue(new Value(input.getPassword(), null));
		}

		SqlProcedureResult result = sqlProcedureController.calculateSqlProcedureResult(sqlInsertRequest);

		try {
			val message = mailSender.createMimeMessage();
			{
				val helper = new MimeMessageHelper(message, true);
				helper.setTo(input.getEmail());
				helper.setFrom(mailService.mailAddress);
				helper.setSubject("COVID-Test-Account");
				helper.setText("Guten Tag " + input.getFirstname() + " " + input.getLastname() + ", "
						+ "\n Sie haben sich erfolgreich registriert und können nun Termine über die App buchen.", true);
			}
			mailSender.send(message);
		} catch (Exception e) {
			logger.error("Could not send login email.", e);
		}

		TestPersonKey tpk = new TestPersonKey();
		tpk.setCTSTestPersonKey(result.getOutputParameters().getRows().get(0).getValues().get(0).getLongValue());
		return tpk;

	}

	@PostMapping(value = "public/testPerson/login", produces = "application/json")
	public long loginTestPerson(@RequestBody UserInfo info) throws Exception {

		if (info.getEmail().isEmpty()) {
			throw new CovidException("Bitte geben Sie Ihre Emailadresse ein.");
		}

		if (info.getPassword().isEmpty()) {
			throw new CovidException("Bitte geben Sie Ihr Passwort ein.");
		}

		Table sqlRequest = new Table();
		sqlRequest.setName("xvctsMobileTestPersonIndex");
		sqlRequest.addColumn(new Column("KeyLong", DataType.LONG, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("Email", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("Password", DataType.STRING, OutputType.INPUT));
		{
			val firstRequestParams = new Row();
			sqlRequest.getRows().add(firstRequestParams);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(new Value(info.getEmail(), null));
			firstRequestParams.addValue(new Value(info.getPassword(), null));
		}
		// Hiermit wird der unsichere Zugriff ermöglicht.
		val requestingAuthority = new Row();
		requestingAuthority.addValue(new Value(false, "1"));
		requestingAuthority.addValue(new Value(false, "2"));
		requestingAuthority.addValue(new Value(false, "3"));

		List<Row> result = sqlViewController.unsecurelyGetIndexView(sqlRequest, Arrays.asList(requestingAuthority)).getRows();

		if (result.size() > 0) {
			// Zurückgeben des KeyLongs
			return result.get(0).getValues().get(0).getLongValue();
		} else {
			throw new CovidException("Fehler beim Login. Bitte überprüfen Sie Ihre angegebene Emailadresse und Ihr Passwort.");
		}

	}

	@PostMapping(value = "public/testPerson/info", produces = "application/json")
	public TestPersonInformation getTestPersonInformation(@RequestBody TestPersonKey key) throws Exception {

		Table sqlRequest = new Table();
		sqlRequest.setName("xpctsReadTestPerson");
		sqlRequest.addColumn(new Column("KeyLong", DataType.LONG, OutputType.INPUT));
		sqlRequest.addColumn(new Column("FirstName", DataType.STRING, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("LastName", DataType.STRING, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("Birthdate", DataType.ZONED, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("Street", DataType.STRING, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("PostalCode", DataType.STRING, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("City", DataType.STRING, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("Phone", DataType.STRING, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("Phone2", DataType.STRING, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("Email", DataType.STRING, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("Password", DataType.STRING, OutputType.OUTPUT));
		{
			val firstRequestParams = new Row();
			sqlRequest.getRows().add(firstRequestParams);
			firstRequestParams.addValue(new Value(key.getCTSTestPersonKey(), null));
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(null);
		}

		SqlProcedureResult sql = sqlProcedureController.calculateSqlProcedureResult(sqlRequest);
		Table result = sql.getOutputParameters();

		// bei Fehlschlag sind alle Felder null, die Email sollte auf jeden Fall da sein, weil man sich sonst auch nicht einloggen könnte
		if (!result.getRows().get(0).getValues().get(9).getStringValue().equals(null)) {
			TestPersonInformation tpi = new TestPersonInformation();
			tpi.setFirstname(result.getRows().get(0).getValues().get(1).getStringValue());
			tpi.setLastname(result.getRows().get(0).getValues().get(2).getStringValue());
			tpi.setBirthdate(result.getRows().get(0).getValues().get(3).getZonedDateTimeValue().toLocalDate());
			tpi.setStreet(result.getRows().get(0).getValues().get(4).getStringValue());
			tpi.setPostalcode(result.getRows().get(0).getValues().get(5).getStringValue());
			tpi.setCity(result.getRows().get(0).getValues().get(6).getStringValue());
			tpi.setPhonenumber(result.getRows().get(0).getValues().get(7).getStringValue());
			tpi.setPhonenumber2(result.getRows().get(0).getValues().get(8).getStringValue());
			tpi.setEmail(result.getRows().get(0).getValues().get(9).getStringValue());
			tpi.setPassword("******");
			return tpi;
		} else {
			throw new CovidException("Fehler beim Laden der Nutzerdaten.");
		}

	}

	private void checkUserInput(TestPersonInformation input) throws Exception {

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

		// Die alternative Telefonnummer darf auch leer sein
		if (input.getPhonenumber2() != null && !input.getPhonenumber2().isEmpty()) {
			if (!input.getPhonenumber2().matches("[0-9]+")) {
				throw new CovidException("Die angegebene alternative Telefonnummer ist nicht gültig!");
			} else if (input.getPhonenumber2().length() > 20) {
				throw new CovidException("Die angegebene alternative Telefonnummer übersteigt das Zeichenlimit!");
			}
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

		if (input.getPassword() == null || input.getPassword().isEmpty()) {
			throw new CovidException("Bitte geben Sie ein Passwort an.");
		} else {
			if (input.getPassword().length() <= 5) {
				throw new CovidException("Bitte geben Sie ein Passwort mit mindestesn 6 Zeichen an.");
			} else if (input.getPassword().length() > 20) {
				throw new CovidException("Das angegebene Passwort übersteigt das Zeichenlimit!");
			}
		}

	}
}

package aero.minova.core.application.system.covid.test.print.controller;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import aero.minova.core.application.system.controller.SqlProcedureController;
import aero.minova.core.application.system.controller.SqlViewController;
import aero.minova.core.application.system.covid.test.print.domain.TestPersonInformation;
import aero.minova.core.application.system.covid.test.print.domain.UserInfo;
import aero.minova.core.application.system.domain.Column;
import aero.minova.core.application.system.domain.DataType;
import aero.minova.core.application.system.domain.OutputType;
import aero.minova.core.application.system.domain.Row;
import aero.minova.core.application.system.domain.Table;
import aero.minova.core.application.system.domain.Value;
import lombok.val;

@RestController
public class TestPersonController {

	@Autowired
	SqlViewController sqlViewController;

	@Autowired
	SqlProcedureController sqlProcedureController;

	private final Pattern emailpattern = Pattern.compile("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
	private final Pattern postalcodepattern = Pattern.compile("^0[1-9]\\d\\d(?<!0100)0|0[1-9]\\d\\d[1-9]|[1-9]\\d{3}[0-8]|[1-9]\\d{3}(?<!9999)9$");

	@PostMapping(value = "testPerson/register", produces = "application/json")
	public void registerTestPerson(@RequestBody TestPersonInformation input) throws Exception {

		checkUserInput(input);

		// Überprüfen, ob die Person bereits im System registriert ist
		Table sqlRequest = new Table();
		sqlRequest.setName("xvctsTestPerson");
		sqlRequest.addColumn(new Column("Email", DataType.STRING, OutputType.INPUT));
		{
			val firstRequestParams = new Row();
			sqlRequest.getRows().add(firstRequestParams);
			firstRequestParams.addValue(new Value(input.getEmail(), null));
		}
		// Hiermit wird der unsichere Zugriff ermöglicht.
		val requestingAuthority = new Row();
		requestingAuthority.addValue(new Value(false, "1"));
		requestingAuthority.addValue(new Value(false, "2"));
		requestingAuthority.addValue(new Value(false, "3"));

		// Überprüfen, ob der Benutzer bereits exisitert, da keine Doppelteneiträge vorhanden sein sollten
		List<Row> viewOutput = sqlViewController.unsecurelyGetIndexView(sqlRequest, Arrays.asList(requestingAuthority)).getRows();
		if (!viewOutput.isEmpty()) {
			throw new RuntimeException("Ein Benutzer mit dieser Emailadresse existiert bereits!");
		}

		// TODO Emailverifikation

	}

	@PostMapping(value = "testPerson/finishregistration", produces = "application/json")
	public void finishRegistrationTestPerson(@RequestBody TestPersonInformation input) throws Exception {
		// Anlegen des Benutzers
		Table sqlRequest = new Table();
		sqlRequest.setName("xpctsInsertTestPerson");
		sqlRequest.addColumn(new Column("KeyLong", DataType.INTEGER, OutputType.OUTPUT));
		sqlRequest.addColumn(new Column("FirstName", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("LastName", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("Street", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("PostalCode", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("City", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("Birthdate", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("Phone", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("Phone2", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("Email", DataType.STRING, OutputType.INPUT));
		sqlRequest.addColumn(new Column("Password", DataType.STRING, OutputType.INPUT));
		{
			val firstRequestParams = new Row();
			sqlRequest.getRows().add(firstRequestParams);
			firstRequestParams.addValue(null);
			firstRequestParams.addValue(new Value(input.getFirstname(), null));
			firstRequestParams.addValue(new Value(input.getLastname(), null));
			firstRequestParams.addValue(new Value(input.getStreet(), null));
			firstRequestParams.addValue(new Value(input.getPostalcode(), null));
			firstRequestParams.addValue(new Value(input.getCity(), null));
			firstRequestParams.addValue(new Value(Instant.from(input.getBirthdate()), null));
			firstRequestParams.addValue(new Value(input.getPhonenumber(), null));
			firstRequestParams.addValue(new Value(input.getPhonenumber2(), null));
			firstRequestParams.addValue(new Value(input.getEmail(), null));
			firstRequestParams.addValue(new Value(input.getPassword(), null));
		}
		// Hiermit wird der unsichere Zugriff ermöglicht.
		val requestingAuthority = new Row();
		requestingAuthority.addValue(new Value(false, "1"));
		requestingAuthority.addValue(new Value(false, "2"));
		requestingAuthority.addValue(new Value(false, "3"));

		sqlProcedureController.calculateSqlProcedureResult(sqlRequest);

	}

	@PostMapping(value = "testPerson/login", produces = "application/json")
	public long loginTestPerson(@RequestBody UserInfo info) throws Exception {

		if (info.getEmail().isEmpty()) {
			throw new RuntimeException("Bitte geben Sie Ihre Emailadresse ein.");
		}

		if (info.getPassword().isEmpty()) {
			throw new RuntimeException("Bitte geben Sie Ihr Passwort ein.");
		}

		Table sqlRequest = new Table();
		sqlRequest.setName("xvctsTestPerson");
		sqlRequest.addColumn(new Column("KeyLong", DataType.INTEGER, OutputType.OUTPUT));
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

		if (result.get(0).getValues().size() > 0) {
			// Zurückgeben des KeyLongs
			return Long.valueOf(result.get(0).getValues().get(0).getIntegerValue());
		} else {
			throw new RuntimeException("Fehler beim Login. Bitte überprüfen Sie Ihre angegebene Emailadresse und Ihr Passwort.");
		}

	}

	private void checkUserInput(TestPersonInformation input) {

		if (input.getFirstname() == null || input.getLastname() == null || input.getFirstname().isEmpty() || input.getLastname().isEmpty()) {
			throw new RuntimeException("Bitte geben Sie Ihren Vor- und Nachnamen an.");
		} else if (input.getFirstname().length() > 50) {
			throw new RuntimeException("Der angegebene Vorname übersteigt das Zeichenlimit!");
		} else if (input.getLastname().length() > 50) {
			throw new RuntimeException("Der angegebene Nachname übersteigt das Zeichenlimit!");
		}

		if (input.getStreet() == null || input.getStreet().isEmpty()) {
			throw new RuntimeException("Bitte geben Sie Ihre Adresse an.");
		}
		if (input.getStreet().length() > 50) {
			throw new RuntimeException("Die angegebene Straße übersteigt das Zeichenlimit!");
		}

		if (input.getCity() == null || input.getCity().isEmpty()) {
			throw new RuntimeException("Bitte geben Sie Ihren Wohnort an.");
		} else if (input.getCity().length() > 20) {
			throw new RuntimeException("Der angegebene Wohnort übersteigt das Zeichenlimit!");
		}

		if (input.getPostalcode() != null && !input.getPostalcode().isEmpty()) {
			Matcher matcher = postalcodepattern.matcher(input.getPostalcode());
			if (!matcher.find()) {
				throw new RuntimeException("Die angegebene Postleitzahl hat kein gültiges Format!");
			}
		} else {
			throw new RuntimeException("Bitte geben Sie Ihre Postleitzahl an.");
		}

		if (input.getBirthdate() != null) {
			if (Instant.now().isAfter(Instant.from(input.getBirthdate()))) {
				throw new RuntimeException("Der angegebene Geburtstag ist ungültig!");
			}
		} else {
			throw new RuntimeException("Bitte geben Sie Ihren Geburtstag an.");
		}

		if (input.getPhonenumber() != null && !input.getPhonenumber().isEmpty()) {
			if (!input.getPhonenumber().matches("[0-9]+")) {
				throw new RuntimeException("Die angegebene Telefonnummer ist nicht gültig!");
			} else if (input.getPhonenumber().length() > 20) {
				throw new RuntimeException("Die angegebene Telefonnummer übersteigt das Zeichenlimit!");
			}
		} else {
			throw new RuntimeException("Bitte geben Sie Ihre Telefonnummer an.");
		}

		// Die alternative Telefonnummer darf auch leer sein
		if (input.getPhonenumber2() != null) {
			if (!input.getPhonenumber2().matches("[0-9]+")) {
				throw new RuntimeException("Die angegebene alternative Telefonnummer ist nicht gültig!");
			} else if (input.getPhonenumber2().length() > 20) {
				throw new RuntimeException("Die angegebene alternative Telefonnummer übersteigt das Zeichenlimit!");
			}
		}

		if (input.getEmail() != null && !input.getEmail().isEmpty()) {
			Matcher matcher = emailpattern.matcher(input.getEmail());
			if (!matcher.find()) {
				throw new RuntimeException("Die angegebene E-Mail Adresse hat kein gültiges Format!");
			} else {
				if (input.getEmail().length() > 50) {
					throw new RuntimeException("Die angegebene Email-Adresse übersteigt das Zeichenlimit!");
				}
			}
		} else {
			throw new RuntimeException("Bitte geben Sie Ihre E-Mailadresse an.");
		}

		if (input.getPassword() == null || input.getPassword().isEmpty()) {
			throw new RuntimeException("Bitte geben Sie ein Passwort an.");
		} else {
			if (input.getPassword().length() <= 5) {
				throw new RuntimeException("Bitte geben Sie ein Passwort mit mindestesn 6 Zeichen an.");
			} else if (input.getPassword().length() > 20) {
				throw new RuntimeException("Das angegebene Passwort übersteigt das Zeichenlimit!");
			}
		}

	}
}

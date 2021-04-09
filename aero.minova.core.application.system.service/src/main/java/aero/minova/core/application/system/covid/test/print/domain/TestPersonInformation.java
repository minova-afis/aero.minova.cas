package aero.minova.core.application.system.covid.test.print.domain;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class TestPersonInformation {
	private String firstname;
	private String lastname;
	private String street;
	private String postalcode;
	private String city;
	private LocalDate birthdate;
	private String phonenumber;
	private String phonenumber2;
	private String email;
	private String password;
}

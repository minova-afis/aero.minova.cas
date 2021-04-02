package aero.minova.covid.test.print;

import java.time.LocalDate;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class TimeLocation {
	private String testStreckKeyText;
	private LocalDate date;
}

package aero.minova.core.application.system.covid.test.print.domain;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class TimeLocationRequest {
	private int testStreckKeyLong;
	private LocalDate date;
}

package aero.minova.covid.test.print.domain;

import java.time.LocalDate;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class DateRange {
	private LocalDate startDate;
	private LocalDate endDate;
}

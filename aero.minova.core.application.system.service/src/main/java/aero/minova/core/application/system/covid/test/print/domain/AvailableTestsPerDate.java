package aero.minova.core.application.system.covid.test.print.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class AvailableTestsPerDate {
    private String testStreckKeyText;
    private LocalDate date;
    private int availableTests;
}

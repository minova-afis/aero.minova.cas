package aero.minova.core.application.system.covid.test.print.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class AvailableTestsPerDate {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd")
            .withZone(ZoneId.systemDefault());

    private String testStreckKeyText;
    private String date;
    private int availableTests;

    public void setDate(LocalDate localDate) {
        date = DATE_FORMATTER.format(localDate);
    }
}

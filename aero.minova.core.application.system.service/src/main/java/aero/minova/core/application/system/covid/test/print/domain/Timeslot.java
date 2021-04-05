package aero.minova.core.application.system.covid.test.print.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class Timeslot {
    private String startTime;
    private String endTime;
    private int availableCapacity;

    public Timeslot(ZonedDateTime startTime, ZonedDateTime endTime, int availableCapacity) {
        if (startTime != null) {
            this.startTime = ISO_OFFSET_DATE_TIME.format(startTime);
        }
        if (endTime != null) {
            this.endTime = ISO_OFFSET_DATE_TIME.format(endTime);
        }
        this.availableCapacity = availableCapacity;
    }
}

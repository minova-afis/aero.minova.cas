package aero.minova.core.application.system.covid.test.print.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class Timeslot {
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private int availableCapacity;

	public Timeslot(LocalDateTime startTime, LocalDateTime endTime, int availableCapacity) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.availableCapacity = availableCapacity;
	}
}

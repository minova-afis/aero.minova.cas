package aero.minova.core.application.system.covid.test.print.domain;

import java.time.Instant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class TestTermin {
	private Long keyLong;
	private Integer CTSTeststreckeKey;
	private Integer CTSTestpersonKey;
	private Instant Starttime;
}

package aero.minova.core.application.system.covid.test.print.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class TestTermin {
	private Long keyLong;
	private long CTSTeststreckeKey;
	private Long CTSTestpersonKey;
	private String Starttime;
}

package aero.minova.core.application.system.covid.test.print.domain;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class TestTermin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long keyLong;
	private Integer CTSTeststreckeKey;
	private Integer CTSTestpersonKey;
	private Instant Starttime;
	private Integer LastAction;
}

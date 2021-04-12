package aero.minova.core.application.system.covid.test.print.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class UserInfo {
	private String email;
	private String password;
}

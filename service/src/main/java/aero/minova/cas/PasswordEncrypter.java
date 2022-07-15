package aero.minova.cas;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class PasswordEncrypter {
	public static void main(String... args) {
		log.info(new BCryptPasswordEncoder().encode("Q1HK.m2Cp_gcF9.3O4na"));
	}
}
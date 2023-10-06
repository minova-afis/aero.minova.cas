package aero.minova.cas;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncrypter {
	public static void main(String... args) {
		System.out.println(new BCryptPasswordEncoder().encode("rqgzxTf71EAx8chvchMi"));
	}
}

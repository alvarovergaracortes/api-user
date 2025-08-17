package cl.bci.common.helper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateEncryptedPassword {
	public static void main(String[] args) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println("Admin123: " + encoder.encode("Admin123"));
		System.out.println("User123: " + encoder.encode("User123"));
	}
}

package cl.bci.common.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
	private final PasswordEncoder passwordEncoder;

	public PasswordUtil(PasswordEncoder encoder) {
		this.passwordEncoder = encoder;
	}
	
	public boolean matches(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
	
	public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}

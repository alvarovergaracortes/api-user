package cl.bci.common.annotation;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class PasswordValidator implements ConstraintValidator<PasswordPattern, String> {
	
	@Value("${app.password.regex}")
	private String passwordRegex;
	
	private Pattern pattern;
	
	@Override
	public void initialize(PasswordPattern constraintAnnotation) {
		// El patrón se inicializa en isValid porque @Value no funciona aquí
	}
	
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (password == null) return false;
		
		if (pattern == null) {
			pattern = Pattern.compile(passwordRegex);
		}
		
		return pattern.matcher(password).matches();
	}
}

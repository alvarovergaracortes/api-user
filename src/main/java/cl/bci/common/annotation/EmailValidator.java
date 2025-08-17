package cl.bci.common.annotation;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class EmailValidator implements ConstraintValidator<EmailPattern, String> {
	
	@Value("${app.email.regex}")
	private String emailPattern;
	
	private Pattern pattern;
	
	
	@Override
	public void initialize(EmailPattern constraintAnnotation) {
		pattern = Pattern.compile(emailPattern);
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && pattern.matcher(value).matches();
	}

}

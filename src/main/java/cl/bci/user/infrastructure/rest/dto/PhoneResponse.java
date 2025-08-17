package cl.bci.user.infrastructure.rest.dto;

public record PhoneResponse(String number, 
		String citycode, 
		String contrycode
)
{}

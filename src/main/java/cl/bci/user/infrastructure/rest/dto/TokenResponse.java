package cl.bci.user.infrastructure.rest.dto;

public record TokenResponse(
		String email,
		String token
) {}

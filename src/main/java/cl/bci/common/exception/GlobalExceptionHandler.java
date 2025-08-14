package cl.bci.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cl.bci.common.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler{
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<ErrorResponse> handleDatabaseException(DatabaseException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorResponse> handleUserException(UserException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(EmailException.class)
	public ResponseEntity<ErrorResponse> handleEmailException(EmailException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
	}
	
	
	@ExceptionHandler(InvalidJwtAuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleInvalidJwt(InvalidJwtAuthenticationException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}
	
	
	@ExceptionHandler(JwtValidationException.class)
	public ResponseEntity<ErrorResponse> handleJwtValidationException(JwtValidationException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.findFirst()
				.orElse("Datos inválidos");
		return buildErrorResponse(message, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
		logger.error("Error inesperado en api-users ", ex);
		return buildErrorResponse("Error interno: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
		return buildErrorResponse("No tienes permisos para acceder a este recurso", HttpStatus.FORBIDDEN);
	}
	
	
	private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpStatus status) {
		return new ResponseEntity<>(new ErrorResponse(message), status);
	}

}

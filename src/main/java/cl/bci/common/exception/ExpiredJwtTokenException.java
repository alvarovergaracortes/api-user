package cl.bci.common.exception;

public class ExpiredJwtTokenException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public ExpiredJwtTokenException(String message) {
		super(message);
	}

}

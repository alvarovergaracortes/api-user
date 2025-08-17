package cl.bci.common.dto;

public enum ErrorCode {
	RESOURCE_NOT_FOUND(404),
	INCORRECT_CREDENTIALS(401),
	UNAUTHORIZED_ROLE(401);
	
	private final int httpCode;

	ErrorCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }
}

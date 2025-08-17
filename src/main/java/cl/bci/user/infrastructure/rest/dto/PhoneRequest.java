package cl.bci.user.infrastructure.rest.dto;

import jakarta.validation.constraints.NotBlank;

public class PhoneRequest {
	@NotBlank(message = "El número no puede estar vacío")
	private String number;
	
	@NotBlank(message = "El código de ciudad no puede estar vacío")
	private String citycode;
	
	@NotBlank(message = "El código de país no puede estar vacío")
	private String contrycode;

	public PhoneRequest() {
	}

	public PhoneRequest(String number, String citycode, String contrycode) {
		this.number = number;
		this.citycode = citycode;
		this.contrycode = contrycode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getContrycode() {
		return contrycode;
	}

	public void setContrycode(String contrycode) {
		this.contrycode = contrycode;
	}
}

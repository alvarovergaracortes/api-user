package cl.bci.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Clase para el manejo de errores con el formato JSON")
public class ErrorResponse {
	
	private String mensaje;
	
	public ErrorResponse() {
		
	}

	public ErrorResponse(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

}

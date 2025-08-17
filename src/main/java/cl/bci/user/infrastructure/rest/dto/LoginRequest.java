package cl.bci.user.infrastructure.rest.dto;

import cl.bci.common.annotation.EmailPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
	  @NotBlank(message="El mail del usuario no puede estar vacío")
	  @EmailPattern
	  private String email;

	  @NotBlank(message="La contraseña no puede estar vacía")
	  @Size(min=8, message="La contraseña debe tener al menos 8 caracteres")
	  private String password;

	  public LoginRequest() {
		  
	  }

	  public LoginRequest(@NotBlank(message = "El mail del usuario no puede estar vacío") String email,
			@NotBlank(message = "La contraseña no puede estar vacía") @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String password) {
		this.email = email;
		this.password = password;
	}



	  public String getEmail() {
		  return email;
	  }

	  public void setEmail(String email) {
		  this.email = email;
	  }

	  public String getPassword() {
		  return password;
	  }

	  public void setPassword(String password) {
		  this.password = password;
	  }
	  

}

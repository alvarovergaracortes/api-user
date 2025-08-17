package cl.bci.user.infrastructure.rest.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import cl.bci.common.annotation.EmailPattern;
import cl.bci.common.annotation.PasswordPattern;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class UserRequest {
	@NotEmpty(message = "El nombre no puede estar vacío")
	private String name;
	
	@NotBlank(message = "El correo no puede estar vacio")
	@EmailPattern
	private String email;
	
	@NotBlank(message = "La contraseña no puede estar vacía")
	@PasswordPattern
	private String password;
	
	@JsonProperty("isactive")
	private Boolean active;
	
	@NotEmpty(message = "Debe incluir al menos un teléfono")
	@Valid
    private List<PhoneRequest> phones;
	
	private String roles;
	
	public UserRequest() {
	}
	
	public UserRequest(@NotEmpty(message = "El nombre no puede estar vacío") String name,
			@NotBlank(message = "El correo no puede estar vacio") String email,
			@NotBlank(message = "La contraseña no puede estar vacía") String password, Boolean active,
			@NotEmpty(message = "Debe incluir al menos un teléfono") @Valid List<PhoneRequest> phones, String roles) {
				this.name = name;
				this.email = email;
				this.password = password;
				this.active = active;
				this.phones = phones;
				this.roles = roles;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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


	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<PhoneRequest> getPhones() {
		return phones;
	}

	public void setPhones(List<PhoneRequest> phones) {
		this.phones = phones;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserRequest [name=" + name + ", email=" + email + ", password=" + password + ", isActive=" + active
				+ ", phones=" + phones + ", roles=" + roles + "]";
	}
	
}

package cl.bci.user.infrastructure.persistence.entity;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class UserEntity {    
	@Id
	@JdbcTypeCode(Types.VARCHAR)
	@Column(length = 36, nullable = false, unique = true)
	private UUID id;
	
	private String name;
	private String email;
	private String password;
	private LocalDateTime created;
	private LocalDateTime modified;
	private LocalDateTime lastLogin;
	
	private String roles;
	
	@Column(length = 1000)
	private String token;
	
	@Column(name = "is_active")
	private boolean isActive;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PhoneEntity> phones;
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getModified() {
		return modified;
	}

	public void setModified(LocalDateTime modified) {
		this.modified = modified;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<PhoneEntity> getPhones() {
		return phones;
	}

	public void setPhones(List<PhoneEntity> phones) {
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
		return "UserEntity [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", created="
				+ created + ", modified=" + modified + ", lastLogin=" + lastLogin + ", roles=" + roles + ", token="
				+ token + ", isActive=" + isActive + ", phones=" + phones + "]";
	}
	
	
}

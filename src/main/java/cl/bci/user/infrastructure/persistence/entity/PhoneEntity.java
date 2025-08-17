package cl.bci.user.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="phone")
public class PhoneEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String number;
	private String citycode;
	private String countrycode;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private UserEntity user;
	
	public PhoneEntity() {
		
	}
	
	public PhoneEntity(Long id, String number, String citycode, String countrycode, UserEntity user) {
		this.id = id;
		this.number = number;
		this.citycode = citycode;
		this.countrycode = countrycode;
		this.user = user;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getCountrycode() {
		return countrycode;
	}
	public void setCountrycode(String countrycode) {
		this.countrycode = countrycode;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
    
}

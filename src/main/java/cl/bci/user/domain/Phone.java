package cl.bci.user.domain;

public class Phone {
	private Long id;
	private String number;
	private String citycode;
	private String contrycode;
	
	
	public Phone(Long id, String number, String citycode, String contrycode) {
		this.id = id;
		this.number = number;
		this.citycode = citycode;
		this.contrycode = contrycode;
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
	public String getContrycode() {
		return contrycode;
	}
	public void setContrycode(String contrycode) {
		this.contrycode = contrycode;
	}

}

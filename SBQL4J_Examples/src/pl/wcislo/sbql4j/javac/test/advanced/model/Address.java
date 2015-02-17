package pl.wcislo.sbql4j.javac.test.advanced.model;

public class Address {
	private String city;
	private String street;
	private Integer house;
	
	public Address(String city, String street, Integer house) {
		this.city = city;
		this.street = street;
		this.house = house;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getHouse() {
		return house;
	}

	public void setHouse(Integer house) {
		this.house = house;
	}
	
	
}

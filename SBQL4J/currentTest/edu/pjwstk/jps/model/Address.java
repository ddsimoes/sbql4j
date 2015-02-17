package edu.pjwstk.jps.model;

public class Address {
	private String city;
	private String street;
	private String zip;
	
	public Address(String city, String street, String zip) {
		super();
		this.city = city;
		this.street = street;
		this.zip = zip;
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
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	
}

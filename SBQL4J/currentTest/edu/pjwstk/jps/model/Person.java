package edu.pjwstk.jps.model;

public class Person {
	private String fName;
	private String lName;
	private Integer age;
	private boolean married;
	private Address address;
	private Firm worksIn;
	
	public Person(String name, String name2, Integer age, boolean married,
			Address address
//			, Firm worksIn
			) {
		super();
		fName = name;
		lName = name2;
		this.age = age;
		this.married = married;
		this.address = address;
//		this.worksIn = worksIn;
	}
	
	public Firm getWorksIn() {
		return worksIn;
	}
	public void setWorksIn(Firm worksIn) {
		this.worksIn = worksIn;
	}
	public String getFName() {
		return fName;
	}
	public void setFName(String name) {
		fName = name;
	}
	public String getLName() {
		return lName;
	}
	public void setLName(String name) {
		lName = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public boolean getMarried() {
		return married;
	}
	public void setMarried(boolean married) {
		this.married = married;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	
}

package pl.wcislo.sbql4j.javac.test.linq_comp.model;

import java.util.List;

public class Customer {
    public String customerID;
    public String companyName;
    public String address;
    public String city;
    public String region;
    public String postalCode;
    public String country;
    public String phone;
    public String fax;
    public List<Order> orders;
    
	public Customer(String customerID, String companyName, String address,
			String city, String region, String postalCode, String country,
			String phone, String fax, List<Order> orders) {
		this.customerID = customerID;
		this.companyName = companyName;
		this.address = address;
		this.city = city;
		this.region = region;
		this.postalCode = postalCode;
		this.country = country;
		this.phone = phone;
		this.fax = fax;
		this.orders = orders;
	}
	
	public class InnterTestClass {
		
	}
	
    
}

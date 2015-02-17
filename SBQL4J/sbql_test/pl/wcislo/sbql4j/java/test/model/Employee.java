package pl.wcislo.sbql4j.java.test.model;

public class Employee extends Person {
	private double salary;
	private Address address;
	
	
	public Employee() {
	}
	
	public Employee(String ename, Double salary, Address address) {
		super();
		this.ename = ename;
		this.salary = salary;
		this.address = address;
	}


	
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
}

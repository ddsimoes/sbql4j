package pl.wcislo.sbql4j.javac.test.advanced.model;

import java.util.List;

public class Department {
	private List<Employee> employs;
	private Employee boss;
	private String name;
	private Double budget;
	private List<String> location;
	public Department(List<Employee> employs, Employee boss, String name, Double budget, List<String> location) {
		super();
		this.employs = employs;
		this.boss = boss;
		this.name = name;
		this.budget = budget;
		this.location = location;
	}
	public List<Employee> getEmploys() {
		return employs;
	}
	public void setEmploys(List<Employee> employs) {
		this.employs = employs;
	}
	public Employee getBoss() {
		return boss;
	}
	public void setBoss(Employee boss) {
		this.boss = boss;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getBudget() {
		return budget;
	}
	public void setBudget(Double budget) {
		this.budget = budget;
	}
	public List<String> getLocation() {
		return location;
	}
	public void setLocation(List<String> location) {
		this.location = location;
	}
	
	
}

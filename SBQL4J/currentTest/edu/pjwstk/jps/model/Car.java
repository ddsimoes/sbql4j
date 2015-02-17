package edu.pjwstk.jps.model;

import java.util.Collection;
import java.util.Date;

public class Car {
	private String model;
	private String color;
	private Integer power;
	private Collection<Person> owner;
	private Date year;
	
	public Car(String model, String color, Integer power,
			 Date year) {
		super();
		this.model = model;
		this.color = color;
		this.power = power;
//		this.owner = owner;
		this.year = year;
	}
	

	@Override
	public String toString() {
		return "Car[model="+model+", color="+color+", power="+power+", year="+year+"]";
	}
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getPower() {
		return power;
	}
	public void setPower(Integer power) {
		this.power = power;
	}
	public Collection<Person> getOwner() {
		return owner;
	}
	public void setOwner(Collection<Person> owner) {
		this.owner = owner;
	}
	public Date getYear() {
		return year;
	}
	public void setYear(Date year) {
		this.year = year;
	}
	
	
}

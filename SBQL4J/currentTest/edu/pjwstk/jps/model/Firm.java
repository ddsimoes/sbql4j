package edu.pjwstk.jps.model;

import java.util.ArrayList;
import java.util.List;

public class Firm {
	private String name;
	private Address address;
	private List<Firm> subdiviseions = new ArrayList();
	
	public Firm(String name, Address address) {
		super();
		this.name = name;
		this.address = address;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public List<Firm> getSubdiviseions() {
		return subdiviseions;
	}
	public void setSubdiviseions(List<Firm> subdiviseions) {
		this.subdiviseions = subdiviseions;
	}
}

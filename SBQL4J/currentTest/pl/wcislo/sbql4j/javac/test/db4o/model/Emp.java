package pl.wcislo.sbql4j.javac.test.db4o.model;

import java.util.List;

public class Emp {
	private String number;
	private String fName;
	private String lName;
	private Address address;
	private boolean married;
	private List<Book> books;
	private Integer age;
	
	public Emp() {
	}
	
	public Emp(String number, String fName, String lName, Address address,
			boolean married, List<Book> books) {
		super();
		this.number = number;
		this.fName = fName;
		this.lName = lName;
		this.address = address;
		this.married = married;
		this.books = books;
	}
	
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public boolean isMarried() {
		return married;
	}
	public void setMarried(boolean married) {
		this.married = married;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}


	public static class Address {
		private String street;
		private Integer number;
		private String zip;
		private String city;
		
		public Address() {
		}
		
		public Address(String street, Integer number, String zip, String city) {
			super();
			this.street = street;
			this.number = number;
			this.zip = zip;
			this.city = city;
		}
		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		public Integer getNumber() {
			return number;
		}
		public void setNumber(Integer number) {
			this.number = number;
		}
		public String getZip() {
			return zip;
		}
		public void setZip(String zip) {
			this.zip = zip;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		
		
	}
	public static class Book {
		private String author;
		private String title;
		
		public Book() {
		}
		
		public Book(String author, String title) {
			super();
			this.author = author;
			this.title = title;
		}
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		
	}
}

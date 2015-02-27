package pl.wcislo.sbql4j.javac.test.inheritance.model;

public class Osoba {
	private String imie;
	private String nazwisko;

	public Osoba(String firstName, String lastName) {
		super();
		this.imie = firstName;
		this.nazwisko = lastName;
	}
	
	public String getImie() {
		return imie;
	}
	public void setImie(String firstName) {
		this.imie = firstName;
	}
	public String getNazwisko() {
		return nazwisko;
	}
	public void setNazwisko(String lastName) {
		this.nazwisko = lastName;
	}
}

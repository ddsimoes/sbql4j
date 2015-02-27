package pl.wcislo.sbql4j.javac.test.inheritance.model;

public class Pracownik extends Osoba {
	private int pensja;
	
	public Pracownik(String firstName, String lastName, int pensja) {
		super(firstName, lastName);
		this.pensja = pensja;
	}

	public int getPensja() {
		return pensja;
	}

	public void setPensja(int pensja) {
		this.pensja = pensja;
	}
	
}

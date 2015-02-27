package pl.wcislo.sbql4j.javac.test.struct.model;

public class Pracownik extends Osoba {
	private int pensja;
	private Firma firma;
	
	public Pracownik(String firstName, String lastName, int pensja, Firma firma) {
		super(firstName, lastName);
		this.pensja = pensja;
		this.firma = firma;
	}

	public int getPensja() {
		return pensja;
	}

	public void setPensja(int pensja) {
		this.pensja = pensja;
	}
	
	public Firma getFirma() {
		return firma;
	}
	public void setFirma(Firma firma) {
		this.firma = firma;
	}
	
}

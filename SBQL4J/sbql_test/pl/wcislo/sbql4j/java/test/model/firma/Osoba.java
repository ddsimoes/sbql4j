package pl.wcislo.sbql4j.java.test.model.firma;

public class Osoba {
	private String imie;
	private String nazwisko;
	
	public Osoba() {
	}
	
	public Osoba(String imie, String nazwisko) {
		this.imie = imie;
		this.nazwisko = nazwisko;
	}
	
	public String getImie() {
		return imie;
	}
	public void setImie(String imie) {
		this.imie = imie;
	}
	public String getNazwisko() {
		return nazwisko;
	}
	public void setNazwisko(String nazwisko) {
		this.nazwisko = nazwisko;
	}
	
	
}

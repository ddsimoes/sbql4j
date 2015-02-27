package pl.wcislo.sbql4j.javac.test.football.model;
import java.util.ArrayList;


public abstract class Osoba {
	
	 protected String imie;
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
	public String getAdres_zamieszkania() {
		return adres_zamieszkania;
	}
	public void setAdres_zamieszkania(String adres_zamieszkania) {
		this.adres_zamieszkania = adres_zamieszkania;
	}
	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	public int getPensja() {
		return pensja;
	}
	public void setPensja(int pensja) {
		this.pensja = pensja;
	}


	private String nazwisko;
	 private String adres_zamieszkania;
	 private String telefon;
	 protected int pensja;
	
	
	
	public Osoba(String imie, String nazwisko, String adres_zamieszkania, String telefon, int pensja){		
		this.imie=imie;
		this.nazwisko=nazwisko;
		this.adres_zamieszkania=adres_zamieszkania;
		this.telefon=telefon;
		this.pensja=pensja;
	 
		
	}
	public Osoba(String imie, String nazwisko){		
		this.imie=imie;
		this.nazwisko=nazwisko;
		
	}
	
	public Osoba(String imie, String nazwisko,int pensja){		
		this.imie=imie;
		this.nazwisko=nazwisko;
		this.pensja = pensja;
	}
	public void wyliczPensje() {
	}
	
	
	public String getimie() {
		return imie;
		  
		} 
}

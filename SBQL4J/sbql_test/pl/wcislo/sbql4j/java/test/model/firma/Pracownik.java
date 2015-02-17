package pl.wcislo.sbql4j.java.test.model.firma;

import java.util.ArrayList;
import java.util.List;

public class Pracownik extends Osoba {
	private String stanowisko;
	private List<Zatrudnienie> pracujeW = new ArrayList<Zatrudnienie>();

	public Pracownik() {
	}
	
	public Pracownik(String imie, String nazwisko, String stanowisko) {
		super(imie, nazwisko);
		this.stanowisko = stanowisko;
	}

	public String getStanowisko() {
		Pracownik prac = new Pracownik();
		String s = "programista";
		prac.setStanowisko(s);
		return stanowisko;
	}
	public void setStanowisko(String stanowisko) {
		this.stanowisko = stanowisko;
	}
	public List<Zatrudnienie> getPracujeW() {
		return pracujeW;
	}
	public void setPracujeW(List<Zatrudnienie> pracujeW) {
		this.pracujeW = pracujeW;
	}
}

package pl.wcislo.sbql4j.java.test.model.firma;

import java.util.Date;

public class Zatrudnienie {
	private Pracownik pracownik;
	private Firma firma;
	private double pensja;
	private Date dataZatrudnienia;
	
	public Zatrudnienie(Pracownik pracownik, Firma firma, double pensja, Date dataZatrudnienia) {
		this.pracownik = pracownik;
		this.firma = firma;
		this.pensja = pensja;
		this.dataZatrudnienia = dataZatrudnienia;
		
		pracownik.getPracujeW().add(this);
		firma.getZatrudnia().add(this);
	}
	public Zatrudnienie() {
	}
	
	public Pracownik getPracownik() {
		return pracownik;
	}
	public void setPracownik(Pracownik pracownik) {
		this.pracownik = pracownik;
	}
	public Firma getFirma() {
		return firma;
	}
	public void setFirma(Firma firma) {
		this.firma = firma;
	}
	public double getPensja() {
		return pensja;
	}
	public void setPensja(double pensja) {
		this.pensja = pensja;
	}
	public Date getDataZatrudnienia() {
		return dataZatrudnienia;
	}
	public void setDataZatrudnienia(Date dataZatrudnienia) {
		this.dataZatrudnienia = dataZatrudnienia;
	}
	
	
}

package pl.wcislo.sbql4j.javac.test.football.model;

import java.util.ArrayList;
import java.util.Date;

public class Lekarz extends Osoba {

	private String specjalizacja;

	public String getSpecjalizacja() {
		return specjalizacja;
	}

	public void setSpecjalizacja(String specjalizacja) {
		this.specjalizacja = specjalizacja;
	}

	public Date getData_zatrudnienia() {
		return Data_zatrudnienia;
	}

	public void setData_zatrudnienia(Date data_zatrudnienia) {
		Data_zatrudnienia = data_zatrudnienia;
	}

	public Date getData_zwolnienia() {
		return Data_zwolnienia;
	}

	public void setData_zwolnienia(Date data_zwolnienia) {
		Data_zwolnienia = data_zwolnienia;
	}

	public int getPremia() {
		return premia;
	}

	public void setPremia(int premia) {
		this.premia = premia;
	}

	public Druzyna getD() {
		return d;
	}

	public void setD(Druzyna d) {
		this.d = d;
	}

	private Date Data_zatrudnienia;
	private Date Data_zwolnienia;
	private int premia;
	private Druzyna d;
	ArrayList<Lekarz> listaLekarzy = new ArrayList<Lekarz>();
	ArrayList<Zabieg> zabieg;

	public Lekarz(String imie, String nazwisko, String adres_zamieszkania,
			String telefon, int pensja, String specjalizacja,
			Date Data_zatrudnienia, int premia, Zabieg zabieg) {
		super(imie, nazwisko, adres_zamieszkania, telefon, pensja);

		this.specjalizacja = specjalizacja;
		this.Data_zatrudnienia = Data_zatrudnienia;
		this.premia = premia;
		this.zabieg.add(zabieg);
	}

	public Lekarz(String imie, String nazwisko, String adres_zamieszkania,
			String telefon, int pensja, String specjalizacja,
			Date Data_zatrudnienia, int premia) {
		super(imie, nazwisko, adres_zamieszkania, telefon, pensja);
		this.specjalizacja = specjalizacja;
		this.Data_zatrudnienia = Data_zatrudnienia;
		this.premia = premia;

	}

	public Lekarz(String imie, String nazwisko, String adres_zamieszkania,
			String telefon, int pensja, String specjalizacja, int id_lekarza,
			Date Data_zatrudnienia, Date Data_zwolnienia, int premia,
			Zabieg zabieg) {
		super(imie, nazwisko, adres_zamieszkania, telefon, pensja);
		this.specjalizacja = specjalizacja;
		this.Data_zatrudnienia = Data_zatrudnienia;
		this.Data_zwolnienia = Data_zwolnienia;
		this.premia = premia;
		this.zabieg.add(zabieg);

	}

	public void wyliczPensje(int premia, int pensja) {
		pensja = pensja + premia;

	}

	public void dodajLekarza(Lekarz lekarz) {
		listaLekarzy.add(lekarz);

	}

	public void edytujLekarza() {

	}

	public void dodajLekarzadoDruzyny(Druzyna druzyna) {

		if (this.d == null) {
			this.d = druzyna;
		}

	}

	@Override
	public String toString() {
		return "Lekarz ["
				+ (specjalizacja != null ? "specjalizacja=" + specjalizacja
						+ ", " : "")
				+ (Data_zatrudnienia != null ? "Data_zatrudnienia="
						+ Data_zatrudnienia + ", " : "")
				+ (Data_zwolnienia != null ? "Data_zwolnienia="
						+ Data_zwolnienia + ", " : "") + "premia=" + premia
				+ ", " + (zabieg != null ? "zabieg=" + zabieg : "") + "]";
	}

	public void dodajZabieg(Zabieg g) {
		if (this.zabieg == null) {
			this.zabieg = new ArrayList<Zabieg>();
			this.zabieg.add(g);

		} else {
			if (!this.zabieg.contains(g)) {
				this.zabieg.add(g);

			}
		}
	}

}

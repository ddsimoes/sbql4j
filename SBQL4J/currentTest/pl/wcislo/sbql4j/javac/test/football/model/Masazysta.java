package pl.wcislo.sbql4j.javac.test.football.model;
import java.util.ArrayList;
import java.util.Date;


public class Masazysta extends Osoba{
	
	private String specjalizacja;
	private Date Data_zatrudnienia;
	private Date Data_zwolnienia;
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

	public int getId_masazysty() {
		return id_masazysty;
	}

	public void setId_masazysty(int id_masazysty) {
		this.id_masazysty = id_masazysty;
	}

	public Druzyna getDruzyna() {
		return druzyna;
	}

	public void setDruzyna(Druzyna druzyna) {
		this.druzyna = druzyna;
	}

	public ArrayList<Masazysta> getListaMasazystow() {
		return listaMasazystow;
	}

	public void setListaMasazystow(ArrayList<Masazysta> listaMasazystow) {
		this.listaMasazystow = listaMasazystow;
	}

	private int id_masazysty;
	private  Druzyna druzyna;
	ArrayList<Masazysta> listaMasazystow = new ArrayList<Masazysta>();

	public Masazysta(String imie, String nazwisko, String adres_zamieszkania,
			String telefon, int pensja,  Date Data_zatrudnienia,
			Date Data_zwolnienia, Druzyna druzyna) {
		super(imie, nazwisko, adres_zamieszkania, telefon, pensja);
		this.Data_zatrudnienia=Data_zatrudnienia;
		this.Data_zwolnienia=Data_zwolnienia;
		this.specjalizacja=specjalizacja;
		this.id_masazysty=id_masazysty;
		this.druzyna=druzyna;
	 
	}
	
	public Masazysta(String imie, String nazwisko, String adres_zamieszkania,
			String telefon, int pensja,  Date Data_zatrudnienia) {
		super(imie, nazwisko, adres_zamieszkania, telefon, pensja);
		this.Data_zatrudnienia=Data_zatrudnienia;
		this.Data_zwolnienia=Data_zwolnienia;
		this.specjalizacja=specjalizacja;
		this.id_masazysty=id_masazysty;
	 
	}
	
	public void wyliczPensje(int premia, int pensja) {	
		pensja = pensja +premia;
		
	}
 
	public void dodajMasazyste(Druzyna d){
		this.druzyna=d;
	
		
	}
	
	@Override
	public String toString() {
		return "Masazysta "+this.getImie() + " " + getNazwisko();
	}

	public void edytujMasazyste(){
		
	}
	
	public void dodajMasazysteDoDruzyny(Masazysta masazysta){
	 
		
	}
	
	public void usunjMasazysteZDruzyny(Masazysta masazysta){
		 
		
	}
	
	public int wyliczLiczbeMasazystow(){
		return	listaMasazystow.size();
	}

}

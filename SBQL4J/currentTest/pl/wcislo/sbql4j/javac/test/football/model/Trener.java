package pl.wcislo.sbql4j.javac.test.football.model;
import java.util.ArrayList;
import java.util.Date;


public class Trener extends Osoba{
	
	@Override
	public String toString() {
		return "Trener [Data_zatrudnienia=" + Data_zatrudnienia
				+ ", Data_zwolnienia=" + Data_zwolnienia + ", premia=" + premia
				 + "]";
	}

	private	Date Data_zatrudnienia;
	private Date Data_zwolnienia;
	private int premia;
	private int id_trenera;
	private Druzyna d;
	ArrayList<Trener> listaTrenerow = new ArrayList<Trener>();

	public Trener(String imie, String nazwisko, String adres_zamieszkania,
			String telefon, int pensja,  Date Data_zatrudnienia,
			Date Data_zwolnienia,int id_trenera) {
		super(imie, nazwisko, adres_zamieszkania, telefon, pensja);
		this.Data_zatrudnienia=Data_zatrudnienia;
		this.Data_zwolnienia=Data_zwolnienia;
		 
	}
	
	public Trener(String imie, String nazwisko, String adres_zamieszkania,
			String telefon, int pensja, Date Data_zatrudnienia) {
		super(imie, nazwisko, adres_zamieszkania, telefon, pensja);
		this.Data_zatrudnienia=Data_zatrudnienia;
 
		 
	}
	public void wyliczPensje(int premia, int pensja) {	
		pensja = pensja +premia;
		
	}
	
	public void dodajTrenera(Trener trener){
		listaTrenerow.add(trener);		
		
	}
	
	public void edytujTrenera(){
		
	}
	
	public void dodajTreneradoDruzyny(Druzyna d){
		d.dodajTrenera(this);
		this.d=d;
		
	}
	
	public void usunTreneraZDruzyny(){
		
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

	public int getId_trenera() {
		return id_trenera;
	}

	public void setId_trenera(int id_trenera) {
		this.id_trenera = id_trenera;
	}

	public ArrayList<Trener> getListaTrenerow() {
		return listaTrenerow;
	}

	public void setListaTrenerow(ArrayList<Trener> listaTrenerow) {
		this.listaTrenerow = listaTrenerow;
	}

}

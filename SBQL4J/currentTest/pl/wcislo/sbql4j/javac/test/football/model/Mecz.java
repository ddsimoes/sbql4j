package pl.wcislo.sbql4j.javac.test.football.model;
import java.util.ArrayList;
import java.util.Date;


public class Mecz {
	private String wynik;
	private String miejsce;
	private String nazwa_stadionu;
	private Date data_meczu ; //zlozony
	private Druzyna druzyna;
	private Druzyna druzyna2;
	private ArrayList<Mecz> listaMeczy = new ArrayList<Mecz>(); 
	
	public Mecz(String miejsce, String nazwa_stadionu, Date data_meczu, Druzyna druzyna,Druzyna druzyna2,String wynik){
		
		this.miejsce=miejsce;
		this.nazwa_stadionu=nazwa_stadionu;
		this.data_meczu=data_meczu;	
		this.druzyna=druzyna;
		this.druzyna2=druzyna2;
		this.wynik=wynik;
		druzyna.DodajMecz(this);
		druzyna2.DodajMecz(this);
	}

	
	public String toString(){
		return "Mecz odb�dzie si� w: " + miejsce + " na stadionie: " + " w terminie: "+
		data_meczu;
	}
	
	public void dodajMecz(Mecz mecz){
		listaMeczy.add(mecz);
		
	}
	
	public void edytujMecz(){
		
	}


	public String getMiejsce() {
		return miejsce;
	}


	public void setMiejsce(String miejsce) {
		this.miejsce = miejsce;
	}


	public String getNazwa_stadionu() {
		return nazwa_stadionu;
	}


	public void setNazwa_stadionu(String nazwa_stadionu) {
		this.nazwa_stadionu = nazwa_stadionu;
	}


	public Date getData_meczu() {
		return data_meczu;
	}


	public String getWynik() {
		return wynik;
	}


	public void setWynik(String wynik) {
		this.wynik = wynik;
	}


	public void setData_meczu(Date data_meczu) {
		this.data_meczu = data_meczu;
	}


	public Druzyna getDruzyna() {
		return druzyna;
	}


	public void setDruzyna(Druzyna druzyna) {
		this.druzyna = druzyna;
	}


	public Druzyna getDruzyna2() {
		return druzyna2;
	}


	public void setDruzyna2(Druzyna druzyna2) {
		this.druzyna2 = druzyna2;
	}


	public ArrayList<Mecz> getListaMeczy() {
		return listaMeczy;
	}


	public void setListaMeczy(ArrayList<Mecz> listaMeczy) {
		this.listaMeczy = listaMeczy;
	}
}

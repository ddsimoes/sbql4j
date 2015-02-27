package pl.wcislo.sbql4j.javac.test.football.model;
import java.util.ArrayList;
import java.util.Calendar;


public class Pilkarz extends Osoba {
 
	
	
	
	private int wiek; //atrybut pochodny --> funkcja
	private Druzyna druzyna; //licznosc 1
	private int dlugosc_kontraktu;//atrybut opcjonalny 
	private Data data_urodz = new Data(); // atrybut z�o�ony
	private ArrayList<Kontuzja> kontuzja; //licznosc *
	private int premia;
	private ArrayList<Druzyna> druzyna2 = new ArrayList<Druzyna>();
	 
	
	//przeciazenie
	public Pilkarz(String imie, String nazwisko, int wiek, Druzyna druzyna, int dlugosc_kontraktu,
			Data data_urodz, int premia, Kontuzja kontuzja){
		super(imie, nazwisko);
		
		this.wiek=wiek;
		this.druzyna=druzyna;
		this.dlugosc_kontraktu=dlugosc_kontraktu;
		this.data_urodz=data_urodz;
		this.premia=premia;
		this.kontuzja.add(kontuzja);
		
	}
	
	public Pilkarz(String imie, String nazwisko, int wiek, Druzyna druzyna, int dlugosc_kontraktu,
			Data data_urodz, int premia, Kontuzja kontuzja,String adres_zamieszkania,String telefon,int pensja  ){
		super(imie, nazwisko, adres_zamieszkania, telefon, pensja);
		this.wiek=wiek;
		this.premia=premia;
		this.data_urodz=data_urodz;
	 
	}
	 
	public Pilkarz(String imie, String nazwisko,int wiek,  Data data_urodz, int premia,int pensja){
		super(imie, nazwisko, pensja);
		this.wiek=wiek;
		this.premia=premia;
		this.data_urodz=data_urodz;
	 
	}
	//metoda klasowa
	/*public static void Wyswietl() {		
		System.out.println("Imie: " + imie + " nazwisko: " + nazwisko);
	}*/
 
	
	public void pokazWiek() {

		int rok = Calendar.getInstance().get(Calendar.YEAR);
		int wiek = rok - data_urodz.pokazRok();		 
		System.out.print(wiek);
	}
	@Override
	public String toString() {
		return "Pilkarz [imie="+imie+"wiek=" + wiek + ", druzyna=" + druzyna.getNazwa()
				+ ", dlugosc_kontraktu=" + dlugosc_kontraktu + ", data_urodz="
				+ data_urodz + ", kontuzja=" + kontuzja + ", premia=" + premia+ ",pensja="+pensja+ "]";
	}

	public void wyliczPensje(int premia, int pensja) {	
		pensja = pensja +premia;
		
	}

	public void dodajDruzyne(Druzyna nowaDruzyna){
		
		if(!druzyna2.contains(nowaDruzyna)){
			druzyna2.add(nowaDruzyna);
			
			this.druzyna = nowaDruzyna;
			nowaDruzyna.dodajZawodnika(this);
		}				
	}
	public void dodajKontuzje(Kontuzja k){
		if (kontuzja !=null){
			
		
		if(!kontuzja.contains(k)){
			kontuzja.add(k);
			
			
		}
		}
		else
		{
			kontuzja = new ArrayList<Kontuzja>();
			kontuzja.add(k);
		}
	}

	public int getWiek() {
		return wiek;
	}

	public void setWiek(int wiek) {
		this.wiek = wiek;
	}

	public Druzyna getDruzyna() {
		return druzyna;
	}

	public void setDruzyna(Druzyna druzyna) {
		this.druzyna = druzyna;
	}

	public int getDlugosc_kontraktu() {
		return dlugosc_kontraktu;
	}

	public void setDlugosc_kontraktu(int dlugosc_kontraktu) {
		this.dlugosc_kontraktu = dlugosc_kontraktu;
	}

	public Data getData_urodz() {
		return data_urodz;
	}

	public void setData_urodz(Data data_urodz) {
		this.data_urodz = data_urodz;
	}

	public ArrayList<Kontuzja> getKontuzja() {
		return kontuzja;
	}

	public void setKontuzja(ArrayList<Kontuzja> kontuzja) {
		this.kontuzja = kontuzja;
	}

	public int getPremia() {
		return premia;
	}

	public void setPremia(int premia) {
		this.premia = premia;
	}

	public ArrayList<Druzyna> getDruzyna2() {
		return druzyna2;
	}

	public void setDruzyna2(ArrayList<Druzyna> druzyna2) {
		this.druzyna2 = druzyna2;
	}
	
	

}

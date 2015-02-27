package pl.wcislo.sbql4j.javac.test.football.model;
import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.ListModel;


public class Druzyna {
	
	private	 String nazwa;
	private int liczba_zawodnikow;
	private String miasto;
	private static int min_liczba_zawodnikow;//atrybut klasowy
	private ArrayList<Pilkarz> pilkarz; //licznosc *
	private ArrayList<Masazysta> masazysta; //licznosc *
	private ArrayList<Mecz> mecz; //licznosc *
	private ArrayList<Stroj> stroj; //licznosc *
	private ArrayList<Oboz> obozy; //licznosc *
	private ArrayList<Pilkarz> lista_zawodnikow = new ArrayList<Pilkarz>(); 
	private  ArrayList<Trener> listaTrenerowWDruzynie = new ArrayList<Trener>();  
	private  ArrayList<Lekarz> listalekarzy = new ArrayList<Lekarz>();
	//private ArrayList<Stroj> listastrojow;  
	
	public Druzyna(String nazwa, int liczba_zawodnikow, String miasto, Pilkarz pilkarz, 
			Masazysta masazysta, Mecz mecz, Stroj stroj){
		
		this.nazwa=nazwa;
		this.liczba_zawodnikow=liczba_zawodnikow;
		this.miasto=miasto;
		this.pilkarz.add(pilkarz);
		this.masazysta.add(masazysta);
		this.mecz.add(mecz);
		this.stroj.add(stroj);
	}
	
	public Druzyna(String nazwa, int liczba_zawodnikow, String miasto, Pilkarz pilkarzj){
		
		this.nazwa=nazwa;
		this.liczba_zawodnikow=liczba_zawodnikow;
		this.miasto=miasto;
 
	}
public Druzyna(String nazwa, int liczba_zawodnikow, String miasto){
		
		this.nazwa=nazwa;
		this.liczba_zawodnikow=liczba_zawodnikow;
		this.miasto=miasto;
 
	}
	public void dodajZawodnika(Pilkarz nowyZawodnik){
	 if(!lista_zawodnikow.contains(nowyZawodnik))
			lista_zawodnikow.add(nowyZawodnik);
			 
	}
	
	public ListModel wyswietlZawodnikow() {
		
	
		//System.out.print("Lista zawodnikow: " + lista_zawodnikow.size());
		
		for (Pilkarz lista : lista_zawodnikow)
			System.out.println(lista + "\n");
		return null;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public int getLiczba_zawodnikow() {
		return liczba_zawodnikow;
	}

	public void setLiczba_zawodnikow(int liczba_zawodnikow) {
		this.liczba_zawodnikow = liczba_zawodnikow;
	}

	public String getMiasto() {
		return miasto;
	}

	public void setMiasto(String miasto) {
		this.miasto = miasto;
	}

	public static int getMin_liczba_zawodnikow() {
		return min_liczba_zawodnikow;
	}

	public static void setMin_liczba_zawodnikow(int min_liczba_zawodnikow) {
		Druzyna.min_liczba_zawodnikow = min_liczba_zawodnikow;
	}

	public ArrayList<Pilkarz> getPilkarz() {
		return pilkarz;
	}

	public void setPilkarz(ArrayList<Pilkarz> pilkarz) {
		this.pilkarz = pilkarz;
	}

	public ArrayList<Masazysta> getMasazysta() {
		return masazysta;
	}

	public void setMasazysta(ArrayList<Masazysta> masazysta) {
		this.masazysta = masazysta;
	}
	public void dodajMasazyste(Masazysta m){
		if (masazysta !=null){
			if (!masazysta.contains(m)){
			masazysta.add(m);
			}
		}
		else {
			masazysta = new ArrayList<Masazysta>();
			masazysta.add(m);
		}
	}

	public ArrayList<Mecz> getMecz() {
		return mecz;
	}

	public void setMecz(ArrayList<Mecz> mecz) {
		this.mecz = mecz;
	}

	public ArrayList<Stroj> getStroj() {
		return stroj;
	}

	public void setStroj(ArrayList<Stroj> stroj) {
		this.stroj = stroj;
	}

	public ArrayList<Pilkarz> getLista_zawodnikow() {
		return lista_zawodnikow;
	}

	public void setLista_zawodnikow(ArrayList<Pilkarz> lista_zawodnikow) {
		this.lista_zawodnikow = lista_zawodnikow;
	}

	public  ArrayList<Trener> getListaTrenerowWDruzynie() {
		return listaTrenerowWDruzynie;
	}

	public  void setListaTrenerowWDruzynie(
			ArrayList<Trener> listaTrenerowWDruzynie) {
		this.listaTrenerowWDruzynie = listaTrenerowWDruzynie;
	}

	@Override
	public String toString() {
		return "Druzyna [nazwa=" + nazwa + ", liczba_zawodnikow="
				+ liczba_zawodnikow + ", miasto=" + miasto + ", masazysta="
				+ masazysta + ", mecz=" + mecz + ", stroj=" + stroj
				+ ", lista_zawodnikow=" + lista_zawodnikow
				+ ", listaTrenerowWDruzynie=" + listaTrenerowWDruzynie.toString() 
				+ ", Lista Lekarzy=" + listalekarzy.toString()  + ", Lista Obozow=" + obozy.toString()+ "]";
	}

public void DodajMecz(Mecz m ){
	if (mecz==null){
		mecz = new ArrayList<Mecz>();
		mecz.add(m);
		
	}
	else{
		if (!mecz.contains(m)){
			mecz.add(m);
		}
	}
}

public void dodajTrenera(Trener trener) {
	// TODO Auto-generated method stub
	if (this.listaTrenerowWDruzynie==null){
		this.listaTrenerowWDruzynie = new ArrayList<Trener>();
		this.listaTrenerowWDruzynie.add(trener);
		
	}
	else {
		if (!this.listaTrenerowWDruzynie.contains(trener)){
			this.listaTrenerowWDruzynie.add(trener);
			
		}
	}
	
}
public void dodajLekarza(Lekarz lekarz) {
	// TODO Auto-generated method stub
	if (this.listalekarzy==null){
		this.listalekarzy = new ArrayList<Lekarz>();
		this.listalekarzy.add(lekarz);
		
	}
	else {
		if (!this.listalekarzy.contains(lekarz)){
			this.listalekarzy.add(lekarz);
			
		}
	}
	
}

public void dodajStroj(Stroj s) {
	
	if (this.stroj==null){
		this.stroj = new ArrayList<Stroj>();
		this.stroj.add(s);
		
	}
	else {
		if (!this.stroj.contains(s)){
			this.stroj.add(s);
			
		}
	}
	
}	
	


public void dodajOboz(Oboz s) {
	
	if (this.obozy==null){
		this.obozy = new ArrayList<Oboz>();
		this.obozy.add(s);
		
	}
	else {
		if (!this.obozy.contains(s)){
			this.obozy.add(s);
			
		}
	}
	
}

public ArrayList<Oboz> getObozy() {
	return obozy;
}

public void setObozy(ArrayList<Oboz> obozy) {
	this.obozy = obozy;
}

public ArrayList<Lekarz> getListalekarzy() {
	return listalekarzy;
}

public void setListalekarzy(ArrayList<Lekarz> listalekarzy) {
	this.listalekarzy = listalekarzy;
}	


}

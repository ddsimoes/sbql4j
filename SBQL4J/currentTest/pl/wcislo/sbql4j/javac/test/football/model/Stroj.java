package pl.wcislo.sbql4j.javac.test.football.model;
import java.util.HashSet;
import java.util.Vector;


public class Stroj {
	
	private String kolor;
	private String rozmiar;
	private Korki firma;
	private Druzyna druzyna;
	
	public Stroj(String kolor, String rozmiar, Druzyna druzyna){
		this.kolor=kolor;
		this.rozmiar=rozmiar;
		this.druzyna=druzyna;

		
	}
 
	public String toString(){
		String inf = "Kolor " + kolor + " rozmiar "+ rozmiar + "\n";
			 
		return inf;		
	}
	//BAG- vector
	private Vector<Korki> kor = new Vector<Korki>();
	private static HashSet<Korki> wszystkieKorki = new HashSet<Korki>();

	public void dodajKorki(Korki korki) throws Exception {
		if (!kor.contains(korki)) {
			// Sprawdz czy ta czesc nie zostala dodana do jakiejs calosci
			if (wszystkieKorki.contains(korki)) {
				throw new Exception("Ta czesc jest juz powiazana z caloscia!");
			}

			kor.add(korki);

			// Zapamietaj na liscie wszystkich czesci (przeciwdziala
			// wspoldzielniu czesci)
			wszystkieKorki.add(korki);
		}
	}
	
	public void WyswietlStroj() {
	 
			System.out.println(wszystkieKorki);

	}

	public String getKolor() {
		return kolor;
	}

	public void setKolor(String kolor) {
		this.kolor = kolor;
	}

	public String getRozmiar() {
		return rozmiar;
	}

	public void setRozmiar(String rozmiar) {
		this.rozmiar = rozmiar;
	}

	public Korki getFirma() {
		return firma;
	}

	public void setFirma(Korki firma) {
		this.firma = firma;
	}

	public Druzyna getDruzyna() {
		return druzyna;
	}

	public void setDruzyna(Druzyna druzyna) {
		this.druzyna = druzyna;
	}

	public Vector<Korki> getKor() {
		return kor;
	}

	public void setKor(Vector<Korki> kor) {
		this.kor = kor;
	}

	public static HashSet<Korki> getWszystkieKorki() {
		return wszystkieKorki;
	}

	public static void setWszystkieKorki(HashSet<Korki> wszystkieKorki) {
		Stroj.wszystkieKorki = wszystkieKorki;
	}
	

}

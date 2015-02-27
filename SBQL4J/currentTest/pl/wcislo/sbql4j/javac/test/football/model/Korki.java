package pl.wcislo.sbql4j.javac.test.football.model;
public class Korki {
	 
	//atrybuty
	private String firma;
	private Stroj calosc;
 
 
	//konstruktuor
	public Korki(Stroj calosc, String firma) {
		 
		this.firma = firma;
		this.calosc = calosc;
		 

	}
	//standardowo przeslonieta metoda toStrnig, tak aby moc wypisac informacje o obiekcie
	public String toString(){
		return "Firma kork�w: " + firma + " Stroj: " + calosc;
	}

	//metoda do utworzenia czesci stroju, do kompozycji
	public static Korki utworzKorki(Stroj calosc, String firma) throws Exception {
		
		if (calosc == null) {
			throw new Exception("Stroj niekompletny!");
		}

		// Utwocz nowa czesc
		Korki kr = new Korki(calosc, firma);

		// Dodaj do calosci
		calosc.dodajKorki(kr);

		return kr;
	}
	public String getFirma() {
		return firma;
	}
	public void setFirma(String firma) {
		this.firma = firma;
	}
	public Stroj getCalosc() {
		return calosc;
	}
	public void setCalosc(Stroj calosc) {
		this.calosc = calosc;
	}
}

package pl.wcislo.sbql4j.javac.test.football.model;
import java.util.ArrayList;


public class PilkarzMecz {
	
	private int liczba_zoltych_kartek;
	private int liczba_czerwonych_kartek;
	private int ilosc_goli;
	private boolean czyste_konto;
	private Pilkarz pilkarz;
	private Mecz mecz;
	
	public ArrayList<PilkarzMecz> PilkarzMecz= new ArrayList<PilkarzMecz>();
	
	public PilkarzMecz(int liczba_zoltych_kartek,int liczba_czerwonych_kartek, int ilosc_goli,boolean czyste_konto,
			Pilkarz pilkarz, Mecz mecz){
		this.liczba_zoltych_kartek=liczba_zoltych_kartek;
		this.liczba_czerwonych_kartek=liczba_czerwonych_kartek;
		this.ilosc_goli=ilosc_goli;
		this.czyste_konto=czyste_konto;
		this.pilkarz=pilkarz;
		this.mecz=mecz;
	} 
	
	public void DodajPilkarzaWMeczu(PilkarzMecz pilkarz){
		PilkarzMecz.add(pilkarz);
	}
	
	@Override
	public String toString() {
		return "PilkarzMecz [liczba_zoltych_kartek=" + liczba_zoltych_kartek
				+ ", liczba_czerwonych_kartek=" + liczba_czerwonych_kartek
				+ ", ilosc_goli=" + ilosc_goli + ", czyste_konto="
				+ czyste_konto + ", "
				+ (pilkarz != null ? "pilkarz=" + pilkarz + ", " : "")
				+ (mecz != null ? "mecz=" + mecz : "") + "]";
	}

	public void EdytujPilkarzaWMeczu(Pilkarz pilkarz){
	 
	}

	public int getLiczba_zoltych_kartek() {
		return liczba_zoltych_kartek;
	}

	public void setLiczba_zoltych_kartek(int liczba_zoltych_kartek) {
		this.liczba_zoltych_kartek = liczba_zoltych_kartek;
	}

	public int getLiczba_czerwonych_kartek() {
		return liczba_czerwonych_kartek;
	}

	public void setLiczba_czerwonych_kartek(int liczba_czerwonych_kartek) {
		this.liczba_czerwonych_kartek = liczba_czerwonych_kartek;
	}

	public int getIlosc_goli() {
		return ilosc_goli;
	}

	public void setIlosc_goli(int ilosc_goli) {
		this.ilosc_goli = ilosc_goli;
	}

	public boolean isCzyste_konto() {
		return czyste_konto;
	}

	public void setCzyste_konto(boolean czyste_konto) {
		this.czyste_konto = czyste_konto;
	}

	public Pilkarz getPilkarz() {
		return pilkarz;
	}

	public void setPilkarz(Pilkarz pilkarz) {
		this.pilkarz = pilkarz;
	}

	public Mecz getMecz() {
		return mecz;
	}

	public void setMecz(Mecz mecz) {
		this.mecz = mecz;
	}

	public ArrayList<PilkarzMecz> getPilkarzMecz() {
		return PilkarzMecz;
	}

	public void setPilkarzMecz(ArrayList<PilkarzMecz> pilkarzMecz) {
		PilkarzMecz = pilkarzMecz;
	}
}

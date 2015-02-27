package pl.wcislo.sbql4j.javac.test.football.model;
public class Data{
 
	
	private int dzien;
	private int miesiac;
	private int rok;
	
	public Data(){
		
	}
	public Data(int dzien, int miesiac, int rok){
		
		this.dzien = dzien;
		this.miesiac = miesiac;
		this.rok = rok;
	}
	public int pokazRok(){
		return this.rok;
		
	}
	
	public String toString(){
		return dzien + "." + miesiac + "." + rok;
	}
	public int getDzien() {
		return dzien;
	}
	public void setDzien(int dzien) {
		this.dzien = dzien;
	}
	public int getMiesiac() {
		return miesiac;
	}
	public void setMiesiac(int miesiac) {
		this.miesiac = miesiac;
	}
	public int getRok() {
		return rok;
	}
	public void setRok(int rok) {
		this.rok = rok;
	}
	
}

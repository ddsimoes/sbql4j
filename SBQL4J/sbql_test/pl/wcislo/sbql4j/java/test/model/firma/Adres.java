package pl.wcislo.sbql4j.java.test.model.firma;

public class Adres {
	private String ulica;
	private String miasto;
	private String kodPocztowy;
	private Integer numer;

	public Adres(String ulica, String miasto, String kodPocztowy, Integer numer) {
		super();
		this.ulica = ulica;
		this.miasto = miasto;
		this.kodPocztowy = kodPocztowy;
		this.numer = numer;
	}
	
	public Adres() {
	}
	
	public String getUlica() {
		return ulica;
	}
	public void setUlica(String ulica) {
		this.ulica = ulica;
	}
	public String getMiasto() {
		return miasto;
	}
	public void setMiasto(String miasto) {
		this.miasto = miasto;
	}
	public String getKodPocztowy() {
		return kodPocztowy;
	}
	public void setKodPocztowy(String kodPocztowy) {
		this.kodPocztowy = kodPocztowy;
	}
	public Integer getNumer() {
		return numer;
	}
	public void setNumer(Integer numer) {
		this.numer = numer;
	}
}

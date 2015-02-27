package pl.wcislo.sbql4j.javac.test.struct.model;

public class Firma {
	private String nazwa;

	public Firma(String nazwa) {
		super();
		this.nazwa = nazwa;
	}
	
	public String getNazwa() {
		return nazwa;
	}
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
}

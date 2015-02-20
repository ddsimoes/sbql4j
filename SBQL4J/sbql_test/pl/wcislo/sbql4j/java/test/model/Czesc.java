package pl.wcislo.sbql4j.java.test.model;

import java.util.List;

public class Czesc {
	private String nazwa;
	private Double koszt;
	private Double masa;
	private List<Skladnik> skladniki;
	
	
	
	public static class Skladnik {
		private int ilosc;
		private Czesc prowadziDo;
		
		public Skladnik() {}
		
		public Skladnik(int ilosc, Czesc czesc) {
			super();
			this.ilosc = ilosc;
			this.prowadziDo = czesc;
		}
		public int getIlosc() {
			return ilosc;
		}
		public void setIlosc(int ilosc) {
			this.ilosc = ilosc;
		}
		public Czesc getProwadziDo() {
			return prowadziDo;
		}
		public void setProwadziDo(Czesc prowadziDo) {
			this.prowadziDo = prowadziDo;
		}
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public List<Skladnik> getSkladniki() {
		return skladniki;
	}

	public void setSkladniki(List<Skladnik> skladniki) {
		this.skladniki = skladniki;
	}

	public Double getKoszt() {
		return koszt;
	}
	public void setKoszt(Double koszt) {
		this.koszt = koszt;
	}
	public Double getMasa() {
		return masa;
	}
	public void setMasa(Double masa) {
		this.masa = masa;
	}

	public Czesc(String nazwa, Double koszt, Double masa) {
		super();
		this.nazwa = nazwa;
		this.koszt = koszt;
		this.masa = masa;
	}	
	
	public Czesc() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nazwa == null) ? 0 : nazwa.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Czesc other = (Czesc) obj;
		if (nazwa == null) {
			if (other.nazwa != null)
				return false;
		} else if (!nazwa.equals(other.nazwa))
			return false;
		return true;
	}
	
	
}
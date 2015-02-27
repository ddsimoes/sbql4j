package pl.wcislo.sbql4j.javac.test.football.model;

import java.util.Date;

public class Kontuzja {
	
	private Date Data_od;
	private Date Data_do;
	private Pilkarz pilkarz;

	public Kontuzja(Date Data_od, Date Data_do, Pilkarz pilkarz){
		this.Data_do=Data_do;
		this.Data_od=Data_od;
		this.pilkarz=pilkarz;
	}

	public Date getData_od() {
		return Data_od;
	}

	public void setData_od(Date data_od) {
		Data_od = data_od;
	}

	@Override
	public String toString() {
		return "Kontuzja [Data_od=" + Data_od + ", Data_do=" + Data_do + "]";
	}

	public Date getData_do() {
		return Data_do;
	}

	public void setData_do(Date data_do) {
		Data_do = data_do;
	}

	public Pilkarz getPilkarz() {
		return pilkarz;
	}

	public void setPilkarz(Pilkarz pilkarz) {
		this.pilkarz = pilkarz;
	}
}

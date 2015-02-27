package pl.wcislo.sbql4j.javac.test.football.model;
import java.util.ArrayList; 
import java.util.Date;

public class Oboz {
	
	private Date Data_od;
	private Date Data_do;	
//	private  ArrayList<Druzyna> druzyna;
	private  ArrayList<Oboz> listaObozow= new ArrayList<Oboz>();
	private Druzyna druzyna;
	

	
/*
	public Date getData_od() {
		return Data_od;
	}

	public void setData_od(Date data_od) {
		Data_od = data_od;
	}

	public Date getData_do() {
		return Data_do;
	}

	public void setData_do(Date data_do) {
		Data_do = data_do;
	}

	public ArrayList<Oboz> getListaObozow() {
		return listaObozow;
	}

	public void setListaObozow(ArrayList<Oboz> listaObozow) {
		this.listaObozow = listaObozow;
	}
*/
	public Oboz(Date Data_od,	Date Data_do, Druzyna druzyna){
		this.Data_do=Data_do;
		this.Data_od=Data_od;
		this.druzyna=druzyna;
		//this.druzyna.add(druzyna);		
	}
	
	public Date getData_od() {
		return Data_od;
	}

	public void setData_od(Date data_od) {
		Data_od = data_od;
	}

	public Date getData_do() {
		return Data_do;
	}

	public void setData_do(Date data_do) {
		Data_do = data_do;
	}

	public ArrayList<Oboz> getListaObozow() {
		return listaObozow;
	}

	public void setListaObozow(ArrayList<Oboz> listaObozow) {
		this.listaObozow = listaObozow;
	}

	public Druzyna getDruzyna() {
		return druzyna;
	}

	public void setDruzyna(Druzyna druzyna) {
		this.druzyna = druzyna;
	}

	public void dodajOboz(Oboz oboz){
		listaObozow.add(oboz);
	}
	
	public void edytujOboz(Oboz oboz){
		
	}

	@Override
	public String toString() {
		return "Oboz [" + (Data_od != null ? "Data_od=" + Data_od + ", " : "")
				+"Data_do=" + Data_do + ", "  + "]";
	}

}

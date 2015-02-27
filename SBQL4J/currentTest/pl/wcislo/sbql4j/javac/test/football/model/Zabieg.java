package pl.wcislo.sbql4j.javac.test.football.model;
public class Zabieg {
	
	private String typ_zabiegu;
	private Lekarz lekarz;
private Pilkarz pilkarz;
	public Zabieg (String typ_zabiegu, Lekarz lekarz){
		this.typ_zabiegu=typ_zabiegu;
		this.lekarz=lekarz;
	}

	public String getTyp_zabiegu() {
		return typ_zabiegu;
	}

	public Pilkarz getPilkarz() {
		return pilkarz;
	}

	

	@Override
	public String toString() {
		return "Zabieg ["
				+ (typ_zabiegu != null ? "typ_zabiegu=" + typ_zabiegu + ", "
						: "") + (pilkarz != null ? "pilkarz=" + pilkarz : "")
				+ "]";
	}

	public void setPilkarz(Pilkarz pilkarz) {
		this.pilkarz = pilkarz;
	}

	public Zabieg(String typ_zabiegu, Lekarz lekarz, Pilkarz pilkarz) {
		super();
		this.typ_zabiegu = typ_zabiegu;
		this.lekarz = lekarz;
		this.pilkarz = pilkarz;
	}

	public void setTyp_zabiegu(String typ_zabiegu) {
		this.typ_zabiegu = typ_zabiegu;
	}

	public Lekarz getLekarz() {
		return lekarz;
	}

	public void setLekarz(Lekarz lekarz) {
		this.lekarz = lekarz;
	}
}

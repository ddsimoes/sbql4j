package pl.wcislo.sbql4j.javac.test.football.data;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import pl.wcislo.sbql4j.javac.test.football.model.Data;
import pl.wcislo.sbql4j.javac.test.football.model.Druzyna;
import pl.wcislo.sbql4j.javac.test.football.model.Kontuzja;
import pl.wcislo.sbql4j.javac.test.football.model.Lekarz;
import pl.wcislo.sbql4j.javac.test.football.model.Masazysta;
import pl.wcislo.sbql4j.javac.test.football.model.Mecz;
import pl.wcislo.sbql4j.javac.test.football.model.Oboz;
import pl.wcislo.sbql4j.javac.test.football.model.Pilkarz;
import pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz;
import pl.wcislo.sbql4j.javac.test.football.model.Stroj;
import pl.wcislo.sbql4j.javac.test.football.model.Trener;
import pl.wcislo.sbql4j.javac.test.football.model.Zabieg;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.config.ObjectClass;
import com.db4o.ext.ExtObjectContainer;

public class ExampleData {
	public static final String DB_FILENAME = "football.db";
	private Random generator = new Random();
	private ArrayList<Mecz> mecze;
	private ArrayList<Druzyna> druzyny;
	private ArrayList<Pilkarz> pilkarze;
	private ArrayList<PilkarzMecz> pilkarzmecz; // todo
	private ObjectContainer dbConn;
	private List<String> imiona = new ArrayList<String>();
	private List<String> nazwiska = new ArrayList<String>();
	private List<String> miasta = new ArrayList<String>();
	private List<String> druzynyLIST = new ArrayList<String>();
	private List<String> specjalisci = new ArrayList<String>();
	private List<String> kolory = new ArrayList<String>();
	private List<String> rozmiary = new ArrayList<String>();
	private List<String> TypyZabiegu = new ArrayList<String>();
	private List<String> stadiony = new ArrayList<String>();

	public static void main(String[] args) throws ParseException {
		ExampleData exampleData = new ExampleData();
	}

	public ExampleData() {
		initData();
		saveData();
	}

	private void initData() {
		InitializeandAdd();

		DodajDruzyny(); // druzyny // i zawodnicy i ich kontuzje
		DodajMasazystowDoDruzyny();// masazysci
		DodajMecze(); // DO DRUZYN!
		DodajTrenerow(); // do druzyny trenerzy
		DodajLekarzy(); // do druzyny lekrarze
		DodajStroje(); // doddruzyn
		DodajObozy(); // do druzyn'
		DodajZabiegi();// do pilkarzy :)
		DodajPilkarzyWMeczach();
	}

	private void InitializeandAdd() {
		pilkarzmecz = new ArrayList<PilkarzMecz>();
		mecze = new ArrayList<Mecz>();
		druzyny = new ArrayList<Druzyna>();
		pilkarze = new ArrayList<Pilkarz>();

		imiona.add("Iwo");
		imiona.add("Cech");
		imiona.add("Cristiano");
		imiona.add("Paulo");
		imiona.add("Karol");
		imiona.add("Eden");
		imiona.add("John");
		imiona.add("Ashley");
		imiona.add("Tibout");
		imiona.add("Leszek");
		imiona.add("Zdzich");
		imiona.add("Jacek");
		imiona.add("Tomek");
		imiona.add("Gracz");
		imiona.add("Gracz2");
		imiona.add("Emil");
		imiona.add("Michal");
		imiona.add("Rafal");
		imiona.add("Pawel");
		imiona.add("Krzysztof");
		imiona.add("Andrzej");
		imiona.add("Ebi");
		imiona.add("Robert");

		nazwiska.add("KOSOWSKI");
		nazwiska.add("Hazard");
		nazwiska.add("Terry");
		nazwiska.add("Pop");
		nazwiska.add("Oscar");
		nazwiska.add("Cahil");
		nazwiska.add("Ronaldo");
		nazwiska.add("Azcipuleta");
		nazwiska.add("Banasiak");
		nazwiska.add("Popielski");
		nazwiska.add("Kowalski");
		nazwiska.add("Memo");
		nazwiska.add("Tran");
		nazwiska.add("Wach");
		nazwiska.add("Giz");
		nazwiska.add("Krul");
		nazwiska.add("Krol");
		nazwiska.add("Terry");
		druzynyLIST.add("Bayer");
		druzynyLIST.add("Bayern");
		druzynyLIST.add("Chelsea");
		druzynyLIST.add("Borussia");
		druzynyLIST.add("Legia");
		druzynyLIST.add("Manchester United");
		druzynyLIST.add("Wisla");
		druzynyLIST.add("Arsenal");
		druzynyLIST.add("Basela");
		druzynyLIST.add("Barcelona");
		druzynyLIST.add("Real Madryt");
		// druzynyLIST.add("Manchester United");

		miasta.add("Warszawa");
		miasta.add("Mediolan");
		miasta.add("Pruszkow");
		miasta.add("Piastow");
		miasta.add("Poznan");
		miasta.add("Londyn");
		miasta.add("Bruksela");

		specjalisci.add("Kardiolog");
		specjalisci.add("chirurg");
		specjalisci.add("chirurg plastyczny");
		specjalisci.add("ortopeda");

		kolory.add("Czerwony");
		kolory.add("Niebieski");
		kolory.add("Bialy");
		kolory.add("Zolty");
		kolory.add("Szary");
		kolory.add("Czerwono-Bialy");
		kolory.add("Czarny");
		kolory.add("Piaskowy");
		kolory.add("Seledynowy");
		kolory.add("Lososiowy");
		kolory.add("Rozowy");

		rozmiary.add("XL");
		rozmiary.add("L");
		rozmiary.add("XXL");

		TypyZabiegu.add("Nastawienie kosci");
		TypyZabiegu.add("Zszycie rany");
		stadiony.add("Santiago Bernabeu");
		stadiony.add("Brittania Stadium");
	}

	private void DodajPilkarzyWMeczach() {

		for (int i = 0; i < druzyny.size(); i++) {
			ArrayList<Mecz> mecze = druzyny.get(i).getMecz();
			if (mecze != null) {
				if (mecze.isEmpty() == false) {

					for (int j = 0; j < druzyny.get(i).getMecz().size(); j++) {
						DodajPilkarzaDoMeczu(mecze.get(j), druzyny.get(i));
					}
				}
			}
		}
	}

	private void DodajPilkarzaDoMeczu(Mecz mecz, Druzyna druzyna) {
		ArrayList<Pilkarz> pilkarze = druzyna.getLista_zawodnikow();
		String wynikmeczu;
		int liczbazwodnikowdokartek = randomInt(1, 10);
		int liczbazawodnikowzkartkami = liczbazwodnikowdokartek;
		int wynik;
		boolean bramkarz = false;
		if (mecz.getDruzyna() == druzyna) {
			wynikmeczu = mecz.getWynik();
			int pos = wynikmeczu.indexOf(":");
			wynik = Integer.parseInt(wynikmeczu.substring(0, pos));
		} else if (mecz.getDruzyna2() == druzyna) {
			wynikmeczu = mecz.getWynik();
			int pos = wynikmeczu.indexOf(":");
			wynik = Integer.parseInt(wynikmeczu.substring(pos + 1,
					wynikmeczu.length()));

			for (int i = 0; i < druzyna.getLista_zawodnikow().size(); i++) {
				boolean tr = randomBoolean();
				if (tr == true && bramkarz == false) {
					bramkarz = true;
					int zoltekartki = 0;
					int doliczenia = i + 1;
					int wylicz = liczbazwodnikowdokartek % doliczenia;
					if (wylicz == 0 && liczbazawodnikowzkartkami != 0) {
						liczbazawodnikowzkartkami--;
						zoltekartki = RandomYellowCards();
					}
					int czerwonekartki = RandomRedCards(zoltekartki);
					PilkarzMecz p = new PilkarzMecz(zoltekartki,
							czerwonekartki, 0, CheckCzysteKonto(wynik),
							pilkarze.get(i), mecz);
					pilkarzmecz.add(p);
				} else {
					int zoltekartki = 0;
					int doliczenia = i + 1;
					int wylicz = liczbazwodnikowdokartek % doliczenia;
					if (wylicz == 0 && liczbazawodnikowzkartkami != 0) {
						liczbazawodnikowzkartkami--;
						zoltekartki = RandomYellowCards();
					}
					int czerwonekartki = RandomRedCards(zoltekartki);
					PilkarzMecz p = new PilkarzMecz(zoltekartki,
							czerwonekartki, GenerateGoals(wynik), false,
							pilkarze.get(i), mecz);
					pilkarzmecz.add(p);

				}
			}

		}
	}

	private int GenerateGoals(int wynik) {
		// TODO Auto-generated method stub
		return 0;
	}

	private boolean CheckCzysteKonto(int wynik) {
		boolean czystekonto = true;
		if (wynik != 0) {
			czystekonto = false;
		}
		return czystekonto;
	}

	private int RandomRedCards(int zoltekartki) {
		int czerwone = 0;
		if (zoltekartki == 2) {
			czerwone = 1;
		}
		if (zoltekartki == 1) {
			czerwone = 0;
		}
		return czerwone;
	}

	private int RandomYellowCards() {
		boolean b = randomBoolean();
		if (b == true) {
			return randomInt(1, 2);
		} else {
			return 0;
		}

	}

	private void DodajZabiegi() {

		for (int i = 0; i < druzyny.size(); i++) {

			ArrayList<Lekarz> lekarzewdruzynie = druzyny.get(i)
					.getListalekarzy();
			if (!(lekarzewdruzynie == null)) {
				for (int j = 0; j < lekarzewdruzynie.size(); j++) {
					DodajZabiegDoLekarza(lekarzewdruzynie.get(j));
				}
			}

		}
	}

	private void DodajZabiegDoLekarza(Lekarz lekarz) {

		int i = generator.nextInt(lekarz.getD().getLista_zawodnikow().size());

		for (int j = 0; j < i; j++) {
			Zabieg g = new Zabieg(RandomTypZabiegu(), lekarz,
					randomPilkarzwDruzynie(lekarz.getD()));
			lekarz.dodajZabieg(g);
		}
	}

	private String RandomTypZabiegu() {

		int s = generator.nextInt(TypyZabiegu.size());
		return TypyZabiegu.get(s);
	}

	private Pilkarz randomPilkarzwDruzynie(Druzyna d) {
		int i = generator.nextInt(d.getLista_zawodnikow().size());
		return d.getLista_zawodnikow().get(i);
	}

	private void DodajObozy() {
		// TODO Auto-generated method stub
		for (int i = 0; i < druzyny.size(); i++) {
			DodajOboz(druzyny.get(i));
		}
	}

	private void DodajOboz(Druzyna druzyna) {

		int j = randomInt(1, 10);
		for (int i = 0; i < j; i++) {
			Date d = randomDate();
			Oboz s = new Oboz(d, RandomDateFromStart(d), druzyna);
			druzyna.dodajOboz(s);
		}
	}

	private void DodajStroje() {

		for (int i = 0; i < druzyny.size(); i++) {
			DodajStroj(druzyny.get(i));
		}

	}

	private void DodajStroj(Druzyna druzyna) {

		int j = randomInt(1, 3);

		for (int i = 0; i < j; i++) {

			Stroj s = new Stroj(randomKolor(), randomRozmiar(), druzyna);
			druzyna.dodajStroj(s);
		}

	}

	private String randomKolor() {
		int s = generator.nextInt(kolory.size());
		return kolory.get(s);
	}

	private String randomRozmiar() {
		int s = generator.nextInt(rozmiary.size());
		return rozmiary.get(s);
	}

	private void DodajLekarzy() {

		int i = druzyny.size();
		// / generator.nextInt(druzyny.size());
		int ilelekarzy = randomInt(1, 5);
		for (int j = 0; j < i; j++) {
			for (int jj = 0; jj < ilelekarzy; jj++) {
				Lekarz t = new Lekarz(randomName(), randomSurname(),
						randomAddres(), randomPhone(), randomInt(1000, 20000),
						randomSpecLekarza(), randomDate(), randomInt(200, 500));

				// t.dodajTreneradoDruzyny(d);
				druzyny.get(j).dodajLekarza(t);
				t.dodajLekarzadoDruzyny(druzyny.get(j));
			}
		}
	}

	private String randomSpecLekarza() {
		// TODO Auto-generated method stub
		int s = generator.nextInt(specjalisci.size());
		return specjalisci.get(s);
	}

	private void DodajTrenerow() {

		int i = generator.nextInt(druzyny.size());

		for (int j = 0; j < i; j++) {
			Trener t = new Trener(randomName(), randomSurname(),
					randomAddres(), randomPhone(), randomInt(1000, 20000),
					randomDate());

			// t.dodajTreneradoDruzyny(d);
			druzyny.get(j).dodajTrenera(t);
			t.dodajTreneradoDruzyny(druzyny.get(j));
		}
	}

	private void DodajMecze() {
		int ilosc = randomInt(4, 12);

		for (int i = 0; i < ilosc; i++) {
			DodajMecz();
		}
	}

	private void DodajMecz() {
		ArrayList<Druzyna> DruzynywMeczu = new ArrayList<Druzyna>();

		DruzynywMeczu = GenerujDruzynydoMeczu();
		Mecz m = new Mecz(RandomMiasto(), randomStadion(), randomDate(2011,
				2014), DruzynywMeczu.get(0), DruzynywMeczu.get(1),
				RandomWynik());
		int pos = druzyny.indexOf(DruzynywMeczu.get(1));
		int pos2 = druzyny.indexOf(DruzynywMeczu.get(0));
		druzyny.get(pos).DodajMecz(m);
		druzyny.get(pos2).DodajMecz(m);
	}

	private String RandomWynik() {

		String wynik = "";
		wynik = wynik.concat(String.valueOf(randomInt(0, 3) + ":"
				+ String.valueOf(randomInt(0, 3))));
		return wynik;
	}

	private String randomStadion() {

		Random generator = new Random();
		int oe = generator.nextInt(stadiony.size());
		String d = stadiony.get(oe);
		return d;
	}

	private ArrayList<Druzyna> GenerujDruzynydoMeczu() {
		ArrayList<Druzyna> DruzynywMeczu = new ArrayList<Druzyna>();
		Druzyna d = randomDruzyna();
		for (int i = 0; i < 2; i++) {

			while (DruzynywMeczu.contains(d)) {
				d = randomDruzyna();
			}
			DruzynywMeczu.add(d);
		}
		return DruzynywMeczu;
	}

	private Druzyna randomDruzyna() {
		// TODO Auto-generated method stub
		Random generator = new Random();
		int oe = generator.nextInt(druzyny.size());
		Druzyna d = druzyny.get(oe);
		return d;
	}

	private void DodajMasazystowDoDruzyny() {
		// TODO Auto-generated method stub
		for (int i = 0; i < druzyny.size(); i++) {
			DodajMasazyste(druzyny.get(i));
		}
	}

	private void DodajMasazyste(Druzyna druzyna) {
		// TODO Auto-generated method stub
		Masazysta m = new Masazysta(randomName(), randomSurname(),
				randomAddres(), randomPhone(), randomInt(2000, 50000),
				randomDate());
		druzyna.dodajMasazyste(m);
		m.setDruzyna(druzyna);
	}

	/*
	 * private String RandomDate() { // TODO Auto-generated method stub
	 * GregorianCalendar gc = new GregorianCalendar();
	 * 
	 * int year = randomInt(1900, 1989);
	 * 
	 * gc.set(gc.YEAR, year);
	 * 
	 * int dayOfYear = randomInt(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
	 * 
	 * gc.set(gc.DAY_OF_YEAR, dayOfYear);
	 * 
	 * return gc.get(gc.YEAR) + "-" + gc.get(gc.MONTH) + "-" +
	 * gc.get(gc.DAY_OF_MONTH); }
	 */
	private String randomPhone() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		int num1 = (rand.nextInt(7) + 1) * 100 + (rand.nextInt(8) * 10)
				+ rand.nextInt(8);
		int num2 = rand.nextInt(743);
		int num3 = rand.nextInt(10000);

		DecimalFormat df3 = new DecimalFormat("000"); // 3 zeros
		DecimalFormat df4 = new DecimalFormat("0000"); // 4 zeros

		String phoneNumber = df3.format(num1) + "-" + df3.format(num2) + "-"
				+ df4.format(num3);
		return phoneNumber;
	}

	private String randomAddres() {
		// TODO Auto-generated method stub
		String adres = randomZip() + " " + RandomMiasto();
		return adres;
	}

	private void DodajDruzyny() {
		// DRUZYNY i pilkarze do druzyny ! no i do gabloty! 'TUTAJ MAMY BLAD
		// TRZA COS DZIABNAC - CHYBA ZROBIONE
		int randompil = generator.nextInt((druzynyLIST.size() - 4) + 1) + 4;
		// int randompil = generator.nextInt(druzynyLIST.size());
		DodajPilkarzy(randompil);
		for (int i = 0; i < randompil; i++) {
			druzyny.add(new Druzyna(RandomNazwa(), 22, RandomMiasto()));
			druzyny.get(druzyny.size() - 1).setLiczba_zawodnikow(22);
			dodajPilkarzyDoDruzyny(druzyny.get(druzyny.size() - 1));
		}
	}

	private void DodajPilkarzy(int randompil) {
		// pilkarze
		for (int i = 0; i < randompil * 22; i++) {
			int wiek = 16 + generator.nextInt(35);

			Calendar cal = GregorianCalendar.getInstance();
			cal.add(Calendar.YEAR, -wiek);
			Date tenDaysAgo = cal.getTime();
			pilkarze.add(new Pilkarz(randomName(), randomSurname(),
					16 + generator.nextInt(35), new Data(tenDaysAgo.getDay(),
							tenDaysAgo.getMonth(), tenDaysAgo.getYear()),
					randomInt(0, 5000), randomInt(200, 1020)));
		}
	}

	private void dodajPilkarzyDoDruzyny(Druzyna druzyna) {
		// TODO Auto-generated method stub
		for (int i = 0; i < druzyna.getLiczba_zawodnikow(); i++) {
			AddPilkarzToDruzyna(druzyna);
			// System.out.println( i);
		}
	}

	private void AddPilkarzToDruzyna(Druzyna druzyna) {

		// TODO Auto-generated method stub
		Random generator = new Random();
		int oe = generator.nextInt(pilkarze.size());
		while (pilkarze.get(oe).getDruzyna() != null) {
			oe = generator.nextInt(pilkarze.size());
		}

		pilkarze.get(oe).setDruzyna(druzyna);
		druzyna.dodajZawodnika(pilkarze.get(oe));
		boolean kontuzja = randomBoolean();

		if (kontuzja) {
			Date start = RandomDateFromBirth(pilkarze.get(oe));
			Date end = RandomDateFromStart(start);
			Kontuzja k = new Kontuzja(start, end, pilkarze.get(oe));
			pilkarze.get(oe).dodajKontuzje(k);
		}
	}

	@SuppressWarnings("deprecation")
	private Date RandomDateFromStart(Date start) {
		// TODO Auto-generated method stub

		return randomDate(start.getYear(), 2014);
	}

	private Date RandomDateFromBirth(Pilkarz pilkarz) {

		return randomDate(pilkarz.getData_urodz().getRok(), 2014);
	}

	private String randomZip() {
		int r = (int) (Math.random() * 100000);
		DecimalFormat df = new DecimalFormat("00000");
		StringBuilder sb = new StringBuilder(df.format(r));
		sb.insert(2, "-");
		return sb.toString();
	}

	private Pilkarz GetRandomPilkarz() {
		Random generator = new Random();
		int oe = generator.nextInt(pilkarze.size());
		Pilkarz P = pilkarze.get(oe);
		return P;
	}

	private String RandomMiasto() {
		// TODO Auto-generated method stub
		Random generator = new Random();
		int oe = generator.nextInt(miasta.size());
		String imie = miasta.get(oe);
		return imie;
	}

	private boolean randomBoolean() {
		int r = (int) (Math.random() * 2);
		return r > 0 ? true : false;
	}

	private int RandomIlosc() {
		// TODO Auto-generated method stub
		Random generator = new Random();
		int oe = generator.nextInt(pilkarze.size());

		return oe;
	}

	private String RandomNazwa() {
		// TODO Auto-generated method stub
		Random generator = new Random();
		int oe = generator.nextInt(druzynyLIST.size());
		String imie = druzynyLIST.get(oe);
		druzynyLIST.remove(oe);
		return imie;
	}

	private String randomSurname() {
		// TODO Auto-generated method stub
		Random generator = new Random();
		int oe = generator.nextInt(nazwiska.size());
		String imie = nazwiska.get(oe);
		return imie;
	}

	private String randomName() {
		// TODO Auto-generated method stub
		Random generator = new Random();
		int oe = generator.nextInt(imiona.size());
		String imie = imiona.get(oe);
		return imie;
	}

	private void saveData() {
		ExtObjectContainer con = getConnection().ext();
		Configuration config = con.configure();
		{
			ObjectClass objectClass = config.objectClass(Pilkarz.class);
			objectClass.objectField("wiek").indexed(true);
			ObjectClass objectClass2 = config.objectClass(Druzyna.class);
			objectClass2.objectField("nazwa").indexed(true);
			ObjectClass objectClass3 = config.objectClass(Trener.class);
			objectClass3.objectField("premia").indexed(true);
			ObjectClass objectClass4 = config.objectClass(PilkarzMecz.class);
			objectClass4.objectField("liczba_zoltych_kartek").indexed(true);
			ObjectClass objectClass5 = config.objectClass(PilkarzMecz.class);
			objectClass5.objectField("liczba_czerwonych_kartek").indexed(true);
			ObjectClass objectClass6 = config.objectClass(PilkarzMecz.class);
			objectClass6.objectField("ilosc_goli").indexed(true);
			ObjectClass objectClass7 = config.objectClass(PilkarzMecz.class);
			objectClass7.objectField("czyste_konto").indexed(true);
		}

		ObjectSet<Mecz> cars = con.query(Mecz.class);
		if (cars.isEmpty()) {
			for (Mecz c : this.mecze) {
				con.store(c);
			}
		}
		ObjectSet<PilkarzMecz> PM = con.query(PilkarzMecz.class);
		if (PM.isEmpty()) {
			for (PilkarzMecz PMs : this.pilkarzmecz) {
				con.store(PMs);
			}
		}
		ObjectSet<Pilkarz> pilkarze = con.query(Pilkarz.class);
		if (pilkarze.isEmpty()) {
			for (Pilkarz f : this.pilkarze) {
				con.store(f);
			}
		}
		ObjectSet<Druzyna> druzyna = con.query(Druzyna.class);
		if (druzyna.isEmpty()) {
			for (Druzyna p : this.druzyny) {
				con.store(p);
			}
		}

		con.commit();
	}

	public ObjectContainer getConnection() {
		if (dbConn == null) {
			File dbFile = new File(DB_FILENAME);
			dbFile.delete();
			dbConn = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
					DB_FILENAME);
		}
		return dbConn;
	}

	private int randomInt(int min, int max) {
		return (int) (Math.random() * (max - min + 1)) + min;
	}

	private Date randomDate() {
		return randomDate(1970, 1996);
	}

	private Date randomDate(int minYear, int maxYear) {
		int year = randomInt(minYear, maxYear);
		int month = randomInt(0, 11);

		GregorianCalendar gc = new GregorianCalendar(year, month, 1);

		int day = randomInt(1, gc.getActualMaximum(gc.DAY_OF_MONTH));

		gc.set(year, month, day);

		return gc.getTime();
	}

	public void querywiek(int min) {
		// Collection<Pilkarz> pi = #{dbConn.(Pilkarz where wiek = min)};
		// System.out.println(pi);
	}

	public static float getAge(final Date birthdate) {
		return getAge(Calendar.getInstance().getTime(), birthdate);
	}

	public static float getAge(final Date current, final Date birthdate) {

		if (birthdate == null) {
			return 0;
		}
		if (current == null) {
			return getAge(birthdate);
		} else {
			final Calendar calend = new GregorianCalendar();
			calend.set(Calendar.HOUR_OF_DAY, 0);
			calend.set(Calendar.MINUTE, 0);
			calend.set(Calendar.SECOND, 0);
			calend.set(Calendar.MILLISECOND, 0);

			calend.setTimeInMillis(current.getTime() - birthdate.getTime());

			float result = 0;
			result = calend.get(Calendar.YEAR) - 1970;
			result += (float) calend.get(Calendar.MONTH) / (float) 12;
			return result;
		}
	}
}

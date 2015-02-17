package pl.wcislo.sbql4j.java.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.wcislo.sbql4j.Statement;
import pl.wcislo.sbql4j.java.test.model.firma.Adres;
import pl.wcislo.sbql4j.java.test.model.firma.Firma;
import pl.wcislo.sbql4j.java.test.model.firma.Pracownik;
import pl.wcislo.sbql4j.java.test.model.firma.Zatrudnienie;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.model.QueryResult;

public class PracownicyTest {
	private static final Log log = LogFactory.getLog(PracownicyTest.class);

	private List<Pracownik> pracownicy = new ArrayList<Pracownik>();
	private List<Firma> firmy = new ArrayList<Firma>();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new PracownicyTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public PracownicyTest() throws Exception {
			initData();
			String expr = "((:prac where \"Warszawa\" in pracujeW.firma.adres.miasto).pracujeW as zatr).(zatr.setPensja(pensja+100), zatr.pracownik.setNazwisko(nazwisko+\" Farciarz\"))";
//			String expr = "(:prac.pracujeW.firma.adres.miasto)";
//			String expr = "(:prac)";

			Statement stmt = new Statement(expr);
			

			stmt.setParam("prac", pracownicy);
			
			Interpreter i = new Interpreter();
			Object res = stmt.execute(i);
			
			log.info(res);
			log.info(pl.wcislo.sbql4j.util.Utils.resultToString((QueryResult) res, false));
			
			stmt = new Statement("(:prac as p).(p, p.pracujeW)");
			stmt.setParam("prac", pracownicy);
			res = stmt.execute(i);
			log.info(pl.wcislo.sbql4j.util.Utils.resultToString((QueryResult) res, false));
	}
	
	private void initData() {
		Firma firma1 = new Firma("Nullpointer Exception - niezawodne oprogramowanie");
		{
			firmy.add(firma1);
			Adres adr1 = new Adres("Wynalazek", "Warszawa", "01-999", 4);
			Adres adr2 = new Adres("Pi�sudzkiego", "Pozna�", "50-000", 23);
			firma1.getAdres().add(adr1);
			firma1.getAdres().add(adr2);
		}
		Firma firma2 = new Firma("Connection Refused - integracja system�w");
		{
			firmy.add(firma2);
			Adres adr1 = new Adres("Baletowa", "Warszawa", "01-888", 2);
			firma2.getAdres().add(adr1);
		}
		Firma firma3 = new Firma("Java�arcie - outsourcing");
		{
			firmy.add(firma3);
			Adres adr1 = new Adres("Szucha", "Krak�w", "01-777", 5);
			firma3.getAdres().add(adr1);
		}
		
		Pracownik prac1 = new Pracownik("Jan", "Kowalski", "programista");
		Pracownik prac2 = new Pracownik("Stefan", "Nowak", "analityk");
		Pracownik prac3 = new Pracownik("Kazimierz", "Bielecki", "kierownik projekt�w");
		pracownicy.add(prac1);
		pracownicy.add(prac2);
		pracownicy.add(prac3);
		Calendar cal = new GregorianCalendar(2006, Calendar.JUNE, 15);
		Zatrudnienie zatr1 = new Zatrudnienie(prac1, firma1, 4500, cal.getTime());
		cal = new GregorianCalendar(2005, Calendar.NOVEMBER, 1);
		Zatrudnienie zatr2 = new Zatrudnienie(prac1, firma2, 2500, cal.getTime());
		cal = new GregorianCalendar(1999, Calendar.APRIL, 22);
		Zatrudnienie zatr3 = new Zatrudnienie(prac2, firma3, 5000, cal.getTime());
		cal = new GregorianCalendar(2002, Calendar.OCTOBER, 10);
		Zatrudnienie zatr4 = new Zatrudnienie(prac3, firma1, 6000, cal.getTime());
		cal = new GregorianCalendar(2001, Calendar.AUGUST, 17);
		Zatrudnienie zatr5 = new Zatrudnienie(prac3, firma3, 3000, cal.getTime());
		
	}
}

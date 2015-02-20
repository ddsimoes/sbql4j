package pl.wcislo.sbql4j.java.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.wcislo.sbql4j.Statement;
import pl.wcislo.sbql4j.java.test.model.Czesc;
import pl.wcislo.sbql4j.java.test.model.Czesc.Skladnik;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.model.QueryResult;

public class CloseByBOMTest {
	private static final Log log = LogFactory.getLog(CloseByBOMTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new CloseByBOMTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public CloseByBOMTest() throws Exception {
//			String expr = "(:silnik closeby (skladniki.czesc))";
//			String expr = "sum(((:silnik,  (1 as ile)) closeby (skladniki.(czesc, (ile * ilosc) as ile)) ).(ile))";
			
//			String expr = "(((:silnik as x, (1 as ile)) closeby " +
//					"x.(skladniki.(czesc as x, (ile*ilosc) as ile))) " +
//					"groupas wszystkieCzesciSilnika)"+
//					".((unique(wszystkieCzesciSilnika.x) as y)"+
//					".(y, sum((wszystkieCzesciSilnika where x = y).ile)))"
//					; 


			String expr = "silnik"; 
			Statement stmt = new Statement(expr);
			
			Czesc silnik = prepareData();
			stmt.setParam("silnik", silnik);
			
			Interpreter i = new Interpreter();
			Object res = stmt.execute(i);
			
			log.info(res);
			log.info(pl.wcislo.sbql4j.util.Utils.resultToString((QueryResult) res, false));
	}
	
	public static Czesc prepareData() {
		Czesc silnik = new Czesc("silnik", 300.40, 1000.40);
		List<Skladnik> s1 = new ArrayList<Skladnik>();
		{
			{
				Czesc uklKorbowy = new Czesc("uklad korbowy", 20.40, 40.40);
				Skladnik sklUklKorbowy = new Skladnik(1, uklKorbowy);
				s1.add(sklUklKorbowy);
				List<Skladnik> s2 = new ArrayList<Skladnik>();
				uklKorbowy.setSkladniki(s2);
				{
					
					{
						Czesc tlok = new Czesc("tlok", 30.80, 70.50);
						Skladnik sklTlok= new Skladnik(1, tlok);
						s2.add(sklTlok);
						List<Skladnik> s3 = new ArrayList<Skladnik>();
						tlok.setSkladniki(s3);
						{
							{
								Czesc sruba = new Czesc("sruba", 50.40, 80.40);
								Skladnik sklSruba = new Skladnik(4, sruba);
								s3.add(sklSruba);
								
							}
									
						}
						
					}
					
					{
						Czesc korbowod = new Czesc("korbowod", 100.40, 90.40);
						Skladnik sklKorbowod = new Skladnik(1, korbowod);
						s2.add(sklKorbowod);
						
					}
					{
						Czesc walKorbowy = new Czesc("wal korbowy", 60.90, 90.90);
						Skladnik sklWalKorbowy = new Skladnik(1, walKorbowy);
						s2.add(sklWalKorbowy);
						
					}
					{
						Czesc sruba = new Czesc("sruba", 40.40, 30.20);
						Skladnik sklSruba = new Skladnik(4, sruba);
						s2.add(sklSruba);
						
					}
				}
			}
			
			{
				Czesc uklRozrzadu = new Czesc("uklad rozrzadu", 70.50, 20.50);
				Skladnik sklUklRozrzadu= new Skladnik(1, uklRozrzadu);
				s1.add(sklUklRozrzadu);
				List<Skladnik> s2 = new ArrayList<Skladnik>();
				uklRozrzadu.setSkladniki(s2);
				{
					{
						Czesc zawor = new Czesc("zawor", 90.10, 10.10);
						Skladnik sklZawor = new Skladnik(4, zawor);
						s2.add(sklZawor);
						
					}

					{
						Czesc dzwigniaZaworowa = new Czesc("dxwignia zaworowa", 40.80, 70.90);
						Skladnik sklDzwigniaZaworowa = new Skladnik(1, dzwigniaZaworowa);
						s2.add(sklDzwigniaZaworowa);
						
					}
					{
						Czesc popychacze = new Czesc("popychacze", 80.40, 20.90);
						Skladnik sklPopychacze = new Skladnik(1, popychacze);
						s2.add(sklPopychacze);
						
					}
					
					{
						Czesc walRozrzadu  = new Czesc("wal rozrzadu", 80.40, 90.10);
						Skladnik sklWalRozrzadu = new Skladnik(1, walRozrzadu);
						s2.add(sklWalRozrzadu);
						
					}
					{
						Czesc sruba = new Czesc("sruba", 60.40, 30.30);
						Skladnik sklSruba = new Skladnik(2, sruba);
						s2.add(sklSruba);
						
					}
					
					
				}
			}
			{
				Czesc uklZasilania = new Czesc("uklad zasilania", 20.30, 80.40);
				Skladnik sklUklZasilania = new Skladnik(1, uklZasilania);
				s1.add(sklUklZasilania);
				List<Skladnik> s2 = new ArrayList<Skladnik>();
				uklZasilania.setSkladniki(s2);
				{
					{
						Czesc gaznika  = new Czesc("ga�nik", 70.80, 90.10);
						Skladnik sklGaznika  = new Skladnik(1, gaznika);
						s2.add(sklGaznika);
						
					}
					
					{
						Czesc pompa = new Czesc("pompa", 90.00, 10.10);
						Skladnik sklPompa = new Skladnik(1, pompa);
						s2.add(sklPompa);
						
					}
					
					{
						Czesc zbiornikPaliwa = new Czesc("zbiornik paliwa", 20.30, 10.0);
						Skladnik sklZbiornikPaliwa = new Skladnik(1, zbiornikPaliwa);
						s2.add(sklZbiornikPaliwa);
						
					}
					
					{
						Czesc filtrPowietrza = new Czesc("filtr powietrza", 30.10, 330.40);
						Skladnik sklFiltrPowietrza = new Skladnik(1, filtrPowietrza);
						s2.add(sklFiltrPowietrza);
						
					}

					{
						Czesc przewodDolotowy = new Czesc("przewod dolotowy", 10.20, 110.40);
						Skladnik sklPrzewodDolotowy = new Skladnik(1, przewodDolotowy);
						s2.add(sklPrzewodDolotowy);
						
					}

					{
						Czesc przewodWylotowy = new Czesc("przew�d wylotowy", 13.40, 770.40);
						Skladnik sklPrzewodWylotowy = new Skladnik(1, przewodWylotowy);
						s2.add(sklPrzewodWylotowy);
						
					}
					{
						Czesc sruba = new Czesc("sruba", 67.55, 23.41);
						Skladnik sklSruba = new Skladnik(1, sruba);
						s2.add(sklSruba);
						
					}
				}			
			}
			{
				Czesc uklZaplonowy = new Czesc("uk�ad zaplonowy", 50.40, 30.40);
				Skladnik sklUklZaplonowy = new Skladnik(1, uklZaplonowy);
				s1.add(sklUklZaplonowy);
				List<Skladnik> s2 = new ArrayList<Skladnik>();
				uklZaplonowy.setSkladniki(s2);
				{
					{
						Czesc cewkaZaplonowa = new Czesc("cewka zaplonowa", 120.11, 33.47);
						Skladnik sklCewkaZaplonowa  = new Skladnik(1, cewkaZaplonowa );
						s2.add(sklCewkaZaplonowa);
						
					}
					{
						Czesc przerywacz = new Czesc("przerywacz", 10.41, 3.12);
						Skladnik sklPrzerywacz = new Skladnik(1, przerywacz);
						s2.add(sklPrzerywacz);
						
					}
					{
						Czesc swiecaZaplonowa = new Czesc("swieca zaplonowa", 50.40, 30.40);
						Skladnik sklRozdzielacz = new Skladnik(1, swiecaZaplonowa);
						s2.add(sklRozdzielacz);
						
					}
					{
						Czesc sruba = new Czesc("sruba", 56.45, 33.43);
						Skladnik sklSruba = new Skladnik(1, sruba);
						s2.add(sklSruba);
						
					}
				}
			}
			Czesc uklSmarowania = new Czesc("uklad smarowania", 15.40, 34.40);
			Skladnik sklUklSmarowania = new Skladnik(1, uklSmarowania);
			s1.add(sklUklSmarowania);
			List<Skladnik> s2 = new ArrayList<Skladnik>();
			uklSmarowania.setSkladniki(s2);
			{ 
				{
					Czesc zbiornikOleju = new Czesc("zbiornik oleju", 57.40, 43.40);
					Skladnik sklZbiornikOleju = new Skladnik(1, zbiornikOleju);
					s2.add(sklZbiornikOleju);
					
				}
				{
					Czesc pompaOleju = new Czesc("pompa olejowa", 17.40, 38.40);
					Skladnik sklpompaOleju = new Skladnik(1, pompaOleju);
					s2.add(sklpompaOleju);
					
				}
				{
					Czesc filtr = new Czesc("filtr", 11.69, 12.38);
					Skladnik sklFiltr = new Skladnik(1, filtr);
					s2.add(sklFiltr);
					
				}
				{
					Czesc sruba = new Czesc("sruba", 51.41, 10.41);
					Skladnik sklSruba = new Skladnik(1, sruba);
					s2.add(sklSruba);
					
				}
			}
			{
				Czesc uklChodzenia = new Czesc("uklad chlodzenia", 50.40, 30.40);
				Skladnik sklUklChodzenia = new Skladnik(1, uklChodzenia);
				s1.add(sklUklChodzenia);
				List<Skladnik> s11 = new ArrayList<Skladnik>();
				uklChodzenia.setSkladniki(s11);
				{

					{
						Czesc chlodnica = new Czesc("chlodnica", 11.40, 3.20);
						Skladnik sklchlodnica = new Skladnik(100, chlodnica);
						s2.add(sklchlodnica);
						
					}
					{
						Czesc pompaWodna = new Czesc("pompa wodna", 59.32, 98.78);
						Skladnik sklpompaWodna = new Skladnik(1, pompaWodna);
						s2.add(sklpompaWodna);
						
					}
					{
						Czesc wentylator = new Czesc("wentylator", 22.44, 3.98);
						Skladnik sklWentylator = new Skladnik(1, wentylator);
						s2.add(sklWentylator);
						
					}

				}
				{
					Czesc sruba = new Czesc("sruba", 5.34, 31.44);
					Skladnik sklSruba = new Skladnik(1, sruba);
					s2.add(sklSruba);
					
				}
			}
		}
		silnik.setSkladniki(s1);
		return silnik;

	}
	
}

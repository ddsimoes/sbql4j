package pl.wcislo.sbql4j.java.test;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

import pl.wcislo.sbql4j.Statement;
import pl.wcislo.sbql4j.java.test.model.Address;
import pl.wcislo.sbql4j.java.test.model.Czesc;
import pl.wcislo.sbql4j.java.test.model.Employee;
import pl.wcislo.sbql4j.java.test.model.Czesc.Skladnik;
import pl.wcislo.sbql4j.java.utils.ReflectUtils;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.xml.parser.Parser;
import pl.wcislo.sbql4j.xml.parser.XMLParser;
import pl.wcislo.sbql4j.xml.parser.store.XMLObjectStore;

public class TestSBA4J {
	private static final Log log = LogFactory.getLog(TestSBA4J.class);
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new TestSBA4J();
	}
	
	public TestSBA4J() throws Exception {
		Object p = testGetFields();
		log.info(p);
//		testCloseBy();
		test_simpleJavaObj();
//		test_getMethods();
	}
	
	private void test_simpleJavaObj() throws Exception {
		 File data = new File("./data/emp1.xml");
		
		Parser parser = new XMLParser(new FileInputStream(data));
		parser.parseData();
		XMLObjectStore store = new XMLObjectStore();
		store.init(parser.getRootObject(), parser.getId2Object());
	
//		String expr = "count( :silnik closeby (skladniki.czesc) where koszt > 50)";
//		String expr = "sum(((:silnik,  (1 as ile)) closeby (skladniki.(czesc, (ile * ilosc) as ile)) ).(ile))";
//		String expr = "(((:f closeby listFiles()) where getName().toLowerCase().endsWith(\".pdf\")).getAbsolutePath() as plik)";
//		String expr = "(((:f closeby listFiles()) where getName().toLowerCase().endsWith(\".txt\")) as plik).plik.renameTo(getName()+\"backup\")";

		
		String expr = "(((:silnik as x, (1 as ile)) closeby x.(skladniki.(czesc as x, (ile*ilosc) as ile))) "
		+ "groupas wszystkieCzesciSilnika)" 
		+ ".((unique(wszystkieCzesciSilnika.x) as y)" 
		+ ".(y, sum((wszystkieCzesciSilnika where x = y).ile)))"
		;
//		String expr = ":emp.getAddress().getCity() as miasto";
//		String expr = ":emp where address.city = \"Warszawa\"";
//		String expr = "(:emp join address).(getEname(), city) ";
//		String expr = "(:emp).nieMaTakiegoPola ";
//		String expr = ":emp where getEname() = \"Kowalski\"";
//		String expr = "(:emp where address.city = \"Warszawa\").setEname(\"Warszawiak\")";
//		String expr = ":emp.getSalary()";
//		String expr = "avg(:emp.setSalary(getSalary()))";
//		String expr = "emp orderby salary desc, ename";
		Statement stmt = new Statement(expr);
		List<Employee> list = new ArrayList<Employee>();
		list.add(new Employee("Kowalski", 5545.50, new Address("Warszawa", "Pejza�owa 2", "02-703")));
		list.add(new Employee("Nowak", 3500d, new Address("Lublin", "pl. Litewski 1", "02-650")));
		
		
		Czesc silnik = testCloseBy();
//		stmt.setParam("emp", list);
		stmt.setParam("silnik", silnik);
		
//		File f = new File("E:/___TEST/");
//		System.out.println("f="+f);
//		stmt.setParam("f", f);
//		stmt.setParam("sal", new Integer(100));
		
		Interpreter i = new Interpreter(store);
		Object res = stmt.execute(i);
		
		log.info(res);
		log.info(pl.wcislo.sbql4j.util.Utils.resultToString((QueryResult) res, false));
		
	}
	
	private void test_getFields() throws Exception {
		Interpreter obj = new Interpreter();
		Class clazz = obj.getClass();
//		List<String> f = Utils.getFieldNames(clazz);
		
//		for(String field : f) {
//			Field jField = clazz.getDeclaredField(field);
//			jField.setAccessible(true);
//			Object o = jField.get(obj);
//			System.out.println(field+" "+o);
//		}	
	}
	
	private void test_getMethods() throws Exception {
		Employee e = new Employee();
//		for(Method m : e.getClass().getDeclaredMethods()) {
//			StringBuilder sb = new StringBuilder();
//			sb.append(m.getName());
//			for(Class<?> c : m.getParameterTypes()) {
//				sb.append(" "+c.getName());
//			}
//			System.out.println(sb.toString());
//		}
		ReflectUtils.getMethodNames(e.getClass());
	}
	

	private Czesc testCloseBy() {
		Czesc silnik = new Czesc("silnik", 300.40, 1000.40);
		List<Skladnik> s1 = new ArrayList<Skladnik>();
		{
			{
				Czesc uklKorbowy = new Czesc("uklad korbowy", 50.40, 30.40);
				Skladnik sklUklKorbowy = new Skladnik(1, uklKorbowy);
				s1.add(sklUklKorbowy);
				List<Skladnik> s2 = new ArrayList<Skladnik>();
				uklKorbowy.setSkladniki(s2);
				{
					
					{
						Czesc tlok = new Czesc("tlok", 50.40, 30.40);
						Skladnik sklTlok= new Skladnik(1, tlok);
						s2.add(sklTlok);
						
					}
					{
						Czesc korbowod = new Czesc("korbowod", 50.40, 30.40);
						Skladnik sklKorbowod = new Skladnik(1, korbowod);
						s2.add(sklKorbowod);
						
					}
					{
						Czesc korbowod = new Czesc("korbowod", 50.40, 30.40);
						Skladnik sklKorbowod = new Skladnik(2, korbowod);
						s2.add(sklKorbowod);
						
					}
					{
						Czesc walKorbowy = new Czesc("wal korbowy", 50.40, 30.40);
						Skladnik sklWalKorbowy = new Skladnik(1, walKorbowy);
						s2.add(sklWalKorbowy);
						
					}
				}
			}
			
			{
				Czesc uklRozrzadu = new Czesc("uklad rozrzadu", 50.40, 30.40);
				Skladnik sklUklRozrzadu= new Skladnik(1, uklRozrzadu);
				s1.add(sklUklRozrzadu);
				List<Skladnik> s2 = new ArrayList<Skladnik>();
				uklRozrzadu.setSkladniki(s2);
				{
					{
						Czesc zawor = new Czesc("zawor", 50.40, 30.40);
						Skladnik sklZawor = new Skladnik(4, zawor);
						s2.add(sklZawor);
						
					}

					{
						Czesc dzwigniaZaworowa = new Czesc("dxwignia zaworowa", 50.40, 30.40);
						Skladnik sklDzwigniaZaworowa = new Skladnik(1, dzwigniaZaworowa);
						s2.add(sklDzwigniaZaworowa);
						
					}
					{
						Czesc popychacze = new Czesc("popychacze", 50.40, 30.40);
						Skladnik sklPopychacze = new Skladnik(1, popychacze);
						s2.add(sklPopychacze);
						
					}
					
					{
						Czesc walRozrzadu  = new Czesc("wal rozrzadu", 50.40, 30.40);
						Skladnik sklWalRozrzadu = new Skladnik(1, walRozrzadu);
						s2.add(sklWalRozrzadu);
						
					}
					
				}
			}
			{
				Czesc uklZasilania = new Czesc("uklad zasilania", 50.40, 30.40);
				Skladnik sklUklZasilania = new Skladnik(1, uklZasilania);
				s1.add(sklUklZasilania);
				List<Skladnik> s2 = new ArrayList<Skladnik>();
				uklZasilania.setSkladniki(s2);
				{
					{
						Czesc gaznika  = new Czesc("ga�nik", 50.40, 30.40);
						Skladnik sklGaznika  = new Skladnik(1, gaznika);
						s2.add(sklGaznika);
						
					}
					
					{
						Czesc pompa = new Czesc("pompa", 50.40, 30.40);
						Skladnik sklPompa = new Skladnik(1, pompa);
						s2.add(sklPompa);
						
					}
					
					{
						Czesc zbiornikPaliwa = new Czesc("zbiornik paliwa", 50.40, 30.40);
						Skladnik sklZbiornikPaliwa = new Skladnik(1, zbiornikPaliwa);
						s2.add(sklZbiornikPaliwa);
						
					}
					
					{
						Czesc filtrPowietrza = new Czesc("filtr powietrza", 50.40, 30.40);
						Skladnik sklFiltrPowietrza = new Skladnik(1, filtrPowietrza);
						s2.add(sklFiltrPowietrza);
						
					}

					{
						Czesc przewodDolotowy = new Czesc("przewod dolotowy", 50.40, 30.40);
						Skladnik sklPrzewodDolotowy = new Skladnik(1, przewodDolotowy);
						s2.add(sklPrzewodDolotowy);
						
					}

					{
						Czesc przewodWylotowy = new Czesc("przew�d wylotowy", 50.40, 30.40);
						Skladnik sklPrzewodWylotowy = new Skladnik(1, przewodWylotowy);
						s2.add(sklPrzewodWylotowy);
						
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
						Czesc cewkaZaplonowa = new Czesc("cewka zaplonowa", 50.40, 30.40);
						Skladnik sklCewkaZaplonowa  = new Skladnik(1, cewkaZaplonowa );
						s2.add(sklCewkaZaplonowa);
						
					}
					{
						Czesc przerywacz = new Czesc("przerywacz", 50.40, 30.40);
						Skladnik sklPrzerywacz = new Skladnik(1, przerywacz);
						s2.add(sklPrzerywacz);
						
					}
					{
						Czesc swiecaZaplonowa = new Czesc("swieca zaplonowa", 50.40, 30.40);
						Skladnik sklRozdzielacz = new Skladnik(1, swiecaZaplonowa);
						s2.add(sklRozdzielacz);
						
					}
					
				}
			}
			Czesc uklSmarowania = new Czesc("uklad smarowania", 50.40, 30.40);
			Skladnik sklUklSmarowania = new Skladnik(1, uklSmarowania);
			s1.add(sklUklSmarowania);
			List<Skladnik> s2 = new ArrayList<Skladnik>();
			uklSmarowania.setSkladniki(s2);
			{ 
				{
					Czesc zbiornikOleju = new Czesc("zbiornik oleju", 50.40, 30.40);
					Skladnik sklZbiornikOleju = new Skladnik(1, zbiornikOleju);
					s2.add(sklZbiornikOleju);
					
				}
				{
					Czesc pompaOleju = new Czesc("pompa olejowa", 50.40, 30.40);
					Skladnik sklpompaOleju = new Skladnik(1, pompaOleju);
					s2.add(sklpompaOleju);
					
				}
				{
					Czesc filtr = new Czesc("filtr", 50.40, 30.40);
					Skladnik sklFiltr = new Skladnik(1, filtr);
					s2.add(sklFiltr);
					
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
						Czesc chlodnica = new Czesc("chlodnica", 50.40, 30.40);
						Skladnik sklchlodnica = new Skladnik(100, chlodnica);
						s2.add(sklchlodnica);
						
					}
					{
						Czesc pompaWodna = new Czesc("pompa wodna", 50.40, 30.40);
						Skladnik sklpompaWodna = new Skladnik(1, pompaWodna);
						s2.add(sklpompaWodna);
						
					}
					{
						Czesc wentylator = new Czesc("wentylator", 50.40, 30.40);
						Skladnik sklWentylator = new Skladnik(1, wentylator);
						s2.add(sklWentylator);
						
					}

				}
			}
		}
		silnik.setSkladniki(s1);
		return silnik;
	}
	
	public Map<String, Field> testGetFields() {
//		public static <T> Map<String, Field> getFieldNames(Class<T> clazz) {

			final Map<String, Field> result = new HashMap<String, Field>();
			List<Class<? super File>> sup = ReflectUtils.getSuperclasses(File.class);
			try {
//				List<Class<? super T>> sup = getSuperclasses(clazz);
				for (final Class<? super File> c : sup) {
					System.out.println(c.getName());
					ClassReader cr = new ClassReader(c.getName());
					EmptyVisitor fieldReader = new EmptyVisitor() {
						@Override
						public FieldVisitor visitField(int access, String name,
								String desc, String signature, Object value) {
							if (!result.containsKey(name)) {
								try {
									result.put(name, c.getDeclaredField(name));
								} catch (SecurityException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NoSuchFieldException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							return null;
						}
					};
					cr.accept(fieldReader, 0);
				}
			} catch (Exception e) {
				System.err.println(sup);
				e.printStackTrace();
			}

			return result;
//		}

	}

}

package pl.wcislo.sbql4j.test;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.wcislo.sbql4j.lang.parser.ParserCup;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.util.Utils;
import pl.wcislo.sbql4j.xml.parser.Parser;
import pl.wcislo.sbql4j.xml.parser.XMLParser;
import pl.wcislo.sbql4j.xml.parser.store.XMLObjectStore;

public class Test1 {
	private static final Log log = LogFactory.getLog(Test1.class);
	
	public static void main(String[] args) throws Exception {
		testMethods();
		File data = new File("data/emp1.xml");
		Parser parser = new XMLParser(new FileInputStream(data));
		parser.parseData();
		XMLObjectStore store = new XMLObjectStore();
		store.init(parser.getRootObject(), parser.getId2Object());
		
		
		String exp1= "emp orderby ename, salary desc";
//		String exp1= "emp orderby (salary DESC, ename)";
//		String exp1= "1 orderby 1";
//		String exp1= "avg(bag(1.321, 2.231) order by 1)";
//		String exp1= "min(emp.salary)";
//		String exp1= "emp.address.(street + \" ala\")";
//		String exp1= "deref((emp where ename = \"Kowalski\").salary )";
//		String exp1= "jnum,jnum";
//		String exp1= "emp where not exists(salary)";
//		String exp1= "(deref jnum),struct(1,2),struct(struct(3),struct(4,5),struct(6,7),8)";
//		String exp1= "bag(1,2)";
//		String exp1= "(struct(1,2) in struct(1,2)),(struct(1,2) in struct(struct(1,2),2))";
//		String exp1= "1 in 1";
//		String exp1= "bag(bag(bag(1,2)))";	
//		String exp1 = "bag()";
//		String exp1 = "bag(1,2,3,4)";
//		String exp1 = "(6,7,bag(1,2,3))";
//		String exp1 = "unique(deref emp.address.city)";
//		String exp1 = "deref((emp where ename = \"Kowalski\").salary) = 300.5";
		

		ParserCup cup = new ParserCup(exp1);
		cup.user_init();
		cup.parse();
		
		Interpreter i = new Interpreter(store);
		Object o = i.evaluateExpression(cup.RESULT);
		log.info(o);
		QueryResult t = (QueryResult) o;
		log.info(Utils.resultToString(t)); 
		
//		if(o instanceof Collection) {
//			System.out.println("Test1.main() o instanceof Collection");
//			for(Object oo : (Collection)o) {
//				if(oo instanceof BaseObject) {
//					System.out.println(Utils.objectToString((BaseObject) oo));
//				}
//			}
//		}
//		System.out.println(o.getClass().getSimpleName()+":"+o);	
		
//		Bag bag = (Bag) o;
//		for(Object o2 : bag) {
//			JPSIdentifier id = (JPSIdentifier) o2;
//			SimpleObject so = (SimpleObject) store.get(id);
//			
//			
//			System.out.println(so.getValue());
//		}
//		
		
		
		
	}
	
	public static void testMethods() {
		for(Method m : String.class.getMethods()) {
			log.info(m.toString());
		}
	}
}
	
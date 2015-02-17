package pl.wcislo.sbql4j.java.test;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.java.test.model.Address;
import pl.wcislo.sbql4j.java.test.model.Employee;


public class JGrammarTest1<E> {
	public static void main(String[] args) {
		List<Employee> employees = new ArrayList<Employee>();
		employees.add(new Employee("Kowalski", 5545.50, new Address("Warszawa", "Pejza�owa 2", "02-703")));
		employees.add(new Employee("Nowak", 3500d, new Address("Lublin", "pl. Litewski 1", "02-650")));
		
		
//		Object p = #:employees.getSalary()#;
//		System.out.println(p);
		
		int salary = 100;
//		#:employees.setSalary(:salary)#;
		
//		Object p = # :employees.("A teraz pracownik "+ename+" zarabia "+getSalary()+" i mieszka w "+getAddress().getCity())#;
//		Object p = //GENERATED FROM: # :employees.(getAddress().getCity())#
//new pl.wcislo.sbql4j.java.utils.JavaStatement(" :employees.(getAddress().getCity())", employees).execute();
//		System.out.println(p);
	}
//	String p = #:emp.setSalary(:salary)#;
//	Object p = new pl.edu.pjwstk.jps.sba4j.utils.JavaStatement(new pl.edu.pjwstk.jps.Statement(" :emp.salary ", emp)).execute();
 
//	public static void main(String[] args) {
//		Reader r = null;
//		StringWriter sw = new StringWriter();
//		char[] buffer = new char[0xFFFF];
//		int read;
//		do {
//			read = r.read(buffer);
//			sw.write(buffer, 0, read);	
//		} while(read > 0);
//		String result = sw.toString();
//		
//	}
}

package pl.wcislo.sbql4j.javac.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SimpleClass {
	private static final Log log = LogFactory.getLog(SimpleClass.class);
	
	private String field1;
	private int field2;
	
	public SimpleClass() {
		// TODO Auto-generated constructor stub
	}

	public SimpleClass(String f1, int f2) {
		super();
		this.field1 = f1;
		this.field2 = f2;
	}
	
	private static void m0() {
		double a = 3.42;
		double b = Math.round(a);
		log.info(b);
	}
	
}

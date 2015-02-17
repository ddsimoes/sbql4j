package pl.wcislo.sbql4j.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.wcislo.sbql4j.lang.parser.ParserCup;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;

public class TestParser {
	private static final Log log = LogFactory.getLog(TestParser.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String exp1= "bag<ArrayList> n";
		//String exp1= "1<>1";
		ParserCup cup = new ParserCup(exp1);
		cup.user_init();
		cup.parse();
		
		Interpreter i = new Interpreter();
		cup.RESULT.accept(i, null);
		log.info(i.getQres().pop());
//		System.out.println(cup.RESULT);
	}

}

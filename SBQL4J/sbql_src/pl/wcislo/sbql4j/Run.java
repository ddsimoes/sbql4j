package pl.wcislo.sbql4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.wcislo.sbql4j.lang.parser.ParserCup;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.util.Utils;
import pl.wcislo.sbql4j.xml.parser.Parser;
import pl.wcislo.sbql4j.xml.parser.XMLParser;
import pl.wcislo.sbql4j.xml.parser.store.XMLObjectStore;

public class Run {
	
	private static final Log log = LogFactory.getLog(Run.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// File data = new File("./data/emp1.xml");
		if (args.length == 0) {
			log.fatal("cannot find database file");
			System.exit(1);
		}
		File data = new File(args[0]);
		if (!data.exists()) {
			log.fatal("cannot find database file");
			System.exit(1);
		}
		Parser parser = new XMLParser(new FileInputStream(data));
		parser.parseData();
		XMLObjectStore store = new XMLObjectStore();
		store.init(parser.getRootObject(), parser.getId2Object());
		
//		Utils.resultToString(parser.getRootObject());

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String in = "";
		StringBuilder sb = new StringBuilder();
		;
		int lineNumber = 0;
		//XXX TODO
//		NTSystem system = new NTSystem();
//		String sysUser = "<" + System.getProperty("user.name") + "@"
//				+ system.getDomain() + ">";
		String sysUser = "emil";

		while (true) {
			if (lineNumber == 0)
				System.out.append(sysUser);

			in = br.readLine().trim();

			if (in.equalsIgnoreCase("exit"))
				break;

			lineNumber++;
			int endIndex = in.indexOf(";");
			if (endIndex == -1) {
				sb.append(in).append(" ");
			} else {
				sb.append(in);
				if (endIndex < in.length() - 1) {
					System.out.println("*** SBQL error in admin.test at line "
							+ lineNumber + ", column " + (endIndex + 1)
							+ ": Unexpected token # ("
							+ (in.substring(endIndex + 1)) + ")");
				} else {

					
					try {
						ParserCup cup = new ParserCup(sb.toString().substring(0,
								sb.length() - 1));
						cup.user_init();
						cup.parse();

						
						Interpreter i = new Interpreter(store);
						Object o = i.evaluateExpression(cup.RESULT);
						System.out.println(o);
						System.out.println(Utils.resultToString((ENVSType) o));
					} catch (Exception e) {
						System.err.println(e.getMessage());
//						e.printStackTrace();
						lineNumber = 0;
					}
				}

				lineNumber = 0;
				sb = new StringBuilder();
			}
		}
	}
}

package pl.wcislo.sbql4j.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggerTest {
	private static final Log log = LogFactory.getLog(LoggerTest.class);
	
	public static void main(String[] args) {
		new LoggerTest();
	}
	
	public LoggerTest() {
		log.debug("Started");
		log.info("info");
		log.warn("warn");
		log.error("error");
		log.fatal("fatal");
		
		
	}
}

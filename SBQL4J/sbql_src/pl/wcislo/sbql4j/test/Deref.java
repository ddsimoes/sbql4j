package pl.wcislo.sbql4j.test;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.wcislo.sbql4j.xml.model.XmlId;
import pl.wcislo.sbql4j.xml.parser.Parser;
import pl.wcislo.sbql4j.xml.parser.XMLParser;
import pl.wcislo.sbql4j.xml.parser.store.XMLObjectStore;

public class Deref {
	private static final Log log = LogFactory.getLog(Deref.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		XmlId id = new XmlId(20);
		
		File data = new File("data/emp1.xml");
		Parser parser = new XMLParser(new FileInputStream(data));
		parser.parseData();
		XMLObjectStore store = new XMLObjectStore();
		store.init(parser.getRootObject(), parser.getId2Object());
		
//		log.info(((SimpleDBObject)store.get(id)));
	}

}

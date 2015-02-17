package pl.wcislo.sbql4j.lang.xml;

import java.io.File;

import pl.wcislo.sbql4j.java.model.runtime.Struct;

public class XMLDataSource {
	private XMLParser parser;
	
	public XMLDataSource(File xmlFile) {
		this.parser = new XMLParser(xmlFile);
	}
	
	public XMLParser getParser() {
		return parser;
	}
	
	public Struct getParsedData() {
		if(!parser.isParsed()) {
			parser.parseSource();
		}
		return parser.getParsedResult();
	}
}

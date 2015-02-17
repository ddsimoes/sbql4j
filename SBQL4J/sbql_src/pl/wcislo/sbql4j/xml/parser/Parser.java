package pl.wcislo.sbql4j.xml.parser;

import java.util.List;
import java.util.Map;

import pl.wcislo.sbql4j.xml.model.ComplexDBObject;
import pl.wcislo.sbql4j.xml.model.DBObject;
import pl.wcislo.sbql4j.xml.model.XmlId;

/**
 * @author emil.wcislo 
 */
public interface Parser {

	public void parseData() throws ParserException;

	public ComplexDBObject getRootObject();

	public List<DBObject> getObjectList();

	public Map<XmlId, DBObject> getId2Object();

}
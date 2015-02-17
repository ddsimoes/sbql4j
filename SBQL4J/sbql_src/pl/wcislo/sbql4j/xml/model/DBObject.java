package pl.wcislo.sbql4j.xml.model;

import java.io.Serializable;

import pl.wcislo.sbql4j.lang.types.ENVSType;

/**
 * @author emil.wcislo 
 */
public interface DBObject extends Serializable, ENVSType {
	public XmlId getId(); 
	public ComplexDBObject getParent();
	public void setParent(ComplexDBObject parent);
	
}
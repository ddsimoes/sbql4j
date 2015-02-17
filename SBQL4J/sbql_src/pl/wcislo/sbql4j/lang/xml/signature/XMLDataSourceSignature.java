package pl.wcislo.sbql4j.lang.xml.signature;

import pl.wcislo.sbql4j.java.model.compiletime.StructSignature;
import pl.wcislo.sbql4j.lang.xml.XMLTypeMapper;

public class XMLDataSourceSignature extends StructSignature {
	private XMLTypeMapper typeMapper;
	
	public XMLDataSourceSignature() {
		super();
	}
	
	public void setTypeMapper(XMLTypeMapper typeMapper) {
		this.typeMapper = typeMapper;
	}
	public XMLTypeMapper getTypeMapper() {
		return typeMapper;
	}

}

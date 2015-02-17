package pl.wcislo.sbql4j.lang.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.runtime.Struct;
import pl.wcislo.sbql4j.lang.xml.XMLTypeMapper.XMLTypeMapperEntry;
import sun.font.LayoutPathImpl.EndType;

public class XMLParser {
//	private SAXBuilder builder;
	private Document doc;
	private Struct parsedResult;
	private List<String> currentPath = new ArrayList<String>();
	private XMLTypeMapper mapper;
	
	public static void main(String[] args) {
		XMLParser reader = new XMLParser(new File("data/books.xml"));
		Struct s = reader.parseSource();
		System.out.println(s);
	}
	
	public XMLParser(File xmlFile) {
		try {
			SAXBuilder builder = new SAXBuilder();
			doc = builder.build(xmlFile);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setMapper(XMLTypeMapper mapper) {
		this.mapper = mapper;
	}
	
	public Struct parseSource() {
		Element rootEl = doc.getRootElement();
		Struct res = new Struct();
		parseElement(rootEl, res);
		this.parsedResult = res;
		return res;
	}
	
	public Struct getParsedResult() {
		return parsedResult;
	}
	public boolean isParsed() {
		return parsedResult != null;
	}
	
	private void parseElement(Element el, Struct parentNode) {
		
		String elName = el.getName();
		currentPath.add(elName);
		XMLTypeMapperEntry entry = mapper.getEntry(currentPath.toArray(new String[]{}));
		boolean simpleTypeWithAttr = (entry != null && entry.isSimpleTypeWithAttributes());
		Object res;
		if(el.getChildren().isEmpty() && !simpleTypeWithAttr) {
			res = parseSimpleElement(el);
		} else if(simpleTypeWithAttr) {
			Struct resStruct = new Struct(true);
			Object value = parseSimpleElement(el);
			resStruct.put("_value", value);
			List<Attribute> attrList = el.getAttributes();
			for(Attribute attr : attrList) {
				String attrName = attr.getName();
				currentPath.add(attrName);
				Object attrVal = parseAttribute(attr);
				
				addParsedElToStruct(attrVal, attrName, resStruct);
				currentPath.remove(currentPath.size()-1);
			}
			res = resStruct;
		} else {
			Struct resStruct = new Struct();
			List<Element> childElList = el.getChildren();
			for(Element childEl : childElList) {
				parseElement(childEl, resStruct);
			}
			List<Attribute> attrList = el.getAttributes();
			for(Attribute attr : attrList) {
				String attrName = attr.getName();
				currentPath.add(attrName);
				Object attrVal = parseAttribute(attr);
				
				addParsedElToStruct(attrVal, attrName, resStruct);
				currentPath.remove(currentPath.size()-1);
			}
			res = resStruct;
		}
		addParsedElToStruct(res, elName, parentNode);
		currentPath.remove(currentPath.size()-1);
	}
	
	private Object parseSimpleElement(Element el) {
		if(mapper != null) {
			return mapper.parseValue(currentPath.toArray(new String[]{}), el.getText());
		} else {
			return el.getText();	
		}
	}
	private Object parseAttribute(Attribute attr) {
		if(mapper != null) {
			return mapper.parseValue(currentPath.toArray(new String[]{}), attr.getValue());
		} else {
			return attr.getValue();	
		}
	}
	
	
	private void addParsedElToStruct(Object o, String oName, Struct s) {
		XMLTypeMapperEntry entry = mapper.getEntry(currentPath.toArray(new String[]{}));
		SCollectionType colType = SCollectionType.NO_COLLECTION;
		if(entry != null) {
			colType = entry.getColType();
		}
		Object currentVal = s.get(oName);
		if(currentVal != null) {
			if(currentVal instanceof Collection) {
				Collection currentCol = (Collection) currentVal;
				currentCol.add(o);
			} else {
				Collection newCol = new ArrayList();
				newCol.add(currentVal);
				newCol.add(o);
				s.put(oName, newCol);
			}
		} else {
			if(colType == SCollectionType.NO_COLLECTION) {
				s.put(oName, o);
			} else {
				Collection newCol = new ArrayList();
				newCol.add(o);
				s.put(oName, newCol);
			}
			
		}
	}
}
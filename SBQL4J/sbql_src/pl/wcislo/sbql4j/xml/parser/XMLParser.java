package pl.wcislo.sbql4j.xml.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.wcislo.sbql4j.xml.model.ComplexDBObject;
import pl.wcislo.sbql4j.xml.model.DBObject;
import pl.wcislo.sbql4j.xml.model.ReferenceDBObject;
import pl.wcislo.sbql4j.xml.model.XmlId;

/**
 * @author emil.wcislo 
 */
public class XMLParser implements Parser {
	private InputSource xmlInputSource;
	private Document xmlDoc;
	private ComplexDBObject rootObject;
	private List<DBObject> objectList = new ArrayList<DBObject>();
	private List<DBObject> objectList1Lev = new ArrayList<DBObject>();
	private Map<XmlId, DBObject> id2Object = new HashMap<XmlId, DBObject>();
	
	private Map<Long, DBObject> xmlId2Object = new HashMap<Long, DBObject>();
	private Map<Long, ReferenceDBObject> unfinishedRefs = new HashMap<Long, ReferenceDBObject>();
	
	public XMLParser(InputSource xmlInputSource) {
		this.xmlInputSource = xmlInputSource;
	}
	public XMLParser(InputStream xmlInputStream) {
		this.xmlInputSource = new InputSource(xmlInputStream);
	}
	public XMLParser(String xml) {
		this.xmlInputSource = new InputSource(new StringReader(xml));
	}
	
	
	/* (non-Javadoc)
	 * @see pl.wcislo.jps.model.parser.Parser#parseData()
	 */
	public void parseData() throws ParserException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(false);
			factory.setIgnoringElementContentWhitespace(true);
			
			DocumentBuilder builder;
			
			builder = factory.newDocumentBuilder();
			
			this.xmlDoc = builder.parse(xmlInputSource);
			parseDocument(xmlDoc);
			completeUnfinishedRefs();
			for(DBObject o : rootObject.getNestedObjects()) {
				objectList1Lev.add(o);	
			}
//			String xmlRes = Utils.objectToString(rootObject);
//			System.out.println(xmlRes);
//			System.out.println();
//			for(BaseObject o : objectList) {
//				System.out.println(Utils.objectToString(o));
//				System.out.println();
//			}
		} catch (ParserConfigurationException e) {
			System.err.println(e.getMessage());
			throw new ParserException(e.getMessage(), e);
		} catch (SAXException e) {
			System.err.println(e.getMessage());
			throw new ParserException(e.getMessage(), e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			throw new ParserException(e.getMessage(), e);
		}
	}
		
	private ComplexDBObject parseDocument(Document doc) {
		this.rootObject = new ComplexDBObject("db");
		Node rootNode = doc.getFirstChild();
		parseNodes(rootNode.getChildNodes(), rootObject);
		return rootObject;
	}
	
	private void parseNodes(NodeList nodeList, ComplexDBObject parent) {
		for(int i=0; i<nodeList.getLength(); i++) {
			Node n = nodeList.item(i);
			if(n.getNodeType() != Node.TEXT_NODE) {
				parseNode(n, parent);
			}
		}
	}
	
	private void parseNode(Node n, ComplexDBObject parent) {
		DBObject object = null;
		Node idRefAtt = n.getAttributes().getNamedItem("IDREF");
		int childNodesCount = n.getChildNodes().getLength(); 
		if(childNodesCount > 1) {
			ComplexDBObject complexObject = new ComplexDBObject(n.getNodeName(), parent);
			object = complexObject;
			parseNodes(n.getChildNodes(), complexObject);
		} else if(idRefAtt == null && childNodesCount == 1){
//			SimpleDBObject<?> simpleObject = SimpleDBObjectFactory.createSimpleObject(n.getNodeName(), n.getFirstChild().getTextContent(), parent); 
//				new SimpleObject<String>(n.getNodeName(), n.getFirstChild().getTextContent(), parent);
//			object = simpleObject;
		} else if(idRefAtt != null) {
			long refId = Long.parseLong(idRefAtt.getTextContent());
			DBObject refObj = xmlId2Object.get(refId);
			ReferenceDBObject ref;
			if(refObj != null) {
				ref = new ReferenceDBObject(n.getNodeName(), refObj, parent);
			} else {
				ref = new ReferenceDBObject(n.getNodeName(), parent);
				unfinishedRefs.put(refId, ref);
			}
			object = ref;
		} else {
			System.err.println("Unsupported xml node: "+n);
			return;
		}
		Node idAtt = n.getAttributes().getNamedItem("XmlId");
		if(idAtt != null) {
			xmlId2Object.put(Long.parseLong(idAtt.getTextContent()), object);
		}
		objectList.add(object);
		id2Object.put(object.getId(), object);
	}
	
	private void completeUnfinishedRefs() {
		for(Iterator<Long> it = unfinishedRefs.keySet().iterator(); it.hasNext(); ) {
			Long l = it.next();
			if(xmlId2Object.containsKey(l)) {
				unfinishedRefs.get(l).setRefObject(xmlId2Object.get(l));
				it.remove();
			}
		}
		if(!unfinishedRefs.isEmpty()) {
			System.err.println("Got dandling refs: ");
			for(Long l : unfinishedRefs.keySet()) {
				System.err.println("\t"+l);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		File data = new File("data/emp1.xml");
		Parser parser = new XMLParser(new FileInputStream(data));
		parser.parseData();
	}
	/* (non-Javadoc)
	 * @see pl.wcislo.jps.model.parser.Parser#getRootObject()
	 */
	public ComplexDBObject getRootObject() {
		return rootObject;
	}
	/* (non-Javadoc)
	 * @see pl.wcislo.jps.model.parser.Parser#getObjectList()
	 */
	public List<DBObject> getObjectList() {
		return objectList;
	}
	/* (non-Javadoc)
	 * @see pl.wcislo.jps.model.parser.Parser#getId2Object()
	 */
	public Map<XmlId, DBObject> getId2Object() {
		return id2Object;
	}
	
}

package pl.wcislo.sbql4j.lang.db4o.indexes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class IndexMetadataXMLStore implements IndexMetadataStore {
	private static final String INDEXED_CLASS = "indexedClass";
	private static final String FIELD = "field";
	private static final String ROOT_EL_NAME = "db4oIndexMetadata";
	private static final String INDEX_ENTRY_EL = "indexEntry";
	
	
	private File xmlFile;
	public IndexMetadataXMLStore(File xmlFile) {
		this.xmlFile = xmlFile;
	}
	
	@Override
	public List<IndexMetadataEntry> loadData() {
		List<IndexMetadataEntry> res = new ArrayList<IndexMetadataEntry>();
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(xmlFile);
			Element root = doc.getRootElement();
			List<Element> entries = root.getChildren();
			for(Element indexEntryElement : entries) {
				String className = indexEntryElement.getChildText(INDEXED_CLASS);
				String fieldName = indexEntryElement.getChildText(FIELD);
				if(className != null && fieldName != null) {
					IndexMetadataEntry e = new IndexMetadataEntry(className, fieldName);
					res.add(e);
				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public void storeData(List<IndexMetadataEntry> data) {
		XMLOutputter xmlOut = new XMLOutputter(Format.getPrettyFormat());
		Document doc = new Document();
		Element rootEl = new Element(ROOT_EL_NAME);
		doc.setRootElement(rootEl);
		for(IndexMetadataEntry e : data) {
			Element entryEl = new Element(INDEX_ENTRY_EL);
			rootEl.addContent(entryEl);
			Element classNameEl = new Element(INDEXED_CLASS);
			classNameEl.setText(e.getIndexedClass());
			entryEl.addContent(classNameEl);
			Element fieldNameEl = new Element(FIELD);
			fieldNameEl.setText(e.getFieldName());
			entryEl.addContent(fieldNameEl);
		}
		try {
			xmlOut.output(doc, new FileWriter(xmlFile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}

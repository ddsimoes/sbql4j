package pl.wcislo.sbql4j.xml.parser.store;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import pl.wcislo.sbql4j.xml.model.ComplexDBObject;
import pl.wcislo.sbql4j.xml.model.DBObject;
import pl.wcislo.sbql4j.xml.model.ReferenceDBObject;
import pl.wcislo.sbql4j.xml.model.XmlId;

public class XMLObjectStore implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6386592313137574091L;
	
	private Map<XmlId, DBObject> id2Object;
	private ComplexDBObject rootObj;
	
	public void init(ComplexDBObject root, Map<XmlId, DBObject> id2Object) {
		this.rootObj = root;
		this.id2Object = id2Object;
	}
	
	public DBObject get(XmlId id) {
		return id2Object.get(id);
	}
	
	public boolean update(XmlId id, DBObject val) {
		DBObject o = id2Object.get(id);
		if(o != null) {
			val.setParent(o.getParent());
			o.setParent(null);
			//TODO czy robic update obiektow podrzednych?
			id2Object.put(id, val);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean delete(DBObject val) {
		return this.delete(val.getId());
	}
	public boolean delete(XmlId id) {
		DBObject o = id2Object.get(id);
		if(o != null) {
			o.setParent(null);
			if(o instanceof ComplexDBObject) {
				ComplexDBObject co = (ComplexDBObject) o;
				for(Iterator<DBObject> it = co.getNestedObjects().iterator(); it.hasNext(); ) {
					DBObject bo = it.next();
					bo.setParent(null);
					it.remove();
				}
			}
			if(o instanceof ReferenceDBObject) {
				ReferenceDBObject ro = (ReferenceDBObject) o;
				ro.setRefObject(null);
			}
			id2Object.remove(id);
			return true;
		} else {
			return false;
		}
	}
	
	public void add(DBObject obj) {
		this.id2Object.put(obj.getId(), obj);
	}

	public ComplexDBObject getRootObj() {
		return rootObj;
	}
	
}

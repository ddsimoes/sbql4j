package pl.wcislo.sbql4j.xml.model;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.lang.types.ENVSType;

/**
 * @author emil.wcislo 
 */
public class ReferenceDBObject implements DBObject {
	private XmlId id;
	private ComplexDBObject parent;
	private String name;
	
	private DBObject refObject;
	
	public ReferenceDBObject(String name) {
		this.name = name;
		this.id = XmlId.getNextID();
	}
	public ReferenceDBObject(String name, ComplexDBObject parent) {
		this(name);
		this.parent = parent;
		parent.getNestedObjects().add(this);
	}
	public ReferenceDBObject(String name, DBObject refObject, ComplexDBObject parent) {
		this(name, parent);
		this.refObject = refObject;
	}
	public DBObject getRefObject() {
		return refObject;
	}
	public void setRefObject(DBObject refObject) {
		this.refObject = refObject;
	}
	@Override
	public List<ENVSType> nested() {
		List<ENVSType> res = new ArrayList<ENVSType>();
		if(refObject != null) {
			res.add(new Binder(refObject.getName(), refObject.getId()));
		}
		return res;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return equalsReference(obj);
		final ReferenceDBObject other = (ReferenceDBObject) obj;
		if (refObject == null) {
			if (other.refObject != null)
				return false;
		} else if (!refObject.equals(other.refObject))
			return false;
		return true;
	}
	
	public boolean equalsReference(Object obj) {
		if(refObject == null) {
			return false;
		}
		if (refObject.getClass() != obj.getClass()) {
			return false;
		}
		DBObject other = (DBObject) obj;
		if(this.getName().equals(other.getName())) {
			return refObject.equals(obj);
		} 
		return false;
	}
	@Override
	public XmlId getId() {
		return id;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public ComplexDBObject getParent() {
		return parent;
	}
	@Override
	public void setParent(ComplexDBObject parent) {
		this.parent = parent;
	}
}

package pl.wcislo.sbql4j.xml.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.lang.types.ComplexType;
import pl.wcislo.sbql4j.lang.types.ENVSType;

/**
 * @author emil.wcislo
 */
public class ComplexDBObject extends ComplexType<DBObject> implements DBObject {

	private XmlId id;
	private ComplexDBObject parent;
	private String name;
	
//	private List<BaseObject> nestedObjects = new ArrayList<BaseObject>();

	public ComplexDBObject(String name) {
		this.name = name;
		this.id = XmlId.getNextID();
	}

	public ComplexDBObject(String name, ComplexDBObject parent) {
		this(name);
		this.parent = parent;
		parent.getNestedObjects().add(this);
	}

//	public List<BaseObject> getNestedObjects() {
//		return nestedObjects;
//	}


	@Override
	public List<ENVSType> nested() {
		List<ENVSType> result = new ArrayList<ENVSType>();
		for (DBObject o : getNestedObjects()) {
			result.add(new Binder(o.getName(), o.getId()));
		}
		return result;
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


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(getId().toString()).append(", ").append(
				getName()).append(", (");
		for(Iterator<DBObject> it = getNestedObjects().iterator(); it.hasNext(); ) {
			sb.append(it.next().getId());
			if(it.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append(")>");
		return sb.toString();
	}
}

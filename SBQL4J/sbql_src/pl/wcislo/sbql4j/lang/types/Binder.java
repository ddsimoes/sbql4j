package pl.wcislo.sbql4j.lang.types;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.model.QueryResult;

public class Binder implements ENVSType {
	public final String name;
	public final QueryResult object;
	
	public Binder(String name, QueryResult object) {
		this.name = name;
		this.object = object;
	}
	
	@Override
	public String toString() {
		return /*this.getClass().getName()+" "+*/name+"("+object+")";
	}
	
	@Override
	public List<ENVSType> nested() {
		List<ENVSType> res = new ArrayList<ENVSType>();
		res.add(this);
		return res;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public QueryResult deref() {
		if(object != null && (object instanceof Binder)) {
			return ((Binder)object).deref();
		} else {
			return object;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Binder) {
			obj = ((Binder)obj).deref();
		}
		return object.equals(obj);
	}

//	@Override
//	public ENVSType executeMethod(String name, Object... params) {
//		if(object instanceof Executable) {
//			return ((Executable)object).executeMethod(name, params); 
//		} else {
//			return new Bag();
//		}
//	}
	
//	
//	@Override
//	public int compareTo(ENVSType o) {
//		if(object instanceof Comparable && o instanceof Comparable) {
//			((Comparable)object).compareTo(o)
//		}
//		return object.compareTo(o);
//	}
}

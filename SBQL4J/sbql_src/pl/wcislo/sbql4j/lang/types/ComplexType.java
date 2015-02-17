package pl.wcislo.sbql4j.lang.types;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.model.QueryResult;

/**
 * 
 * @author Emil
 *
 * @param <E> type of nested object (BaseObject or JavaObject)
 */
public abstract class ComplexType<E extends QueryResult> implements QueryResult {

	protected List<E> nestedObjects = new ArrayList<E>();
	
//	@Override
//	public List<ENVSType> nested() {
//		List<ENVSType> result = new ArrayList<ENVSType>();
//		for (ENVSType o : getNestedObjects()) {
//			result.add(new Binder(o.getName(), o.getId()));
//		}
//		return result;
//	}
	
	public List<E> getNestedObjects() {
		return nestedObjects;
	}

}

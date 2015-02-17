package pl.wcislo.sbql4j.model.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.model.QueryResult;



public abstract class AbstractCollectionResult implements CollectionResult {
	private CollectionType cType;
	protected Collection<QueryResult> innerCol;
	
	public AbstractCollectionResult(CollectionType cType, Collection<QueryResult> innerCol) {
		this.cType = cType;
		this.innerCol = innerCol;
	}
	
	@Override
	public CollectionType getCollectionType() {
		return cType;
	}
	
	@Override
	public List<ENVSType> nested() {
		return Collections.emptyList();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(cType).append("[");
		for(Iterator<QueryResult> i = this.iterator(); i.hasNext(); ) {
			sb.append(i.next().toString());	
			if(i.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	public Class<? extends Collection> getInnerCollectionType() {
		if(innerCol == null) return null;
		return innerCol.getClass();
	}

	//------------------ interfejs collection ---------------------------//
	public boolean add(QueryResult e) {
		if(e instanceof CollectionResult) {
			return innerCol.addAll((CollectionResult) e);
		} else {
			return innerCol.add(e);	
		}
		
	}

	public boolean addAll(Collection<? extends QueryResult> c) {
		return innerCol.addAll(c);
	}

	public void clear() {
		innerCol.clear();
	}

	public boolean contains(Object o) {
		return innerCol.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return innerCol.containsAll(c);
	}

	public boolean equals(Object o) {
		return innerCol.equals(o);
	}

	public int hashCode() {
		return innerCol.hashCode();
	}

	public boolean isEmpty() {
		return innerCol.isEmpty();
	}

	public Iterator<QueryResult> iterator() {
		return innerCol.iterator();
	}

	public boolean remove(Object o) {
		return innerCol.remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return innerCol.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return innerCol.retainAll(c);
	}

	public int size() {
		return innerCol.size();
	}

	public Object[] toArray() {
		return innerCol.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return innerCol.toArray(a);
	}
	
	

}


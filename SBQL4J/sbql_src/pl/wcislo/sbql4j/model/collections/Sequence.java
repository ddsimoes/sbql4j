package pl.wcislo.sbql4j.model.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import pl.wcislo.sbql4j.exception.SBQLException;
import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.model.QueryResult;


public class Sequence<T extends List<QueryResult>> extends AbstractCollectionResult implements List<QueryResult> {

	public Sequence(T list) {
		super(CollectionType.SEQUENCE, list);
	}
	
	public Sequence() {
		super(CollectionType.SEQUENCE, new ArrayList<QueryResult>());
	}

	
	
	
	/**
	 * Test method
	 * @param args
	 */
	public static void main(String[] args) {
		Sequence s = new Sequence();
		Binder b = new Binder("asdf", null); 
		s.add(b);
	}
	
	@Override
	public Sequence createSameType() {
		try {
			Class<List> clazz = (Class<List>) this.innerCol.getClass();
			Sequence s = new Sequence(clazz.newInstance());
			return s;
		} catch (Exception e) {
			throw new SBQLException();
		}
	}

	
//-------------------------------- interfejs List -----------------------------//	
	public void add(int index, QueryResult element) {
		if(element instanceof CollectionResult) {
			((List<QueryResult>) innerCol).addAll((CollectionResult)element);
		} else {
			((List<QueryResult>) innerCol).add(index, element);	
		}
	}

	public boolean addAll(int index, Collection<? extends QueryResult> c) {
		return ((List<QueryResult>) innerCol).addAll(index, c);
	}

	public QueryResult get(int index) {
		return ((List<QueryResult>) innerCol).get(index);
	}

	public int indexOf(Object o) {
		return ((List<QueryResult>) innerCol).indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return ((List<QueryResult>) innerCol).lastIndexOf(o);
	}

	public ListIterator<QueryResult> listIterator() {
		return ((List<QueryResult>) innerCol).listIterator();
	}

	public ListIterator<QueryResult> listIterator(int index) {
		return ((List<QueryResult>) innerCol).listIterator(index);
	}

	public QueryResult remove(int index) {
		return ((List<QueryResult>) innerCol).remove(index);
	}

	public QueryResult set(int index, QueryResult element) {
		return ((List<QueryResult>) innerCol).set(index, element);
	}

	public List<QueryResult> subList(int fromIndex, int toIndex) {
		return ((List<QueryResult>) innerCol).subList(fromIndex, toIndex);
	}

	
}

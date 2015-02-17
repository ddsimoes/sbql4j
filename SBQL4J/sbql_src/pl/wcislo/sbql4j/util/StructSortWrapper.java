package pl.wcislo.sbql4j.util;

import java.awt.image.DataBufferShort;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import pl.wcislo.sbql4j.exception.SBQLException;
import pl.wcislo.sbql4j.java.model.runtime.JavaObject;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression.SortType;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.model.StructSBQL;
import pl.wcislo.sbql4j.xml.parser.store.XMLObjectStore;

public class StructSortWrapper extends StructSBQL implements Comparable<StructSortWrapper> {
	private List<SortType> sortTypes = new ArrayList<SortType>();
	private List<Comparator> comparators = new ArrayList<Comparator>();
	private XMLObjectStore store;
	
	public StructSortWrapper(XMLObjectStore store) {
		this.store = store;
	}
	
	@Override
	public boolean add(QueryResult e) {
		boolean b = super.add(e);
		sortTypes.add(null);
		comparators.add(null);
		return b;
	}
	
	@Override
	public void add(int index, QueryResult element) {
		super.add(index, element);
		sortTypes.add(index, null);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends QueryResult> c) {
		boolean b = super.addAll(index, c);
		sortTypes.addAll(index, new ArrayList<SortType>());
		return b;
	}
	
	public boolean add(QueryResult e, SortType sortType, Comparator c) {
		boolean b = super.add(e);
		sortTypes.add(sortType);
		comparators.add(c);
		return b;
	}
	
	@Override
	public int compareTo(StructSortWrapper o) {
		//porownujemy od drugiego pola;
		if(o == null) {
			throw new SBQLException("StructSortWrapper.compareTo(), another struct is null");
		}
		int res = 0;
		for(int i=1; i<this.size(); i++) {
			SortType st = sortTypes.get(i);
			QueryResult qr1 = this.get(i);
			Object o1 = Utils.toSimpleValue(qr1, store);
			QueryResult qr2 = o.get(i);
			Object o2 = Utils.toSimpleValue(qr2, store);
			
//			Comparable c2 = (Comparable)o.get(i);
			//jak nie ma po czym porownywac w drugim obiekcie to zawsze -1
			if(o2 != null) {
				Comparator comp = comparators.get(i);
				if(comp == null) {
					Comparable c1 = (Comparable) o1;
					Comparable c2 = (Comparable) o2;
					res = c1.compareTo(c2);
				} else {
					res = comp.compare(o1, o2);
				}
			} else {
				res = -1;
			}
			if(st == SortType.DESC) {
				res = -res;
			}
			if(res != 0) {
				break;
			}
		}
		return res;
	}
}

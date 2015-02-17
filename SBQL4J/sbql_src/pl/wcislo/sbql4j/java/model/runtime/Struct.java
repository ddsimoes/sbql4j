package pl.wcislo.sbql4j.java.model.runtime;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.OrderedMapIterator;
import org.apache.commons.collections.map.ListOrderedMap;

public class Struct implements Map<String, Object>  {
	private ListOrderedMap innerCol = new ListOrderedMap();
	private boolean xmlSimpleTypeWithAttr;


	public Struct() {
		super();
	}
	public Struct(boolean xmlSimpleTypeWithAttr) {
		super();
		this.xmlSimpleTypeWithAttr = xmlSimpleTypeWithAttr;
	}
	

	public void add(Object obj) {
		put(""+innerCol.size(), obj);
	}
	public void addAll(Collection obj) {
		for(Object c : obj) {
			add(c);
		}
	}

	public List asList() {
		return innerCol.asList();
	}


	public void clear() {
		innerCol.clear();
	}


	public boolean containsKey(Object key) {
		return innerCol.containsKey(key);
	}


	public boolean containsValue(Object value) {
		return innerCol.containsValue(value);
	}


	public Set entrySet() {
		return innerCol.entrySet();
	}


	public boolean equals(Object object) {
		return innerCol.equals(object);
	}


	public Object firstKey() {
		return innerCol.firstKey();
	}


	public Object get(int index) {
		return innerCol.get(index);
	}


	public Object get(Object key) {
		return innerCol.get(key);
	}


	public Object getValue(int index) {
		return innerCol.getValue(index);
	}


	public int hashCode() {
		return innerCol.hashCode();
	}


	public int indexOf(Object key) {
		return innerCol.indexOf(key);
	}


	public boolean isEmpty() {
		return innerCol.isEmpty();
	}


	public List keyList() {
		return innerCol.keyList();
	}


	public Set keySet() {
		return innerCol.keySet();
	}


	public Object lastKey() {
		return innerCol.lastKey();
	}


	public MapIterator mapIterator() {
		return innerCol.mapIterator();
	}


	public Object nextKey(Object key) {
		return innerCol.nextKey(key);
	}


	public OrderedMapIterator orderedMapIterator() {
		return innerCol.orderedMapIterator();
	}


	public Object previousKey(Object key) {
		return innerCol.previousKey(key);
	}


	public Object put(int index, Object key, Object value) {
		return innerCol.put(index, key, value);
	}


	public Object put(String key, Object value) {
		return innerCol.put(key, value);
	}


	public void putAll(Map map) {
		innerCol.putAll(map);
	}


	public Object remove(int index) {
		return innerCol.remove(index);
	}


	public Object remove(Object key) {
		return innerCol.remove(key);
	}


	public Object setValue(int index, Object value) {
		return innerCol.setValue(index, value);
	}


	public int size() {
		return innerCol.size();
	}


	public String toString() {
		return innerCol.toString();
	}


	public List valueList() {
		return innerCol.valueList();
	}


	public Collection values() {
		return innerCol.values();
	}
	
	public boolean isXmlSimpleTypeWithAttr() {
		return xmlSimpleTypeWithAttr;
	}
	public void setXmlSimpleTypeWithAttr(boolean xmlSimpleTypeWithAttr) {
		this.xmlSimpleTypeWithAttr = xmlSimpleTypeWithAttr;
	}

}

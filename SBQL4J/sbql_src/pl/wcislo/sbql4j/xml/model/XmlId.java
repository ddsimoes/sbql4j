package pl.wcislo.sbql4j.xml.model;

import java.util.Collections;
import java.util.List;

import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.model.QueryResult;



public class XmlId implements Comparable<XmlId>, QueryResult {
	private static long index=0;
	public final long val;

	public XmlId(long val) {
		this.val = val;
	}
	
	public static XmlId getNextID() {
		return new XmlId(index++);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (val ^ (val >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final XmlId other = (XmlId) obj;
		if (val != other.val)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "i"+val;
	}

	@Override
	public List<ENVSType> nested() {
		return Collections.emptyList();
	}

	public int compareTo(XmlId o) {
		if(o.val  == val)
			return 0;
		return -1;
	}
}

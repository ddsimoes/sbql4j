package pl.wcislo.sbql4j.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pl.wcislo.sbql4j.lang.types.ENVSType;

public class StructSBQL extends ArrayList<QueryResult> implements SingleResult {
	@Override
	public List<ENVSType> nested() {
		List<ENVSType> res = new ArrayList<ENVSType>();
		for(QueryResult o : this) {
			res.addAll(o.nested());
		}
		res.remove(null);
		return res;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final StructSBQL other = (StructSBQL) obj;
		if(this.size() != other.size()) {
			return false;
		}
		for(int i=0; i<this.size(); i++) {
			QueryResult t1 = this.get(i);
			QueryResult t2 = other.get(i);
			if(t1 == null || t2 == null) {
				return false;
			}
			if(!t1.equals(t2)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("S[");
		for(Iterator<QueryResult> i = this.iterator(); i.hasNext(); ) {
			sb.append(i.next().toString());	
			if(i.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
}

package pl.wcislo.sbql4j.java.model.runtime;

import pl.wcislo.sbql4j.model.QueryResult;

public interface JavaObject extends QueryResult {
	/**
	 * Returns dereferenced value
	 */
	public Object getValue();
	
}

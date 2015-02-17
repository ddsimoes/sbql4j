package pl.wcislo.sbql4j.lang.codegen;

import java.util.HashMap;
import java.util.Map;

public class IdentifierGenerator {
	private Map<String, Integer> prefixCount = new HashMap<String, Integer>();
	
	public String genIdent(String prefix) {
		Integer c = prefixCount.get(prefix);
		if(c == null) {
			c = 0;
		} else {
			c++;
		}
		prefixCount.put(prefix, c);
		return "_"+prefix+c;
	}
}

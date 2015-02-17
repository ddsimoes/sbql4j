package pl.wcislo.sbql4j.util;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

public class NameGenerator {
	private static NameGenerator instance;
	
	public static NameGenerator newInstance() {
		instance = new NameGenerator();
		return instance;
	}
	
	public static NameGenerator getInstance() {
		if(instance == null) {
			instance = newInstance(); 
		}
		return instance;
	}

	private Map<String, Integer> namesMap;
	public NameGenerator() {
		namesMap = new HashedMap();
	}
	
	public String genName(String prefix) {
		Integer prefixCount = namesMap.get(prefix);
		String res;
		if(prefixCount == null) {
			prefixCount = 0;
			res = "_"+prefix;
		} else {
			res = "_"+prefix+prefixCount;
		}
		prefixCount++;
		namesMap.put(prefix, prefixCount);
		return res;
	}
}

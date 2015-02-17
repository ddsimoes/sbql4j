package pl.wcislo.sbql4j.model;

import java.util.List;

import pl.wcislo.sbql4j.lang.types.ENVSType;

public interface QueryResult {
	public List<? extends ENVSType> nested();
}

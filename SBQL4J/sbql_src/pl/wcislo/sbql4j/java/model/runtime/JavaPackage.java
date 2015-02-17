package pl.wcislo.sbql4j.java.model.runtime;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.lang.types.ENVSType;


public class JavaPackage implements ENVSType {
	private String name;
	
	public JavaPackage(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<? extends ENVSType> nested() {
		List res = new ArrayList();
		res.add(this);
		return res;
	}
}
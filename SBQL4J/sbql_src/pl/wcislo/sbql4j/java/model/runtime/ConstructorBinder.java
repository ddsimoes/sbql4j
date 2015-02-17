package pl.wcislo.sbql4j.java.model.runtime;

import java.util.Collections;
import java.util.List;

import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.lang.types.Executable;


public abstract class ConstructorBinder implements ENVSType, Executable {

	protected Class owner;
	
	public ConstructorBinder(Class owner) {
		this.owner = owner;
	}

	public abstract boolean isApplicableTo(Class owner, Object... params);
	
	public abstract Object execute(Object... params) throws Exception;
	
	public Class getOwner() {
		return owner;
	}
	
	@Override
	public List<? extends ENVSType> nested() {
		return Collections.EMPTY_LIST;
	}
}
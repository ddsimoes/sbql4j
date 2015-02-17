package pl.wcislo.sbql4j.java.model.runtime;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.lang.types.ComplexType;
import pl.wcislo.sbql4j.lang.types.ENVSType;

public abstract class JavaComplexObject<T> extends ComplexType<JavaObject> implements JavaObject  {

//	public final String name;
	public final T value;
	protected List<MethodBinder> nestedMethods;
	protected List<Binder> nestedFields;
	protected boolean nestedObjectsInitialized;
	
	public JavaComplexObject(T value) {
//		this.name = name;
		this.value = value;
	}
	
	@Override
	public List<? extends ENVSType> nested() {
		if(!nestedObjectsInitialized) {
			initNestedObjects();
			nestedObjectsInitialized = true;
		}
		List<ENVSType> result = new ArrayList<ENVSType>();
		result.addAll(nestedMethods);
		result.addAll(nestedFields);
		return result;
		
	}
	
	@Override
	public Object getValue() {
		return value;
	}
	
	protected abstract void initNestedObjects();
	public List<Binder> getNestedFields() {
		return nestedFields;
	}
	public List<MethodBinder> getNestedMethods() {
		return nestedMethods;
	}

}
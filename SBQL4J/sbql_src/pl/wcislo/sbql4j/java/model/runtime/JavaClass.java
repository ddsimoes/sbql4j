package pl.wcislo.sbql4j.java.model.runtime;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.lang.types.ComplexType;
import pl.wcislo.sbql4j.lang.types.ENVSType;

public abstract class JavaClass<T> extends ComplexType<JavaObject> implements JavaObject  {
	public final Class<T> value;
	protected List<MethodBinder> nestedMethods;
	protected List<Binder> nestedFields;
	protected List<ConstructorBinder> nestedConstr;
	protected boolean nestedObjectsInitialized;
	
	public JavaClass(Class<T> value) {
		this.value = value;
	}
	
	@Override
	public Object getValue() {
		return value;
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
		result.addAll(nestedConstr);
		return result;
		
	}
	
	protected abstract void initNestedObjects();

}
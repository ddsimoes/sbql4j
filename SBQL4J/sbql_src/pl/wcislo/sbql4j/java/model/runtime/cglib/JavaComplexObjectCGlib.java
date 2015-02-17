package pl.wcislo.sbql4j.java.model.runtime.cglib;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.cglib.reflect.FastMethod;
import pl.wcislo.sbql4j.java.model.runtime.JavaComplexObject;
import pl.wcislo.sbql4j.java.model.runtime.JavaObject;
import pl.wcislo.sbql4j.java.model.runtime.MethodBinder;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.reflect.JavaObjectReflectFactory;
import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.model.collections.CollectionResult;

public class JavaComplexObjectCGlib<T> extends JavaComplexObject<T> {
	
	private final Map<String, Field> valFields;
	private final Map<String, List<FastMethod>> valMethods; 
	
	public JavaComplexObjectCGlib(T value, Map<String, Field> valFields, Map<String,List<FastMethod>> valMethods) {
		super(value);
		this.valFields = valFields;
		this.valMethods = valMethods;
	}
	
	protected void initNestedObjects() {
		this.nestedObjects = new ArrayList<JavaObject>();
		this.nestedFields = new ArrayList<Binder>();
		this.nestedMethods = new ArrayList<MethodBinder>();
		
//		EW/ ja cos takiego napisalem?? niemozliwe
//		JavaObjectReflectFactory rFac = JavaObjectReflectFactory.getInstance();
		JavaObjectFactory rFac = JavaObjectAbstractFactory.getJavaObjectFactory();
		try {
			nestedFields.add(new Binder("self", this));
			for(String fName : valFields.keySet()) {
				Field field = valFields.get(fName);
				field.setAccessible(true);
				Object obj = field.get(value);
				if(obj == null) {
					obj = new ArrayList<ENVSType>();
				}
				CollectionResult fObjects = rFac.createJavaObject(obj);
//				if(fObjects.size() == 1) {
//					nestedFields.add(new Binder(fName, fObjects.iterator().next()));
//				} else {
					nestedFields.add(new Binder(fName, fObjects));
//				}
//				nestedObjects.addAll(fObjects);
//				for(JavaObject jo : fObjects) {
						
//				}
			}
			for(String mName : valMethods.keySet()) {
				List<FastMethod> listMethod = valMethods.get(mName);
				for(FastMethod method : listMethod) {
					MethodBinder mb = new MethodBinderCGlib(method, value);
					nestedMethods.add(mb);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public List<ENVSType> nested() {
//		if(!nestedObjectsInitialized) {
//			initNestedObjects();
//			nestedObjectsInitialized = true;
//		}
//		List<ENVSType> result = new ArrayList<ENVSType>();
//		for (JavaObject o : getNestedObjects()) {
//			result.add(new Binder(o.getName(), o));
//		}
//		for (MethodBinderCGlib methodBinder : nestedMethods) {
//			result.add(methodBinder);
//		}
//		return result;
//	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<J ");
		if(value != null) {
			sb.append(value.getClass().getName());
		}
		sb.append(", (");
		for(Iterator<String> it = valFields.keySet().iterator(); it.hasNext(); ) {
			String fName = it.next();
			String fClass = valFields.get(fName).getType().getName();
			sb.append(fName).append(":").append(fClass);
			if(it.hasNext()) {
				sb.append(", ");
			}
		}
//		for(int i=0; i<valFields.keySet().size(); i++ ) {
//			sb.append(valFields.keySet().get(i)+":"+valFieldTypes.get(i).getName());
//			if(i<valFieldNames.size() - 1) {
//				sb.append(", ");
//			}
//		}
		sb.append(")>");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		final JavaComplexObjectCGlib other = (JavaComplexObjectCGlib) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
}
 
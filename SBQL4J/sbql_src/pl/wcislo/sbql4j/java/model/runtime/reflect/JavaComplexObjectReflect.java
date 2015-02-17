package pl.wcislo.sbql4j.java.model.runtime.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.wcislo.sbql4j.java.model.runtime.JavaComplexObject;
import pl.wcislo.sbql4j.java.model.runtime.JavaObject;
import pl.wcislo.sbql4j.java.model.runtime.MethodBinder;
import pl.wcislo.sbql4j.java.model.runtime.factory.reflect.JavaObjectReflectFactory;
import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.model.collections.Bag;

public class JavaComplexObjectReflect<T> extends JavaComplexObject<T> {
	
	private final Map<String, Field> valFields;
	private final Map<String, List<Method>> valMethods; 
	
	public JavaComplexObjectReflect(T value, Map<String, Field> valFields, Map<String,List<Method>> valMethods) {
		super(value);
		this.valFields = valFields;
		this.valMethods = valMethods;
	}
	
	public void initNestedObjects() {
		this.nestedObjects = new ArrayList<JavaObject>();
		this.nestedFields = new ArrayList<Binder>();
		this.nestedMethods = new ArrayList<MethodBinder>();
		try {
			nestedFields.add(new Binder("self", this));
			for(String fName : valFields.keySet()) {
				Field field = valFields.get(fName);
				field.setAccessible(true);
				Object obj = field.get(value);
				if(obj == null) {
					obj = new Bag();
				}
				Collection fObjects = JavaObjectReflectFactory.getInstance().createJavaObject(obj);
				nestedObjects.addAll(fObjects);
				for(Object jo : fObjects) {
					nestedFields.add(new Binder(fName, (JavaObject)jo));	
				}
			}
			for(String mName : valMethods.keySet()) {
				List<Method> listMethod = valMethods.get(mName);
				for(Method method : listMethod) {
					MethodBinder mb = new MethodBinderReflect(method, value);
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
//		for (MethodBinderReflect methodBinder : nestedMethods) {
//			result.add(methodBinder);
//		}
//		return result;
//	}

//	public String getName() {
//		return name;
//	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<J ");
		if(this.value != null) {
			sb.append(this.value.getClass().getName());
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

//	public ENVSType executeMethod(String mName, Object... params) {
////		System.out.println();
//		return Utils.executeMethod(mName, value, valMethods, params);
//	}

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
		final JavaComplexObjectReflect other = (JavaComplexObjectReflect) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
}
 
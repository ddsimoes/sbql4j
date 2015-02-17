package pl.wcislo.sbql4j.java.model.runtime.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import pl.wcislo.sbql4j.java.model.runtime.ConstructorBinder;
import pl.wcislo.sbql4j.java.model.runtime.JavaClass;
import pl.wcislo.sbql4j.java.model.runtime.JavaObject;
import pl.wcislo.sbql4j.java.model.runtime.MethodBinder;
import pl.wcislo.sbql4j.java.model.runtime.factory.reflect.JavaObjectReflectFactory;
import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.model.collections.CollectionResult;

//public class JavaClassCGlib<T> extends ComplexType<JavaObject> implements JavaObject {
public class JavaClassReflect<T> extends JavaClass<T> {
//	public final String name;
	
//	private List<MethodBinderReflect> nestedMethods;
	
	//statyczne pola
	private final Map<String, Field> valFields;
	//statyczne metody
	private final Map<String, List<Method>> valMethods; 
	
	public JavaClassReflect(Class<T> clazz, Map<String, Field> valFields, Map<String,List<Method>> valMethods) {
		super(clazz);
//		this.clazz = clazz;
//		this.name = name;
		this.valFields = valFields;
		this.valMethods = valMethods;
	}
	
	protected void initNestedObjects() {
//		this.nestedObjects = new ArrayList<JavaObject>();
		this.nestedMethods = new ArrayList<MethodBinder>();
		this.nestedFields = new ArrayList<Binder>();
		this.nestedConstr = new ArrayList<ConstructorBinder>();
		try {
			for(String fName : valFields.keySet()) {
				Field field = valFields.get(fName);
				field.setAccessible(true);
				Object obj = field.get(null);
				if(obj == null) {
					obj = new Bag();
				}
				CollectionResult fObjects = JavaObjectReflectFactory.getInstance().createJavaObject(obj);
//				nestedObjects.addAll(fObjects);
				for(QueryResult jo : fObjects) {
					nestedFields.add(new Binder(fName, jo));	
				}
			}
//			nestedObjects.addAll(JavaObjectReflectFactory.createJavaObject(, obj));
			for(String mName : valMethods.keySet()) {
				List<Method> listMethod = valMethods.get(mName);
				for(Method method : listMethod) {
					nestedMethods.add(new MethodBinderReflect(method, null));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	@Override
//	public String getName() {
//		return value.getName();
//	}

	
}

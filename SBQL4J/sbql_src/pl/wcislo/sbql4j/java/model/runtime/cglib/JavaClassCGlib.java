package pl.wcislo.sbql4j.java.model.runtime.cglib;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.cglib.reflect.FastConstructor;
import net.sf.cglib.reflect.FastMethod;
import pl.wcislo.sbql4j.java.model.runtime.ConstructorBinder;
import pl.wcislo.sbql4j.java.model.runtime.JavaClass;
import pl.wcislo.sbql4j.java.model.runtime.JavaObject;
import pl.wcislo.sbql4j.java.model.runtime.MethodBinder;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.reflect.JavaObjectReflectFactory;
import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.lang.types.ENVSType;

//public class JavaClassCGlib<T> extends ComplexType<JavaObject> implements JavaObject {
public class JavaClassCGlib<T> extends JavaClass<T> {
//	public final String name;
	
//	private List<MethodBinderReflect> nestedMethods;
	
	//statyczne pola
	private final Map<String, Field> valFields;
	//statyczne metody
	private final Map<String, List<FastMethod>> valMethods; 
	
	private final List<FastConstructor> valConstr;
	
	public JavaClassCGlib(Class<T> clazz, Map<String, Field> valFields, Map<String,List<FastMethod>> valMethods, List<FastConstructor> valConstr) {
		super(clazz);
//		this.clazz = clazz;
//		this.name = name;
		this.valFields = valFields;
		this.valMethods = valMethods;
		this.valConstr = valConstr;
	}
	
	protected void initNestedObjects() {
		this.nestedObjects = new ArrayList<JavaObject>();
		this.nestedMethods = new ArrayList<MethodBinder>();
		this.nestedFields = new ArrayList<Binder>();
		this.nestedConstr = new ArrayList<ConstructorBinder>();
		try {
			for(String fName : valFields.keySet()) {
				Field field = valFields.get(fName);
				field.setAccessible(true);
				Object obj = field.get(null);
				if(obj == null) {
					obj = new ArrayList<ENVSType>();
				}
				//TODO ale ozochozzi??
//				Collection fObjects = JavaObjectReflectFactory.getInstance().createJavaObject(obj);
				Collection fObjects = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaObject(obj);
				nestedObjects.addAll(fObjects);
				for(Object jo : fObjects) {
					nestedFields.add(new Binder(fName, (JavaObject)jo));	
				}
			}
//			nestedObjects.addAll(JavaObjectReflectFactory.createJavaObject(, obj));
			for(String mName : valMethods.keySet()) {
				List<FastMethod> listMethod = valMethods.get(mName);
				for(FastMethod method : listMethod) {
					nestedMethods.add(new MethodBinderCGlib(method, null));
				}
			}
			for(FastConstructor fc : valConstr) {
				nestedConstr.add(new ConstructorBinderCGlib(fc, fc.getDeclaringClass()));
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

package pl.wcislo.sbql4j.java.model.runtime.factory.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectFactory;
import pl.wcislo.sbql4j.java.model.runtime.reflect.JavaClassReflect;
import pl.wcislo.sbql4j.java.model.runtime.reflect.JavaComplexObjectReflect;
import pl.wcislo.sbql4j.java.model.runtime.reflect.JavaPackageDEPRECIATED;
import pl.wcislo.sbql4j.java.utils.ReflectUtils;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.tools.javac.code.Type;


public class JavaObjectReflectFactory extends JavaObjectFactory {

	private static JavaObjectReflectFactory instance;
	public static JavaObjectReflectFactory getInstance() {
		if(instance == null) {
			instance = new JavaObjectReflectFactory();
		}
		return instance;
	}
	private JavaObjectReflectFactory() {}
	
	protected final Map<Class, Map<String, Field>> classValFields = new HashMap<Class, Map<String, Field>>();
	protected final Map<Class, Map<String, List<Method>>> classValMethods = new HashMap<Class, Map<String, List<Method>>>();

	protected final Map<Class, Map<String, Field>> staticClassValFields = new HashMap<Class, Map<String, Field>>();
	protected final Map<Class, Map<String, List<Method>>> staticClassValMethods = new HashMap<Class, Map<String, List<Method>>>();
	
	protected final Map<Class, List<Constructor>> constrCache = new HashMap<Class, List<Constructor>>();

	public Map<String, Field> getValFields(Class valClass,
			boolean staticOnly) {
		return getValFields(valClass, null, staticOnly);
	}
	
	public Map<String, Field> getValFields(Class valClass, Type.ClassType javaType, boolean staticOnly) {
		Map<Class, Map<String, Field>> classValFields = staticOnly ? this.staticClassValFields : this.classValFields;
		Map<String, Field> valFields = classValFields.get(valClass);
		if (valFields == null) {
			synchronized (JavaObjectReflectFactory.class) {
				valFields = classValFields.get(valClass);
				if (valFields == null) {
					valFields = ReflectUtils.getFieldNames(valClass, javaType, staticOnly);
					classValFields.put(valClass, valFields);
				}
			}
		}
		return valFields;
	}

	public Map<String, Field> getValFields(Class valClass) {
		return getValFields(valClass, false);
	}

	public Map<String, List<Method>> getValMethods(Class valClass, boolean staticOnly) {
		Map<Class, Map<String, List<Method>>> classValMethods = staticOnly ? this.staticClassValMethods : this.classValMethods;
		Map<String, List<Method>> valMethods = classValMethods.get(valClass);
		if (valMethods == null) {
			synchronized (JavaObjectReflectFactory.class) {
				valMethods = classValMethods.get(valClass);
				if (valMethods == null) {
					valMethods = ReflectUtils.getMethodNames(valClass, staticOnly);
					classValMethods.put(valClass, valMethods);
				}
			}
		}
		return valMethods;
	}
	
	public  List<Constructor> getConstructors(Class clazz) {
		List<Constructor> res = constrCache.get(clazz);
		if(res == null) {
			synchronized (JavaObjectReflectFactory.class) {
				res = constrCache.get(clazz);
				if(res == null) {
					res = Arrays.asList(clazz.getConstructors());
					constrCache.put(clazz, res);
				}
			}
		}
		return res;
	}

	// key - full path
	Map<String, JavaPackageDEPRECIATED> pckMap;

	@Override
	public <T> JavaComplexObjectReflect<T> createJavaComplexObject(T value) {
		if (value == null)
			return null;
		Class<T> valClass = (Class<T>) value.getClass();
		Map<String, Field> valFields = getValFields(valClass);
		if (valFields == null) {
			synchronized (JavaObjectReflectFactory.class) {
				valFields = classValFields.get(valClass);
				if (valFields == null) {
					valFields = ReflectUtils.getFieldNames(valClass, false);
					classValFields.put(valClass, valFields);
				}
			}
		}
		Map<String, List<Method>> valMethods = getValMethods(valClass, false);
		if (valMethods == null) {
			synchronized (JavaObjectReflectFactory.class) {
				valMethods = classValMethods.get(valClass);
				if (valMethods == null) {
					valMethods = ReflectUtils.getMethodNames(valClass, false);
					classValMethods.put(valClass, valMethods);
				}
			}
		}

		// System.out.println(valClass.getName());
		// Utils.getMethodNames(valClass);

		return new JavaComplexObjectReflect<T>(value, valFields,valMethods);
	}

	@Override
	public <T> JavaClassReflect<T> createJavaClassObject(Class<T> clazz) {
		// Class<T> valClass = (Class<T>) value.getClass();
		Map<String, Field> valFields = getValFields(clazz, true);
		Map<String, List<Method>> valMethods = getValMethods(clazz, true);

		return new JavaClassReflect<T>(clazz, valFields, valMethods);
	}


	@Override
	public List<JavaPackageDEPRECIATED> createJavaPackages(List<Package> reflectPckgs) {
		List<JavaPackageDEPRECIATED> res = new ArrayList<JavaPackageDEPRECIATED>();
		pckMap = new HashMap<String, JavaPackageDEPRECIATED>();
		for (Package rp : reflectPckgs) {
			String pName = rp.getName();
			String[] pNameTokens = pName.split("\\.");
			StringBuilder pckPath = new StringBuilder();
			JavaPackageDEPRECIATED parentPck = null;
			for (String token : pNameTokens) {
				pckPath.append(token);
				JavaPackageDEPRECIATED jp = pckMap.get(pckPath.toString());
				if (jp == null) {
					// tworzymy
					jp = new JavaPackageDEPRECIATED(token, parentPck);
					pckMap.put(pckPath.toString(), jp);
				}
				// dodajemy stworzony pakiet do wyniku (tylko pakiety glowne -
				// korzeniowe)
				if (parentPck == null && !res.contains(jp)) {
					res.add(jp);
				}
				pckPath.append(".");
				// dodajemy podpakiet do pakietu nadrzednego
				if (parentPck != null
						&& !parentPck.getSubPackages().contains(jp)) {
					parentPck.getSubPackages().add(jp);
				}
				parentPck = jp;
				jp.nested();

			}
		}
		return res;
	}
	
	public static Method findMethod(String methodName, Object[] params, Map<String, List<Method>> mDef) {
		List<Method> list = mDef.get(methodName);
		if(list == null) {
			return null;
		}
		for(Method m : list) {
			Class[] pTypes = m.getParameterTypes();
			if(pTypes.length != params.length) {
				continue;
			}
			for(int i=0; i<params.length; i++) {
				if(!pTypes[i].isInstance(params[i])) {
					continue;
				}
			}
			return m;
		}
		return null;
	}
	
	
	public static QueryResult executeMethod(String mName, Object value, Map<String, List<Method>> valMethods, Object... params ) {
		Method m = findMethod(mName, params, valMethods);
		if(m == null) {
			String message = value.getClass().getName() + " no such method " + mName;
//			System.err.println(value.getClass().getName() + " no such method " + mName);
			throw new RuntimeException(message);
		}
		Object result = null;
		try {
			m.setAccessible(true);
			result = m.invoke(value, params);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		if(result == null) {
			return new Bag();
		} else {
			Bag b = new Bag();
//			if(pl.wcislo.sbql4j.util.Utils.isSimpleType(result)) {
//				b.add(SimpleTypeFactory.createSimpleType(result));	
//			} else {
				JavaObjectFactory jof = JavaObjectAbstractFactory.getJavaObjectFactory();
				b.addAll(jof.createJavaObject(result));
//			}
			return b;
		}
	}
}

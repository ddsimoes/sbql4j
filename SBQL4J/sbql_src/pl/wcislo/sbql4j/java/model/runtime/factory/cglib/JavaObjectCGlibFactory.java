package pl.wcislo.sbql4j.java.model.runtime.factory.cglib;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cglib.reflect.FastConstructor;
import net.sf.cglib.reflect.FastMethod;
import pl.wcislo.sbql4j.java.model.runtime.cglib.JavaClassCGlib;
import pl.wcislo.sbql4j.java.model.runtime.cglib.JavaComplexObjectCGlib;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.reflect.JavaObjectReflectFactory;
import pl.wcislo.sbql4j.java.model.runtime.reflect.JavaPackageDEPRECIATED;
import pl.wcislo.sbql4j.java.utils.CGlibUtils;
import pl.wcislo.sbql4j.java.utils.ReflectUtils;

public class JavaObjectCGlibFactory extends JavaObjectFactory {

	private static JavaObjectCGlibFactory instance;
	public static JavaObjectCGlibFactory getInstance() {
		if(instance == null) {
			instance = new JavaObjectCGlibFactory();
		}
		return instance; 
	}
	private JavaObjectCGlibFactory() {}
	
	protected final Map<Class, Map<String, Field>> classValFields = new HashMap<Class, Map<String, Field>>();
	protected final Map<Class, Map<String, List<FastMethod>>> classValMethods = new HashMap<Class, Map<String, List<FastMethod>>>();

	protected final Map<Class, Map<String, Field>> staticClassValFields = new HashMap<Class, Map<String, Field>>();
	protected final Map<Class, Map<String, List<FastMethod>>> staticClassValMethods = new HashMap<Class, Map<String, List<FastMethod>>>();

	protected final Map<Class, List<FastConstructor>> classConstr = new HashMap<Class, List<FastConstructor>>();
	
	public Map<String, Field> getValFields(Class valClass,
			boolean staticOnly) {
		Map<Class, Map<String, Field>> classValFields = staticOnly ? this.staticClassValFields : this.classValFields;
		Map<String, Field> valFields = classValFields.get(valClass);
		if (valFields == null) {
			synchronized (JavaObjectReflectFactory.class) {
				valFields = classValFields.get(valClass);
				if (valFields == null) {
					valFields = ReflectUtils.getFieldNames(valClass, staticOnly);
					classValFields.put(valClass, valFields);
				}
			}
		}
		return valFields;
	}

	public Map<String, Field> getValFields(Class valClass) {
		return getValFields(valClass, false);
	}

	public Map<String, List<FastMethod>> getValMethods(Class valClass, boolean staticOnly) {
		Map<Class, Map<String, List<FastMethod>>> classValMethods = staticOnly ? this.staticClassValMethods : this.classValMethods;
		Map<String, List<FastMethod>> valMethods = classValMethods.get(valClass);
		if (valMethods == null) {
			synchronized (JavaObjectCGlibFactory.class) {
				valMethods = classValMethods.get(valClass);
				if (valMethods == null) {
					valMethods = CGlibUtils.getMethodNames(valClass, staticOnly);
					classValMethods.put(valClass, valMethods);
				}
			}
		}
		return valMethods;
	}
	
	public List<FastConstructor> getValConstr(Class clazz) {
		List<FastConstructor> cList = classConstr.get(clazz);
		if(cList == null) {
			synchronized (JavaObjectCGlibFactory.class) {
				cList = classConstr.get(clazz);
				if(cList == null) {
					cList = CGlibUtils.getConstrucotrs(clazz);
				}
			}
		}
		return cList;
	}
	

	// key - full path
	Map<String, JavaPackageDEPRECIATED> pckMap;

	@Override
	public <T> JavaComplexObjectCGlib<T> createJavaComplexObject(T value) {
		if (value == null)
			return null;
		Class<T> valClass = (Class<T>) value.getClass();
		Map<String, Field> valFields = getValFields(valClass);
		if (valFields == null) {
			synchronized (JavaObjectReflectFactory.class) {
				valFields = classValFields.get(valClass);
				if (valFields == null) {
					valFields = ReflectUtils.getFieldNames(valClass, null, false);
					classValFields.put(valClass, valFields);
				}
			}
		}
		Map<String, List<FastMethod>> valMethods = getValMethods(valClass, false);
		if (valMethods == null) {
			synchronized (JavaObjectReflectFactory.class) {
				valMethods = classValMethods.get(valClass);
				if (valMethods == null) {
					valMethods = CGlibUtils.getMethodNames(valClass, false);
					classValMethods.put(valClass, valMethods);
				}
			}
		}

		// System.out.println(valClass.getName());
		// Utils.getMethodNames(valClass);

		return new JavaComplexObjectCGlib<T>(value, valFields,valMethods);
	}

	@Override
	public <T> JavaClassCGlib<T> createJavaClassObject(Class<T> clazz) {
		// Class<T> valClass = (Class<T>) value.getClass();
		Map<String, Field> valFields = getValFields(clazz, true);
		Map<String, List<FastMethod>> valMethods = getValMethods(clazz, true);
		List<FastConstructor> valConstr = getValConstr(clazz);
		return new JavaClassCGlib<T>(clazz, valFields, valMethods, valConstr);
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
//	
//	public static Method findMethod(String methodName, Object[] params, Map<String, List<Method>> mDef) {
//		List<Method> list = mDef.get(methodName);
//		if(list == null) {
//			return null;
//		}
//		for(Method m : list) {
//			Class[] pTypes = m.getParameterTypes();
//			if(pTypes.length != params.length) {
//				continue;
//			}
//			for(int i=0; i<params.length; i++) {
//				if(!pTypes[i].isInstance(params[i])) {
//					continue;
//				}
//			}
//			return m;
//		}
//		return null;
//	}
	
	
//	public static ENVSType executeMethod(String mName, Object value, Map<String, List<Method>> valMethods, Object... params ) {
//		Method m = findMethod(mName, params, valMethods);
//		if(m == null) {
//			String message = value.getClass().getName() + " no such method " + mName;
////			System.err.println(value.getClass().getName() + " no such method " + mName);
//			throw new RuntimeException(message);
//		}
//		Object result = null;
//		try {
//			m.setAccessible(true);
//			result = m.invoke(value, params);
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage(), e);
//		}
//		if(result == null) {
//			return new Bag();
//		} else {
//			Bag b = new Bag();
//			if(pl.wcislo.sbql4j.util.Utils.isSimpleType(result)) {
//				b.add(SimpleTypeFactory.createSimpleType(result));	
//			} else {
//				JavaObjectFactory jof = JavaObjectAbstractFactory.getJavaObjectFactory();
//				b.addAll(jof.createJavaObject(m.getName()+"_result", result));
//			}
//			return b;
//		}
//	}
}

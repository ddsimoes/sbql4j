package pl.wcislo.sbql4j.java.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastConstructor;
import net.sf.cglib.reflect.FastMethod;

public class CGlibUtils {
	private static final Log log = LogFactory.getLog(CGlibUtils.class);
	
	public static <T> Map<String, List<FastMethod>> getMethodNames(Class<T> clazz, final boolean staticOnly) {
		final Map<String, List<FastMethod>> result = new HashMap<String, List<FastMethod>>();
		try {
			List<Class<? super T>> sup = ReflectUtils.getSuperclasses(clazz);
			List<FastClass> supCGlib = new ArrayList<FastClass>(sup.size());
//			for(Class c : sup) {
//				supCGlib.add(FastClass.create(c));
//			}
			
			for (final Class<? super T> c : sup) {
				FastClass classCGlib = FastClass.create(c);
				for (Method m : c.getDeclaredMethods()) {
					if(staticOnly && !((m.getModifiers() & Modifier.STATIC) > 0)) {
						continue;
					}
					List<FastMethod> list = result.get(m.getName());
					if (list == null) {
						list = new ArrayList<FastMethod>();
						result.put(m.getName(), list);
					}

					boolean match = false;
					for (FastMethod other : list) {
						if(staticOnly && !((other.getModifiers() & Modifier.STATIC) > 0)) {
							continue;
						}
						if (m.getName().equals(other.getName())) {
							if (!m.getReturnType()
									.equals(other.getReturnType()))
								continue;

							Class[] params1 = m.getParameterTypes();
							Class[] params2 = other.getParameterTypes();
							if (params1.length == params2.length) {
								boolean paramsMatch = true;
								for (int i = 0; i < params1.length
										&& paramsMatch; i++) {
									if (params1[i] != params2[i]) {
										paramsMatch = false;
									}
								}
								if (paramsMatch) {
									match = true;
									break;
								}

							}
						}
					}
					
					if (!match && Modifier.isPublic(m.getModifiers())) {
						try {
							FastMethod fm = classCGlib.getMethod(m);
							list.add(fm);
						} catch(RuntimeException e) {
							log.warn("something bad happend when accesing method "+m.getName()+" in class "+clazz.getCanonicalName()+": "+e.getMessage());
						}
					}

//					StringBuilder sb = new StringBuilder();
//					sb.append(m.getName());
//					for (Class<?> cParam : m.getParameterTypes()) {
//						sb.append(" " + cParam.getName());
//					}
//					System.out.println(sb.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static <T> Map<String, List<FastMethod>> getMethodNames(Class<T> clazz) {
		return getMethodNames(clazz, false);
	}
	
	public static <T> List<FastConstructor> getConstrucotrs(Class<T> clazz) {
		try {
			List<FastConstructor> res = new ArrayList<FastConstructor>();
			FastClass fc = FastClass.create(clazz);
			Constructor[] cTab = clazz.getConstructors();
			for(Constructor c : cTab) {
				res.add(fc.getConstructor(c));
			}
			return res;
		} catch (RuntimeException e) {
			throw e;
		}
		
	}
}

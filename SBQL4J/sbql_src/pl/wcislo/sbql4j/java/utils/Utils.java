package pl.wcislo.sbql4j.java.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.wcislo.sbql4j.java.test.model.Person;

public class Utils {
	private static final Log log = LogFactory.getLog(Utils.class);
	
//	public static <T> Map<String, Field> getFieldNames(Class<T> clazz, final boolean staticOnly) {
//
//		final Map<String, Field> result = new HashMap<String, Field>();
//		if(clazz.isArray()) {
//			return result;
//		}
//		List<Class<? super T>> sup = getSuperclasses(clazz);
//		try {
////			List<Class<? super T>> sup = getSuperclasses(clazz);
//			for (final Class<? super T> c : sup) {
////				System.out.println(c.getName());
//				ClassReader cr = new ClassReader(c.getName());
//				EmptyVisitor fieldReader = new EmptyVisitor() {
//					@Override
//					public FieldVisitor visitField(int access, String name,
//							String desc, String signature, Object value) {
//						if (!result.containsKey(name)) {
//							try {
//								if(!staticOnly || (staticOnly && (access & Opcodes.ACC_STATIC) > 0)) {
//									result.put(name, c.getDeclaredField(name));
//								}
//							} catch (SecurityException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} catch (NoSuchFieldException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//						return null;
//					}
//				};
//				cr.accept(fieldReader, 0);
//			}
//		} catch (Exception e) {
//			System.err.println(clazz+" "+sup);
//			e.printStackTrace();
//		}
//
//		return result;
//	}
//
//	public static <T> Map<String, List<Method>> getMethodNames(Class<T> clazz) {
//		return getMethodNames(clazz, false);
//	}
//	
//	public static <T> Map<String, List<Method>> getMethodNames(Class<T> clazz, final boolean staticOnly) {
//		final Map<String, List<Method>> result = new HashMap<String, List<Method>>();
//		try {
//			List<Class<? super T>> sup = getSuperclasses(clazz);
//			for (final Class<? super T> c : sup) {
//				for (Method m : c.getDeclaredMethods()) {
//					if(staticOnly && !((m.getModifiers() & Modifier.STATIC) > 0)) {
//						continue;
//					}
//					List<Method> list = result.get(m.getName());
//					if (list == null) {
//						list = new ArrayList<Method>();
//						result.put(m.getName(), list);
//					}
//
//					boolean match = false;
//					for (Method other : list) {
//						if(staticOnly && !((other.getModifiers() & Modifier.STATIC) > 0)) {
//							continue;
//						}
//						if (m.getName().equals(other.getName())) {
//							if (!m.getReturnType()
//									.equals(other.getReturnType()))
//								continue;
//
//							Class[] params1 = m.getParameterTypes();
//							Class[] params2 = other.getParameterTypes();
//							if (params1.length == params2.length) {
//								boolean paramsMatch = true;
//								for (int i = 0; i < params1.length
//										&& paramsMatch; i++) {
//									if (params1[i] != params2[i]) {
//										paramsMatch = false;
//									}
//								}
//								if (paramsMatch) {
//									match = true;
//									break;
//								}
//
//							}
//						}
//					}
//					if (!match) {
//						list.add(m);
//					}
//
////					StringBuilder sb = new StringBuilder();
////					sb.append(m.getName());
////					for (Class<?> cParam : m.getParameterTypes()) {
////						sb.append(" " + cParam.getName());
////					}
////					System.out.println(sb.toString());
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return result;
//	}
//
//	public static <T> List<Class<? super T>> getSuperclasses(Class<T> clazz) {
//		List<Class<? super T>> result = new ArrayList<Class<? super T>>();
////		result.add(clazz);
//		Class<? super T> ancestor = clazz;
////		Class<? super T> ancestor = clazz.getSuperclass();
//		while (ancestor != Object.class && ancestor != null) {
//			result.add(ancestor);
//			ancestor = ancestor.getSuperclass();
//		}
//		// Collections.reverse(result);
//		return result;
//	}
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
//	
//	
//	public static ENVSType executeMethod(String mName, Object value, Map<String, List<Method>> valMethods, Object... params ) {
//		Method m = Utils.findMethod(mName, params, valMethods);
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
	
	/**
	 * Zamienia kanoniczna nazwe typu (np. java.lang.String) 
	 * na nazwe binarna (np. java/lang/String)
	 * 
	 * @param cName
	 * @return Nazwa binarna
	 */
	public static String canonicalToBinaryName(String cName) {
		String bName = cName.replace(".", "/");
		return bName;
	}
	
	public static Class<?>[] getParamTypes(Object... params) {
		if(params == null) {
			params = new Object[0];
		}
		Class<?>[] t = new Class<?>[params.length];
		for(int i=0; i<t.length; i++) {
			t[i] = params[i].getClass();
		}
		return t;
	}
	
	public static Class<? extends Number> getBinaryOperatorResult(Class<? extends Number> leftType, 
			Class<? extends Number> rightType) {
		Class<? extends Number> returnType = null;
		if(leftType == Double.class || rightType == Double.class) {
			returnType = Double.class;
		} else if(leftType == Long.class || rightType == Long.class) {
			returnType = Long.class;
		} else if(leftType == Float.class || rightType == Float.class) {
			returnType = Float.class;
		} else if(leftType == Integer.class || rightType == Integer.class) {
			returnType = Integer.class;
		} else if(leftType == Short.class || rightType == Short.class) {
			returnType = Short.class;
		} else if(leftType == Byte.class || rightType == Byte.class) {
			returnType = Byte.class;
		}
		return returnType;
	}
	
	public static Class getSharedAncestor(Class c1, Class c2) {
		Class res = null;
		while(c1 != null && c2 != null) {
			if(c1.isAssignableFrom(c2)) {
				return c1;
			} else if(c2.isAssignableFrom(c1)) {
				return c2;
			}
			c1 = c1.getSuperclass();
			c2 = c2.getSuperclass();
		}
		return res;
	}
	
	public static void main(String[] args) {
		Class c1 = List.class;
		Class c2 = ArrayList.class;
		
		Class c3 = getSharedAncestor(c1, c2);
		log.info(c3);
	}
}

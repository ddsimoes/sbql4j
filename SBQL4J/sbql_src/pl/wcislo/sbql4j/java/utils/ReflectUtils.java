package pl.wcislo.sbql4j.java.utils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.EmptyVisitor;

import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectFactory;
import pl.wcislo.sbql4j.java.preprocessor.ant.SBQL4JAntTask;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import sbql4jx.tools.JavaFileManager;
import sbql4jx.tools.JavaFileObject;
import sbql4jx.tools.StandardLocation;
import sbql4jx.tools.JavaFileObject.Kind;

public class ReflectUtils {
	private static final Log log = LogFactory.getLog(ReflectUtils.class);
	
	
	public static <T> Map<String, Field> getFieldNames(Class<T> clazz, final boolean staticOnly) {
		return getFieldNames(clazz, null, staticOnly);
	}
	
	public static <T> Map<String, Field> getFieldNames(Class<T> clazz, Type.ClassType javaType, final boolean staticOnly) {

		final Map<String, Field> result = new HashMap<String, Field>();
		if(clazz.isArray()) {
			return result;
		}
		List<Class<? super T>> sup = getSuperclasses(clazz);
		try {
//			List<Class<? super T>> sup = getSuperclasses(clazz);
			for (Class<? super T> cIter : sup) {
//				System.out.println(c.getName());\
				//XXX DEBUG
//	    		ClassLoader cl = Thread.currentThread().getContextClassLoader();
//	    		InputStream is = cl.getResourceAsStream(c.getName().replace('.', '/')+ ".class");
//				JavaNameResolver jns = JavaNameResolver.getInstance();
//				if(jns != null) {
//					String cName = c.getName();
//					Symbol s = jns.findJavacType(cName);
//					log.debug("found javac type: "+s);
//				}
				if(cIter.isPrimitive()) {
					cIter = getWrapperClassForPrimivite(cIter);
				}
				final Class c = cIter;
				ClassReader cr;
				try {
					cr = new ClassReader(c.getName());
				} catch(IOException e) {
					log.debug("Cannot find class "+c.getName()+" on system classpath, attempt to get java file manager");
					JavaFileManager fManager = SBQL4JAntTask.getFileManager();
					if(fManager != null) {
						JavaFileObject jfo = fManager.getJavaFileForInput(StandardLocation.CLASS_PATH, c.getName(), Kind.CLASS);
						log.debug("Got JavaFileObject for class "+c.getName()+": "+jfo+", attempt to open class data stream");
						cr = new ClassReader(jfo.openInputStream());
					} else {
						log.error("JavaFileManager not found, cannot open class file: "+c.getName());
						throw e;
					}
				}
//				if(javaType != null) {
//					InputStream classStream = ((ClassSymbol)javaType.tsym).classfile.openInputStream();
////					InputStream classStream = new FileInputStream(new File(scrUri));
//					cr = new ClassReader(classStream);
//				} else {
//					cr = new ClassReader(c.getName());
//				}
				
				EmptyVisitor fieldReader = new EmptyVisitor() {
					@Override
					public FieldVisitor visitField(int access, String name,
							String desc, String signature, Object value) {
						if (!result.containsKey(name)) {
							try {
								if(!staticOnly || (staticOnly && (access & Opcodes.ACC_STATIC) > 0)) {
									result.put(name, c.getDeclaredField(name));
								}
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchFieldException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						return null;
					}
				};
				cr.accept(fieldReader, 0);
			}
		} catch (Exception e) {
			System.err.println(clazz+" "+sup);
			e.printStackTrace();
		}

		return result;
	}

	public static <T> Map<String, List<Method>> getMethodNames(Class<T> clazz, final boolean staticOnly) {
		final Map<String, List<Method>> result = new HashMap<String, List<Method>>();
		try {
			List<Class<? super T>> sup = getSuperclasses(clazz);
			for (final Class<? super T> c : sup) {
				for (Method m : c.getDeclaredMethods()) {
					if(staticOnly && !((m.getModifiers() & Modifier.STATIC) > 0)) {
						continue;
					}
					List<Method> list = result.get(m.getName());
					if (list == null) {
						list = new ArrayList<Method>();
						result.put(m.getName(), list);
					}

					boolean match = false;
					for (Method other : list) {
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
					if (!match) {
						list.add(m);
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
	
	public static  List<Constructor> getConstructors(Class clazz) {
		List<Constructor> res = new ArrayList<Constructor>();
		List<Constructor> list = Arrays.asList(clazz.getConstructors());
		res.addAll(list);
		return res;
	}

	public static <T> Map<String, List<Method>> getMethodNames(Class<T> clazz) {
		return getMethodNames(clazz, false);
	}
	
	public static <T> List<Class<? super T>> getSuperclasses(Class<T> clazz) {
		List<Class<? super T>> result = new ArrayList<Class<? super T>>();
//		result.add(clazz);
		Class<? super T> ancestor = clazz;
//		Class<? super T> ancestor = clazz.getSuperclass();
		while (ancestor != Object.class && ancestor != null) {
			result.add(ancestor);
			ancestor = ancestor.getSuperclass();
		}
		// Collections.reverse(result);
		return result;
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
	//		System.err.println(value.getClass().getName() + " no such method " + mName);
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
	
	public static Class getWrapperClassForPrimivite(Class clazz) {
		if(Boolean.TYPE.equals(clazz)) return Boolean.class;
		if(Character.TYPE.equals(clazz)) return Character.class;
		if(Byte.TYPE.equals(clazz)) return Byte.class;
		if(Short.TYPE.equals(clazz)) return Short.class;
		if(Integer.TYPE.equals(clazz)) return Integer.class;
		if(Long.TYPE.equals(clazz)) return Long.class;
		if(Float.TYPE.equals(clazz)) return Float.class;
		if(Double.TYPE.equals(clazz)) return Double.class;
		return null;
	}
}

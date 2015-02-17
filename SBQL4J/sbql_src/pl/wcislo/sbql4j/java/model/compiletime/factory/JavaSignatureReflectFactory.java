package pl.wcislo.sbql4j.java.model.compiletime.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.wcislo.sbql4j.exception.NotSupportedException;
import pl.wcislo.sbql4j.java.model.compiletime.ClassSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ConstructorSignature;
import pl.wcislo.sbql4j.java.model.compiletime.MethodSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.StructSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.runtime.factory.reflect.JavaObjectReflectFactory;
import pl.wcislo.sbql4j.tools.javac.code.Attribute;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.Attribute.Compound;

@Deprecated
public class JavaSignatureReflectFactory extends JavaSignatureFactory {
//	private static final Log log = LogFactory.getLog(JavaSignatureReflectFactory.class);
	
	public static final ValueSignature VOID_VAL_SIG = null; //new ValueSignature(Void.class, SCollectionType.NO_COLLECTION, null, null);
	public static final ClassSignature VOID_CLASS_SIG = null; //new ClassSignature(Void.class, null, null, null);
	
	private Map<Class, ValueSignature> cacheValueSig = new HashMap<Class, ValueSignature>();
	private Map<Class, ClassSignature> cacheClassSig = new HashMap<Class, ClassSignature>();
	
	private Map<Method, MethodSignature> cacheMethods = new HashMap<Method, MethodSignature>();
	private Map<Constructor, ConstructorSignature> cacheConstructors = new HashMap<Constructor, ConstructorSignature>();
	
	private JavaObjectReflectFactory fac = JavaObjectReflectFactory.getInstance();
	
	private JavaSignatureReflectFactory() {
		super();
	}
	
	private static JavaSignatureReflectFactory instance;
	public static JavaSignatureReflectFactory getInstance() {
		if(instance == null) {
			instance = new JavaSignatureReflectFactory();
		}
		return instance;
	}

	@Override
	public ValueSignature createValueSignature(Type javaType,
			boolean staticMembersOnly) throws NotSupportedException {
		return (ValueSignature) createSignature(javaType, staticMembersOnly, new ArrayList<Attribute.Compound>());
	}
	
//	public ValueSignature createValueSignature(Type javaType, boolean staticMembersOnly) throws NotSupportedException {
	public Signature createSignature(Type javaCompilerType, boolean staticMembersOnly, 
			List<Compound> annotationList) throws NotSupportedException {
		java.lang.reflect.Type javaType = compilerTypeToClass(javaCompilerType);
		if(javaType == null) return null;
		if(javaType == Void.TYPE) return VOID_VAL_SIG;
		Class javaClass = null;
		ParameterizedType paramType = null;
		if(javaType instanceof Class) {
			javaClass = (Class)javaType;
		} else if(javaType instanceof ParameterizedType) {
			paramType = (ParameterizedType) javaType;
			javaClass = (Class)paramType.getRawType();
		} else if(javaType instanceof GenericArrayType) {
			GenericArrayType gt = (GenericArrayType) javaType;
//			Type t = gt.getGenericComponentType();
			java.lang.reflect.Type t = Object.class;
			ValueSignature nSig = createValueSignature(javaCompilerType, staticMembersOnly);
			nSig.setColType(SCollectionType.SEQUENCE);
			return nSig;
		}
		
		javaClass = checkPrimitiveClass(javaClass);
		SCollectionType cType = SCollectionType.NO_COLLECTION;
		if(List.class.isAssignableFrom(javaClass)) {
			cType = SCollectionType.SEQUENCE;
		} else if(Collection.class.isAssignableFrom(javaClass)) {
			cType = SCollectionType.BAG;
		}
		if(javaClass.isArray()) {
			if(cType != SCollectionType.NO_COLLECTION) {
//				throw new NotSupportedException("Type: "+paramType+" is nested collection, which is not supported", paramType);
			}
			cType = SCollectionType.SEQUENCE;
		}
		if(cType != SCollectionType.NO_COLLECTION) {
			Class newJavaType = Object.class;
			if(!javaClass.isArray()) {
				if(paramType != null) {
					java.lang.reflect.Type t0 = paramType.getActualTypeArguments()[0];
//					Type compParamType = javaCompilerType.typarams_field.get(0);
//					ValueSignature vs = createValueSignature(compParamType, staticMembersOnly);
					ValueSignature vs = null;
					if(vs.isCollectionResult()) {
						//nie wspieramy zagniezdzonych kolecji
//						throw new NotSupportedException("Type: "+paramType+" is nested collection, which is not supported", paramType);
					}
					vs.setColType(cType);
					return vs;
				} else if(javaClass.getTypeParameters().length > 0){
					//gdy mamy klase, a nie typ kompilacji, trzeba wyroznic typ kolekcji
					TypeVariable t = javaClass.getTypeParameters()[0];
//					log.debug("type variable for class "+javaClass+" is "+t);
				}
			} else {
				Class arrayClass = javaClass.getComponentType();
//				ValueSignature vs = createValueSignature(arrayClass, staticMembersOnly);
				ValueSignature vs = null;
				if(vs.isCollectionResult()) {
					//nie wspieramy zagniezdzonych kolecji
//					throw new NotSupportedException("Type: "+paramType+" is nested collection, which is not supported", paramType);
				}
				vs.setColType(cType);
				return vs;
			}
			javaType = newJavaType;
			javaClass = newJavaType;
//			TypeVariable[] gen = javaType.getTypeParameters();
//			if(gen.length > 0) {
//				TypeVariable g0 = gen[0];
//				Type[] bounds = g0.getBounds();
//				System.out.println();
//				if(g0 instanceof Class) {
//					ValueSignature vs = createValueSignature((Class) g0, staticMembersOnly);
//					vs.sColType = cType;
//					return vs;
//				}
			
		}
		ValueSignature res = cacheValueSig.get(javaType);
		if(res == null) {
			Map<String, Field> fields = fac.getValFields(javaClass, staticMembersOnly);
			Map<String, List<Method>> methods = fac.getValMethods(javaClass, staticMembersOnly);
//			res = new ValueSignature(javaClass, cType, fields, methods);
//			cacheValueSig.put(javaType, res);
		} 
		return res;
	}
	
//	public ClassSignature createClassSignature(Class javaClass, pl.wcislo.sbql4j.tools.javac.code.Type.ClassType javaType) {
	public ClassSignature createClassSignature(Type.ClassType javaType) {
		Class javaClass = compilerTypeToClass(javaType);
		if(javaClass == null) return null;
		if(javaClass == Void.TYPE) return VOID_CLASS_SIG;
		javaClass = checkPrimitiveClass(javaClass);
		ClassSignature res = cacheClassSig.get(javaClass);
		if(res == null) {
			Map<String, Field> fields = fac.getValFields(javaClass, javaType, true);
			Map<String, List<Method>> methods = fac.getValMethods(javaClass, true);
			List<Constructor> constr = fac.getConstructors(javaClass);
//			res = new ClassSignature(javaClass, fields, methods, constr);
			cacheClassSig.put(javaClass, res);
		}
		return res;
	}
	
//	public JavaMethodSignature createJavaMethodSignature(Method m) {
	@Deprecated
	public MethodSignature createJavaMethodSignature(Symbol.MethodSymbol method) {
		Method m = null;
		if(m == null) return null;
//		javaClass = checkPrimitiveClass(javaClass);
		MethodSignature res = cacheMethods.get(m);
		if(res == null) {
			java.lang.reflect.Type rTypeGen = m.getGenericReturnType();
			Class rType = m.getReturnType();
			
//			ValueSignature resTypeSig = createJavaSignature( rTypeGen );
			StructSignature paramsSig = new StructSignature();
			if(m.getParameterTypes() != null) {
				TypeVariable<Method>[] params = m.getTypeParameters();
				
				
				for(Class c : m.getParameterTypes()) {
//					paramsSig.addField(createJavaSignature(c));
				}
			}
//			res = new JavaMethodSignature(m, m.getDeclaringClass(), resTypeSig, paramsSig);
			cacheMethods.put(m, res);
		}
		return res;
	}
	
	@Deprecated
//	public JavaConstructorSignature createJavaConstructorSignature(Constructor constr) {
	public ConstructorSignature createJavaConstructorSignature(Symbol.MethodSymbol constr2) {
		Constructor constr = null;
		if(constr == null) return null;
//		javaClass = checkPrimitiveClass(javaClass);
		ConstructorSignature res = cacheConstructors.get(constr);
		if(res == null) {
			java.lang.reflect.Type rTypeGen = constr.getDeclaringClass();
//			Class rType = m.getReturnType();
			
//			ValueSignature resTypeSig = createJavaSignature( rTypeGen );
			StructSignature paramsSig = new StructSignature();
			if(constr.getParameterTypes() != null) {
//				TypeVariable<Constructor>[] params = constr.getTypeParameters();
				
				
				for(Class c : constr.getParameterTypes()) {
//					paramsSig.addField(createJavaSignature(c));
				}
			}
//			res = new JavaConstructorSignature(constr, constr.getDeclaringClass(), resTypeSig, paramsSig);
			cacheConstructors.put(constr, res);
		}
		return res;
	}
	
	@Deprecated
	public ValueSignature createJavaSignature(Type javaType) throws NotSupportedException {
//		return createValueSignature(javaType, false);
		return null;
	}
	
	public static Class checkPrimitiveClass(Class c) {
		if(c == null) return c;
		if(c.isPrimitive()) {
			if(c == Boolean.TYPE) {
				return Boolean.class;
			} else if(c == Character.TYPE) { 
				return Character.class;
			} else if(c == Byte.TYPE) {
				return Byte.class;
			} else if(c == Short.TYPE) {
				return Short.class;
			} else if(c == Integer.TYPE) {
				return Integer.class;
			} else if(c == Long.TYPE) {
				return Long.class;
			} else if(c == Float.TYPE) {
				return Float.class;
			} else if(c == Double.TYPE) {
				return Double.class;
			}
			return null;
		} else {
			return c;
		}
	}
}

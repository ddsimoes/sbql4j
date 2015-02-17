package pl.wcislo.sbql4j.java.model.compiletime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.code.Symtab;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.TypeTags;
import pl.wcislo.sbql4j.tools.javac.code.Types;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.ClassSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Type.ClassType;
import pl.wcislo.sbql4j.tools.javac.comp.JavaNameResolver;
import pl.wcislo.sbql4j.tools.javac.comp.Resolve;
import pl.wcislo.sbql4j.tools.javac.util.Name;
import pl.wcislo.sbql4j.tools.javac.util.Name.Table;

public class ClassTypes {
	private static final Log log = LogFactory.getLog(ClassTypes.class);
	
	public final Symtab symtab;
	public final Name.Table names;
	public final Types types;
	private JavaNameResolver jnr;
	
	private Map<String, Type> primitiveTypes;
	private Map<String, Type> classCache;
	
	private static ClassTypes instance;
	
	public final Type voidType;
	public final ClassType listType;
	public final ClassType collectionType;
	public final ClassType objectClassType;
	
	
	private ClassTypes(Symtab symtab, Table names, JavaNameResolver jnr, Types types) {
		super();
		this.symtab = symtab;
		this.names = names;
		this.jnr = jnr;
		this.types = types;
		initPrimitiveTypes();
		this.classCache = new HashMap<String, Type>();
		this.voidType = symtab.voidType;
		this.listType = (ClassType)getCompilerType(List.class);
		this.collectionType = (ClassType)getCompilerType(Collection.class);
		this.objectClassType = (ClassType)symtab.objectType;
		
	}

	private void initPrimitiveTypes() {
		primitiveTypes = new HashMap<String, Type>();
		primitiveTypes.put(Byte.TYPE.toString(), symtab.byteType);
		primitiveTypes.put(Character.TYPE.toString(), symtab.charType);
		primitiveTypes.put(Short.TYPE.toString(), symtab.shortType);
		primitiveTypes.put(Integer.TYPE.toString(), symtab.intType);
		primitiveTypes.put(Long.TYPE.toString(), symtab.longType);
		primitiveTypes.put(Float.TYPE.toString(), symtab.floatType);
		primitiveTypes.put(Double.TYPE.toString(), symtab.doubleType);
		primitiveTypes.put(Boolean.TYPE.toString(), symtab.booleanType);
	}

	public static ClassTypes initInstance(Symtab symtab, Name.Table names, JavaNameResolver jnr, Types types) {
		instance = new ClassTypes(symtab, names, jnr, types);
		return instance;
	}
	public static ClassTypes getInstance() {
//		if(instance == null) {
//			initInstance(Symtab.instance(null), 
//					Table.instance(null), 
//					new JavaNameResolver(Resolve.instance(null),
//							null, 0, Table.instance(null)), Types.instance(null));
//		}
		return instance;
	}
	
	public Type getCompilerType(java.lang.reflect.Type reflectType) {
		Type res;
		res = primitiveTypes.get(reflectType.toString());
		if(res != null) {
			return res;
		}
		String name;
		if(reflectType instanceof Class) {
			name = ((Class)reflectType).getName();
			Type t = classCache.get(name);
			if(t == null) {
				Symbol s = jnr.findJavacType(name);
				if(s != null) {
					t = s.type;
					classCache.put(name, t);
				} else {
					return null;
				}
			}
			return t;
		} else {
			return null;
		}
	}
	
	//trzeba uwazac, bo to nie zawsze dziala - w szegolnosci gdy danej klasy nie ma na klaspaczu preprecesora
	public static java.lang.reflect.Type getReflectType(Type compilerType) {
		if(compilerType.isPrimitive()) {
			if(compilerType.tag == TypeTags.BYTE) return Byte.class;
			if(compilerType.tag == TypeTags.CHAR) return Character.class;
			if(compilerType.tag == TypeTags.SHORT) return Short.class;
			if(compilerType.tag == TypeTags.INT) return Integer.class;
			if(compilerType.tag == TypeTags.LONG) return Long.class;
			if(compilerType.tag == TypeTags.FLOAT) return Float.class;
			if(compilerType.tag == TypeTags.DOUBLE) return Double.class;
			if(compilerType.tag == TypeTags.BOOLEAN) return Boolean.class;
		} else {
			try {
				return Class.forName(compilerType.tsym.toString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public ClassType checkPrimitiveType(final Type t) {
		if(t instanceof ClassType) {
			return (ClassType)t;
		} else if(t.isPrimitive()){
			if(t == symtab.booleanType) {
				return (ClassType)getCompilerType(Boolean.class);
			} else if(t == symtab.charType) { 
				return (ClassType)getCompilerType(Character.class);
			} else if(t == symtab.byteType) {
				return (ClassType)getCompilerType(Byte.class);
			} else if(t == symtab.shortType) {
				return (ClassType)getCompilerType(Short.class);
			} else if(t == symtab.intType) {
				return (ClassType)getCompilerType(Integer.class);
			} else if(t == symtab.longType) {
				return (ClassType)getCompilerType(Long.class);
			} else if(t == symtab.floatType) {
				return (ClassType)getCompilerType(Float.class);
			} else if(t == symtab.doubleType) {
				return (ClassType)getCompilerType(Double.class);
			} else {
//				log.warn("unsupproted primitive type: "+t);
				return null;
			}
		} else {
//			log.warn("unsupproted type: "+t);
			return null;
		}
	}
	
//	public static boolean isImplementingInterface(ClassType ct, ClassType inter) {
//		if(ct == null || inter == null) {
//			return false;
//		}
//		for(Type i : ct.interfaces_field) {
//			if(i.toString().equals(inter.toString())) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	public boolean isSubClass(Type subClass, Type superClass) {
		if(subClass == null || superClass == null) {
			return false;
		}
		if( !(subClass instanceof ClassType) || !(superClass instanceof ClassType)) {
			return false;
		}
		return isSubClass((ClassType)subClass, (ClassType)superClass);
	}
	
	public boolean isSubClass(ClassType subClass, ClassType superClass) {
		if(subClass == null || superClass == null) {
			return false;
		}
		boolean result = subClass.tsym.isSubClass(superClass.tsym, types);
		return result;
		
//		ClassType upClass = subClass;
//		while(upClass != null) {
//			if(upClass.toString().equals(superClass.toString())) {
//				return true;
//			}
//			upClass = upClass.tsym.isInheritedIn(clazz, types)
//		}
//		return false;
//		for(Type i : subClass.getEnclosingType()) {
//			if(i.toString().equals(superClass.toString())) {
//				return true;
//			}
//		}
//		return false;
	}
	
	public Type getSharedAncestor(Type t1, Type t2) {
		t1 = checkPrimitiveType(t1);
		t2 = checkPrimitiveType(t2);
		if(!(t1 instanceof ClassType) || !(t2 instanceof ClassType)) {
			log.warn("invalid one of types: t1="+t1+" t2="+t2);
			return null;
		}
		ClassType ct1 = (ClassType)t1;
		ClassType ct2 = (ClassType)t2;
		ClassType res = null;
		while(ct1 != null && ct2 != null) {
			if(ct1.tsym.isSubClass(ct2.tsym, types)) {
				return ct2;
			} else if(ct2.tsym.isSubClass(ct1.tsym, types)) {
				return ct1;
			}
			ct1 = (ClassType) ((ClassSymbol)ct1.tsym).getSuperclass();
			ct2 = (ClassType) ((ClassSymbol)ct2.tsym).getSuperclass();
		}
		return res;
	}
	
	
	public void debug() {
		ClassType t1 = (ClassType) getCompilerType(ArrayList.class);
		ClassType t2 = (ClassType) getCompilerType(Vector.class);
		Type shared = getSharedAncestor(t1, t2);
		log.debug("shared: "+shared);
		
		
//		boolean b = t1.interfaces_field.contains(t2);
//		boolean b = isSubClass(t1, t2);
//		boolean b2 = isSubClass(t2, t1);
//		log.debug(b+" "+b2);
		
//		log.debug("ClassTypes.debug() enter");
//		java.lang.reflect.Type[] testTypes = new java.lang.reflect.Type[] { Boolean.TYPE, Boolean.class, Integer.TYPE, Short.TYPE, Object.class, Customer.class, Customer.InnterTestClass.class };
//		for(java.lang.reflect.Type t : testTypes) {
//			Type t1 = getCompilerType(t);	
//			log.debug("compiler type for reflect type: "+t+"="+t1);
//		}
//		log.debug("ClassTypes.debug() end");
	}
}

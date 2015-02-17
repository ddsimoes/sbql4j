package pl.wcislo.sbql4j.java.model.compiletime.factory;

import java.util.List;

import pl.wcislo.sbql4j.exception.NotSupportedException;
import pl.wcislo.sbql4j.java.model.compiletime.ClassSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ConstructorSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Db4oConnectionSignature;
import pl.wcislo.sbql4j.java.model.compiletime.MethodSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.TypeTags;
import pl.wcislo.sbql4j.tools.javac.code.Attribute.Compound;

public abstract class JavaSignatureFactory {

	public abstract ValueSignature createValueSignature(Type javaType, boolean staticMembersOnly) throws NotSupportedException;
	public abstract Signature createSignature(Type javaType, boolean staticMembersOnly, List<Compound> annotationList) throws NotSupportedException;
	public abstract ClassSignature createClassSignature(Type.ClassType javaType);
	public abstract MethodSignature createJavaMethodSignature(Symbol.MethodSymbol method);
	public abstract ConstructorSignature createJavaConstructorSignature(Symbol.MethodSymbol constr);
	public abstract ValueSignature createJavaSignature(Type javaType) throws NotSupportedException;

	
	public static Class compilerTypeToClass(Type t) {
		if(t.isPrimitive()) {
			if(t.tag == TypeTags.BYTE) return Byte.class;
			if(t.tag == TypeTags.CHAR) return Character.class;
			if(t.tag == TypeTags.SHORT) return Short.class;
			if(t.tag == TypeTags.INT) return Integer.class;
			if(t.tag == TypeTags.LONG) return Long.class;
			if(t.tag == TypeTags.FLOAT) return Float.class;
			if(t.tag == TypeTags.DOUBLE) return Double.class;
			if(t.tag == TypeTags.BOOLEAN) return Boolean.class;
		} else {
			try {
				return Class.forName(t.tsym.toString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Db4oConnectionSignature createDb4oConnectionSignature(Type compilerType, SCollectionType col) {
		return new Db4oConnectionSignature(compilerType, col);
	}
}
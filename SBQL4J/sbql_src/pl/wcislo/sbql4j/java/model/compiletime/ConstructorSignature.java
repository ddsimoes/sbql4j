package pl.wcislo.sbql4j.java.model.compiletime;

import java.lang.reflect.Constructor;

import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.ClassSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.MethodSymbol;

public class ConstructorSignature extends ValueSignature implements StaticEVNSType {
	
	public final MethodSymbol constructor;
	public final ClassSymbol ownerClass;
	public final ValueSignature returnTypeSig;
	public final StructSignature paramsSig;
	private NestedInfo nestedInfo;
	
	public ConstructorSignature(MethodSymbol constructor, ClassSymbol ownerClass, ValueSignature returnTypeSig, StructSignature paramsSig) {
		super(returnTypeSig.getType(), returnTypeSig.getColType(),  returnTypeSig.fields, returnTypeSig.methods);
		this.constructor = constructor;
		this.ownerClass = ownerClass;
		this.returnTypeSig = returnTypeSig;
		this.paramsSig = paramsSig;
	}
	
	@Override
	public ConstructorSignature clone() {
		ConstructorSignature s = new ConstructorSignature(constructor, ownerClass, returnTypeSig, paramsSig.clone());
		s.setAssociatedExpression(this.getAssociatedExpression());
		s.setResultSource(this.getResultSource());
		return s;
	}
	
	@Override
	public String getName() {
		return constructor.name.toString();
	}
	
	public boolean isApplicableTo(
//			String mName, 
			StructSignature paramsSig) {
//		if(this.constructor.getName().equals(mName)) {
			if(this.paramsSig.isStructuralTypeCompatible(paramsSig)) {
				return true;
			}
//		}
		return false;
	}

	public NestedInfo getNestedInfo() {
		return nestedInfo;
	}

	public void setNestedInfo(NestedInfo nestedInfo) {
		this.nestedInfo = nestedInfo;
	}

}

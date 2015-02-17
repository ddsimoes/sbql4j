package pl.wcislo.sbql4j.java.model.compiletime;

import java.sql.Date;

import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.ClassSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.MethodSymbol;

public class MethodSignature extends ValueSignature implements StaticEVNSType {
	
//	public final Method method;
//	public final Class ownerClass;
	
	public final MethodSymbol method;
	public final ClassSymbol ownerClass;
	
	public final ValueSignature returnTypeSig;
	public StructSignature paramsSig;
	private NestedInfo nestedInfo;
	
	public MethodSignature(MethodSymbol method, ClassSymbol ownerClass, ValueSignature returnTypeSig, StructSignature paramsSig) {
		super(returnTypeSig.getType(), returnTypeSig.getColType(),  returnTypeSig.fields, returnTypeSig.methods);
		this.method = method;
		this.ownerClass = ownerClass;
		this.returnTypeSig = returnTypeSig;
		this.paramsSig = paramsSig;
		this.setArrayType(returnTypeSig.isArrayType());
	}
	
	@Override
	public MethodSignature clone() {
		MethodSignature s = new MethodSignature(method, ownerClass, returnTypeSig, paramsSig.clone());
		s.setAssociatedExpression(this.getAssociatedExpression());
		s.setColType(this.getColType());
		s.setResultSource(this.getResultSource());
		return s;
		
		
	}
	
	@Override
	public String getName() {
		return method.name.toString();
	}
	
	public boolean isApplicableTo(String mName, StructSignature paramsSig) {
		if(this.method.name.toString().equals(mName)) {
//			if(this.paramsSig.isStructuralTypeCompatible(paramsSig)) {
			if(paramsSig.isStructuralTypeCompatible(this.paramsSig)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "<MethodSig "+getType()+" "+method.toString()+" "+getColType()+">";
	}

	@Override
	public NestedInfo getNestedInfo() {
		return nestedInfo;
	}
	
	@Override
	public void setNestedInfo(NestedInfo nestedInfo) {
		this.nestedInfo = nestedInfo;
	}
}

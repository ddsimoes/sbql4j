package pl.wcislo.sbql4j.java.model.compiletime;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;

import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureAbstractFactory;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureFactory;
import pl.wcislo.sbql4j.lang.parser.expression.NestingExpression;
import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.MethodSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.VarSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Type.ClassType;
import pl.wcislo.sbql4j.tools.javac.util.Context;
import pl.wcislo.sbql4j.tools.javac.util.Log;

/**
 * This class implements value signatures
 *
 */

public class ValueSignature extends Signature {
	
	private org.apache.commons.logging.Log log = LogFactory.getLog(ValueSignature.class);

//	public final Class type;
//	protected Map<String, Field> fields;
//	protected Map<String, List<Method>> methods;
	
	private final Type type;
	protected final List<Symbol.VarSymbol> fields;
	protected final List<Symbol.MethodSymbol> methods;
	private boolean isArrayType;
	private boolean primitiveType;
	
	
//	public ValueSignature(Class valueType, SCollectionType collectionType, Map<String, Field> fields, Map<String, List<Method>> methods) {
	public ValueSignature(ValueSignature vs) {
		super(vs.getType().toString(), vs.getColType());
		this.type = vs.getType();
		this.fields = vs.fields;
		this.methods = vs.methods;
	}
	
	public ValueSignature(Type valueType, SCollectionType collectionType, List<Symbol.VarSymbol> fields, List<Symbol.MethodSymbol> methods) {
		super(valueType.toString(), collectionType);
		this.type = valueType;
		this.fields = fields;
		this.methods = methods;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "<ValueSIG type="+getType()+" collectionType="+getColType()+">";
	}
	
	@Override
	public ValueSignature clone() {
		ValueSignature res = new ValueSignature(this.getType(), this.getColType(), this.fields, this.methods);
		res.setColType(this.getColType());
		res.setArrayType(this.isArrayType());
		res.setResultName(this.getResultName());
		res.setAssociatedExpression(this.getAssociatedExpression());
		res.setPrimitiveType(primitiveType);
		res.setResultSource(this.getResultSource());
		return res;
	}
	
//	@Override
//	public String getName() {
//		return "";
//	}
	
	protected void initNestedSigs(NestingExpression nestingExpression) {
		nestedSigs = new ArrayList<StaticEVNSType>();
		if(! (this instanceof ClassSignature)) {
			BinderSignature selfbs = new BinderSignature("self", this, true, this.getResultSource());
			selfbs.setAssociatedExpression(this.getAssociatedExpression());
			NestedInfo ni = new NestedInfo(this, null, nestingExpression);
			selfbs.setNestedInfo(ni);
//			selfbs.resultSource = this.resultSource;
			nestedSigs.add(selfbs);	
		}
		JavaSignatureFactory sigFac = JavaSignatureAbstractFactory.getJavaSignatureFactory();
	
		for(VarSymbol vs : fields) {
			Type varType = vs.type;
			ValueSignature vSig = sigFac.createValueSignature(varType, vs.isStatic());
			vSig.setResultSource(this.getResultSource());
			BinderSignature bs = new BinderSignature(vs.name.toString(), vSig, false, this.getResultSource());
			bs.setAssociatedExpression(this.getAssociatedExpression());
			NestedInfo ni = new NestedInfo(this, null, nestingExpression);
			bs.setNestedInfo(ni);
//			bs.resultSource = this.resultSource;
			nestedSigs.add(bs);
		}
		
		for(MethodSymbol ms : methods) {
			MethodSignature mSig = sigFac.createJavaMethodSignature(ms);
			mSig.setResultSource(this.getResultSource());
			mSig.setAssociatedExpression(this.getAssociatedExpression());
			NestedInfo ni = new NestedInfo(this, null, nestingExpression);
			mSig.setNestedInfo(ni);
			mSig.setResultSource(this.getResultSource());
			nestedSigs.add(mSig);
		}
	};
	
	@Override
	public boolean isTypeCompatible(Signature sig) {
		ClassTypes ct = ClassTypes.getInstance();
		ValueSignature valSig;
		if(sig instanceof ValueSignature) {
			valSig = (ValueSignature) sig;
		} else if(sig instanceof BinderSignature) {
			//automatyczna dereferencja
			Signature bValSig = ((BinderSignature) sig).value;
			bValSig.setColType(sig.getColType());
			if(!(bValSig instanceof ValueSignature)) {
				return false;
			}
			valSig = (ValueSignature) bValSig;
		} else {
			return false;
		}
		boolean typeOK = ct.isSubClass(this.getType(), valSig.getType());
		boolean collectionTypeOK = this.getColType() == sig.getColType();
		return typeOK && collectionTypeOK;
	}
	
	public boolean isTypeCompatible(Class javaType) {
		ClassTypes cTypes = ClassTypes.getInstance();
		Type javaCType = cTypes.getCompilerType(javaType);
		if( !(javaCType instanceof ClassType) || !(this.getType() instanceof ClassType) ) {
			log.warn("incompatible types: "+this.getType()+" "+javaCType);
			return false;
		}
		ClassType ct1 = (ClassType) this.getType();
		ClassType ct2 = (ClassType) javaCType;
		
		boolean result = cTypes.isSubClass(ct1, ct2);
		return result;
	}
	
	public boolean isTypeCompatible(Type javaCType) {
		ClassTypes cTypes = ClassTypes.getInstance();
//		Type javaCType = cTypes.getCompilerType(javaType);
		if( !(javaCType instanceof ClassType) || !(this.getType() instanceof ClassType) ) {
			log.warn("incompatible types: "+this.getType()+" "+javaCType);
			return false;
		}
		ClassType ct1 = (ClassType) this.getType();
		ClassType ct2 = (ClassType) javaCType;
		
		boolean result = cTypes.isSubClass(ct1, ct2);
		return result;
	}
	
	@Override
	public Signature getDerefSignature() {
		return this;
	}
	
	@Override
	public Signature getDerefSignatureWithCardinality() {
		return this;
	}

	public void setArrayType(boolean isArrayType) {
		this.isArrayType = isArrayType;
	}

	public boolean isArrayType() {
		return isArrayType;
	}

	public void setPrimitiveType(boolean primitiveType) {
		this.primitiveType = primitiveType;
	}

	public boolean isPrimitiveType() {
		return primitiveType;
	}
	
}

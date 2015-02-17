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

public class Db4oConnectionSignature extends ValueSignature {
	
	private org.apache.commons.logging.Log log = LogFactory.getLog(Db4oConnectionSignature.class);

//	protected final List<Symbol.VarSymbol> fields;
//	protected final List<Symbol.MethodSymbol> methods;
//	public boolean isArrayType;
//	public boolean primitiveType;
	
	
//	public ValueSignature(Class valueType, SCollectionType collectionType, Map<String, Field> fields, Map<String, List<Method>> methods) {
	public Db4oConnectionSignature(Type valueType, SCollectionType collectionType) {
		super(valueType, collectionType, null, null);
//		this.fields = fields;
//		this.methods = methods;
	}

	@Override
	public String toString() {
		return "<Db4oConnSIG collectionType="+getColType()+">";
	}
	
	@Override
	public Db4oConnectionSignature clone() {
		Db4oConnectionSignature res = new Db4oConnectionSignature(this.getType(), this.getColType());
		res.setColType(this.getColType());
//		res.isArrayType = this.isArrayType;
		res.setResultName(this.getResultName());
		res.setAssociatedExpression(this.getAssociatedExpression());
//		res.primitiveType = primitiveType;
		res.setResultSource(this.getResultSource());
		return res;
	}
	
//	@Override
//	public String getName() {
//		return "";
//	}
	
	protected void initNestedSigs(NestingExpression nestingExpression) {
		nestedSigs = new ArrayList<StaticEVNSType>();
		Db4oNestedMarker dbMarker = new Db4oNestedMarker();
		NestedInfo ni = new NestedInfo(this, -1, nestingExpression);
		dbMarker.setNestedInfo(ni);
		nestedSigs.add(dbMarker);
	};
	
	@Override
	public boolean isTypeCompatible(Signature sig) {
		Db4oConnectionSignature valSig;
		if(sig instanceof Db4oConnectionSignature) {
			valSig = (Db4oConnectionSignature) sig;
		} else if(sig instanceof BinderSignature) {
			//automatyczna dereferencja
			Signature bValSig = ((BinderSignature) sig).value;
			bValSig.setColType(sig.getColType());
			if(!(bValSig instanceof Db4oConnectionSignature)) {
				return false;
			}
			valSig = (Db4oConnectionSignature) bValSig;
		} else {
			return false;
		}
		boolean typeOK = valSig instanceof Db4oConnectionSignature;
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
	
}

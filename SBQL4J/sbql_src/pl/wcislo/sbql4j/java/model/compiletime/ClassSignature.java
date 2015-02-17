package pl.wcislo.sbql4j.java.model.compiletime;

import java.util.List;

import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureAbstractFactory;
import pl.wcislo.sbql4j.lang.parser.expression.NestingExpression;
import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.MethodSymbol;

/**
 * This class implements value signatures
 *
 */

public class ClassSignature extends ValueSignature {
	
//	public final Class type;
//	protected Map<String, Field> staticFields;
//	protected Map<String, List<Method>> staticMethods;
	
//	protected List<Constructor> constructors;
	protected List<MethodSymbol> constructors;
	
	public ClassSignature(Type ownerClass, List<Symbol.VarSymbol> fields, 
			List<Symbol.MethodSymbol> methods, List<MethodSymbol> constructors) {
		super(ownerClass, SCollectionType.NO_COLLECTION, fields, methods);
		this.constructors = constructors;
	}

	@Override
	public String toString() {
		return "<ClassSIG type="+getType()+" collectionType="+getColType()+">";
	}
	
	@Override
	public ClassSignature clone() {
		ClassSignature res = new ClassSignature(this.getType(), this.fields, this.methods, this.constructors);
		res.setAssociatedExpression(this.getAssociatedExpression());
		res.setColType(this.getColType());
		res.setResultSource(this.getResultSource());
		return res;
	}
	
	@Override
	protected void initNestedSigs(NestingExpression nestingExpression) {
		super.initNestedSigs(nestingExpression);
		for(MethodSymbol constr : constructors) {
			ConstructorSignature sig = JavaSignatureAbstractFactory.getJavaSignatureFactory().createJavaConstructorSignature(constr);
			nestedSigs.add(sig);
		}
	}
	
//	@Override
//	public List<ENVSType> nested() {
//		List<ENVSType> res = super.nested();
//		//TODO EW
////		JavaSignatureFactory fac = JavaSignatureReflectFactory.getInstance();
////		for(MethodSymbol constr : constructors) {
////			JavaConstructorSignature sig = fac.createJavaConstructorSignature(constr);
////			res.add(sig);
////		}
//		return res;
//	}
}

package pl.wcislo.sbql4j.lang.parser.terminals;

import pl.wcislo.sbql4j.java.model.compiletime.BinderSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectFactory;
import pl.wcislo.sbql4j.lang.codegen.noqres.QueryCodeGenNoQres;
import pl.wcislo.sbql4j.lang.codegen.nostacks.QueryCodeGenNoStacks;
import pl.wcislo.sbql4j.lang.codegen.simple.QueryCodeGenSimple;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorType;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.lang.tree.visitors.OperatorVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TypeChecker;
import pl.wcislo.sbql4j.lang.types.ENVSType;

public abstract class Operator extends Terminal {

	protected static final JavaObjectFactory javaObjectFactory = JavaObjectAbstractFactory.getJavaObjectFactory();
//	protected static final JavaSignatureFactory javaSigFactory = JavaSignatureAbstractFactory.getJavaSignatureFactory();
	
	public final OperatorType type;
	public Operator(OperatorType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return ""+this.type;
//		return this.getClass().getName()+" "+this.type;
	}
	
	public abstract <T,V extends TreeVisitor> T accept(OperatorVisitor<T, V> opVisitor, V treeVisitor, Expression opExpr, Expression... subExprs);
	
//	public abstract boolean checkType(ENVSType... args);
	public abstract void eval(Interpreter interpreter, Expression... args );
	public abstract void evalStatic(TypeChecker checker, Expression... args );
//	public abstract void generateSimpleCode(QueryCodeGenSimple gen, Expression... args);
//	public abstract void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args);
//	public abstract void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args);
//		
//	}
	
	protected boolean validateArgumentsNumber(int num, ENVSType[] args) {
		boolean res = args != null && args.length == num;
		if(!res) return res;
		for(ENVSType arg : args) {
			if(arg == null) {
				return false;
			}
		}
		return true;
	}
	
	public Class getAllowedGenericType() {
		return null;
	}
	
	protected boolean checkSingleResultSignature(TypeChecker checker, Expression e) {
		Signature s = e.getSignature();
		if(s instanceof BinderSignature) {
			s = ((BinderSignature)s).value;
		}
		if(s.getColType() != SCollectionType.NO_COLLECTION) {
			checker.addError(e, "expected single result, got: "+s.getColType());
			return false;
		}
		return true;
	}
}

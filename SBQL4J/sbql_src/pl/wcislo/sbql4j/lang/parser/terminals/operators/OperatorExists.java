package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectAbstractFactory;
import pl.wcislo.sbql4j.lang.codegen.noqres.QueryCodeGenNoQres;
import pl.wcislo.sbql4j.lang.codegen.nostacks.QueryCodeGenNoStacks;
import pl.wcislo.sbql4j.lang.codegen.simple.QueryCodeGenSimple;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.terminals.Operator;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.lang.tree.visitors.OperatorVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TypeChecker;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.model.collections.CollectionResult;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorExists extends Operator {

	public OperatorExists(OperatorType type) {
		super(type);
	}
	
	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitExists(this, treeVisitor, opExpr, subExprs);
	};
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		QueryResult e1res = interpreter.getQres().pop();
		CollectionResult col = Utils.objectToCollection(e1res);
		boolean result = !col.isEmpty();
		interpreter.getQres().add(JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject(result));
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identCol = gen.generateIdentifier("col");
//		String identResult = gen.generateIdentifier("result");
//		
//		gen.printExpressionTrace("//OperatorExists - start\n");
//		sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
//		sb.append("CollectionResult "+identCol+" = Utils.objectToCollection("+identE1Res+"); \n");
//		sb.append("boolean "+identResult+" = !col.isEmpty(); \n");
//		sb.append("qres.add(JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject("+identResult+")); \n");
//		gen.printExpressionTrace("//OperatorExists - end\n");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		Expression opExpr = args[0];
//		Expression argExpr = args[1];
//		StringBuilder sb = gen.sb;
//		String identArgRes = argExpr.getSignature().getResultName();
//		
//		gen.printExpressionTrace("//OperatorExists - start\n");
//		if(argExpr.getSignature().sColType != SCollectionType.NO_COLLECTION) {
//			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject(!"+identArgRes+".isEmpty()); \n");
//		} else {
//			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject("+identArgRes+" != null); \n");
//		}
//		gen.printExpressionTrace("//OperatorExists - end\n");	
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		Expression opExpr = args[0];
//		Expression argExpr = args[1];
//		StringBuilder sb = gen.sb;
//		String identArgRes = argExpr.getSignature().getResultName();
//		
//		gen.printExpressionTrace("//OperatorExists - start\n");
//		if(argExpr.getSignature().sColType != SCollectionType.NO_COLLECTION) {
//			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = !"+identArgRes+".isEmpty(); \n");
//		} else {
//			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identArgRes+" != null; \n");
//		}
//		gen.printExpressionTrace("//OperatorExists - end\n");		
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression valExpression = args[0];
		Expression existExpression = args[1];
		ValueSignature sig = JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createValueSignature(ClassTypes.getInstance().getCompilerType(Boolean.class), false);
		existExpression.setSignature(sig);
	}

}
	
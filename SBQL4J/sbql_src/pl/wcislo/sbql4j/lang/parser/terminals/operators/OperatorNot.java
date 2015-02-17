package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureAbstractFactory;
import pl.wcislo.sbql4j.lang.codegen.noqres.QueryCodeGenNoQres;
import pl.wcislo.sbql4j.lang.codegen.nostacks.QueryCodeGenNoStacks;
import pl.wcislo.sbql4j.lang.codegen.simple.QueryCodeGenSimple;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.terminals.Operator;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.lang.tree.visitors.OperatorVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TypeChecker;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorNot extends Operator {

	public OperatorNot(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitNot(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		Boolean b = (Boolean) Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());
		interpreter.getQres().add(javaObjectFactory.createJavaComplexObject(!b));
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identB = gen.generateIdentifier("b");
//		gen.printExpressionTrace("//OperatorNot - start \n");
//		sb.append("Boolean "+identB+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append("qres.add(javaObjectFactory.createJavaComplexObject(!"+identB+")); \n");
//		gen.printExpressionTrace("//OperatorNot - start \n");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		Expression opExpr = args[0];
//		Expression argExpr = args[1];
//		Expression genericTypeExpr = args[2];
//		
//		StringBuilder sb = gen.sb;
//		String identB = gen.generateIdentifier("b");
//		gen.printExpressionTrace("//OperatorNot - start \n");
//		sb.append("Boolean "+identB+" = (Boolean) Utils.toSimpleValue("+argExpr.getSignature().getResultName()+", store); \n");
//		sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject(!"+identB+")); \n");
//		gen.printExpressionTrace("//OperatorNot - start \n");	
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		Expression opExpr = args[0];
//		Expression argExpr = args[1];
//		Expression genericTypeExpr = args[2];
//		
//		StringBuilder sb = gen.sb;
//		String identB = gen.generateIdentifier("b");
//		gen.printExpressionTrace("//OperatorNot - start \n");
////		sb.append("Boolean "+identB+" = (Boolean) Utils.toSimpleValue("+argExpr.signature.getResultName()+", store); \n");
//		sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = !"+argExpr.getSignature().getResultName()+"; \n");
//		gen.printExpressionTrace("//OperatorNot - end \n");		
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression valExpression = args[0];
		Expression existExpression = args[1];;
		ClassTypes ct = ClassTypes.getInstance();
		ValueSignature expectedSig = new ValueSignature(ct.getCompilerType(Boolean.class), SCollectionType.NO_COLLECTION, null, null);
		ValueSignature leftSig = (ValueSignature)valExpression.getSignature();
		boolean isLeftTypeBoolean = leftSig.isTypeCompatible(expectedSig);
		String expType = "Boolean";
		if(!isLeftTypeBoolean) {
			checker.addInvalidTypeError(valExpression, expType, leftSig.getTypeName());
		}
		ValueSignature vsig = JavaSignatureAbstractFactory.getJavaSignatureFactory().createValueSignature(ct.getCompilerType(Boolean.class), false);
		existExpression.setSignature(vsig);
	}

}

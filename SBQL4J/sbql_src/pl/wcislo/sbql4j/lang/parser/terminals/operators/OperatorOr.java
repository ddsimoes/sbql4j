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
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorOr extends Operator {

	public OperatorOr(OperatorType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitOr(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		Boolean rightArg = (Boolean) Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());		
		Boolean leftArg = (Boolean) Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());
		QueryResult result = javaObjectFactory.createJavaComplexObject(leftArg || rightArg);
		interpreter.getQres().push(result);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identRightArg = gen.generateIdentifier("rightArg");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResult = gen.generateIdentifier("result");
//		
//		gen.printExpressionTrace("//OperatorOr - start \n");
//		sb.append("Boolean "+identRightArg+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append("Boolean "+identLeftArg+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+" || "+identRightArg+"); \n");
//		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorOr - start \n");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		StringBuilder sb = gen.sb;
//		String identRightArg = gen.generateIdentifier("rightArg");
//		String identLeftArg = gen.generateIdentifier("leftArg");
////		String identResult = gen.generateIdentifier("result");
//		String identLeftRes = leftExpr.getSignature().getResultName();
//		String identRightRes = rightExpr.getSignature().getResultName();
//		
//		gen.printExpressionTrace("//OperatorOr - start \n");
//		sb.append("Boolean "+identRightArg+" = (Boolean) Utils.toSimpleValue("+identRightRes+", store); \n");
//		sb.append("Boolean "+identLeftArg+" = (Boolean) Utils.toSimpleValue("+identLeftRes+", store); \n");
//		sb.append(opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+" || "+identRightArg+"); \n");
////		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorOr - end \n");	
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		StringBuilder sb = gen.sb;
//		String identRightArg = rightExpr.getSignature().getResultName();
//		String identLeftArg = leftExpr.getSignature().getResultName();
////		String identResult = gen.generateIdentifier("result");
////		String identLeftRes = leftExpr.signature.getResultName();
////		String identRightRes = rightExpr.signature.getResultName();
//		
//		gen.printExpressionTrace("//OperatorOr - start \n");
////		sb.append("Boolean "+identRightArg+" = (Boolean) Utils.toSimpleValue("+identRightRes+", store); \n");
////		sb.append("Boolean "+identLeftArg+" = (Boolean) Utils.toSimpleValue("+identLeftRes+", store); \n");
//		sb.append(opExpr.getSignature().genJavaDeclarationCode()+" = "+identLeftArg+" || "+identRightArg+"; \n");
////		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorOr - end \n");	
//	}

	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression leftExpr = args[0];
		Expression rightExpr = args[1];
		Expression opExpr = args[2];
		
		
		ClassTypes ct = ClassTypes.getInstance();
		ValueSignature leftExpectedSig = new ValueSignature(ct.getCompilerType(Boolean.class), SCollectionType.NO_COLLECTION, null, null);
		ValueSignature rightExpectedSig = leftExpectedSig;
		
		
//		Class leftType = ((ValueSignature)leftExpr.signature).type;
//		Class rightType = ((ValueSignature)rightExpr.signature).type;
		ValueSignature leftSig = (ValueSignature)leftExpr.getSignature().getDerefSignatureWithCardinality();
		ValueSignature rightSig = (ValueSignature)rightExpr.getSignature().getDerefSignatureWithCardinality();
//		
//		
//		Class leftType = null;
//		Class rightType = null;
//		Class returnType = null;
//		
		boolean isLeftTypeBoolean = leftSig.isTypeCompatible(leftExpectedSig);
		boolean isRightTypeBoolean = rightSig.isTypeCompatible(rightExpectedSig);
		
		String expType = "Boolean";
//		String errorMsg = "Boolean expected, got ";
		if(!isLeftTypeBoolean) {
			checker.addInvalidTypeError(leftExpr, expType, leftSig.getTypeName());
		}
		if(!isRightTypeBoolean) {
			checker.addInvalidTypeError(rightExpr, expType, rightSig.getTypeName());
		}
		ValueSignature vsig = JavaSignatureAbstractFactory.getJavaSignatureFactory().createValueSignature(ct.getCompilerType(Boolean.class), false);
		opExpr.setSignature(vsig);
	}
}

package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.JavaObject;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectFactory;
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

public class OperatorAnd extends Operator {

	public OperatorAnd(OperatorType type) {
		super(type);
	}
	
	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitAnd(this, treeVisitor, opExpr, subExprs);
	};

	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		Object rightArg = Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());		
		Object leftArg = Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());
		
		JavaObject result = null;
		if ((leftArg instanceof Boolean) && (rightArg instanceof Boolean)) {
			Boolean b1 = (Boolean) leftArg;
			Boolean b2 = (Boolean) rightArg;
			JavaObjectFactory fac = JavaObjectAbstractFactory.getJavaObjectFactory();
			result = fac.createJavaComplexObject(b1 && b2);
		} else {
			throw new RuntimeException(
					"OperatorAnd.eval() invalid type: left=" + leftArg
							+ " right=" + rightArg);
		} 
		interpreter.getQres().push(result);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identRightArg = gen.generateIdentifier("rightArg");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResult = gen.generateIdentifier("result");
//		
//		gen.printExpressionTrace("//OperatorAnd - start \n");
//		sb.append("Boolean "+identRightArg+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append("Boolean "+identLeftArg+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+" && "+identRightArg+"); \n");
//		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorAnd - start \n");
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
//		String identResult = gen.generateIdentifier("result");
//		
//		String identLeftRes = leftExpr.getSignature().getResultName();
//		String identRightRes = rightExpr.getSignature().getResultName();
//		
//		gen.printExpressionTrace("//OperatorAnd - start \n");
//		sb.append("Boolean "+identRightArg+" = (Boolean) Utils.toSimpleValue("+identRightRes+", store); \n");
//		sb.append("Boolean "+identLeftArg+" = (Boolean) Utils.toSimpleValue("+identLeftRes+", store); \n");
//		sb.append(opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+" && "+identRightArg+"); \n");
////		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorAnd - start \n");
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
//		
//		gen.printExpressionTrace("//OperatorAnd - start \n");
////		sb.append("Boolean "+identRightArg+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
////		sb.append("Boolean "+identLeftArg+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append(opExpr.getSignature().genJavaDeclarationCode()+" = "+identLeftArg+" && "+identRightArg+"; \n");
////		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorAnd - end \n");
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
		ValueSignature leftSig = (ValueSignature)leftExpr.getSignature();
		ValueSignature rightSig = (ValueSignature)rightExpr.getSignature();
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

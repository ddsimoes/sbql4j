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
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorDivide extends Operator {

	public OperatorDivide(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitDivide(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		Number rightArg = (Number) Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());
		Number leftArg = (Number) Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());
		Number resultNum = MathUtils.divide(leftArg, rightArg);
		QueryResult result = javaObjectFactory.createJavaComplexObject(resultNum);
		interpreter.getQres().push(result);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identRightArg = gen.generateIdentifier("rightArg");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResultNum = gen.generateIdentifier("resultNum");
//		String identResult = gen.generateIdentifier("result");
//		gen.printExpressionTrace("//OperatorDivide - start \n");
//		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append("Number "+identResultNum+" = MathUtils.divide("+identLeftArg+", "+identRightArg+"); \n");
//		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
//		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorDivide - end \n");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
////		StringBuilder sb = gen.sb;
////		String identRightArg = gen.generateIdentifier("rightArg");
////		String identLeftArg = gen.generateIdentifier("leftArg");
////		String identResultNum = gen.generateIdentifier("resultNum");
////		String identResult = gen.generateIdentifier("result");
////		gen.printExpressionTrace("//OperatorDivide - start \n");
////		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
////		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
////		sb.append("Number "+identResultNum+" = MathUtils.divide("+identLeftArg+", "+identRightArg+"); \n");
////		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
////		sb.append("qres.push("+identResult+"); \n");
////		gen.printExpressionTrace("//OperatorDivide - end \n");	
////		
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		String identLeftRes = leftExpr.getSignature().getResultName();
//		String identRightRes = rightExpr.getSignature().getResultName();
//		
//		StringBuilder sb = gen.sb;
//		String identRightArg = gen.generateIdentifier("rightArg");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResultNum = gen.generateIdentifier("resultNum");
//		String identResult = opExpr.getSignature().getResultName();
//		gen.printExpressionTrace("//OperatorDivide - start \n");
//		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue("+identRightRes+", store); \n");
//		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue("+identLeftRes+", store); \n");
//		sb.append("Number "+identResultNum+" = MathUtils.divide("+identLeftArg+", "+identRightArg+"); \n");
//		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
////		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorDivide - end \n");
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
////		StringBuilder sb = gen.sb;
////		String identRightArg = gen.generateIdentifier("rightArg");
////		String identLeftArg = gen.generateIdentifier("leftArg");
////		String identResultNum = gen.generateIdentifier("resultNum");
////		String identResult = gen.generateIdentifier("result");
////		gen.printExpressionTrace("//OperatorDivide - start \n");
////		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
////		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
////		sb.append("Number "+identResultNum+" = MathUtils.divide("+identLeftArg+", "+identRightArg+"); \n");
////		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
////		sb.append("qres.push("+identResult+"); \n");
////		gen.printExpressionTrace("//OperatorDivide - end \n");	
////		
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		String identLeftRes = leftExpr.getSignature().getResultName();
//		String identRightRes = rightExpr.getSignature().getResultName();
//		
//		StringBuilder sb = gen.sb;
//		String identRightArg = gen.generateIdentifier("rightArg");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResultNum = gen.generateIdentifier("resultNum");
//		String identResult = opExpr.getSignature().getResultName();
//		gen.printExpressionTrace("//OperatorDivide - start \n");
////		sb.append(""+opExpr.signature.genJavaDeclarationCode()+" = "+identLeftRes+" / "+identRightRes+"; \n");
//		opExpr.getSignature().setResultName("("+identLeftRes+" / "+identRightRes+")");
////		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue("+identRightRes+", store); \n");
////		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue("+identLeftRes+", store); \n");
////		sb.append("Number "+identResultNum+" = MathUtils.divide("+identLeftArg+", "+identRightArg+"); \n");
////		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
////		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorDivide - end \n");	
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression leftExpr = args[0];
		Expression rightExpr = args[1];
		Expression opExpr = args[2];
		
		Type leftType = ((ValueSignature)leftExpr.getSignature()).getType();
		Type rightType = ((ValueSignature)rightExpr.getSignature()).getType();
		Class returnClass = Double.class;
//		Type returnType = ClassTypes.getInstance().getCompilerType(Double.class);
		
		ValueSignature expectedSig = 
			JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createJavaSignature(ClassTypes.getInstance().getCompilerType(Number.class));
		expectedSig.setColType(SCollectionType.NO_COLLECTION);

		boolean isLeftTypeNumber = leftExpr.getSignature().isTypeCompatible(expectedSig);
		boolean isRightTypeNumber = rightExpr.getSignature().isTypeCompatible(expectedSig);
	
		if(!isLeftTypeNumber || !isRightTypeNumber) {
			checker.addError(opExpr, "Number expected, got left="+leftExpr.getSignature()+" right="+rightExpr.getSignature());
		}
		Class leftClass = (Class) ClassTypes.getInstance().getReflectType(leftType);
		Class rightClass = (Class) ClassTypes.getInstance().getReflectType(rightType);
		returnClass = pl.wcislo.sbql4j.java.utils.Utils.getBinaryOperatorResult(leftClass, rightClass);
		
		ValueSignature vsig = 
			JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createValueSignature(ClassTypes.getInstance().getCompilerType(returnClass), false);
		
//		ValueSignature vsig = JavaSignatureAbstractFactory.getJavaSignatureFactory().
//			createValueSignature(returnType, false);
		opExpr.setSignature(vsig);
	}

}

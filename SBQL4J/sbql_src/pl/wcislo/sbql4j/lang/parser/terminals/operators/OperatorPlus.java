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

public class OperatorPlus extends Operator {

	public OperatorPlus(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitPlus(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		Object rightArg = Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());
		Object leftArg = Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());
		QueryResult result;
		if(leftArg instanceof String || rightArg instanceof String) {
			result = javaObjectFactory.createJavaComplexObject(leftArg.toString().concat(rightArg.toString()));
		} else {
			Number resultNum = MathUtils.sum((Number)leftArg, (Number)rightArg);
			result = javaObjectFactory.createJavaComplexObject(resultNum);
		}
		interpreter.getQres().push(result);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identRightArg = gen.generateIdentifier("rightArg");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResult = gen.generateIdentifier("result");
//		String identResultNum = gen.generateIdentifier("resultNum");
//		
//		gen.printExpressionTrace("//OperatorPlus - start \n");  
//		sb.append("Object "+identRightArg+" = Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append("QueryResult "+identResult+"; \n");
//		sb.append("if("+identLeftArg+" instanceof String || "+identRightArg+" instanceof String) { \n");
//		sb.append("	"+identResult+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+".toString().concat("+identRightArg+".toString())); \n");
//		sb.append("} else { \n");
//		sb.append("	Number "+identResultNum+" = MathUtils.sum((Number)"+identLeftArg+", (Number)"+identRightArg+"); \n");
//		sb.append("	"+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
//		sb.append("} \n");
//		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorPlus - end \n");
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
//		String identResult = opExpr.getSignature().getResultName();
//		String identResultNum = gen.generateIdentifier("resultNum");
//		
//		String identLeftRes = leftExpr.getSignature().getResultName();
//		String identRightRes = rightExpr.getSignature().getResultName();
//		
//		gen.printExpressionTrace("//OperatorPlus - start \n");  
//		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store); \n");
//		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store); \n");
//		sb.append("QueryResult "+identResult+"; \n");
//		sb.append("if("+identLeftArg+" instanceof String || "+identRightArg+" instanceof String) { \n");
//		sb.append("	"+identResult+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+".toString().concat("+identRightArg+".toString())); \n");
//		sb.append("} else { \n");
//		sb.append("	Number "+identResultNum+" = MathUtils.sum((Number)"+identLeftArg+", (Number)"+identRightArg+"); \n");
//		sb.append("	"+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
//		sb.append("} \n");
////		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorPlus - end \n");	
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		StringBuilder sb = gen.sb;
//		String identRightArg = gen.generateIdentifier("rightArg");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResult = opExpr.getSignature().getResultName();
//		String identResultNum = gen.generateIdentifier("resultNum");
//		
//		String identLeftRes = leftExpr.getSignature().getResultName();
//		String identRightRes = rightExpr.getSignature().getResultName();
//		
//		gen.printExpressionTrace("//OperatorPlus - start \n");  
////		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store); \n");
////		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store); \n");
////		sb.append("QueryResult "+identResult+"; \n");
////		sb.append("if("+identLeftArg+" instanceof String || "+identRightArg+" instanceof String) { \n");
////		sb.append("	"+identResult+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+".toString().concat("+identRightArg+".toString())); \n");
////		sb.append("} else { \n");
////		sb.append("	Number "+identResultNum+" = MathUtils.sum((Number)"+identLeftArg+", (Number)"+identRightArg+"); \n");
////		sb.append("	"+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
////		sb.append("} \n");
////		sb.append(""+opExpr.signature.genJavaDeclarationCode()+" = "+identLeftRes+" + "+identRightRes+"; \n");
//		opExpr.getSignature().setResultName("("+identLeftRes+" + "+identRightRes+")");
////		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorPlus - end \n");		
//	}

	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression leftExpr = args[0];
		Expression rightExpr = args[1];
		Expression plusExpr = args[2];
		
		Type leftType = ((ValueSignature)leftExpr.getSignature()).getType();
		Type rightType = ((ValueSignature)rightExpr.getSignature()).getType();
		Class returnClass = Integer.class;
		
		ValueSignature expectedSig = 
			JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createJavaSignature(ClassTypes.getInstance().getCompilerType(Number.class));
		expectedSig.setColType(SCollectionType.NO_COLLECTION);
		
		ValueSignature expectedSig2 = 
			JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createJavaSignature(ClassTypes.getInstance().getCompilerType(String.class));
		expectedSig2.setColType(SCollectionType.NO_COLLECTION);

		boolean isLeftTypeNumber = leftExpr.getSignature().isTypeCompatible(expectedSig);
		boolean isRightTypeNumber = rightExpr.getSignature().isTypeCompatible(expectedSig);
		boolean isLeftTypeString = leftExpr.getSignature().isTypeCompatible(expectedSig2);
		boolean isRightTypeString = rightExpr.getSignature().isTypeCompatible(expectedSig2);
		
		boolean isLeftTypeOK = isLeftTypeNumber || isLeftTypeString;
		boolean isRightTypeOK = isRightTypeNumber || isRightTypeString;
		
		if(!isLeftTypeOK || !isRightTypeOK) {
			checker.addError(plusExpr, "Number or String expected, got left="+leftExpr.getSignature()+" right="+rightExpr.getSignature());
		}
		
		Class leftClass = (Class) ClassTypes.getInstance().getReflectType(leftType);
		Class rightClass = (Class) ClassTypes.getInstance().getReflectType(rightType);
		if(isLeftTypeString || isRightTypeString) {
			returnClass = String.class;
		} else {
			returnClass = pl.wcislo.sbql4j.java.utils.Utils.getBinaryOperatorResult(leftClass, rightClass);
		}
		
		ValueSignature vsig = 
			JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createValueSignature(ClassTypes.getInstance().getCompilerType(returnClass), false);
		plusExpr.setSignature(vsig);
	}
}
package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import pl.wcislo.sbql4j.java.model.compiletime.ClassSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.JavaObject;
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

public class OperatorInstanceof extends Operator {

	public OperatorInstanceof(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitInstanceof(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		Class clazz = (Class)Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());		
		Object leftArg = Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());
		Boolean result = clazz.isInstance(leftArg);
		JavaObject res = javaObjectFactory.createJavaComplexObject(result);
		interpreter.getQres().push(res);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identClazz = gen.generateIdentifier("clazz");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResult = gen.generateIdentifier("result");
//		String identRes = gen.generateIdentifier("res");
//		
//		gen.printExpressionTrace("//OperatorInstanceof - start \n");
//		sb.append("Class "+identClazz+" = (Class) Utils.toSimpleValue(qres.pop(), store); \n");	
//		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append("Boolean "+identResult+" = "+identClazz+".isInstance("+identLeftArg+"); \n");
//		sb.append("JavaObject "+identRes+" = javaObjectFactory.createJavaComplexObject("+identResult+"); \n");
//		sb.append("qres.push("+identRes+"); \n");
//		gen.printExpressionTrace("//OperatorInstanceof - end \n");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		String identClazz = gen.generateIdentifier("clazz");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResult = gen.generateIdentifier("result");
//		String identRes = opExpr.getSignature().getResultName();
//		
//		gen.printExpressionTrace("//OperatorInstanceof - start \n");
//		sb.append("Class "+identClazz+" = (Class) Utils.toSimpleValue("+rightExpr.getSignature().getResultName()+", store); \n");	
//		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+leftExpr.getSignature().getResultName()+", store); \n");
//		sb.append("Boolean "+identResult+" = "+identClazz+".isInstance("+identLeftArg+"); \n");
//		sb.append("QueryResult "+identRes+" = javaObjectFactory.createJavaComplexObject("+identResult+"); \n");
////		sb.append("qres.push("+identRes+"); \n");
//		gen.printExpressionTrace("//OperatorInstanceof - end \n");	
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		String identClazz = gen.generateIdentifier("clazz");
//		String identLeftArg = leftExpr.getSignature().getResultName();
//		String identRightArg = rightExpr.getSignature().getResultName();
//		String identResult = gen.generateIdentifier("result");
//		String identRes = opExpr.getSignature().getResultName();
//		
//		gen.printExpressionTrace("//OperatorInstanceof - start \n");
//		sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identLeftArg+" instanceof "+identRightArg);
////		rightExpr.accept(gen, null);
//		sb.append("; \n");
//		gen.printExpressionTrace("//OperatorInstanceof - end \n");		
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression objectExpression = args[0];
		Expression classExpression = args[1];
		Expression opExpression = args[2];
		
		Signature s1 = objectExpression.getSignature().getDerefSignatureWithCardinality();
		Signature s2 = classExpression.getSignature().getDerefSignatureWithCardinality();
		if(s1 instanceof ValueSignature) {
//			ValueSignature vs1 = (ValueSignature) s1;
			if(s2 instanceof ClassSignature) {
//				ClassSignature cs2 = (ClassSignature) s2;
			} else {
				checker.addInvalidTypeError(classExpression, "Class", s2.getTypeName());
			}
		} else {
			checker.addInvalidTypeError(objectExpression, "Object", s1.getTypeName());
		}
		
		
		ValueSignature sig = JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createValueSignature(ClassTypes.getInstance().getCompilerType(Boolean.class), false);
		opExpression.setSignature(sig);
	}

}

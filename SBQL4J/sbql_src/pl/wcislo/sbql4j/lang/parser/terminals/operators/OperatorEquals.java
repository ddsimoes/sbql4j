package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
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
import pl.wcislo.sbql4j.util.Utils;

public class OperatorEquals extends Operator {

	public OperatorEquals(OperatorType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitEquals(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		Object rightArg = Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());		
		Object leftArg = Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());
		QueryResult result;
		result = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject((Utils.equalsNumeric(leftArg, rightArg)));
		interpreter.getQres().push(result);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identRightArg = gen.generateIdentifier("rightArg");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResult = gen.generateIdentifier("result");
//		gen.printExpressionTrace("//OperatorEquals - start \n");
//		sb.append("Object "+identRightArg+" = Utils.toSimpleValue(qres.pop(), store);\n");		
//		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue(qres.pop(), store);\n");
//		sb.append("QueryResult "+identResult+";\n");
//		sb.append(""+identResult+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject((Utils.equalsNumeric("+identLeftArg+", "+identRightArg+")));\n");
//		sb.append("qres.push("+identResult+");\n");
//		gen.printExpressionTrace("//OperatorEquals - end \n");
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
//		gen.printExpressionTrace("//OperatorEquals - start \n");
//		String identLeftRes = leftExpr.getSignature().getResultName();
//		String identRightRes = rightExpr.getSignature().getResultName();
//		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store);\n");		
//		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store);\n");
//		sb.append(" "+opExpr.getSignature().genSBQLDeclarationCode()+"; \n");
//		sb.append(""+identResult+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject((Utils.equalsNumeric("+identLeftArg+", "+identRightArg+")));\n");
//		gen.printExpressionTrace("//OperatorEquals - end \n");	
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
//		String identResult = opExpr.getSignature().getResultName();
//		gen.printExpressionTrace("//OperatorEquals - start \n");
//		String identResType = opExpr.getSignature().getJavaTypeString();
//		String identResName = opExpr.getSignature().getResultName();
//		sb.append(""+identResType+" "+identResName+" = OperatorUtils.equalsSafe("+identLeftArg+", "+identRightArg+"); \n");
//		gen.printExpressionTrace("//OperatorEquals - end \n");		
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression equalsExpression = args[2];
		ValueSignature sig = JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createValueSignature(ClassTypes.getInstance().getCompilerType(Boolean.class), false);
		equalsExpression.setSignature(sig);
	}

}

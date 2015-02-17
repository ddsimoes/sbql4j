package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
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

public class OperatorCount extends Operator {

	public OperatorCount(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitCount(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		QueryResult result;
		int count = 0;
	
		Object objects = interpreter.getQres().pop();
		if(objects instanceof CollectionResult) {
			count = ((CollectionResult) objects).size();
		} else {
			count = 1;
		}
		result = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject(count);
		interpreter.getQres().push(result);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		Expression argExpr = args[0];
//		StringBuilder sb = gen.sb;
//		String identResult = gen.generateIdentifier("result");
//		String identCount = gen.generateIdentifier("count");
//		String identObjects = gen.generateIdentifier("objects");
//		
//		gen.printExpressionTrace("//OperatorCount - start \n");
//		sb.append("QueryResult "+identResult+"; \n");
//		sb.append("int "+identCount+"; \n");
//		
//		if(argExpr.getSignature().sColType == SCollectionType.NO_COLLECTION) {
//			sb.append("	"+identCount+" = 1; \n");
//		} else {
//			sb.append("Object "+identObjects+" = qres.pop(); \n");
//			sb.append("	"+identCount+" = ((CollectionResult) "+identObjects+").size(); \n");	
//		}
//		sb.append(""+identResult+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject("+identCount+"); \n");
//		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorCount - end");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		Expression opExpr = args[0];
//		Expression argExpr = args[1];
//		
//		
//		StringBuilder sb = gen.sb;
//		String identResult = opExpr.getSignature().getResultName();
//		String identColRes = argExpr.getSignature().getResultName();
//		String identCount = gen.generateIdentifier("count");
//		
//		gen.printExpressionTrace("//OperatorCount - start \n");
//		sb.append("int "+identCount+"; \n");
//		
//		if(argExpr.getSignature().sColType == SCollectionType.NO_COLLECTION) {
//			sb.append("	"+identCount+" = 1; \n");
//		} else {
//			sb.append("	"+identCount+" = "+identColRes+".size(); \n");	
//		}
//		sb.append("QueryResult "+identResult+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject("+identCount+"); \n");
//		gen.printExpressionTrace("//OperatorCount - end");	
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		Expression opExpr = args[0];
//		Expression argExpr = args[1];
//		
//		
//		StringBuilder sb = gen.sb;
//		String identResult = opExpr.getSignature().getResultName();
//		String identColRes = argExpr.getSignature().getResultName();
//		String identCount = opExpr.getSignature().getResultName();
//		
//		gen.printExpressionTrace("//OperatorCount - start \n");
////		sb.append("int "+identCount+"; \n");
//		
//		if(argExpr.getSignature().sColType == SCollectionType.NO_COLLECTION) {
//			sb.append(opExpr.getSignature().genJavaDeclarationCode()+" = 1; \n");
//		} else {
//			sb.append(opExpr.getSignature().genJavaDeclarationCode()+" = "+identColRes+".size(); \n");	
//		}
////		sb.append("QueryResult "+identResult+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject("+identCount+"); \n");
//		gen.printExpressionTrace("//OperatorCount - end");
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression valueExpr = args[0];
		Expression opExpr = args[1];
		
		Signature valSig = valueExpr.getSignature();
		Signature resSig = JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createValueSignature(ClassTypes.getInstance().getCompilerType(Integer.class), false);
		opExpr.setSignature(resSig);
	}

}

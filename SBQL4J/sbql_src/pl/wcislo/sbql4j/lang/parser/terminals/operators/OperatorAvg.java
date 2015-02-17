package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import java.util.Collection;

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
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorAvg extends Operator {

	public OperatorAvg(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitAvg(this, treeVisitor, opExpr, subExprs);
	};
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		QueryResult result;
		QueryResult objects = interpreter.getQres().pop();
		double resultS =0;
		if(objects instanceof Collection) {
			Collection<QueryResult> section = (Collection<QueryResult>) objects;
			for(QueryResult secEl : section) {
				Number n = (Number) Utils.toSimpleValue(secEl, interpreter.getStore());
				resultS += n.doubleValue();   
			}
			resultS = resultS/(section.size());
		} else {
			resultS = ((Number) (Utils.toSimpleValue(objects, interpreter.getStore()))).doubleValue();
		}
		result = JavaObjectAbstractFactory.getJavaObjectFactory(). 
			createJavaComplexObject(resultS);
		
		interpreter.getQres().push(result);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb; 
//		String identResult = gen.generateIdentifier("result");
//		String identObjects = gen.generateIdentifier("objects");
//		String identResultS = gen.generateIdentifier("resultS");
//		String identSection = gen.generateIdentifier("section");
//		String identSecEl = gen.generateIdentifier("secEl");
//		String identN = gen.generateIdentifier("n");
//		gen.printExpressionTrace("//OperatorAvg - start                                                          \n");
//		sb.append("QueryResult "+identResult+";                                                          \n");
//		sb.append("QueryResult "+identObjects+" = qres.pop();                                            \n");
//		sb.append("double "+identResultS+" = 0;                                                          \n");
//		sb.append("if("+identObjects+" instanceof Collection) {                                          \n");
//		sb.append("	Collection<QueryResult> "+identSection+" = (Collection<QueryResult>) "+identObjects+";        \n");
//		sb.append("	for(QueryResult "+identSecEl+" : "+identSection+") {                                          \n");
//		sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identSecEl+", store);                  \n");
//		sb.append("		"+identResultS+" += "+identN+".doubleValue();                                             \n");
//		sb.append("	}                                                                           \n");
//		sb.append("	"+identResultS+" = "+identResultS+" / ("+identSection+".size());                                         \n");
//		sb.append("} else {                                                                     \n");
//		sb.append("	"+identResultS+" = ((Number) (Utils.toSimpleValue("+identObjects+", store))).doubleValue();   \n");
//		sb.append("}                                                                            \n");
//		sb.append(""+identResult+" = JavaObjectAbstractFactory.getJavaObjectFactory().                   \n");
//		sb.append("	createJavaComplexObject("+identResultS+");                                           \n");
//		sb.append("                                                                             \n");
//		sb.append("qres.push("+identResult+");                                                           \n");
//		gen.printExpressionTrace("//OperatorAvg - end                                                          ");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		Expression opExpr = args[0];
//		Expression argExpr = args[1];
//		
//		
//		StringBuilder sb = gen.sb;
////		String identE1Res = gen.generateIdentifier("e1res");
////		String identColRes = gen.generateIdentifier("colRes");
//		String identAvgSum = gen.generateIdentifier("avgSum");
//		String identArgRes = argExpr.getSignature().getResultName();
//		String identColEl = gen.generateIdentifier("avgEl");
//		String identN = gen.generateIdentifier("n");
//		gen.printExpressionTrace("//OperatorAvg - start \n");
//		if(argExpr.getSignature().sColType != SCollectionType.NO_COLLECTION) {
////			sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
////			sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
//			sb.append("Number "+identAvgSum+" = null; \n");
////			sb.append("try { \n");
//			sb.append("	for (QueryResult "+identColEl+" : "+identArgRes+") { \n");
//			sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identColEl+", store); \n");
//			sb.append("		"+identAvgSum+" = MathUtils.sum("+identAvgSum+", "+identN+"); \n");
//			sb.append("	} \n");
//			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identAvgSum+".doubleValue() / "+identArgRes+".size()); \n");
////			sb.append(" \n");	
////			sb.append("} catch(ClassCastException e) { \n");
////			sb.append("	throw new RuntimeException(\"OperatorSum.eval() invalid type: type should be a primitive value\"); \n");
////			sb.append("} \n");
//		} else {
//			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identArgRes+")); \n");
//		}
//		gen.printExpressionTrace("//OperatorAvg - end");
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		Expression opExpr = args[0];
//		Expression argExpr = args[1];
//		
//		
//		StringBuilder sb = gen.sb;
////		String identE1Res = gen.generateIdentifier("e1res");
////		String identColRes = gen.generateIdentifier("colRes");
//		String identAvgSum = gen.generateIdentifier("avgSum");
//		String identArgRes = argExpr.getSignature().getResultName();
//		String identColEl = gen.generateIdentifier("avgEl");
//		String identN = gen.generateIdentifier("n");
//		gen.printExpressionTrace("//OperatorAvg - start \n");
//		if(argExpr.getSignature().sColType != SCollectionType.NO_COLLECTION) {
////			sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
////			sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
//			sb.append("Number "+identAvgSum+" = null; \n");
////			sb.append("try { \n");
//			sb.append("	for (Number "+identColEl+" : "+identArgRes+") { \n");
////			sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identColEl+", store); \n");
//			sb.append("		"+identAvgSum+" = MathUtils.sum("+identAvgSum+", "+identColEl+"); \n");
//			sb.append("	} \n");
//			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identAvgSum+".doubleValue() / "+identArgRes+".size(); \n");
////			sb.append(" \n");	
////			sb.append("} catch(ClassCastException e) { \n");
////			sb.append("	throw new RuntimeException(\"OperatorSum.eval() invalid type: type should be a primitive value\"); \n");
////			sb.append("} \n");
//		} else {
//			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identArgRes+"; \n");
//		}
//		gen.printExpressionTrace("//OperatorAvg - end \n");
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
		Expression opExpr = args[1];
		
//		Type returnType = ClassTypes.getInstance().getCompilerType(Double.class);
		
		ValueSignature expectedSig = 
			JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createJavaSignature(ClassTypes.getInstance().getCompilerType(Number.class));
		expectedSig.setColType(SCollectionType.BAG);
		
		ValueSignature expectedSig2 = expectedSig.clone();
		expectedSig2.setColType(SCollectionType.SEQUENCE);
		
		boolean isLeftTypeOK = leftExpr.getSignature().isTypeCompatible(expectedSig) || leftExpr.getSignature().isTypeCompatible(expectedSig2);
//		boolean isRightTypeOK = rightExpr.signature.isTypeCompatible(expectedSig) || rightExpr.signature.isTypeCompatible(expectedSig2);
		
		Type returnType = ClassTypes.getInstance().getCompilerType(Double.class);
		
//		Signature s1 = leftExpr.signature.getDerefSignature();
//		if(s1 instanceof ValueSignature) {
//			ValueSignature vs1 = (ValueSignature) s1;
//			if(vs1.isTypeCompatible(Float.class)) {
//				returnType = ClassTypes.getInstance().getCompilerType(Float.class);
//			} else if(vs1.isTypeCompatible(Integer.class)) {
//				returnType = ClassTypes.getInstance().getCompilerType(Integer.class);
//			} else if(vs1.isTypeCompatible(Long.class)) {
//				returnType = ClassTypes.getInstance().getCompilerType(Long.class);
//			}
//		} 
		
//		if(!isLeftTypeOK || !isRightTypeOK) {
		if(!isLeftTypeOK) {
			checker.addError(opExpr, "Collection expected, got "+leftExpr.getSignature());
		}
		ValueSignature vsig = JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createValueSignature(returnType, false);
		opExpr.setSignature(vsig);	
	}

}

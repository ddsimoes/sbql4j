package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
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
import pl.wcislo.sbql4j.model.collections.CollectionResult;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorSum extends Operator {

	public OperatorSum(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitSum(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
			
		QueryResult result;
		
		QueryResult objects = interpreter.getQres().pop();
		CollectionResult colRes = Utils.objectToCollection(objects);
		Number sum = null;
		for(QueryResult qr : colRes) {				
			Number n = (Number)Utils.toSimpleValue(qr, interpreter.getStore());
			sum = MathUtils.sum(sum, n);
		}
		result = javaObjectFactory.createJavaComplexObject(sum);
		interpreter.getQres().push(result);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identColRes = gen.generateIdentifier("colRes");
//		String identSum = gen.generateIdentifier("sum");
//		String identObject = gen.generateIdentifier("object");
//		String identN = gen.generateIdentifier("n");
//		gen.printExpressionTrace("//OperatorSum - start \n");
//		sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
//		sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
//		sb.append("Number "+identSum+" = null; \n");
//		sb.append("try { \n");
//		sb.append("	for (QueryResult "+identObject+" : "+identColRes+") { \n");
//		sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identObject+", store); \n");
//		sb.append("		"+identSum+" = MathUtils.sum("+identSum+", "+identN+"); \n");
//		sb.append("	} \n");
//		sb.append("	qres.push(javaObjectFactory.createJavaComplexObject("+identSum+")); \n");
//		sb.append(" \n");	
//		sb.append("} catch(ClassCastException e) { \n");
//		sb.append("	throw new RuntimeException(\"OperatorSum.eval() invalid type: type should be a primitive value\"); \n");
//		sb.append("} \n");		
//		gen.printExpressionTrace("//OperatorSum - end \n");
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
//		String identSum = gen.generateIdentifier("sum");
//		String identArgRes = argExpr.getSignature().getResultName();
//		String identColEl = gen.generateIdentifier("sumEl");
//		String identN = gen.generateIdentifier("n");
//		gen.printExpressionTrace("//OperatorSum - start \n");
//		if(argExpr.getSignature().sColType != SCollectionType.NO_COLLECTION) {
////			sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
////			sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
//			sb.append("Number "+identSum+" = null; \n");
////			sb.append("try { \n");
//			sb.append("	for (QueryResult "+identColEl+" : "+identArgRes+") { \n");
//			sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identColEl+", store); \n");
//			sb.append("		"+identSum+" = MathUtils.sum("+identSum+", "+identN+"); \n");
//			sb.append("	} \n");
//			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identSum+"); \n");
////			sb.append(" \n");	
////			sb.append("} catch(ClassCastException e) { \n");
////			sb.append("	throw new RuntimeException(\"OperatorSum.eval() invalid type: type should be a primitive value\"); \n");
////			sb.append("} \n");
//		} else {
//			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identArgRes+")); \n");
//		}
//				
//		gen.printExpressionTrace("//OperatorSum - end \n");	
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
//		String identSum = gen.generateIdentifier("sum");
//		String identArgRes = argExpr.getSignature().getResultName();
//		String identColEl = gen.generateIdentifier("sumEl");
//		String identN = gen.generateIdentifier("n");
//		gen.printExpressionTrace("//OperatorSum - start \n");
//		if(argExpr.getSignature().sColType != SCollectionType.NO_COLLECTION) {
////			sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
////			sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
//			sb.append("Number "+identSum+" = null; \n");
////			sb.append("try { \n");
//			sb.append("	for (Number "+identColEl+" : "+identArgRes+") { \n");
////			sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identColEl+", store); \n");
//			sb.append("		"+identSum+" = MathUtils.sum("+identSum+", "+identColEl+"); \n");
//			sb.append("	} \n");
//			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = ("+opExpr.getSignature().getJavaTypeString()+")"+identSum+"; \n");
////			sb.append(" \n");	
////			sb.append("} catch(ClassCastException e) { \n");
////			sb.append("	throw new RuntimeException(\"OperatorSum.eval() invalid type: type should be a primitive value\"); \n");
////			sb.append("} \n");
//		} else {
//			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identArgRes+"; \n");
//		}
//				
//		gen.printExpressionTrace("//OperatorSum - end \n");		
//	}

	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression valueExpr = args[0];
		Expression opExpr = args[1];
		
		ValueSignature expectedSig = 
			JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createJavaSignature(ClassTypes.getInstance().getCompilerType(Object.class));
		expectedSig.setColType(SCollectionType.BAG);
		
		ValueSignature expectedSig2 = expectedSig.clone();
		expectedSig2.setColType(SCollectionType.SEQUENCE);
		
		boolean isLeftTypeOK = valueExpr.getSignature().isTypeCompatible(expectedSig) || valueExpr.getSignature().isTypeCompatible(expectedSig2);
//		boolean isRightTypeOK = rightExpr.signature.isTypeCompatible(expectedSig) || rightExpr.signature.isTypeCompatible(expectedSig2);
	
		Type returnType = ClassTypes.getInstance().getCompilerType(Double.class);
		
		Signature s1 = valueExpr.getSignature().getDerefSignatureWithCardinality();
		if(s1 instanceof ValueSignature) {
			ValueSignature vs1 = (ValueSignature) s1;
			if(vs1.isTypeCompatible(Float.class)) {
				returnType = ClassTypes.getInstance().getCompilerType(Float.class);
			} else if(vs1.isTypeCompatible(Integer.class)) {
				returnType = ClassTypes.getInstance().getCompilerType(Integer.class);
			} else if(vs1.isTypeCompatible(Long.class)) {
				returnType = ClassTypes.getInstance().getCompilerType(Long.class);
			}
		} 
		
//		if(!isLeftTypeOK || !isRightTypeOK) {
		if(!isLeftTypeOK) {
			checker.addError(opExpr, "Collection expected, got "+valueExpr.getSignature());
		}
		ValueSignature vsig = JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createValueSignature(returnType, false);
		opExpr.setSignature(vsig);	
	}
}

package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

public class OperatorMin extends Operator {
	private static final Log log = LogFactory.getLog(OperatorMin.class);
	
	public OperatorMin(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitMin(this, treeVisitor, opExpr, subExprs);
	};
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		QueryResult e1res = interpreter.getQres().pop();
		CollectionResult colRes = Utils.objectToCollection(e1res);
		Number min = null;
		try {
			for (QueryResult object : colRes) {
				Number n = (Number) Utils.toSimpleValue(object, interpreter.getStore());
				min = MathUtils.min(min, n);
			}
			interpreter.getQres().push(javaObjectFactory.createJavaComplexObject(min));
		}catch(ClassCastException e){
			throw new RuntimeException("OperatorMin.eval() invalid type: type should be a primitive value");
		}
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identColRes = gen.generateIdentifier("colRes");
//		String identMin = gen.generateIdentifier("min");
//		String identObject = gen.generateIdentifier("object");
//		String identN = gen.generateIdentifier("n");
//		gen.printExpressionTrace("//OperatorMin - start \n");
//		sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
//		sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
//		sb.append("Number "+identMin+" = null; \n");
//		sb.append("try { \n");
//		sb.append("	for (QueryResult "+identObject+" : "+identColRes+") { \n");
//		sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identObject+", store); \n");
//		sb.append("		"+identMin+" = MathUtils.min("+identMin+", "+identN+"); \n");
//		sb.append("	} \n");
//		sb.append("	qres.push(javaObjectFactory.createJavaComplexObject("+identMin+")); \n");
//		sb.append(" \n");	
//		sb.append("} catch(ClassCastException e) { \n");
//		sb.append("	throw new RuntimeException(\"OperatorMax.eval() invalid type: type should be a primitive value\"); \n");
//		sb.append("} \n");		
//		gen.printExpressionTrace("//OperatorMin - end \n");
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
//		String identMin = gen.generateIdentifier("min");
//		String identArgRes = argExpr.getSignature().getResultName();
//		String identColEl = gen.generateIdentifier("minEl");
//		String identN = gen.generateIdentifier("n");
//		gen.printExpressionTrace("//OperatorMin - start \n");
//		if(argExpr.getSignature().sColType != SCollectionType.NO_COLLECTION) {
////			sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
////			sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
//			sb.append("Number "+identMin+" = null; \n");
////			sb.append("try { \n");
//			sb.append("	for (QueryResult "+identColEl+" : "+identArgRes+") { \n");
//			sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identColEl+", store); \n");
//			sb.append("		"+identMin+" = MathUtils.min("+identMin+", "+identN+"); \n");
//			sb.append("	} \n");
//			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identMin+"); \n");
////			sb.append(" \n");	
////			sb.append("} catch(ClassCastException e) { \n");
////			sb.append("	throw new RuntimeException(\"OperatorSum.eval() invalid type: type should be a primitive value\"); \n");
////			sb.append("} \n");
//		} else {
//			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identArgRes+")); \n");
//		}
//		gen.printExpressionTrace("//OperatorMin - end \n");	
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
//		String identMin = gen.generateIdentifier("min");
//		String identArgRes = argExpr.getSignature().getResultName();
//		String identColEl = gen.generateIdentifier("minEl");
//		String identN = gen.generateIdentifier("n");
//		gen.printExpressionTrace("//OperatorMin - start \n");
//		if(argExpr.getSignature().sColType != SCollectionType.NO_COLLECTION) {
////			sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
////			sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
//			sb.append("Number "+identMin+" = null; \n");
////			sb.append("try { \n");
//			sb.append("	for (Number "+identColEl+" : "+identArgRes+") { \n");
////			sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identColEl+", store); \n");
//			sb.append("		"+identMin+" = MathUtils.min("+identMin+", "+identColEl+"); \n");
//			sb.append("	} \n");
//			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = ("+opExpr.getSignature().getJavaTypeString()+")"+identMin+"; \n");
////			sb.append(" \n");	
////			sb.append("} catch(ClassCastException e) { \n");
////			sb.append("	throw new RuntimeException(\"OperatorSum.eval() invalid type: type should be a primitive value\"); \n");
////			sb.append("} \n");
//		} else {
//			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identArgRes+"; \n");
//		}
//		gen.printExpressionTrace("//OperatorMin - end \n");		
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression valueExpr = args[0];
		Expression opExpr = args[1];
		
		Signature valSig = valueExpr.getSignature();
		Type resType = ClassTypes.getInstance().getCompilerType(Double.class);
		if(valSig instanceof ValueSignature) {
			resType = ((ValueSignature)valSig).getType();
		}
		
		Signature resSig = JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createValueSignature(resType, false);
		opExpr.setSignature(resSig);
	}
}

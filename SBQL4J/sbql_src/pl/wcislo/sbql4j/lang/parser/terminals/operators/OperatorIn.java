package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import java.util.Iterator;

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
import pl.wcislo.sbql4j.util.Utils;

public class OperatorIn extends Operator {

	public OperatorIn(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitIn(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		CollectionResult e2res = Utils.objectToCollection(interpreter.getQres().pop());
		CollectionResult e1res = Utils.objectToCollection(interpreter.getQres().pop());
		boolean result = true;
		for(Iterator<QueryResult> i1 = e1res.iterator(); result && i1.hasNext(); ) {
			QueryResult t1 = i1.next();
			boolean r2 = false;
			for(Iterator<QueryResult> i2 = e2res.iterator(); !r2 && i2.hasNext(); ) {
				QueryResult t2 = i2.next();
				if(Utils.equalsWithDeref(t1, t2, interpreter.getStore())) {
					r2 = true;
				}
			}
			if(!r2) {
				result = false;
			}
		}		 		
		interpreter.getQres().push(javaObjectFactory.createJavaComplexObject(result));
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identE2Res = gen.generateIdentifier("e2res");
//		String identResult = gen.generateIdentifier("result");
//		String identI1 = gen.generateIdentifier("i1");
//		String identI2 = gen.generateIdentifier("i2");
//		String identT1 = gen.generateIdentifier("t1");
//		String identT2 = gen.generateIdentifier("t2");
//		String identR2 = gen.generateIdentifier("r2");
//		gen.printExpressionTrace("//OperatorIn - start \n");
//		sb.append("CollectionResult "+identE2Res+" = Utils.objectToCollection(qres.pop()); \n");
//		sb.append("CollectionResult "+identE1Res+" = Utils.objectToCollection(qres.pop()); \n");
//		sb.append("boolean "+identResult+" = true; \n");
//		sb.append("for(Iterator<QueryResult> "+identI1+" = "+identE1Res+".iterator(); "+identResult+" && "+identI1+".hasNext(); ) { \n");
//		sb.append("	QueryResult "+identT1+" = "+identI1+".next(); \n");
//		sb.append("	boolean "+identR2+" = false; \n");
//		sb.append("	for(Iterator<QueryResult> "+identI2+" = "+identE2Res+".iterator(); !"+identR2+" && "+identI2+".hasNext(); ) { \n");
//		sb.append("		QueryResult "+identT2+" = "+identI2+".next(); \n");
//		sb.append("		if(Utils.equalsWithDeref("+identT1+", "+identT2+", store)) { \n");
//		sb.append("			"+identR2+" = true; \n");
//		sb.append("		} \n");
//		sb.append("	} \n");
//		sb.append("	if(!"+identR2+") { \n");
//		sb.append("		"+identResult+" = false; \n");
//		sb.append("	} \n");
//		sb.append("} \n");		 		
//		sb.append("qres.push(javaObjectFactory.createJavaComplexObject("+identResult+")); \n");
//		gen.printExpressionTrace("//OperatorIn - end \n");
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
//		Signature leftSig = leftExpr.getSignature();
//		Signature rightSig = rightExpr.getSignature();
//		Signature opSig = opExpr.getSignature();
//		
//		boolean isLeftCol = leftSig.sColType != SCollectionType.NO_COLLECTION;
//		boolean isRightCol = rightSig.sColType != SCollectionType.NO_COLLECTION;
//		
//		String identLeftRes = leftSig.getResultName();
//		String identRightRes = rightSig.getResultName();
//		
//		gen.printExpressionTrace("//OperatorIn - start \n");
//		if(isLeftCol && isRightCol) {
//			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaObject("+identRightRes+".containsAll("+identLeftRes+")); \n");
//		} else {
//			String identLeftCol = gen.generateIdentifier("inLeftCol");
//			String identRightCol = gen.generateIdentifier("inRightCol");
//			sb.append("CollectionResult "+identLeftCol+" = objectToCollection("+identLeftRes+"); \n");
//			sb.append("CollectionResult "+identRightCol+" = objectToCollection("+identRightRes+"); \n");
//			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaObject("+identRightCol+".containsAll("+identLeftCol+")); \n");
//		}
//		gen.printExpressionTrace("//OperatorIn - end \n");	
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
//		Signature leftSig = leftExpr.getSignature();
//		Signature rightSig = rightExpr.getSignature();
//		Signature opSig = opExpr.getSignature();
//		
//		boolean isLeftCol = leftSig.sColType != SCollectionType.NO_COLLECTION;
//		boolean isRightCol = rightSig.sColType != SCollectionType.NO_COLLECTION;
//		
//		String identLeftRes = leftSig.getResultName();
//		String identRightRes = rightSig.getResultName();
//		
//		gen.printExpressionTrace("//OperatorIn - start \n");
//		if(isLeftCol && isRightCol) {
//			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identRightRes+".containsAll("+identLeftRes+"); \n");
//		} else {
//			String identLeftCol = gen.generateIdentifier("inLeftCol");
//			String identRightCol = gen.generateIdentifier("inRightCol");
//			if(!isLeftCol) {
//				sb.append("Collection "+identLeftCol+" = new ArrayList(); \n");
//				sb.append(identLeftCol+".add("+identLeftRes+"); \n");
//			} else {
//				sb.append("Collection "+identLeftCol+" = new ArrayList("+identLeftRes+"); \n");
//			}
//			if(!isRightCol) {
//				sb.append("Collection "+identRightCol+" = new ArrayList(); \n");
//				sb.append(identRightCol+".add("+identRightRes+"); \n");
//			} else {
//				sb.append("Collection "+identRightCol+" = new ArrayList("+identRightRes+"); \n");
//			}
//			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identRightCol+".containsAll("+identLeftCol+"); \n");
//		}
//		gen.printExpressionTrace("//OperatorIn - end \n");		
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression leftExpr = args[0];
		Expression rightExpr = args[1];
		Expression inExpression = args[2];
		ValueSignature sig = JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createValueSignature(ClassTypes.getInstance().getCompilerType(Boolean.class), false);
		inExpression.setSignature(sig);
	}
}


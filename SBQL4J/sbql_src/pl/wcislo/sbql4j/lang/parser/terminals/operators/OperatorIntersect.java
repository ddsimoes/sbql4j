package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import org.apache.commons.collections.CollectionUtils;

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
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.model.collections.CollectionResult;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorIntersect extends Operator {

	public OperatorIntersect(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitIntersect(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		CollectionResult e2res = Utils.objectToCollection(interpreter.getQres().pop());
		CollectionResult e1res = Utils.objectToCollection(interpreter.getQres().pop());
		Bag result = new Bag();
		result.addAll(CollectionUtils.intersection(e1res, e2res));
		interpreter.getQres().push(result);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identE2Res = gen.generateIdentifier("e2res");
//		String identResult = gen.generateIdentifier("result");
//		
//		gen.printExpressionTrace("//OperatorIntersect - start \n");
//		sb.append("CollectionResult "+identE2Res+" = Utils.objectToCollection(qres.pop()); \n");
//		sb.append("CollectionResult "+identE1Res+" = Utils.objectToCollection(qres.pop()); \n");
//		sb.append("Bag "+identResult+" = new Bag(); \n");
//		sb.append(""+identResult+".addAll(CollectionUtils.intersection("+identE1Res+", "+identE2Res+")); \n");
//		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorIntersect - end \n");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		StringBuilder sb = gen.sb;
//		String identResult = opExpr.getSignature().getResultName();
//				
//		Signature sLeft = leftExpr.getSignature();
//		Signature sRight = rightExpr.getSignature();
//		
//		String identERight = sRight.getResultName();
//		String identELeft = sLeft.getResultName();
//		
//		gen.printExpressionTrace("//OperatorIntersect - start \n");
//		sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = new "+opExpr.getSignature().sColType.genSBQLDeclCode()+"(); \n");
//		sb.append(""+identResult+".addAll(CollectionUtils.intersection(");
//		if(sLeft.sColType != SCollectionType.NO_COLLECTION) {
//			sb.append(identELeft);
//		} else {
//			sb.append("Utils.objectToCollection("+identELeft+")");
//		}
//		sb.append(", ");
//		if(sRight.sColType != SCollectionType.NO_COLLECTION) {
//			sb.append(identERight);
//		} else {
//			sb.append("Utils.objectToCollection("+identERight+")");
//		}
//		sb.append(")); \n");
//		gen.printExpressionTrace("//OperatorIntersect - end \n");	
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		String identResult = opExpr.getSignature().getResultName();
//				
//		Signature sLeft = leftExpr.getSignature();
//		Signature sRight = rightExpr.getSignature();
//		
//		String identERight = sRight.getResultName();
//		String identELeft = sLeft.getResultName();
//		
//		String identLeftCol = gen.generateIdentifier("minusLeftCol");
//		String identRightCol = gen.generateIdentifier("minusRightCol");
//		
//		boolean isLeftCol = sLeft.sColType != SCollectionType.NO_COLLECTION;
//		boolean isRightCol = sRight.sColType != SCollectionType.NO_COLLECTION;
//		
//		gen.printExpressionTrace("//OperatorIntersect - start \n");
//		sb.append("Collection "+identLeftCol+" = ");
//		if(isLeftCol) {
//			sb.append("new ArrayList("+identELeft+"); \n");
//		} else {
//			sb.append("new ArrayList(); \n");
//			sb.append(identLeftCol+".add("+identELeft+"); \n");
//		}
//		sb.append("Collection "+identRightCol+" = ");
//		if(isRightCol) {
//			sb.append("new ArrayList("+identERight+"); \n");
//		} else {
//			sb.append("new ArrayList(); \n");
//			sb.append(identRightCol+".add("+identERight+"); \n");
//		}
//		sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = new "+opExpr.getSignature().getJavaTypeStringAssigment()+"(); \n");
//		sb.append(""+identResult+".addAll(CollectionUtils.intersection("+identLeftCol+", "+identRightCol+")); \n");
//		gen.printExpressionTrace("//OperatorIntersect - end \n");	
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression leftExpr = args[0];
		Expression rightExpr = args[1];
		Expression opExpr = args[2];
		ClassTypes cTypes = ClassTypes.getInstance();
		Type leftType = ((ValueSignature)leftExpr.getSignature()).getType();
		Type rightType = ((ValueSignature)rightExpr.getSignature()).getType();
		Type returnType = cTypes.getSharedAncestor(leftType, rightType);
//		
////		if(leftType != rightType) {
////			checker.addError(opExpr, "Heterogenic unions are not allowed. Types: left="+leftType+" right="+rightType);
////			returnType = Object.class;
////		}
		ValueSignature vsig = JavaSignatureAbstractFactory.getJavaSignatureFactory().createJavaSignature(returnType);
		
		SCollectionType leftCType = leftExpr.getSignature().getColType();
		SCollectionType rightCType = rightExpr.getSignature().getColType();
		SCollectionType cType = SCollectionType.getWorseSCType(leftCType, rightCType);
//		if(leftCType == SCollectionType.NO_COLLECTION && rightCType == SCollectionType.NO_COLLECTION) {
//			cType = SCollectionType.NO_COLLECTION;
//		} else {
//			cType = SCollectionType.BAG;
//		}
		vsig.setColType(cType);
		opExpr.setSignature(vsig);
	}
}

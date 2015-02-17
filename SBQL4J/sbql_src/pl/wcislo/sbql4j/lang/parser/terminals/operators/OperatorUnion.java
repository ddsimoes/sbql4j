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
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.model.collections.CollectionResult;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorUnion extends Operator {

	public OperatorUnion(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitUnion(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		Bag eres = new Bag();
		CollectionResult eRight = Utils.objectToCollection(interpreter.getQres().pop());
		CollectionResult eLeft = Utils.objectToCollection(interpreter.getQres().pop());
		eres.addAll(eLeft);
		eres.addAll(eRight);		
		interpreter.getQres().push(eres);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identEres = gen.generateIdentifier("eres");
//		String identERight = gen.generateIdentifier("eRight");
//		String identELeft = gen.generateIdentifier("eLeft");
//		gen.printExpressionTrace("//OperatorUnion - start \n");
//		sb.append("Bag "+identEres+" = new Bag(); \n");
//		sb.append("CollectionResult "+identERight+" = Utils.objectToCollection(qres.pop()); \n");
//		sb.append("CollectionResult "+identELeft+" = Utils.objectToCollection(qres.pop()); \n");
//		sb.append(""+identEres+".addAll("+identELeft+"); \n");
//		sb.append(""+identEres+".addAll("+identERight+"); \n");		
//		sb.append("qres.push("+identEres+"); \n");
//		gen.printExpressionTrace("//OperatorUnion - end \n");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		Signature sLeft = leftExpr.getSignature();
//		Signature sRight = rightExpr.getSignature();
//		
//		StringBuilder sb = gen.sb;
//		String identEres = opExpr.getSignature().getResultName();
//		String identERight = sRight.getResultName();
//		String identELeft = sLeft.getResultName();
//		gen.printExpressionTrace("//OperatorUnion - start \n");
////		sb.append("Bag "+identEres+" = new Bag(); \n");
//		sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = new "+opExpr.getSignature().sColType.genSBQLDeclCode()+"(); \n");
//		if(sLeft.sColType != SCollectionType.NO_COLLECTION) {
//			sb.append(""+identEres+".addAll("+identELeft+"); \n");
//		} else {
//			sb.append(""+identEres+".add("+identELeft+"); \n");
//		}
//		if(sRight.sColType != SCollectionType.NO_COLLECTION) {
//			sb.append(""+identEres+".addAll("+identERight+"); \n");
//		} else {
//			sb.append(""+identEres+".add("+identERight+"); \n");
//		}
////		sb.append("CollectionResult "+identELeft+" = Utils.objectToCollection(qres.pop()); \n");
////		sb.append(""+identEres+".addAll("+identELeft+"); \n");
////		sb.append(""+identEres+".addAll("+identERight+"); \n");		
////		sb.append("qres.push("+identEres+"); \n");
//		gen.printExpressionTrace("//OperatorUnion - end \n");	
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		Signature sLeft = leftExpr.getSignature();
//		Signature sRight = rightExpr.getSignature();
//		
//		StringBuilder sb = gen.sb;
//		String identEres = opExpr.getSignature().getResultName();
//		String identERight = sRight.getResultName();
//		String identELeft = sLeft.getResultName();
//		gen.printExpressionTrace("//OperatorUnion - start \n");
////		sb.append("Bag "+identEres+" = new Bag(); \n");
//		sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = new "+opExpr.getSignature().getJavaTypeStringAssigment()+"(); \n");
//		if(sLeft.sColType != SCollectionType.NO_COLLECTION) {
//			sb.append(""+identEres+".addAll("+identELeft+"); \n");
//		} else {
//			sb.append(""+identEres+".add("+identELeft+"); \n");
//		}
//		if(sRight.sColType != SCollectionType.NO_COLLECTION) {
//			sb.append(""+identEres+".addAll("+identERight+"); \n");
//		} else {
//			sb.append(""+identEres+".add("+identERight+"); \n");
//		}
////		sb.append("CollectionResult "+identELeft+" = Utils.objectToCollection(qres.pop()); \n");
////		sb.append(""+identEres+".addAll("+identELeft+"); \n");
////		sb.append(""+identEres+".addAll("+identERight+"); \n");		
////		sb.append("qres.push("+identEres+"); \n");
//		gen.printExpressionTrace("//OperatorUnion - end \n");		
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
		SCollectionType cType;
		SCollectionType leftCType = leftExpr.getSignature().getColType();
		SCollectionType rightCType = rightExpr.getSignature().getColType();
		cType = SCollectionType.getUnionSCType(leftCType, rightCType);
		vsig.setColType(cType);
		opExpr.setSignature(vsig);
	}
	
}


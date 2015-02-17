package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import java.util.LinkedHashSet;
import java.util.Set;

import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
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
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.model.collections.CollectionResult;
import pl.wcislo.sbql4j.model.collections.Sequence;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorUnique extends Operator {

	public OperatorUnique(OperatorType type) {
		super(type);
	}
	
	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitUnique(this, treeVisitor, opExpr, subExprs);
	};	

	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		QueryResult objects = interpreter.getQres().pop();
		objects = Utils.objectToCollection(objects);
		QueryResult result = null;
		if (objects instanceof Bag) {
			result = new Bag();
		} else if (objects instanceof Sequence) {
			result = new Sequence();
		}
		Set<QueryResult> tmp = new LinkedHashSet<QueryResult>();
		tmp.addAll((CollectionResult) objects);
		((CollectionResult) result).addAll(tmp);
		interpreter.getQres().push(result);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identObjects = gen.generateIdentifier("objects");
//		String identResult = gen.generateIdentifier("result");
//		String identTmp = gen.generateIdentifier("tmp");
//		
//		gen.printExpressionTrace("//OperatorUnique - start \n");
//		sb.append("QueryResult "+identObjects+" = qres.pop(); \n"); 
//		sb.append(""+identObjects+" = Utils.objectToCollection("+identObjects+"); \n");
//		sb.append("QueryResult "+identResult+" = null; \n");
//        sb.append(" \n");
//		sb.append("if ("+identObjects+" instanceof Bag) { \n");
//		sb.append("	"+identResult+" = new Bag(); \n");
//		sb.append("} else if ("+identObjects+" instanceof Sequence) { \n");
//		sb.append("	"+identResult+" = new Sequence(); \n");
//		sb.append("} \n");
//		sb.append("Set<QueryResult> "+identTmp+" = new LinkedHashSet<QueryResult>(); \n");
//		sb.append(""+identTmp+".addAll((CollectionResult) "+identObjects+"); \n");
//		sb.append("((CollectionResult) "+identResult+").addAll("+identTmp+"); \n");
//		sb.append("qres.push("+identResult+"); \n");
//		gen.printExpressionTrace("//OperatorUnique - end \n");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		Expression opExpr = args[0];
//		Expression rightExpr = args[1];
//		
//		
//		StringBuilder sb = gen.sb;
//		String identObjects = gen.generateIdentifier("objects");
//		String identResult = opExpr.getSignature().getResultName();
//		String identTmp = gen.generateIdentifier("tmp");
//		String identRightRes = rightExpr.getSignature().getResultName();
//		
//		gen.printExpressionTrace("//OperatorUnique - start \n");
//		if(opExpr.getSignature().sColType == SCollectionType.NO_COLLECTION) {
//			//only one element - no changes
//			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = "+identRightRes+"; \n");
//		} else {
//			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = new "+opExpr.getSignature().sColType.genSBQLDeclCode()+"(); \n");
////			sb.append("QueryResult "+identObjects+" = qres.pop(); \n"); 
////			sb.append(""+identObjects+" = Utils.objectToCollection("+identObjects+"); \n");
////			sb.append("QueryResult "+identResult+" = null; \n");
////	        sb.append(" \n");
////			sb.append("if ("+identObjects+" instanceof Bag) { \n");
////			sb.append("	"+identResult+" = new Bag(); \n");
////			sb.append("} else if ("+identObjects+" instanceof Sequence) { \n");
////			sb.append("	"+identResult+" = new Sequence(); \n");
////			sb.append("} \n");
//			sb.append("Set<QueryResult> "+identTmp+" = new LinkedHashSet<QueryResult>(); \n");
//			sb.append(""+identTmp+".addAll((CollectionResult) "+identRightRes+"); \n");
//			sb.append(""+identResult+".addAll("+identTmp+"); \n");
////			sb.append("qres.push("+identResult+"); \n");
//		}
//		gen.printExpressionTrace("//OperatorUnique - end \n");	
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		Expression opExpr = args[0];
//		Expression rightExpr = args[1];
//		
//		
//		StringBuilder sb = gen.sb;
//		String identObjects = gen.generateIdentifier("objects");
//		String identResult = opExpr.getSignature().getResultName();
//		String identTmp = gen.generateIdentifier("tmp");
//		String identRightRes = rightExpr.getSignature().getResultName();
//		String rightElType = rightExpr.getSignature().getJavaTypeStringSingleResult();
//		
//		gen.printExpressionTrace("//OperatorUnique - start \n");
//		if(opExpr.getSignature().sColType == SCollectionType.NO_COLLECTION) {
//			//only one element - no changes
//			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identRightRes+"; \n");
//		} else {
//			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = new "+opExpr.getSignature().getJavaTypeStringAssigment()+"(); \n");
////			sb.append("QueryResult "+identObjects+" = qres.pop(); \n"); 
////			sb.append(""+identObjects+" = Utils.objectToCollection("+identObjects+"); \n");
////			sb.append("QueryResult "+identResult+" = null; \n");
////	        sb.append(" \n");
////			sb.append("if ("+identObjects+" instanceof Bag) { \n");
////			sb.append("	"+identResult+" = new Bag(); \n");
////			sb.append("} else if ("+identObjects+" instanceof Sequence) { \n");
////			sb.append("	"+identResult+" = new Sequence(); \n");
////			sb.append("} \n");
//			sb.append("Set<"+rightElType+"> "+identTmp+" = new LinkedHashSet<"+rightElType+">(); \n");
//			sb.append(""+identTmp+".addAll( "+identRightRes+"); \n");
//			sb.append(""+identResult+".addAll("+identTmp+"); \n");
////			sb.append("qres.push("+identResult+"); \n");
//		}
//		gen.printExpressionTrace("//OperatorUnique - end \n");		
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression expr = args[0];
		Expression opExpr = args[1];
		
		Signature sig = expr.getSignature();
		Signature resSig = sig.clone();
		opExpr.setSignature(resSig);
	}
}

package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
import pl.wcislo.sbql4j.model.StructSBQL;
import pl.wcislo.sbql4j.model.collections.CollectionResult;

public class OperatorStruct extends Operator {
	private static final Log log = LogFactory.getLog(OperatorStruct.class);
	
	
	public OperatorStruct(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitStruct(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		StructSBQL eres = new StructSBQL();
		QueryResult e1res = interpreter.getQres().pop();
		StructSBQL s;
		if(e1res instanceof StructSBQL) {
			s = (StructSBQL) e1res;
		} else {
			s = new StructSBQL();
			if(e1res instanceof CollectionResult) {
				s.addAll((CollectionResult) e1res);
			} else {
				s.add(e1res);
			}
			
		}
		eres.addAll(s);
		interpreter.getQres().push(eres);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identEres = gen.generateIdentifier("eres");
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identS = gen.generateIdentifier("s");
//		gen.printExpressionTrace("//OperatorStruct - start \n");
//		sb.append("Struct "+identEres+" = new Struct(); \n");
//		sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
//		sb.append("Struct "+identS+"; \n");
//		sb.append("if("+identE1Res+" instanceof Struct) { \n");
//		sb.append("	"+identS+" = (Struct) "+identE1Res+"; \n");
//		sb.append("} else { \n");
//		sb.append("	"+identS+" = new Struct(); \n");
//		sb.append("	if("+identE1Res+" instanceof CollectionResult) { \n");
//		sb.append("		"+identS+".addAll((CollectionResult) "+identE1Res+"); \n");
//		sb.append("	} else { \n");
//		sb.append("		"+identS+".add("+identE1Res+"); \n");
//		sb.append("	} \n");
//		sb.append(" \n");	
//		sb.append("} \n");
//		sb.append("eres.addAll("+identS+"); \n");
//		sb.append("qres.push("+identEres+"); \n");
//		gen.printExpressionTrace("//OperatorStruct - end \n");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		
//		Expression opExpr = args[0];
//		Expression argExpr = args[1];
//		Expression genExpr = args[2];
//		
//		String identEres = opExpr.getSignature().getResultName();
//		String identArgRes = argExpr.getSignature().getResultName();
////		String identE1Res = gen.generateIdentifier("e1res");
//		String identS = gen.generateIdentifier("s");
//		gen.printExpressionTrace("//OperatorStruct - start \n");
//		sb.append("Struct "+identEres+" = new Struct(); \n");
////		sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
//		sb.append("Struct "+identS+"; \n");
//		sb.append("if("+identArgRes+" instanceof Struct) { \n");
//		sb.append("	"+identS+" = (Struct) "+identArgRes+"; \n");
//		sb.append("} else { \n");
//		sb.append("	"+identS+" = new Struct(); \n");
//		sb.append("	if("+identArgRes+" instanceof CollectionResult) { \n");
//		sb.append("		"+identS+".addAll((CollectionResult) "+identArgRes+"); \n");
//		sb.append("	} else { \n");
//		sb.append("		"+identS+".add("+identArgRes+"); \n");
//		sb.append("	} \n");
//		sb.append(" \n");	
//		sb.append("} \n");
//		sb.append("eres.addAll("+identS+"); \n");
////		sb.append("qres.push("+identEres+"); \n");
//		gen.printExpressionTrace("//OperatorStruct - end \n");	
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		
//		Expression opExpr = args[0];
//		Expression argExpr = args[1];
//		Expression genExpr = args[2];
//		
//		String identEres = opExpr.getSignature().getResultName();
//		String identArgRes = argExpr.getSignature().getResultName();
////		String identE1Res = gen.generateIdentifier("e1res");
//		String identS = gen.generateIdentifier("s");
//		gen.printExpressionTrace("//OperatorStruct - start \n");
//		sb.append("Struct "+identEres+" = new Struct(); \n");
////		sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
//		sb.append("Struct "+identS+"; \n");
//		sb.append("if("+identArgRes+" instanceof Struct) { \n");
//		sb.append("	"+identS+" = (Struct) "+identArgRes+"; \n");
//		sb.append("} else { \n");
//		sb.append("	"+identS+" = new Struct(); \n");
//		sb.append("	if("+identArgRes+" instanceof CollectionResult) { \n");
//		sb.append("		"+identS+".addAll((CollectionResult) "+identArgRes+"); \n");
//		sb.append("	} else { \n");
//		sb.append("		"+identS+".add("+identArgRes+"); \n");
//		sb.append("	} \n");
//		sb.append(" \n");	
//		sb.append("} \n");
//		sb.append("eres.addAll("+identS+"); \n");
////		sb.append("qres.push("+identEres+"); \n");
//		gen.printExpressionTrace("//OperatorStruct - end \n");		
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		// TODO Auto-generated method stub
		
	}

}

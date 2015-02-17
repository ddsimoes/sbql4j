package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import java.util.Collection;
import java.util.List;

import pl.wcislo.sbql4j.exception.SBQLException;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.StructSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.runtime.JavaClass;
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
import pl.wcislo.sbql4j.model.collections.Sequence;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorSequence extends Operator {

	public OperatorSequence(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitSequence(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		Sequence eres = null;
		if(args.length < 1) {
			eres = new Sequence();
		} else {
			//mamy generyczna sekwencje - args[1] okresla jej typ
			Expression bagTypeExpr = args[1];
			bagTypeExpr.accept(interpreter, null);
			QueryResult qr = interpreter.getQres().pop();
			QueryResult clazz = Utils.collectionToObject(qr);
			JavaClass<List> jc = (JavaClass<List>)clazz;
			try {
				List c = jc.value.newInstance();
				eres = new Sequence<List<QueryResult>>(c);
			} catch (Exception e) {
				throw new SBQLException(e);
			}
		}
		QueryResult e1res = interpreter.getQres().pop();
		
		StructSBQL s;
		if(e1res instanceof StructSBQL) {
			s = (StructSBQL) e1res;
		} else {
			s = new StructSBQL();
			if(e1res instanceof Collection) {
				s.addAll((Collection) e1res);
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
//
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identS = gen.generateIdentifier("s");
//
//		gen.printExpressionTrace("//OperatorSequence - start \n");
//		sb.append("Sequence " + identEres + " = null;                                                		 \n");
//		if (args.length < 1) {
//			sb.append("	" + identEres + " = new Sequence();                                                \n");
//		} else {
//			sb.append("	//mamy generyczna sekwencje - args[1] okresla jej typ                    \n");
//			Expression sequenceTypeExpr = args[1];
//			sequenceTypeExpr.accept(gen, null);
//			String identQr = gen.generateIdentifier("qr");
//			String identClazz = gen.generateIdentifier("clazz");
//			String identJc = gen.generateIdentifier("jc");
//			String identC = gen.generateIdentifier("c");
//
//			sb.append("	                                                                     \n");
//			sb.append("	QueryResult " + identQr + " = qres.pop();                                         \n");
//			sb.append("	QueryResult " + identClazz + " = Utils.collectionToObject(" + identQr + ");                    \n");
//			sb.append("	JavaClass<List> " + identJc + " = (JavaClass<List>) " + identClazz + ";             \n");
//			sb.append("	try {                                                                \n");
//			sb.append("		List " + identC + " = " + identJc + ".value.newInstance();                           \n");
//			sb.append("		" + identEres + " = new Bag<List<QueryResult>>(" + identC + ");                      \n");
//			sb.append("	} catch (Exception e) {                                              \n");
//			sb.append("		throw new SBQLException(e);                                             \n");
//			sb.append("	}                                                                    \n");
//		}
//		sb.append("QueryResult " + identE1Res + " = qres.pop();                                        \n");
//		sb.append("Struct " + identS + ";                                                              \n");
//		sb.append("if(" + identE1Res + " instanceof Struct) {                                          \n");
//		sb.append("	" + identS + " = (Struct) " + identE1Res + ";                                                   \n");
//		sb.append("} else {                                                               \n");
//		sb.append("	" + identS + " = new Struct();                                                     \n");
//		sb.append("	if(" + identE1Res + " instanceof Collection) {                                     \n");
//		sb.append("		" + identS + ".addAll((Collection) " + identE1Res + ");                                     \n");
//		sb.append("	} else {                                                              \n");
//		sb.append("		" + identS + ".add(" + identE1Res + ");                                                     \n");
//		sb.append("	}                                                                     \n");
//		sb.append("}                                                                      \n");
//		sb.append("" + identEres + ".addAll(" + identS + ");                                                        \n");
//		sb.append("qres.push(" + identEres + ");                                                       \n");
//		gen.printExpressionTrace("//OperatorSequence - end \n");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		Expression opExpr = args[0];
//		Expression argExpr = args[1];
//		Expression genExpr = args[2];
//		
//		StringBuilder sb = gen.sb;
//		String identEres = opExpr.getSignature().getResultName();
//
//		gen.printExpressionTrace("//OperatorSequence - start");
//		sb.append("Sequence " + identEres + "; \n");
//		if (genExpr == null) {
//			sb.append("	" + identEres + " = new Sequence();                                                \n");
//		} else {
//			//generic bag
//			genExpr.accept(gen, null);
//			String identGenExprRes = genExpr.getSignature().getResultName();
//			String identJc = gen.generateIdentifier("jc");
//			String identC = gen.generateIdentifier("c");
//			sb.append("	JavaClass<Collection> " + identJc + " = (JavaClass<Collection>) " + identGenExprRes + "; \n");
//			sb.append("	try { \n");
//			sb.append("		Collection " + identC + " = " + identJc + ".value.newInstance(); \n");
//			sb.append("		" + identEres + " = new Sequence<Collection<QueryResult>>(" + identC + "); \n");
//			sb.append("	} catch (Exception e) { \n");
//			sb.append("		throw new SBQLException(e); \n");
//			sb.append("	} \n");
//		}
//		String identArgRes = argExpr.getSignature().getResultName();
//		if(argExpr.getSignature() instanceof StructSignature || argExpr.getSignature().sColType != SCollectionType.NO_COLLECTION) {
//			sb.append("" + identEres + ".addAll(" + identArgRes + "); \n");
//		} else {
//			sb.append("" + identEres + ".add(" + identArgRes + "); \n");
//		}
//		gen.printExpressionTrace("//OperatorSequence - end");
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		Expression opExpr = args[0];
//		Expression argExpr = args[1];
//		Expression genExpr = args[2];
//		
//		StringBuilder sb = gen.sb;
//		String identEres = opExpr.getSignature().getResultName();
//
//		gen.printExpressionTrace("//OperatorSequence - start");
//		sb.append("Sequence " + identEres + "; \n");
//		if (genExpr == null) {
//			sb.append("	" + identEres + " = new Sequence();                                                \n");
//		} else {
//			//generic bag
//			genExpr.accept(gen, null);
//			String identGenExprRes = genExpr.getSignature().getResultName();
//			String identJc = gen.generateIdentifier("jc");
//			String identC = gen.generateIdentifier("c");
//			sb.append("	JavaClass<Collection> " + identJc + " = (JavaClass<Collection>) " + identGenExprRes + "; \n");
//			sb.append("	try { \n");
//			sb.append("		Collection " + identC + " = " + identJc + ".value.newInstance(); \n");
//			sb.append("		" + identEres + " = new Sequence<Collection<QueryResult>>(" + identC + "); \n");
//			sb.append("	} catch (Exception e) { \n");
//			sb.append("		throw new SBQLException(e); \n");
//			sb.append("	} \n");
//		}
//		String identArgRes = argExpr.getSignature().getResultName();
//		if(argExpr.getSignature() instanceof StructSignature || argExpr.getSignature().sColType != SCollectionType.NO_COLLECTION) {
//			sb.append("" + identEres + ".addAll(" + identArgRes + "); \n");
//		} else {
//			sb.append("" + identEres + ".add(" + identArgRes + "); \n");
//		}
//		gen.printExpressionTrace("//OperatorSequence - end");	
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Signature sig = args[0].getSignature();
		Expression currentExpr = args[1];
		Signature res = sig.clone();
		res.setColType(SCollectionType.SEQUENCE);
		currentExpr.setSignature(res);
	}
	
	@Override
	public Class getAllowedGenericType() {
		return List.class;
	}

}

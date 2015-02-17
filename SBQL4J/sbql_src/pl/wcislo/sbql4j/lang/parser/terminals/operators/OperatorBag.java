package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import java.util.Collection;

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
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorBag extends Operator {

	public OperatorBag(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitBag(this, treeVisitor, opExpr, subExprs);
	};
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		Bag eres = null;
		if (args.length < 1) {
			eres = new Bag();
		} else {
			// mamy generyczny bag - args[1] okresla jego typ
			Expression bagTypeExpr = args[1];
			bagTypeExpr.accept(interpreter, null);
			QueryResult qr = interpreter.getQres().pop();
			QueryResult clazz = Utils.collectionToObject(qr);
			JavaClass<Collection> jc = (JavaClass<Collection>) clazz;
			try {
				Collection c = jc.value.newInstance();
				eres = new Bag<Collection<QueryResult>>(c);
			} catch (Exception e) {
				throw new SBQLException(e);
			}
		}
		QueryResult e1res = interpreter.getQres().pop();
		StructSBQL s;
		if (e1res instanceof StructSBQL) {
			s = (StructSBQL) e1res;
		} else {
			s = new StructSBQL();
			if (e1res instanceof Collection) {
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
//		Expression opExpr = args[0];
//		Expression argExpr = args[1];
//		Expression genericTypeExpr = args[2];
//		StringBuilder sb = gen.sb;
//		String identEres = gen.generateIdentifier("eres");
//
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identS = gen.generateIdentifier("s");
//
//		gen.printExpressionTrace("//OperatorBag - start");
//		sb.append("Bag " + identEres + " = null;                                                		 \n");
//		if (genericTypeExpr == null) {
//			sb.append("	" + identEres + " = new Bag();                                                \n");
//		} else {
//			sb.append("	//mamy generyczny bag - args[1] okresla jego typ                     \n");
////			Expression bagTypeExpr = args[1];
//			genericTypeExpr.accept(gen, null);
//			String identQr = gen.generateIdentifier("qr");
//			String identClazz = gen.generateIdentifier("clazz");
//			String identJc = gen.generateIdentifier("jc");
//			String identC = gen.generateIdentifier("c");
//
//			sb.append("	                                                                     \n");
//			sb.append("	QueryResult " + identQr + " = qres.pop();                                         \n");
//			sb.append("	QueryResult " + identClazz + " = Utils.collectionToObject(" + identQr + ");                    \n");
//			sb.append("	JavaClass<Collection> " + identJc + " = (JavaClass<Collection>) " + identClazz + ";             \n");
//			sb.append("	try {                                                                \n");
//			sb.append("		Collection " + identC + " = " + identJc + ".value.newInstance();                           \n");
//			sb.append("		" + identEres + " = new Bag<Collection<QueryResult>>(" + identC + ");                      \n");
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
//		gen.printExpressionTrace("//OperatorBag - end");
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
//		gen.printExpressionTrace("//OperatorBag - start");
//		sb.append("Bag " + identEres + "; \n");
//		if (genExpr == null) {
//			sb.append("	" + identEres + " = new Bag();                                                \n");
//		} else {
//			//generic bag
//			genExpr.accept(gen, null);
//			String identGenExprRes = genExpr.getSignature().getResultName();
//			String identJc = gen.generateIdentifier("jc");
//			String identC = gen.generateIdentifier("c");
//			sb.append("	JavaClass<Collection> " + identJc + " = (JavaClass<Collection>) " + identGenExprRes + "; \n");
//			sb.append("	try { \n");
//			sb.append("		Collection " + identC + " = " + identJc + ".value.newInstance(); \n");
//			sb.append("		" + identEres + " = new Bag<Collection<QueryResult>>(" + identC + "); \n");
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
//		gen.printExpressionTrace("//OperatorBag - end");	
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
//		String argType = argExpr.getSignature().getJavaTypeString();
//		boolean isArgCol = argExpr.getSignature().sColType != SCollectionType.NO_COLLECTION;
////		String identEres = opExpr.signature.getResultName();
//
//		gen.printExpressionTrace("//OperatorBag - start");
////		sb.append("Bag " + identEres + "; \n");
//		if (genExpr == null) {
//			sb.append(""+ opExpr.getSignature().genJavaDeclarationCode() + " = new ArrayList();                                                \n");
//		} else {
//			//generic bag
//			genExpr.accept(gen, null);
//			String identGenExprRes = genExpr.getSignature().getResultName();
//			String identJc = gen.generateIdentifier("jc");
//			String identC = gen.generateIdentifier("c");
//			sb.append("	try { \n");
//			sb.append("	Collection<"+argType+">" + identEres + " = ");
//			genExpr.accept(gen, null);
//			sb.append(".newInstance(); \n");
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
//		gen.printExpressionTrace("//OperatorBag - end");		
//	}

	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Signature sig = args[0].getSignature();
		if(sig instanceof StructSignature && !sig.isCollectionResult()) {
			StructSignature ssig = (StructSignature)sig;
			if(ssig.fieldsNumber() > 0) {
				Signature fSig0 = ssig.getFields()[0];
				sig = fSig0;
			}
		}
		Expression currentExpr = args[1];
		Signature res = sig.clone();
		res.setColType(SCollectionType.BAG);
		currentExpr.setSignature(res);
	}

	@Override
	public Class getAllowedGenericType() {
		return Collection.class;
	}

}

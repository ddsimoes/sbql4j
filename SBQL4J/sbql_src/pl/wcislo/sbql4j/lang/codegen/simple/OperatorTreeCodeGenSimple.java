package pl.wcislo.sbql4j.lang.codegen.simple;

import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.StructSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorAnd;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorAvg;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorBag;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorComma;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorCount;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorDivide;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorElementAt;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorEquals;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorExcept;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorExists;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorIn;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorInstanceof;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorIntersect;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorLess;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorLessOrEqual;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMax;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMin;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMinus;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorModulo;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMore;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMoreOrEqual;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMultiply;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorNot;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorNotEquals;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorOr;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorPlus;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorSequence;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorStruct;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorSum;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorUnion;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorUnique;
import pl.wcislo.sbql4j.lang.tree.visitors.OperatorVisitor;

public class OperatorTreeCodeGenSimple implements OperatorVisitor<Void, QueryCodeGenSimple> {

	@Override
	public Void visitAnd(OperatorAnd op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		
		gen.printExpressionTrace("//OperatorAnd - start \n");
		sb.append("Boolean "+identRightArg+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("Boolean "+identLeftArg+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+" && "+identRightArg+"); \n");
		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorAnd - start \n");
		return null;
	}
	
	@Override
	public Void visitAvg(OperatorAvg op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb; 
		String identResult = gen.generateIdentifier("result");
		String identObjects = gen.generateIdentifier("objects");
		String identResultS = gen.generateIdentifier("resultS");
		String identSection = gen.generateIdentifier("section");
		String identSecEl = gen.generateIdentifier("secEl");
		String identN = gen.generateIdentifier("n");
		gen.printExpressionTrace("//OperatorAvg - start                                                          \n");
		sb.append("QueryResult "+identResult+";                                                          \n");
		sb.append("QueryResult "+identObjects+" = qres.pop();                                            \n");
		sb.append("double "+identResultS+" = 0;                                                          \n");
		sb.append("if("+identObjects+" instanceof Collection) {                                          \n");
		sb.append("	Collection<QueryResult> "+identSection+" = (Collection<QueryResult>) "+identObjects+";        \n");
		sb.append("	for(QueryResult "+identSecEl+" : "+identSection+") {                                          \n");
		sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identSecEl+", store);                  \n");
		sb.append("		"+identResultS+" += "+identN+".doubleValue();                                             \n");
		sb.append("	}                                                                           \n");
		sb.append("	"+identResultS+" = "+identResultS+" / ("+identSection+".size());                                         \n");
		sb.append("} else {                                                                     \n");
		sb.append("	"+identResultS+" = ((Number) (Utils.toSimpleValue("+identObjects+", store))).doubleValue();   \n");
		sb.append("}                                                                            \n");
		sb.append(""+identResult+" = JavaObjectAbstractFactory.getJavaObjectFactory().                   \n");
		sb.append("	createJavaComplexObject("+identResultS+");                                           \n");
		sb.append("                                                                             \n");
		sb.append("qres.push("+identResult+");                                                           \n");
		gen.printExpressionTrace("//OperatorAvg - end                                                          ");
		return null;
	}
	
	@Override
	public Void visitBag(OperatorBag op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		Expression genericTypeExpr = subExprs[1];
		StringBuilder sb = gen.sb;
		String identEres = gen.generateIdentifier("eres");

		String identE1Res = gen.generateIdentifier("e1res");
		String identS = gen.generateIdentifier("s");

		gen.printExpressionTrace("//OperatorBag - start");
		sb.append("Bag " + identEres + " = null;                                                		 \n");
		if (genericTypeExpr == null) {
			sb.append("	" + identEres + " = new Bag();                                                \n");
		} else {
			sb.append("	//mamy generyczny bag - args[1] okresla jego typ                     \n");
//			Expression bagTypeExpr = args[1];
			genericTypeExpr.accept(gen, null);
			String identQr = gen.generateIdentifier("qr");
			String identClazz = gen.generateIdentifier("clazz");
			String identJc = gen.generateIdentifier("jc");
			String identC = gen.generateIdentifier("c");

			sb.append("	                                                                     \n");
			sb.append("	QueryResult " + identQr + " = qres.pop();                                         \n");
			sb.append("	QueryResult " + identClazz + " = Utils.collectionToObject(" + identQr + ");                    \n");
			sb.append("	JavaClass<Collection> " + identJc + " = (JavaClass<Collection>) " + identClazz + ";             \n");
			sb.append("	try {                                                                \n");
			sb.append("		Collection " + identC + " = " + identJc + ".value.newInstance();                           \n");
			sb.append("		" + identEres + " = new Bag<Collection<QueryResult>>(" + identC + ");                      \n");
			sb.append("	} catch (Exception e) {                                              \n");
			sb.append("		throw new SBQLException(e);                                             \n");
			sb.append("	}                                                                    \n");
		}
		sb.append("QueryResult " + identE1Res + " = qres.pop();                                        \n");
		sb.append("Struct " + identS + ";                                                              \n");
		sb.append("if(" + identE1Res + " instanceof Struct) {                                          \n");
		sb.append("	" + identS + " = (Struct) " + identE1Res + ";                                                   \n");
		sb.append("} else {                                                               \n");
		sb.append("	" + identS + " = new Struct();                                                     \n");
		sb.append("	if(" + identE1Res + " instanceof Collection) {                                     \n");
		sb.append("		" + identS + ".addAll((Collection) " + identE1Res + ");                                     \n");
		sb.append("	} else {                                                              \n");
		sb.append("		" + identS + ".add(" + identE1Res + ");                                                     \n");
		sb.append("	}                                                                     \n");
		sb.append("}                                                                      \n");
		sb.append("" + identEres + ".addAll(" + identS + ");                                                        \n");
		sb.append("qres.push(" + identEres + ");                                                       \n");
		gen.printExpressionTrace("//OperatorBag - end");
		return null;
	}
	
	@Override
	public Void visitComma(OperatorComma op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identEres = gen.generateIdentifier("eres");
		String identE2Res = gen.generateIdentifier("e2res");
		String identE1Res = gen.generateIdentifier("e1res");
		String identRes = gen.generateIdentifier("res");
		
		gen.printExpressionTrace("//OperatorComma - start \n");                                 
		sb.append("Bag "+identEres+" = new Bag();                                       \n");
		sb.append("QueryResult "+identE2Res+" = qres.pop();                             \n");
		sb.append("QueryResult "+identE1Res+" = qres.pop();                             \n");
		sb.append("QueryResult "+identRes+" = Utils.cartesianProduct("+identE1Res+", "+identE2Res+");     \n");
		sb.append("qres.push("+identRes+");                                             \n");
		gen.printExpressionTrace("//OperatorComma - end \n");
		return null;
	}
	
	@Override
	public Void visitCount(OperatorCount op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		Expression argExpr = subExprs[0];
		StringBuilder sb = gen.sb;
		String identResult = gen.generateIdentifier("result");
		String identCount = gen.generateIdentifier("count");
		String identObjects = gen.generateIdentifier("objects");
		
		gen.printExpressionTrace("//OperatorCount - start \n");
		sb.append("QueryResult "+identResult+"; \n");
		sb.append("int "+identCount+"; \n");
		
		if(argExpr.getSignature().getColType() == SCollectionType.NO_COLLECTION) {
			sb.append("	"+identCount+" = 1; \n");
		} else {
			sb.append("Object "+identObjects+" = qres.pop(); \n");
			sb.append("	"+identCount+" = ((CollectionResult) "+identObjects+").size(); \n");	
		}
		sb.append(""+identResult+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject("+identCount+"); \n");
		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorCount - end");
		return null;
	}
	
	@Override
	public Void visitDivide(OperatorDivide op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResultNum = gen.generateIdentifier("resultNum");
		String identResult = gen.generateIdentifier("result");
		gen.printExpressionTrace("//OperatorDivide - start \n");
		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("Number "+identResultNum+" = MathUtils.divide("+identLeftArg+", "+identRightArg+"); \n");
		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorDivide - end \n");
		return null;
	}
	
	@Override
	public Void visitElementAt(OperatorElementAt op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		Signature leftSig = leftExpr.getSignature().getDerefSignatureWithCardinality();
		Signature rightSig = rightExpr.getSignature().getDerefSignatureWithCardinality();
		
		String identRightVal = gen.generateIdentifier("rightVal");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identLeftCol = gen.generateIdentifier("leftCol");
		String identResult = gen.generateIdentifier("result");
		String identI = gen.generateIdentifier("i");
		String identIt = gen.generateIdentifier("it");
		
		gen.printExpressionTrace("//OperatorElementAt - start \n");
		sb.append("QueryResult "+identRightVal+" = qres.pop();\n");
		sb.append("QueryResult "+identLeftArg+" = qres.pop();\n");
		sb.append("if("+identLeftArg+" instanceof Binder) {\n");
		sb.append("	"+identLeftArg+" = ((Binder)"+identLeftArg+").object;\n");
		sb.append("}\n");
		sb.append("if(!("+identLeftArg+" instanceof CollectionResult)) {\n");
		sb.append("	throw new RuntimeException(\"execpted Sequence, got: \"+"+identLeftArg+".getClass());\n");
		sb.append("}\n");
		sb.append("CollectionResult "+identLeftCol+" = (CollectionResult) "+identLeftArg+";\n");
//		if(!(rightVal instanceof Struct)) {
		if(!(rightSig instanceof StructSignature)) {
			String identRightArg = gen.generateIdentifier("rightArg");
			
			sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightVal+", store);\n");
			sb.append("if(!("+identRightArg+" instanceof Integer || "+identRightArg+" instanceof Long || "+identRightArg+" instanceof Short || "+identRightArg+" instanceof Byte)) {\n");
			sb.append("	throw new RuntimeException(\"execpted Number, got: \"+"+identRightArg+".getClass());\n");
			sb.append("}\n");
			sb.append("Integer "+identI+" = ((Number)"+identRightArg+").intValue();\n");
			sb.append("QueryResult "+identResult+";\n");
			sb.append("if("+identLeftCol+".size() < "+identI+"+1) {\n");
			sb.append("	"+identResult+" = new Bag();\n");
			sb.append("} else {\n");
			if(leftSig.getColType() == SCollectionType.SEQUENCE) {
				sb.append("		"+identResult+" = ((Sequence)"+identLeftCol+").get("+identI+");\n");	
			} else {
				
				String identJ = gen.generateIdentifier("j");
				String identQr = gen.generateIdentifier("qr");
				sb.append("		Iterator<QueryResult> "+identIt+" = "+identLeftCol+".iterator();\n");
				sb.append("		QueryResult "+identQr+" = "+identIt+".next();\n");
				sb.append("		for(int "+identJ+"=0; "+identJ+"<"+identI+"; "+identJ+"++) {\n");
				sb.append("			"+identQr+" = "+identIt+".next();\n");
				sb.append("		}\n");
				sb.append("		"+identResult+" = "+identQr+";\n");
			}
			sb.append("}\n");
			sb.append("qres.push("+identResult+");\n");
		} else {
			String identBounds = gen.generateIdentifier("bounds");
			String identLowBound = gen.generateIdentifier("lowBound");
			String identUpBound = gen.generateIdentifier("upBound");
			
			StructSignature boundsSig = (StructSignature) rightSig;
			sb.append("StructSBQL "+identBounds+" = (StructSBQL) "+identRightVal+";\n");
			sb.append("Integer "+identLowBound+" = (Integer) Utils.toSimpleValue("+identBounds+".get(0), store);\n");
			sb.append("Integer "+identUpBound+";\n");
			if(boundsSig.getFields().length < 2) {
				sb.append("	"+identUpBound+" = Integer.MAX_VALUE;\n");
			} else {
				sb.append("	"+identUpBound+" = (Integer) Utils.toSimpleValue("+identBounds+".get(1), store);\n");
			}
			sb.append("CollectionResult "+identResult+";\n");
			if(leftSig.getColType() == SCollectionType.SEQUENCE) {
				sb.append("	"+identResult+" = new Sequence();\n");
			} else {
				sb.append("	"+identResult+" = new Bag();\n");
			}
			sb.append("Iterator<QueryResult> "+identIt+" = "+identLeftCol+".iterator();\n");
			sb.append("int "+identI+" = 0;\n");
			sb.append("while("+identIt+".hasNext() && "+identI+" <= "+identUpBound+") {\n");
			sb.append("	QueryResult el = "+identIt+".next();\n");
			sb.append("	if("+identI+" >= "+identLowBound+" && "+identI+" <= "+identUpBound+") {\n");
			sb.append("		"+identResult+".add(el);\n");
			sb.append("	}\n");
			sb.append("	"+identI+"++;\n");
			sb.append("}\n");
			sb.append("qres.push("+identResult+");\n");
			gen.printExpressionTrace("//OperatorElementAt - end \n");
		}
		return null;
	}
	
	@Override
	public Void visitEquals(OperatorEquals op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		gen.printExpressionTrace("//OperatorEquals - start \n");
		sb.append("Object "+identRightArg+" = Utils.toSimpleValue(qres.pop(), store);\n");		
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue(qres.pop(), store);\n");
		sb.append("QueryResult "+identResult+";\n");
		sb.append(""+identResult+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject((Utils.equalsNumeric("+identLeftArg+", "+identRightArg+")));\n");
		sb.append("qres.push("+identResult+");\n");
		gen.printExpressionTrace("//OperatorEquals - end \n");
		return null;
	}
	
	@Override
	public Void visitExcept(OperatorExcept op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identE1Res = gen.generateIdentifier("e1res");
		String identE2Res = gen.generateIdentifier("e2res");
		String identResult = gen.generateIdentifier("result");
		String identI1 = gen.generateIdentifier("i1");
		String identI2 = gen.generateIdentifier("i2");
		String identT1 = gen.generateIdentifier("t1");
		String identT2 = gen.generateIdentifier("t2");
		
		gen.printExpressionTrace("//OperatorMinusSet - start \n");
		sb.append("Bag "+identResult+" = new Bag(); \n");
		sb.append("CollectionResult "+identE2Res+" = Utils.objectToCollection(qres.pop()); \n");
		sb.append("CollectionResult "+identE1Res+" = Utils.objectToCollection(qres.pop()); \n");
		sb.append(" \n");
		sb.append(""+identResult+".addAll("+identE1Res+"); \n");
		sb.append("for(Iterator<QueryResult> "+identI1+" = "+identResult+".iterator(); "+identI1+".hasNext(); ) { \n");
		sb.append("	QueryResult "+identT1+" = "+identI1+".next(); \n");
		sb.append("	for(Iterator<QueryResult> "+identI2+" = "+identE2Res+".iterator(); "+identI2+".hasNext(); ) { \n");
		sb.append("		QueryResult "+identT2+" = "+identI2+".next(); \n");
		sb.append("		if(Utils.equalsWithDeref("+identT1+", "+identT2+", store)) { \n");
		sb.append("			"+identI1+".remove(); \n");
		sb.append("			break; \n");
		sb.append("		} \n");
		sb.append("	} \n");
		sb.append("} \n");
		sb.append("qres.push("+identResult+"); \n");	
		gen.printExpressionTrace("//OperatorMinusSet - end \n");
		return null;
	}
	
	@Override
	public Void visitExists(OperatorExists op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identE1Res = gen.generateIdentifier("e1res");
		String identCol = gen.generateIdentifier("col");
		String identResult = gen.generateIdentifier("result");
		
		gen.printExpressionTrace("//OperatorExists - start\n");
		sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
		sb.append("CollectionResult "+identCol+" = Utils.objectToCollection("+identE1Res+"); \n");
		sb.append("boolean "+identResult+" = !col.isEmpty(); \n");
		sb.append("qres.add(JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject("+identResult+")); \n");
		gen.printExpressionTrace("//OperatorExists - end\n");
		return null;
	}
	
	@Override
	public Void visitIn(OperatorIn op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identE1Res = gen.generateIdentifier("e1res");
		String identE2Res = gen.generateIdentifier("e2res");
		String identResult = gen.generateIdentifier("result");
		String identI1 = gen.generateIdentifier("i1");
		String identI2 = gen.generateIdentifier("i2");
		String identT1 = gen.generateIdentifier("t1");
		String identT2 = gen.generateIdentifier("t2");
		String identR2 = gen.generateIdentifier("r2");
		gen.printExpressionTrace("//OperatorIn - start \n");
		sb.append("CollectionResult "+identE2Res+" = Utils.objectToCollection(qres.pop()); \n");
		sb.append("CollectionResult "+identE1Res+" = Utils.objectToCollection(qres.pop()); \n");
		sb.append("boolean "+identResult+" = true; \n");
		sb.append("for(Iterator<QueryResult> "+identI1+" = "+identE1Res+".iterator(); "+identResult+" && "+identI1+".hasNext(); ) { \n");
		sb.append("	QueryResult "+identT1+" = "+identI1+".next(); \n");
		sb.append("	boolean "+identR2+" = false; \n");
		sb.append("	for(Iterator<QueryResult> "+identI2+" = "+identE2Res+".iterator(); !"+identR2+" && "+identI2+".hasNext(); ) { \n");
		sb.append("		QueryResult "+identT2+" = "+identI2+".next(); \n");
		sb.append("		if(Utils.equalsWithDeref("+identT1+", "+identT2+", store)) { \n");
		sb.append("			"+identR2+" = true; \n");
		sb.append("		} \n");
		sb.append("	} \n");
		sb.append("	if(!"+identR2+") { \n");
		sb.append("		"+identResult+" = false; \n");
		sb.append("	} \n");
		sb.append("} \n");		 		
		sb.append("qres.push(javaObjectFactory.createJavaComplexObject("+identResult+")); \n");
		gen.printExpressionTrace("//OperatorIn - end \n");
		return null;
	}
	
	@Override
	public Void visitInstanceof(OperatorInstanceof op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identClazz = gen.generateIdentifier("clazz");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		String identRes = gen.generateIdentifier("res");
		
		gen.printExpressionTrace("//OperatorInstanceof - start \n");
		sb.append("Class "+identClazz+" = (Class) Utils.toSimpleValue(qres.pop(), store); \n");	
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("Boolean "+identResult+" = "+identClazz+".isInstance("+identLeftArg+"); \n");
		sb.append("JavaObject "+identRes+" = javaObjectFactory.createJavaComplexObject("+identResult+"); \n");
		sb.append("qres.push("+identRes+"); \n");
		gen.printExpressionTrace("//OperatorInstanceof - end \n");
		return null;
	}
	
	@Override
	public Void visitIntersect(OperatorIntersect op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identE1Res = gen.generateIdentifier("e1res");
		String identE2Res = gen.generateIdentifier("e2res");
		String identResult = gen.generateIdentifier("result");
		
		gen.printExpressionTrace("//OperatorIntersect - start \n");
		sb.append("CollectionResult "+identE2Res+" = Utils.objectToCollection(qres.pop()); \n");
		sb.append("CollectionResult "+identE1Res+" = Utils.objectToCollection(qres.pop()); \n");
		sb.append("Bag "+identResult+" = new Bag(); \n");
		sb.append(""+identResult+".addAll(CollectionUtils.intersection("+identE1Res+", "+identE2Res+")); \n");
		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorIntersect - end \n");
		return null;
	}
	
	@Override
	public Void visitLess(OperatorLess op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		String identN1 = gen.generateIdentifier("n1");
		String identN2 = gen.generateIdentifier("n2");
		String identC1 = gen.generateIdentifier("c1");
		String identC2 = gen.generateIdentifier("c2");
		String identCRes = gen.generateIdentifier("cRes");
		String identRes = gen.generateIdentifier("res");
		gen.printExpressionTrace("//OperatorLess - start\n");
		sb.append("Object "+identRightArg+" = Utils.toSimpleValue(qres.pop(), store);		         \n");
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue(qres.pop(), store);                  \n");
		sb.append("                                                                          \n");
		sb.append("Boolean "+identResult+";                                                           \n");
		sb.append("if("+identLeftArg+" instanceof Number && "+identRightArg+" instanceof Number) {             \n");
		sb.append("	Number "+identN1+" = (Number) "+identLeftArg+";                                            \n");
		sb.append("	Number "+identN2+" = (Number) "+identRightArg+";                                           \n");
		sb.append("	"+identResult+" = "+identN1+".doubleValue() < "+identN2+".doubleValue();                            \n");
		sb.append("} else {                                                                  \n");
		sb.append("	Comparable "+identC1+" = (Comparable) "+identLeftArg+";                                    \n");
		sb.append("	Comparable "+identC2+" = (Comparable) "+identRightArg+";                                   \n");
		sb.append("	int "+identCRes+" = MathUtils.compareSafe("+identC1+", "+identC2+"); \n");
		sb.append("	"+identResult+" = "+identCRes+" < 0;                                                       \n");
		sb.append("}                                                                         \n");
		sb.append("JavaComplexObject "+identRes+" = JavaObjectAbstractFactory.getJavaObjectFactory(). \n");
		sb.append("	createJavaComplexObject("+identResult+");                                         \n");
		sb.append("qres.push("+identRes+");		                                                     \n");
		gen.printExpressionTrace("//OperatorLess - end\n");
		return null;
	}
	
	@Override
	public Void visitLessOrEqual(OperatorLessOrEqual op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		String identN1 = gen.generateIdentifier("n1");
		String identN2 = gen.generateIdentifier("n2");
		String identC1 = gen.generateIdentifier("c1");
		String identC2 = gen.generateIdentifier("c2");
		String identCRes = gen.generateIdentifier("cRes");
		String identRes = gen.generateIdentifier("res");
		gen.printExpressionTrace("//OperatorLess - start\n");
		sb.append("Object "+identRightArg+" = Utils.toSimpleValue(qres.pop(), store);		         \n");
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue(qres.pop(), store);                  \n");
		sb.append("                                                                          \n");
		sb.append("Boolean "+identResult+";                                                           \n");
		sb.append("if("+identLeftArg+" instanceof Number && "+identRightArg+" instanceof Number) {             \n");
		sb.append("	Number "+identN1+" = (Number) "+identLeftArg+";                                            \n");
		sb.append("	Number "+identN2+" = (Number) "+identRightArg+";                                           \n");
		sb.append("	"+identResult+" = "+identN1+".doubleValue() <= "+identN2+".doubleValue();                            \n");
		sb.append("} else {                                                                  \n");
		sb.append("	Comparable "+identC1+" = (Comparable) "+identLeftArg+";                                    \n");
		sb.append("	Comparable "+identC2+" = (Comparable) "+identRightArg+";                                   \n");
		sb.append("	int "+identCRes+" = MathUtils.compareSafe("+identC1+", "+identC2+"); \n");
		sb.append("	"+identResult+" = "+identCRes+" <= 0;                                                       \n");
		sb.append("}                                                                         \n");
		sb.append("JavaComplexObject "+identRes+" = JavaObjectAbstractFactory.getJavaObjectFactory(). \n");
		sb.append("	createJavaComplexObject("+identResult+");                                         \n");
		sb.append("qres.push("+identRes+");		                                                     \n");
		gen.printExpressionTrace("//OperatorLess - end\n");	
		return null;
	}
	
	@Override
	public Void visitMax(OperatorMax op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identE1Res = gen.generateIdentifier("e1res");
		String identColRes = gen.generateIdentifier("colRes");
		String identMax = gen.generateIdentifier("max");
		String identObject = gen.generateIdentifier("object");
		String identN = gen.generateIdentifier("n");
		gen.printExpressionTrace("//OperatorMax - start \n");
		sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
		sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
		sb.append("Number "+identMax+" = null; \n");
		sb.append("try { \n");
		sb.append("	for (QueryResult "+identObject+" : "+identColRes+") { \n");
		sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identObject+", store); \n");
		sb.append("		"+identMax+" = MathUtils.max("+identMax+", "+identN+"); \n");
		sb.append("	} \n");
		sb.append("	qres.push(javaObjectFactory.createJavaComplexObject("+identMax+")); \n");
		sb.append(" \n");	
		sb.append("} catch(ClassCastException e) { \n");
		sb.append("	throw new RuntimeException(\"OperatorMax.eval() invalid type: type should be a primitive value\"); \n");
		sb.append("} \n");		
		gen.printExpressionTrace("//OperatorMax - end \n");
		return null;
	}
	
	@Override
	public Void visitMin(OperatorMin op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identE1Res = gen.generateIdentifier("e1res");
		String identColRes = gen.generateIdentifier("colRes");
		String identMin = gen.generateIdentifier("min");
		String identObject = gen.generateIdentifier("object");
		String identN = gen.generateIdentifier("n");
		gen.printExpressionTrace("//OperatorMin - start \n");
		sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
		sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
		sb.append("Number "+identMin+" = null; \n");
		sb.append("try { \n");
		sb.append("	for (QueryResult "+identObject+" : "+identColRes+") { \n");
		sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identObject+", store); \n");
		sb.append("		"+identMin+" = MathUtils.min("+identMin+", "+identN+"); \n");
		sb.append("	} \n");
		sb.append("	qres.push(javaObjectFactory.createJavaComplexObject("+identMin+")); \n");
		sb.append(" \n");	
		sb.append("} catch(ClassCastException e) { \n");
		sb.append("	throw new RuntimeException(\"OperatorMax.eval() invalid type: type should be a primitive value\"); \n");
		sb.append("} \n");		
		gen.printExpressionTrace("//OperatorMin - end \n");
		return null;
	}
	
	@Override
	public Void visitMinus(OperatorMinus op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResultNum = gen.generateIdentifier("resultNum");
		String identResult = gen.generateIdentifier("result");
		gen.printExpressionTrace("//OperatorMinus - start \n");
		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("Number "+identResultNum+" = MathUtils.subtract("+identLeftArg+", "+identRightArg+"); \n");
		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorMinus - end \n");
		return null;
	}
	
	@Override
	public Void visitModulo(OperatorModulo op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResultNum = gen.generateIdentifier("resultNum");
		String identResult = gen.generateIdentifier("result");
		gen.printExpressionTrace("//OperatorModulo - start \n");
		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("Number "+identResultNum+" = MathUtils.modulo("+identLeftArg+", "+identRightArg+"); \n");
		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorModulo - end \n");
		return null;
	}
	
	@Override
	public Void visitMore(OperatorMore op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		String identN1 = gen.generateIdentifier("n1");
		String identN2 = gen.generateIdentifier("n2");
		String identC1 = gen.generateIdentifier("c1");
		String identC2 = gen.generateIdentifier("c2");
		String identCRes = gen.generateIdentifier("cRes");
		String identRes = gen.generateIdentifier("res");
		gen.printExpressionTrace("//OperatorLess - start\n");
		sb.append("Object "+identRightArg+" = Utils.toSimpleValue(qres.pop(), store);		         \n");
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue(qres.pop(), store);                  \n");
		sb.append("                                                                          \n");
		sb.append("Boolean "+identResult+";                                                           \n");
		sb.append("if("+identLeftArg+" instanceof Number && "+identRightArg+" instanceof Number) {             \n");
		sb.append("	Number "+identN1+" = (Number) "+identLeftArg+";                                            \n");
		sb.append("	Number "+identN2+" = (Number) "+identRightArg+";                                           \n");
		sb.append("	"+identResult+" = "+identN1+".doubleValue() > "+identN2+".doubleValue();                            \n");
		sb.append("} else {                                                                  \n");
		sb.append("	Comparable "+identC1+" = (Comparable) "+identLeftArg+";                                    \n");
		sb.append("	Comparable "+identC2+" = (Comparable) "+identRightArg+";                                   \n");
		sb.append("	int "+identCRes+" = MathUtils.compareSafe("+identC1+", "+identC2+"); \n");
		sb.append("	"+identResult+" = "+identCRes+" > 0;                                                       \n");
		sb.append("}                                                                         \n");
		sb.append("JavaComplexObject "+identRes+" = JavaObjectAbstractFactory.getJavaObjectFactory(). \n");
		sb.append("	createJavaComplexObject("+identResult+");                                         \n");
		sb.append("qres.push("+identRes+");		                                                     \n");
		gen.printExpressionTrace("//OperatorLess - end\n");	
		return null;
	}
	
	@Override
	public Void visitMoreOrEqual(OperatorMoreOrEqual op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		String identN1 = gen.generateIdentifier("n1");
		String identN2 = gen.generateIdentifier("n2");
		String identC1 = gen.generateIdentifier("c1");
		String identC2 = gen.generateIdentifier("c2");
		String identCRes = gen.generateIdentifier("cRes");
		String identRes = gen.generateIdentifier("res");
		gen.printExpressionTrace("//OperatorLess - start\n");
		sb.append("Object "+identRightArg+" = Utils.toSimpleValue(qres.pop(), store);		         \n");
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue(qres.pop(), store);                  \n");
		sb.append("                                                                          \n");
		sb.append("Boolean "+identResult+";                                                           \n");
		sb.append("if("+identLeftArg+" instanceof Number && "+identRightArg+" instanceof Number) {             \n");
		sb.append("	Number "+identN1+" = (Number) "+identLeftArg+";                                            \n");
		sb.append("	Number "+identN2+" = (Number) "+identRightArg+";                                           \n");
		sb.append("	"+identResult+" = "+identN1+".doubleValue() >= "+identN2+".doubleValue();                            \n");
		sb.append("} else {                                                                  \n");
		sb.append("	Comparable "+identC1+" = (Comparable) "+identLeftArg+";                                    \n");
		sb.append("	Comparable "+identC2+" = (Comparable) "+identRightArg+";                                   \n");
		sb.append("	int "+identCRes+" = MathUtils.compareSafe("+identC1+", "+identC2+"); \n");
		sb.append("	"+identResult+" = "+identCRes+" >= 0;                                                       \n");
		sb.append("}                                                                         \n");
		sb.append("JavaComplexObject "+identRes+" = JavaObjectAbstractFactory.getJavaObjectFactory(). \n");
		sb.append("	createJavaComplexObject("+identResult+");                                         \n");
		sb.append("qres.push("+identRes+");		                                                     \n");
		gen.printExpressionTrace("//OperatorLess - end\n");	
		return null;
	}
	
	@Override
	public Void visitMultiply(OperatorMultiply op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResultNum = gen.generateIdentifier("resultNum");
		String identResult = gen.generateIdentifier("result");
		gen.printExpressionTrace("//OperatorMultiply - start \n");
		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("Number "+identResultNum+" = MathUtils.multiply("+identLeftArg+", "+identRightArg+"); \n");
		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorMultiply - end \n");
		return null;
	}
	
	@Override
	public Void visitNot(OperatorNot op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identB = gen.generateIdentifier("b");
		gen.printExpressionTrace("//OperatorNot - start \n");
		sb.append("Boolean "+identB+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("qres.add(javaObjectFactory.createJavaComplexObject(!"+identB+")); \n");
		gen.printExpressionTrace("//OperatorNot - start \n");
		return null;
	}
	
	@Override
	public Void visitNotEquals(OperatorNotEquals op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		gen.printExpressionTrace("//OperatorEquals - start \n");
		sb.append("Object "+identRightArg+" = Utils.toSimpleValue(qres.pop(), store);\n");		
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue(qres.pop(), store);\n");
		sb.append("QueryResult "+identResult+";\n");
		sb.append(""+identResult+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject((!Utils.equalsNumeric("+identLeftArg+", "+identRightArg+")));\n");
		sb.append("qres.push("+identResult+");\n");
		gen.printExpressionTrace("//OperatorEquals - end \n");
		return null;
	}
	
	@Override
	public Void visitOr(OperatorOr op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		
		gen.printExpressionTrace("//OperatorOr - start \n");
		sb.append("Boolean "+identRightArg+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("Boolean "+identLeftArg+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+" || "+identRightArg+"); \n");
		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorOr - start \n");
		return null;
	}
	
	@Override
	public Void visitPlus(OperatorPlus op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		String identResultNum = gen.generateIdentifier("resultNum");
		
		gen.printExpressionTrace("//OperatorPlus - start \n");  
		sb.append("Object "+identRightArg+" = Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue(qres.pop(), store); \n");
		sb.append("QueryResult "+identResult+"; \n");
		sb.append("if("+identLeftArg+" instanceof String || "+identRightArg+" instanceof String) { \n");
		sb.append("	"+identResult+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+".toString().concat("+identRightArg+".toString())); \n");
		sb.append("} else { \n");
		sb.append("	Number "+identResultNum+" = MathUtils.sum((Number)"+identLeftArg+", (Number)"+identRightArg+"); \n");
		sb.append("	"+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
		sb.append("} \n");
		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorPlus - end \n");
		return null;
	}
	
	@Override
	public Void visitSequence(OperatorSequence op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identEres = gen.generateIdentifier("eres");

		String identE1Res = gen.generateIdentifier("e1res");
		String identS = gen.generateIdentifier("s");

		gen.printExpressionTrace("//OperatorSequence - start \n");
		sb.append("Sequence " + identEres + " = null;                                                		 \n");
		if (subExprs.length < 2) {
			sb.append("	" + identEres + " = new Sequence();                                                \n");
		} else {
			sb.append("	//mamy generyczna sekwencje - args[1] okresla jej typ                    \n");
			Expression sequenceTypeExpr = subExprs[1];
			sequenceTypeExpr.accept(gen, null);
			String identQr = gen.generateIdentifier("qr");
			String identClazz = gen.generateIdentifier("clazz");
			String identJc = gen.generateIdentifier("jc");
			String identC = gen.generateIdentifier("c");

			sb.append("	                                                                     \n");
			sb.append("	QueryResult " + identQr + " = qres.pop();                                         \n");
			sb.append("	QueryResult " + identClazz + " = Utils.collectionToObject(" + identQr + ");                    \n");
			sb.append("	JavaClass<List> " + identJc + " = (JavaClass<List>) " + identClazz + ";             \n");
			sb.append("	try {                                                                \n");
			sb.append("		List " + identC + " = " + identJc + ".value.newInstance();                           \n");
			sb.append("		" + identEres + " = new Bag<List<QueryResult>>(" + identC + ");                      \n");
			sb.append("	} catch (Exception e) {                                              \n");
			sb.append("		throw new SBQLException(e);                                             \n");
			sb.append("	}                                                                    \n");
		}
		sb.append("QueryResult " + identE1Res + " = qres.pop();                                        \n");
		sb.append("Struct " + identS + ";                                                              \n");
		sb.append("if(" + identE1Res + " instanceof Struct) {                                          \n");
		sb.append("	" + identS + " = (Struct) " + identE1Res + ";                                                   \n");
		sb.append("} else {                                                               \n");
		sb.append("	" + identS + " = new Struct();                                                     \n");
		sb.append("	if(" + identE1Res + " instanceof Collection) {                                     \n");
		sb.append("		" + identS + ".addAll((Collection) " + identE1Res + ");                                     \n");
		sb.append("	} else {                                                              \n");
		sb.append("		" + identS + ".add(" + identE1Res + ");                                                     \n");
		sb.append("	}                                                                     \n");
		sb.append("}                                                                      \n");
		sb.append("" + identEres + ".addAll(" + identS + ");                                                        \n");
		sb.append("qres.push(" + identEres + ");                                                       \n");
		gen.printExpressionTrace("//OperatorSequence - end \n");
		return null;
	}
	
	@Override
	public Void visitStruct(OperatorStruct op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identEres = gen.generateIdentifier("eres");
		String identE1Res = gen.generateIdentifier("e1res");
		String identS = gen.generateIdentifier("s");
		gen.printExpressionTrace("//OperatorStruct - start \n");
		sb.append("Struct "+identEres+" = new Struct(); \n");
		sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
		sb.append("Struct "+identS+"; \n");
		sb.append("if("+identE1Res+" instanceof Struct) { \n");
		sb.append("	"+identS+" = (Struct) "+identE1Res+"; \n");
		sb.append("} else { \n");
		sb.append("	"+identS+" = new Struct(); \n");
		sb.append("	if("+identE1Res+" instanceof CollectionResult) { \n");
		sb.append("		"+identS+".addAll((CollectionResult) "+identE1Res+"); \n");
		sb.append("	} else { \n");
		sb.append("		"+identS+".add("+identE1Res+"); \n");
		sb.append("	} \n");
		sb.append(" \n");	
		sb.append("} \n");
		sb.append("eres.addAll("+identS+"); \n");
		sb.append("qres.push("+identEres+"); \n");
		gen.printExpressionTrace("//OperatorStruct - end \n");
		return null;
	}
	
	@Override
	public Void visitSum(OperatorSum op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identE1Res = gen.generateIdentifier("e1res");
		String identColRes = gen.generateIdentifier("colRes");
		String identSum = gen.generateIdentifier("sum");
		String identObject = gen.generateIdentifier("object");
		String identN = gen.generateIdentifier("n");
		gen.printExpressionTrace("//OperatorSum - start \n");
		sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
		sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
		sb.append("Number "+identSum+" = null; \n");
		sb.append("try { \n");
		sb.append("	for (QueryResult "+identObject+" : "+identColRes+") { \n");
		sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identObject+", store); \n");
		sb.append("		"+identSum+" = MathUtils.sum("+identSum+", "+identN+"); \n");
		sb.append("	} \n");
		sb.append("	qres.push(javaObjectFactory.createJavaComplexObject("+identSum+")); \n");
		sb.append(" \n");	
		sb.append("} catch(ClassCastException e) { \n");
		sb.append("	throw new RuntimeException(\"OperatorSum.eval() invalid type: type should be a primitive value\"); \n");
		sb.append("} \n");		
		gen.printExpressionTrace("//OperatorSum - end \n");
		return null;
	}
	
	@Override
	public Void visitUnion(OperatorUnion op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identEres = gen.generateIdentifier("eres");
		String identERight = gen.generateIdentifier("eRight");
		String identELeft = gen.generateIdentifier("eLeft");
		gen.printExpressionTrace("//OperatorUnion - start \n");
		sb.append("Bag "+identEres+" = new Bag(); \n");
		sb.append("CollectionResult "+identERight+" = Utils.objectToCollection(qres.pop()); \n");
		sb.append("CollectionResult "+identELeft+" = Utils.objectToCollection(qres.pop()); \n");
		sb.append(""+identEres+".addAll("+identELeft+"); \n");
		sb.append(""+identEres+".addAll("+identERight+"); \n");		
		sb.append("qres.push("+identEres+"); \n");
		gen.printExpressionTrace("//OperatorUnion - end \n");
		return null;
	}
	
	@Override
	public Void visitUnique(OperatorUnique op, QueryCodeGenSimple gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		String identObjects = gen.generateIdentifier("objects");
		String identResult = gen.generateIdentifier("result");
		String identTmp = gen.generateIdentifier("tmp");
		
		gen.printExpressionTrace("//OperatorUnique - start \n");
		sb.append("QueryResult "+identObjects+" = qres.pop(); \n"); 
		sb.append(""+identObjects+" = Utils.objectToCollection("+identObjects+"); \n");
		sb.append("QueryResult "+identResult+" = null; \n");
        sb.append(" \n");
		sb.append("if ("+identObjects+" instanceof Bag) { \n");
		sb.append("	"+identResult+" = new Bag(); \n");
		sb.append("} else if ("+identObjects+" instanceof Sequence) { \n");
		sb.append("	"+identResult+" = new Sequence(); \n");
		sb.append("} \n");
		sb.append("Set<QueryResult> "+identTmp+" = new LinkedHashSet<QueryResult>(); \n");
		sb.append(""+identTmp+".addAll((CollectionResult) "+identObjects+"); \n");
		sb.append("((CollectionResult) "+identResult+").addAll("+identTmp+"); \n");
		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorUnique - end \n");
		return null;
	}
	
}

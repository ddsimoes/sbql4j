package pl.wcislo.sbql4j.lang.codegen.noqres;

import pl.wcislo.sbql4j.java.model.compiletime.BinderSignature;
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

public class OperatorTreeCodeGenNoQres implements OperatorVisitor<Void, QueryCodeGenNoQres> {
	
	@Override
	public Void visitAnd(OperatorAnd op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResult = gen.generateIdentifier("result");
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorAnd - start \n");
		sb.append("Boolean "+identRightArg+" = (Boolean) Utils.toSimpleValue("+identRightRes+", store); \n");
		sb.append("Boolean "+identLeftArg+" = (Boolean) Utils.toSimpleValue("+identLeftRes+", store); \n");
		sb.append(opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+" && "+identRightArg+"); \n");
		gen.printExpressionTrace("//OperatorAnd - start \n");
		return null;
	}
	
	@Override
	public Void visitAvg(OperatorAvg op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression argExpr = subExprs[0];
		
		StringBuilder sb = gen.sb;
		String identAvgSum = gen.generateIdentifier("avgSum");
		String identArgRes = argExpr.getSignature().getResultName();
		String identColEl = gen.generateIdentifier("avgEl");
		String identN = gen.generateIdentifier("n");
		gen.printExpressionTrace("//OperatorAvg - start \n");
		if(argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
			sb.append("Number "+identAvgSum+" = null; \n");
			sb.append("	for (QueryResult "+identColEl+" : "+identArgRes+") { \n");
			sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identColEl+", store); \n");
			sb.append("		"+identAvgSum+" = MathUtils.sum("+identAvgSum+", "+identN+"); \n");
			sb.append("	} \n");
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identAvgSum+".doubleValue() / "+identArgRes+".size()); \n");
		} else {
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identArgRes+")); \n");
		}
		gen.printExpressionTrace("//OperatorAvg - end ");
		return null;
	}
	
	@Override
	public Void visitBag(OperatorBag op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		Expression genExpr = subExprs[1];
		
		StringBuilder sb = gen.sb;
		String identEres = opExpr.getSignature().getResultName();

		gen.printExpressionTrace("//OperatorBag - start");
		sb.append("Bag " + identEres + "; \n");
		if (genExpr == null) {
			sb.append("	" + identEres + " = new Bag();                                                \n");
		} else {
			//generic bag
			genExpr.accept(gen, null);
			String identGenExprRes = genExpr.getSignature().getResultName();
			String identJc = gen.generateIdentifier("jc");
			String identC = gen.generateIdentifier("c");
			sb.append("	JavaClass<Collection> " + identJc + " = (JavaClass<Collection>) " + identGenExprRes + "; \n");
			sb.append("	try { \n");
			sb.append("		Collection " + identC + " = " + identJc + ".value.newInstance(); \n");
			sb.append("		" + identEres + " = new Bag<Collection<QueryResult>>(" + identC + "); \n");
			sb.append("	} catch (Exception e) { \n");
			sb.append("		throw new SBQLException(e); \n");
			sb.append("	} \n");
		}
		String identArgRes = argExpr.getSignature().getResultName();
		if(argExpr.getSignature() instanceof StructSignature || argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
			sb.append("" + identEres + ".addAll(" + identArgRes + "); \n");
		} else {
			sb.append("" + identEres + ".add(" + identArgRes + "); \n");
		}
		gen.printExpressionTrace("//OperatorBag - end");	
		return null;
	}
	
	@Override
	public Void visitComma(OperatorComma op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		StringBuilder sb = gen.sb;
		String identRightVal = rightExpr.getSignature().getResultName();
		String identLeftVal = leftExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorComma - start \n");                                 
		if(opExpr.getSignature().getDerefSignatureWithCardinality().getColType() == SCollectionType.NO_COLLECTION) {
			sb.append(opExpr.getSignature().genSBQLDeclarationCode()+" = Utils.cartesianProductSingle("+identLeftVal+", "+identRightVal+"); \n");
		} else {
			sb.append(opExpr.getSignature().genSBQLDeclarationCode()+" = Utils.cartesianProduct("+identLeftVal+", "+identRightVal+"); \n");
		}
		gen.printExpressionTrace("//OperatorComma - end \n");	
		return null;
	}
	
	@Override
	public Void visitCount(OperatorCount op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		
		StringBuilder sb = gen.sb;
		String identResult = opExpr.getSignature().getResultName();
		String identColRes = argExpr.getSignature().getResultName();
		String identCount = gen.generateIdentifier("count");
		
		gen.printExpressionTrace("//OperatorCount - start \n");
		sb.append("int "+identCount+"; \n");
		
		if(argExpr.getSignature().getColType() == SCollectionType.NO_COLLECTION) {
			sb.append("	"+identCount+" = 1; \n");
		} else {
			sb.append("	"+identCount+" = "+identColRes+".size(); \n");	
		}
		sb.append("QueryResult "+identResult+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject("+identCount+"); \n");
		gen.printExpressionTrace("//OperatorCount - end");	
		return null;
	}
	
	@Override
	public Void visitDivide(OperatorDivide op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResultNum = gen.generateIdentifier("resultNum");
		String identResult = opExpr.getSignature().getResultName();
		gen.printExpressionTrace("//OperatorDivide - start \n");
		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue("+identRightRes+", store); \n");
		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue("+identLeftRes+", store); \n");
		sb.append("Number "+identResultNum+" = MathUtils.divide("+identLeftArg+", "+identRightArg+"); \n");
		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
//		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorDivide - end \n");
		return null;
	}
	
	@Override
	public Void visitElementAt(OperatorElementAt op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		StringBuilder sb = gen.sb;
		Signature leftSig = leftExpr.getSignature().getDerefSignatureWithCardinality();
		Signature rightSig = rightExpr.getSignature().getDerefSignatureWithCardinality();
		
//		String identRightVal = gen.generateIdentifier("rightVal");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		
		String identRightVal = rightExpr.getSignature().getResultName();
		String identLeftArg = leftExpr.getSignature().getResultName();
		
//		String identLeftCol = gen.generateIdentifier("leftCol");
		String identResult = opExpr.getSignature().getResultName();
		String identI = gen.generateIdentifier("i");
		String identIt = gen.generateIdentifier("it");
		
		gen.printExpressionTrace("//OperatorElementAt - start \n");
//		sb.append("QueryResult "+identRightVal+" = qres.pop();\n");
//		sb.append("QueryResult "+identLeftArg+" = qres.pop();\n");
//		sb.append("if("+identLeftArg+" instanceof Binder) {\n");
		if(leftExpr.getSignature() instanceof BinderSignature && leftExpr.getSignature().getColType() == SCollectionType.NO_COLLECTION) {
			sb.append("	"+identLeftArg+" = ((Binder)"+identLeftArg+").object;\n");
		}
//		sb.append("CollectionResult "+identLeftCol+" = (CollectionResult) "+identLeftArg+";\n");
//		if(!(rightVal instanceof Struct)) {
		if(!(rightSig instanceof StructSignature)) {
			sb.append("Integer "+identI+" = (Integer) Utils.toSimpleValue("+identRightVal+", store);\n");
			sb.append("QueryResult "+identResult+";\n");
			sb.append("if("+identLeftArg+".size() < "+identI+"+1) {\n");
			sb.append("	"+identResult+" = new "+leftSig.getColType().genSBQLDeclCode()+"();\n");
			sb.append("} else {\n");
			if(leftSig.getColType() == SCollectionType.SEQUENCE) {
//				sb.append("		"+identResult+" = ((Sequence)"+identLeftCol+").get("+identI+");\n");
				sb.append("		"+identResult+" = ((Sequence)"+identLeftArg+").get("+identI+");\n");
			} else {
				
				String identJ = gen.generateIdentifier("j");
				String identQr = gen.generateIdentifier("qr");
//				sb.append("		Iterator<QueryResult> "+identIt+" = "+identLeftCol+".iterator();\n");
				sb.append("		Iterator<QueryResult> "+identIt+" = "+identLeftArg+".iterator();\n");
				sb.append("		QueryResult "+identQr+" = "+identIt+".next();\n");
				sb.append("		for(int "+identJ+"=0; "+identJ+"<"+identI+"; "+identJ+"++) {\n");
				sb.append("			"+identQr+" = "+identIt+".next();\n");
				sb.append("		}\n");
				sb.append("		"+identResult+" = "+identQr+";\n");
			}
			sb.append("}\n");
//			sb.append("qres.push("+identResult+");\n");
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
			sb.append("Iterator<QueryResult> "+identIt+" = "+identLeftArg+".iterator();\n");
			sb.append("int "+identI+" = 0;\n");
			sb.append("while("+identIt+".hasNext() && "+identI+" <= "+identUpBound+") {\n");
			sb.append("	QueryResult el = "+identIt+".next();\n");
			sb.append("	if("+identI+" >= "+identLowBound+" && "+identI+" <= "+identUpBound+") {\n");
			sb.append("		"+identResult+".add(el);\n");
			sb.append("	}\n");
			sb.append("	"+identI+"++;\n");
			sb.append("}\n");
//			sb.append("qres.push("+identResult+");\n");
			gen.printExpressionTrace("//OperatorElementAt - start \n");
		}	
		return null;
	}
	
	@Override
	public Void visitEquals(OperatorEquals op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = opExpr.getSignature().getResultName();
		gen.printExpressionTrace("//OperatorEquals - start \n");
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store);\n");		
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store);\n");
		sb.append(" "+opExpr.getSignature().genSBQLDeclarationCode()+"; \n");
		sb.append(""+identResult+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject((Utils.equalsNumeric("+identLeftArg+", "+identRightArg+")));\n");
		gen.printExpressionTrace("//OperatorEquals - end \n");	
		return null;
	}
	
	@Override
	public Void visitExcept(OperatorExcept op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		String identResult = opExpr.getSignature().getResultName();
				
		Signature sLeft = leftExpr.getSignature();
		Signature sRight = rightExpr.getSignature();
		
		String identERight = sRight.getResultName();
		String identELeft = sLeft.getResultName();
		
		gen.printExpressionTrace("//OperatorExcept - start \n");
		sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = new "+opExpr.getSignature().getColType().genSBQLDeclCode()+"(); \n");
		sb.append(""+identResult+".addAll(CollectionUtils.subtract(");
		if(sLeft.getColType() != SCollectionType.NO_COLLECTION) {
			sb.append(identELeft);
		} else {
			sb.append("Utils.objectToCollection("+identELeft+")");
		}
		sb.append(", ");
		if(sRight.getColType() != SCollectionType.NO_COLLECTION) {
			sb.append(identERight);
		} else {
			sb.append("Utils.objectToCollection("+identERight+")");
		}
		sb.append(")); \n");	
		gen.printExpressionTrace("//OperatorExcept - end \n");	
		return null;
	}
	
	@Override
	public Void visitExists(OperatorExists op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		StringBuilder sb = gen.sb;
		String identArgRes = argExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorExists - start\n");
		if(argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject(!"+identArgRes+".isEmpty()); \n");
		} else {
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject("+identArgRes+" != null); \n");
		}
		gen.printExpressionTrace("//OperatorExists - end\n");	
		return null;
	}
	
	@Override
	public Void visitIn(OperatorIn op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		Signature leftSig = leftExpr.getSignature();
		Signature rightSig = rightExpr.getSignature();
		Signature opSig = opExpr.getSignature();
		
		boolean isLeftCol = leftSig.getColType() != SCollectionType.NO_COLLECTION;
		boolean isRightCol = rightSig.getColType() != SCollectionType.NO_COLLECTION;
		
		String identLeftRes = leftSig.getResultName();
		String identRightRes = rightSig.getResultName();
		
		gen.printExpressionTrace("//OperatorIn - start \n");
		if(isLeftCol && isRightCol) {
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaObject("+identRightRes+".containsAll("+identLeftRes+")); \n");
		} else {
			String identLeftCol = gen.generateIdentifier("inLeftCol");
			String identRightCol = gen.generateIdentifier("inRightCol");
			sb.append("CollectionResult "+identLeftCol+" = objectToCollection("+identLeftRes+"); \n");
			sb.append("CollectionResult "+identRightCol+" = objectToCollection("+identRightRes+"); \n");
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaObject("+identRightCol+".containsAll("+identLeftCol+")); \n");
		}
		gen.printExpressionTrace("//OperatorIn - end \n");	
		return null;
	}
	
	@Override
	public Void visitInstanceof(OperatorInstanceof op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		String identClazz = gen.generateIdentifier("clazz");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		String identRes = opExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorInstanceof - start \n");
		sb.append("Class "+identClazz+" = (Class) Utils.toSimpleValue("+rightExpr.getSignature().getResultName()+", store); \n");	
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+leftExpr.getSignature().getResultName()+", store); \n");
		sb.append("Boolean "+identResult+" = "+identClazz+".isInstance("+identLeftArg+"); \n");
		sb.append("QueryResult "+identRes+" = javaObjectFactory.createJavaComplexObject("+identResult+"); \n");
//		sb.append("qres.push("+identRes+"); \n");
		gen.printExpressionTrace("//OperatorInstanceof - end \n");	
		return null;
	}
	
	@Override
	public Void visitIntersect(OperatorIntersect op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		StringBuilder sb = gen.sb;
		String identResult = opExpr.getSignature().getResultName();
				
		Signature sLeft = leftExpr.getSignature();
		Signature sRight = rightExpr.getSignature();
		
		String identERight = sRight.getResultName();
		String identELeft = sLeft.getResultName();
		
		gen.printExpressionTrace("//OperatorIntersect - start \n");
		sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = new "+opExpr.getSignature().getColType().genSBQLDeclCode()+"(); \n");
		sb.append(""+identResult+".addAll(CollectionUtils.intersection(");
		if(sLeft.getColType() != SCollectionType.NO_COLLECTION) {
			sb.append(identELeft);
		} else {
			sb.append("Utils.objectToCollection("+identELeft+")");
		}
		sb.append(", ");
		if(sRight.getColType() != SCollectionType.NO_COLLECTION) {
			sb.append(identERight);
		} else {
			sb.append("Utils.objectToCollection("+identERight+")");
		}
		sb.append(")); \n");
		gen.printExpressionTrace("//OperatorIntersect - end \n");	
		return null;
	}
	
	@Override
	public Void visitLess(OperatorLess op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		String identN1 = gen.generateIdentifier("n1");
		String identN2 = gen.generateIdentifier("n2");
		String identC1 = gen.generateIdentifier("c1");
		String identC2 = gen.generateIdentifier("c2");
		String identCRes = gen.generateIdentifier("cRes");
		String identRes = opExpr.getSignature().getResultName();
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorLess - start\n");
		sb.append(" "+opExpr.getSignature().genSBQLDeclarationCode()+"; \n");
		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store); \n");
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store); \n");
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
		sb.append(" "+identRes+" = JavaObjectAbstractFactory.getJavaObjectFactory(). \n");
		sb.append("	createJavaComplexObject("+identResult+");                                         \n");
		gen.printExpressionTrace("//OperatorLess - end\n");	
		return null;
	}
	
	@Override
	public Void visitLessOrEqual(OperatorLessOrEqual op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		String identN1 = gen.generateIdentifier("n1");
		String identN2 = gen.generateIdentifier("n2");
		String identC1 = gen.generateIdentifier("c1");
		String identC2 = gen.generateIdentifier("c2");
		String identCRes = gen.generateIdentifier("cRes");
		String identRes = opExpr.getSignature().getResultName();
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorLess - start\n");
		sb.append(" "+opExpr.getSignature().genSBQLDeclarationCode()+"; \n");
		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store); \n");
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store); \n");
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
		sb.append(" "+identRes+" = JavaObjectAbstractFactory.getJavaObjectFactory(). \n");
		sb.append("	createJavaComplexObject("+identResult+");                                         \n");
		gen.printExpressionTrace("//OperatorLess - end\n");	
		return null;
	}
	
	@Override
	public Void visitMax(OperatorMax op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		
		
		StringBuilder sb = gen.sb;
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identColRes = gen.generateIdentifier("colRes");
		String identMax = gen.generateIdentifier("max");
		String identArgRes = argExpr.getSignature().getResultName();
		String identColEl = gen.generateIdentifier("maxEl");
		String identN = gen.generateIdentifier("n");
		gen.printExpressionTrace("//OperatorMax - start \n");
		if(argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
			sb.append("Number "+identMax+" = null; \n");
			sb.append("	for (QueryResult "+identColEl+" : "+identArgRes+") { \n");
			sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identColEl+", store); \n");
			sb.append("		"+identMax+" = MathUtils.max("+identMax+", "+identN+"); \n");
			sb.append("	} \n");
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identMax+"); \n");
		} else {
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identArgRes+")); \n");
		}
		gen.printExpressionTrace("//OperatorMax - end \n");	
		return null;
	}
	
	@Override
	public Void visitMin(OperatorMin op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression argExpr = subExprs[0];
		
		StringBuilder sb = gen.sb;
		String identMin = gen.generateIdentifier("min");
		String identArgRes = argExpr.getSignature().getResultName();
		String identColEl = gen.generateIdentifier("minEl");
		String identN = gen.generateIdentifier("n");
		gen.printExpressionTrace("//OperatorMin - start \n");
		if(argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
			sb.append("Number "+identMin+" = null; \n");
			sb.append("	for (QueryResult "+identColEl+" : "+identArgRes+") { \n");
			sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identColEl+", store); \n");
			sb.append("		"+identMin+" = MathUtils.min("+identMin+", "+identN+"); \n");
			sb.append("	} \n");
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identMin+"); \n");
		} else {
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identArgRes+")); \n");
		}
		gen.printExpressionTrace("//OperatorMin - end \n");	
		return null;
	}
	
	@Override
	public Void visitMinus(OperatorMinus op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
//		Expression opExpr = args[2];
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResultNum = gen.generateIdentifier("resultNum");
//		String identResult = opExpr.signature.getResultName();
		gen.printExpressionTrace("//OperatorMinus - start \n");
		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue("+identRightRes+", store); \n");
		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue("+identLeftRes+", store); \n");
		sb.append("Number "+identResultNum+" = MathUtils.subtract("+identLeftArg+", "+identRightArg+"); \n");
		sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
//		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorMinus - end \n");	
		return null;
	}
	
	@Override
	public Void visitModulo(OperatorModulo op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResultNum = gen.generateIdentifier("resultNum");
		String identResult = opExpr.getSignature().getResultName();
		gen.printExpressionTrace("//OperatorModulo - start \n");
		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue("+identRightRes+", store); \n");
		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue("+identLeftRes+", store); \n");
		sb.append("Number "+identResultNum+" = MathUtils.modulo("+identLeftArg+", "+identRightArg+"); \n");
		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
//		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorModulo - end \n");
		return null;
	}
	
	@Override
	public Void visitMore(OperatorMore op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		String identN1 = gen.generateIdentifier("n1");
		String identN2 = gen.generateIdentifier("n2");
		String identC1 = gen.generateIdentifier("c1");
		String identC2 = gen.generateIdentifier("c2");
		String identCRes = gen.generateIdentifier("cRes");
		String identRes = opExpr.getSignature().getResultName();
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorMore - start\n");
		sb.append(" "+opExpr.getSignature().genSBQLDeclarationCode()+"; \n");
		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store); \n");
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store); \n");
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
		sb.append(" "+identRes+" = JavaObjectAbstractFactory.getJavaObjectFactory(). \n");
		sb.append("	createJavaComplexObject("+identResult+");                                         \n");
//		sb.append("qres.push("+identRes+");		                                                     \n");
		gen.printExpressionTrace("//OperatorMore - end\n");		
		return null;
	}
	
	@Override
	public Void visitMoreOrEqual(OperatorMoreOrEqual op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = gen.generateIdentifier("result");
		String identN1 = gen.generateIdentifier("n1");
		String identN2 = gen.generateIdentifier("n2");
		String identC1 = gen.generateIdentifier("c1");
		String identC2 = gen.generateIdentifier("c2");
		String identCRes = gen.generateIdentifier("cRes");
		String identRes = opExpr.getSignature().getResultName();
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorMoreOrEqual - start\n");
		sb.append(" "+opExpr.getSignature().genSBQLDeclarationCode()+"; \n");
		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store); \n");
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store); \n");
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
		sb.append(" "+identRes+" = JavaObjectAbstractFactory.getJavaObjectFactory(). \n");
		sb.append("	createJavaComplexObject("+identResult+");                                         \n");
//		sb.append("qres.push("+identRes+");		                                                     \n");
		gen.printExpressionTrace("//OperatorMoreOrEqual - end\n");	
		return null;
	}
	
	@Override
	public Void visitMultiply(OperatorMultiply op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
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
	public Void visitNot(OperatorNot op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
//		Expression genericTypeExpr = subExprs[1];
		
		StringBuilder sb = gen.sb;
		String identB = gen.generateIdentifier("b");
		gen.printExpressionTrace("//OperatorNot - start \n");
		sb.append("Boolean "+identB+" = (Boolean) Utils.toSimpleValue("+argExpr.getSignature().getResultName()+", store); \n");
		sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject(!"+identB+")); \n");
		gen.printExpressionTrace("//OperatorNot - start \n");	
		return null;
	}
	
	@Override
	public Void visitNotEquals(OperatorNotEquals op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		
		StringBuilder sb = gen.sb;
		String identRightArg = rightExpr.getSignature().getResultName();
		String identLeftArg = leftExpr.getSignature().getResultName();
		String identResult = opExpr.getSignature().getResultName();
		gen.printExpressionTrace("//OperatorEquals - start \n");
		String identResType = opExpr.getSignature().getJavaTypeString();
		String identResName = opExpr.getSignature().getResultName();
		sb.append(""+identResType+" "+identResName+" = !OperatorUtils.equalsSafe("+identLeftArg+", "+identRightArg+"); \n");
		gen.printExpressionTrace("//OperatorEquals - end \n");	
		return null;
	}
	
	@Override
	public Void visitOr(OperatorOr op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResult = gen.generateIdentifier("result");
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorOr - start \n");
		sb.append("Boolean "+identRightArg+" = (Boolean) Utils.toSimpleValue("+identRightRes+", store); \n");
		sb.append("Boolean "+identLeftArg+" = (Boolean) Utils.toSimpleValue("+identLeftRes+", store); \n");
		sb.append(opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+" || "+identRightArg+"); \n");
//		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorOr - end \n");
		return null;
	}
	
	@Override
	public Void visitPlus(OperatorPlus op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		StringBuilder sb = gen.sb;
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = opExpr.getSignature().getResultName();
		String identResultNum = gen.generateIdentifier("resultNum");
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorPlus - start \n");  
		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store); \n");
		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store); \n");
		sb.append("QueryResult "+identResult+"; \n");
		sb.append("if("+identLeftArg+" instanceof String || "+identRightArg+" instanceof String) { \n");
		sb.append("	"+identResult+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+".toString().concat("+identRightArg+".toString())); \n");
		sb.append("} else { \n");
		sb.append("	Number "+identResultNum+" = MathUtils.sum((Number)"+identLeftArg+", (Number)"+identRightArg+"); \n");
		sb.append("	"+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
		sb.append("} \n");
//		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorPlus - end \n");
		return null;
	}
	
	@Override
	public Void visitSequence(OperatorSequence op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		Expression genExpr = subExprs[1];
		
		StringBuilder sb = gen.sb;
		String identEres = opExpr.getSignature().getResultName();

		gen.printExpressionTrace("//OperatorSequence - start");
		sb.append("Sequence " + identEres + "; \n");
		if (genExpr == null) {
			sb.append("	" + identEres + " = new Sequence();                                                \n");
		} else {
			//generic bag
			genExpr.accept(gen, null);
			String identGenExprRes = genExpr.getSignature().getResultName();
			String identJc = gen.generateIdentifier("jc");
			String identC = gen.generateIdentifier("c");
			sb.append("	JavaClass<Collection> " + identJc + " = (JavaClass<Collection>) " + identGenExprRes + "; \n");
			sb.append("	try { \n");
			sb.append("		Collection " + identC + " = " + identJc + ".value.newInstance(); \n");
			sb.append("		" + identEres + " = new Sequence<Collection<QueryResult>>(" + identC + "); \n");
			sb.append("	} catch (Exception e) { \n");
			sb.append("		throw new SBQLException(e); \n");
			sb.append("	} \n");
		}
		String identArgRes = argExpr.getSignature().getResultName();
		if(argExpr.getSignature() instanceof StructSignature || argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
			sb.append("" + identEres + ".addAll(" + identArgRes + "); \n");
		} else {
			sb.append("" + identEres + ".add(" + identArgRes + "); \n");
		}
		gen.printExpressionTrace("//OperatorSequence - end");
		return null;
	}
	
	@Override
	public Void visitStruct(OperatorStruct op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.sb;
		
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
//		Expression genExpr = subExprs[1];
		
		String identEres = opExpr.getSignature().getResultName();
		String identArgRes = argExpr.getSignature().getResultName();
//		String identE1Res = gen.generateIdentifier("e1res");
		String identS = gen.generateIdentifier("s");
		gen.printExpressionTrace("//OperatorStruct - start \n");
		sb.append("Struct "+identEres+" = new Struct(); \n");
//		sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
		sb.append("Struct "+identS+"; \n");
		sb.append("if("+identArgRes+" instanceof Struct) { \n");
		sb.append("	"+identS+" = (Struct) "+identArgRes+"; \n");
		sb.append("} else { \n");
		sb.append("	"+identS+" = new Struct(); \n");
		sb.append("	if("+identArgRes+" instanceof CollectionResult) { \n");
		sb.append("		"+identS+".addAll((CollectionResult) "+identArgRes+"); \n");
		sb.append("	} else { \n");
		sb.append("		"+identS+".add("+identArgRes+"); \n");
		sb.append("	} \n");
		sb.append(" \n");	
		sb.append("} \n");
		sb.append("eres.addAll("+identS+"); \n");
//		sb.append("qres.push("+identEres+"); \n");
		gen.printExpressionTrace("//OperatorStruct - end \n");	
		return null;
	}
	
	@Override
	public Void visitSum(OperatorSum op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression argExpr = subExprs[0];
		
		StringBuilder sb = gen.sb;
		String identSum = gen.generateIdentifier("sum");
		String identArgRes = argExpr.getSignature().getResultName();
		String identColEl = gen.generateIdentifier("sumEl");
		String identN = gen.generateIdentifier("n");
		gen.printExpressionTrace("//OperatorSum - start \n");
		if(argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
			sb.append("Number "+identSum+" = null; \n");
			sb.append("	for (QueryResult "+identColEl+" : "+identArgRes+") { \n");
			sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identColEl+", store); \n");
			sb.append("		"+identSum+" = MathUtils.sum("+identSum+", "+identN+"); \n");
			sb.append("	} \n");
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identSum+"); \n");
		} else {
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identArgRes+")); \n");
		}
		gen.printExpressionTrace("//OperatorSum - end \n");	
		return null;
	}
	
	@Override
	public Void visitUnion(OperatorUnion op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		Signature sLeft = leftExpr.getSignature();
		Signature sRight = rightExpr.getSignature();
		
		StringBuilder sb = gen.sb;
		String identEres = opExpr.getSignature().getResultName();
		String identERight = sRight.getResultName();
		String identELeft = sLeft.getResultName();
		gen.printExpressionTrace("//OperatorUnion - start \n");
//		sb.append("Bag "+identEres+" = new Bag(); \n");
		sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = new "+opExpr.getSignature().getColType().genSBQLDeclCode()+"(); \n");
		if(sLeft.getColType() != SCollectionType.NO_COLLECTION) {
			sb.append(""+identEres+".addAll("+identELeft+"); \n");
		} else {
			sb.append(""+identEres+".add("+identELeft+"); \n");
		}
		if(sRight.getColType() != SCollectionType.NO_COLLECTION) {
			sb.append(""+identEres+".addAll("+identERight+"); \n");
		} else {
			sb.append(""+identEres+".add("+identERight+"); \n");
		}
		gen.printExpressionTrace("//OperatorUnion - end \n");	
		return null;
	}
	
	@Override
	public Void visitUnique(OperatorUnique op, QueryCodeGenNoQres gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression rightExpr = subExprs[0];
		
		
		StringBuilder sb = gen.sb;
		String identObjects = gen.generateIdentifier("objects");
		String identResult = opExpr.getSignature().getResultName();
		String identTmp = gen.generateIdentifier("tmp");
		String identRightRes = rightExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorUnique - start \n");
		if(opExpr.getSignature().getColType() == SCollectionType.NO_COLLECTION) {
			//only one element - no changes
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = "+identRightRes+"; \n");
		} else {
			sb.append(""+opExpr.getSignature().genSBQLDeclarationCode()+" = new "+opExpr.getSignature().getColType().genSBQLDeclCode()+"(); \n");
			sb.append("Set<QueryResult> "+identTmp+" = new LinkedHashSet<QueryResult>(); \n");
			sb.append(""+identTmp+".addAll((CollectionResult) "+identRightRes+"); \n");
			sb.append(""+identResult+".addAll("+identTmp+"); \n");
		}
		gen.printExpressionTrace("//OperatorUnique - end \n");	
		return null;
	}
}

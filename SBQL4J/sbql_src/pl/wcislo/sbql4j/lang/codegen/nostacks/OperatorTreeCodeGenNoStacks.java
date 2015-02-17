package pl.wcislo.sbql4j.lang.codegen.nostacks;

import pl.wcislo.sbql4j.java.model.compiletime.BinderSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ClassSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.StructSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.compiletime.XMLSimpleTypeWithAttributesSignature;
import pl.wcislo.sbql4j.lang.codegen.QueryCodeGenerator;
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
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.xml.utils.XMLUtils;

public class OperatorTreeCodeGenNoStacks implements OperatorVisitor<Void, QueryCodeGenerator> {
	
	@Override
	public Void visitAnd(OperatorAnd op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		
		StringBuilder sb = gen.getAppender();
		String identRes = opExpr.getSignature().getResultName();
		
		
		
		gen.printExpressionTrace("//OperatorAnd - start", opExpr);
		leftExpr.accept(gen, null);
		String identLeftArg = leftExpr.getSignature().getResultName();
		identLeftArg = XMLUtils.genDerefCodeIfNecessary(identLeftArg, leftExpr.getSignature(), gen, sb);
		sb.append(opExpr.getSignature().genJavaDeclarationCode()+"; \n");
		sb.append("if(!"+identLeftArg+") {\n");
		sb.append(identRes+" = false;\n");
		sb.append("} else {\n");
		rightExpr.accept(gen, null);
		String identRightArg = rightExpr.getSignature().getResultName();
		identRightArg = XMLUtils.genDerefCodeIfNecessary(identRightArg, rightExpr.getSignature(), gen, sb);
		sb.append(identRes+" = "+identRightArg+";\n");
		sb.append("}\n");
//		sb.append("Boolean "+identRightArg+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append("Boolean "+identLeftArg+" = (Boolean) Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append(opExpr.getSignature().genJavaDeclarationCode()+" = "+identLeftArg+" && "+identRightArg+"; \n");
//		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorAnd - end", opExpr);
		return null;
	}
	
	@Override
	public Void visitAvg(OperatorAvg op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		commonVisitUnarySubexpr(argExpr, gen);
		
		StringBuilder sb = gen.getAppender();
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identColRes = gen.generateIdentifier("colRes");
		String identAvgSum = gen.generateIdentifier("avgSum");
		String identArgRes = argExpr.getSignature().getResultName();
		String identColEl = gen.generateIdentifier("avgEl");
		String identN = gen.generateIdentifier("n");
		gen.printExpressionTrace("//OperatorAvg - start", opExpr);
		identArgRes = XMLUtils.genDerefCodeIfNecessary(identArgRes, argExpr.getSignature(), gen, sb);
		if(argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = 0d; \n");
			sb.append("if("+identArgRes+" != null && !"+identArgRes+".isEmpty()) {\n");
			sb.append("    Number "+identAvgSum+" = null; \n");
			sb.append("	   for (Number "+identColEl+" : "+identArgRes+") { \n");
			sb.append("        "+identAvgSum+" = MathUtils.sum("+identAvgSum+", "+identColEl+"); \n");
			sb.append("	   } \n");
			sb.append("    "+opExpr.getSignature().getResultName()+" = "+identAvgSum+".doubleValue() / "+identArgRes+".size(); \n");
			sb.append("} \n");
		} else {
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identArgRes+"; \n");
		}
		gen.printExpressionTrace("//OperatorAvg - end", opExpr);
		return null;
	}
	
	@Override
	public Void visitBag(OperatorBag op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		Expression genExpr = subExprs[1];
		commonVisitUnarySubexpr(argExpr, gen);
		if(genExpr != null) {
			commonVisitUnarySubexpr(genExpr, gen);
		}
		
		StringBuilder sb = gen.getAppender();
		String identEres = opExpr.getSignature().getResultName();
		String argType = argExpr.getSignature().getJavaTypeString();
		boolean isArgCol = argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION;
//		String identEres = opExpr.signature.getResultName();

		gen.printExpressionTrace("//OperatorBag - start", opExpr);
//		sb.append("Bag " + identEres + "; \n");
		if (genExpr == null) {
			sb.append(""+ opExpr.getSignature().genJavaDeclarationCode() + " = new ArrayList();\n");
		} else {
			//generic bag
			genExpr.accept(gen, null);
			String identGenExprRes = genExpr.getSignature().getResultName();
			String identJc = gen.generateIdentifier("jc");
			String identC = gen.generateIdentifier("c");
			sb.append("	try { \n");
			sb.append("	Collection<"+argType+">" + identEres + " = ");
			genExpr.accept(gen, null);
			sb.append(".newInstance(); \n");
			sb.append("	} catch (Exception e) { \n");
			sb.append("		throw new SBQLException(e); \n");
			sb.append("	} \n");
		}
		String identArgRes = argExpr.getSignature().getResultName();
		if(argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
			sb.append("" + identEres + ".addAll(" + identArgRes + "); \n");
		} else if(argExpr.getSignature() instanceof StructSignature) {
			sb.append("" + identEres + ".addAll(" + identArgRes + ".values()); \n");
		} else {
			sb.append("" + identEres + ".add(" + identArgRes + "); \n");
		}
		gen.printExpressionTrace("//OperatorBag - end", opExpr);		
		return null;
	}
	
	@Override
	public Void visitComma(OperatorComma op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
		StringBuilder sb = gen.getAppender();
		String identRightVal = rightExpr.getSignature().getResultName();
		String identLeftVal = leftExpr.getSignature().getResultName();
		String identOpResult = opExpr.getSignature().getResultName();
		
		String leftName = (leftExpr.getSignature() instanceof BinderSignature) ? ((BinderSignature)leftExpr.getSignature()).name : "";
		String rightName = (rightExpr.getSignature() instanceof BinderSignature) ? ((BinderSignature)rightExpr.getSignature()).name : "";
		
		boolean isLeftCol = leftExpr.getSignature().isCollectionResult();
		boolean isRightCol = rightExpr.getSignature().isCollectionResult();
		
		gen.printExpressionTrace("//OperatorComma - start", opExpr);
		sb.append(opExpr.getSignature().genJavaDeclarationCode()+" = OperatorUtils.");
		if(isLeftCol ) {
			if(isRightCol ) {
				sb.append("cartesianProductCC");
			} else {
				sb.append("cartesianProductCS");
			}
		} else {
			if(isRightCol ) {
				sb.append("cartesianProductSC");
			} else {
				sb.append("cartesianProductSS");
			}
		}
		sb.append("("+identLeftVal+", "+identRightVal+", \""+leftName+"\", \""+rightName+"\"); \n");
		gen.printExpressionTrace("//OperatorComma - end", opExpr);		
		return null;
	}
	
	@Override
	public Void visitCount(OperatorCount op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		commonVisitUnarySubexpr(argExpr, gen);
		
		StringBuilder sb = gen.getAppender();
		String identResult = opExpr.getSignature().getResultName();
		String identColRes = argExpr.getSignature().getResultName();
		String identCount = opExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorCount - start", opExpr);
//		sb.append("int "+identCount+"; \n");
		
		if(argExpr.getSignature().getColType() == SCollectionType.NO_COLLECTION) {
			sb.append(opExpr.getSignature().genJavaDeclarationCode()+" = 1; \n");
		} else {
			sb.append(opExpr.getSignature().genJavaDeclarationCode()+" = "+identColRes+".size(); \n");	
		}
//		sb.append("QueryResult "+identResult+" = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject("+identCount+"); \n");
		gen.printExpressionTrace("//OperatorCount - end", opExpr);
		return null;
	}
	
	@Override
	public Void visitDivide(OperatorDivide op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		StringBuilder sb = gen.getAppender();
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResultNum = gen.generateIdentifier("resultNum");
		String identResult = opExpr.getSignature().getResultName();
		gen.printExpressionTrace("//OperatorDivide - start", opExpr);
		identLeftArg = XMLUtils.genDerefCodeIfNecessary(identLeftArg, leftExpr.getSignature(), gen, sb);
		identRightArg = XMLUtils.genDerefCodeIfNecessary(identRightArg, rightExpr.getSignature(), gen, sb);
//		sb.append(""+opExpr.signature.genJavaDeclarationCode()+" = "+identLeftRes+" / "+identRightRes+"; \n");
//		opExpr.getSignature().setResultName("("+identLeftRes+" / "+identRightRes+")");
		sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identLeftRes+" / "+identRightRes+"; \n");
//		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue("+identRightRes+", store); \n");
//		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue("+identLeftRes+", store); \n");
//		sb.append("Number "+identResultNum+" = MathUtils.divide("+identLeftArg+", "+identRightArg+"); \n");
//		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
//		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorDivide - end", opExpr);	
		return null;
	}

	@Override
	public Void visitElementAt(OperatorElementAt op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.getAppender();
		Expression leftExpr = subExprs[0];
		commonVisitUnarySubexpr(leftExpr, gen);
		Expression rightExpr = subExprs[1];
		if(rightExpr != null) {
			commonVisitUnarySubexpr(rightExpr, gen);
		}
//		Expression opExpr = args[2];
		Signature leftSig = leftExpr.getSignature();
		Signature rightSig = rightExpr.getSignature();
		Signature opSig = opExpr.getSignature();
		
		String identResult = opSig.getResultName();
		
		gen.printExpressionTrace("//OperatorElementAt - start", opExpr);
		sb.append(""+opSig.genJavaDeclarationCode()+"; \n");
//		sb.append("	if("+leftSig.getResultName()+".isEmpty()) {\n");
		sb.append("	"+identResult+" = ");
		if(opSig.getColType() != SCollectionType.NO_COLLECTION) {
			sb.append("new ArrayList(); \n");
		} else {
			sb.append("null; \n");
		}
//		sb.append("	} else { \n");
		sb.append("if(!"+leftSig.getResultName()+".isEmpty()) {\n");
		if(!(rightSig instanceof StructSignature)) {
			String identRightArg = rightSig.getResultName();
			identRightArg = XMLUtils.genDerefCodeIfNecessary(identRightArg, rightSig, gen, sb);
			if(leftSig.getColType() == SCollectionType.SEQUENCE) {
				sb.append(""+identResult+" = "+leftSig.getResultName()+".get("+identRightArg+");\n");	
			} else {
				String identJ = gen.generateIdentifier("j");
				String identQr = gen.generateIdentifier("elementAtEl");
				String identIt = gen.generateIdentifier("elementAtIteraotr");
//				sb.append("		"+opSig.genJavaDeclarationCode()+"; \n");
				sb.append("		Iterator<"+leftExpr.getSignature().getJavaTypeStringSingleResult()+"> "+identIt+" = "+leftSig.getResultName()+".iterator();\n");
//				sb.append("		QueryResult "+identQr+" = "+identIt+".next();\n");
				sb.append("		for(int "+identJ+"=0; "+identJ+"<"+identRightArg+"+1; "+identJ+"++) {\n");
				sb.append("			"+opSig.getResultName()+" = "+identIt+".next();\n");
				sb.append("		}\n");
//				sb.append("		"+identResult+" = "+identQr+";\n");
			}
//			sb.append("}\n");
//			sb.append("qres.push("+identResult+");\n");
		} else {
			sb.append(""+identResult+" = new "+opSig.getJavaTypeStringAssigment()+"(); \n");
			String identBounds = gen.generateIdentifier("bounds");
			String identLowBound = gen.generateIdentifier("lowBound");
			String identUpBound = gen.generateIdentifier("upBound");
			
			StructSignature boundsSig = (StructSignature) rightSig;
//			sb.append("Struct "+identBounds+" = (Struct) "+identRightVal+";\n");
			sb.append("Integer "+identLowBound+" = "+boundsSig.getFields()[0].getResultName()+"; \n");
//			sb.append("Integer "+identUpBound+";\n");
			if(boundsSig.getFields().length < 2) {
				sb.append("Integer "+identUpBound+" = "+leftSig.getResultName()+".size()-1; \n");
			} else {
				sb.append("Integer "+identUpBound+" = "+boundsSig.getFields()[1].getResultName()+" ;\n");
			}
			
			if(leftSig.getColType() == SCollectionType.SEQUENCE) {
				sb.append("	if("+identUpBound+" >= "+leftSig.getResultName()+".size()) {\n");
				sb.append("	"+identUpBound+" = "+leftSig.getResultName()+".size() - 2; \n");
				sb.append("	} \n");
				sb.append("	"+opSig.getResultName()+" = "+leftSig.getResultName()+".subList("+identLowBound+", "+identUpBound+"+1);\n");
			} else {
				String identIt = gen.generateIdentifier("elementAtIteraotr");
				String identI = gen.generateIdentifier("i");
				sb.append("Iterator<"+leftExpr.getSignature().getJavaTypeStringSingleResult()+"> "+identIt+" = "+leftSig.getResultName()+".iterator();\n");
				sb.append("int "+identI+" = 0;\n");
				sb.append("while("+identIt+".hasNext() && "+identI+" <= "+identUpBound+") {\n");
				sb.append("	"+leftExpr.getSignature().getJavaTypeStringSingleResult()+" el = "+identIt+".next();\n");
				sb.append("	if("+identI+" >= "+identLowBound+" && "+identI+" <= "+identUpBound+") {\n");
				sb.append("		"+opSig.getResultName()+".add(el);\n");
				sb.append("	}\n");
				sb.append("	"+identI+"++;\n");
				sb.append("}\n");
			}
		}	
		sb.append("	} \n");
		gen.printExpressionTrace("//OperatorElementAt - end", opExpr);
		return null;
	}

	@Override
	public Void visitEquals(OperatorEquals op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		StringBuilder sb = gen.getAppender();
		String identRightArg = rightExpr.getSignature().getResultName();
		String identLeftArg = leftExpr.getSignature().getResultName();
		String identResult = opExpr.getSignature().getResultName();
		gen.printExpressionTrace("//OperatorEquals - start", opExpr);
		identLeftArg = XMLUtils.genDerefCodeIfNecessary(identLeftArg, leftExpr.getSignature(), gen, sb);
		identRightArg = XMLUtils.genDerefCodeIfNecessary(identRightArg, rightExpr.getSignature(), gen, sb);
		String identResType = opExpr.getSignature().getJavaTypeString();
		String identResName = opExpr.getSignature().getResultName();
		sb.append(""+identResType+" "+identResName+" = OperatorUtils.equalsSafe("+identLeftArg+", "+identRightArg+"); \n");
		gen.printExpressionTrace("//OperatorEquals - end", opExpr);		
		return null;
	}
	
	@Override
	public Void visitExcept(OperatorExcept op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.getAppender();
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
//		Expression opExpr = args[2];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		String identResult = opExpr.getSignature().getResultName();
				
		Signature sLeft = leftExpr.getSignature();
		Signature sRight = rightExpr.getSignature();
		
		String identERight = sRight.getResultName();
		String identELeft = sLeft.getResultName();
		
		String identLeftCol = gen.generateIdentifier("minusLeftCol");
		String identRightCol = gen.generateIdentifier("minusRightCol");
		
		boolean isLeftCol = sLeft.getColType() != SCollectionType.NO_COLLECTION;
		boolean isRightCol = sRight.getColType() != SCollectionType.NO_COLLECTION;
		
		gen.printExpressionTrace("//OperatorExcept - start", opExpr);
		sb.append("Collection "+identLeftCol+" = ");
		if(isLeftCol) {
			sb.append("new ArrayList("+identELeft+"); \n");
		} else {
			sb.append("new ArrayList(); \n");
			sb.append(identLeftCol+".add("+identELeft+"); \n");
		}
		sb.append("Collection "+identRightCol+" = ");
		if(isRightCol) {
			sb.append("new ArrayList("+identERight+"); \n");
		} else {
			sb.append("new ArrayList(); \n");
			sb.append(identRightCol+".add("+identERight+"); \n");
		}
		
		sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = new "+opExpr.getSignature().getJavaTypeStringAssigment()+"(); \n");
		sb.append(""+identResult+".addAll(CollectionUtils.subtract("+identLeftCol+", "+identRightCol+")); \n");
		gen.printExpressionTrace("//OperatorExcept - end", opExpr);	
		return null;
	}
	
	@Override
	public Void visitExists(OperatorExists op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		commonVisitUnarySubexpr(argExpr, gen);
		StringBuilder sb = gen.getAppender();
		String identArgRes = argExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorExists - start", opExpr);
		if(argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = !"+identArgRes+".isEmpty(); \n");
		} else {
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identArgRes+" != null; \n");
		}
		gen.printExpressionTrace("//OperatorExists - end", opExpr);		
		return null;
	}
	
	@Override
	public Void visitIn(OperatorIn op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.getAppender();
		
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
		Signature leftSig = leftExpr.getSignature();
		Signature rightSig = rightExpr.getSignature();
		Signature opSig = opExpr.getSignature();
		
		boolean isLeftCol = leftSig.getColType() != SCollectionType.NO_COLLECTION;
		boolean isRightCol = rightSig.getColType() != SCollectionType.NO_COLLECTION;
		
		String identLeftRes = leftSig.getResultName();
		String identRightRes = rightSig.getResultName();
		
		gen.printExpressionTrace("//OperatorIn - start", opExpr);
		if(isLeftCol && isRightCol) {
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identRightRes+".containsAll("+identLeftRes+"); \n");
		} else {
			String identLeftCol = gen.generateIdentifier("inLeftCol");
			String identRightCol = gen.generateIdentifier("inRightCol");
			if(!isLeftCol) {
				sb.append("Collection "+identLeftCol+" = new ArrayList(); \n");
				sb.append(identLeftCol+".add("+identLeftRes+"); \n");
			} else {
				sb.append("Collection "+identLeftCol+" = new ArrayList("+identLeftRes+"); \n");
			}
			if(!isRightCol) {
				sb.append("Collection "+identRightCol+" = new ArrayList(); \n");
				sb.append(identRightCol+".add("+identRightRes+"); \n");
			} else {
				sb.append("Collection "+identRightCol+" = new ArrayList("+identRightRes+"); \n");
			}
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identRightCol+".containsAll("+identLeftCol+"); \n");
		}
		gen.printExpressionTrace("//OperatorIn - end", opExpr);		
		return null;
	}
	
	@Override
	public Void visitInstanceof(OperatorInstanceof op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.getAppender();
		
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
		String identClazz = gen.generateIdentifier("clazz");
		String identLeftArg = leftExpr.getSignature().getResultName();
		ClassSignature cs = (ClassSignature) rightExpr.getSignature();
		String identRightArg = cs.getType().tsym.toString();
		String identResult = gen.generateIdentifier("result");
		String identRes = opExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorInstanceof - start", opExpr);
		sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identLeftArg+" instanceof "+identRightArg);
//		rightExpr.accept(gen, null);
		sb.append("; \n");
		gen.printExpressionTrace("//OperatorInstanceof - end", opExpr);	
		return null;
	}
	
	@Override
	public Void visitIntersect(OperatorIntersect op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.getAppender();
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
		String identResult = opExpr.getSignature().getResultName();
				
		Signature sLeft = leftExpr.getSignature();
		Signature sRight = rightExpr.getSignature();
		
		String identERight = sRight.getResultName();
		String identELeft = sLeft.getResultName();
		
		String identLeftCol = gen.generateIdentifier("minusLeftCol");
		String identRightCol = gen.generateIdentifier("minusRightCol");
		
		boolean isLeftCol = sLeft.getColType() != SCollectionType.NO_COLLECTION;
		boolean isRightCol = sRight.getColType() != SCollectionType.NO_COLLECTION;
		
		gen.printExpressionTrace("//OperatorIntersect - start", opExpr);
		sb.append("Collection "+identLeftCol+" = ");
		if(isLeftCol) {
			sb.append("new ArrayList("+identELeft+"); \n");
		} else {
			sb.append("new ArrayList(); \n");
			sb.append(identLeftCol+".add("+identELeft+"); \n");
		}
		sb.append("Collection "+identRightCol+" = ");
		if(isRightCol) {
			sb.append("new ArrayList("+identERight+"); \n");
		} else {
			sb.append("new ArrayList(); \n");
			sb.append(identRightCol+".add("+identERight+"); \n");
		}
		sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = new "+opExpr.getSignature().getJavaTypeStringAssigment()+"(); \n");
		sb.append(""+identResult+".addAll(CollectionUtils.intersection("+identLeftCol+", "+identRightCol+")); \n");
		gen.printExpressionTrace("//OperatorIntersect - end", opExpr);	
		return null;
	}
	
	@Override
	public Void visitLess(OperatorLess op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
		StringBuilder sb = gen.getAppender();
		String identResult = opExpr.getSignature().getResultName();
		
		String identLeftArg = leftExpr.getSignature().getResultName();
		String identRightArg = rightExpr.getSignature().getResultName();
		
		ValueSignature s1 = (ValueSignature)leftExpr.getSignature().getDerefSignatureWithCardinality();
		ValueSignature s2 = (ValueSignature)rightExpr.getSignature().getDerefSignatureWithCardinality();
		
		ClassTypes ct = ClassTypes.getInstance();
		Type comparableType = ct.getCompilerType(Comparable.class);
		Type numberType = ct.getCompilerType(Number.class);
		
		gen.printExpressionTrace("//OperatorLess - start", opExpr);
		identLeftArg = XMLUtils.genDerefCodeIfNecessary(identLeftArg, leftExpr.getSignature(), gen, sb);
		identRightArg = XMLUtils.genDerefCodeIfNecessary(identRightArg, rightExpr.getSignature(), gen, sb);
		sb.append("                                                                          \n");
		sb.append("Boolean "+identResult+" = ");
		if(ct.isSubClass(s1.getType(), numberType) && ct.isSubClass(s2.getType(), numberType)) {
			sb.append(identLeftArg+" < "+identRightArg+"; \n");
		} else {
			sb.append(identLeftArg+".compareTo("+identRightArg+") < 0; \n");
		}
		gen.printExpressionTrace("//OperatorLess - end", opExpr);	
		return null;
	}
	
	@Override
	public Void visitLessOrEqual(OperatorLessOrEqual op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
		StringBuilder sb = gen.getAppender();
		String identResult = opExpr.getSignature().getResultName();
		
		String identLeftArg = leftExpr.getSignature().getResultName();
		String identRightArg = rightExpr.getSignature().getResultName();
		
		ValueSignature s1 = (ValueSignature)leftExpr.getSignature().getDerefSignatureWithCardinality();
		ValueSignature s2 = (ValueSignature)rightExpr.getSignature().getDerefSignatureWithCardinality();
		
		ClassTypes ct = ClassTypes.getInstance();
		Type comparableType = ct.getCompilerType(Comparable.class);
		Type numberType = ct.getCompilerType(Number.class);
		
		gen.printExpressionTrace("//OperatorLessOrEqual - start", opExpr);
		identLeftArg = XMLUtils.genDerefCodeIfNecessary(identLeftArg, leftExpr.getSignature(), gen, sb);
		identRightArg = XMLUtils.genDerefCodeIfNecessary(identRightArg, rightExpr.getSignature(), gen, sb);
		sb.append("                                                                          \n");
		sb.append("Boolean "+identResult+" = ");
		if(ct.isSubClass(s1.getType(), numberType) && ct.isSubClass(s2.getType(), numberType)) {
			sb.append(identLeftArg+" <= "+identRightArg+"; \n");
		} else {
			sb.append(identLeftArg+".compareTo("+identRightArg+") <= 0; \n");
		}
		gen.printExpressionTrace("//OperatorLessOrEqual - end", opExpr);	
		return null;
	}
	
	@Override
	public Void visitMax(OperatorMax op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		commonVisitUnarySubexpr(argExpr, gen);
		
		StringBuilder sb = gen.getAppender();
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identColRes = gen.generateIdentifier("colRes");
		String identMax = gen.generateIdentifier("max");
		String identArgRes = argExpr.getSignature().getResultName();
		String identColEl = gen.generateIdentifier("maxEl");
		String identN = gen.generateIdentifier("n");
		gen.printExpressionTrace("//OperatorMax - start", opExpr);
		identArgRes = XMLUtils.genDerefCodeIfNecessary(identArgRes, argExpr.getSignature(), gen, sb);
		if(argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
//			sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
//			sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
			sb.append("Number "+identMax+" = null; \n");
//			sb.append("try { \n");
			sb.append("	for (Number "+identColEl+" : "+identArgRes+") { \n");
//			sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identColEl+", store); \n");
			sb.append("		"+identMax+" = MathUtils.max("+identMax+", "+identColEl+"); \n");
			sb.append("	} \n");
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = ("+opExpr.getSignature().getJavaTypeString()+")"+identMax+"; \n");
//			sb.append(" \n");	
//			sb.append("} catch(ClassCastException e) { \n");
//			sb.append("	throw new RuntimeException(\"OperatorSum.eval() invalid type: type should be a primitive value\"); \n");
//			sb.append("} \n");
		} else {
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identArgRes+"; \n");
		}
		gen.printExpressionTrace("//OperatorMax - end", opExpr);		
		return null;
	}
	
	@Override
	public Void visitMin(OperatorMin op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		commonVisitUnarySubexpr(argExpr, gen);
		
		StringBuilder sb = gen.getAppender();
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identColRes = gen.generateIdentifier("colRes");
		String identMin = gen.generateIdentifier("min");
		String identArgRes = argExpr.getSignature().getResultName();
		String identColEl = gen.generateIdentifier("minEl");
		String identN = gen.generateIdentifier("n");
		gen.printExpressionTrace("//OperatorMin - start", opExpr);
		identArgRes = XMLUtils.genDerefCodeIfNecessary(identArgRes, argExpr.getSignature(), gen, sb);
		if(argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
//			sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
//			sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
			sb.append("Number "+identMin+" = null; \n");
//			sb.append("try { \n");
			sb.append("	for (Number "+identColEl+" : "+identArgRes+") { \n");
//			sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identColEl+", store); \n");
			sb.append("		"+identMin+" = MathUtils.min("+identMin+", "+identColEl+"); \n");
			sb.append("	} \n");
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = ("+opExpr.getSignature().getJavaTypeString()+")"+identMin+"; \n");
//			sb.append(" \n");	
//			sb.append("} catch(ClassCastException e) { \n");
//			sb.append("	throw new RuntimeException(\"OperatorSum.eval() invalid type: type should be a primitive value\"); \n");
//			sb.append("} \n");
		} else {
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identArgRes+"; \n");
		}
		gen.printExpressionTrace("//OperatorMin - end", opExpr);		
		return null;
	}
	
	@Override
	public Void visitMinus(OperatorMinus op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
//		Expression opExpr = args[2];
		StringBuilder sb = gen.getAppender();
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResultNum = gen.generateIdentifier("resultNum");
		String identResult = gen.generateIdentifier("result");
		gen.printExpressionTrace("//OperatorMinus - start", opExpr);
		identLeftRes = XMLUtils.genDerefCodeIfNecessary(identLeftRes, leftExpr.getSignature(), gen, sb);
		identRightRes = XMLUtils.genDerefCodeIfNecessary(identRightRes, rightExpr.getSignature(), gen, sb);
//		sb.append("Number "+identRightArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append("Number "+identLeftArg+" = (Number) Utils.toSimpleValue(qres.pop(), store); \n");
//		sb.append("Number "+identResultNum+" = MathUtils.subtract("+identLeftArg+", "+identRightArg+"); \n");
//		sb.append("QueryResult "+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
//		sb.append("qres.push("+identResult+"); \n");
		sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identLeftRes+" - "+identRightRes+"; \n");
		gen.printExpressionTrace("//OperatorMinus - end", opExpr);		
		return null;
	}
	
	@Override
	public Void visitModulo(OperatorModulo op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
		StringBuilder sb = gen.getAppender();
		String identResult = opExpr.getSignature().getResultName();
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		ValueSignature s1 = (ValueSignature)leftExpr.getSignature().getDerefSignatureWithCardinality();
		ValueSignature s2 = (ValueSignature)rightExpr.getSignature().getDerefSignatureWithCardinality();
		
		ClassTypes ct = ClassTypes.getInstance();
		Type comparableType = ct.getCompilerType(Comparable.class);
		Type numberType = ct.getCompilerType(Number.class);
		
		gen.printExpressionTrace("//OperatorModulo - start", opExpr);
		identLeftRes = XMLUtils.genDerefCodeIfNecessary(identLeftRes, leftExpr.getSignature(), gen, sb);
		identRightRes = XMLUtils.genDerefCodeIfNecessary(identRightRes, rightExpr.getSignature(), gen, sb);
		sb.append(opExpr.getSignature().genJavaDeclarationCode()+" = ");
		sb.append(identLeftRes+" % "+identRightRes+"; \n");
		gen.printExpressionTrace("//OperatorModulo - end", opExpr);	
		return null;
	}
	
	@Override
	public Void visitMore(OperatorMore op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
		StringBuilder sb = gen.getAppender();
		String identResult = opExpr.getSignature().getResultName();
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		ValueSignature s1 = (ValueSignature)leftExpr.getSignature().getDerefSignatureWithCardinality();
		ValueSignature s2 = (ValueSignature)rightExpr.getSignature().getDerefSignatureWithCardinality();
		
		ClassTypes ct = ClassTypes.getInstance();
		Type comparableType = ct.getCompilerType(Comparable.class);
		Type numberType = ct.getCompilerType(Number.class);
		
		gen.printExpressionTrace("//OperatorMore - start", opExpr);
		identLeftRes = XMLUtils.genDerefCodeIfNecessary(identLeftRes, leftExpr.getSignature(), gen, sb);
		identRightRes = XMLUtils.genDerefCodeIfNecessary(identRightRes, rightExpr.getSignature(), gen, sb);
//		sb.append(" "+opExpr.signature.genSBQLDeclarationCode()+"; \n");
//		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store); \n");
//		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store); \n");
		sb.append("                                                                          \n");
		sb.append("Boolean "+identResult+" = ");
		
//		Signature lSig = leftExpr.getSignature();
//		boolean isLeftPrimitiveType =  lSig instanceof ValueSignature && ((ValueSignature)lSig).primitiveType;
//		Signature rSig = rightExpr.getSignature();
//		boolean isRightPrimitiveType =  rSig instanceof ValueSignature && ((ValueSignature)rSig).primitiveType;
//		if(!isLeftPrimitiveType) {
//			sb.append(identLeftRes + "==null ");
//		}
//		if(!isLeftPrimitiveType && !isRightPrimitiveType) {
//			sb.append("||");
//		}
//		if(!isRightPrimitiveType) {
//			sb.append(identRightRes + "==null ");
//		}
//		if(!isLeftPrimitiveType || !isRightPrimitiveType) {
//			sb.append("? false : ");
//		}
		sb.append(GenNoStacksHelper.genEmptyValueCheckBinaryOperator(leftExpr.getSignature(), rightExpr.getSignature(), 
				"false", "true", "false"));
		if(ct.isSubClass(s1.getType(), numberType) && ct.isSubClass(s2.getType(), numberType)) {
			sb.append(identLeftRes+" > "+identRightRes+"; \n");
		} else {
			sb.append(identLeftRes+".compareTo("+identRightRes+") > 0; \n");
		}
		gen.printExpressionTrace("//OperatorMore - end", opExpr);	
		return null;
	}
	
	@Override
	public Void visitMoreOrEqual(OperatorMoreOrEqual op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
		StringBuilder sb = gen.getAppender();
		String identResult = opExpr.getSignature().getResultName();
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		ValueSignature s1 = (ValueSignature)leftExpr.getSignature().getDerefSignatureWithCardinality();
		ValueSignature s2 = (ValueSignature)rightExpr.getSignature().getDerefSignatureWithCardinality();
		
		ClassTypes ct = ClassTypes.getInstance();
		Type comparableType = ct.getCompilerType(Comparable.class);
		Type numberType = ct.getCompilerType(Number.class);
		
		gen.printExpressionTrace("//OperatorMoreOrEqual - start", opExpr);
		identLeftRes = XMLUtils.genDerefCodeIfNecessary(identLeftRes, leftExpr.getSignature(), gen, sb);
		identRightRes = XMLUtils.genDerefCodeIfNecessary(identRightRes, rightExpr.getSignature(), gen, sb);
//		sb.append(" "+opExpr.signature.genSBQLDeclarationCode()+"; \n");
//		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store); \n");
//		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store); \n");
		sb.append("                                                                          \n");
		sb.append("Boolean "+identResult+" = ");
		if(ct.isSubClass(s1.getType(), numberType) && ct.isSubClass(s2.getType(), numberType)) {
			sb.append(identLeftRes+" >= "+identRightRes+"; \n");
		} else {
			sb.append(identLeftRes+".compareTo("+identRightRes+") >= 0; \n");
		}
		gen.printExpressionTrace("//OperatorMoreOrEqual - end", opExpr);	
		return null;
	}
	
	@Override
	public Void visitMultiply(OperatorMultiply op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.getAppender();
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		gen.printExpressionTrace("//OperatorMultiply - start", opExpr);
		identLeftRes = XMLUtils.genDerefCodeIfNecessary(identLeftRes, leftExpr.getSignature(), gen, sb);
		identRightRes = XMLUtils.genDerefCodeIfNecessary(identRightRes, rightExpr.getSignature(), gen, sb);
		sb.append(opExpr.getSignature().genJavaDeclarationCode()+" = ");
		sb.append(identLeftRes+" * "+identRightRes+"; \n");
		gen.printExpressionTrace("//OperatorMultiply - end", opExpr);	
		return null;
	}
	
	@Override
	public Void visitNot(OperatorNot op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		commonVisitUnarySubexpr(argExpr, gen);
		
		Expression genericTypeExpr = subExprs[1];
		
		StringBuilder sb = gen.getAppender();
		String identB = gen.generateIdentifier("b");
		String identArgRes = argExpr.getSignature().getResultName();
		gen.printExpressionTrace("//OperatorNot - start", opExpr);
		identArgRes = XMLUtils.genDerefCodeIfNecessary(identArgRes, argExpr.getSignature(), gen, sb);
		sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = !"+argExpr.getSignature().getResultName()+"; \n");
		gen.printExpressionTrace("//OperatorNot - end", opExpr);
		return null;
	}
	
	@Override
	public Void visitNotEquals(OperatorNotEquals op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
		StringBuilder sb = gen.getAppender();
		String identRightRes = rightExpr.getSignature().getResultName();
		String identLeftRes = leftExpr.getSignature().getResultName();
//		String identResult = opExpr.getSignature().getResultName();
		gen.printExpressionTrace("//OperatorEquals - start", opExpr);
		identLeftRes = XMLUtils.genDerefCodeIfNecessary(identLeftRes, leftExpr.getSignature(), gen, sb);
		identRightRes = XMLUtils.genDerefCodeIfNecessary(identRightRes, rightExpr.getSignature(), gen, sb);
		String identResType = opExpr.getSignature().getJavaTypeString();
		String identResName = opExpr.getSignature().getResultName();
		sb.append(""+identResType+" "+identResName+" = !OperatorUtils.equalsSafe("+identLeftRes+", "+identRightRes+"); \n");
		gen.printExpressionTrace("//OperatorEquals - end", opExpr);	
		return null;
	}
	
	@Override
	public Void visitOr(OperatorOr op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		StringBuilder sb = gen.getAppender();
		String identRes = opExpr.getSignature().getResultName();
		gen.printExpressionTrace("//OperatorOr - start", opExpr);
		leftExpr.accept(gen, null);
		String identLeftRes = leftExpr.getSignature().getResultName();
		identLeftRes = XMLUtils.genDerefCodeIfNecessary(identLeftRes, leftExpr.getSignature(), gen, sb);
		sb.append(opExpr.getSignature().genJavaDeclarationCode()+"; \n");
		sb.append("if(!"+identLeftRes+") {\n");
		sb.append(identRes+" = true;\n");
		sb.append("} else {\n");
		rightExpr.accept(gen, null);
		String identRightRes = rightExpr.getSignature().getResultName();
		identRightRes = XMLUtils.genDerefCodeIfNecessary(identRightRes, rightExpr.getSignature(), gen, sb);
		sb.append(identRes+" = "+identRightRes+";\n");
		sb.append("}\n");
		
//		sb.append("Boolean "+identRightArg+" = (Boolean) Utils.toSimpleValue("+identRightRes+", store); \n");
//		sb.append("Boolean "+identLeftArg+" = (Boolean) Utils.toSimpleValue("+identLeftRes+", store); \n");
//		sb.append(opExpr.getSignature().genJavaDeclarationCode()+" = "+identLeftArg+" || "+identRightArg+"; \n");
//		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorOr - end", opExpr);
		return null;
	}
	
	@Override
	public Void visitPlus(OperatorPlus op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		StringBuilder sb = gen.getAppender();
		String identRightArg = gen.generateIdentifier("rightArg");
		String identLeftArg = gen.generateIdentifier("leftArg");
		String identResult = opExpr.getSignature().getResultName();
		String identResultNum = gen.generateIdentifier("resultNum");
		
		String identLeftRes = leftExpr.getSignature().getResultName();
		String identRightRes = rightExpr.getSignature().getResultName();
		
		gen.printExpressionTrace("//OperatorPlus - start", opExpr);  
		identLeftRes = XMLUtils.genDerefCodeIfNecessary(identLeftRes, leftExpr.getSignature(), gen, sb);
		identRightRes = XMLUtils.genDerefCodeIfNecessary(identRightRes, rightExpr.getSignature(), gen, sb);
//		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store); \n");
//		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store); \n");
//		sb.append("QueryResult "+identResult+"; \n");
//		sb.append("if("+identLeftArg+" instanceof String || "+identRightArg+" instanceof String) { \n");
//		sb.append("	"+identResult+" = javaObjectFactory.createJavaComplexObject("+identLeftArg+".toString().concat("+identRightArg+".toString())); \n");
//		sb.append("} else { \n");
//		sb.append("	Number "+identResultNum+" = MathUtils.sum((Number)"+identLeftArg+", (Number)"+identRightArg+"); \n");
//		sb.append("	"+identResult+" = javaObjectFactory.createJavaComplexObject("+identResultNum+"); \n");
//		sb.append("} \n");
//		sb.append(""+opExpr.signature.genJavaDeclarationCode()+" = "+identLeftRes+" + "+identRightRes+"; \n");
		
//		opExpr.getSignature().setResultName("("+identLeftRes+" + "+identRightRes+")");
//		opExpr.getSignature().setResultName(""+identLeftRes+" + "+identRightRes+"");
		sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identLeftRes+" + "+identRightRes+"; \n");
//		sb.append("qres.push("+identResult+"); \n");
		gen.printExpressionTrace("//OperatorPlus - end", opExpr);		
		return null;
	}
	
	@Override
	public Void visitSequence(OperatorSequence op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		Expression genExpr = subExprs[1];
		commonVisitUnarySubexpr(argExpr, gen);
		if(genExpr != null) {
			commonVisitUnarySubexpr(genExpr, gen);
		}
		
		StringBuilder sb = gen.getAppender();
		String identEres = opExpr.getSignature().getResultName();

		gen.printExpressionTrace("//OperatorSequence - start", opExpr);
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
		gen.printExpressionTrace("//OperatorSequence - end", opExpr);	
		return null;
	}
	
	@Override
	public Void visitStruct(OperatorStruct op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		StringBuilder sb = gen.getAppender();
		Expression argExpr = subExprs[0];
		commonVisitUnarySubexpr(argExpr, gen);
		
		String identEres = opExpr.getSignature().getResultName();
		String identArgRes = argExpr.getSignature().getResultName();
//		String identE1Res = gen.generateIdentifier("e1res");
		String identS = gen.generateIdentifier("s");
		gen.printExpressionTrace("//OperatorStruct - start", opExpr);
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
		gen.printExpressionTrace("//OperatorStruct - end", opExpr);	
		return null;
	}
	
	@Override
	public Void visitSum(OperatorSum op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression argExpr = subExprs[0];
		commonVisitUnarySubexpr(argExpr, gen);
		
		StringBuilder sb = gen.getAppender();
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identColRes = gen.generateIdentifier("colRes");
		String identSum = gen.generateIdentifier("sum");
		String identArgRes = argExpr.getSignature().getResultName();
		String identColEl = gen.generateIdentifier("sumEl");
		String identN = gen.generateIdentifier("n");
		gen.printExpressionTrace("//OperatorSum - start", opExpr);
		identArgRes = XMLUtils.genDerefCodeIfNecessary(identArgRes, argExpr.getSignature(), gen, sb);
		if(argExpr.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
//			sb.append("QueryResult "+identE1Res+" = qres.pop(); \n");
//			sb.append("CollectionResult "+identColRes+" = Utils.objectToCollection("+identE1Res+"); \n");
			sb.append("Number "+identSum+" = null; \n");
//			sb.append("try { \n");
			sb.append("	for (Number "+identColEl+" : "+identArgRes+") { \n");
//			sb.append("		Number "+identN+" = (Number) Utils.toSimpleValue("+identColEl+", store); \n");
			sb.append("		"+identSum+" = MathUtils.sum("+identSum+", "+identColEl+"); \n");
			sb.append("	} \n");
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = ("+opExpr.getSignature().getJavaTypeString()+")"+identSum+"; \n");
//			sb.append(" \n");	
//			sb.append("} catch(ClassCastException e) { \n");
//			sb.append("	throw new RuntimeException(\"OperatorSum.eval() invalid type: type should be a primitive value\"); \n");
//			sb.append("} \n");
		} else {
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identArgRes+"; \n");
		}
				
		gen.printExpressionTrace("//OperatorSum - end", opExpr);		
		return null;
	}
	
	@Override
	public Void visitUnion(OperatorUnion op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
		Expression leftExpr = subExprs[0];
		Expression rightExpr = subExprs[1];
		commonVisitBinarySubexpr(leftExpr, rightExpr, gen);
		
		Signature sLeft = leftExpr.getSignature();
		Signature sRight = rightExpr.getSignature();
		
		StringBuilder sb = gen.getAppender();
		String identEres = opExpr.getSignature().getResultName();
		String identERight = sRight.getResultName();
		String identELeft = sLeft.getResultName();
		gen.printExpressionTrace("//OperatorUnion - start", opExpr);
//		sb.append("Bag "+identEres+" = new Bag(); \n");
		sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = new "+opExpr.getSignature().getJavaTypeStringAssigment()+"(); \n");
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
//		sb.append("CollectionResult "+identELeft+" = Utils.objectToCollection(qres.pop()); \n");
//		sb.append(""+identEres+".addAll("+identELeft+"); \n");
//		sb.append(""+identEres+".addAll("+identERight+"); \n");		
//		sb.append("qres.push("+identEres+"); \n");
		gen.printExpressionTrace("//OperatorUnion - end", opExpr);		
		return null;
	}
	
	@Override
	public Void visitUnique(OperatorUnique op, QueryCodeGenerator gen, Expression opExpr, Expression... subExprs) {
//		Expression opExpr = args[0];
		Expression rightExpr = subExprs[0];
		commonVisitUnarySubexpr(rightExpr, gen);
		
		StringBuilder sb = gen.getAppender();
		String identObjects = gen.generateIdentifier("objects");
		String identResult = opExpr.getSignature().getResultName();
		String identTmp = gen.generateIdentifier("tmp");
		String identRightRes = rightExpr.getSignature().getResultName();
		String rightElType = rightExpr.getSignature().getJavaTypeStringSingleResult();
		
		gen.printExpressionTrace("//OperatorUnique - start", opExpr);
		if(opExpr.getSignature().getColType() == SCollectionType.NO_COLLECTION) {
			//only one element - no changes
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = "+identRightRes+"; \n");
		} else {
			sb.append(""+opExpr.getSignature().genJavaDeclarationCode()+" = new "+opExpr.getSignature().getJavaTypeStringAssigment()+"(); \n");
//			sb.append("QueryResult "+identObjects+" = qres.pop(); \n"); 
//			sb.append(""+identObjects+" = Utils.objectToCollection("+identObjects+"); \n");
//			sb.append("QueryResult "+identResult+" = null; \n");
//	        sb.append(" \n");
//			sb.append("if ("+identObjects+" instanceof Bag) { \n");
//			sb.append("	"+identResult+" = new Bag(); \n");
//			sb.append("} else if ("+identObjects+" instanceof Sequence) { \n");
//			sb.append("	"+identResult+" = new Sequence(); \n");
//			sb.append("} \n");
			sb.append("Set<"+rightElType+"> "+identTmp+" = new LinkedHashSet<"+rightElType+">(); \n");
			sb.append(""+identTmp+".addAll( "+identRightRes+"); \n");
			sb.append(""+identResult+".addAll("+identTmp+"); \n");
//			sb.append("qres.push("+identResult+"); \n");
		}
		gen.printExpressionTrace("//OperatorUnique - end", opExpr);	
		return null;
	}
	
	private void commonVisitBinarySubexpr(Expression leftExpr, Expression rightExpr, QueryCodeGenerator mainTreeVis) {
		leftExpr.accept(mainTreeVis, null);
		rightExpr.accept(mainTreeVis, null);
	}
	private void commonVisitUnarySubexpr(Expression subExpr, QueryCodeGenerator mainTreeVis) {
		subExpr.accept(mainTreeVis, null);
//		rightExpr.accept(mainTreeVis, null);
	}
}

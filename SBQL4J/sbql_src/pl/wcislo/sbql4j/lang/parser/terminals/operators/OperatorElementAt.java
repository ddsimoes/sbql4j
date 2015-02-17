package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import java.util.Iterator;

import pl.wcislo.sbql4j.java.model.compiletime.BinderSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.StructSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureAbstractFactory;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureFactory;
import pl.wcislo.sbql4j.lang.codegen.noqres.QueryCodeGenNoQres;
import pl.wcislo.sbql4j.lang.codegen.nostacks.QueryCodeGenNoStacks;
import pl.wcislo.sbql4j.lang.codegen.simple.QueryCodeGenSimple;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.terminals.Operator;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.lang.tree.visitors.OperatorVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TypeChecker;
import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.model.StructSBQL;
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.model.collections.CollectionResult;
import pl.wcislo.sbql4j.model.collections.Sequence;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorElementAt extends Operator {

	public OperatorElementAt(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitElementAt(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		QueryResult rightVal = interpreter.getQres().pop();
		QueryResult leftArg = interpreter.getQres().pop();
		if(leftArg instanceof Binder) {
			leftArg = ((Binder)leftArg).object;
		}
		if(!(leftArg instanceof CollectionResult)) {
			throw new RuntimeException("execpted Sequence, got: "+leftArg.getClass());
		}
		CollectionResult leftCol = (CollectionResult) leftArg;
		if(!(rightVal instanceof StructSBQL)) {
			Object rightArg = Utils.toSimpleValue(rightVal, interpreter.getStore());
			if(!(rightArg instanceof Integer || rightArg instanceof Long || rightArg instanceof Short || rightArg instanceof Byte)) {
				throw new RuntimeException("execpted Number, got: "+rightArg.getClass());
			}
			Integer i = ((Number)rightArg).intValue();
			QueryResult result;
			if(leftCol.size() < i+1) {
				result = new Bag();
			} else {
				if(leftCol instanceof Sequence) {
					result = ((Sequence)leftCol).get(i);	
				} else {
					Iterator<QueryResult> it = leftCol.iterator();
					QueryResult qr = it.next();
					for(int j=0; j<i; j++) {
						qr = it.next();
					}
					result = qr;
				}
			}
			interpreter.getQres().push(result);
		} else {
			StructSBQL bounds = (StructSBQL) rightVal;
			Integer lowBound = (Integer) Utils.toSimpleValue(bounds.get(0), interpreter.getStore());
			Integer upBound;
			if(bounds.size() < 2) {
				upBound = Integer.MAX_VALUE;
			} else {
				upBound = (Integer) Utils.toSimpleValue(bounds.get(1), interpreter.getStore());
			}
			CollectionResult result;
			if(leftCol instanceof Sequence) {
				result = new Sequence();
			} else {
				result = new Bag();
			}
			Iterator<QueryResult> it = leftCol.iterator();
			int i=0;
			while(it.hasNext() && i <= upBound) {
				QueryResult el = it.next();
				if(i >= lowBound && i <= upBound) {
					result.add(el);
				}
				i++;
			}
			interpreter.getQres().push(result);
		}
	}
//	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Signature leftSig = leftExpr.getSignature().getDerefSignatureWithCardinality();
//		Signature rightSig = rightExpr.getSignature().getDerefSignatureWithCardinality();
//		
//		String identRightVal = gen.generateIdentifier("rightVal");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identLeftCol = gen.generateIdentifier("leftCol");
//		String identResult = gen.generateIdentifier("result");
//		String identI = gen.generateIdentifier("i");
//		String identIt = gen.generateIdentifier("it");
//		
//		gen.printExpressionTrace("//OperatorElementAt - start \n");
//		sb.append("QueryResult "+identRightVal+" = qres.pop();\n");
//		sb.append("QueryResult "+identLeftArg+" = qres.pop();\n");
//		sb.append("if("+identLeftArg+" instanceof Binder) {\n");
//		sb.append("	"+identLeftArg+" = ((Binder)"+identLeftArg+").object;\n");
//		sb.append("}\n");
//		sb.append("if(!("+identLeftArg+" instanceof CollectionResult)) {\n");
//		sb.append("	throw new RuntimeException(\"execpted Sequence, got: \"+"+identLeftArg+".getClass());\n");
//		sb.append("}\n");
//		sb.append("CollectionResult "+identLeftCol+" = (CollectionResult) "+identLeftArg+";\n");
////		if(!(rightVal instanceof Struct)) {
//		if(!(rightSig instanceof StructSignature)) {
//			String identRightArg = gen.generateIdentifier("rightArg");
//			
//			sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightVal+", store);\n");
//			sb.append("if(!("+identRightArg+" instanceof Integer || "+identRightArg+" instanceof Long || "+identRightArg+" instanceof Short || "+identRightArg+" instanceof Byte)) {\n");
//			sb.append("	throw new RuntimeException(\"execpted Number, got: \"+"+identRightArg+".getClass());\n");
//			sb.append("}\n");
//			sb.append("Integer "+identI+" = ((Number)"+identRightArg+").intValue();\n");
//			sb.append("QueryResult "+identResult+";\n");
//			sb.append("if("+identLeftCol+".size() < "+identI+"+1) {\n");
//			sb.append("	"+identResult+" = new Bag();\n");
//			sb.append("} else {\n");
//			if(leftSig.sColType == SCollectionType.SEQUENCE) {
//				sb.append("		"+identResult+" = ((Sequence)"+identLeftCol+").get("+identI+");\n");	
//			} else {
//				
//				String identJ = gen.generateIdentifier("j");
//				String identQr = gen.generateIdentifier("qr");
//				sb.append("		Iterator<QueryResult> "+identIt+" = "+identLeftCol+".iterator();\n");
//				sb.append("		QueryResult "+identQr+" = "+identIt+".next();\n");
//				sb.append("		for(int "+identJ+"=0; "+identJ+"<"+identI+"; "+identJ+"++) {\n");
//				sb.append("			"+identQr+" = "+identIt+".next();\n");
//				sb.append("		}\n");
//				sb.append("		"+identResult+" = "+identQr+";\n");
//			}
//			sb.append("}\n");
//			sb.append("qres.push("+identResult+");\n");
//		} else {
//			String identBounds = gen.generateIdentifier("bounds");
//			String identLowBound = gen.generateIdentifier("lowBound");
//			String identUpBound = gen.generateIdentifier("upBound");
//			
//			StructSignature boundsSig = (StructSignature) rightSig;
//			sb.append("StructSBQL "+identBounds+" = (StructSBQL) "+identRightVal+";\n");
//			sb.append("Integer "+identLowBound+" = (Integer) Utils.toSimpleValue("+identBounds+".get(0), store);\n");
//			sb.append("Integer "+identUpBound+";\n");
//			if(boundsSig.getFields().length < 2) {
//				sb.append("	"+identUpBound+" = Integer.MAX_VALUE;\n");
//			} else {
//				sb.append("	"+identUpBound+" = (Integer) Utils.toSimpleValue("+identBounds+".get(1), store);\n");
//			}
//			sb.append("CollectionResult "+identResult+";\n");
//			if(leftSig.sColType == SCollectionType.SEQUENCE) {
//				sb.append("	"+identResult+" = new Sequence();\n");
//			} else {
//				sb.append("	"+identResult+" = new Bag();\n");
//			}
//			sb.append("Iterator<QueryResult> "+identIt+" = "+identLeftCol+".iterator();\n");
//			sb.append("int "+identI+" = 0;\n");
//			sb.append("while("+identIt+".hasNext() && "+identI+" <= "+identUpBound+") {\n");
//			sb.append("	QueryResult el = "+identIt+".next();\n");
//			sb.append("	if("+identI+" >= "+identLowBound+" && "+identI+" <= "+identUpBound+") {\n");
//			sb.append("		"+identResult+".add(el);\n");
//			sb.append("	}\n");
//			sb.append("	"+identI+"++;\n");
//			sb.append("}\n");
//			sb.append("qres.push("+identResult+");\n");
//			gen.printExpressionTrace("//OperatorElementAt - end \n");
//		}
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		Signature leftSig = leftExpr.getSignature();
//		Signature rightSig = rightExpr.getSignature();
//		Signature opSig = opExpr.getSignature();
//		
//		String identResult = opSig.getResultName();
//		
//		gen.printExpressionTrace("//OperatorElementAt - start \n");
//		sb.append(""+opSig.genJavaDeclarationCode()+"; \n");
//		sb.append("	if("+leftSig.getResultName()+".isEmpty()) {\n");
//		sb.append("	"+identResult+" = ");
//		if(opSig.sColType != SCollectionType.NO_COLLECTION) {
//			sb.append("new ArrayList(); \n");
//		} else {
//			sb.append("null; \n");
//		}
//		sb.append("	} else { \n");
//		if(!(rightSig instanceof StructSignature)) {
//			if(leftSig.sColType == SCollectionType.SEQUENCE) {
//				sb.append(""+identResult+" = "+leftSig.getResultName()+".get("+rightSig.getResultName()+");\n");	
//			} else {
//				String identJ = gen.generateIdentifier("j");
//				String identQr = gen.generateIdentifier("elementAtEl");
//				String identIt = gen.generateIdentifier("elementAtIteraotr");
////				sb.append("		"+opSig.genJavaDeclarationCode()+"; \n");
//				sb.append("		Iterator<"+leftExpr.getSignature().getJavaTypeStringSingleResult()+"> "+identIt+" = "+leftSig.getResultName()+".iterator();\n");
////				sb.append("		QueryResult "+identQr+" = "+identIt+".next();\n");
//				sb.append("		for(int "+identJ+"=0; "+identJ+"<"+rightSig.getResultName()+"; "+identJ+"++) {\n");
//				sb.append("			"+opSig.getResultName()+" = "+identIt+".next();\n");
//				sb.append("		}\n");
////				sb.append("		"+identResult+" = "+identQr+";\n");
//			}
////			sb.append("}\n");
////			sb.append("qres.push("+identResult+");\n");
//		} else {
//			sb.append(""+identResult+" = new "+opSig.getJavaTypeStringAssigment()+"(); \n");
//			String identBounds = gen.generateIdentifier("bounds");
//			String identLowBound = gen.generateIdentifier("lowBound");
//			String identUpBound = gen.generateIdentifier("upBound");
//			
//			StructSignature boundsSig = (StructSignature) rightSig;
////			sb.append("Struct "+identBounds+" = (Struct) "+identRightVal+";\n");
//			sb.append("Integer "+identLowBound+" = "+boundsSig.getFields()[0].getResultName()+"; \n");
////			sb.append("Integer "+identUpBound+";\n");
//			if(boundsSig.getFields().length < 2) {
//				sb.append("Integer "+identUpBound+" = "+leftSig.getResultName()+".size()-1; \n");
//			} else {
//				sb.append("Integer "+identUpBound+" = "+boundsSig.getFields()[1].getResultName()+" ;\n");
//			}
//			
//			if(leftSig.sColType == SCollectionType.SEQUENCE) {
//				sb.append("	if("+identUpBound+" >= "+leftSig.getResultName()+".size()) {\n");
//				sb.append("	"+identUpBound+" = "+leftSig.getResultName()+".size() - 2; \n");
//				sb.append("	} \n");
//				sb.append("	"+opSig.getResultName()+" = "+leftSig.getResultName()+".subList("+identLowBound+", "+identUpBound+"+1);\n");
//			} else {
//				String identIt = gen.generateIdentifier("elementAtIteraotr");
//				String identI = gen.generateIdentifier("i");
//				sb.append("Iterator<"+leftExpr.getSignature().getJavaTypeStringSingleResult()+"> "+identIt+" = "+leftSig.getResultName()+".iterator();\n");
//				sb.append("int "+identI+" = 0;\n");
//				sb.append("while("+identIt+".hasNext() && "+identI+" <= "+identUpBound+") {\n");
//				sb.append("	"+leftExpr.getSignature().getJavaTypeStringSingleResult()+" el = "+identIt+".next();\n");
//				sb.append("	if("+identI+" >= "+identLowBound+" && "+identI+" <= "+identUpBound+") {\n");
//				sb.append("		"+opSig.getResultName()+".add(el);\n");
//				sb.append("	}\n");
//				sb.append("	"+identI+"++;\n");
//				sb.append("}\n");
//			}
//		}	
//		sb.append("	} \n");
//		gen.printExpressionTrace("//OperatorElementAt - end \n");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		StringBuilder sb = gen.sb;
//		Signature leftSig = leftExpr.getSignature().getDerefSignatureWithCardinality();
//		Signature rightSig = rightExpr.getSignature().getDerefSignatureWithCardinality();
//		
////		String identRightVal = gen.generateIdentifier("rightVal");
////		String identLeftArg = gen.generateIdentifier("leftArg");
////		
//		String identRightVal = rightExpr.getSignature().getResultName();
//		String identLeftArg = leftExpr.getSignature().getResultName();
//		
////		String identLeftCol = gen.generateIdentifier("leftCol");
//		String identResult = opExpr.getSignature().getResultName();
//		String identI = gen.generateIdentifier("i");
//		String identIt = gen.generateIdentifier("it");
//		
//		gen.printExpressionTrace("//OperatorElementAt - start \n");
////		sb.append("QueryResult "+identRightVal+" = qres.pop();\n");
////		sb.append("QueryResult "+identLeftArg+" = qres.pop();\n");
////		sb.append("if("+identLeftArg+" instanceof Binder) {\n");
//		if(leftExpr.getSignature() instanceof BinderSignature && leftExpr.getSignature().sColType == SCollectionType.NO_COLLECTION) {
//			sb.append("	"+identLeftArg+" = ((Binder)"+identLeftArg+").object;\n");
//		}
////		sb.append("CollectionResult "+identLeftCol+" = (CollectionResult) "+identLeftArg+";\n");
////		if(!(rightVal instanceof Struct)) {
//		if(!(rightSig instanceof StructSignature)) {
//			sb.append("Integer "+identI+" = (Integer) Utils.toSimpleValue("+identRightVal+", store);\n");
//			sb.append("QueryResult "+identResult+";\n");
//			sb.append("if("+identLeftArg+".size() < "+identI+"+1) {\n");
//			sb.append("	"+identResult+" = new "+leftSig.sColType.genSBQLDeclCode()+"();\n");
//			sb.append("} else {\n");
//			if(leftSig.sColType == SCollectionType.SEQUENCE) {
////				sb.append("		"+identResult+" = ((Sequence)"+identLeftCol+").get("+identI+");\n");
//				sb.append("		"+identResult+" = ((Sequence)"+identLeftArg+").get("+identI+");\n");
//			} else {
//				
//				String identJ = gen.generateIdentifier("j");
//				String identQr = gen.generateIdentifier("qr");
////				sb.append("		Iterator<QueryResult> "+identIt+" = "+identLeftCol+".iterator();\n");
//				sb.append("		Iterator<QueryResult> "+identIt+" = "+identLeftArg+".iterator();\n");
//				sb.append("		QueryResult "+identQr+" = "+identIt+".next();\n");
//				sb.append("		for(int "+identJ+"=0; "+identJ+"<"+identI+"; "+identJ+"++) {\n");
//				sb.append("			"+identQr+" = "+identIt+".next();\n");
//				sb.append("		}\n");
//				sb.append("		"+identResult+" = "+identQr+";\n");
//			}
//			sb.append("}\n");
////			sb.append("qres.push("+identResult+");\n");
//		} else {
//			String identBounds = gen.generateIdentifier("bounds");
//			String identLowBound = gen.generateIdentifier("lowBound");
//			String identUpBound = gen.generateIdentifier("upBound");
//			
//			StructSignature boundsSig = (StructSignature) rightSig;
//			sb.append("StructSBQL "+identBounds+" = (StructSBQL) "+identRightVal+";\n");
//			sb.append("Integer "+identLowBound+" = (Integer) Utils.toSimpleValue("+identBounds+".get(0), store);\n");
//			sb.append("Integer "+identUpBound+";\n");
//			if(boundsSig.getFields().length < 2) {
//				sb.append("	"+identUpBound+" = Integer.MAX_VALUE;\n");
//			} else {
//				sb.append("	"+identUpBound+" = (Integer) Utils.toSimpleValue("+identBounds+".get(1), store);\n");
//			}
//			sb.append("CollectionResult "+identResult+";\n");
//			if(leftSig.sColType == SCollectionType.SEQUENCE) {
//				sb.append("	"+identResult+" = new Sequence();\n");
//			} else {
//				sb.append("	"+identResult+" = new Bag();\n");
//			}
//			sb.append("Iterator<QueryResult> "+identIt+" = "+identLeftArg+".iterator();\n");
//			sb.append("int "+identI+" = 0;\n");
//			sb.append("while("+identIt+".hasNext() && "+identI+" <= "+identUpBound+") {\n");
//			sb.append("	QueryResult el = "+identIt+".next();\n");
//			sb.append("	if("+identI+" >= "+identLowBound+" && "+identI+" <= "+identUpBound+") {\n");
//			sb.append("		"+identResult+".add(el);\n");
//			sb.append("	}\n");
//			sb.append("	"+identI+"++;\n");
//			sb.append("}\n");
////			sb.append("qres.push("+identResult+");\n");
//			gen.printExpressionTrace("//OperatorElementAt - start \n");
//		}	
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression leftExpr = args[0];
		Expression rightExpr = args[1];
		Expression opExpr = args[2];
		
		JavaSignatureFactory jSigFac = JavaSignatureAbstractFactory.getJavaSignatureFactory();
		ClassTypes ct = ClassTypes.getInstance();
		
		Signature leftSig = leftExpr.getSignature().getDerefSignatureWithCardinality();
		if(!(leftSig instanceof ValueSignature || leftSig instanceof StructSignature)) {
			checker.addError(leftExpr, "expected value or struct type, got: "+leftSig.getColType());
		}
		
		if(SCollectionType.NO_COLLECTION == leftSig.getColType()) {
			checker.addError(leftExpr, "expected collection type, got: "+leftSig.getColType());
		}

//		ValueSignature leftValSig = (ValueSignature)leftSig;
		Signature rightSig = rightExpr.getSignature().getDerefSignatureWithCardinality();
		SCollectionType resultSCType;
		if(rightSig instanceof ValueSignature) {
			//collection[3]
			ValueSignature rightValSig = (ValueSignature) rightSig;
			ValueSignature expectedSig = jSigFac.createJavaSignature(ct.getCompilerType(Integer.class));
			expectedSig.setColType(SCollectionType.NO_COLLECTION);
			boolean isRightTypeInt = rightValSig.isTypeCompatible(expectedSig);
			if(!isRightTypeInt) {
				checker.addError(opExpr, "Integer expected, got "+rightValSig);
			}
			resultSCType = SCollectionType.NO_COLLECTION;
		} else if(rightSig instanceof StructSignature) {
			//collection[3..4], //collection[3..*]
			StructSignature boundsSig = (StructSignature) rightSig;
			if(boundsSig.fieldsNumber() == 2) {
				//collection[3..4]
				Signature lowBoundSig = boundsSig.getFields()[0].getDerefSignatureWithCardinality();
				Signature upBoundSig = boundsSig.getFields()[1].getDerefSignatureWithCardinality();
				if(!(lowBoundSig instanceof ValueSignature) || !(upBoundSig instanceof ValueSignature) ) {
					checker.addError(rightExpr, "bounds must be both values, got low: "+lowBoundSig+", up: "+upBoundSig);
				} else {
					ValueSignature lowValSig = (ValueSignature) lowBoundSig;
					ValueSignature upValSig = (ValueSignature) upBoundSig;
					ValueSignature expectedSig = jSigFac.createJavaSignature(ct.getCompilerType(Integer.class));
					expectedSig.setColType(SCollectionType.NO_COLLECTION);
					boolean isLowTypeInt = lowValSig.isTypeCompatible(expectedSig);
					boolean isUpTypeInt = upValSig.isTypeCompatible(expectedSig);
					if(!isLowTypeInt) {
						checker.addError(opExpr, "Integer expected as lower bound, got "+lowValSig);
					}
					if(!isUpTypeInt) {
						checker.addError(opExpr, "Integer expected as upper bound, got "+upValSig);
					}
				}
			} else if(boundsSig.fieldsNumber() == 1) {
				Signature lowBoundSig = boundsSig.getFields()[0].getDerefSignatureWithCardinality();
				if(!(lowBoundSig instanceof ValueSignature)) {
					checker.addError(rightExpr, "lower bound must be value, got "+lowBoundSig);
				} else {
					ValueSignature lowValSig = (ValueSignature) lowBoundSig;
					ValueSignature expectedSig = jSigFac.createJavaSignature(ct.getCompilerType(Integer.class));
					expectedSig.setColType(SCollectionType.NO_COLLECTION);
					boolean isLowTypeInt = lowValSig.isTypeCompatible(expectedSig);
					if(!isLowTypeInt) {
						checker.addError(opExpr, "Integer expected as lower bound, got "+lowValSig);
					}
				}
			} else {
				checker.addError(opExpr, "invalid bounds: "+rightSig);
			}
			resultSCType = leftSig.getColType();
		} else {
			checker.addError(opExpr, "invalid type: "+rightSig);
			resultSCType = SCollectionType.NO_COLLECTION;
		}

		Signature vsig = leftSig.clone();
		vsig.setColType(resultSCType);
		opExpr.setSignature(vsig);
		
	}

}

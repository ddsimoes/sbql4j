package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.JavaComplexObject;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectAbstractFactory;
import pl.wcislo.sbql4j.lang.codegen.noqres.QueryCodeGenNoQres;
import pl.wcislo.sbql4j.lang.codegen.nostacks.QueryCodeGenNoStacks;
import pl.wcislo.sbql4j.lang.codegen.simple.QueryCodeGenSimple;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.terminals.Operator;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.lang.tree.visitors.OperatorVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TypeChecker;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.util.Utils;



public class OperatorMoreOrEqual extends Operator {

	public OperatorMoreOrEqual(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitMoreOrEqual(this, treeVisitor, opExpr, subExprs);
	};	
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		Object rightArg = Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());		
		Object leftArg = Utils.toSimpleValue(interpreter.getQres().pop(), interpreter.getStore());
		
		Boolean result;
		if(leftArg instanceof Number && rightArg instanceof Number) {
			Number n1 = (Number) leftArg;
			Number n2 = (Number) rightArg;
			result = n1.doubleValue() >= n2.doubleValue();
		} else {
			Comparable c1 = (Comparable) leftArg;
			Comparable c2 = (Comparable) rightArg;
			int cRes = MathUtils.compareSafe(c1, c2);
			result = cRes >= 0;
		}
		JavaComplexObject res = JavaObjectAbstractFactory.getJavaObjectFactory().
			createJavaComplexObject(result);
		 
		interpreter.getQres().push(res);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identRightArg = gen.generateIdentifier("rightArg");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResult = gen.generateIdentifier("result");
//		String identN1 = gen.generateIdentifier("n1");
//		String identN2 = gen.generateIdentifier("n2");
//		String identC1 = gen.generateIdentifier("c1");
//		String identC2 = gen.generateIdentifier("c2");
//		String identCRes = gen.generateIdentifier("cRes");
//		String identRes = gen.generateIdentifier("res");
//		gen.printExpressionTrace("//OperatorLess - start\n");
//		sb.append("Object "+identRightArg+" = Utils.toSimpleValue(qres.pop(), store);		         \n");
//		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue(qres.pop(), store);                  \n");
//		sb.append("                                                                          \n");
//		sb.append("Boolean "+identResult+";                                                           \n");
//		sb.append("if("+identLeftArg+" instanceof Number && "+identRightArg+" instanceof Number) {             \n");
//		sb.append("	Number "+identN1+" = (Number) "+identLeftArg+";                                            \n");
//		sb.append("	Number "+identN2+" = (Number) "+identRightArg+";                                           \n");
//		sb.append("	"+identResult+" = "+identN1+".doubleValue() >= "+identN2+".doubleValue();                            \n");
//		sb.append("} else {                                                                  \n");
//		sb.append("	Comparable "+identC1+" = (Comparable) "+identLeftArg+";                                    \n");
//		sb.append("	Comparable "+identC2+" = (Comparable) "+identRightArg+";                                   \n");
//		sb.append("	int "+identCRes+" = MathUtils.compareSafe("+identC1+", "+identC2+"); \n");
//		sb.append("	"+identResult+" = "+identCRes+" >= 0;                                                       \n");
//		sb.append("}                                                                         \n");
//		sb.append("JavaComplexObject "+identRes+" = JavaObjectAbstractFactory.getJavaObjectFactory(). \n");
//		sb.append("	createJavaComplexObject("+identResult+");                                         \n");
//		sb.append("qres.push("+identRes+");		                                                     \n");
//		gen.printExpressionTrace("//OperatorLess - end\n");	
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		StringBuilder sb = gen.sb;
//		String identRightArg = gen.generateIdentifier("rightArg");
//		String identLeftArg = gen.generateIdentifier("leftArg");
//		String identResult = gen.generateIdentifier("result");
//		String identN1 = gen.generateIdentifier("n1");
//		String identN2 = gen.generateIdentifier("n2");
//		String identC1 = gen.generateIdentifier("c1");
//		String identC2 = gen.generateIdentifier("c2");
//		String identCRes = gen.generateIdentifier("cRes");
//		String identRes = opExpr.getSignature().getResultName();
//		
//		String identLeftRes = leftExpr.getSignature().getResultName();
//		String identRightRes = rightExpr.getSignature().getResultName();
//		
//		gen.printExpressionTrace("//OperatorLess - start\n");
//		sb.append(" "+opExpr.getSignature().genSBQLDeclarationCode()+"; \n");
//		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store); \n");
//		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store); \n");
//		sb.append("                                                                          \n");
//		sb.append("Boolean "+identResult+";                                                           \n");
//		sb.append("if("+identLeftArg+" instanceof Number && "+identRightArg+" instanceof Number) {             \n");
//		sb.append("	Number "+identN1+" = (Number) "+identLeftArg+";                                            \n");
//		sb.append("	Number "+identN2+" = (Number) "+identRightArg+";                                           \n");
//		sb.append("	"+identResult+" = "+identN1+".doubleValue() >= "+identN2+".doubleValue();                            \n");
//		sb.append("} else {                                                                  \n");
//		sb.append("	Comparable "+identC1+" = (Comparable) "+identLeftArg+";                                    \n");
//		sb.append("	Comparable "+identC2+" = (Comparable) "+identRightArg+";                                   \n");
//		sb.append("	int "+identCRes+" = MathUtils.compareSafe("+identC1+", "+identC2+"); \n");
//		sb.append("	"+identResult+" = "+identCRes+" >= 0;                                                       \n");
//		sb.append("}                                                                         \n");
//		sb.append(" "+identRes+" = JavaObjectAbstractFactory.getJavaObjectFactory(). \n");
//		sb.append("	createJavaComplexObject("+identResult+");                                         \n");
////		sb.append("qres.push("+identRes+");		                                                     \n");
//		gen.printExpressionTrace("//OperatorLess - end\n");	
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		StringBuilder sb = gen.sb;
//		String identResult = opExpr.getSignature().getResultName();
//		
//		String identLeftRes = leftExpr.getSignature().getResultName();
//		String identRightRes = rightExpr.getSignature().getResultName();
//		
//		ValueSignature s1 = (ValueSignature)leftExpr.getSignature().getDerefSignatureWithCardinality();
//		ValueSignature s2 = (ValueSignature)rightExpr.getSignature().getDerefSignatureWithCardinality();
//		
//		ClassTypes ct = ClassTypes.getInstance();
//		Type comparableType = ct.getCompilerType(Comparable.class);
//		Type numberType = ct.getCompilerType(Number.class);
//		
//		gen.printExpressionTrace("//OperatorMoreOrEqual - start\n");
////		sb.append(" "+opExpr.signature.genSBQLDeclarationCode()+"; \n");
////		sb.append("Object "+identRightArg+" = Utils.toSimpleValue("+identRightRes+", store); \n");
////		sb.append("Object "+identLeftArg+" = Utils.toSimpleValue("+identLeftRes+", store); \n");
//		sb.append("                                                                          \n");
//		sb.append("Boolean "+identResult+" = ");
//		if(ct.isSubClass(s1.type, numberType) && ct.isSubClass(s2.type, numberType)) {
//			sb.append(identLeftRes+" >= "+identRightRes+"; \n");
//		} else {
//			sb.append(identLeftRes+".compareTo("+identRightRes+") >= 0; \n");
//		}
//		gen.printExpressionTrace("//OperatorMoreOrEqual - end\n");	
//	}

	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression leftExpr = args[0];
		Expression rightExpr = args[1];
		Expression opExpr = args[2];
		
		Signature s1 = leftExpr.getSignature().getDerefSignatureWithCardinality();
		Signature s2 = rightExpr.getSignature().getDerefSignatureWithCardinality();
		boolean isLeftSigValue = s1 instanceof ValueSignature;
		boolean isRightSigValue = s2 instanceof ValueSignature;
		if(isLeftSigValue && isRightSigValue) {
			ValueSignature vs1 = (ValueSignature) s1;
			ValueSignature vs2 = (ValueSignature) s2;
			ValueSignature numberSig = 
				JavaSignatureAbstractFactory.getJavaSignatureFactory().
				createJavaSignature(ClassTypes.getInstance().getCompilerType(Number.class));
			numberSig.setColType(SCollectionType.NO_COLLECTION);
			boolean isLeftTypeNumber = vs1.isTypeCompatible(numberSig);
			boolean isRightTypeNumber = vs2.isTypeCompatible(numberSig);
			if(!isLeftTypeNumber || !isRightTypeNumber) {
				//may be both comparable and same type 
				ValueSignature comparableSig = 
					JavaSignatureAbstractFactory.getJavaSignatureFactory().
					createJavaSignature(ClassTypes.getInstance().getCompilerType(Comparable.class));
				comparableSig.setColType(SCollectionType.NO_COLLECTION);
				boolean isLeftComparable = vs1.isTypeCompatible(comparableSig);
				boolean isLeftCompRight = vs1.isTypeCompatible(vs2);
				boolean isRightCompLeft = vs2.isTypeCompatible(vs1);
				if(!isLeftComparable || !(isLeftCompRight && isRightCompLeft)) {
					checker.addInvalidTypeError(opExpr, "both Number or same Comparable", "left="+s1.getTypeName()+", right="+s2.getTypeName());
				}
				
			}
		} else {
			if(!isLeftSigValue) {
				checker.addInvalidTypeError(leftExpr, "Number or Comparable", s1.getTypeName());
			}
			if(!isRightSigValue) {
				checker.addInvalidTypeError(leftExpr, "Number or Comparable", s2.getTypeName());
			}
		}
		Type returnType = ClassTypes.getInstance().getCompilerType(Boolean.class);
		ValueSignature vsig = JavaSignatureAbstractFactory.getJavaSignatureFactory().
			createValueSignature(returnType, false);
		opExpr.setSignature(vsig);
	}

}

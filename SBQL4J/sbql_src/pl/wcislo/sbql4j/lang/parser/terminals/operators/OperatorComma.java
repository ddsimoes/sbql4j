package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import pl.wcislo.sbql4j.java.model.compiletime.BinderSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.ResultSource;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.lang.codegen.noqres.QueryCodeGenNoQres;
import pl.wcislo.sbql4j.lang.codegen.nostacks.QueryCodeGenNoStacks;
import pl.wcislo.sbql4j.lang.codegen.simple.QueryCodeGenSimple;
import pl.wcislo.sbql4j.lang.parser.expression.BinaryExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.terminals.Operator;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.lang.tree.visitors.OperatorVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TypeChecker;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorComma extends Operator {

	public OperatorComma(OperatorType type) {
		super(type);
	}

	@Override
	public <T, V extends TreeVisitor> T accept(OperatorVisitor<T,V> opVisitor, V treeVisitor, Expression opExpr, Expression[] subExprs) {
		return opVisitor.visitComma(this, treeVisitor, opExpr, subExprs);
	};
	
	@Override
	public void eval(Interpreter interpreter, Expression... args) {
		Bag eres = new Bag();
		QueryResult e2res = interpreter.getQres().pop();
		QueryResult e1res = interpreter.getQres().pop();

		QueryResult res = Utils.cartesianProduct(e1res, e2res);
		interpreter.getQres().push(res);
	}
	
//	@Override
//	public void generateSimpleCode(QueryCodeGenSimple gen, Expression... args) {
//		StringBuilder sb = gen.sb;
//		String identEres = gen.generateIdentifier("eres");
//		String identE2Res = gen.generateIdentifier("e2res");
//		String identE1Res = gen.generateIdentifier("e1res");
//		String identRes = gen.generateIdentifier("res");
//		
//		gen.printExpressionTrace("//OperatorComma - start \n");                                 
//		sb.append("Bag "+identEres+" = new Bag();                                       \n");
//		sb.append("QueryResult "+identE2Res+" = qres.pop();                             \n");
//		sb.append("QueryResult "+identE1Res+" = qres.pop();                             \n");
//		sb.append("QueryResult "+identRes+" = Utils.cartesianProduct("+identE1Res+", "+identE2Res+");     \n");
//		sb.append("qres.push("+identRes+");                                             \n");
//		gen.printExpressionTrace("//OperatorComma - end \n");
//	}
//	
//	@Override
//	public void generateSimpleCodeNoQres(QueryCodeGenNoQres gen, Expression... args) {
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		StringBuilder sb = gen.sb;
//		String identRightVal = rightExpr.getSignature().getResultName();
//		String identLeftVal = leftExpr.getSignature().getResultName();
//		
//		gen.printExpressionTrace("//OperatorComma - start \n");                                 
//		if(opExpr.getSignature().getDerefSignatureWithCardinality().sColType == SCollectionType.NO_COLLECTION) {
//			sb.append(opExpr.getSignature().genSBQLDeclarationCode()+" = Utils.cartesianProductSingle("+identLeftVal+", "+identRightVal+"); \n");
//		} else {
//			sb.append(opExpr.getSignature().genSBQLDeclarationCode()+" = Utils.cartesianProduct("+identLeftVal+", "+identRightVal+"); \n");
//		}
//		gen.printExpressionTrace("//OperatorComma - end \n");	
//	}
//	
//	@Override
//	public void generateCodeNoStacks(QueryCodeGenNoStacks gen, Expression... args) {
//		Expression leftExpr = args[0];
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
//		
//		StringBuilder sb = gen.sb;
//		String identRightVal = rightExpr.getSignature().getResultName();
//		String identLeftVal = leftExpr.getSignature().getResultName();
//		String identOpResult = opExpr.getSignature().getResultName();
//		
//		String leftName = (leftExpr.getSignature() instanceof BinderSignature) ? ((BinderSignature)leftExpr.getSignature()).name : "";
//		String rightName = (rightExpr.getSignature() instanceof BinderSignature) ? ((BinderSignature)rightExpr.getSignature()).name : "";
//		
//		boolean isLeftCol = leftExpr.getSignature().isCollectionResult();
//		boolean isRightCol = rightExpr.getSignature().isCollectionResult();
//		
////		boolean isLeftAuxCol = leftExpr.signature.isAuixiliaryCollection();
////		boolean isRightAuxCol = rightExpr.signature.isAuixiliaryCollection();
//		
//		gen.printExpressionTrace("//OperatorComma - start \n");
////		if(!isLeftAuxCol && !isRightAuxCol) {
//			sb.append(opExpr.getSignature().genJavaDeclarationCode()+" = OperatorUtils.");
//			if(isLeftCol ) {
//				if(isRightCol ) {
//					sb.append("cartesianProductCC");
//				} else {
//					sb.append("cartesianProductCS");
//				}
//			} else {
//				if(isRightCol ) {
//					sb.append("cartesianProductSC");
//				} else {
//					sb.append("cartesianProductSS");
//				}
//			}
//			sb.append("("+identLeftVal+", "+identRightVal+", \""+leftName+"\", \""+rightName+"\"); \n");
////		} else {
////			String identLeftAuxColEl = opExpr.signature.getResultName()+"_leftEl";
////			String identRightAuxColEl = opExpr.signature.getResultName()+"_rightEl";
////			sb.append(opExpr.signature.genJavaDeclarationCode()+" = new "+opExpr.signature.getJavaTypeStringAssigment()+"();\n");
////			if(isLeftAuxCol) {
////				sb.append("for("+leftExpr.signature.getJavaTypeStringSingleResult()+" "+identLeftAuxColEl+" : "+identLeftVal+") {\n");
////				identLeftVal = identLeftAuxColEl; 
////			}
////			if(isRightAuxCol) {
////				sb.append("for("+rightExpr.signature.getJavaTypeStringSingleResult()+" "+identRightAuxColEl+" : "+identRightVal+") {\n");
////				identRightVal = identRightAuxColEl; 
////			}
////			sb.append(identOpResult+".");
////			if(isLeftCol || isRightCol) {
////				sb.append("addAll(");
////			} else {
////				sb.append("add(");
////			}
////			sb.append("OperatorUtils.");
////			if(isLeftCol) {
////				if(isRightCol) {
////					sb.append("cartesianProductCC");
////				} else {
////					sb.append("cartesianProductCS");
////				}
////			} else {
////				if(isRightCol) {
////					sb.append("cartesianProductSC");
////				} else {
////					sb.append("cartesianProductSS");
////				}
////			}
////			sb.append("("+identLeftVal+", "+identRightVal+", \""+leftName+"\", \""+rightName+"\")");
////			sb.append(");\n");
////			if(isRightAuxCol) {
////				sb.append("}\n");
////			}
////			if(isLeftAuxCol) {
////				sb.append("}\n");
////			}
////		}
//		gen.printExpressionTrace("//OperatorComma - end \n");		
//	}
	
	@Override
	public void evalStatic(TypeChecker checker, Expression... args) {
		Expression leftExpr = args[0];
		Expression rightExpr = args[1];
		Expression opExpr = args[2];
		
		Signature s1 = leftExpr.getSignature();
		Signature s2 = rightExpr.getSignature();

		Signature res = Utils.createCartesianSignature(s1, s2);
		ResultSource rs = TypeChecker.calculateResultSource((BinaryExpression) opExpr);
		res.setResultSource(rs);
		opExpr.setSignature(res);
	}

}

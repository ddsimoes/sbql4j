package pl.wcislo.sbql4j.lang.views.replace;

import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.tree.visitors.TraversingASTAdapter;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.Type.ClassType;

public class ViewInvocationReplacer extends TraversingASTAdapter<Expression, Void> {
//	public ViewInvocationReplacer(SBQL4JStatement stmt) {
//		super(stmt);
//	}

	@Override
	public Expression visitNameExpression(NameExpression expr, Void object) {
		Signature bSig = expr.getSignature();
//		if (bSig instanceof ValueSignature) {
//			ValueSignature vSig = (ValueSignature) bSig;
//			Type vType = vSig.type;
//			if (vType instanceof ClassType) {
//				ClassType ct = (ClassType) vType;
//				if (ct.isViewDelcaration()) {
//					if (ct.viewInfo != null) {
//						// now we are sure, that view is used in the query
//						// replacement
//						Expression replacement = ct.viewInfo
//								.getViewExpressionReadReplacement();
//						if (expr != getRootExpression()) {
//							expr.parentExpression.replaceSubExpression(expr,
//									replacement);
//						} else {
//							setRootExpression(replacement);
//						}
//						this.evaluateExpression(replacement);
//						return replacement;
//					}
//				}
//			}
//		}
		return expr;
	}
	
	@Override
	public Expression evaluateExpression(Expression expr) {
		super.evaluateExpression(expr);
		return getRootExpression();
	}
}

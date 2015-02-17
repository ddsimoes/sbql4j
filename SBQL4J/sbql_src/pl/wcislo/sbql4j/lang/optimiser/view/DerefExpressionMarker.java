/**
 * 
 */
package pl.wcislo.sbql4j.lang.optimiser.view;

import pl.wcislo.sbql4j.lang.parser.expression.BinarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ConstructorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.DerefExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorComma;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorStruct;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorUnion;
import pl.wcislo.sbql4j.lang.tree.visitors.TraversingASTAdapter;

/**
 * @author Emil
 *
 */
public class DerefExpressionMarker extends TraversingASTAdapter<Void, Void> {
	private Expression rootExpression;
	public Expression getRootExpression() {
		return rootExpression;
	}
	
	@Override
	public Void evaluateExpression(Expression expr) {
		this.rootExpression = expr;
		return super.evaluateExpression(expr);
	}

	@Override
	public Void visitConstructorExpression(ConstructorExpression expr,
			Void object) {
		return super.visitConstructorExpression(expr, object);
	}
	
	@Override
	public Void visitBinarySimpleOperatorExpression(
			BinarySimpleOperatorExpression expr, Void object) {
		if(! (expr.op instanceof OperatorComma) && 
		   ! (expr.op instanceof OperatorStruct) &&
		   ! (expr.op instanceof OperatorUnion)
		) {
			markExpressionToDeref(expr.ex1);
			markExpressionToDeref(expr.ex2);
		}
		super.visitBinarySimpleOperatorExpression(expr, object);
		return null;
	}
	
	
	private void markExpressionToDeref(Expression ex) {
		DerefExpression de = new DerefExpression(ex);
		Expression parentExpr = ex.parentExpression;
		if(parentExpr == rootExpression) {
			rootExpression = de;
		} else {
			parentExpr.replaceSubExpression(ex, de);
		}
	}
}
 
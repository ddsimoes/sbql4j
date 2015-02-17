package pl.wcislo.sbql4j.lang.db4o.indexes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.wcislo.sbql4j.lang.parser.expression.BinarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorType;
import pl.wcislo.sbql4j.lang.tree.visitors.TraversingASTAdapter;

public class PredicateFinder extends TraversingASTAdapter {
	private Set<Expression> andExpr = new HashSet<Expression>();
	private boolean foundOr;
	
	@Override
	public Object visitBinarySimpleOperatorExpression(
			BinarySimpleOperatorExpression expr, Object object) {
		if(expr.op.type == OperatorType.AND) {
			checkAndCandidate(expr.ex1);
			checkAndCandidate(expr.ex2);
		} else if(expr.op.type == OperatorType.OR) {
			foundOr = true;
		}
		return super.visitBinarySimpleOperatorExpression(expr, object);
//		return null;
	}
	
	private void checkAndCandidate(Expression expr) {
		if(expr instanceof BinarySimpleOperatorExpression) {
			BinarySimpleOperatorExpression ex = (BinarySimpleOperatorExpression) expr;
			OperatorType exType = ex.op.type;
			if(exType == OperatorType.AND || exType == OperatorType.OR) {
				return;
			} else {
				andExpr.add(ex);
			}
		} else {
			andExpr.add(expr);
		}
	}
	
	public Set<Expression> getAndExpr() {
		return andExpr;
	}
	public boolean isFoundOr() {
		return foundOr;
	}
}

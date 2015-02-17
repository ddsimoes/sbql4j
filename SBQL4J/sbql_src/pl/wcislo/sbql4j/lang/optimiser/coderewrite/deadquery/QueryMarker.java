package pl.wcislo.sbql4j.lang.optimiser.coderewrite.deadquery;

import pl.wcislo.sbql4j.lang.parser.expression.AsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.BinarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.CloseByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ComaExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ConditionalExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ConstructorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.DerefExpression;
import pl.wcislo.sbql4j.lang.parser.expression.DotExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.ForEachExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ForallExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ForanyExpression;
import pl.wcislo.sbql4j.lang.parser.expression.GroupAsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.JoinExpression;
import pl.wcislo.sbql4j.lang.parser.expression.LiteralExpression;
import pl.wcislo.sbql4j.lang.parser.expression.MethodExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression;
import pl.wcislo.sbql4j.lang.parser.expression.RangeExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class QueryMarker implements TreeVisitor {

	@Override
	public Object evaluateExpression(Expression expr) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitAsExpression(AsExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitBinarySimpleOperatorExpression(BinarySimpleOperatorExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitCloseByExpression(CloseByExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitComaExpression(ComaExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitConditionalExpression(ConditionalExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitConstructorExpression(ConstructorExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitDerefExpression(DerefExpression derefExpression, Object object) {
		commonVisitExpression(derefExpression);
		return null;
	}

	@Override
	public Object visitDotExpression(DotExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitForallExpression(ForallExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitForanyExpression(ForanyExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitForEachExpression(ForEachExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitGroupAsExpression(GroupAsExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitJoinExpression(JoinExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitLiteralExpression(LiteralExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitMethodExpression(MethodExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitNameExpression(NameExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitOrderByExpression(OrderByExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitOrderByParamExpression(OrderByParamExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitRangeExpression(RangeExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitUnaryExpression(UnarySimpleOperatorExpression expr, Object object) {
		commonVisitExpression(expr);
		return null;
	}

	@Override
	public Object visitWhereExpression(WhereExpression whereExpression, Object object) {
		commonVisitExpression(whereExpression);
		return null;
	}
	
	protected void commonVisitExpression(Expression ex) {
		if(!ex.deadQData.isMarked()) {
			ex.deadQData.setMarked(true);
			Expression upExpr = ex;
			while(upExpr.parentExpression != null 
					&& !upExpr.parentExpression.deadQData.isMarked()) {
//				if(upExpr.parentExpression instanceof ConditionalExpression) {
//					upExpr.parentExpression.accept(this, null);
//				}
				upExpr.parentExpression.deadQData.setMarked(true);
				upExpr = upExpr.parentExpression;
			}
			for(Expression e : ex.deadQData.getResultingExpressions()) {
				e.accept(this, null);
			}
		}
	}
	
}

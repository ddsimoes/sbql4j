package pl.wcislo.sbql4j.lang.tree.visitors;

import pl.wcislo.sbql4j.java.utils.SBQL4JStatement;
import pl.wcislo.sbql4j.lang.parser.expression.AsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.BinaryExpression;
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
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.JoinExpression;
import pl.wcislo.sbql4j.lang.parser.expression.LiteralExpression;
import pl.wcislo.sbql4j.lang.parser.expression.MethodExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression;
import pl.wcislo.sbql4j.lang.parser.expression.RangeExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;

/**
 * TraversingASTAdapter Visitor that performs tree traversal Added visit
 * auxiliary methods for generalizing common behavior of unary, binary
 * (algebraic and nonalgebraic) visitors
 * 
 * Its similar to TraversingASTAdapter from ODRA2 system
 * 
 */
public class TraversingASTAdapter<R,T> implements TreeVisitor<R,T> {

//	public TraversingASTAdapter(SBQL4JStatement stmt) {
//		this.stmt = stmt;
//	}
	
	private Expression rootExpr;
	
//	/**
//	 * used if its necessary to replace root expression
//	 */
//	private SBQL4JStatement stmt;
	protected R commonVisitUnaryExpression(UnarySimpleOperatorExpression expr, T attr) {
		expr.ex1.accept(this, attr);
		return commonVisitExpression(expr, attr);
	}

	protected R commonVisitBinaryExpression(BinaryExpression expr, T attr) {
		expr.ex1.accept(this, attr);
		expr.ex2.accept(this, attr);
		return commonVisitExpression(expr, attr);
	}
	
	protected R commonVisitExpression(Expression expr, T attr) {
		return null;
	}
	
	@Override
	public R evaluateExpression(Expression expr) {
		this.rootExpr = expr;
		return expr.accept(this, null);
	}

	@Override
	public R visitAsExpression(AsExpression expr, T object) {
		return expr.ex1.accept(this, null);
	}

	@Override
	public R visitBinarySimpleOperatorExpression(BinarySimpleOperatorExpression expr, T object) {
		return commonVisitBinaryExpression(expr, object);
	}

	@Override
	public R visitCloseByExpression(CloseByExpression expr, T object) {
		return commonVisitBinaryExpression(expr, object);
	}

	@Override
	public R visitComaExpression(ComaExpression expr, T object) {
		return commonVisitBinaryExpression(expr, object);
	}

	@Override
	public R visitConditionalExpression(ConditionalExpression expr, T object) {
		expr.conditionExpr.accept(this, object);
		expr.trueExpr.accept(this, object);
		expr.falseExpr.accept(this, object);
		return null;
	}

	@Override
	public R visitConstructorExpression(ConstructorExpression expr, T object) {
		if(expr.classNameExpr != null) {
			expr.classNameExpr.accept(this, object);
		}
		if(expr.paramsExpression != null) {
			expr.paramsExpression.accept(this, object);
		}
		return null;
	}

	@Override
	public R visitDerefExpression(DerefExpression derefExpression, T object) {
		return derefExpression.ex1.accept(this, object);
	}

	@Override
	public R visitDotExpression(DotExpression expr, T object) {
		return commonVisitBinaryExpression(expr, object);
	}

	@Override
	public R visitForEachExpression(ForEachExpression expr, T object) {
		if(expr.exprs != null) {
			for(Expression e : expr.exprs) {
				e.accept(this, object);
			}
		}
		return null;
	}

	@Override
	public R visitForallExpression(ForallExpression expr, T object) {
		return commonVisitBinaryExpression(expr, object);
	}

	@Override
	public R visitForanyExpression(ForanyExpression expr, T object) {
		return commonVisitBinaryExpression(expr, object);
	}

	@Override
	public R visitGroupAsExpression(GroupAsExpression expr, T object) {
		return expr.ex1.accept(this, object);
	}

	@Override
	public R visitNameExpression(NameExpression expr, T object) {
		return null;
	}

	@Override
	public R visitJoinExpression(JoinExpression expr, T object) {
		return commonVisitBinaryExpression(expr, object);
	}

	@Override
	public R visitLiteralExpression(LiteralExpression expr, T object) {
		return null;
	}

	@Override
	public R visitMethodExpression(MethodExpression expr, T object) {
		if(expr.paramsExpression != null) {
			expr.paramsExpression.accept(this, object);
		}
		return null;
	}

	@Override
	public R visitOrderByExpression(OrderByExpression expr, T object) {
		expr.ex1.accept(this, object);
		if(expr.paramExprs != null) {
			for(Expression e : expr.paramExprs) {
				e.accept(this, object);
			}
		}
		return null;
	}

	@Override
	public R visitOrderByParamExpression(OrderByParamExpression expr, T object) {
		expr.paramExpression.accept(this, object);
		if(expr.comparatorExpression != null) {
			expr.comparatorExpression.accept(this, object);
		}
		return null;
	}

	@Override
	public R visitRangeExpression(RangeExpression expr, T object) {
		if(expr.ex1 != null) {
			expr.ex1.accept(this, object);
		}
		if(expr.ex2 != null) {
			expr.ex2.accept(this, object);
		}
		return null;
	}

	@Override
	public R visitUnaryExpression(UnarySimpleOperatorExpression expr, T object) {
		return commonVisitUnaryExpression(expr, object);
	}

	@Override
	public R visitWhereExpression(WhereExpression expr, T object) {
		return commonVisitBinaryExpression(expr, object);
	}

	protected Expression getRootExpression() {
		return rootExpr;
	}
	protected void setRootExpression(Expression expr) {
		this.rootExpr = expr;
	}
//	protected SBQL4JStatement getStatement() {
//		return stmt;
//	}
}
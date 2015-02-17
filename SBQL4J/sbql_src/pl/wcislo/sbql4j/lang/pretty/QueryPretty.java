package pl.wcislo.sbql4j.lang.pretty;

import java.util.Iterator;

import pl.wcislo.sbql4j.lang.optimiser.coderewrite.db4oindex.Db4oIndexInvocationExpression;
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
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.JoinExpression;
import pl.wcislo.sbql4j.lang.parser.expression.LiteralExpression;
import pl.wcislo.sbql4j.lang.parser.expression.MethodExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression;
import pl.wcislo.sbql4j.lang.parser.expression.RangeExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression.SortType;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class QueryPretty implements TreeVisitor {

	private StringBuilder sb = new StringBuilder();
	private QueryOperatorPretty opPretty = new QueryOperatorPretty(this);
	
	public String toString() {
		return sb.toString();
	}
	
	void print(String s) {
		sb.append(s);
	}
	
	public static String printQuery(Expression rootTreeNode) {
		return new QueryPretty().evaluateExpression(rootTreeNode).toString();
	}
	
	@Override
	public String evaluateExpression(Expression expr) {
		expr.accept(this, null);
		return sb.toString();
	}

	@Override
	public Object visitAsExpression(AsExpression expr, Object object) {
//		print("(");
		acceptSubexpressionWithPriority(expr.ex1, expr, this);
		print(" as "+expr.identifier.val);
//		print(")");
		return object;
	}

	@Override
	public Object visitBinarySimpleOperatorExpression(BinarySimpleOperatorExpression expr, Object object) {
//		expr.ex1.accept(this, object);
		expr.op.accept(opPretty, this, expr, expr.ex1, expr.ex2);
//		print(" ");0
		return null;
	}

	@Override
	public Object visitCloseByExpression(CloseByExpression expr, Object object) {
//		expr.ex1.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex1, expr, this);
		print(" close by ");
//		expr.ex2.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex2, expr, this);
//		print(" ");
		return null;
	}

	@Override
	public Object visitComaExpression(ComaExpression expr, Object object) {
//		expr.ex1.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex1, expr, this);
		print(", ");
//		expr.ex2.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex2, expr, this);
//		print(" ");
		return null;
	}

	@Override
	public Object visitConditionalExpression(ConditionalExpression expr, Object object) {
//		expr.conditionExpr.accept(this, object);
		acceptSubexpressionWithPriority(expr.conditionExpr, expr, this);
		print(" ? ");
//		expr.trueExpr.accept(this, object);
		acceptSubexpressionWithPriority(expr.trueExpr, expr, this);
		print(" : ");
//		expr.falseExpr.accept(this, object);
		acceptSubexpressionWithPriority(expr.falseExpr, expr, this);
//		print(" ");
		return null;
	}

	@Override
	public Object visitConstructorExpression(ConstructorExpression expr, Object object) {
		print(" new ");
		if(expr.classNameExpr != null) {
			expr.classNameExpr.accept(this, object);
		} else if(expr.classNameLiteral != null) {
			print(expr.classNameLiteral);
		}
		print("(");
		if(expr.paramsExpression != null) {
			expr.paramsExpression.accept(this, object);
		}
		print(")");
		return null;
	}

	@Override
	public Object visitDerefExpression(DerefExpression derefExpression, Object object) {
		print("deref(");
		acceptSubexpressionWithPriority(derefExpression.ex1, derefExpression, this);
		print(")");
		return null;
	}

	@Override
	public Object visitDotExpression(DotExpression expr, Object object) {
//		expr.ex1.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex1, expr, this);
		print(".");
//		expr.ex2.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex2, expr, this);
//		print(" ");
		return null;
	}

	@Override
	public Object visitForEachExpression(ForEachExpression expr, Object object) {
		print(" foreach(");
		expr.toIterate.accept(this, object);
		print(") {\n");
		for(Expression e : expr.exprs) {
			e.accept(this, object);
			print("; \n");
		}
		print("} \n");
		return null;
	}

	@Override
	public Object visitForallExpression(ForallExpression expr, Object object) {
		print(" all ");
//		expr.ex1.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex1, expr, this);
		print(" ");
//		expr.ex2.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex2, expr, this);
//		print(" ");
		return null;
	}

	@Override
	public Object visitForanyExpression(ForanyExpression expr, Object object) {
		print(" any ");
//		expr.ex1.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex1, expr, this);
		print(" ");
//		expr.ex2.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex2, expr, this);
//		print(" ");
		return null;
	}

	@Override
	public Object visitGroupAsExpression(GroupAsExpression expr, Object object) {
		
//		expr.expr1.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex1, expr, this);
		print(" group as ");
		print(expr.identifier.val);
//		print(" ");
		return null;
	}

	@Override
	public Object visitNameExpression(NameExpression expr, Object object) {
		if(expr instanceof Db4oIndexInvocationExpression) {
			Db4oIndexInvocationExpression idxExpr = (Db4oIndexInvocationExpression) expr;
			print(idxExpr.l.val+"_ByIndex["+idxExpr.getIndexedFieldName()+"](");
			idxExpr.getParamExpression().accept(this, null);
			print(")");
		} else {
			print(expr.l.val);
		}
//		print(" ");
		return null;
	}

	@Override
	public Object visitJoinExpression(JoinExpression expr, Object object) {
//		expr.ex1.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex1, expr, this);
		print(" join ");
//		expr.ex2.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex2, expr, this);
//		print(" ");
		return null;
	}

	@Override
	public Object visitLiteralExpression(LiteralExpression expr, Object object) {
		print(expr.generateJavaCode());
//		print(" ");
		return null;
	}

	@Override
	public Object visitMethodExpression(MethodExpression expr, Object object) {
		print(expr.methodName);
		print("(");
		if(expr.paramsExpression != null) {
			expr.paramsExpression.accept(this, object);
		}
		print(")");
		return null;
	}

	@Override
	public Object visitOrderByExpression(OrderByExpression expr, Object object) {
//		expr.ex1.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex1, expr, this);
		print(" order by (");
		for(Iterator<OrderByParamExpression> eIt = expr.paramExprs.iterator(); eIt.hasNext(); ) {
			OrderByParamExpression e = eIt.next(); 
//			e.accept(this, object);
			acceptSubexpressionWithPriority(e, expr, this);
			if(eIt.hasNext()) {
				print(", ");
			}
		}
		print(")");
		return null;
	}

	@Override
	public Object visitOrderByParamExpression(OrderByParamExpression expr, Object object) {
		expr.paramExpression.accept(this, object);
		if(expr.sortType != SortType.ASC) {
			print(" ");
			print(expr.sortType.toString());
			print(" ");
		}
		if(expr.comparatorExpression != null) {
			print(" using ");
			expr.comparatorExpression.accept(this, object);
		}
		return null;
	}

	@Override
	public Object visitRangeExpression(RangeExpression expr, Object object) {
//		expr.ex1.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex1, expr, this);
		print("..");
		if(!expr.isUpperUnbounded && expr.ex2 != null) {
//			expr.ex2.accept(this, object);
			acceptSubexpressionWithPriority(expr.ex2, expr, this);
		} else {
			print("*");
		}
		return null;
	}

	@Override
	public Object visitUnaryExpression(UnarySimpleOperatorExpression expr, Object object) {
		expr.op.accept(opPretty, this, expr, expr.ex1);
//		expr.ex1.accept(this, object);
		return null;
	}

	@Override
	public Object visitWhereExpression(WhereExpression expr, Object object) {
//		expr.ex1.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex1, expr, this);
		print(" where ");
//		expr.ex2.accept(this, object);
		acceptSubexpressionWithPriority(expr.ex2, expr, this);
		return null;
	}

	
	private void acceptSubexpressionWithPriority(Expression subExpr, Expression parentExpr, TreeVisitor vis) {
		boolean useParenthesis = subExpr.priority < parentExpr.priority;
		if(useParenthesis) {
			print("(");
		}
		subExpr.accept(vis, null);
		if(useParenthesis) {
			print(")");
		}
	}
}

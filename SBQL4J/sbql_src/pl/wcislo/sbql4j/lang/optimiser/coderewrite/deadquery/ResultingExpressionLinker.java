package pl.wcislo.sbql4j.lang.optimiser.coderewrite.deadquery;

import pl.wcislo.sbql4j.java.model.compiletime.BindResult;
import pl.wcislo.sbql4j.java.model.compiletime.BinderSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.StaticEVNSType;
import pl.wcislo.sbql4j.lang.parser.expression.AsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.BinaryExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ConditionalExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ConstructorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.DotExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.ForEachExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ForallExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ForanyExpression;
import pl.wcislo.sbql4j.lang.parser.expression.GroupAsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression;
import pl.wcislo.sbql4j.lang.parser.expression.RangeExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.tree.visitors.TraversingASTAdapter;

public class ResultingExpressionLinker extends TraversingASTAdapter {
	
	@Override
	protected Object commonVisitBinaryExpression(BinaryExpression expr, Object attr) {
		boolean onlyRight = 
			(expr instanceof DotExpression) ||
			(expr instanceof ForallExpression) ||
			(expr instanceof ForanyExpression);
		if(!onlyRight) {
			expr.deadQData.addResultingExpression(expr.ex1);
//			expr.ex1.accept(this, attr);
		}
		expr.deadQData.addResultingExpression(expr.ex2);
		expr.deadQData.setResultingLinkerVisited(true);
		expr.ex1.accept(this, attr);
		expr.ex2.accept(this, attr);
		return null;
	}
	
	@Override
	protected Object commonVisitUnaryExpression(UnarySimpleOperatorExpression expr, Object attr) {
		expr.deadQData.addResultingExpression(expr.ex1);
		expr.deadQData.setResultingLinkerVisited(true);
		return super.commonVisitUnaryExpression(expr, attr);
	}
	
	@Override
	public Object visitNameExpression(NameExpression expr, Object object) {
		if(!expr.deadQData.isResultingLinkerVisited()) {
			expr.deadQData.setResultingLinkerVisited(true);
			for(BindResult br : expr.getBindResults()) {
				if(br.binder != null) {
					if(br.binder instanceof StaticEVNSType) {
						Expression resultingFrom = ((Signature)br.binder).getAssociatedExpression();
						if(resultingFrom != null) {
							expr.deadQData.addResultingExpression(resultingFrom);
							evaluateExpression(resultingFrom);
						}
					}
				}
			}
		}
		return super.visitNameExpression(expr, object);
	}
	
	@Override
	public Object visitAsExpression(AsExpression expr, Object object) {
		expr.deadQData.addResultingExpression(expr.ex1);
		expr.deadQData.setResultingLinkerVisited(true);
		return super.visitAsExpression(expr, object);
	}
	
	@Override
	public Object visitGroupAsExpression(GroupAsExpression expr, Object object) {
		expr.deadQData.addResultingExpression(expr.ex1);
		expr.deadQData.setResultingLinkerVisited(true);
		return super.visitGroupAsExpression(expr, object);
	}
	
	@Override
	public Object visitConditionalExpression(ConditionalExpression expr, Object object) {
		expr.deadQData.addResultingExpression(expr.conditionExpr);
		expr.deadQData.addResultingExpression(expr.trueExpr);
		expr.deadQData.addResultingExpression(expr.falseExpr);
		expr.deadQData.setResultingLinkerVisited(true);
		return super.visitConditionalExpression(expr, object);
	}
	
	@Override
	public Object visitConstructorExpression(ConstructorExpression expr, Object object) {
		if(expr.classNameExpr != null) {
			expr.deadQData.addResultingExpression(expr.classNameExpr);
		}
		if(expr.paramsExpression != null) {
			expr.deadQData.addResultingExpression(expr.paramsExpression);
		}
		expr.deadQData.setResultingLinkerVisited(true);
		return super.visitConstructorExpression(expr, object);
	}
	
	@Override
	public Object visitForEachExpression(ForEachExpression expr, Object object) {
		if(expr.exprs != null) {
			for(Expression e : expr.exprs) {
				expr.deadQData.addResultingExpression(e);
			}
		}
		expr.deadQData.setResultingLinkerVisited(true);
		return null;
	}
	
	@Override
	public Object visitRangeExpression(RangeExpression expr, Object object) {
		if(expr.ex1 != null) {
			expr.deadQData.addResultingExpression(expr.ex1);
		}
		if(expr.ex2 != null) {
			expr.deadQData.addResultingExpression(expr.ex2);
		}
		expr.deadQData.setResultingLinkerVisited(true);
		return null;
	}
	
	@Override
	public Object visitOrderByExpression(OrderByExpression expr, Object object) {
		expr.deadQData.addResultingExpression(expr.ex1);
		if(expr.paramExprs != null) {
			for(Expression e : expr.paramExprs) {
				expr.deadQData.addResultingExpression(e);
			}
		}
		expr.deadQData.setResultingLinkerVisited(true);
		return null;
	}

	@Override
	public Object visitOrderByParamExpression(OrderByParamExpression expr, Object object) {
		expr.deadQData.addResultingExpression(expr.paramExpression);
		if(expr.comparatorExpression != null) {
			expr.deadQData.addResultingExpression(expr.comparatorExpression);
		}
		expr.deadQData.setResultingLinkerVisited(true);
		return null;
	}
	
	
}
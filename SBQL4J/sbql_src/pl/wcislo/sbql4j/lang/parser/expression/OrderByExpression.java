package pl.wcislo.sbql4j.lang.parser.expression;

import java.util.List;

import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class OrderByExpression extends Expression implements NestingExpression {
	public final Expression ex1;
//	public final Expression ex2;
	public final List<OrderByParamExpression> paramExprs;
	
	public OrderByExpression(int pos, Expression ex1, List<OrderByParamExpression> paramExprs) {
		super(pos, ExpressionType.ORDER_BY.priority);
		this.ex1 = ex1;
//		this.ex2 = ex2;
		this.paramExprs = paramExprs;
		
		setParentExpression(ex1);
		for(Expression e : paramExprs) {
			setParentExpression(e);	
		}
	}
	
	public OrderByExpression(Expression ex1, List<OrderByParamExpression> paramExprs) {
		this(-1, ex1, paramExprs);
	}
	
	@Override
	public void replaceSubExpression(Expression oldExpr, Expression newExpr) {
		
	}
	
	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitOrderByExpression(this, object);
	}
	
	private NestingExpressionHelper nestedExpressionHelper = new NestingExpressionHelper();
	@Override
	public String getNestedVarName() {
		return nestedExpressionHelper.getNestedVarName();
	}
	@Override
	public void setNestedVarName(String nestedVarName) {
		this.nestedExpressionHelper.setNestedVarName(nestedVarName);
	}
	@Override
	public String getNestedLoopVarName() {
		return nestedExpressionHelper.getNestedLoopVarName();
	}
	@Override
	public void setNestedLoopVarName(String nestedLoopVarName) {
		this.nestedExpressionHelper.setNestedLoopVarName(nestedLoopVarName);
	}
	@Override
	public int getENVSOpeningLevel() {
		return nestedExpressionHelper.getENVSOpeningLevel();
	}
	@Override
	public void setENVSOpeningLevel(int lev) {
		this.nestedExpressionHelper.setENVSOpeningLevel(lev);
	}
	@Override
	public Expression getExpression() {
		return this;
	}
}
package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class CloseByExpression extends BinaryExpression implements NestingExpression {
		
	public CloseByExpression(int pos, Expression ex1, Expression ex2) {
		super(pos, ExpressionType.CLOSE_BY.priority, ex1, ex2);
	}
	
	public CloseByExpression(Expression ex1, Expression ex2) {
		this(-1, ex1, ex2);
	}

	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitCloseByExpression(this, object);
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

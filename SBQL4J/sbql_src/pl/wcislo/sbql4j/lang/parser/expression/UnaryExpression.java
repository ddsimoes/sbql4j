package pl.wcislo.sbql4j.lang.parser.expression;


public abstract class UnaryExpression extends Expression {

	public Expression ex1;
	
	public UnaryExpression(int pos, Expression ex1, int priority) {
		super(pos, priority);
		this.ex1 = ex1;
		setParentExpression(ex1);
	}
	
	public UnaryExpression(Expression ex1, int priority) {
		this(-1, ex1, priority);
	}
	
	@Override
	public void replaceSubExpression(Expression oldExpr, Expression newExpr) {
		if(ex1 == oldExpr) {
			ex1 = newExpr;
			setParentExpression(newExpr);
		}
	}
}

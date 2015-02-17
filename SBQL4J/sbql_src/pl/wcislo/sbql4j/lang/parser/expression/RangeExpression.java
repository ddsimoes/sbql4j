package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class RangeExpression extends Expression {
	public final Expression ex1;
	public final Expression ex2;
	public final boolean isUpperUnbounded;
	
	public RangeExpression(int pos, Expression ex1, Expression ex2) {
		super(pos, ExpressionType.RANGE.priority);
		this.ex1 = ex1;
		this.ex2 = ex2;
		this.isUpperUnbounded = false;
		setParentExpression(ex1);
		setParentExpression(ex2);
	}
	
	public RangeExpression(Expression ex1, Expression ex2) {
		this(-1, ex1);
	}
	
	public RangeExpression(int pos, Expression ex1) {
		super(pos, ExpressionType.RANGE.priority);
		this.ex1 = ex1;
		this.ex2 = null; 
		this.isUpperUnbounded = true;
		setParentExpression(ex1);
	}
	
	public RangeExpression(Expression ex1) {
		this(-1, ex1);
	}

	
	
	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitRangeExpression(this, object);
	}

	@Override
	public void replaceSubExpression(Expression oldExpr, Expression newExpr) {
		
	}
	
}

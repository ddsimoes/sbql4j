package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class ConditionalExpression extends Expression {
	public Expression conditionExpr;
	public Expression trueExpr;
	public Expression falseExpr;
	
	public ConditionalExpression(int pos, Expression conditionExpr, Expression trueExpr, Expression falseExpr) {
		super(pos, ExpressionType.CONDITIONAL.priority);
		this.conditionExpr = conditionExpr;
		this.trueExpr = trueExpr;
		this.falseExpr = falseExpr;
		setParentExpression(conditionExpr);
		setParentExpression(trueExpr);
		setParentExpression(falseExpr);
	}
	
	public ConditionalExpression(Expression conditionExpr, Expression trueExpr, Expression falseExpr) {
		this(-1, conditionExpr, trueExpr, falseExpr);
	}

	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitConditionalExpression(this, object);
	}
	
	@Override
	public void replaceSubExpression(Expression oldExpr, Expression newExpr) {
		if(oldExpr.equals(conditionExpr)) {
			conditionExpr = newExpr;
			setParentExpression(newExpr);
		} else if(oldExpr.equals(trueExpr)) {
			trueExpr = newExpr;
			setParentExpression(newExpr);
		} else if(oldExpr.equals(falseExpr)) {
			falseExpr = newExpr;
			setParentExpression(newExpr);
		}
	}

}

package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class ComaExpression extends BinaryExpression {

	public ComaExpression(int pos, Expression ex1, Expression ex2) {
		super(pos, ExpressionType.COMMA.priority, ex1, ex2);
	}
	
	public ComaExpression(Expression ex1, Expression ex2) {
		this(-1, ex1, ex2);
	}
	

	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		Object o = visitor.visitComaExpression(this, object);
		return o;
	}
}

package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class DerefExpression extends UnaryExpression {
	
	public DerefExpression(int pos, Expression ex1) {
		super(pos, ex1, ExpressionType.DEREF.priority);
	}
	
	public DerefExpression(Expression ex1) {
		this(-1, ex1);
	}

	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitDerefExpression(this, object);
	}
}
package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.lang.parser.terminals.Name;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class GroupAsExpression extends UnaryExpression {
//	public Expression expr1;
	public Name identifier;
	
	public GroupAsExpression(int pos, Expression expr1, Name identifier) {
		super(pos, expr1, ExpressionType.GROUP_AS.priority);
		this.identifier = identifier;
		setParentExpression(expr1);
	}
	
	public GroupAsExpression(Expression expr1, Name identifier) {
		this(-1, expr1, identifier);
	}

	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitGroupAsExpression(this, object);
	}
}

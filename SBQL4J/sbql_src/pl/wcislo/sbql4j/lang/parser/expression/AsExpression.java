package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.lang.parser.terminals.Name;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class AsExpression extends UnaryExpression {
	public Name identifier;
	
	public AsExpression(int pos, Expression expr1, Name identifier) {
		super(pos, expr1, ExpressionType.AS.priority);
		this.identifier = identifier;
		setParentExpression(expr1);
	}
	
	public AsExpression(Expression expr1, Name identifier) {
		this(-1, expr1, identifier);
	}

	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitAsExpression(this, object);
	}

}

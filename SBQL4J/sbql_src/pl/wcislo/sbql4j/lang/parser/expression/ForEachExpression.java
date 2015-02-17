package pl.wcislo.sbql4j.lang.parser.expression;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class ForEachExpression extends Expression {
	public final Expression toIterate;
	public final List<Expression> exprs;
	
	public ForEachExpression(int pos, Expression toIterate, List<Expression> exprs) {
		super(pos, ExpressionType.FOREACH.priority);
		this.toIterate = toIterate;
		this.exprs = exprs;
		setParentExpression(toIterate);
		setParentExpression(exprs);
	}
	
	public ForEachExpression(Expression toIterate, List<Expression> exprs) {
		this(-1, toIterate, exprs);
	}

	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitForEachExpression(this, object);
	}
	
	@Override
	public void replaceSubExpression(Expression oldExpr, Expression newExpr) {
		ArrayList<Expression> newList = new ArrayList<Expression>(exprs);
		for(int i=0; i<newList.size(); i++) {
			if(newList.get(i).equals(oldExpr)) {
				newList.remove(i);
				newList.add(i, newExpr);
				setParentExpression(newExpr);
			}
		}
	}

}

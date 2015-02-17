package pl.wcislo.sbql4j.lang.parser.expression;

import java.util.List;

import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.lang.optimiser.coderewrite.deadquery.DeadQueryExpressionData;
import pl.wcislo.sbql4j.lang.parser.TreeNode;
import pl.wcislo.sbql4j.lang.pretty.QueryPretty;

public abstract class Expression extends TreeNode {
	/**
	 * Used for static analyse
	 */
	private Signature signature;
	/**
	 * position in source query code
	 */
	public final int position;
	public final int priority;
	public Expression parentExpression;
	public DeadQueryExpressionData deadQData = new DeadQueryExpressionData(this);
	public boolean shouldBeDeref = false;

	public Expression(int position, int priority) {
		this.position = position;
		this.priority = priority;
	}
	
	public Expression(int priority) {
		this(-1, priority);
	}
	
	protected void setParentExpression(Expression child) {
		if(child != null) {
			child.parentExpression = this;
		}
	}
	
	protected void setParentExpression(List<Expression> children) {
		if(children != null) {
			for(Expression child : children) {
				child.parentExpression = this;	
			}
		}
	}
	
	public abstract void replaceSubExpression(Expression oldExpr, Expression newExpr);

	public void setSignature(Signature signature) {
		signature.setAssociatedExpression(this);
		this.signature = signature;
		
	}

	public Signature getSignature() {
		return signature;
	}
	
	@Override
	public String toString() {
		return QueryPretty.printQuery(this);
	}
}
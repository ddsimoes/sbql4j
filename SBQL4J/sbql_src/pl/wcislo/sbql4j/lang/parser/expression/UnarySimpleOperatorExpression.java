package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.lang.parser.terminals.Operator;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class UnarySimpleOperatorExpression extends UnaryExpression {

	public final Expression genericExpression;
	public final Operator op;
	
	public UnarySimpleOperatorExpression(int pos, Expression ex1, Operator op) {
		super(pos, ex1, op.type.priority);
		this.ex1 = ex1;
		this.op = op;
		this.genericExpression = null;
		setParentExpression(ex1);
		setParentExpression(genericExpression);
	}
	
	public UnarySimpleOperatorExpression(Expression ex1, Operator op) {
		this(-1, ex1, op);
	}
	
	/**
	 * 
	 * @param pos
	 * @param ex1
	 * @param op
	 * @param genericExpression Operator parametryzowany (np bag<ArrayList>)
	 */
	public UnarySimpleOperatorExpression(int pos, Expression ex1, Operator op, Expression genericExpression) {
		super(pos, ex1, op.type.priority);
		this.op = op;
		this.genericExpression = genericExpression;
	}
	
	public UnarySimpleOperatorExpression(Expression ex1, Operator op, Expression genericExpression) {
		this(-1, ex1, op, genericExpression);
	}
	
//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append(op.toString());
//	
//		if(ex1 != null) {
//			sb.append(ex1.toString());
//		}
//		
//		return sb.toString();
//	}


	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitUnaryExpression(this, object);
	}

}

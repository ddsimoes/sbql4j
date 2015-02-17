package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.lang.parser.terminals.Operator;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class BinarySimpleOperatorExpression extends BinaryExpression {
	public final Operator op;
	
	public BinarySimpleOperatorExpression(int pos, Expression ex1, Expression ex2, Operator op) {
		super(pos, op.type.priority, ex1, ex2);
		this.op = op;
	}
	
	public BinarySimpleOperatorExpression(Expression ex1, Expression ex2, Operator op) {
		this(-1, ex1, ex2, op);
	}
	
//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		if(ex1 != null) {
//			sb.append(ex1.toString());
//		}
//		sb.append(" ").append(op.toString()).append(" ");
//		if(ex2 != null) {
//			sb.append(ex2.toString());
//		}
//		return sb.toString();
////		String s = this.getClass().getName()+" left: ["+ (ex1 == null ? "null" : "\n"+ex1.toString()) + "] op: "+op+" right: " +(ex2 == null ? "null" : "\n"+ex2.toString());
////		return s;
//	}

	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitBinarySimpleOperatorExpression(this, object);
	}
}

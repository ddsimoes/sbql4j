package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class LiteralExpression<E extends Comparable<E>> extends Expression {
	public final E l;
	
	public LiteralExpression(int pos, E l) {
		super(pos, ExpressionType.NAME.priority);
		this.l = l;
	}
	
	public LiteralExpression(E l) {
		this(-1, l);
	}
	
	@Override
	public final Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitLiteralExpression(this, object);
	}
	
	public void generateSimpleCode(StringBuilder sb) {
//		StringBuilder sb = gen.sb;
//		sb.append("//LiteralExpression - start\n");
		if(l instanceof String) {
			sb.append("\"").append(l.toString()).append("\"");
		} else if(l instanceof Double) {
			sb.append("Double.valueOf(").append(l.toString()).append(")");
		} else if(l instanceof Float) {
			sb.append("Float.valueOf(").append(l.toString()).append(")");
		} else if(l instanceof Long) {
			sb.append("Long.valueOf(").append(l.toString()).append(")");
		} else if(l instanceof Integer) {
			sb.append("Integer.valueOf(").append(l.toString()).append(")");
		} else if(l instanceof Short) {
			sb.append("Short.valueOf(").append(l.toString()).append(")");
		} else if(l instanceof Byte) {
			sb.append("Byte.valueOf(").append(l.toString()).append(")");
		} else if(l instanceof Boolean) {
			sb.append("Boolean.valueOf(").append(l.toString()).append(")");
		} else if(l instanceof Character) {
			sb.append("'").append(l.toString()).append("'");
		}
//		sb.append("\n");
//		sb.append("//LiteralExpression - end\n");
	}
	
	public String generateJavaCode() {
		if(l instanceof String) {
			return "\""+l.toString()+"\"";
		} else if(l instanceof Character) {	
			return "'"+l.toString()+"'";
		} else {
			return l.toString();
		}
	}
	
	@Override
	public void replaceSubExpression(Expression oldExpr, Expression newExpr) {
	}

}
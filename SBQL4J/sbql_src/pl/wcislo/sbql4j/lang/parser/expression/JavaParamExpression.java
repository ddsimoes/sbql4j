package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class JavaParamExpression extends Expression {

	private String paramName;
	private Object paramValue;
	
	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return null;
//		return visitor.visitJavaParamExpression(this, object);
	}

	public JavaParamExpression(int pos, String paramName) {
		super(pos, ExpressionType.NAME.priority);
		this.paramName = paramName;
	}
	
	public JavaParamExpression(String paramName) {
		this(-1, paramName);
	}

	public Object getParamValue() {
		return paramValue;
	}

	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamName() {
		return paramName;
	}

	@Override
	public void replaceSubExpression(Expression oldExpr, Expression newExpr) {
		
	}
	
	
}

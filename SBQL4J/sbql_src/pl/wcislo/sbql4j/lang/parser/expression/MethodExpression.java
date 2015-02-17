package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.java.model.compiletime.BindExpression;
import pl.wcislo.sbql4j.java.model.compiletime.BindResult;
import pl.wcislo.sbql4j.java.model.compiletime.StaticEVNSType;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class MethodExpression extends Expression implements BindExpression {

//	public final Expression objectExpression;
//	public final MethodExpressionSignature mSignature;
	public Expression paramsExpression;
	public final String methodName;
	
	
	public BindResult bindResult;
	
	private StaticEVNSType boundSignature;
	
	public MethodExpression(int pos, String methodName, Expression paramsExpr) {
		super(pos, ExpressionType.NAME.priority);
		this.methodName = methodName;
		this.paramsExpression = paramsExpr;
//		this.mSignature = signature;
	}
	
	public MethodExpression(String methodName, Expression paramsExpr) {
		this(-1, methodName, paramsExpr);
	}
	
	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitMethodExpression(this, object);
	}
	
//	public static class MethodExpressionSignature {
//		
//		public int pos;
//		
//		public MethodExpressionSignature(String methodName) {
//			this.methodName = methodName;
//			this.paramsExpression = null;
//		}
//
//		public MethodExpressionSignature(String methodName, Expression paramsExpression) {
//			this.paramsExpression = paramsExpression;
//			this.methodName = methodName;
//		}
//		
//		@Override
//		public String toString() {
//			if(paramsExpression != null) {
//				return "MethodExpressionSignature["+methodName+"("+paramsExpression.signature+")]";
//			} else {
//				return "MethodExpressionSignature["+methodName+"()]";
//			}
//		}
//	}
	
	@Override
	public void replaceSubExpression(Expression oldExpr, Expression newExpr) {
		if(oldExpr.equals(paramsExpression)) {
			paramsExpression = newExpr;
			setParentExpression(newExpr);
		}
	}
	
	@Override
	public StaticEVNSType getBoundSignature() {
		return boundSignature;
	}
	@Override
	public void setBoundSignature(StaticEVNSType b) {
		this.boundSignature = b;			
	}
	
	public boolean isNestedFrom(Expression candidate) {
		if(this.bindResult != null && this.bindResult.boundValueInfo != null && this.bindResult.boundValueInfo.nestedFrom != null) {
			return this.bindResult.boundValueInfo.nestedFrom.equals(candidate.getSignature());	
		} else {
			return false;
		}
		
	}

}

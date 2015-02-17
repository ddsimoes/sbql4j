package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.java.model.compiletime.BindExpression;
import pl.wcislo.sbql4j.java.model.compiletime.StaticEVNSType;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class ConstructorExpression extends Expression implements BindExpression, NestingExpression {

//	public final String className;
	/**
	 * uzywane przy pelnej nazwie klasy 
	 */
	public DotExpression classNameExpr;
	/**
	 * uzywane przy skroconej nazwie klasy 
	 */
	public final String classNameLiteral;
	public Expression paramsExpression;
	private StaticEVNSType boundSignature;
	
	
	public ConstructorExpression(int pos, DotExpression classNameExpr, Expression paramsExpression) {
		super(pos, ExpressionType.NEW.priority);
		this.classNameExpr = classNameExpr;
		this.classNameLiteral = null;
		this.paramsExpression = paramsExpression;
		setParentExpression(paramsExpression);
		setParentExpression(classNameExpr);
	}
	
	public ConstructorExpression(DotExpression classNameExpr, Expression paramsExpression) {
		this(-1, classNameExpr, paramsExpression);
	}
	
	public ConstructorExpression(int pos, String classNameLiteral, Expression paramsExpression) {
		super(pos, ExpressionType.NEW.priority);
		this.classNameExpr = null;
		this.classNameLiteral = classNameLiteral;
		this.paramsExpression = paramsExpression;
		setParentExpression(paramsExpression);
		setParentExpression(classNameExpr);
	}
	
	public ConstructorExpression(String classNameLiteral, Expression paramsExpression) {
		this(-1, classNameLiteral, paramsExpression);
	}
	
	@Override
	public void replaceSubExpression(Expression oldExpr, Expression newExpr) {
		if(oldExpr.equals(classNameExpr)) {
			classNameExpr = (DotExpression)newExpr;
			setParentExpression(newExpr);
		} else if(oldExpr.equals(paramsExpression)) {
			paramsExpression = newExpr;
			setParentExpression(newExpr);
		}
	}
	
	
	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitConstructorExpression(this, object);
	}

	public StaticEVNSType getBoundSignature() {
		return boundSignature;
	}

	public void setBoundSignature(StaticEVNSType boundSignature) {
		this.boundSignature = boundSignature;
	}
	
	private NestingExpressionHelper nestedExpressionHelper = new NestingExpressionHelper();
	@Override
	public String getNestedVarName() {
		return nestedExpressionHelper.getNestedVarName();
	}
	@Override
	public void setNestedVarName(String nestedVarName) {
		this.nestedExpressionHelper.setNestedVarName(nestedVarName);
	}
	@Override
	public String getNestedLoopVarName() {
		return nestedExpressionHelper.getNestedLoopVarName();
	}
	@Override
	public void setNestedLoopVarName(String nestedLoopVarName) {
		this.nestedExpressionHelper.setNestedLoopVarName(nestedLoopVarName);
	}
	@Override
	public int getENVSOpeningLevel() {
		return nestedExpressionHelper.getENVSOpeningLevel();
	}
	@Override
	public void setENVSOpeningLevel(int lev) {
		this.nestedExpressionHelper.setENVSOpeningLevel(lev);
	}
	@Override
	public Expression getExpression() {
		return this;
	}

}

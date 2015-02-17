package pl.wcislo.sbql4j.lang.parser.expression;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.java.model.compiletime.BindResult;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class DotExpression extends BinaryExpression implements NestingExpression {
	private List<BindResult> varToIncludeToDb4oQuery = new ArrayList<BindResult>();
	
	
	public DotExpression(int pos, Expression ex1, Expression ex2) {
		super(pos, ExpressionType.DOT.priority, ex1, ex2);
	}
	
	public DotExpression(Expression ex1, Expression ex2) {
		this(-1, ex1, ex2);
	}
	
	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitDotExpression(this, object);
	}
	
	public Expression getFirstLeftPackageExpr() {
//		IdentifierExpression res = null;
		Expression currentExpr = this;
		if(this.ex2 instanceof NameExpression) {
		
			NameExpression currentNameExpr = (NameExpression)this.ex2;
			boolean parentDot = true;
			while(parentDot) {
	//			boolean isLeftPackage = ex1 != null && ex1.getSignature() instanceof PackageSignature;
	//			if(isLeftPackage && ex1 instanceof NameExpression) {
	//				return (NameExpression) ex1;
	//			}
				parentDot = currentExpr.parentExpression != null && currentExpr.parentExpression instanceof DotExpression;
				DotExpression parentDotExpr = parentDot ? (DotExpression) currentExpr.parentExpression : null;
				if(parentDot) {
					currentExpr = ((DotExpression)currentExpr.parentExpression).ex1;
				}
				if(currentExpr instanceof NameExpression) {
					currentNameExpr = (NameExpression) currentExpr;
				}
			}
			return currentNameExpr;
		} else {
			return this.ex1;
		}
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
	public List<BindResult> getVarToIncludeToDb4oQuery() {
		return varToIncludeToDb4oQuery;
	}
	public void setVarToIncludeToDb4oQuery(
			List<BindResult> varToIncludeToDb4oQuery) {
		this.varToIncludeToDb4oQuery = varToIncludeToDb4oQuery;
	}
}

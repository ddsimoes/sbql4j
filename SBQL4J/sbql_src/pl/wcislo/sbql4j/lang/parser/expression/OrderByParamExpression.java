package pl.wcislo.sbql4j.lang.parser.expression;

import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class OrderByParamExpression extends Expression {
	public enum SortType {
		ASC,
		DESC
	}
	public final SortType sortType;
	public final Expression paramExpression;
	public final Expression comparatorExpression;
	
	
	public OrderByParamExpression(int pos, Expression paramExpression, SortType sortType, Expression comparatorExpression) {
		super(pos, ExpressionType.NAME.priority);
		this.paramExpression = paramExpression;
		this.comparatorExpression = comparatorExpression;
		if(sortType == null) {
			this.sortType = SortType.ASC;
		} else {
			this.sortType = sortType;
		}
		setParentExpression(paramExpression);
		setParentExpression(comparatorExpression);
	}
	
	public OrderByParamExpression(Expression paramExpression, SortType sortType, Expression comparatorExpression) {
		this(-1, paramExpression, sortType, comparatorExpression);
	}

	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitOrderByParamExpression(this, object);
	}
	
	@Override
	public void replaceSubExpression(Expression oldExpr, Expression newExpr) {
		
	}
}

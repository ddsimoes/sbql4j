package pl.wcislo.sbql4j.lang.tree.visitors;

import pl.wcislo.sbql4j.lang.parser.expression.AsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.BinarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.CloseByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ComaExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ConditionalExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ConstructorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.DerefExpression;
import pl.wcislo.sbql4j.lang.parser.expression.DotExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.ForEachExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ForallExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ForanyExpression;
import pl.wcislo.sbql4j.lang.parser.expression.GroupAsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.JoinExpression;
import pl.wcislo.sbql4j.lang.parser.expression.LiteralExpression;
import pl.wcislo.sbql4j.lang.parser.expression.MethodExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression;
import pl.wcislo.sbql4j.lang.parser.expression.RangeExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;

public interface TreeVisitor<R,T> {
	public Object evaluateExpression(Expression expr);
	
	/**
	 * Binary Algebraic Expression
	 * @param expr
	 * @param object
	 * @return
	 */
	public R visitBinarySimpleOperatorExpression(BinarySimpleOperatorExpression expr, T object);
//	public R visitBinaryNonAExpression(BinaryNonAExpression expr, Object object);
	public R visitUnaryExpression(UnarySimpleOperatorExpression expr, T object);
//	public R visitUnaryExpressionWithGeneric(UnaryExpression expr, Object object);
	public R visitLiteralExpression(LiteralExpression expr, T object);
	public R visitNameExpression(NameExpression expr, T object);
	public R visitWhereExpression(WhereExpression whereExpression, T object);
	public R visitDotExpression(DotExpression dotExpression, T object);
	public R visitJoinExpression(JoinExpression joinExpression, T object);
	public R visitForallExpression(ForallExpression expr, T object);
	public R visitForanyExpression(ForanyExpression expr, T object);
//	public R visitJavaParamExpression(JavaParamExpression expr, Object object);
	public R visitMethodExpression(MethodExpression expr, T object);
	public R visitOrderByExpression(OrderByExpression expr, T object);
	public R visitOrderByParamExpression(OrderByParamExpression expr, T object);
	public R visitCloseByExpression(CloseByExpression expr, T object);
	public R visitDerefExpression(DerefExpression derefExpression, T object);
	public R visitAsExpression(AsExpression expr, T object);
	public R visitGroupAsExpression(GroupAsExpression expr, T object);
	public R visitComaExpression(ComaExpression expr, T object);
	public R visitForEachExpression(ForEachExpression expr, T object);
	public R visitConstructorExpression(ConstructorExpression expr, T object);
	public R visitRangeExpression(RangeExpression expr, T object);
	public R visitConditionalExpression(ConditionalExpression expr, T object);
//	public R visitConditionalExpression(ConditionalExpression expr, T object);
	
//	public void visitBagExpression(BagExpression expr, Object object);
//	public void visitStructExpression(StructExpression expr, Object object);
}

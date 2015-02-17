package pl.wcislo.sbql4j.lang.tree.visitors;

import pl.wcislo.sbql4j.lang.optimiser.coderewrite.db4oindex.Db4oIndexInvocationExpression;
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
import pl.wcislo.sbql4j.util.NameGenerator;

/**
 * @author Emil Wcislo
 *
 * Decorates expression signatures with variable names
 * that can be used during code generation
 *
 */
public class ExpressionResultNameDecorator implements TreeVisitor {

	private NameGenerator nGen = NameGenerator.newInstance();
	
	@Override
	public Object evaluateExpression(Expression expr) {
		expr.accept(this, null);
		expr.getSignature().setResultName(nGen.genName("queryResult"));
		return null;
	}

	@Override
	public Object visitAsExpression(AsExpression expr, Object object) {
		expr.ex1.accept(this, object);
		expr.getSignature().setResultName(nGen.genName("asResult_"+expr.identifier.val));
//		expr.signature.setResultName(expr.identifier.val);
		return null;
	}

	@Override
	public Object visitBinarySimpleOperatorExpression(BinarySimpleOperatorExpression expr, Object object) {
		expr.ex1.accept(this, object);
		expr.ex2.accept(this, object);
		String prefix = expr.op.type.toString().toLowerCase()+"Result";
		expr.getSignature().setResultName(nGen.genName(prefix));
		return null;
	}

//	@Override
//	public Object visitBinaryNonAExpression(BinaryNonAExpression expr, Object object) {
//		expr.ex1.accept(this, object);
//		expr.ex2.accept(this, object);
//		String prefix = expr.op.type.toString().toLowerCase()+"Result";
//		expr.signature.setResultName(nGen.genName(prefix));
//		return null;
//	}

	@Override
	public Object visitCloseByExpression(CloseByExpression expr, Object object) {
		expr.ex1.accept(this, object);
		expr.ex2.accept(this, object);
		expr.getSignature().setResultName(nGen.genName("closeByResult"));
		expr.setNestedVarName(nGen.genName("closeByEl"));
		expr.setNestedLoopVarName(nGen.genName("closeByIndex"));
		return null;
	}

	@Override
	public Object visitComaExpression(ComaExpression expr, Object object) {
		expr.ex1.accept(this, object);
		expr.ex2.accept(this, object);
		expr.getSignature().setResultName(nGen.genName("commaResult"));
		return null;
	}

	@Override
	public Object visitConditionalExpression(ConditionalExpression expr, Object object) {
		expr.getSignature().setResultName(nGen.genName("conditionalResult"));
		expr.conditionExpr.accept(this, object);
		expr.trueExpr.accept(this, object);
		expr.falseExpr.accept(this, object);
		return object;
	}

	@Override
	public Object visitConstructorExpression(ConstructorExpression expr, Object object) {
		Expression paramsExpr = expr.paramsExpression;
		if(paramsExpr != null) {
			paramsExpr.accept(this, object);
		}
		expr.getSignature().setResultName(nGen.genName("constrResult"));
		return null;
	}

	@Override
	public Object visitDerefExpression(DerefExpression derefExpression, Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitDotExpression(DotExpression expr, Object object) {
		expr.ex1.accept(this, object);
		expr.ex2.accept(this, object);
		expr.getSignature().setResultName(nGen.genName("dotResult"));
		expr.setNestedVarName(nGen.genName("dotEl"));
		expr.setNestedLoopVarName(nGen.genName("dotIndex"));
		return null;
	}

	@Override
	public Object visitForEachExpression(ForEachExpression expr, Object object) {
//		expr.ex1.accept(this, object);
//		expr.ex2.accept(this, object);
		return null;
	}

	@Override
	public Object visitForallExpression(ForallExpression expr, Object object) {
		expr.ex1.accept(this, object);
		expr.ex2.accept(this, object);
		expr.getSignature().setResultName(nGen.genName("allResult"));
		expr.setNestedVarName(nGen.genName("allEl"));
		expr.setNestedLoopVarName(nGen.genName("allIndex"));
		return null;
	}

	@Override
	public Object visitForanyExpression(ForanyExpression expr, Object object) {
		expr.ex1.accept(this, object);
		expr.ex2.accept(this, object);
		expr.getSignature().setResultName(nGen.genName("anyResult"));
		expr.setNestedVarName(nGen.genName("anyEl"));
		expr.setNestedLoopVarName(nGen.genName("anyIndex"));
		return null;
	}

	@Override
	public Object visitGroupAsExpression(GroupAsExpression expr, Object object) {
		expr.ex1.accept(this, object);
		expr.getSignature().setResultName(nGen.genName("groupAsResult"+expr.identifier.val));
//		expr.getSignature().setResultName(nGen.genName("ident_"+expr.identifier.val+""));
		return null;
	}

	@Override
	public Object visitNameExpression(NameExpression expr, Object object) {
		if(expr.boundGetterMethodAsIdentifier) {
//			expr.getSignature().setResultName(expr.getBindResults().get(0).genJavaCode()+"()");
			expr.getSignature().setResultName(nGen.genName("identMeth_"+expr.l.val+""));
		} else {
			expr.getSignature().setResultName(nGen.genName("ident_"+expr.l.val+""));	
		}
		if(expr instanceof Db4oIndexInvocationExpression) {
			Db4oIndexInvocationExpression indexExpr = (Db4oIndexInvocationExpression) expr;
			indexExpr.getParamExpression().accept(this, object);
		}
		return null;
	}

	@Override
	public Object visitJoinExpression(JoinExpression expr, Object object) {
		expr.ex1.accept(this, object);
		expr.ex2.accept(this, object);
		expr.getSignature().setResultName(nGen.genName("joinResult"));
		expr.setNestedVarName(nGen.genName("joinEl"));
		expr.setNestedLoopVarName(nGen.genName("joinIndex"));
		return null;
	}

	@Override
	public Object visitLiteralExpression(LiteralExpression expr, Object object) {
//		expr.signature.setResultName(nGen.genName("literal_"+expr.l.toString().replace(".", "_")+""));
		expr.getSignature().setResultName(nGen.genName("literal_"));
		return null;
	}

	@Override
	public Object visitMethodExpression(MethodExpression expr, Object object) {
		Expression paramsExpr = expr.paramsExpression;
		if(paramsExpr != null) {
			paramsExpr.accept(this, object);
		}
		String prefix = "mth_"+expr.methodName+"Result";
		expr.getSignature().setResultName(nGen.genName(prefix));
		return null;
	}

	@Override
	public Object visitOrderByExpression(OrderByExpression expr, Object object) {
 		expr.ex1.accept(this, object);
		for(Expression e : expr.paramExprs) {
			e.accept(this, object);
		}
		expr.getSignature().setResultName(nGen.genName("orderByResult"));
		expr.setNestedVarName(nGen.genName("orderByEl"));
		expr.setNestedLoopVarName(nGen.genName("orderByIndex"));
		return null;
	}

	@Override
	public Object visitOrderByParamExpression(OrderByParamExpression expr, Object object) {
		String paramName = nGen.genName("orderByParam");
//		String comparatorName = nGen.genName("orderByComparator");
		expr.getSignature().setResultName(paramName);
//		expr.paramExpression.signature.setResultName(paramName);
//		expr.comparatorExpression.signature.setResultName(comparatorName);
		expr.paramExpression.accept(this, object);
		if(expr.comparatorExpression != null) {
			expr.comparatorExpression.accept(this, object);
		}
		return null;
	}

	@Override
	public Object visitRangeExpression(RangeExpression expr, Object object) {
		expr.ex1.accept(this, object);
		if(expr.ex2 != null) {
			expr.ex2.accept(this, object);
		}
		expr.getSignature().setResultName(nGen.genName("rangeResult"));
		return null;
	}

	@Override
	public Object visitUnaryExpression(UnarySimpleOperatorExpression expr, Object object) {
		expr.ex1.accept(this, object);
		String prefix = expr.op.type.toString().toLowerCase()+"Result";
		expr.getSignature().setResultName(nGen.genName(prefix));
		return null;
	}

	@Override
	public Object visitWhereExpression(WhereExpression expr, Object object) {
		expr.ex1.accept(this, object);
		expr.ex2.accept(this, object);
		expr.getSignature().setResultName(nGen.genName("whereResult"));
		expr.setNestedLoopVarName(nGen.genName("whereLoopIndex"));
		expr.setNestedVarName(nGen.genName("whereEl"));
		return null;
	}

}

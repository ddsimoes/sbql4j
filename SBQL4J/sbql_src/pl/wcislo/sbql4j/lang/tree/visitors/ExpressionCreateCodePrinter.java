package pl.wcislo.sbql4j.lang.tree.visitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pl.wcislo.sbql4j.java.model.compiletime.NestedInfo;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.ResultSource;
import pl.wcislo.sbql4j.java.utils.Pair;
import pl.wcislo.sbql4j.lang.parser.expression.AsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.BinaryExpression;
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
import pl.wcislo.sbql4j.lang.parser.expression.JoinExpression;
import pl.wcislo.sbql4j.lang.parser.expression.LiteralExpression;
import pl.wcislo.sbql4j.lang.parser.expression.MethodExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression;
import pl.wcislo.sbql4j.lang.parser.expression.RangeExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnaryExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;
import pl.wcislo.sbql4j.lang.parser.terminals.Name;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorFactory;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorType;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;

public class ExpressionCreateCodePrinter implements TreeVisitor<Void, Void> {

	public ExpressionCreateCodePrinter() {
	}
	
	private StringBuilder sb = new StringBuilder();
	private Set<Pair<String>> javaHeapNames = new HashSet<Pair<String>>();
	private List<String> topLevelClassNames = new ArrayList<String>();
	
	@Override
	public String toString() {
		return sb.toString();
	}
	
	@Override
	public Object evaluateExpression(Expression expr) {
		Object o = expr.accept(this, null);
		sb.append(";");
		return o;
	}

	@Override
	public Void visitBinarySimpleOperatorExpression(
			BinarySimpleOperatorExpression expr, Void object) {
		commonVisitBinarySimpleOperatorExpression(expr);
		return null;
	}

	@Override
	public Void visitUnaryExpression(UnarySimpleOperatorExpression expr,
			Void object) {
		commonVisitUnaryExpression(expr);
		return null;
	}

	@Override
	public Void visitLiteralExpression(LiteralExpression expr, Void object) {
		sb.append("new LiteralExpression(");
		expr.generateSimpleCode(sb);
		sb.append(")");
		return null;
	}

	@Override
	public Void visitNameExpression(NameExpression expr, Void object) {
		String name = expr.fullName != null ? expr.fullName : expr.l.val;
		sb.append("new NameExpression(new Name(\""+name+"\", 0))");
		String varName = null;
		if(expr.getSignature().getResultSource() == ResultSource.JAVA_HEAP) {
			//if parameter value was created within query 
			//(for example binder created by independent query optimisator)
			//should use proper variable name
			NestedInfo ni = expr.getBindResults().get(0).boundValueInfo;
			if(ni != null) {
				varName = ni.nestedFrom.getResultName();
			}
			if(varName == null) {
				javaHeapNames.add(new Pair<String>(expr.l.val, expr.l.val));
			} else {
				javaHeapNames.add(new Pair<String>(expr.l.val, varName));
			}
		}
		if(expr.fullName != null) {
			if(!topLevelClassNames.contains(expr.fullName)) {
				topLevelClassNames.add(expr.fullName);
			}
		}
		return null;
	}

	@Override
	public Void visitWhereExpression(WhereExpression whereExpression,
			Void object) {
		commonVisitBinaryExpression(whereExpression);
		return null;
	}

	@Override
	public Void visitDotExpression(DotExpression dotExpression, Void object) {
		commonVisitBinaryExpression(dotExpression);
		return null;
	}

	@Override
	public Void visitJoinExpression(JoinExpression joinExpression, Void object) {
		commonVisitBinaryExpression(joinExpression);
		return null;
	}

	@Override
	public Void visitForallExpression(ForallExpression expr, Void object) {
		commonVisitBinaryExpression(expr);
		return null;
	}

	@Override
	public Void visitForanyExpression(ForanyExpression expr, Void object) {
		commonVisitBinaryExpression(expr);
		return null;
	}

	@Override
	public Void visitMethodExpression(MethodExpression expr, Void object) {
		sb.append("new MethodExpression(\"");
		sb.append(expr.methodName);
		sb.append("\", ");
		if(expr.paramsExpression != null) {
			expr.paramsExpression.accept(this, null);
		}
		sb.append(")");
		return null;
	}

	@Override
	public Void visitOrderByExpression(OrderByExpression expr, Void object) {
		sb.append("new OrderByExpression(");
		expr.ex1.accept(this, null);
		sb.append(", ");
		sb.append("Arrays.asList(new OrderByParamExpression[] {");
		for(int i=0; i<expr.paramExprs.size(); i++) {
			OrderByParamExpression p = expr.paramExprs.get(i);
			p.accept(this, null);
			if(i < expr.paramExprs.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("} ");
		sb.append(") ");
		return null;
	}

	@Override
	public Void visitOrderByParamExpression(OrderByParamExpression expr,
			Void object) {
		sb.append("new OrderByParamExpression(");
		expr.paramExpression.accept(this, null);
		sb.append(", ");
		sb.append("OrderByParamExpression.SortType.").append(expr.sortType);
		sb.append(", ");
		commonAcceptExpression(expr.comparatorExpression);
		sb.append(")");
		return null;
	}

	@Override
	public Void visitCloseByExpression(CloseByExpression expr, Void object) {
		commonVisitBinaryExpression(expr);
		return null;
	}

	@Override
	public Void visitDerefExpression(DerefExpression derefExpression,
			Void object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitAsExpression(AsExpression expr, Void object) {
		sb.append("new AsExpression(");
		commonAcceptExpression(expr.ex1);
		sb.append(", ");
		sb.append("new Name(\"");
		sb.append(expr.identifier.val);
		sb.append("\")");
		sb.append(")");
		return null;
	}

	@Override
	public Void visitGroupAsExpression(GroupAsExpression expr, Void object) {
		sb.append("new GroupAsExpression(0, ");
		commonAcceptExpression(expr.ex1);
		sb.append(", ");
		sb.append("new Name(\"");
		sb.append(expr.identifier.val);
		sb.append("\")");
		sb.append(")");
		return null;
	}

	@Override
	public Void visitComaExpression(ComaExpression expr, Void object) {
		commonVisitBinaryExpression(expr);
		return null;
	}

	@Override
	public Void visitForEachExpression(ForEachExpression expr, Void object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitConstructorExpression(ConstructorExpression expr,
			Void object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitRangeExpression(RangeExpression expr, Void object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitConditionalExpression(ConditionalExpression expr,
			Void object) {
		// TODO Auto-generated method stub
		return null;
	}


	private void commonVisitBinarySimpleOperatorExpression(BinarySimpleOperatorExpression e) {
		sb.append("new "+e.getClass().getSimpleName()+"(");
		e.ex1.accept(this, null);
		sb.append(", ");
		e.ex2.accept(this, null);
		sb.append(", ");
		sb.append("OperatorFactory.getOperator(OperatorType.valueOf(\"").append(e.op.toString()).append("\"))");
		sb.append(")");
	}
	
	public static void main(String[] args) {
		OperatorType o = OperatorType.valueOf("EQUALS");
		Object o1 = OperatorFactory.getOperator(o);
		System.out.println(o1);
	}
	
	private void commonVisitBinaryExpression(BinaryExpression e) {
		sb.append("new "+e.getClass().getSimpleName()+"(");
		e.ex1.accept(this, null);
		sb.append(", ");
		e.ex2.accept(this, null);
		sb.append(")");
	}

	private void commonVisitUnaryExpression(UnaryExpression e) {
		sb.append("new "+e.getClass().getSimpleName()+"(");
		e.ex1.accept(this, null);
		sb.append(")");
	}
	private void commonAcceptExpression(Expression e) {
		if(e != null) {
			e.accept(this, null);
		} else {
			sb.append("null");
		}
	}
	
	public String genJavaHeapNamesInclude() {
		StringBuilder sb = new StringBuilder();
		if(javaHeapNames.isEmpty()) {
			sb.append("new java.util.ArrayList<pl.wcislo.sbql4j.java.utils.Pair<Object>>()");	
		} else {
			sb.append("java.util.Arrays.asList(");
	    	for(Iterator<Pair<String>> i = javaHeapNames.iterator(); i.hasNext(); ) {
	    		Pair<String> s = i.next();
	    		sb.append("new pl.wcislo.sbql4j.java.utils.Pair<Object>(");
	    		sb.append("\"").append(s.name).append("\",");
	    		sb.append(s.val);
	    		sb.append(")");
	    		if(i.hasNext()) {
	    			sb.append(", ");
	    		}
	    	}
	    	sb.append(") ");
		}
		return sb.toString();
	}
	
	public String genTopLevelClassNamesInclude() {
		StringBuilder sb = new StringBuilder();
		if(topLevelClassNames.isEmpty()) {
			sb.append("new java.util.ArrayList<String>()");	
		} else {
			sb.append("java.util.Arrays.asList(");
	    	for(Iterator<String> i = topLevelClassNames.iterator(); i.hasNext(); ) {
	    		String s = i.next();
//	    		sb.append("new pl.wcislo.sbql4j.java.utils.Pair<Object>(");
	    		sb.append("\"").append(s).append("\"");
//	    		sb.append(s.val);
//	    		sb.append(")");
	    		if(i.hasNext()) {
	    			sb.append(", ");
	    		}
	    	}
	    	sb.append(") ");
		}
		return sb.toString();
	}
}

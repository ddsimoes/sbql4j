package pl.wcislo.sbql4j.lang.pretty;

import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.RangeExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorAnd;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorAvg;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorBag;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorComma;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorCount;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorDivide;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorElementAt;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorEquals;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorExcept;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorExists;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorIn;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorInstanceof;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorIntersect;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorLess;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorLessOrEqual;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMax;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMin;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMinus;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorModulo;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMore;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMoreOrEqual;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMultiply;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorNot;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorNotEquals;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorOr;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorPlus;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorSequence;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorStruct;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorSum;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorUnion;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorUnique;
import pl.wcislo.sbql4j.lang.tree.visitors.OperatorVisitor;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class QueryOperatorPretty implements OperatorVisitor<Void, QueryPretty> {
	
	private QueryPretty parentPretty;
	
	public QueryOperatorPretty(QueryPretty parent) {
		this.parentPretty = parent;
	}

	@Override
	public Void visitAnd(OperatorAnd op, QueryPretty parent, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" and ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitAvg(OperatorAvg op, QueryPretty parent, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		print(" avg(");
		subExprs[0].accept(parentPretty, null);
//		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(")");
		return null;
	}

	@Override
	public Void visitBag(OperatorBag op, QueryPretty parent, Expression opExpr, Expression... subExprs) {
		print(" bag");
		if(opExpr instanceof UnarySimpleOperatorExpression) {
			UnarySimpleOperatorExpression ue = (UnarySimpleOperatorExpression)opExpr;
			if(ue.genericExpression != null) {
				print("<");
				ue.genericExpression.accept(parentPretty, null);
				print(">");
			}
		} else {
			print(" ");
		}
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitComma(OperatorComma op, QueryPretty parent, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(", ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitCount(OperatorCount op, QueryPretty parent, Expression opExpr, Expression... subExprs) {
		print(" count(");
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(")");
		return null;
	}

	@Override
	public Void visitDivide(OperatorDivide op, QueryPretty parent, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print("/ ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitElementAt(OperatorElementAt op, QueryPretty parent, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print("[");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		print("]");
		return null;
	}
	
	@Override
	public Void visitEquals(OperatorEquals op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" == ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitExcept(OperatorExcept op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" minus ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitExists(OperatorExists op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
		print(" exists ");
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitIn(OperatorIn op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" in ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitInstanceof(OperatorInstanceof op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" instanceof ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitIntersect(OperatorIntersect op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" intersect ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitLess(OperatorLess op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" < ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitLessOrEqual(OperatorLessOrEqual op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" <= ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitMax(OperatorMax op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
		print(" max(");
		subExprs[0].accept(parentPretty, null);
//		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(")");
		return null;
	}

	@Override
	public Void visitMin(OperatorMin op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
		print(" min(");
		subExprs[0].accept(parentPretty, null);
//		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(")");
		return null;
	}

	@Override
	public Void visitMinus(OperatorMinus op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" - ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitModulo(OperatorModulo op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" % ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitMore(OperatorMore op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" > ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitMoreOrEqual(OperatorMoreOrEqual op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" >= ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitMultiply(OperatorMultiply op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" * ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitNot(OperatorNot op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
		print("!");
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitNotEquals(OperatorNotEquals op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" != ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitOr(OperatorOr op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" || ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitPlus(OperatorPlus op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" + ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitSequence(OperatorSequence op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
		print("sequence");
		if(opExpr instanceof UnarySimpleOperatorExpression) {
			UnarySimpleOperatorExpression ue = (UnarySimpleOperatorExpression)opExpr;
			if(ue.genericExpression != null) {
				print("<");
				ue.genericExpression.accept(parentPretty, null);
				print(">");
			}
		} else {
			print(" ");
		}
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitStruct(OperatorStruct op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
		print(" struct ");
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitSum(OperatorSum op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
		print(" sum ");
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitUnion(OperatorUnion op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		print(" union ");
//		subExprs[1].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[1], opExpr, parentPretty);
		return null;
	}

	@Override
	public Void visitUnique(OperatorUnique op, QueryPretty treeVisitor, Expression opExpr, Expression... subExprs) {
		print(" unique ");
//		subExprs[0].accept(parentPretty, null);
		acceptSubexpressionWithPriority(subExprs[0], opExpr, parentPretty);
		return null;
	}	
	
	private void print(String s) {
		parentPretty.print(s);
	}
	
	private void acceptSubexpressionWithPriority(Expression subExpr, Expression parentExpr, TreeVisitor vis) {
		boolean useParenthesis = subExpr.priority < parentExpr.priority;
		if(useParenthesis) {
			print("(");
		}
		subExpr.accept(vis, null);
		if(useParenthesis) {
			print(")");
		}
	}
}

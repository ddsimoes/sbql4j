package pl.wcislo.sbql4j.lang.tree.visitors;

import pl.wcislo.sbql4j.lang.parser.expression.Expression;
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

public class OperatorVisitorAdapter implements OperatorVisitor {

	@Override
	public Object visitAnd(OperatorAnd op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		return null;
	}

	@Override
	public Object visitAvg(OperatorAvg op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitBag(OperatorBag op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitComma(OperatorComma op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitCount(OperatorCount op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitDivide(OperatorDivide op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitElementAt(OperatorElementAt op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitEquals(OperatorEquals op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExcept(OperatorExcept op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExists(OperatorExists op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitIn(OperatorIn op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitInstanceof(OperatorInstanceof op,
			TreeVisitor treeVisitor, Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitIntersect(OperatorIntersect op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitLess(OperatorLess op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitLessOrEqual(OperatorLessOrEqual op,
			TreeVisitor treeVisitor, Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitMax(OperatorMax op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitMin(OperatorMin op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitMinus(OperatorMinus op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitModulo(OperatorModulo op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitMore(OperatorMore op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitMoreOrEqual(OperatorMoreOrEqual op,
			TreeVisitor treeVisitor, Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitMultiply(OperatorMultiply op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitNot(OperatorNot op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitNotEquals(OperatorNotEquals op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitOr(OperatorOr op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitPlus(OperatorPlus op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitSequence(OperatorSequence op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStruct(OperatorStruct op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitSum(OperatorSum op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitUnion(OperatorUnion op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitUnique(OperatorUnique op, TreeVisitor treeVisitor,
			Expression opExpr, Expression... subExprs) {
		// TODO Auto-generated method stub
		return null;
	}

}

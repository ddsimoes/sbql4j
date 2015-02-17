package pl.wcislo.sbql4j.lang.optimiser.coderewrite.deadquery;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.lang.parser.expression.Expression;

/**
 * Class representing data used in analysis of dead subqueries 
 * 
 * @author Emil
 *
 */
public class DeadQueryExpressionData {
	/**
	 * An expression which own the data 
	 */
	private final Expression ownerExpression;
	/**
	 * Expressions which are resulting from owner expression
	 */
	private final List<Expression> resultingExpressions;
	
	private boolean marked;
	
	/**
	 * If resutling linker already visited this expression
	 */
	private boolean resultingLinkerVisited;
	
	
	public DeadQueryExpressionData(Expression ownerExpression) {
		this.ownerExpression = ownerExpression;
		this.resultingExpressions = new ArrayList<Expression>();
	}
	
	public Expression getOwnerExpression() {
		return ownerExpression;
	}
	public void addResultingExpression(Expression rExpr) {
		rExpr.getSignature().setResultSource(ownerExpression.getSignature().getResultSource());
		this.resultingExpressions.add(rExpr);
	}
	public List<Expression> getResultingExpressions() {
		return new ArrayList<Expression>(resultingExpressions);
	}
	public boolean isMarked() {
		return marked;
	}
	public void setMarked(boolean marked) {
		this.marked = marked;
	}

	public boolean isResultingLinkerVisited() {
		return resultingLinkerVisited;
	}

	public void setResultingLinkerVisited(boolean resultingLinkerVisited) {
		this.resultingLinkerVisited = resultingLinkerVisited;
	}
	
	
}

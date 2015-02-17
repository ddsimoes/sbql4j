package pl.wcislo.sbql4j.lang.optimiser.coderewrite.db4oindex;

import java.util.Iterator;
import java.util.List;

import pl.wcislo.sbql4j.lang.parser.expression.BinarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorFactory;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorType;

public class Db4oIndexInserter {
	private Expression queryRootExpression;
	private List<Db4oIndexFit> toReplace;
	
	public Db4oIndexInserter(Expression queryRootExpression,
			List<Db4oIndexFit> toReplace) {
		super();
		this.queryRootExpression = queryRootExpression;
		this.toReplace = toReplace;
	}
	
	public Expression getQueryRootExpression() {
		return queryRootExpression;
	}
	public List<Db4oIndexFit> getToReplace() {
		return toReplace;
	}
	
	/**
	 * @return root expression after replacement
	 */
	public Expression perform() {
		for(Db4oIndexFit entry : toReplace) {
			WhereExpression selectionExpr = entry.getSelectionExpr();
			int pos = entry.getSelectionExpr().position;
			NameExpression collectionExpr = entry.getObjectBindExpr();
			Expression indexParamExpr = entry.getPredicateParam();
			String indexedField = entry.getIndexedFieldName();
			Db4oIndexInvocationExpression indexExpr = 
				new Db4oIndexInvocationExpression(pos, collectionExpr, indexParamExpr, indexedField);
			Expression res = indexExpr;
			if(!entry.getAdditionalPredicates().isEmpty()) {
				Expression addPred = assembleAdditionalPredicates(entry.getAdditionalPredicates());
				res = new WhereExpression(indexExpr, addPred);
			}
			
			if(selectionExpr == queryRootExpression) {
				queryRootExpression = res;
			} else {
				selectionExpr.parentExpression.replaceSubExpression(selectionExpr, res);
			}
		}
		return queryRootExpression;
	}
	
	private Expression assembleAdditionalPredicates(List<Expression> addPredicates) {
		if(addPredicates.size() == 1) {
			return addPredicates.get(0);
		} else if(addPredicates.size() > 1) {
			Iterator<Expression> it = addPredicates.iterator();
			Expression leftExpr = it.next();
			while(it.hasNext()) {
				Expression rightExpr = it.next();
				BinarySimpleOperatorExpression andExpr = 
					new BinarySimpleOperatorExpression(leftExpr, rightExpr, OperatorFactory.getOperator(OperatorType.AND));
				leftExpr = andExpr;
			}
			return leftExpr;
		} else {
			return null;
		}
	}
	
}
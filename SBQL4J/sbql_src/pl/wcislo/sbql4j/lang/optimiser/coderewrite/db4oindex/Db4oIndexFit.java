package pl.wcislo.sbql4j.lang.optimiser.coderewrite.db4oindex;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;

public class Db4oIndexFit {
	private WhereExpression selectionExpr;
	private NameExpression objectBindExpr;
	private Expression predicateParam;
	private String indexedFieldName;
	
	/**
	 * Additional predicates connected with 'and' 
	 */
//	private Expression additionalPredicates;
	private List<Expression> additionalPredicates = new ArrayList<Expression>();
	
	public Db4oIndexFit(WhereExpression selectionExpr,
			NameExpression objectBindExpr, Expression predicate,
			String indexedFieldName) {
		this.selectionExpr = selectionExpr;
		this.objectBindExpr = objectBindExpr;
		this.predicateParam = predicate;
		this.indexedFieldName = indexedFieldName;
//		this.additionalPredicates = new ArrayList<Expression>();
	}
//	
//	public Db4oIndexFit(WhereExpression selectionExpr,
//			NameExpression objectBindExpr, Expression predicate,
//			String indexedFieldName, List<Expression> additionalPredicates) {
//		this.selectionExpr = selectionExpr;
//		this.objectBindExpr = objectBindExpr;
//		this.predicateParam = predicate;
//		this.indexedFieldName = indexedFieldName;
////		this.additionalPredicates = additionalPredicates;
//	}

	public WhereExpression getSelectionExpr() {
		return selectionExpr;
	}

	public NameExpression getObjectBindExpr() {
		return objectBindExpr;
	}

	public Expression getPredicateParam() {
		return predicateParam;
	}

	public String getIndexedFieldName() {
		return indexedFieldName;
	}
	public List<Expression> getAdditionalPredicates() {
		return additionalPredicates;
	}
//	public void setAdditionalPredicates(Expression additionalPredicates) {
//		this.additionalPredicates = additionalPredicates;
//	}
	
	
	
}

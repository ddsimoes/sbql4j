package pl.wcislo.sbql4j.lang.optimiser.coderewrite.db4oindex;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.ResultSource;
import pl.wcislo.sbql4j.lang.db4o.indexes.IndexManager;
import pl.wcislo.sbql4j.lang.db4o.indexes.IndexMetadataEntry;
import pl.wcislo.sbql4j.lang.db4o.indexes.PredicateFinder;
import pl.wcislo.sbql4j.lang.parser.expression.BinarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.MethodExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorEquals;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorType;
import pl.wcislo.sbql4j.lang.tree.visitors.TraversingASTAdapter;

public class Db4oIndexFitter extends TraversingASTAdapter<Void, Void> {
	private IndexManager indexManager;
	private List<Db4oIndexFit> result = new ArrayList<Db4oIndexFit>();

	public Db4oIndexFitter(IndexManager indexManager) {
		super();
		this.indexManager = indexManager;
	}

	public List<Db4oIndexFit> getResult() {
		return result;
	}

	@Override
	public Void visitWhereExpression(WhereExpression expr, Void object) {
		if (expr.ex1 instanceof NameExpression) {
			NameExpression nExpr = (NameExpression) expr.ex1;
			Signature nameSig = nExpr.getSignature();
			if (nameSig.getResultSource() == ResultSource.DB4O
					&& nExpr.fullName != null) {
				// now we're sure that bound name is db4o stored object
				// collection
				// let's find index entries for this objects collection
				List<IndexMetadataEntry> indexesForClass = indexManager
						.getIndexesForClass(nExpr.fullName);
				if (!indexesForClass.isEmpty()) {
					Db4oIndexFit res = checkWherePredicates(expr, expr.ex2,
							nExpr, indexesForClass);
					if (res != null) {
						this.result.add(res);
					}
				}
			}
		}
		return super.visitWhereExpression(expr, object);
	}

	private Db4oIndexFit checkWherePredicates(WhereExpression whereExpr,
			Expression predicates,
			NameExpression indexedObjectExpression,
			List<IndexMetadataEntry> indexesForClass) {
		// TODO only one predicate for selection is supported, should be
		// extended to support 'and'
		if (predicates instanceof BinarySimpleOperatorExpression) {
			BinarySimpleOperatorExpression predicate = (BinarySimpleOperatorExpression) predicates;
			if(predicate.op.type == OperatorType.AND) {
				System.out.println(predicate);
				Db4oIndexFit res = checkWherePredicates(whereExpr, predicate.ex1, indexedObjectExpression, indexesForClass);
				if(res != null) {
					//check if other predicated are connected with 'and'
					PredicateFinder pf = new PredicateFinder();
					pf.evaluateExpression(predicate.ex2);
					if(!pf.isFoundOr()) {
						if(pf.getAndExpr().isEmpty()) {
							res.getAdditionalPredicates().add(predicate.ex2);
						} else {
							res.getAdditionalPredicates().addAll(pf.getAndExpr());	
						}
//						res.setAdditionalPredicates(predicate.ex2);
						return res;
					}
					return null;
				} else {
					res = checkWherePredicates(whereExpr, predicate.ex2, indexedObjectExpression, indexesForClass);
					if(res != null) {
						//check if other predicated are connected with 'and'
						PredicateFinder pf = new PredicateFinder();
						pf.evaluateExpression(predicate.ex1);
						if(!pf.isFoundOr()) {
							if(pf.getAndExpr().isEmpty()) {
								res.getAdditionalPredicates().add(predicate.ex1);
							} else {
								res.getAdditionalPredicates().addAll(pf.getAndExpr());	
							}
//							res.setAdditionalPredicates(predicate.ex1);
							return res;
						}
						return null;
					}
				}
			} else if(predicate.op.type == OperatorType.EQUALS) {
				Expression fieldExpr = null;
				Expression conditionExpr = null;
				if (predicate.ex1 instanceof NameExpression 
						&& ((NameExpression)predicate.ex1).isNestedFrom(indexedObjectExpression) ) {
					fieldExpr = (NameExpression) predicate.ex1;
					conditionExpr = predicate.ex2;
				} else if (predicate.ex1 instanceof MethodExpression 
						&& ((MethodExpression)predicate.ex1).isNestedFrom(indexedObjectExpression) ) {
					fieldExpr = (MethodExpression) predicate.ex1;
					conditionExpr = predicate.ex2;
				} else if (predicate.ex2 instanceof NameExpression
						&& ((NameExpression)predicate.ex2).isNestedFrom(indexedObjectExpression) ) {
					fieldExpr = (NameExpression) predicate.ex2;
					conditionExpr = predicate.ex1;
				} else if (predicate.ex2 instanceof MethodExpression
						&& ((MethodExpression)predicate.ex2).isNestedFrom(indexedObjectExpression) ) {
					fieldExpr = (MethodExpression) predicate.ex2;
					conditionExpr = predicate.ex1;
				} else {
					return null;
				}
				if (!indexedObjectExpression.deadQData
						.getResultingExpressions().contains(conditionExpr)) {
					Db4oIndexFit res = checkEqualsPredicate(fieldExpr, conditionExpr, 
							indexesForClass, whereExpr, indexedObjectExpression);
					if(res != null) {
						return res;
					}
				}
			}
		}
		return null;
	}
	
	private Db4oIndexFit checkEqualsPredicate(Expression fieldExpr, 
			Expression conditionExpr,
			List<IndexMetadataEntry> indexesForClass,
			WhereExpression whereExpr,
			NameExpression indexedObjectExpression) {
		// one side of the predicate should be field of queried
		// objects
		
			// now we can search for appropriate index for given
			// field
		String fieldName;
		if(fieldExpr instanceof NameExpression) {
			fieldName = ((NameExpression)fieldExpr).l.val;
		} else if(fieldExpr instanceof MethodExpression) {
			String fn = ((MethodExpression)fieldExpr).methodName;
			fieldName = Character.toLowerCase(fn.charAt(3)) + fn.substring(4);
		} else {
			return null;
		}
		for (IndexMetadataEntry idxE : indexesForClass) {
//			String fieldName = fieldExpr.l.val;
			if (idxE.getFieldName().equals(fieldName)) {
				Db4oIndexFit fit = new Db4oIndexFit(whereExpr,
					indexedObjectExpression, 
					conditionExpr, 
					fieldName);
				return fit;
			}
		}
		return null;
	}
}

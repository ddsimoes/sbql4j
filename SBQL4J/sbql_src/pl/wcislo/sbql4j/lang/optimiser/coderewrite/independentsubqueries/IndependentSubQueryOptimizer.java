package pl.wcislo.sbql4j.lang.optimiser.coderewrite.independentsubqueries;

import pl.wcislo.sbql4j.lang.optimiser.QueryOptimiser;
import pl.wcislo.sbql4j.lang.parser.expression.BinaryExpression;
import pl.wcislo.sbql4j.lang.parser.expression.BinarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.DotExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.ForallExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ForanyExpression;
import pl.wcislo.sbql4j.lang.parser.expression.GroupAsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.JoinExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NestingExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;
import pl.wcislo.sbql4j.lang.parser.terminals.Name;
import pl.wcislo.sbql4j.lang.pretty.QueryPretty;
import pl.wcislo.sbql4j.lang.tree.visitors.TraversingASTAdapter;
import pl.wcislo.sbql4j.lang.tree.visitors.TypeChecker;
import pl.wcislo.sbql4j.lang.optimiser.coderewrite.independentsubqueries.IndependentSubQueryOptimizer.OptimizationKind;

/**
 * SBQLIndependentQueryOptimizer - class implementing independent query
 * optimization method (pushing and factoring)
 * 
 * @author radamus
 */
public class IndependentSubQueryOptimizer extends TraversingASTAdapter<Object, OptimizationKind> implements QueryOptimiser {

	private int nameSuffix = 0;
	// TODO - possibly we should create and work on the copy of the original
	// tree
	private Expression root;
	private String originalQuery;
	private String queryAfterOptimization;

	@Override
	public String getOriginalQuery() {
		return this.originalQuery;
	}
	
	@Override
	public String getQueryAfterOptimization() {
		return queryAfterOptimization;
	}
	
	// TODO - it should be better expressed!!
	private TypeChecker typeChecker;

	public IndependentSubQueryOptimizer(Expression rootTreeNode, TypeChecker typeChecker) {
		this.root = rootTreeNode;
		this.originalQuery = QueryPretty.printQuery(rootTreeNode);
		this.typeChecker = typeChecker;
	}

	public void reset() {
		nameSuffix = 0;
	}

	@Override
	public Expression optimise() {
		// if(ConfigDebug.ASSERTS) assert query != null: "query != null";
		root.accept(this, OptimizationKind.ANY);
		this.queryAfterOptimization = QueryPretty.printQuery(root);
		typeChecker.evaluateExpression(root);
		return root;
	}

	public Object visitDotExpression(DotExpression expr, OptimizationKind attr) {
		applyIndependentSubQueryMethod(expr, attr);

		return null;
	}

	public Object visitForAllExpression(ForallExpression expr, OptimizationKind attr) {
		applyIndependentSubQueryMethod(expr, attr);
		return null;
	}

	public Object visitForSomeExpression(ForanyExpression expr, OptimizationKind attr) {
		applyIndependentSubQueryMethod(expr, attr);
		return null;
	}

	public Object visitJoinExpression(JoinExpression expr, OptimizationKind attr) {
		applyIndependentSubQueryMethod(expr, attr);
		return null;
	}

	public Object visitOrderByExpression(OrderByExpression expr, OptimizationKind attr) {
		applyIndependentSubQueryMethod(expr, attr);
		return null;
	}

	public Object visitWhereExpression(WhereExpression expr, OptimizationKind attr) {
		applyIndependentSubQueryMethod(expr, attr);
		return null;
	}

	/**
	 * Perform optimization: 1. search for the independent query and possible
	 * optimization kind 2. rewrite the query (change the AST)
	 * 
	 * @param expr
	 *            - non-algebraic AST node - context for the optimization
	 *            process
	 * @param attr
	 *            - visitor parameter - unused
	 * @throws Exception
	 */
	private void applyIndependentSubQueryMethod(NestingExpression expr, OptimizationKind attr) {
		// if(ConfigDebug.ASSERTS) assert expr.getEnvsInfo() != null :
		// "the ENVS binding levels are not present in the AST";
		IndependentSubQuerySearcher searcher = new IndependentSubQuerySearcher(expr);
		if(expr.getExpression() instanceof BinaryExpression) {
			BinaryExpression bNestExpr = (BinaryExpression) expr.getExpression();
			Expression subExpr = (Expression) bNestExpr.ex2.accept(searcher, attr);
			while (subExpr != null) { // TODO recurrent optimization
				switch (searcher.optKind) {
				case PUSHING:
					pushing(expr, subExpr, searcher.pushPredicateType);
					break;
				case FACTORING:
					factoring(expr, subExpr);
					break;
				default:
					assert false : "unknown optimization kind " + searcher.optKind;
					break;
				}
				// determine the scope number and binding levels for the new form of
				// a whole query
				String newQuery = QueryPretty.printQuery(root);
				System.out.println(newQuery);
				typeChecker.evaluateExpression(root);
	//			root.accept(typeChecker, null);
				// apply the method to the independent sub-query
				subExpr.accept(this, attr);
				// find another sub-query of E2 independent from expr (operator Q)
				searcher = new IndependentSubQuerySearcher(expr);
				subExpr = (Expression) bNestExpr.ex2.accept(searcher, attr);
			}
			// if(subExpr != null){
			bNestExpr.ex1.accept(this, attr);
			bNestExpr.ex2.accept(this, attr);
		} else if(expr.getExpression() instanceof OrderByExpression) {
			OrderByExpression bNestExpr = (OrderByExpression) expr.getExpression();
			for(Expression ex2 : bNestExpr.paramExprs) {
				searcher = new IndependentSubQuerySearcher(expr);
				Expression subExpr = (Expression) ex2.accept(searcher, attr);
				while (subExpr != null) { // TODO recurrent optimization
					switch (searcher.optKind) {
					case PUSHING:
						pushing(expr, subExpr, searcher.pushPredicateType);
						break;
					case FACTORING:
						factoring(expr, subExpr);
						break;
					default:
						assert false : "unknown optimization kind " + searcher.optKind;
						break;
					}
					// determine the scope number and binding levels for the new form of
					// a whole query
					String newQuery = QueryPretty.printQuery(root);
					System.out.println(newQuery);
					typeChecker.evaluateExpression(root);
		//			root.accept(typeChecker, null);
					// apply the method to the independent sub-query
					subExpr.accept(this, attr);
					// find another sub-query of E2 independent from expr (operator Q)
					searcher = new IndependentSubQuerySearcher(expr);
					subExpr = (Expression) ex2.accept(searcher, attr);
				}
			}
			// if(subExpr != null){
			bNestExpr.ex1.accept(this, attr);
			for(Expression ex2 : bNestExpr.paramExprs) {
				ex2.accept(this, attr);	
			}
			
		}
		// }

	}

	/**
	 * Factor indepented subquery before operator
	 * 
	 * @param expr
	 *            node of the non-algebraic operator
	 * @param subExpr
	 *            - sub-query that should be factored out
	 */
	private void factoring(NestingExpression expr, Expression subExpr) {

		String auxname = "_aux" + nameSuffix++;
		subExpr.parentExpression.replaceSubExpression(subExpr, new NameExpression(0, new Name(auxname, 0)));

		if (expr.getExpression().parentExpression != null) {
			expr.getExpression().parentExpression.replaceSubExpression(
				expr.getExpression(), 
				new DotExpression(0, new GroupAsExpression(0, subExpr, new Name(auxname, 0)), expr.getExpression()));
		} else {
			root = new DotExpression(0, new GroupAsExpression(0, subExpr, new Name(auxname, 0)), expr.getExpression());
		}

	}

	/**
	 * Push indepented subquery before operator
	 * 
	 * @param expr
	 *            - node of the non-algebraic operator
	 * @param subExpr
	 *            - sub-query that should be pushed
	 * @param predicateType
	 *            - tells whether we push whole predicate or only a part of it
	 */
	private void pushing(NestingExpression expr, Expression subExpr, int predicateType) {
		BinaryExpression bExpr = (BinaryExpression) expr;
		switch (predicateType) {
		case IndependentSubQuerySearcher.WHOLE:
//			if (ConfigDebug.ASSERTS)
//				assert subExpr.getParentExpression() instanceof WhereExpression : "Not a whole predicate: " + subExpr.getParentExpression().getClass().getSimpleName();
//			if (ConfigDebug.ASSERTS)
//				assert subExpr.getParentExpression().getParentExpression() != null : "nonexistent parent of selection operator";
			Expression leftexpr = ((BinaryExpression)subExpr.parentExpression).ex1;
			// attach left subquery to 'where' parent
			subExpr.parentExpression.parentExpression.replaceSubExpression(subExpr.parentExpression, leftexpr);
			// now push where predicate
			bExpr.replaceSubExpression(bExpr.ex1, new WhereExpression(0, bExpr.ex1, subExpr));
			break;
		case IndependentSubQuerySearcher.PART:
//			if (ConfigDebug.ASSERTS)
//				assert (subExpr.getParentExpression() instanceof SimpleBinaryExpression) && (((SimpleBinaryExpression) subExpr.getParentExpression()).O.equals(Operator.opAnd)) : "Not a whole predicate: " + subExpr.getParentExpression().getClass().getSimpleName();
			BinarySimpleOperatorExpression andExpr = (BinarySimpleOperatorExpression) subExpr.parentExpression;
			if (andExpr.ex1.equals(subExpr)) {
				andExpr.parentExpression.replaceSubExpression(andExpr, andExpr.ex2);
			} else { // andExpr.E2.equals(subExpr)
				andExpr.parentExpression.replaceSubExpression(andExpr, andExpr.ex1);
			}
			bExpr.replaceSubExpression(bExpr.ex1, new WhereExpression(0, bExpr.ex1, subExpr));
			break;
		default:
//			if (ConfigDebug.ASSERTS)
//				assert false : "unknown pushing type";
			break;
		}

	}

	public enum OptimizationKind {
		NONE, PUSHING, FACTORING, ANY;
	}

}

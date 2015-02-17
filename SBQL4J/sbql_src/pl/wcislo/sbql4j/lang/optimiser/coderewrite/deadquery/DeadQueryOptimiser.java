package pl.wcislo.sbql4j.lang.optimiser.coderewrite.deadquery;

import pl.wcislo.sbql4j.lang.optimiser.QueryOptimiser;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.pretty.QueryPretty;
import pl.wcislo.sbql4j.lang.tree.visitors.TypeChecker;

public class DeadQueryOptimiser implements QueryOptimiser {
	private Expression rootTreeNode;
	private TypeChecker typeChecker;
	private String originalQuery;
	private String queryAfterOptimization;
	
	@Override
	public String getOriginalQuery() {
		return this.originalQuery;
	}
	
	@Override
	public String getQueryAfterOptimization() {
		return this.queryAfterOptimization;
	}
	
	public DeadQueryOptimiser(Expression rootTreeNode, TypeChecker typeChecker) {
		this.rootTreeNode = rootTreeNode;
		this.originalQuery = QueryPretty.printQuery(rootTreeNode);
		this.typeChecker = typeChecker;
	}
	
	public Expression optimise() {
		ResultingExpressionLinker rel = new ResultingExpressionLinker();
		rel.evaluateExpression(rootTreeNode);
		QueryMarker qm = new QueryMarker();
		qm.evaluateExpression(rootTreeNode);
		DeadQueryRemover dqm = new DeadQueryRemover();
		dqm.evaluateExpression(rootTreeNode);
		this.queryAfterOptimization = QueryPretty.printQuery(rootTreeNode);
		typeChecker.evaluateExpression(rootTreeNode);
		return rootTreeNode;
	}
}

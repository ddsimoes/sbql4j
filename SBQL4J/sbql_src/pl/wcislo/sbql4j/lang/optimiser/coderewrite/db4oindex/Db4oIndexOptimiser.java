package pl.wcislo.sbql4j.lang.optimiser.coderewrite.db4oindex;

import java.util.List;

import pl.wcislo.sbql4j.lang.db4o.indexes.IndexManager;
import pl.wcislo.sbql4j.lang.optimiser.QueryOptimiser;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;
import pl.wcislo.sbql4j.lang.pretty.QueryPretty;
import pl.wcislo.sbql4j.lang.tree.visitors.TypeChecker;

public class Db4oIndexOptimiser implements QueryOptimiser {
	private Expression rootTreeNode;
	private String originalQuery;
	private TypeChecker typeChecker;
	private IndexManager indexManager;
	
	private Db4oIndexFitter fitter;

	public Db4oIndexOptimiser(Expression rootTreeNode, TypeChecker typeChecker, IndexManager indexManager) {
		this.rootTreeNode = rootTreeNode;
		this.originalQuery = QueryPretty.printQuery(rootTreeNode);
		this.typeChecker = typeChecker;
		this.indexManager = indexManager;
		
		this.fitter = new Db4oIndexFitter(indexManager);
	}
	
	@Override
	public Expression optimise() {
		fitter.evaluateExpression(rootTreeNode);
		List<Db4oIndexFit> res = fitter.getResult();
		Db4oIndexInserter inserter = new Db4oIndexInserter(rootTreeNode, res);
		this.rootTreeNode = inserter.perform();
		typeChecker.evaluateExpression(rootTreeNode);
		return rootTreeNode;
	}

	@Override
	public String getOriginalQuery() {
		return originalQuery;
	}

	@Override
	public String getQueryAfterOptimization() {
		return QueryPretty.printQuery(rootTreeNode);
	}

}

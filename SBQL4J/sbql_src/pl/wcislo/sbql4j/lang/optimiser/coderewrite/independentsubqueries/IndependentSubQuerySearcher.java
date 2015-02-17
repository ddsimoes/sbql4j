package pl.wcislo.sbql4j.lang.optimiser.coderewrite.independentsubqueries;

import pl.wcislo.sbql4j.lang.optimiser.coderewrite.independentsubqueries.IndependentSubQueryOptimizer.OptimizationKind;
import pl.wcislo.sbql4j.lang.parser.expression.AsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.BinaryExpression;
import pl.wcislo.sbql4j.lang.parser.expression.BinarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.CloseByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ComaExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ConstructorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.DerefExpression;
import pl.wcislo.sbql4j.lang.parser.expression.DotExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.ForallExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ForanyExpression;
import pl.wcislo.sbql4j.lang.parser.expression.GroupAsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.JoinExpression;
import pl.wcislo.sbql4j.lang.parser.expression.LiteralExpression;
import pl.wcislo.sbql4j.lang.parser.expression.MethodExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NestingExpression;
import pl.wcislo.sbql4j.lang.parser.expression.RangeExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnaryExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorType;
import pl.wcislo.sbql4j.lang.tree.visitors.TraversingASTAdapter;


/**
 * IndependentSubQuerySearcher
 * search engine for independent query optimization method 
 * @author radamus
 * 07.04.07 TransitiveClosureExpression checking added
 * last modified 15.01.08 error fix (check if the where subexpression is a selection predicate - it could be a left sub-expression)
 */
class IndependentSubQuerySearcher extends TraversingASTAdapter<Expression, OptimizationKind> {
	private NestingExpression context;
	OptimizationKind optKind = OptimizationKind.NONE;
	int pushPredicateType = 0;
//	Expression independentSubQuery = null;
	

	/**
	 * @param context - the non-algebraic operator AST node 
	 * - context for the independency searcher
	 */
	public IndependentSubQuerySearcher(NestingExpression context) {
		assert !(context instanceof CloseByExpression) : "TransitiveClosureExpression canot be optimzed";
		this.context = context;
		
	}
	
//	@Override
//	public Expression visitAsExpression(AsExpression expr, Object attr) {
//		return unaryCheck(expr, attr);
//	}

	@Override
	public Expression visitComaExpression(ComaExpression expr, OptimizationKind attr) {
		return binaryCheck(expr, attr);
	}

	@Override
	public Expression visitDerefExpression(DerefExpression derefExpression, OptimizationKind object) {
		return super.visitDerefExpression(derefExpression, object);
	}

	@Override
	public Expression visitRangeExpression(RangeExpression expr, OptimizationKind object) {
		return super.visitRangeExpression(expr, object);
	}

	@Override
	public Expression visitDotExpression(DotExpression expr, OptimizationKind attr) {
		return binaryCheck(expr, attr);
	}

	@Override
	public Expression visitForallExpression(ForallExpression expr, OptimizationKind attr) {
		return binaryCheck(expr, attr);
	}

	@Override
	public Expression visitForanyExpression(ForanyExpression expr, OptimizationKind attr) {
		return binaryCheck(expr, attr);
	}

	@Override
	public Expression visitGroupAsExpression(GroupAsExpression expr, OptimizationKind attr) {
		return unaryCheck(expr, attr);
	}

	@Override
	public Expression visitWhereExpression(WhereExpression expr, OptimizationKind attr) {
		return binaryCheck(expr, attr);
	}
	
	@Override
	public Expression visitBinarySimpleOperatorExpression(BinarySimpleOperatorExpression expr, OptimizationKind object) {
		return binaryCheck(expr, object);
	}
	@Override
	public Expression visitUnaryExpression(UnarySimpleOperatorExpression expr, OptimizationKind attr) {
		return unaryCheck(expr, attr);
	}
	@Override
	public Expression visitJoinExpression(JoinExpression expr, OptimizationKind object) {
		return binaryCheck(expr, object);
	}
	@Override
	public Expression visitAsExpression(AsExpression expr, OptimizationKind object) {
		return unaryCheck(expr, object);
	}
	
	@Override
	public Expression visitMethodExpression(MethodExpression expr, OptimizationKind attr) {
		if(expr.paramsExpression != null) {
			return unaryCheck(expr, expr.paramsExpression, attr);
		} else {
			return super.visitMethodExpression(expr, attr);
		}
	}
	
	@Override
	public Expression visitConstructorExpression(ConstructorExpression expr, OptimizationKind attr) {
		if(expr.paramsExpression != null) {
			return unaryCheck(expr, expr.paramsExpression, attr);
		} else {
			return super.visitConstructorExpression(expr, attr);
		}
	}


	/**
	 * Returns the AST node representing direct non-algebraic operator for the expr
	 * @param expr
	 * @return the AST node representing direct non-algebraic operator for the expr
	 */
	private NestingExpression directNonAlgebraicOperator(Expression expr){
		Expression e = expr.parentExpression;
		while(e != null && !(e instanceof NestingExpression)){
			e = e.parentExpression;
		}
		return (NestingExpression)e;
	}
	
	/** Algorithim main method
	 * @param expr - expression (sub-query) that should be checked
	 * @param reqKind - what kind of optimization do we search
	 * for the possibility of pushing or factoring out
	 * @return
	 * @throws Exception
	 */
	private boolean canMethodBeApplied(Expression expr, OptimizationKind reqKind) {
		NestingExpression nexpr = directNonAlgebraicOperator(expr);
		
		//we cannot optimize TransitiveClosureExpressions
		while(nexpr instanceof CloseByExpression){
			nexpr = directNonAlgebraicOperator(nexpr.getExpression());
		}
		
		boolean distributive = isDistributive(context.getExpression());
		
		//if subquery (expr) is independent of all non-algebraic operators that pushes 
		//their scopes, being bottom scopes for subquery, onto the scope opened by 'context'
		//if there are any, if there are none the condition evaluates to TRUE
		while(nexpr != context){
			IndependencyChecker ind = new IndependencyChecker(nexpr);
			expr.accept(ind, null);
			if(!ind.isIndependent)
				return false;
			if(distributive && !isDistributive(nexpr.getExpression())) distributive = false; 
			nexpr = directNonAlgebraicOperator(nexpr.getExpression());
		}
		
		/*
		 * if direct non-algebraic operator is where AND all non-algebraic operators up to 'context'
		 * are distributive AND ('expr' is whole predicate OR 'expr' is predicate part connected with 'and')
		 */ 
		if( 	(reqKind == OptimizationKind.ANY || reqKind == OptimizationKind.PUSHING)  
			&&	distributive
			&& (directNonAlgebraicOperator(expr) != context) //RA modification
			&& (directNonAlgebraicOperator(expr) instanceof WhereExpression)
			&&(isWholeSelectionPredicate(expr) || isProperPartOfSelectionPredicate(expr))){
			optKind = OptimizationKind.PUSHING;
			return true;
		}else if((reqKind == OptimizationKind.ANY || reqKind == OptimizationKind.FACTORING)) { //expr cannot be pushed, check whether it can be factored out 
			IndependencyChecker ind = new IndependencyChecker(context);
			expr.accept(ind, null);
			if(ind.isIndependent){
				optKind = OptimizationKind.FACTORING;
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * Check if the operator is distributive
	 * @param expr - AST node representing the operator
	 * @return true if distributive, false otherwise
	 */
	private boolean isDistributive(Expression expr){
		if((expr instanceof JoinExpression) 
				|| (expr instanceof DotExpression) 
				|| (expr instanceof WhereExpression)
				|| (expr instanceof BinarySimpleOperatorExpression && 
						((BinarySimpleOperatorExpression)expr).op.type == OperatorType.INTERSECT)
				|| (expr instanceof ComaExpression)){
			return true;
		}
		return false;
	}
	
	/** 
	 * Check if the expr represents the whole selection predicate  
	 * @param expr
	 * @return true if whole, false otherwise
	 */
	private boolean isWholeSelectionPredicate(Expression expr){
		if(expr.parentExpression instanceof WhereExpression && 
				((WhereExpression)expr.parentExpression).ex1 != expr){
			this.pushPredicateType = WHOLE;
			return true;
		}
		return false;
	}
	
	/**
	 * Check if the expr represents part of the selection predicate that can be pushed
	 * (connected with the remaining with use of logical AND)
	 * @param expr
	 * @return true if suitable, false otherwise
	 */
	private boolean isProperPartOfSelectionPredicate(Expression expr){
		if((expr.parentExpression instanceof BinarySimpleOperatorExpression) && 
				(((BinarySimpleOperatorExpression)expr.parentExpression).op.type == OperatorType.AND)) {
			this.pushPredicateType = PART;
			return true;
		}
		return false;
	}
	
	
	private Expression unaryCheck(UnaryExpression expr, OptimizationKind attr) {
//		if(this.canMethodBeApplied(expr, (OptimizationKind)attr)) {
//			return expr;
//		}
//		return (Expression)expr.ex1.accept(this, attr);
		return unaryCheck(expr, expr.ex1, attr);
	}
	
	private Expression unaryCheck(Expression parentExpr, Expression child, OptimizationKind attr) {
		if(this.canMethodBeApplied(parentExpr, (OptimizationKind)attr)) {
			return parentExpr;
		}
		return (Expression)child.accept(this, attr);
	}
	
	private Expression binaryCheck(BinaryExpression expr, OptimizationKind attr) {
//		if(this.canMethodBeApplied(expr, (OptimizationKind)attr)){
//			return expr;
//		}
//		Expression e = (Expression)expr.ex1.accept(this, attr);
//		return (Expression)(e != null ? e :  expr.ex2.accept(this, attr));
		return binaryCheck(expr, expr.ex1, expr.ex2, attr);
	}
	
	private Expression binaryCheck(Expression parentExpr, Expression leftChild, 
			Expression rightChild, OptimizationKind attr) {
		if(this.canMethodBeApplied(parentExpr, attr)){
			return parentExpr;
		}
		Expression e = (Expression)leftChild.accept(this, attr);
		return (Expression)(e != null ? e :  rightChild.accept(this, attr));
		
	}
	
	private boolean isLiteralExpression(Expression expr){ 
		if(expr instanceof LiteralExpression) {
			return true;
		} else {
			return false;
		}
	}
	static final int WHOLE = 1;
	static final int PART = 2;

}

/**
 * 
 */
package pl.wcislo.sbql4j.lang.optimiser.coderewrite.independentsubqueries;

import pl.wcislo.sbql4j.lang.parser.expression.MethodExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NestingExpression;
import pl.wcislo.sbql4j.lang.tree.visitors.TraversingASTAdapter;

/**
 * IndependencyChecker 
 * check if the name is indepentend from the non-algebraic operator
 * (context).
 * @author radamus
 *last modified: 2006-11-30
 */
public class IndependencyChecker extends TraversingASTAdapter {
	boolean isIndependent;
	private NestingExpression context; //non algebraic operator for which we check
											//whether the name is independent

	/**
	 * @param context - non-algebraic operator - the context for independency 
	 * checking process
	 */
	public IndependencyChecker(NestingExpression context) {
		this.context = context;
		isIndependent = true;
	}
	
	public Object visitNameExpression(NameExpression expr, Object attr) {

		//if name was bound in the environment created by the 'context' non-algebraic operator
		//the name is not independent
		int envsOpeningLev = context.getENVSOpeningLevel();
		int envsLevBoundAt = expr.getBindResults().get(0).envsStackLevel;
		if (envsLevBoundAt == envsOpeningLev) {
			isIndependent = false;
		}

		return null;

	}
	
	@Override
	public Object visitMethodExpression(MethodExpression expr, Object object) {
		if (expr.bindResult.envsStackLevel == context.getENVSOpeningLevel()) {
			isIndependent = false;
		}
		return null;
	}

	public boolean isIndependent()
	{
		return isIndependent;
	}
}

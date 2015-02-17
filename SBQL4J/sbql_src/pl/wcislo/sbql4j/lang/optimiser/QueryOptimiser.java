package pl.wcislo.sbql4j.lang.optimiser;

import pl.wcislo.sbql4j.lang.parser.expression.Expression;

public interface QueryOptimiser {
	public Expression optimise();
	public String getOriginalQuery();
	public String getQueryAfterOptimization();
}

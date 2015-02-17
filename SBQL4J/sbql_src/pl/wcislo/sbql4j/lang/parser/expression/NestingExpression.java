package pl.wcislo.sbql4j.lang.parser.expression;


/**
 * @author Emil Wcislo
 * 
 */
public interface NestingExpression {
	
//	public NestingExpression(int pos, int priority) {
//		super(pos, priority);
//	}
	
	/**
	 * A name of variable for that nested operation is performed. 
	 */
	public String getNestedVarName();
	public void setNestedVarName(String nestedVarName);

	/**
	 * A name of variable that is used as loop variable for nested elements 
	 */
	public String getNestedLoopVarName();
	public void setNestedLoopVarName(String nestedLoopVarName);
	
	/**
	 * A level of ENVS stack, which is opened by the expression (indexed from 0 for root level)
	 */
	public int getENVSOpeningLevel();
	public void setENVSOpeningLevel(int lev);
	
	public Expression getExpression();
	
}

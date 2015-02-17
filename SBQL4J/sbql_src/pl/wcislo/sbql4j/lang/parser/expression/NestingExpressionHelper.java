package pl.wcislo.sbql4j.lang.parser.expression;

public class NestingExpressionHelper {
	private String nestedVarName;
	private String nestedLoopVarName;
	private int envsOpeningLevel;
	
	public String getNestedVarName() {
		return nestedVarName;
	}
	public void setNestedVarName(String nestedVarName) {
		this.nestedVarName = nestedVarName;
	}
	public String getNestedLoopVarName() {
		return nestedLoopVarName;
	}
	public void setNestedLoopVarName(String nestedLoopVarName) {
		this.nestedLoopVarName = nestedLoopVarName;
	}
	public int getENVSOpeningLevel() {
		return this.envsOpeningLevel;
	}
	public void setENVSOpeningLevel(int lev) {
		this.envsOpeningLevel = lev;
	}
}

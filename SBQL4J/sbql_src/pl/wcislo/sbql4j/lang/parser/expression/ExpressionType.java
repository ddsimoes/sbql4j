package pl.wcislo.sbql4j.lang.parser.expression;

public enum ExpressionType {
	AS(150),
	GROUP_AS(150),
	COMMA(90),
	CONDITIONAL(80),
	DEREF(10),
	FOREACH(10),
	ORDER_BY(70),
	CLOSE_BY(70),
	NEW(20),
	DOT(190),
	FORALL(40),
	FORANY(40),
	NAME(1000),
	JOIN(30),
	RANGE(180),
	WHERE(30)
	;
	
	public final int priority;
	private ExpressionType(int priority) {
		this.priority = priority;
	}
}

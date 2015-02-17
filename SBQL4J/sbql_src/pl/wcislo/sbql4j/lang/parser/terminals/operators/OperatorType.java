package pl.wcislo.sbql4j.lang.parser.terminals.operators;

import pl.wcislo.sbql4j.lang.parser.terminals.Operator;

public enum OperatorType {
	PLUS("\\+", OperatorPlus.class, 2, 130),
	MINUS("\\-", OperatorMinus.class, 2, 130),
	MULTIPLY("\\*", OperatorMultiply.class, 2, 140),
	DIVIDE("\\/", OperatorDivide.class, 2, 140),
	MODULO("\\%", OperatorModulo.class, 2, 140),
	EQUALS("==", OperatorEquals.class, 2, 120),
	NOT_EQUALS("!=", OperatorNotEquals.class, 2, 120),
	MORE(">", OperatorMore.class, 2, 120),
	OR("OR|or|\\|\\|", OperatorOr.class, 2, 110),
	AND("AND|and|\\&\\&", OperatorAnd.class, 2, 110),
	UNION("UNION|union", OperatorUnion.class, 2, 60),
	SUM("SUM|sum", OperatorSum.class, 1, 160),
	COUNT("COUNT|count", OperatorCount.class, 1, 160),
	AVG("AVG|avg", OperatorAvg.class, 1, 160),
	UNIQUE("UNIQUE|unique", OperatorUnique.class, 1, 160),
	LESS("<| < ", OperatorLess.class, 2, 120),
	MORE_OR_EQUAL(">=", OperatorMoreOrEqual.class, 2, 120),
	LESS_OR_EQUAL("<=", OperatorLessOrEqual.class, 2, 120),
	MIN("MIN|min", OperatorMin.class, 1, 160),
	MAX("MAX|max", OperatorMax.class, 1, 160),
	COMA("\\,", OperatorComma.class, 2, 90),
	IN("IN|in", OperatorIn.class, 2, 60),
	EXISTS("EXISTS|exists", OperatorExists.class, 1, 160),
	NOT("NOT|not", OperatorNot.class, 1, 50),
	EXCEPT("MINUS|minus|EXCEPT|except", OperatorExcept.class, 2, 160),
	INTERSECT("INTERSECT|intersect", OperatorIntersect.class, 2, 160),
	STRUCT("STRUCT|struct", OperatorStruct.class, 1, 50),
	BAG("BAG|bag", OperatorBag.class, 1, 50),
	SEQUENCE("SEQUENCE|sequence", OperatorSequence.class, 1, 50),
	ELEMENT_AT("\\[", OperatorElementAt.class, 2, 170),
	INSTANCEOF("INSTANCEOF|instanceof", OperatorInstanceof.class, 2, 160);
		
	public final String val;
	public final Class<? extends Operator> clazz;
	public final int argumentCount;
	public final int priority;
	private OperatorType(String val, Class<? extends Operator> clazz, 
			int argumentCount, int priority) {
		this.val = val;
		this.clazz = clazz;
		this.argumentCount = argumentCount;
		this.priority = priority;
	}
}

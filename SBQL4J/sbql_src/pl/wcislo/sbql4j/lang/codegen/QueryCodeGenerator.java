package pl.wcislo.sbql4j.lang.codegen;

import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public abstract class QueryCodeGenerator<R,T> implements TreeVisitor<R, T> {
	public abstract StringBuilder getAppender();
	public abstract void printExpressionTrace(String trace, Expression e);
	public abstract String generateIdentifier(String prefix);
}

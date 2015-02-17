package pl.wcislo.sbql4j.lang.db4o.codegen.interpreter;

import java.util.List;

import pl.wcislo.sbql4j.lang.codegen.CodeGenerator;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4o;
import pl.wcislo.sbql4j.lang.parser.expression.DotExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.tree.visitors.ExpressionCreateCodePrinter;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCImport;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCSbqlExpression;

public class CodeGenDb4oInterpreter extends CodeGeneratorDb4o {

	public CodeGenDb4oInterpreter(CodeGenerator parentCodeGen) {
		super(parentCodeGen);
	}
	
	private int generatedQueries = 0;

	@Override
	public String generateQueriesClass(int i, int j, List<JCImport> imports) {
		//no query class is generated in interpreter mode		
		return null;
	}

	@Override
	public String generateQueryInvocation(int i, int j, Expression tree) {
    	StringBuilder sb = new StringBuilder();
    	DotExpression dotExpression = (DotExpression)tree;
    	String dbContainerIdent = dotExpression.ex1.getSignature().getResultName();
		String identQueryExpr = genIdent("queryExpr"); 
		ExpressionCreateCodePrinter ePrint = new ExpressionCreateCodePrinter();
		ePrint.evaluateExpression(dotExpression.ex2);
		String declQueryExpr = ePrint.toString();
    	sb.append("Expression ").append(identQueryExpr).append(" = ").append(declQueryExpr);
		String identQueryObject = genIdent("db4oQuery");
		String dotResTypeString = dotExpression.getSignature().getJavaTypeString();
		String dotResDecl = dotExpression.getSignature().genJavaDeclarationCode();
		sb
		.append("Db4oSBQLQuery<")
		.append(dotResTypeString)
		.append(">")
		.append(identQueryObject)
		.append(" = new Db4oSBQLInterpretedQuery<")
		.append(dotResTypeString)
		.append(">(")
		.append(identQueryExpr)
		.append(",")
		.append(ePrint.genJavaHeapNamesInclude())
		.append(",")
		.append(ePrint.genTopLevelClassNamesInclude())
		.append(");\n");
		sb
		.append(dotResDecl)
		.append("=")
		.append(dbContainerIdent)
		.append(".query(")
		.append(identQueryObject)
		.append(");\n");
		generatedQueries++;
		return sb.toString();
	}
	
	@Override
	public int getGeneratedQueriesCount() {
		return generatedQueries;
	}
	
	@Override
	public boolean isGeneratingQueryClasses() {
		return false;
	}

}

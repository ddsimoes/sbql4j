package pl.wcislo.sbql4j.lang.codegen.interpreter;

import java.util.List;

import pl.wcislo.sbql4j.lang.codegen.CodeGenerator;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4oFactory.CodeGenTypeDB4O;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCImport;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCSbqlExpression;

public class CodeGenInterpreter extends CodeGenerator {
	
	public CodeGenInterpreter(CodeGenTypeDB4O db4oCodeGenType) {
		super(db4oCodeGenType);
	}

	@Override
	public String generateQueriesClass(int i, List<JCImport> imports) {
		//no query class is generated in interpreter mode		
		return null;
	}

	@Override
	public String generateQueryInvocation(JCSbqlExpression tree) {
    	StringBuilder sb = new StringBuilder();
    	if(tree.resultIsAssigned) {
    		sb.append("(").append(tree.exprTree.getSignature().getJavaTypeString()).append(")");
    	}
    	sb.append("new pl.wcislo.sbql4j.java.utils.JavaStatement(");
    	String expr = tree.expr.replace("\"", "\\\"");
    	expr = expr.replaceAll("\n", "");
    	expr = expr.replaceAll("\r", "\"+\r\"");
    	sb.append("\"").append(expr).append("\", ");
    	
    	
    	if(tree.localVarContext.size() > 0) {
    		sb.append("java.util.Arrays.asList(");
        	for(int i=0; i<tree.localVarContext.size(); i++) {
        		Symbol s = tree.localVarContext.get(i);
        		
        		
        		sb.append("new pl.wcislo.sbql4j.java.utils.Pair<Object>(");
        		sb.append("\"").append(s.name.toString()).append("\",");
        		sb.append(s.name.toString());
        		sb.append(")");
        		if(i<tree.localVarContext.size()-1) {
        			sb.append(", ");
        		}
        	}
        	sb.append(") ");
    	} else {
    		sb.append("new java.util.ArrayList<pl.wcislo.sbql4j.java.utils.Pair<Object>>()");
    	}
    	sb.append(", ");
    	if(tree.localClassContext.size() > 0) {
        	sb.append("java.util.Arrays.asList(");
        	
        	for(int i=0; i<tree.localClassContext.size(); i++) {
        		Symbol s = tree.localClassContext.get(i);
        		sb.append("new pl.wcislo.sbql4j.java.utils.Pair<Class>(");
        		sb.append("\"").append(s.name.toString().replace("$", ".")).append("\",");
        		sb.append(s.name.toString().replace("$", ".")).append(".class");
        		sb.append(")");
        		if(i<tree.localClassContext.size()-1) {
        			sb.append(", ");
        		}
        	} 
        	sb.append(")");
    	} else {
    		sb.append("new java.util.ArrayList<pl.wcislo.sbql4j.java.utils.Pair<Class>>()");
    	}
    	sb.append(", ");
    	if(tree.localPackageContext.size() > 0) {
    		sb.append("java.util.Arrays.asList(");
    		for(int i=0; i<tree.localPackageContext.size(); i++) {
        		Symbol s = tree.localPackageContext.get(i);
        		sb.append("\"").append(s.name.toString()).append("\"");
        		if(i<tree.localPackageContext.size()-1) {
        			sb.append(", ");
        		}
    		}
    		sb.append(")");
    	} else {
    		sb.append("new java.util.ArrayList<String>()");
    	}
    	
    	sb.append(").execute()");
		return sb.toString();
	}
	
	@Override
	public int getGeneratedQueriesCount() {
		return 0;
	}
	@Override
	public int getCurrentQueryIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

}

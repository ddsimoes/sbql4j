package pl.wcislo.sbql4j.lang.codegen.simple;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.lang.codegen.CodeGenerator;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4oFactory.CodeGenTypeDB4O;
import pl.wcislo.sbql4j.tools.javac.code.Symbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.ClassSymbol;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCImport;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCSbqlExpression;

public class CodeGenSimple extends CodeGenerator {

	private List<JCSbqlExpression> qTrees = new ArrayList<JCSbqlExpression>();
	private StringBuilder sb;
	
	public CodeGenSimple(CodeGenTypeDB4O db4oCodeGenType) {
		super(db4oCodeGenType);
	}

	@Override
	public String generateQueriesClass(int queryIndex, List<JCImport> imports) {
		JCSbqlExpression tree = null;
		try {
			tree = qTrees.get(queryIndex);
		} catch(Exception e) {
		}
		if(tree == null) return null;
		
		sb = new StringBuilder();
		sb.append("package ").append(getQueriesClassPackage()).append(";").append("\n");
		sb.append("\n");
		sb.append("import java.util.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.exception.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.java.model.runtime.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.java.model.runtime.factory.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.java.utils.Pair;").append("\n");
		sb.append("import pl.wcislo.sbql4j.lang.codegen.simple.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.lang.types.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.util.Utils;").append("\n");
		sb.append("import pl.wcislo.sbql4j.model.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.model.collections.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.util.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.xml.model.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.xml.parser.store.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.lang.parser.terminals.operators.*;");
		sb.append("import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression.SortType;");
		sb.append("import org.apache.commons.collections.CollectionUtils;");
		
		for(ClassSymbol cs : tree.localClassContext) {
			sb.append("import ").append(cs.fullname).append(";").append("\n");
		}
		for(JCImport im : imports) {
			sb.append(im.toString()).append("\n");
		}
				
		
		sb.append("\n");
		sb.append("public class ").append(getQueriesClassName()).append(queryIndex).append(" extends BaseSimpleQueryExecutor {").append("\n");
		sb.append("    public ").append(getQueriesClassName()).append(queryIndex).append("(").append("\n");
		sb.append("        List<Pair<Object>> varList,").append("\n");
		sb.append("        List<Pair<Class>> staticList,").append("\n");
		sb.append("        List<String> usedPackages) {").append("\n");
		sb.append("            super(varList, staticList, usedPackages);").append("\n");
		sb.append("    }").append("\n");
		sb.append("\n");
		sb.append(" protected Stack<QueryResult> qres = new Stack<QueryResult>(); \n");		
		QueryCodeGenSimple queryPretty = new QueryCodeGenSimple(this);
//		for(int i=0; i<qTrees.size(); i++) {
//			JCSbqlExpression expr = qTrees.get(i);
			sb.append("/** query='").append(tree.expr.trim()).append("' \n");
			sb.append("*/");
//			sb.append("        public ").append(expr.exprTree.signature.getJavaTypeString()).append(" executeQuery").append(i).append("() {").append("\n");
			sb.append("        public ").append(tree.exprTree.getSignature().getJavaTypeString()).append(" executeQuery").append("() {").append("\n");
			sb.append(queryPretty.evaluateExpression(tree.exprTree));
			sb.append("        }");
//		}
		sb.append("}").append("\n");
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public String generateQueryInvocation(JCSbqlExpression tree) {
		qTrees.add(tree);
    	StringBuilder sb = new StringBuilder();
    	
//    	if(tree.resultIsAssigned) {
//    		sb.append("(").append(returnTypeToString(tree.resultType)).append(")");
//    	}
    	
       	sb.append("new ").append(getQueriesClassName()+(qTrees.size()-1)).append("(");
//    	String expr = tree.expr.replace("\"", "\\\"");
//    	expr = expr.replaceAll("\n", "");
//    	expr = expr.replaceAll("\r", "\"+\r\"");
//    	sb.append("\"").append(expr).append("\", ");
    	
    	
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
    	
//    	sb.append(").executeQuery").append(qTrees.size()-1).append("()");
    	sb.append(").executeQuery").append("()");
		return sb.toString();
	}
	
	@Override
	public int getGeneratedQueriesCount() {
		return qTrees.size();
	}
	@Override
	public int getCurrentQueryIndex() {
		return qTrees.size()-1;
	}
	

}

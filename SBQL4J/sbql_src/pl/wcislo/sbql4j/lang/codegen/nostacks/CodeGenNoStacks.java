package pl.wcislo.sbql4j.lang.codegen.nostacks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.lang.codegen.CodeGenerator;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4oFactory.CodeGenTypeDB4O;
import pl.wcislo.sbql4j.lang.pretty.QueryPretty;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.ClassSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.VarSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.Type.ArrayType;
import pl.wcislo.sbql4j.tools.javac.code.Type.ClassType;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCImport;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCSbqlExpression;

public class CodeGenNoStacks extends CodeGenerator {

	private List<JCSbqlExpression> qTrees = new ArrayList<JCSbqlExpression>();
	private StringBuilder sb;
	
	public CodeGenNoStacks(CodeGenTypeDB4O db4oCodeGenType) {
		super(db4oCodeGenType);
	}

	@Override
	public String generateQueriesClass(int queryIndex, List<JCImport> imports) {
		JCSbqlExpression tree = null;
		try {
			tree = qTrees.get(queryIndex);
		} catch(Exception e) {
		}
		if(tree == null) {
			return null;
		}
		sb = new StringBuilder();
		sb.append("package ").append(getQueriesClassPackage()).append(";").append("\n");
		sb.append("\n");
		sb.append("import java.util.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.exception.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.java.model.runtime.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.java.model.runtime.factory.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.java.utils.ArrayUtils;").append("\n");
		sb.append("import pl.wcislo.sbql4j.java.utils.OperatorUtils;").append("\n");
		sb.append("import pl.wcislo.sbql4j.java.utils.Pair;").append("\n");
		sb.append("import pl.wcislo.sbql4j.lang.codegen.simple.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.lang.codegen.nostacks.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.lang.types.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.lang.db4o.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.lang.xml.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.lang.db4o.codegen.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.lang.db4o.codegen.interpreter.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.lang.db4o.codegen.nostacks.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.util.Utils;").append("\n");
		sb.append("import pl.wcislo.sbql4j.model.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.model.collections.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.util.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.xml.model.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.xml.parser.store.*;").append("\n");
		sb.append("import pl.wcislo.sbql4j.lang.parser.terminals.operators.*;").append("\n");;
		sb.append("import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression.SortType;").append("\n");;
		sb.append("import org.apache.commons.collections.CollectionUtils;").append("\n");;
		sb.append("import pl.wcislo.sbql4j.lang.parser.expression.*;").append("\n");;
		sb.append("import pl.wcislo.sbql4j.lang.parser.terminals.*;").append("\n");;
		sb.append("import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;").append("\n");;
		
		for(ClassSymbol cs : tree.localClassContext) {
			sb.append("import ").append(cs.fullname).append(";").append("\n");
		}
		for(JCImport im : imports) {
			sb.append(im.toString()).append("\n");
		}
		sb.append("\n");
		sb.append("public class ").append(getQueriesClassName()).append(queryIndex).append(" { \n"); // extends PureJavaQuery
//		.append(" extends BaseSimpleQueryExecutor {")
		//Declare query params as class fields
		for(VarSymbol field : tree.localVarContext) {
			//types are changed to proprer query types - ie. int[] to List<Integer>, Product[] to List<Product>
			sb.append("private ").append(getParamType(field.type)).append(" "+field.name.toString()).append("; \n");
		}
		
		sb.append("    public ").append(getQueriesClassName()).append(queryIndex).append("(").append("\n");
		//All query params as constructor params
		for(Iterator<VarSymbol> fieldIt = tree.localVarContext.iterator(); fieldIt.hasNext(); ) {
			VarSymbol field = fieldIt.next();

			sb.append("final "+field.type.toString()).append(" "+field.name.toString());
			if(fieldIt.hasNext()) {
				sb.append(",");
			}
			sb.append("\n");
		}
		sb.append(") {").append("\n");
//		sb.append("            super(varList, staticList, usedPackages);").append("\n");
		//Assign query params to class fields
		for(VarSymbol field : tree.localVarContext) {
			sb.append("this.").append(field.name.toString()).append(" = ");
			//if param is array, it should be converted to list of proper type 
			//(primitive types are converted to wrapper classes - int to Integer...)
			if(field.type instanceof ArrayType) {
				sb.append("ArrayUtils.toList("+field.name.toString()).append("); \n");
			} else {
				sb.append(field.name.toString()).append("; \n");
			}
			
		}
		sb.append("    }").append("\n");
		sb.append("\n");
		
		QueryCodeGenNoStacks queryPretty = new QueryCodeGenNoStacks(this, getDb4oCodeGenType(), queryIndex);
		sb.append("/**\n * original query='").append(tree.expr.trim()).append("' \n *");
		sb.append("\n * query after optimization='");
		QueryPretty qp = new QueryPretty();
		qp.evaluateExpression(tree.exprTree);
		sb.append(qp.toString()).append("'\n");
		sb.append("*/\n");
		sb.append("        public ").append(tree.exprTree.getSignature().getJavaTypeString(true)).append(" executeQuery").append("() ");
		Collection<Type> exceptions = tree.usedJNR.getResolvedMethodDeclaredExecptions();
		if(!exceptions.isEmpty()) {
			sb.append("throws ");
			for(Iterator<Type> it = exceptions.iterator(); it.hasNext(); ) {
				Type ex = it.next();
				sb.append(ex.toString());
				if(it.hasNext()) {
					sb.append(", ");
				}
			}
		}
		
		sb.append(" {").append("\n");
		sb.append(queryPretty.evaluateExpression(tree.exprTree));
		sb.append("        }");
		sb.append("}").append("\n");
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public String generateQueryInvocation(JCSbqlExpression tree) {
		if(tree.viewQuery) {
			return "";
		}
		qTrees.add(tree);
    	StringBuilder sb = new StringBuilder();
    	
       	sb.append("new ").append(getQueriesClassName()+(qTrees.size()-1)).append("(");

		for(Iterator<VarSymbol> fieldIt = tree.localVarContext.iterator(); fieldIt.hasNext(); ) {
			VarSymbol field = fieldIt.next();
			sb.append(field.name.toString());
			if(fieldIt.hasNext()) {
				sb.append(",");
			}
//			sb.append("\n");
		}

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
	
	private String getParamType(Type t) {
		if(t.isPrimitive()) {
			t = ClassTypes.getInstance().checkPrimitiveType(t);
		}
		if(t instanceof ArrayType) {
			ArrayType javaArrayType = (ArrayType) t;
			Type elementType = javaArrayType.elemtype;
			return "List<"+getParamType(elementType)+">";
		} else if(t instanceof ClassType) {
			return t.toString();
		} else {
			return null;
		}
	}
	


}

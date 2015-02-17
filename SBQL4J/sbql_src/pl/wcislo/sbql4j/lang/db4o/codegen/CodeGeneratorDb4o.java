package pl.wcislo.sbql4j.lang.db4o.codegen;

import java.util.List;

import pl.wcislo.sbql4j.java.preprocessor.PreprocessorRun;
import pl.wcislo.sbql4j.lang.codegen.CodeGenerator;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCImport;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCSbqlExpression;

public abstract class CodeGeneratorDb4o {
	public abstract String generateQueryInvocation(int sbqlQueryIndex, int db4oQueryIndex, Expression jcTree);
	public abstract String generateQueriesClass(int sbqlQueryIndex, int db4oQueryIndex, List<JCImport> imports);
	
	private String queriesClassName;
	private String queriesClassPackage;
	private CodeGenerator parentCodeGen;
	
	public CodeGeneratorDb4o(CodeGenerator parentCodeGen) {
		super();
		this.parentCodeGen = parentCodeGen;
	}
	
	public void setQueriesClassName(String cName) {
		this.queriesClassName = cName;
	}
	public String getQueriesClassName() {
		return queriesClassName;
	}
	public void setQueriesClassPackage(String queriesClassPackage) {
		this.queriesClassPackage = queriesClassPackage;
	}
	public String getQueriesClassPackage() {
		return queriesClassPackage;
	}
	public abstract int getGeneratedQueriesCount();
	public abstract boolean isGeneratingQueryClasses();
	
	public String genIdent(String prefix) {
		return parentCodeGen.genIdent(prefix);
	}
	public CodeGenerator getParentCodeGen() {
		return parentCodeGen;
	}
}
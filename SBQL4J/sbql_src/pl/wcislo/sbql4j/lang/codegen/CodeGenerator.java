package pl.wcislo.sbql4j.lang.codegen;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4o;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4oFactory;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4oFactory.CodeGenTypeDB4O;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCImport;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCSbqlExpression;

public abstract class CodeGenerator {
	public abstract String generateQueryInvocation(JCSbqlExpression jcTree);
	public abstract String generateQueriesClass(int queryIndex, List<JCImport> imports);
	
	private String queriesClassName;
	private String queriesClassPackage;
	private CodeGenTypeDB4O db4oCodeGenType;
//	private CodeGeneratorDb4o db4oCodeGen;
	private List<CodeGeneratorDb4o> genDb4oInvocations;
	private IdentifierGenerator identGen = new IdentifierGenerator();
	
	
	public CodeGenerator(CodeGenTypeDB4O db4oCodeGenType) {
		super();
		this.db4oCodeGenType = db4oCodeGenType;
//		this.db4oCodeGen = CodeGeneratorDb4oFactory.createCodeGenerator(db4oCodeGenType, this);
		this.genDb4oInvocations = new ArrayList<CodeGeneratorDb4o>();
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
	public List<CodeGeneratorDb4o> getGenDb4oInvocations() {
		return genDb4oInvocations;
	}
//	public CodeGeneratorDb4o getDb4oCodeGen() {
//		return db4oCodeGen;
//	}
	public CodeGenTypeDB4O getDb4oCodeGenType() {
		return db4oCodeGenType;
	}
	public abstract int getGeneratedQueriesCount();
//	public abstract void printExpressionTrace(String trace);
	public String genIdent(String prefix) {
		return identGen.genIdent(prefix);
	}
	
	public abstract int getCurrentQueryIndex();
	
}

package org;

import java.io.File;

import pl.wcislo.sbql4j.java.preprocessor.PreprocessorRun;
import pl.wcislo.sbql4j.lang.codegen.CodeGeneratorFactory.CodeGenType;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4oFactory.CodeGenTypeDB4O;

public class RunCurrentTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String baseDir = "E:\\Java\\workspaces\\mgr\\JavaCompiler\\";
//		String baseDir = "E:\\Szkoła\\zajecia\\jps\\workspace\\JavaCompiler\\";
		String baseDirSbql4j = "D:\\_workspaces\\NCBiR\\SBQL4J\\";
		String baseDirDb4o = "D:\\_workspaces\\NCBiR\\db4oj_sbql4j\\";
		String baseDir = "D:\\_workspaces\\NCBiR\\poleposition-0.40\\";
		
	    File destDir = new File(baseDir + "dist");
	    File[] s4jFiles = new File[] {
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\linq_comp\\OrderByTest.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\linq_comp\\SimpleCodeGeneratorTest.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\linq_comp\\LinqComparison.s4j"),
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\FileCloseByTest.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\ClassPackageTest.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\advanced\\AdvancedQueries.s4j"),
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\advanced\\AdvancedQueries2.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\EqualsTest.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\optimize\\deadquery\\DeadQueryOptimizerTest.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\optimize\\independentsubqueries\\IndependentSubqueriesOptimizerTest.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\view\\SimpleView1.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\view\\SimpleView2.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\view\\ViewTest1.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\view\\DerefMarkerTest.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\db4o\\Db4oSimpleTest.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\db4o\\Db4oLinqTest.s4j"),
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\db4o\\Db4oOperatorTest.s4j")
//	    	new File(baseDir+ "src\\org\\Test.s4j")
//	    	new File(baseDir + "currentTest\\edu\\pjwstk\\jps\\ExampleData.s4j")
//	    	new File(baseDir + "currentTest\\edu\\pjwstk\\jps\\JPSQueries1.s4j")
	    	new File(baseDir + "src\\org\\polepos\\teams\\db4o_sbql4j\\Sbql4jQueries.s4j")
	    };
	    String classpath = baseDirSbql4j + "classes;"+ baseDirSbql4j + "config;"+baseDirDb4o+"bin;" + baseDir + "bin;";
	    CodeGenType cgt = CodeGenType.NO_STACKS;
	    CodeGenTypeDB4O cgtDb4o = CodeGenTypeDB4O.NO_STACKS;
	    PreprocessorRun.Config.db4oMetabase = new File("db4oIndexes.xml");
	    
	    PreprocessorRun.run(destDir, s4jFiles, classpath, cgt, cgtDb4o, true, true);
	    
	}

}

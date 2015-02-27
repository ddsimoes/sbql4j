package pl.wcislo.sbql4j.javac.test;

import java.io.File;

import org.apache.commons.io.FileUtils;

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
		String baseDir = ".\\";
		String baseDirDb4o = "..\\db4oj\\";
	    File destDir = new File(baseDir + "dist");
	    if(destDir.exists()) {
	    	FileUtils.deleteQuietly(destDir);
//	    	destDir.delete();
	    }
	    destDir.mkdirs();
	    File[] s4jFiles = new File[] {
//	    	new File(baseDir + "currentTest\\ml\\wcislo\\sbql4j\\javac\\test\\linq_comp\\OrderByTest.s4j")
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
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\db4o\\Db4oSimpleTest.s4j"),
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\db4o\\Db4oLinqTest.s4j"),
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\db4o\\Db4oOperatorTest.s4j"),
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\db4o\\advanced\\AdvancedQueriesTest.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\db4o\\Db4oIndexQueries.s4j"),	
//	    	new File(baseDir + "currentTest\\edu\\pjwstk\\jps\\ExampleData.s4j")
//	    	new File(baseDir + "currentTest\\edu\\pjwstk\\jps\\JPSQueries1.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\xml\\XMLQuerySimpleTest.s4j"),
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\closeby\\CloseByTest.s4j"),
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\inheritance\\InheritanceTest.s4j"),
//    		new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\struct\\StructTest.s4j"),
    		new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\football\\query\\KORQueries.s4j"),
	    };
	    String classpath = baseDir + "classes;"+ baseDir + "config;"+baseDirDb4o+"bin;";
	    CodeGenType cgt = CodeGenType.NO_STACKS;
	    CodeGenTypeDB4O cgtDb4o = CodeGenTypeDB4O.NO_STACKS;
	    
	    PreprocessorRun.Config.optimiseDb4oIndexes = true;
	    PreprocessorRun.Config.db4oMetabase = new File("db4oIndexes.xml");
	    PreprocessorRun.run(destDir, s4jFiles, classpath, cgt, cgtDb4o, false, true);
	    
	}

}

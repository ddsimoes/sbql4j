package pl.wcislo.sbql4j.javac.test;

import java.io.File;

import pl.wcislo.sbql4j.java.preprocessor.PreprocessorRun;
import pl.wcislo.sbql4j.lang.codegen.CodeGeneratorFactory.CodeGenType;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4oFactory.CodeGenTypeDB4O;

public class RunCurrentTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String baseDir = "E:\\Java\\workspaces\\mgr\\SBQL4JTests\\";
		String baseDir = "..\\"; 
	    File destDir = new File(baseDir + "dist"); 
	    File[] s4jFiles = new File[] {
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\linq_comp\\OrderByTest.s4j")
//	    	new File(baseDir + "currentTest\\pl\\wcislo\\sbql4j\\javac\\test\\linq_comp\\SimpleCodeGeneratorTest.s4j")
	    	new File(baseDir + "src\\pl\\wcislo\\sbql4j\\javac\\test\\linq_comp\\LinqComparison.s4j")
	    };
	    String classpath = baseDir + "bin;"+ baseDir + "config;"+baseDir+"lib\\sbql4j_test.jar;";
	    CodeGenType cgt = CodeGenType.NO_STACKS;
	    PreprocessorRun.run(destDir, s4jFiles, classpath, cgt, CodeGenTypeDB4O.NO_STACKS, true, true);
	}
}

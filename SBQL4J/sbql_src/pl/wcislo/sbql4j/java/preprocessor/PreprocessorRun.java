package pl.wcislo.sbql4j.java.preprocessor;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildException;

import pl.wcislo.sbql4j.lang.codegen.CodeGenerator;
import pl.wcislo.sbql4j.lang.codegen.CodeGeneratorFactory;
import pl.wcislo.sbql4j.lang.codegen.CodeGeneratorFactory.CodeGenType;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4o;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4oFactory.CodeGenTypeDB4O;
import pl.wcislo.sbql4j.tools.javac.api.JavacTaskImpl;
import pl.wcislo.sbql4j.tools.javac.api.JavacTool;
import pl.wcislo.sbql4j.tools.javac.code.Type.ClassType;
import pl.wcislo.sbql4j.tools.javac.comp.AttrContext;
import pl.wcislo.sbql4j.tools.javac.comp.Env;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCImport;
import pl.wcislo.sbql4j.tools.javac.tree.Pretty;
import pl.wcislo.sbql4j.tools.javac.util.ListBuffer;
import sbql4jx.tools.JavaCompiler;
import sbql4jx.tools.JavaCompiler.CompilationTask;
import sbql4jx.tools.JavaFileObject;
import sbql4jx.tools.StandardJavaFileManager;
import de.hunsicker.jalopy.Jalopy;

public class PreprocessorRun {
	private static final Log log = LogFactory.getLog(PreprocessorRun.class);

	public static class Config {
		public static boolean printExpressionTrace = false;
		public static boolean printQueryPretty = true;
		public static boolean optimiseDeadQueries = true;
		public static boolean optimiseIndependentQueries = true;
		
		public static boolean optimiseDb4oIndexes = true;
		public static File db4oMetabase;
	}

	public static void run(File destDir, File[] s4jFiles, String classpath, CodeGenType codeGenType, CodeGenTypeDB4O cGenDb4o,
		boolean optimiseDeadQueries, boolean optimiseIndependentQueries) {
			Config.optimiseDeadQueries = optimiseDeadQueries;
			Config.optimiseIndependentQueries = optimiseIndependentQueries;
    		JavaCompiler compiler = new JavacTool();
    		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
    		
    		Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjects(s4jFiles);

    		List<String> options = new ArrayList<String>();
//    		options.add("-attrparseonly");
    		options.add("-cp");
    		options.add(classpath);
    		
    		options.add("-printsource");
    		
    		StringWriter sb = new StringWriter();
    		CompilationTask task = compiler.getTask(null, fileManager, null, options,
    				null, compilationUnits1);

    		JavacTaskImpl javacTask = (JavacTaskImpl) task;
//    		Object o = javacTask.enter();
    		try {
//    			Object ooo = fileManager.getJavaFileForInput(StandardLocation.CLASS_PATH, "java.lang.Number", Kind.CLASS);
//    			Object ooo = fileManager.getJavaFileForInput(StandardLocation.CLASS_PATH, "pl.wcislo.sbql4j.java.test.model.Employee", Kind.CLASS);
	    		Object o = javacTask.analyze();
	    		log.info("Sucessfully preprocessed task");
	    		//XXX debug
	    		
	    		ListBuffer<Env<AttrContext>> l = javacTask.getGenList();
	    		
	    		Jalopy codeFormatter = new Jalopy();
	    		int fileIndex=0;
	    		for(Env<AttrContext> env : l) {
	    			if(env.enclClass != null 
	    					&& env.enclClass.type != null 
	    					&& env.enclClass.type instanceof ClassType
	    					&& ((ClassType)env.enclClass.type).viewInfo != null ) {
	    				//for views we're not generating code directly 
	    				continue;
	    			}
//	    			JavaFileObject jfo = env.toplevel.sourcefile;
//	    			log("JavaFileObject class: "+jfo.getClass());
	    			String sourceFileName = env.toplevel.sourcefile.toString();
	    			
	    			String packagePath = env.toplevel.packge.toString().replace(".", File.separator);
	    			File destFileDir = new File(destDir, packagePath);
	    			File f0 = new File(sourceFileName);
	    			String fName = f0.getName();
	    			String destFile = packagePath+File.separator+fName.substring(0, fName.length() - 4)+".java";
//	    			if(sourceFileName.endsWith(".sbql4j")) {
	    				File f = new File(destDir, destFile);
	    				log.info("Creating java file: "+f);
	    				f.getParentFile().mkdirs();
	    				f.createNewFile();
	    				FileWriter fw = new FileWriter(f);
	    				CodeGenerator codeGen = CodeGeneratorFactory.createCodeGenerator(codeGenType, cGenDb4o);
	    				String genClassName = env.enclClass.name.toString()+"_SbqlQuery";
	    				codeGen.setQueriesClassName(genClassName);
	    				String genClassPackage = env.enclClass.sym.packge().toString();
	    				codeGen.setQueriesClassPackage(genClassPackage);
	    				try {
	    					Pretty pretty = new Pretty(fw, true, codeGen);
	    					pretty.printExpr(env.toplevel);
	    					
	    				} finally {
	    					fw.close();
	    				}
	    				
	    				List<JCImport> imports = new ArrayList<JCImport>();
	    				for(JCTree t : env.toplevel.defs) {
	    					if(t instanceof JCImport) {
	    						imports.add((JCImport)t);
	    					}
	    				}
	    				
	    				for(int i = 0; i<codeGen.getGeneratedQueriesCount(); i++) {
	    					codeGen.getGenDb4oInvocations().clear();
	    					String queriesClassContent = codeGen.generateQueriesClass(i, imports);
		    				if(queriesClassContent != null) {
	    						File qClassFile = new File(destFileDir, genClassName+i+".java");
	    						qClassFile.getParentFile().mkdirs();
	    						qClassFile.createNewFile();
	    						FileWriter qfw = new FileWriter(qClassFile);
	    						try {
		    						qfw.append(queriesClassContent);
	    						} finally {
	    							qfw.close();
	    						}
	    						codeFormatter.setInput(qClassFile);
	    						codeFormatter.setOutput(qClassFile);
	    						codeFormatter.format();
	    					}
		    				for(int j=0; j<codeGen.getGenDb4oInvocations().size(); j++) {
		    					CodeGeneratorDb4o genDb4o = codeGen.getGenDb4oInvocations().get(j);
		    					if(genDb4o.isGeneratingQueryClasses()) {
		    						genDb4o.setQueriesClassPackage(genClassPackage);
		    						String genDb4oClassName = codeGen.getQueriesClassName() + i + "Db4o" + j;
		    						
		    						String db4oQClassContent = genDb4o.generateQueriesClass(i, j, imports);
		    						File qClassFile = new File(destFileDir, genDb4oClassName+".java");
		    						qClassFile.getParentFile().mkdirs();
		    						qClassFile.createNewFile();
		    						FileWriter qfw = new FileWriter(qClassFile);
		    						try {
			    						qfw.append(db4oQClassContent);
		    						} finally {
		    							qfw.close();
		    						}
		    						codeFormatter.setInput(qClassFile);
		    						codeFormatter.setOutput(qClassFile);
		    						codeFormatter.format();
		    					}
		    				}
	    				}
	    				
//	    			}
	    				fileIndex++;
	    		}
	    		log.info("Sucessfully writed "+fileIndex+" files");
    		} catch (ExceptionInInitializerError e) {
    			System.err.println(e.getMessage());
    			e.printStackTrace();
    			throw new BuildException(e);
    		} catch (Exception e) {
    			throw new BuildException(e);
    		}
	}
}

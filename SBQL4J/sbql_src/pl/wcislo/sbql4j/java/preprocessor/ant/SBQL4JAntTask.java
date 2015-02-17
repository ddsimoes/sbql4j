package pl.wcislo.sbql4j.java.preprocessor.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.Path;

import pl.wcislo.sbql4j.java.preprocessor.PreprocessorRun;
import pl.wcislo.sbql4j.lang.codegen.CodeGeneratorFactory.CodeGenType;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4oFactory.CodeGenTypeDB4O;
import sbql4jx.tools.JavaFileManager;

public class SBQL4JAntTask extends MatchingTask {
	/**
	 * Used to read class files from classpath
	 */
	private static JavaFileManager fileManager;
	public static JavaFileManager getFileManager() {
		return fileManager;
	}
    private File destDir;
    private File sourceDir;
    private Path classpath;
    private String codeGenType = CodeGenType.NO_STACKS.toString();
    private String codeGenTypeDb4o = CodeGenTypeDB4O.NO_STACKS.toString();
    private Boolean printExpressionTrace = false;
    private Boolean printQueryPretty = false;
    private Boolean optimiseDeadQueries = true;
	private Boolean optimiseIndependentQueries = true;
	
	private Boolean optimiseDb4oIndexes = true;
	private File db4oMetabase;
    
    public void setDestDir(File destDir) {
		this.destDir = destDir;
	}
    public void setSourceDir(File sourceDir) {
		this.sourceDir = sourceDir;
	}
    public void setClasspath(Path classpath) {
      this.classpath = classpath;
    }
    public void setCodeGenType(String codeGenType) {
		this.codeGenType = codeGenType;
	}
    public void setCodeGenDb4oType(String codeGenDb4oType) {
		this.codeGenTypeDb4o = codeGenDb4oType;
	}
    public void setPrintExpressionTrace(String printExpressionTrace) {
		this.printExpressionTrace = Boolean.valueOf(printExpressionTrace);
	}
    public void setPrintQueryPretty(Boolean printQueryPretty) {
		this.printQueryPretty = printQueryPretty;
	}
    public void setOptimiseDeadQueries(Boolean optimiseDeadQueries) {
		this.optimiseDeadQueries = optimiseDeadQueries;
	}
	public void setOptimiseIndependentQueries(Boolean optimiseIndependentQueries) {
		this.optimiseIndependentQueries = optimiseIndependentQueries;
	}
	public void setDb4oMetabase(File db4oMetabase) {
		this.db4oMetabase = db4oMetabase;
	}
	public void setOptimiseDb4oIndexes(Boolean optimiseDb4oIndexes) {
		this.optimiseDb4oIndexes = optimiseDb4oIndexes;
	}
    
    public void execute() throws BuildException {
    	log("sourceDir="+sourceDir);
    	log("destDir="+destDir);
    	log("classPath="+classpath);
    	log("codeGenType="+codeGenType);
    	log("printExpressionTrace="+printExpressionTrace);
    	PreprocessorRun.Config.printExpressionTrace = this.printExpressionTrace;
    	PreprocessorRun.Config.printQueryPretty = this.printQueryPretty;
    	PreprocessorRun.Config.optimiseDeadQueries = this.optimiseDeadQueries;
    	PreprocessorRun.Config.optimiseIndependentQueries = this.optimiseIndependentQueries;
    	PreprocessorRun.Config.optimiseDb4oIndexes = this.optimiseDb4oIndexes;
    	PreprocessorRun.Config.db4oMetabase = this.db4oMetabase;
    	
    	CodeGenType codeGT = CodeGenType.valueOf(codeGenType);
    	if(codeGT == null) {
    		StringBuilder allowedCGTypes = new StringBuilder();
    		CodeGenType[] cgt = CodeGenType.values();
    		for(int i=0; i<cgt.length; i++) {
    			allowedCGTypes.append(cgt[i].toString());
    			if(i<cgt.length - 1) {
    				allowedCGTypes.append(",");
    			}
    		}
    		log("Invalid codeGenType, only allowed values: "+allowedCGTypes.toString(), Project.MSG_ERR);
    		return;
    	}
    	
    	CodeGenTypeDB4O codeGTDb4o = CodeGenTypeDB4O.valueOf(codeGenTypeDb4o);
    	if(codeGTDb4o == null) {
    		StringBuilder allowedCGTypes = new StringBuilder();
    		CodeGenTypeDB4O[] cgt = CodeGenTypeDB4O.values();
    		for(int i=0; i<cgt.length; i++) {
    			allowedCGTypes.append(cgt[i].toString());
    			if(i<cgt.length - 1) {
    				allowedCGTypes.append(",");
    			}
    		}
    		log("Invalid codeGenTypeDb4o, only allowed values: "+allowedCGTypes.toString(), Project.MSG_ERR);
    		return;
    	}
        DirectoryScanner ds = getDirectoryScanner(sourceDir);
        String[] includes = new String[] {"**\\*.s4j"};
        ds.setIncludes(includes);
        ds.scan();
        String[] s4jFileNames = ds.getIncludedFiles();
        File[] s4jFiles = new File[s4jFileNames.length];
        File baseDir = sourceDir;
        for(int i=0; i<s4jFileNames.length; i++) {
        	s4jFiles[i] = new File(baseDir, s4jFileNames[i]);
        }
        
        
        if (s4jFiles.length == 0) {
            log("No s4j files found.", Project.MSG_WARN);
        } else {
            log("Generating " + s4jFiles.length + " java files from s4j files.");
            PreprocessorRun.run(destDir, s4jFiles, classpath.toString(), codeGT, codeGTDb4o, optimiseDeadQueries, optimiseIndependentQueries);
        }
    }

}
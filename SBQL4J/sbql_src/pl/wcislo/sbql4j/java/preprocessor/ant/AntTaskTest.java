package pl.wcislo.sbql4j.java.preprocessor.ant;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;

public class AntTaskTest {
	public static void main(String[] args) {
		SBQL4JAntTask task = new SBQL4JAntTask();
		Project project = new Project();
		project.setName("SBQL");
		project.setBasedir(".");
		Path classPath = new Path(project, ";bin;classes;config;lib/asm-all-3.1.jar;lib/java-cup-11a.jar;lib/commons-logging-1.1.1.jar;lib/commons-logging-adapters-1.1.1.jar;lib/commons-logging-api-1.1.1.jar;lib/log4j-1.2.15.jar");
		task.setClasspath(classPath);
		File dist = new File(project.getBaseDir(), "dist");
		dist.mkdir();
		task.setDestDir(dist);
		File src = new File(project.getBaseDir(), "sbql_test");//,"sbql_src"
		task.setSourceDir(src);
		task.setProject(project);
		
		task.execute();
//		task.setClasspath(classpath)
	}
}

/* Copyright (C) 2004 - 2010  Versant Inc.  http://www.db4o.com */

package org.polepos.enhance;

import java.io.*;

import com.versant.core.jdo.tools.enhancer.*;

public class VodEnhancer {
	
	public static void main(String[] args) throws Exception {
		String propertiesFilePath = "settings/versant.properties";
		String outputDir = "bin";
		
		Enhancer enhancer = new Enhancer();
		enhancer.setPropertiesFile(new File(propertiesFilePath));
		
		System.out.println("Enhancing to: " + new File(outputDir).getAbsolutePath());
		
		enhancer.setOutputDir(new File(outputDir));
		enhancer.enhance();
	}

}

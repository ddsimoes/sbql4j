/* 
This file is part of the PolePosition database benchmark
http://www.polepos.org

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public
License along with this program; if not, write to the Free
Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
MA  02111-1307, USA. */

package org.polepos.enhance;

import java.io.*;

import org.polepos.enhance.IOServices.*;

public class JavaServices {

    public static String java(String className) throws IOException, InterruptedException{
        return IOServices.exec(javaExecutable(), javaRunArguments(className));
    }
    
    public static String java(String className, String[] args) throws IOException, InterruptedException{
        return IOServices.exec(javaExecutable(), javaRunArguments(className, args));
    }

    public static ProcessRunner startJava(String className, String[] args) throws IOException {
        return IOServices.start(javaExecutable(), javaRunArguments(className, args));
    }

    private static String javaExecutable() {
        for (int i = 0; i < vmTypes.length; i++) {
            if(vmTypes[i].identified()){
                return vmTypes[i].executable();
            }
        }
        throw new RuntimeException("VM " + vmName() + " not known. Please add as JavaVM class to end of JavaServices class.");
    }
    
    private static String[] javaRunArguments(String className) {
    	return javaRunArguments(className, new String[0]);
    }

    private static String[] javaRunArguments(String className, String[] args) {
    	String[] allArgs = new String[args.length + 3];
    	allArgs[0] = "-classpath";
    	allArgs[1] = IOServices.joinArgs(
						File.pathSeparator,
						new String[] {
						JavaServices.javaTempPath(), 
						currentClassPath(),
						
				}, runningOnWindows());
        allArgs[2] = className;
        System.arraycopy(args, 0, allArgs, 3, args.length);
        return allArgs;
        
    }

    private static String currentClassPath(){
        return property("java.class.path");
    }
    
    static String javaHome(){
        return property("java.home");
    }
    
    static String vmName(){
        return property("java.vm.name");
    }
    
    static String property(String propertyName){
        return System.getProperty(propertyName);
    }
    
    private static final JavaVM[] vmTypes = new JavaVM[]{
        new SunWindows(),
    };
    
    static interface JavaVM {
        boolean identified();
        String executable();
    }
    
    static class SunWindows implements JavaVM{
        public String executable() {
            return  Path4.combine(Path4.combine(javaHome(), "bin"), "java");
        }
        public boolean identified() {
            return true;
        }
    }
    
	public static String javaTempPath()
	{
		return IOServices.buildTempPath("java");
	}
	
	private static boolean runningOnWindows() {
		String osName = System.getProperty("os.name");
		if(osName == null) {
			return false;
		}
		return osName.indexOf("Win") >= 0;
	}
	
}

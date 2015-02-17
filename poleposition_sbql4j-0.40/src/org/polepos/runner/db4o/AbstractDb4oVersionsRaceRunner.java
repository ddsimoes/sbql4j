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


package org.polepos.runner.db4o;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;

import org.polepos.framework.*;
import org.polepos.reporters.*;
import org.polepos.runner.*;
import org.polepos.teams.db4o.*;


public abstract class AbstractDb4oVersionsRaceRunner extends AbstractRunner {
	
	public abstract DriverBase[] drivers();
	
	private String _workspace;
	
	
	public Team db4oTeam(){
		return db4oTeam(null, null);
	}
	
	public Team db4oTeam(String jarName) {
		return db4oTeam(jarName, null);
	}
	
	public Team configuredDb4oTeam(ConfigurationSetting... configurations) {
		return db4oTeam(workspace(), null, drivers(), configurations);
	}
	
	public Team configuredDb4oTeam(String jarName, ConfigurationSetting... configurations) {
		return db4oTeam(jarName, null, drivers(), configurations);
	}

	
	public Team db4oTeam(int[] options) {
    	return db4oTeam(null, options) ;
    }
	
	public Team db4oTeam(String jarName, int[] options) {
    	return db4oTeam(jarName, options, drivers(), null) ;
    }
    
	@Override
	protected Reporter[] reporters() {
		return DefaultReporterFactory.defaultReporters();
	}

    private Team db4oTeam(String jarName, int[] options, DriverBase[] drivers, ConfigurationSetting[] configurations) {
    	boolean loadDrivers = drivers == null || drivers.length == 0;
        try {
            Team team = null;    
            if(jarName == null){
                team = instantiateTeam((Class<? extends Team>)Class.forName(Db4oTeam.class.getName()), loadDrivers);
            }else{
                String[] prefixes={"com.db4o.","org.polepos.teams.db4o."};

                File[] projectPaths = projectPaths();
                URL[] urls = new URL[projectPaths.length + 1];
                for (int projectIdx = 0; projectIdx < projectPaths.length; projectIdx++) {
					urls[projectIdx] = new File(projectPaths[projectIdx], "bin").toURI().toURL();
				}
                urls[urls.length - 1] = jarURL(workspace(), jarName);
                
                ClassLoader loader=new VersionClassLoader(urls, prefixes, Team.class.getClassLoader());
                team = instantiateTeam((Class<? extends Team>)loader.loadClass(Db4oTeam.class.getName()), loadDrivers);
            }
            team.configure(options, configurations);
            if(jarName != null){
                team.getClass().getMethod("setJarName", new Class[]{String.class}).invoke(team, jarName);
            }
            
            for (int i = 0; i < drivers.length; i++) {
                String driverName = drivers[i].getClass().getName();
                invoke(team, "addDriver", driverName);
            }
            
            return team;
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }
    
    private Team instantiateTeam(Class<? extends Team> clazz, boolean loadDrivers) throws Exception {
    	Constructor<? extends Team> constr = clazz.getConstructor(new Class<?>[] { Boolean.TYPE });
    	return constr.newInstance(new Object[] { loadDrivers });
    }
    
    private URL jarURL(String workspace, String jarName) throws MalformedURLException{
    	for (File jarPath : libPaths()) {
            File file = new File(jarPath, jarName);
            if( file.exists()){
                return file.toURL();
            }
		}
        return null;
    }

	private void guessWorkSpace() {
        File absoluteFile = new File(new File("lib").getAbsolutePath());
        _workspace = absoluteFile.getParentFile().getParentFile().getAbsolutePath();
        System.out.println("Guessed workspace:\n" + _workspace + "\n");
    }
    
    private String workspace() {
    	if(_workspace == null) {
    	    _workspace = System.getProperty("polepos.dir");
    	    if (_workspace == null) {
    	        guessWorkSpace();
    	    }
    	}
    	return _workspace;
    }
    
    private static void invoke(Object onObject, String methodName, Object param) throws Exception{
        Class clazz = onObject.getClass();
        Method method = clazz.getMethod(methodName, param.getClass());
        method.invoke(onObject, new Object[]{param});
    }

    protected File[] libPaths() {
    	File[] projectPaths = projectPaths();
    	File[] libPaths = new File[projectPaths.length];
    	for (int pathIdx = 0; pathIdx < projectPaths.length; pathIdx++) {
			libPaths[pathIdx] = new File(projectPaths[pathIdx], "lib");
		}
    	return libPaths;
    }

    protected File[] projectPaths() {
    	return new File[]{ workspaceFile("polepos"), workspaceFile("db4opolepos") };
    }
    
    private File workspaceFile(String path) {
    	return new File(workspace(), path);
    }
}

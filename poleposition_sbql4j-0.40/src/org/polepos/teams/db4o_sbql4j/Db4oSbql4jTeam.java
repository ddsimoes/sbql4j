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

package org.polepos.teams.db4o_sbql4j;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.polepos.framework.*;

public class Db4oSbql4jTeam extends Team {
    
    private String _name = db4oName(); 
    
    private final List<DriverBase> _drivers;

	private int[] _options;
	
	private ConfigurationSetting[] _configurations;
	
	private Car[] _cars;
	
    public Db4oSbql4jTeam(boolean loadDrivers) {
        _drivers = new ArrayList<DriverBase>();
        if(loadDrivers) {
        	addDrivers();
        }
    }

    public Db4oSbql4jTeam() {
    	this(true);
    }

    private void addDrivers(){
    	addDriver(new FlatObjectDb4oSbql4j());
//    	addDriver(new NestedListsDb4oSbql4j());
//    	addDriver(new InheritanceHierarchyDb4oSbql4j());
//    	addDriver(new ComplexDb4oSbql4j());
//        addDriver(new MelbourneDb4oSbql4j());
//        addDriver(new SepangDb4oSbql4j());
//        addDriver(new BahrainDb4oSbql4j());
//        addDriver(new ImolaDb4oSbql4j());
//        addDriver(new BarcelonaDb4oSbql4j());
//        addDriver(new MonacoDb4oSbql4j());
//        addDriver(new NurburgringDb4oSbql4j());
//        addDriver(new MontrealDb4oSbql4j());
    }
    
    @Override
    public String name(){
		return _name;
	}
    
    @Override
    public String description() {
        return "the open source object database for Java and .NET";
    }
    
    

    @Override
    public Car[] cars(){
    	if(_cars == null){
    		_cars = new Car[]{ new Db4oSbql4jCar(this, _options, _configurations) };
    	}
		return _cars;
	}
    
    public void addDriver(DriverBase driver){
        _drivers.add(driver);
    }
    
    public void addDriver(String driverName){
        try {
            Class<?> clazz = this.getClass().getClassLoader().loadClass(driverName);
            Constructor<?> constr = clazz.getConstructor();
            addDriver((DriverBase)constr.newInstance());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public DriverBase[] drivers() {
        return _drivers.toArray(new DriverBase[0]);
    }

    @Override
    public String website() {
        return "http://www.db4o.com";
    }

    @Override
    public void configure(int[] options, ConfigurationSetting[] configurations) {
    	_options = options;
    	_configurations = configurations;
        _name = db4oName();
        
        if(configurations != null){
        	for (int i = 0; i < configurations.length; i++) {
        		_name += " " + configurations[i].name();
			}
        }
        
        if(options != null){
            for (int i = 0; i < options.length; i++) {
                try{
                    switch (options[i]){
                        case Db4oSbql4jOptions.CLIENT_SERVER:
                            _name += " C/S";
                            break;
                        case Db4oSbql4jOptions.CLIENT_SERVER_TCP:
                            _name += " TCP";
                            break;
                        case Db4oSbql4jOptions.MEMORY_IO:
                            _name += " MemIO";
                            break;
                        case Db4oSbql4jOptions.LAZY_QUERIES:
                            _name += " Q:LAZY";
                        	break;
                        case Db4oSbql4jOptions.SNAPSHOT_QUERIES:
                            _name += " Q:SNAP";
                        	break;
                        case Db4oSbql4jOptions.NORMAL_COLLECTION:
                        	_name += " NC";
                        	break;
                        case Db4oSbql4jOptions.BTREE_FREESPACE:
                            _name += " f:B";
                            break;
                        default:
                    
                    }
                }catch (Throwable t){
                    System.err.println("db4o option not available in this version");
                    t.printStackTrace();
                }
            }
        }
    }
    
    private String db4oName(){
        return "db4o_sbql4j";
    }

	public void setUp() {
		new File(Db4oSbql4jCar.FOLDER).mkdirs();
	    try {
	    	defaultCar().deleteDatabaseFile(); 
		} 
	    catch (IOException e) {
	    	throw new RuntimeException(e);
		}
	}

	private Db4oSbql4jCar defaultCar() {
		Car car = cars()[0];
		return (Db4oSbql4jCar) car;
	}

	protected void tearDown() {
		defaultCar().stopServer();
	}
    
	public final String databaseFile(){
        return defaultCar().databaseFile();
    }
    
    public void setJarName(String jarName){
        _name = _name.replaceAll("db4o", jarName);
    }
    
}

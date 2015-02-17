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

import org.polepos.framework.*;

import com.db4o.*;
import com.db4o.config.*;
import com.db4o.ext.*;
import com.db4o.io.*;


public class Db4oSbql4jCar extends Car {
	
	private transient ObjectServer _server;
	
	private transient ObjectContainer _objectContainer;
    
    public static final int SERVER_PORT = 4588;
    
    public static final String SERVER_HOST = "localhost";
    
    public static final String SERVER_USER = "db4o";
    
    public static final String SERVER_PASSWORD = "db4o";
    
    public static final String FOLDER;
    
    static {
        FOLDER = Db4oSbql4jTeam.class.getResource("/").getPath() + "data/db4o";
    }

    public static final String DB4O_FILE = "dbbench.yap";

	public static final String PATH = FOLDER + "/" + DB4O_FILE;
    
	private String name;
	
	private int[] _options;  
	
	private ConfigurationSetting[] _configurations;
	

	public Db4oSbql4jCar(Team team, int[] options, ConfigurationSetting[] configurations) {
		super(team, "0xFFCA07");
		_options = options;
		_configurations = configurations;
		name = Db4o.version().substring(5);
	}
	
	public int [] options() {
		return _options;
	}

	@Override
	public String name() {
		return name;
	}
    
    /**
     * Open database in the configured mode.
     */
    public ExtObjectContainer openObjectContainer(Configuration serverConfiguration, Configuration objectContainerConfiguration)
    {
    	configure(serverConfiguration);
    	configure(objectContainerConfiguration);
    	
        if (!isClientServer()) {
        	return openFile(objectContainerConfiguration);
		}
        
        if(isClientServerOverTcp()){
        	return openNetworkingClient(serverConfiguration, objectContainerConfiguration);
        }
        // embedded client server mode
        return openEmbeddedClient(serverConfiguration);
	}

	private boolean isClientServer() {
		return Db4oSbql4jOptions.containsOption(_options, Db4oSbql4jOptions.CLIENT_SERVER);
	}
	
	private boolean isClientServerOverTcp() {
		return Db4oSbql4jOptions.containsOption(_options, Db4oSbql4jOptions.CLIENT_SERVER_TCP);
	}
	
    public void configure(Configuration config) {
    	
    	config.messageLevel(-1);
    	
    	if(_configurations != null){
    		
            ClassLoader loader=this.getClass().getClassLoader();

    		for(ConfigurationSetting setting : _configurations){
    			try{
    				String settingsClassName = setting.getClass().getName();
					Class<?> loadedClass = loader.loadClass(settingsClassName);
					ConfigurationSetting newInstance = (ConfigurationSetting) loadedClass.newInstance();
    				newInstance.apply(config);
    			} catch ( Throwable t){
    				// Hint: ConfigurationSetting should be in the com.db4o namespace,
    				//       so it gets loaded by the version ClassLoader.  
                    System.err.println("db4o option not available in this version");
                    
                    t.printStackTrace();
    			}
    		}
    	}
    	
        if(_options != null){
            for (int i = 0; i < _options.length; i++) {
                try{
                    switch (_options[i]){
//                        case Db4oOptions.MEMORY_IO:
//                            config.io(new com.db4o.io.MemoryIoAdapter());
//                            break;
                        case Db4oSbql4jOptions.CACHED_BTREE_ROOT:
                            config.bTreeCacheHeight(1);
                            break;
                        case Db4oSbql4jOptions.LAZY_QUERIES:
                        	config.queries().evaluationMode(QueryEvaluationMode.LAZY);
                        	break;
                        case Db4oSbql4jOptions.SNAPSHOT_QUERIES:
                        	config.queries().evaluationMode(QueryEvaluationMode.SNAPSHOT);
                        	break;
                        case Db4oSbql4jOptions.BTREE_FREESPACE:
                            config.freespace().useBTreeSystem();
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
	
	private Storage newStorage(){
		// return new MemoryStorage();
		return new CachingStorage(new FileStorage());
	}
	
	private Storage _storage;

	private void startServer(Configuration config) {
        if(_server == null){
	        _server = Db4o.openServer(config, PATH, SERVER_PORT);
	        _server.grantAccess(SERVER_USER, SERVER_PASSWORD);
        }
	}

	public void stopServer() {
		if(_objectContainer != null){
			if(! _objectContainer.ext().isClosed()){
				_objectContainer.close();
			}
			_objectContainer = null;
		}
    	if(_server != null){
    		_server.close();
    		_server = null;
    	}
	}
	
    public void deleteDatabaseFile() throws IOException {
    	stopServer();
		try{
			storage().delete(PATH);
		}catch (Throwable t){
			System.err.println("Storages not yet available in this db4o version.");
			new File(PATH).delete();
			t.printStackTrace();
		}
    }    

	public final String databaseFile(){
        return PATH;
    }

	public ExtObjectContainer openFile(Configuration config) {
		
		// We keep the opened ObjectContainer here to be able
		// to create sessions on top of it in a concurrency run.
		
		if(_objectContainer == null){
			_objectContainer = Db4o.openFile(config(config), PATH).ext();
			return _objectContainer.ext();
		}
		return _objectContainer.ext().openSession().ext();
	}

	public ExtObjectContainer openNetworkingClient(Configuration serverConfiguration, Configuration objectContainerConfiguration) {
		startServer(config(serverConfiguration));
		return Db4o.openClient(config(objectContainerConfiguration), SERVER_HOST, SERVER_PORT, SERVER_USER, SERVER_PASSWORD).ext();
	}

	public ExtObjectContainer openEmbeddedClient(Configuration config) {
		startServer(config(config));
		return _server.openClient().ext();
	}

	private Configuration config(Configuration config) {
		try{
			config.storage(storage());
		}catch (Throwable t){
			System.err.println("Storages not yet available in this db4o version.");
			t.printStackTrace();
		}
		return config;
	}

	private Storage storage() {
		if(_storage == null){
			_storage = newStorage();
		}
		return _storage;
	}

	public void close(ExtObjectContainer container) {
		if(container != null && ! container.ext().isClosed()) {
			
			container.close();
			// give the weak reference collector thread time to end
			// TODO check whether this is really necessary, if it is, can't we somehow join on this guy?
//					ThreadUtil.sleepIgnoreInterruption(500);

		}
		
		if(container == _objectContainer){
			
			// set the state to null here, otherwise we open a session 
			// on top of it in the next open call
			_objectContainer = null;
			
		}

	}
	
}

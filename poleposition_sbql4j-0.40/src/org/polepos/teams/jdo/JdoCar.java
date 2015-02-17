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

package org.polepos.teams.jdo;

import java.sql.*;
import java.util.*;


import javax.jdo.*;
import javax.jdo.datastore.*;

import org.polepos.framework.*;
import org.polepos.teams.jdbc.*;


public class JdoCar extends Car {

    private transient PersistenceManagerFactory _persistenceManagerFactory;

    private final String              mDbName;
    private final String              mName;

    JdoCar(Team team, String name, String dbName, String color) throws CarMotorFailureException {
    	super(team, color);

        mName = name;
        mDbName = dbName;

        _website = Jdo.settings().getWebsite(name);
        _description = Jdo.settings().getDescription(name);

        initialize();

    }

    private boolean isSQL() {
        return mDbName != null;
    }
    
    private void initialize() {
        
        Properties properties = new Properties();

        properties.setProperty("javax.jdo.PersistenceManagerFactoryClass", Jdo.settings()
            .getFactory(mName));
        
        // properties.setProperty("javax.jdo.option.NontransactionalRead", "true");
        
        properties.setProperty("javax.jdo.option.Multithreaded", "true");
        properties.setProperty("javax.jdo.option.Optimistic", "true");
        
        // Versant VODJDO specific settings
        properties.setProperty("versant.metadata.0", "org/polepos/teams/jdo/data/vod.jdo");

        properties.setProperty("versant.allowPmfCloseWithPmHavingOpenTx","true");
        properties.setProperty("versant.vdsSchemaEvolve","true");
        
        properties.setProperty("versant.hyperdrive", "true");
        properties.setProperty("versant.remoteAccess", "false");
        
        // Turning this on can make the Concurrency tests crash.
        // Versant reports this is fixed. 
        // TODO: Test again against the latest VOD release 
        properties.setProperty("versant.l2CacheEnabled", "false");
        
        // Reduces RPC calls for VOD for optimistic read from 3 to 1 
        properties.setProperty("versant.retainConnectionInOptTx", "true");
        
        properties.setProperty("versant.l2CacheMaxObjects", "5000000");
        properties.setProperty("versant.l2QueryCacheEnabled", "true");
        properties.setProperty("versant.logDownloader", "none");
        properties.setProperty("versant.logging.logEvents", "none");
        properties.setProperty("versant.metricSnapshotIntervalMs", "1000000000");
        properties.setProperty("versant.metricStoreCapacity", "0");
        properties.setProperty("versant.vdsNamingPolicy", "none");
        
        
        
        
        properties.setProperty("versant.remoteMaxActive", "30");
        properties.setProperty("versant.maxActive", "30");

        if (isSQL()) {
            try {
                Class.forName(Jdbc.settings().getDriverClass(mDbName)).newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            properties.setProperty("javax.jdo.option.ConnectionDriverName", Jdbc.settings()
                .getDriverClass(mDbName));
            String connectUrl = Jdbc.settings().getConnectUrl(mDbName);
            
			properties.setProperty("javax.jdo.option.ConnectionURL", connectUrl);
            
            String user = Jdbc.settings().getUsername(mDbName);
            if (user != null) {
                properties.setProperty("javax.jdo.option.ConnectionUserName", user);
            }

            String password = Jdbc.settings().getPassword(mDbName);
            if (password != null) {
                properties.setProperty("javax.jdo.option.ConnectionPassword", password);
            }
        } else {

            properties.setProperty("javax.jdo.option.ConnectionURL", Jdo.settings().getURL(mName));

            String user = Jdo.settings().getUsername(mName);
            if (user != null) {
                properties.setProperty("javax.jdo.option.ConnectionUserName", user);
            }

            String password = Jdo.settings().getPassword(mName);
            if (password != null) {
                properties.setProperty("javax.jdo.option.ConnectionPassword", password);
            }
        }

        properties.setProperty("datanucleus.autoCreateSchema", "true");
        
//        properties.setProperty("datanucleus.validateTables", "false");
//        properties.setProperty("datanucleus.validateConstraints", "false");
//        properties.setProperty("datanucleus.metadata.validate", "false");
        
        properties.setProperty("datanucleus.connectionPool.maxIdle", "15");
        properties.setProperty("datanucleus.connectionPool.minIdle", "5");
        properties.setProperty("datanucleus.connectionPool.maxActive", "30");
        
        
        properties.setProperty("datanucleus.autoCreateConstraints", "false");
//        properties.setProperty("datanucleus.validateColumns", "false");
        
        
        properties.setProperty("datanucleus.connectionPoolingType", "DBCP");
        
		properties.setProperty("datanucleus.persistenceByReachabilityAtCommit", "false");
		properties.setProperty("datanucleus.manageRelationships", "false");
         
        
        _persistenceManagerFactory = JDOHelper.getPersistenceManagerFactory(properties, JDOHelper.class.getClassLoader());
    }

    public PersistenceManager getPersistenceManager() {
    	
        PersistenceManager persistenceManager = _persistenceManagerFactory.getPersistenceManager();
        
        if(! "hsqldb".equals(mDbName)){
        	return persistenceManager;
        }
        
        JDOConnection dataStoreConnection = persistenceManager.getDataStoreConnection();
        Connection connection = (Connection) dataStoreConnection.getNativeConnection();
        
        JdbcCar.hsqlDbWriteDelayToZero(connection);
        try {
        	
        	// Closing the connection here really feels strange, but otherwise
        	// Datanucleus hangs, probably because it runs out of JDBC connections.
        	
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return persistenceManager;
    }

    @Override
    public String name() {
       
        if(isSQL()){
            return Jdo.settings().getName(mName) + "/" +Jdbc.settings().getName(mDbName)+"-"+Jdbc.settings().getVersion(mDbName);
        }
        return Jdo.settings().getVendor(mName) + "/" + Jdo.settings().getName(mName)+"-"+Jdo.settings().getVersion(mName);

    }

}

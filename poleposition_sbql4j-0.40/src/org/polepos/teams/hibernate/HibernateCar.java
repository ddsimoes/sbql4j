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

package org.polepos.teams.hibernate;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.classic.Session;
import org.hibernate.tool.hbm2ddl.*;
import org.polepos.framework.*;
import org.polepos.teams.jdbc.*;

public class HibernateCar extends Car {
    
    private transient SessionFactory _sessionFactory;
    
    private final String _dbType;
    
    public HibernateCar(Team team, String dbType, String color){
    	super(team, color);
        _dbType = dbType;
    }

    public String name(){
        return Jdbc.settings().getName(_dbType)+"-"+Jdbc.settings().getVersion(_dbType);
    }
    
    public Session openSession() {
    	
    	if(_sessionFactory == null){
    		_sessionFactory = getSessionFactory();
    	}
                
        Session session = _sessionFactory.openSession();
        if("hsqldb".equals(_dbType)){
            session.createSQLQuery("SET WRITE_DELAY 0").executeUpdate();
		}
		return session;
    }
    
	public void recreateSessionFactory() {
		if(_sessionFactory != null){
			_sessionFactory.close();
		}
		_sessionFactory = getSessionFactory();
	}
    
    public void closeSession(Session session){
    	session.close();
    }
    
    private SessionFactory getSessionFactory()
    {
        Configuration cfg = new Configuration();
		for (Class clazz : HibernateTeam.persistentClasses()) {
			cfg.addClass(clazz);
		}
        
        try{
            Class.forName( Jdbc.settings().getDriverClass( _dbType ) ).newInstance();
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
        
        String connectUrl = Jdbc.settings().getConnectUrl( _dbType );
		cfg.setProperty("hibernate.connection.url", connectUrl);
        
        String user = Jdbc.settings().getUsername( _dbType );
        if(user != null){
            cfg.setProperty("hibernate.connection.user", user);
        }
        
        String password = Jdbc.settings().getPassword( _dbType );
        if(password != null){
            cfg.setProperty("hibernate.connection.password", password);
        }
        
        String dialect = Jdbc.settings().getHibernateDialect( _dbType );
        if(dialect != null){
            cfg.setProperty("hibernate.dialect", dialect);    
        }
        
        String jdbcDriverClass = Jdbc.settings().getDriverClass( _dbType );
        if(jdbcDriverClass != null){
            cfg.setProperty("hibernate.connection.driver_class", jdbcDriverClass);    
        }
        
        cfg.setProperty("hibernate.query.substitutions", "true 1, false 0, yes 'Y', no 'N'");
        cfg.setProperty("hibernate.connection.pool_size", "20");
        cfg.setProperty("hibernate.proxool.pool_alias", "pool1");
        cfg.setProperty("hibernate.jdbc.batch_size", "20");
        cfg.setProperty("hibernate.jdbc.fetch_size", "500");
        cfg.setProperty("hibernate.use_outer_join", "true");
        cfg.setProperty("hibernate.jdbc.batch_versioned_data", "true");
        cfg.setProperty("hibernate.jdbc.use_streams_for_binary", "true");
        cfg.setProperty("hibernate.max_fetch_depth", "1");
        cfg.setProperty("hibernate.cache.region_prefix", "hibernate.test");
        cfg.setProperty("hibernate.cache.use_query_cache", "true");
        cfg.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.EhCacheProvider");
        
        cfg.setProperty("hibernate.proxool.pool_alias", "pool1");
        
        cfg.setProperty("hibernate.connection.writedelay", "0");
 
        SessionFactory factory = cfg.buildSessionFactory();     
        new SchemaExport(cfg).create(false, true);
        return factory;         
    }



}

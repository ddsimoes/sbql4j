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

package org.polepos.teams.jvi;

import org.polepos.framework.Car;
import org.polepos.framework.CarMotorFailureException;
import org.polepos.framework.CheckSummable;
import org.polepos.framework.DriverBase;
import org.polepos.framework.TurnSetup;

import com.versant.trans.Query;
import com.versant.trans.QueryResult;
import com.versant.trans.TransSession;


/**
 * @author Christian Ernst
 */
public abstract class JviDriver extends DriverBase{
    
	private transient TransSession mSession;
    
	public void takeSeatIn( Car car, TurnSetup setup) throws CarMotorFailureException{
        super.takeSeatIn(car, setup);
        jviCar().initialize();
	}
    
	public void prepare(){
		mSession = jviCar().getTransSession();
	}
	
	public void backToPit(){
        if(db().isActive()){
        	db().rollback();
        }
        mSession.endSession();
        mSession = null;
	}
	
	protected JviCar jviCar(){
		return (JviCar)car();
	}
	
	protected TransSession db(){
		return mSession;
	}
    
    public void begin(){
        // JVI automaticly starts new session
//    	join();
    }
    
    public void commit(){
        db().commitAndRetain();
//        leave();
    }
    
    public void store(Object obj){
        db().makePersistent(obj);
    }
    
    protected void doQuery( Query q){
        QueryResult result = q.execute();
        Object o = result.next();
        while(o != null){
            if(o instanceof CheckSummable){
                addToCheckSum(((CheckSummable)o).checkSum());
            }
            o = result.next();
        }
        q.close();
    }
    
    protected void readExtent(Class clazz){
    	begin();
        
    	Query q = new Query(db(),"SELECT selfoid FROM "+ clazz.getName());
        QueryResult result = q.execute();
        Object o = result.next();
        while(o != null){
            if(o instanceof CheckSummable){
                addToCheckSum(((CheckSummable)o).checkSum());
            }
            o = result.next();
        }
        q.close();
        commit();
    }

}

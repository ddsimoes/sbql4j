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

package org.polepos.teams.jpa;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.polepos.framework.Car;
import org.polepos.framework.CarMotorFailureException;
import org.polepos.framework.CheckSummable;
import org.polepos.framework.DriverBase;
import org.polepos.framework.TurnSetup;


/**
 * @author Christian Ernst
 */
public abstract class JpaDriver extends DriverBase{
    
	private transient EntityManager mEntityManager;
    
	public void takeSeatIn( Car car, TurnSetup setup) throws CarMotorFailureException{
        super.takeSeatIn(car, setup);
	}
    
	public void prepare(){
		mEntityManager = jpaCar().getEntityManager();
	}
	
	public void backToPit(){
        EntityTransaction tx = db().getTransaction();
        if(tx.isActive()){
            tx.commit();
        }
        mEntityManager.close();
	}
	
	protected JpaCar jpaCar(){
		return (JpaCar)car();
	}
	
	protected EntityManager db(){
		return mEntityManager;
	}
    
    public void begin(){
        db().getTransaction().begin();
    }
    
    public void commit(){
        db().getTransaction().commit();
    }
    
    public void store(Object obj){
        db().persist(obj);
    }
    
    protected void doQuery( Query q, Object param){
    	q.setParameter("param", param);
        List result = (List)q.getResultList();
        Iterator it = result.iterator();
        while(it.hasNext()){
            Object o = it.next();
            if(o instanceof CheckSummable){
                addToCheckSum(((CheckSummable)o).checkSum());
            }
        }
    }
    
    protected void readExtent(Class clazz){
    	begin();
        
        Iterator itr = db().createQuery("SELECT this FROM "+clazz.getSimpleName()+ " this").getResultList().iterator();
        while (itr.hasNext()){
            Object o = itr.next();
            if(o instanceof CheckSummable){
                addToCheckSum(((CheckSummable)o).checkSum());    
            }
        }
        
        commit();
    }
    
    @Override
	public boolean supportsConcurrency() {
    	
    	// Unfortunately OpenJpa has problems generating primary keys concurrently
    	// so we failed to run OpenJpa in concurrent mode
    	
		return false;
	}

}

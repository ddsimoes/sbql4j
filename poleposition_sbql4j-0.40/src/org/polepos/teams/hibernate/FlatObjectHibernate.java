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
import org.polepos.circuits.flatobject.*;
import org.polepos.teams.hibernate.data.*;


public class FlatObjectHibernate extends HibernateDriver implements FlatObject{
     
    private final String FROM = "from org.polepos.teams.hibernate.data.IndexedObject";
    
	public void write(){
		Transaction tx = begin();
        initializeTestId(objectCount());
		while ( hasMoreTestIds()){
			IndexedObject indexedObject = new IndexedObject(nextTestId());
			store(indexedObject);
			if(doCommit()){
				tx.commit();
				tx.begin();
			}
            addToCheckSum(indexedObject);
		}
	}
	
    public void queryIndexedString() {
        initializeTestId(setup().getSelectCount());
        while(hasMoreTestIds()) {
        	doSingleResultQuery( FROM + " where String='" + IndexedObject.queryString(nextTestId()) + "'" );
        }
    }
    
    public void queryIndexedInt() {
        initializeTestId(setup().getSelectCount());
        while(hasMoreTestIds()) {
        	doSingleResultQuery(selectFromNextTestId());
        }
    }

	private String selectFromNextTestId() {
		return FROM + " where Int=" + nextTestId();
	}
    
    public void update() {
    	Transaction tx = begin();
        initializeTestIdD(setup().getUpdateCount());
        while(hasMoreTestIds()) {
			IndexedObject indexedObject = (IndexedObject) db().iterate(selectFromNextTestId()).next();
			indexedObject.updateString();
        	store(indexedObject);
            addToCheckSum(indexedObject);
        }
        tx.commit();
	}
    
    public void delete() {
    	Transaction tx = begin();
        initializeTestId(setup().getUpdateCount());
        while(hasMoreTestIds()) {
			IndexedObject indexedObject = (IndexedObject) db().iterate(selectFromNextTestId()).next();
			addToCheckSum(indexedObject);
        	delete(indexedObject);
        }
        tx.commit();
	}
	
}

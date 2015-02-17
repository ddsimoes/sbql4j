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

package org.polepos.teams.db4o;

import org.polepos.circuits.flatobject.*;
import org.polepos.data.*;

import com.db4o.*;
import com.db4o.config.*;
import com.db4o.query.*;


public class FlatObjectDb4o extends Db4oDriver implements FlatObject{
		
	@Override
	public void configure(Configuration config) {
		indexField(config, IndexedObject.class , "_int");
		indexField(config, IndexedObject.class , "_string");
	}
	
	public void write(){
        initializeTestId(objectCount());
		while ( hasMoreTestIds()){
			IndexedObject indexedObject = new IndexedObject(nextTestId());
			store(indexedObject);
			purge(indexedObject);
			if(doCommit()){
				commit();
			}
            addToCheckSum(indexedObject);
		}
	}

    public void queryIndexedString() {
        initializeTestId(selectCount());
        while(hasMoreTestIds()) {
            Query q = db().query();
            q.constrain( IndexedObject.class );
            q.descend( "_string" ).constrain(IndexedObject.queryString(nextTestId()));
            doQuery(q);
        }
    }

    public void queryIndexedInt() {
        initializeTestId(selectCount());
        while(hasMoreTestIds()) {
    		Query q = db().query();
    		q.constrain( IndexedObject.class );
    		q.descend( "_int" ).constrain(nextTestId());
    		doQuery(q);
        }
    }

    public void update() {
        initializeTestId(updateCount());
        while(hasMoreTestIds()) {
            Query q = db().query();
        	q.constrain( IndexedObject.class );
        	q.descend("_int").constrain(nextTestId());
        	ObjectSet<IndexedObject> objectSet = q.execute();
        	IndexedObject indexedObject = objectSet.next();
        	indexedObject.updateString();
        	store(indexedObject);
            addToCheckSum(indexedObject);
        }
        commit();
	}
	
    public void delete() {
        initializeTestId(updateCount());
        while(hasMoreTestIds()) {
            Query q = db().query();
        	q.constrain( IndexedObject.class );
        	q.descend("_int").constrain(nextTestId());
        	ObjectSet<IndexedObject> objectSet = q.execute();
        	IndexedObject indexedObject = objectSet.next();
        	addToCheckSum(indexedObject);
            delete(indexedObject);
        }
        commit();
	}
	
}

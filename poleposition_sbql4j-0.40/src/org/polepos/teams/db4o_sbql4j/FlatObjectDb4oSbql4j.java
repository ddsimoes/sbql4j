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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.polepos.circuits.flatobject.FlatObject;
import org.polepos.data.IndexedObject;
import org.polepos.framework.CheckSummable;

import pl.wcislo.sbql4j.lang.db4o.indexes.IndexManager;
import pl.wcislo.sbql4j.lang.db4o.indexes.IndexMetadataXMLStore;

import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.query.Query;


public class FlatObjectDb4oSbql4j extends Db4oSbql4jDriver implements FlatObject{
		
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
//		IndexManager im = new IndexManager(db(), new IndexMetadataXMLStore(new File("db4oIndexes.xml")));
//		System.out.println(im.getMetabase());
	}

    public void queryIndexedString() {
        initializeTestId(selectCount());
//        while(hasMoreTestIds()) {
//            Query q = db().query();
//            q.constrain( IndexedObject.class );
//            q.descend( "_string" ).constrain(IndexedObject.queryString(nextTestId()));
//            doQuery(q);
//        }
        List<String> stringIds = new ArrayList<String>();
        while(hasMoreTestIds()) {
            stringIds.add(IndexedObject.queryString(nextTestId()));
        }
        for(String s : stringIds) {
//        	Collection<IndexedObject> res = Sbql4jQueriesModified.flatObjectIndexedString(db(), s);
        	Collection<IndexedObject> res = Sbql4jQueries.flatObjectIndexedString(db(), s);
        	for(CheckSummable ch : res) {
            	addToCheckSum(ch.checkSum());
            }
        }
    }

    public void queryIndexedInt() {
        initializeTestId(selectCount());
//        while(hasMoreTestIds()) {
//    		Query q = db().query();
//    		q.constrain( IndexedObject.class );
//    		q.descend( "_int" ).constrain(nextTestId());
//    		doQuery(q);
//        }
        List<Integer> ids = new ArrayList<Integer>();
        while(hasMoreTestIds()) {
    		ids.add(nextTestId());
        }
        for(Integer i : ids) {
	        Collection<IndexedObject> res = Sbql4jQueries.flatObjectIndexedInt(db(), i);
	        for(CheckSummable ch : res) {
	        	addToCheckSum(ch.checkSum());
	        }
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

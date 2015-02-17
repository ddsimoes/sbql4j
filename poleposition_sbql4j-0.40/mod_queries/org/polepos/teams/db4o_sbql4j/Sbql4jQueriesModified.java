package org.polepos.teams.db4o_sbql4j;

import java.util.*;
import org.polepos.circuits.flatobject.*;
import org.polepos.data.*;
import com.db4o.*;
import com.db4o.config.*;
import com.db4o.query.*;
import pl.wcislo.sbql4j.java.model.runtime.Struct;

public class Sbql4jQueriesModified {
    
    public Sbql4jQueriesModified() {
        super();
    }
    
    public static Collection<IndexedObject> flatObjectIndexedString(ObjectContainer db, String stringId) {
        return db.query(new QueryIndexedObjectByString(stringId));
    }
    
//    public static Collection<IndexedObject> flatObjectIndexedInt(ObjectContainer db, List<Integer> intIds) {
//        return new Sbql4jQueries_SbqlQuery1(db,intIds).executeQuery();
//    }
//    
//    public static Collection<InheritanceHierarchy4> inheritanceHierarchyQuery(ObjectContainer db, int i2Constrain) {
//        return new Sbql4jQueries_SbqlQuery2(db,i2Constrain).executeQuery();
//    }
//    
//    public static Collection<ComplexHolder2> complexQuery(ObjectContainer db, int i2Constrain) {
//        return new Sbql4jQueries_SbqlQuery3(db,i2Constrain).executeQuery();
//    }
}
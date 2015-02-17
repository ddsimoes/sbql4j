/* Copyright (C) 2004 - 2006  db4objects Inc.  http://www.db4o.com */

package org.polepos.teams.db4o_sbql4j;

import org.polepos.circuits.montreal.*;

import com.db4o.config.*;
import com.db4o.query.*;


public class MontrealDb4oSbql4j extends Db4oSbql4jDriver implements MontrealDriver {
	

	@Override
	public void configure(Configuration config) {
		
	}

    public void write() {
        
        int count = 1000;
        int elements = setup().getObjectSize();
        
        begin();
        for (int i = 1; i<= count; i++) {
            store(ListHolder.generate(i, elements));
        }
        commit();
    }

    public void read() {
        
        Query q = db().query();
        q.constrain(ListHolder.class);
        doQuery(q);
        
    }

}

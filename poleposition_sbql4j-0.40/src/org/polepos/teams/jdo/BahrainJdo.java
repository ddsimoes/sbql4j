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

import java.util.*;

import javax.jdo.*;

import org.polepos.circuits.bahrain.*;
import org.polepos.teams.jdo.data.*;



public class BahrainJdo extends JdoDriver implements BahrainDriver{
	
	
    
    public void write(){
        
        begin();
        
        int numobjects = setup().getObjectCount();
        int commitinterval = setup().getCommitInterval();
        int commitctr = 0;
        for ( int i = 1; i <= numobjects; i++ ){
            JdoIndexedPilot p = new JdoIndexedPilot( "Pilot_" + i, "Jonny_" + i, i , i );
            db().makePersistent( p );
            addToCheckSum(i);
            if ( commitinterval > 0  &&  ++commitctr >= commitinterval ){
                commitctr = 0;
                commit();
                begin();
            }
        }
        
        commit();
    }
    
    
 
    public void queryIndexedString() {
        int count = setup().getSelectCount();
        String filter = "this.mName == param";
        for (int i = 1; i <= count; i++) {
            Query query = db().newQuery(JdoIndexedPilot.class, filter);
            query.declareParameters("String param");
            doQuery(query, "Pilot_" + i);
        }
    }
    
    
            
    public void queryString() {
        int count = setup().getSelectCount();
        String filter = "this.mFirstName == param";
        for (int i = 1; i <= count; i++) {
            Query query = db().newQuery(JdoIndexedPilot.class, filter);
            query.declareParameters("String param");
            doQuery(query, "Jonny_" + i);
        }
    }

    public void queryIndexedInt() {
        int count = setup().getSelectCount();
        String filter = "this.mLicenseID == param";
        for (int i = 1; i <= count; i++) {
            Query query = db().newQuery(JdoIndexedPilot.class, filter);
            query.declareParameters("Integer param");
            doQuery(query, new Integer(i));
        }
    }

    public void queryInt() {
        int count = setup().getSelectCount();
        String filter = "this.mPoints == param";
        for (int i = 1; i <= count; i++) {
            Query query = db().newQuery(JdoIndexedPilot.class, filter);
            query.declareParameters("Integer param");
            doQuery(query, new Integer(i));
        }
    }

	public void update() {
		PersistenceManager pm = db();
	    int updateCount = setup().getUpdateCount();
	    pm.currentTransaction().begin();
	    Extent extent = pm.getExtent(JdoIndexedPilot.class, false);
	    Iterator it = extent.iterator();
	    for (int i = 1; i <= updateCount; i++) {
	        JdoIndexedPilot p = (JdoIndexedPilot)it.next();
	        p.setName( p.getName().toUpperCase() );
	        addToCheckSum(1);
	    }
	    extent.closeAll();
	    pm.currentTransaction().commit();
	}
    
    public void delete() {
        begin();
        int commitinterval = setup().getCommitInterval();
        int commitctr = 0;
        Extent extent = db().getExtent(JdoIndexedPilot.class, false);
        Iterator it = extent.iterator();
        while(it.hasNext()){
            db().deletePersistent(it.next());
            addToCheckSum(1);
            if ( commitinterval > 0  &&  ++commitctr >= commitinterval ){
                commitctr = 0;
                commit();
                begin();
            }
        }
        extent.closeAll();
        commit();
    }

}

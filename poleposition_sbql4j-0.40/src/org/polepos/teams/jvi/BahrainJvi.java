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

import org.polepos.circuits.bahrain.BahrainDriver;
import org.polepos.teams.jvi.data.JviIndexedPilot;

import com.versant.trans.Query;
import com.versant.trans.QueryResult;


/**
 * @author Christian Ernst
 */
public class BahrainJvi extends JviDriver implements BahrainDriver{
    
    public void write(){
        
        begin();
        
        int numobjects = setup().getObjectCount();
        int commitinterval = setup().getCommitInterval();
        
        int commitctr = 0;
        for ( int i = 1; i <= numobjects; i++ ){
            
             
            JviIndexedPilot p = new JviIndexedPilot( "Pilot_" + i, "Jonny_" + i, i , i );
            db().makePersistent( p );
            
            if ( commitinterval > 0  &&  ++commitctr >= commitinterval ){
                commitctr = 0;
                commit();
                begin();
                Log.logger.fine( "commit while writing at " + i+1 ); //NOI18N
            }
            addToCheckSum(i);
        }
        
        commit();
    }
    
    
 
    public void queryIndexedString() {
    	begin();
        int count = setup().getSelectCount();
        String filter = "SELECT selfoid FROM "+ JviIndexedPilot.class.getName()+ " WHERE mName = $param";
        for (int i = 1; i <= count; i++) {
            Query query = new Query(db(),filter);
            query.bind("param", "Pilot_" + i);
            doQuery(query);
        }
        commit();
    }
    
    
            
    public void queryString() {
    	begin();
        int count = setup().getSelectCount();
        String filter = "SELECT selfoid FROM "+ JviIndexedPilot.class.getName()+ " WHERE mFirstName = $param";
        for (int i = 1; i <= count; i++) {
        	Query query = new Query(db(),filter);
            query.bind("param","Jonny_" + i);
            doQuery(query);
        }
        commit();
    }

    public void queryIndexedInt() {
    	begin();
        int count = setup().getSelectCount();
        String filter = "SELECT selfoid FROM "+ JviIndexedPilot.class.getName()+ " WHERE mLicenseID = $param";
        for (int i = 1; i <= count; i++) {
        	Query query = new Query(db(),filter);
            query.bind("param",new Integer(i));
            doQuery(query);
        }
        commit();
    }

    public void queryInt() {
        int count = setup().getSelectCount();
        String filter = "SELECT selfoid FROM "+ JviIndexedPilot.class.getName()+ " WHERE mPoints = $param";
        for (int i = 1; i <= count; i++) {
        	Query query = new Query(db(),filter);
            query.bind("param",new Integer(i));
            doQuery(query);
        }
    }

    public void update() {
        
        begin();
        int updateCount = setup().getUpdateCount();
        Query q = new Query(db(),"SELECT selfoid FROM "+ JviIndexedPilot.class.getName());
        QueryResult result = q.execute();
        for (int i = 1; i <= updateCount; i++) {
            JviIndexedPilot p = (JviIndexedPilot)result.next();
            p.setName( p.getName().toUpperCase() );
            addToCheckSum(1);
        }
        q.close();
        commit();
    }
    
    public void delete() {
        begin();
        Query q = new Query(db(),"SELECT selfoid FROM "+ JviIndexedPilot.class.getName());
        QueryResult result = q.execute();
        Object o = result.next();
        while(o != null){
            db().deleteObject(o);
            addToCheckSum(1);
            o = result.next();
        }
        q.close();
        commit();
    }

}

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

import org.polepos.circuits.bahrain.*;
import org.polepos.data.*;
import org.polepos.runner.db4o.*;

import com.db4o.*;
import com.db4o.config.*;
import com.db4o.query.*;



/**
 * @author Herkules
 */
public class BahrainDb4o extends Db4oDriver implements BahrainDriver{
		

	@Override
	public void configure(Configuration config) {
		indexField(config, Pilot.class , "mName");
		indexField(config, Pilot.class , "mLicenseID");
	}
	
	public void write(){
        int commitctr = 0;
        int count = setup().getObjectCount();
        int commitInterval = setup().getCommitInterval();
		for ( int i = 1; i <= count; i++ ){
			store( new Pilot( "Pilot_" + i, "Jonny_" + i, i , i ) );
            if ( commitInterval> 0  &&  ++commitctr >= commitInterval ){
                commitctr = 0;
                commit();
            }
            addToCheckSum(i);
		}
		commit();
	}
 
    public void queryIndexedString() {
        int count = setup().getSelectCount();
        for (int i = 1; i <= count; i++) {
            Query q = db().query();
            q.constrain( Pilot.class );
            q.descend( "mName" ).constrain("Pilot_" + i);
            doQuery( q );
        }
        
    }
            
    public void queryString() {
        int count = setup().getSelectCount();
        for (int i = 1; i <= count; i++) {
            Query q = db().query();
            q.constrain( Pilot.class );
            q.descend( "mFirstName" ).constrain("Jonny_" + i);
            doQuery( q );
        }
    }

    public void queryIndexedInt() {
        int count = setup().getSelectCount();
        for (int i = 1; i <= count; i++) {
    		Query q = db().query();
    		q.constrain( Pilot.class );
    		q.descend( "mLicenseID" ).constrain(new Integer(i));
    		doQuery( q );
        }
    }

    public void queryInt() {
        int count = setup().getSelectCount();
        for (int i = 1; i <= count; i++) {
    		Query q = db().query();
    		q.constrain( Pilot.class );
    		q.descend( "mPoints" ).constrain(new Integer(i));
    		doQuery( q );
        }
    }

    public void update() {
        int updateCount = setup().getUpdateCount();
        Query q = db().query();
    	q.constrain( Pilot.class );
    	ObjectSet set = q.execute();
        for (int i = 1; i <= updateCount; i++) {
            Pilot p = (Pilot)set.next();
            p.setName( p.getName().toUpperCase() );
            store(p);
            addToCheckSum(1);
        }
    	db().commit();
	}
	
    public void delete() {
    	int commitInterval = setup().getCommitInterval();
        int commitctr = 0;
    	Query q = db().query();
    	q.constrain( Pilot.class );
    	ObjectSet deleteset = q.execute();
    	while ( deleteset.hasNext() ){
    		db().delete( deleteset.next() );
            if ( commitInterval> 0  &&  ++commitctr >= commitInterval ){
                commitctr = 0;
                commit();
            }
            addToCheckSum(1);
    	}
    	db().commit();
	}
	
}

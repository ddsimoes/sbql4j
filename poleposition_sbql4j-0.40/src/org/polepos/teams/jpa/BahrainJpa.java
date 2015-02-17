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

import javax.persistence.Query;

import org.polepos.circuits.bahrain.BahrainDriver;
import org.polepos.teams.jpa.data.JpaIndexedPilot;


/**
 * @author Christian Ernst
 */
public class BahrainJpa extends JpaDriver implements BahrainDriver{
    
    public void write(){
        
        begin();
        
        int numobjects = setup().getObjectCount();
        int commitinterval = setup().getCommitInterval();
        
        int commitctr = 0;
        for ( int i = 1; i <= numobjects; i++ ){
            
             
            JpaIndexedPilot p = new JpaIndexedPilot( "Pilot_" + i, "Jonny_" + i, i , i );
            db().persist( p );
            
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
        String filter = "SELECT this FROM JpaIndexedPilot this WHERE this.mName = :param";
        for (int i = 1; i <= count; i++) {
            Query query = db().createQuery(filter);
            doQuery(query, "Pilot_" + i);
        }
        commit();
    }
    
    
            
    public void queryString() {
    	begin();
        int count = setup().getSelectCount();
        String filter = "SELECT this FROM JpaIndexedPilot this WHERE this.mFirstName = :param";
        for (int i = 1; i <= count; i++) {
            Query query = db().createQuery(filter);
            doQuery(query, "Jonny_" + i);
        }
        commit();
    }

    public void queryIndexedInt() {
    	begin();
        int count = setup().getSelectCount();
        String filter = "SELECT this FROM JpaIndexedPilot this WHERE this.mLicenseID = :param";
        for (int i = 1; i <= count; i++) {
            Query query = db().createQuery( filter);
            doQuery(query, new Integer(i));
        }
        commit();
    }

    public void queryInt() {
    	 begin();
        int count = setup().getSelectCount();
        String filter = "SELECT this FROM JpaIndexedPilot this WHERE this.mPoints = :param";
        for (int i = 1; i <= count; i++) {
            Query query = db().createQuery(filter);
            doQuery(query, new Integer(i));
        }
        commit();
    }

    public void update() {
        
        begin();
        int updateCount = setup().getUpdateCount();
        Iterator it = db().createQuery("SELECT this FROM JpaIndexedPilot this").getResultList().iterator();
        for (int i = 1; i <= updateCount; i++) {
            JpaIndexedPilot p = (JpaIndexedPilot)it.next();
            p.setName( p.getName().toUpperCase() );
            addToCheckSum(1);
        }
        commit();
    }
    
    public void delete() {
        begin();
        Iterator it = db().createQuery("SELECT this FROM JpaIndexedPilot this").getResultList().iterator();
        while(it.hasNext()){
            db().remove(it.next());
            addToCheckSum(1);
        }
        commit();
    }

}

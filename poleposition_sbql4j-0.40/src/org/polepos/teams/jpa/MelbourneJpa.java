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

import org.polepos.circuits.melbourne.MelbourneDriver;
import org.polepos.teams.jpa.data.JpaPilot;

/**
 * @author Christian Ernst
 */
public class MelbourneJpa extends JpaDriver implements MelbourneDriver{
	
	public void write(){
        
		begin();
        
        int numobjects = setup().getObjectCount();
        int commitinterval = setup().getCommitInterval();
		
		int commitctr = 0;
		for ( int i = 1; i <= numobjects; i++ ){
            
			JpaPilot p = new JpaPilot( "Pilot_" + i, i );
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
	
	public void read(){
        readExtent(JpaPilot.class);
	}
    
    public void read_hot() {
        read();
    }
    
	public void delete(){
        
        int numobjects =setup().getObjectCount();
        int commitinterval = setup().getCommitInterval();

		begin();
		
		Iterator itr = db().createQuery("SELECT this FROM JpaPilot this").getResultList().iterator();
		int commitctr = 0;
		for ( int i = 0; i < numobjects; i++ ){
            
			JpaPilot p = (JpaPilot)itr.next();
			db().remove( p );
            
			if ( commitinterval > 0  && ++commitctr >= commitinterval ){
				commitctr = 0;
				
                commit();
				begin();
				Log.logger.fine( "commit while deleting at " + i+1 ); //NOI18N
                
				itr = db().createQuery("SELECT this FROM JpaPilot this").getResultList().iterator();
			}
		}
        
		commit();

	}
}

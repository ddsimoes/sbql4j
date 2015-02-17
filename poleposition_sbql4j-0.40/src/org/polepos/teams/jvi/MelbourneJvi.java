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


import org.polepos.circuits.melbourne.MelbourneDriver;
import org.polepos.teams.jvi.data.JviPilot;

import com.versant.trans.Query;
import com.versant.trans.QueryResult;

/**
 * @author Christian Ernst
 */
public class MelbourneJvi extends JviDriver implements MelbourneDriver{
	
	public void write(){
		begin();
        
        int numobjects = setup().getObjectCount();
        int commitinterval = setup().getCommitInterval();
		
		int commitctr = 0;
		for ( int i = 1; i <= numobjects; i++ ){
            
			JviPilot p = new JviPilot( "Pilot_" + i, i );
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
	
	public void read(){
        readExtent(JviPilot.class);
	}
    
    public void read_hot() {
        read();
    }
    
	public void delete(){
        
        int numobjects = setup().getObjectCount();
        int commitinterval = setup().getCommitInterval();

		begin();
		
        Query q = new Query(db(),"SELECT selfoid FROM "+ JviPilot.class.getName());
        QueryResult result = q.execute();
		int commitctr = 0;
		for ( int i = 0; i < numobjects; i++ ){
            
			JviPilot p = (JviPilot) result.next();
			db().deleteObject( p );
            
			if ( commitinterval > 0  && ++commitctr >= commitinterval ){
				commitctr = 0;
                
                q.close();
				
                commit();
				begin();
				Log.logger.fine( "commit while deleting at " + i+1 ); //NOI18N

		        q = new Query(db(),"SELECT selfoid FROM "+ JviPilot.class.getName());
		        result = q.execute();
			}
		}
		
		q.close();
		commit();

	}
}

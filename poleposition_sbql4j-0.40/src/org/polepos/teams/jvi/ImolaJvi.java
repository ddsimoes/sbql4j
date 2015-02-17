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

import org.polepos.circuits.imola.ImolaDriver;
import org.polepos.teams.jvi.data.JviPilot;

/**
 * @author Christian Ernst
 */
public class ImolaJvi extends JviDriver implements ImolaDriver{
    
    private Object[] oids;
    
    public void store() {
        int count = setup().getObjectCount();
        oids = new Object[setup().getSelectCount()];
        begin();
        for ( int i = 1; i <= count; i++ ){
            storePilot(i);
        }
        commit();
    }

    public void retrieve() {
    	begin();
        for(Object id: oids) {
            JviPilot pilot=(JviPilot)(Object)db().loidToJod((Long)id);
            if(pilot==null) {
                System.err.println("Object not found by ID.");
            }else{
                addToCheckSum(pilot.getPoints());
            }
        }   
        commit();
    }

    private void storePilot(int idx) {
        JviPilot pilot = new JviPilot( "Pilot_" + idx, "Jonny_" + idx, idx , idx );
        db().makePersistent( pilot );
        
        if(idx <= setup().getSelectCount()) {
            oids[idx - 1] =  db().getOidAsLong(pilot);
        }
        if ( isCommitPoint(idx) ){
            db().commit();
            // JVI does automaticly a begin()
        }
    }

    private boolean isCommitPoint(int idx) {
        int commitInterval=setup().getCommitInterval();
        return commitInterval> 0  &&  idx%commitInterval==0 && idx<setup().getObjectCount();
    }


}

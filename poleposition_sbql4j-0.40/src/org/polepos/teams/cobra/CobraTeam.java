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

package org.polepos.teams.cobra;

import java.util.HashSet;
import java.util.Set;

import org.polepos.framework.Car;
import org.polepos.framework.DriverBase;
import org.polepos.framework.Team;

/**
 * @author Christian Ernst
 */
public class CobraTeam extends Team{
    
	private final Car[] mCars;
    
    public CobraTeam() {
        
    	Set<CobraCar> cars=new HashSet<CobraCar>();
            try {
                cars.add(new CobraCar(this, "Cobra", null));
            } catch (Exception e) {
                e.printStackTrace();
            } 
         mCars= new Car[cars.size()];
         cars.toArray(mCars);
        
    }
    
	
    @Override
	public String name(){
		return "Cobra";
	}

    @Override
    public String description() {
        return "the Cobra team";
    }

    @Override
	public Car[] cars(){
		return mCars;
	}
    
    public String databaseFile() {
    	// not supported yet
    	return null;
    }

    @Override
    public DriverBase[] drivers() {
        return new DriverBase[]{
            new MelbourneCobra(),
            new SepangCobra()
        };
    }
    
    @Override
    public String website() {
        return null;
    }


	@Override
    public void setUp() {
		
		// ??? Why is this all commented out?
		
		// Looks like someone has copied all the code from JDO but forgot to implement.
		// Let's log:
		
		System.err.println("CobraTeam#setup not implemented. It should delete all database files.");
		
		
//		for(int i = 0; i < mCars.length;i++){		
//			
//		    PersistenceManager pm = ((CobraCar)mCars[i]).getPersistenceManager();
//		    pm.currentTransaction().begin();
//		    pm.deletePersistentAll((Collection) pm.newQuery(pm.getExtent(JB0.class,true)).execute());
//		    pm.currentTransaction().commit();
//		    
//		    pm.currentTransaction().begin();
//		    pm.deletePersistentAll((Collection) pm.newQuery(pm.getExtent(JdoIndexedPilot.class,true)).execute());
//		    pm.currentTransaction().commit();
//		    
//		    pm.currentTransaction().begin();
//		    pm.deletePersistentAll((Collection) pm.newQuery(pm.getExtent(JdoPilot.class,true)).execute());
//		    pm.currentTransaction().commit();
//		    
//		    pm.currentTransaction().begin();
//		    pm.deletePersistentAll((Collection) pm.newQuery(pm.getExtent(JdoTree.class,true)).execute());
//		    pm.currentTransaction().commit();
//		    
//		    pm.currentTransaction().begin();
//		    pm.deletePersistentAll((Collection) pm.newQuery(pm.getExtent(JdoLightObject.class,true)).execute());
//		    pm.currentTransaction().commit();
//		    
//		    pm.currentTransaction().begin();
//		    pm.deletePersistentAll((Collection) pm.newQuery(pm.getExtent(JdoListHolder.class,true)).execute());
//		    pm.currentTransaction().commit();
//		    
//		    pm.currentTransaction().begin();
//		    pm.deletePersistentAll((Collection) pm.newQuery(pm.getExtent(JN1.class,true)).execute());
//		    pm.currentTransaction().commit();
//	    
//		}
	}

		
}

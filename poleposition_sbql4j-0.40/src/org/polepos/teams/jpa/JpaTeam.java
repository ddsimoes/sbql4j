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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.polepos.framework.Car;
import org.polepos.framework.DriverBase;
import org.polepos.framework.Team;
import org.polepos.teams.jpa.data.JPB0;
import org.polepos.teams.jpa.data.JPN1;
import org.polepos.teams.jpa.data.JpaIndexedPilot;
import org.polepos.teams.jpa.data.JpaLightObject;
import org.polepos.teams.jpa.data.JpaListHolder;
import org.polepos.teams.jpa.data.JpaPilot;
import org.polepos.teams.jpa.data.JpaTree;

/**
 * @author Christian Ernst
 */
public class JpaTeam extends Team{
    
	private final Car[] mCars;
    
    public JpaTeam() {
        
        String[] impls = Jpa.settings().getJpaImplementations();
        
        if(impls == null){
            System.out.println("No JPA engine configured.");
            mCars = new Car[0];
        }else{
        
            List <Car> cars = new ArrayList<Car>();
            
            for (String impl : impls) {
                
                String[] jdosqldbs = Jpa.settings().getJdbc(impl);
                
                if(jdosqldbs != null && jdosqldbs.length > 0){
                    for(String sqldb : jdosqldbs){
                        try {
                            cars.add(new JpaCar(this, impl, sqldb, Jpa.settings().color(impl)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    try {
                        cars.add(new JpaCar(this, impl, null, Jpa.settings().color(impl)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } 
                }
            }
            
            mCars = new Car[ cars.size() ];
            cars.toArray(mCars);
        }
        
    }
    
	
    @Override
	public String name(){
		return "JPA";
	}

    @Override
    public String description() {
        return "the JPA team";
    }

    @Override
	public Car[] cars(){
		return mCars;
	}
    
    @Override
    public DriverBase[] drivers() {
        return new DriverBase[]{
            new MelbourneJpa(),
            new SepangJpa(),
            new BahrainJpa(),
            new ImolaJpa(),
            new BarcelonaJpa(),
            new MonacoJpa(),
            new MontrealJpa(),
            new NurburgringJpa()
        };
    }
    
    @Override
    public String website() {
        return null;
    }
    
    public String databaseFile() {
    	// not supported yet
    	return null;
    }
    
    public void setUp() {
		for(int i = 0; i < mCars.length;i++){		
			
			EntityManager em = ((JpaCar)mCars[i]).getEntityManager();

		    em.getTransaction().begin();
		    em.createQuery("delete from "+ JPB0.class.getSimpleName() + " this ").executeUpdate();
		    em.getTransaction().commit();
		    
		    em.getTransaction().begin();
		    em.createQuery("delete from "+ JpaIndexedPilot.class.getSimpleName() + " this ").executeUpdate();
		    em.getTransaction().commit();
		    
		    em.getTransaction().begin();
		    em.createQuery("delete from "+ JpaPilot.class.getSimpleName() + " this ").executeUpdate();
		    em.getTransaction().commit();

		    em.getTransaction().begin();
		    em.createQuery("delete from "+ JpaTree.class.getSimpleName() + " this ").executeUpdate();
		    em.getTransaction().commit();

		    em.getTransaction().begin();
		    em.createQuery("delete from "+ JpaLightObject.class.getSimpleName() + " this ").executeUpdate();
		    em.getTransaction().commit();
		    
		    em.getTransaction().begin();
		    em.createQuery("delete from "+ JpaListHolder.class.getSimpleName() + " this ").executeUpdate();
		    em.getTransaction().commit();
		    
		    em.getTransaction().begin();
		    em.createQuery("delete from "+ JPN1.class.getSimpleName() + " this ").executeUpdate();
		    em.getTransaction().commit();
		    	    
		}
	}

	
}

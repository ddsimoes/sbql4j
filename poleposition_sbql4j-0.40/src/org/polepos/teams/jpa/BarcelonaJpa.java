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

import org.polepos.circuits.barcelona.BarcelonaDriver;
import org.polepos.teams.jpa.data.JPB4;


/**
 * @author Christian Ernst
 */
public class BarcelonaJpa extends JpaDriver implements BarcelonaDriver{
    
    public void write(){
        int count = setup().getObjectCount();
        begin();
        for (int i = 1; i<= count; i++) {
            JPB4 b4 = new JPB4();
            b4.setAll(i);
            store(b4);
        }
        commit();
    }
    
    public void read(){
        readExtent(JPB4.class);
    }
    
    public void query(){
    	begin();
        int count = setup().getSelectCount();
        String filter = "SELECT this FROM JPB4 this WHERE this.b2 = :param";
        for (int i = 1; i <= count; i++) {
            Query query = db().createQuery(filter);
            doQuery(query, i);
        }
        commit();
    }
    
    public void delete(){
        begin();
        Iterator it = db().createQuery("SELECT this FROM JPB4 this").getResultList().iterator();
        while(it.hasNext()){
            db().remove(it.next());
            addToCheckSum(5);
        }
        commit();
    }

}

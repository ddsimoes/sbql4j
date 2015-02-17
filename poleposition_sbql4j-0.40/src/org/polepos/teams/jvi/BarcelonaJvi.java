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

import org.polepos.circuits.barcelona.BarcelonaDriver;
import org.polepos.teams.jvi.data.JVB4;

import com.versant.trans.Query;
import com.versant.trans.QueryResult;


/**
 * @author Christian Ernst
 */
public class BarcelonaJvi extends JviDriver implements BarcelonaDriver{
    
    public void write(){
        int count = setup().getObjectCount();
        begin();
        for (int i = 1; i<= count; i++) {
            JVB4 b4 = new JVB4();
            b4.setAll(i);
            store(b4);
        }
        commit();
    }
    
    public void read(){
        readExtent(JVB4.class);
    }
    
    public void query(){
    	begin();
        int count = setup().getSelectCount();
        String filter = "SELECT selfoid FROM "+ JVB4.class.getName()+ " WHERE b2 = $param";
        for (int i = 1; i <= count; i++) {
            Query query = new Query(db(),filter);
            query.bind("param",i);
            doQuery(query);
        }
        commit();
    }
    
    public void delete(){
        begin();
        Query q = new Query(db(),"SELECT selfoid FROM "+ JVB4.class.getName());
        QueryResult result = q.execute();
        Object o = result.next();
        while(o != null){
            db().deleteObject(o);
            addToCheckSum(5);
            o = result.next();
        }
        q.close();
        commit();
    }

}

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

package org.polepos.teams.hibernate;

import org.hibernate.Transaction;
import org.polepos.circuits.nurburgring.NurburgringDriver;
import org.polepos.teams.hibernate.data.HN1;


public class NurburgringHibernate extends HibernateDriver implements NurburgringDriver{
    
	private final String FROM = "from org.polepos.teams.hibernate.data.HN1";

    public void write(){
        
        int numobjects = setup().getObjectCount();
        int commitinterval  = setup().getCommitInterval();
        int commitctr = 0;
        
        Transaction tx = db().beginTransaction();
        for ( int i = 1; i <= numobjects; i++ ){
            db().save(HN1.generate(i));
            
            if ( commitinterval > 0  &&  ++commitctr >= commitinterval ){
                commitctr = 0;
                tx.commit();
                tx = db().beginTransaction();
            }
            
            addToCheckSum(i);
        }
        tx.commit();
    }

    public void read(){
    	doQuery(FROM);
    }

}

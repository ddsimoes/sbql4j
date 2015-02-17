
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

package org.polepos.teams.jdo;

import org.polepos.circuits.montreal.MontrealDriver;
import org.polepos.teams.jdo.data.JdoListHolder;


/**
 * @author Christian Ernst
 */
public class MontrealJdo extends JdoDriver implements MontrealDriver {
    
	 public void write() {
	        
	        int count = 1000;
	        int elements = setup().getObjectSize();
	        
	        begin();
	        for (int i = 1; i<= count; i++) {
	            store(JdoListHolder.generate(i, elements));
	        }
	        commit();
	    }

	    public void read() {
	        readExtent(JdoListHolder.class);
	    }

}

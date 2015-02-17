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


package org.polepos.circuits.complex;

import org.polepos.framework.*;

@CircuitDescription("write, read, query, update and delete complex object graph")
public interface Complex {
	
    @Order(1)
    public void write();
    
    @Order(2)
    public void read();
    
    @Order(3)
    public void query();
    
    @Order(4)
    public void update();
    
    @Order(5)
    public void delete();

}

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

import org.polepos.framework.*;

import com.versant.odbms.ConnectionInfo;
import com.versant.odbms.ConnectionProperties;
import com.versant.odbms.DatastoreManager;
import com.versant.odbms.DatastoreManagerFactory;

/**
 * @author Christian Ernst
 */
public class CobraCar extends Car {

    private transient DatastoreManagerFactory mFactory;

    private final String              mDbName;
    private final String              mName;

    CobraCar(Team team, String name, String dbName) throws CarMotorFailureException {
    	super(team, "0xC86879");

        mName = name;
        mDbName = dbName;

        _website = "http://www.versant.com";
        _description = "Versant Object Database";

        initialize();

    }
    
    private void initialize() {

    	ConnectionInfo con = new ConnectionInfo("dbbench", "localhost", 5019, System.getProperty("user.name"),"");
    	mFactory = new DatastoreManagerFactory(con, new ConnectionProperties());

    }

    /**
     *
     */
    public DatastoreManager getDatastoreManager() {
        return mFactory.getDatastoreManager();
    }

	public String name() {
		return mName;
	}


}

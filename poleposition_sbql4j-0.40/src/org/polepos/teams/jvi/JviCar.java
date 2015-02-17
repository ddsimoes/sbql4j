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

import java.io.IOException;
import java.util.Properties;

import org.polepos.framework.*;

import com.versant.trans.TransSession;

/**
 * @author Christian Ernst
 */
public class JviCar extends Car {
	
	private static int id =0;
    private final String              _dbName;
    private final String              _name;

    public JviCar(Team team, String name, String dbName) throws CarMotorFailureException,  IOException{
    	super(team, "0xE68282");

        _name = name;
        _dbName = dbName;

        _website = Jvi.settings().getWebsite(name);
        _description = Jvi.settings().getDescription(name);

    }

    public void initialize() {
 
    }

    /**
     *
     */
    public TransSession getTransSession() {
    	Properties props = new Properties();
    	props.put("database", Jvi.settings().getConnectUrl(_name));
    	//props.put("sessionName", "polepos-" + new Random().nextInt(Integer.MAX_VALUE));
    	props.put("sessionName", "polepos-" + id++);
    	//props.put("options", Integer.toString(Constants.DONT_JOIN));
        return new TransSession(props);
        
    }
   
    @Override
    public String name() {
        
        return Jvi.settings().getVendor(_name) + "/" + Jvi.settings().getName(_name)+"-"+Jvi.settings().getVersion(_name);

    }

}

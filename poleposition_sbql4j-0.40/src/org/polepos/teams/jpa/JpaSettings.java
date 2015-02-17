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

import org.polepos.framework.RdbmsSettings;

/**
 * @author Christian Ernst
 */
public class JpaSettings extends RdbmsSettings{
    
    private final static String KEY_JPA = "jpa";
    private final static String KEY_ENHANCE = "enhance";
    private final static String KEY_ENHANCER = "enhancer";
    private final static String KEY_CONNECTURL = "javax.jdo.option.ConnectionURL";
	private final static String FILENAME = "settings/Jpa.properties";
	
	public JpaSettings(){
        super(FILENAME);
	}
    
    public String[] getJpaImplementations(){
        return getArray( KEY_JPA );
    }
	
	public String getConnectUrl(){
		return get( KEY_CONNECTURL );
	}
    
    public boolean enhance(){
        return getBoolean(KEY_ENHANCE);
    }
    
    public String enhancer(){
        return get(KEY_ENHANCER);
    }


}

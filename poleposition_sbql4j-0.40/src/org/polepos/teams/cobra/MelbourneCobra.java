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


import java.util.Arrays;

import org.polepos.circuits.melbourne.MelbourneDriver;
import org.polepos.teams.jdo.Log;
import org.polepos.teams.jdo.data.JdoPilot;

import com.versant.odbms.DataStoreLockMode;
import com.versant.odbms.DatastoreInfo;
import com.versant.odbms.DatastoreManager;
import com.versant.odbms.Options;
import com.versant.odbms.model.DatastoreObject;
import com.versant.odbms.model.DatastoreSchemaClass;
import com.versant.odbms.model.DatastoreSchemaField;
import com.versant.odbms.model.SchemaEditor;
import com.versant.odbms.query.DatastoreQuery;

/**
 * @author Christian Ernst
 */
public class MelbourneCobra extends CobraDriver implements MelbourneDriver{
	
	public void write(){
    	
        DatastoreManager dm = db();
		DatastoreInfo info = dm.getPrimaryDatastoreInfo();
		SchemaEditor editor = dm.getSchemaEditor();

		DatastoreSchemaClass dsc = editor.findClass(JdoPilot.class.getName(), info);
		
		DatastoreSchemaField dsfName = dsc.findField("mName");
		DatastoreSchemaField dsfPoints = dsc.findField("mPoints");
		
		begin();
        
        int numobjects = setup().getObjectCount();
        int commitinterval = setup().getCommitInterval();
		
		int commitctr = 0;
		
		for ( int i = 1; i <= numobjects; i++ ){

			DatastoreObject dos = new DatastoreObject(dm.getNewLoid(),dsc,info); 
            
			dos.allocate();
			dos.setIsNew(true);
			               
			dos.writeString(dsfName,new String("Pilot_" + i));
			dos.writeInt(dsfPoints, i);

			store(dos);
			
			if ( commitinterval > 0  &&  ++commitctr >= commitinterval ){
				commitctr = 0;
				commit();
				begin();
				Log.logger.fine( "commit while writing at " + i+1 ); //NOI18N
			}
            addToCheckSum(i);
		}
		
		commit();
	}
	
	public void read(){
		readExtent(JdoPilot.class,"mPoints");
	}
    
    public void read_hot() {
    	readExtent(JdoPilot.class,"mPoints");
    }
    
	public void delete(){
		
		DatastoreManager dm = db();
		
        int numobjects =setup().getObjectCount();
        int commitinterval = setup().getCommitInterval();

		begin();

		DatastoreQuery qry = new DatastoreQuery(JdoPilot.class.getName());
		Object[] loids = dm.executeQuery(qry, DataStoreLockMode.NOLOCK, DataStoreLockMode.NOLOCK, Options.NO_OPTIONS);

		for ( int i = 0; i < numobjects; i+=commitinterval ){
			
			DatastoreObject[] dsos = dm.getLoidsAsDSO(Arrays.copyOfRange(loids, i, i+commitinterval));
			dm.groupDeleteObjects(dsos, Options.NO_OPTIONS);
            commit();
			begin();
		}
		commit();
	}
}

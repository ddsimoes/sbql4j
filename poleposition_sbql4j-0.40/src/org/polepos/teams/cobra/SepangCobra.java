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
import org.polepos.circuits.sepang.SepangDriver;
import org.polepos.teams.jdo.data.JdoTree;

import com.versant.odbms.DataStoreLockMode;
import com.versant.odbms.DatastoreInfo;
import com.versant.odbms.DatastoreLoid;
import com.versant.odbms.DatastoreManager;
import com.versant.odbms.Options;
import com.versant.odbms.model.DatastoreObject;
import com.versant.odbms.model.DatastoreSchemaClass;
import com.versant.odbms.model.DatastoreSchemaField;
import com.versant.odbms.model.SchemaEditor;

/**
 * @author Christian Ernst
 */
public class SepangCobra extends CobraDriver implements SepangDriver {
    
    private long oid;
    
	private static long idGenerator;
	
    private DatastoreObject writeTree(int depth){
        idGenerator = 0;
        return writeTree(depth, 0);
    }
    
    private DatastoreObject writeTree(int maxDepth, int currentDepth){
        
        if(maxDepth <= 0){
            return null;
        }
        
        DatastoreManager dm = db();
		DatastoreInfo info = dm.getPrimaryDatastoreInfo();
		SchemaEditor editor = dm.getSchemaEditor();
		DatastoreSchemaClass dsc = editor.findClass(JdoTree.class.getName(), info);
		DatastoreSchemaField dsfName = dsc.findField("name");
		DatastoreSchemaField dsfId = dsc.findField("id");
		DatastoreSchemaField dsfDepth = dsc.findField("depth");
		DatastoreSchemaField dsfPreceding = dsc.findField("preceding");
		DatastoreSchemaField dsfSubsequent = dsc.findField("subsequent");
		
		DatastoreObject dos = new DatastoreObject(dm.getNewLoid(),dsc,info); 
 		dos.allocate();
		dos.setIsNew(true);

        if(currentDepth == 0){
        	dos.writeString(dsfName, "root");
        }else{
        	dos.writeString(dsfName, "node at depth " + currentDepth);
        }
        dos.writeLong(dsfId, ++idGenerator);
        dos.writeInt(dsfDepth,currentDepth);
        
        DatastoreObject preceding = writeTree(maxDepth - 1, currentDepth + 1);
        DatastoreObject subsequent = writeTree(maxDepth - 1, currentDepth + 1);
        
        if(preceding != null){
        	dos.writeLOID(dsfPreceding, preceding.getLOID());
        }
        if(subsequent != null){
        	dos.writeLOID(dsfSubsequent,subsequent.getLOID() );
        }
        
        store(dos);
        
        return dos;
    }

    private void readTree(DatastoreObject tree){
    	
    	DatastoreManager dm = db();
		DatastoreInfo info = dm.getPrimaryDatastoreInfo();
		SchemaEditor editor = dm.getSchemaEditor();
		DatastoreSchemaClass dsc = editor.findClass(JdoTree.class.getName(), info);
		DatastoreSchemaField dsfDepth = dsc.findField("depth");
		DatastoreSchemaField dsfPreceding = dsc.findField("preceding");
		DatastoreSchemaField dsfSubsequent = dsc.findField("subsequent");
    	
    	addToCheckSum(tree.readInt(dsfDepth));
    	
    	long loidPreceding = tree.readLOID(dsfPreceding);
    	long loidSubsequent = tree.readLOID(dsfSubsequent);
    	DatastoreObject preceding = null;
    	DatastoreObject subsequent = null;
    	
    	if(loidPreceding != 0){
    		preceding = db().getLoidsAsDSO(new DatastoreLoid[]{new DatastoreLoid(loidPreceding)})[0];
    		db().readObject(preceding, DataStoreLockMode.NOLOCK, Options.NO_OPTIONS);
        	readTree(preceding);
    	}
    	if(loidSubsequent != 0){
    		subsequent = db().getLoidsAsDSO(new DatastoreLoid[]{new DatastoreLoid(loidSubsequent)})[0];
    		db().readObject(subsequent, DataStoreLockMode.NOLOCK, Options.NO_OPTIONS);
        	readTree(subsequent);
    	}
    	
    }
    
	public void write(){
		begin();
        DatastoreObject tree = writeTree(setup().getDepth());
        oid = tree.getLOID();
		commit();
	}

	public void read(){
		
		DatastoreObject tree = db().getLoidsAsDSO(new DatastoreLoid[]{new DatastoreLoid(oid)})[0];
		db().readObject(tree, DataStoreLockMode.NOLOCK, Options.NO_OPTIONS);
		readTree(tree);
	}
    
    private void deleteTree(DatastoreObject tree){
    	
    	DatastoreManager dm = db();
		DatastoreInfo info = dm.getPrimaryDatastoreInfo();
		SchemaEditor editor = dm.getSchemaEditor();
		DatastoreSchemaClass dsc = editor.findClass(JdoTree.class.getName(), info);

		DatastoreSchemaField dsfPreceding = dsc.findField("preceding");
		DatastoreSchemaField dsfSubsequent = dsc.findField("subsequent");
    	
		long loidPreceding = tree.readLOID(dsfPreceding);
    	long loidSubsequent = tree.readLOID(dsfSubsequent);
    	DatastoreObject preceding = null;
    	DatastoreObject subsequent = null;
    	
    	if(loidPreceding != 0){
    		preceding = db().getLoidsAsDSO(new DatastoreLoid[]{new DatastoreLoid(loidPreceding)})[0];
    		db().readObject(preceding, DataStoreLockMode.NOLOCK, Options.NO_OPTIONS);
        	deleteTree(preceding);
    	}
    	if(loidSubsequent != 0){
    		subsequent = db().getLoidsAsDSO(new DatastoreLoid[]{new DatastoreLoid(loidSubsequent)})[0];
    		db().readObject(subsequent, DataStoreLockMode.NOLOCK, Options.NO_OPTIONS);
    		deleteTree(subsequent);
    	}
    	db().groupDeleteObjects(new DatastoreObject[]{tree}, Options.NO_OPTIONS);
    	
    	
    }
    
	public void delete(){
		begin();
		DatastoreObject tree = db().getLoidsAsDSO(new DatastoreLoid[]{new DatastoreLoid(oid)})[0];
		db().readObject(tree, DataStoreLockMode.NOLOCK, Options.NO_OPTIONS);
		deleteTree(tree);
		db().groupDeleteObjects(new DatastoreObject[]{tree}, Options.NO_OPTIONS);
		commit();
	}
    

}

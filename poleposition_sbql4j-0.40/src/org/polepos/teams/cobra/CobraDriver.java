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

import java.util.HashSet;
import java.util.Set;

import org.polepos.framework.Car;
import org.polepos.framework.CarMotorFailureException;
import org.polepos.framework.DriverBase;
import org.polepos.framework.TurnSetup;

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
public abstract class CobraDriver extends DriverBase{
    
	private transient DatastoreManager mDatastoreManager;
    private transient Set newObjects = new HashSet();
    
	public void takeSeatIn( Car car, TurnSetup setup) throws CarMotorFailureException{
        super.takeSeatIn(car, setup);
	}
    
	public void prepare(){
		mDatastoreManager = cobraCar().getDatastoreManager();
	}
	
	public void backToPit(){
        if(db().isTransactionActive()){
            db().commitTransaction();
        }
        mDatastoreManager.close();
        mDatastoreManager = null;
	}
	
	protected CobraCar cobraCar(){
		return (CobraCar)car();
	}
	
	protected DatastoreManager db(){
		return mDatastoreManager;
	}
    
    public void begin(){
        db().beginTransaction();
    }
    
    public void commit(){
        if(newObjects.size() != 0){
        	db().groupWriteObjects((DatastoreObject[])newObjects.toArray(new DatastoreObject[]{}),Options.NO_OPTIONS );
        }
        db().commitTransaction();
        newObjects.clear();
    }
    
    public void store(Object obj){
    	newObjects.add(obj);
    }
    
//    protected void doQuery( Query q, Object param){
//        Collection result = (Collection)q.execute(param);
//        Iterator it = result.iterator();
//        while(it.hasNext()){
//            Object o = it.next();
//            if(o instanceof CheckSummable){
//                addToCheckSum(((CheckSummable)o).checkSum());
//            }
//        }
//    }
//    
    protected void readExtent(Class clazz,String checksumField){
    	
    	DatastoreManager dm = db();
		DatastoreInfo info = dm.getPrimaryDatastoreInfo();
		SchemaEditor editor = dm.getSchemaEditor();

		DatastoreSchemaClass dsc = editor.findClass(clazz.getName(), info);
		DatastoreSchemaField dsfChecksum = dsc.findField(checksumField);
		
		dm.beginTransaction();

		DatastoreQuery qry = new DatastoreQuery(clazz.getName());

		Object[] loids = dm.executeQuery(qry, DataStoreLockMode.NOLOCK, DataStoreLockMode.NOLOCK, Options.NO_OPTIONS);

		DatastoreObject[] dsos = dm.getLoidsAsDSO(loids);

		dm.groupReadObjects(dsos, DataStoreLockMode.NOLOCK, Options.NO_OPTIONS);

		for(int i = 0; i < dsos.length;i++){
			addToCheckSum(dsos[i].readInt(dsfChecksum));
		}
		dm.rollbackTransaction();
    }
    
}

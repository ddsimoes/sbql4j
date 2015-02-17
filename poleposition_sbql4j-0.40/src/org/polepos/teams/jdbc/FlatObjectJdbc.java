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

package org.polepos.teams.jdbc;

import java.sql.*;

import org.polepos.circuits.flatobject.*;
import org.polepos.data.*;
import org.polepos.framework.*;


public class FlatObjectJdbc extends JdbcDriver implements FlatObject
{
    
    public static final int ID = 1;
    
    public static final int INT = 2;
    
    public static final int STRING = 3;
    
    
    

	public void takeSeatIn(Car car, TurnSetup setup) throws CarMotorFailureException {	
		super.takeSeatIn(car, setup);
		
        openConnection();
        dropTable("flatobject");
        
        // Sorry for the ugly field names. 
        // Starting each field name with an underscore, e.g. '_int' won't work with JavaDb.
        // Plain field names, e.g. id, int, string won't work with MySQL. 
        
        createTable(
        		"flatobject", 
        		new String[]{ "fid", "fint", "fstring"}, 
        		new Class[]{Integer.TYPE,Integer.TYPE, String.class}
        );
        
        createIndex( "flatobject", "fid" );
        createIndex( "flatobject", "fint" );
        createIndex( "flatobject", "fstring" );
        close();
	}

	public void write() throws SQLException{
        initializeTestId(objectCount());
        PreparedStatement preparedStatement = prepareStatement("insert into flatobject (fid, fint, fstring) values (?,?,?)");
        while ( hasMoreTestIds() ){
        	IndexedObject indexedObject = new IndexedObject(nextTestId());
        	preparedStatement.setInt(ID, indexedObject._int);
        	preparedStatement.setInt(INT, indexedObject._int);
        	preparedStatement.setString(STRING, indexedObject._string);
        	preparedStatement.addBatch();
        	if( doCommit()){
        		preparedStatement.executeBatch();
        		commit();
        	}
            addToCheckSum(indexedObject);
		}
	}
    
	public void queryIndexedString(){
		PreparedStatement stat = prepareStatement("select * from flatobject where fstring = ?");
        initializeTestId(setup().getSelectCount());
        while(hasMoreTestIds()) {
        	performIndexedObjectQuery(stat, IndexedObject.queryString(nextTestId()));
        }
        closePreparedStatement(stat);
    }

	public void queryIndexedInt(){
		PreparedStatement stat = prepareStatement("select * from flatobject where fint = ?");
        initializeTestId(setup().getSelectCount());
        while(hasMoreTestIds()) {
        	performIndexedObjectQuery(stat, nextTestId());
        }
        closePreparedStatement(stat);
    }

    public void update() throws SQLException{
        initializeTestId(setup().getUpdateCount());
        PreparedStatement selectStatement = prepareStatement("select * from flatobject where fid=?");
		PreparedStatement updateStatement = prepareStatement("update flatobject set fstring=? where fid=?");
        while(hasMoreTestIds()) {
        	selectStatement.setInt(1, nextTestId());
        	ResultSet rs = selectStatement.executeQuery();
        	rs.next();
        	IndexedObject indexedObject = new IndexedObject(rs.getInt(INT), rs.getString(STRING));
        	indexedObject.updateString();
        	updateStatement.setString(1, indexedObject._string);
        	updateStatement.setInt(2, indexedObject._int);
        	updateStatement.addBatch();
        	addToCheckSum(indexedObject);
        }
        updateStatement.executeBatch();
        closePreparedStatement(updateStatement);
        closePreparedStatement(selectStatement);
        commit();
	}
    
    public void delete() throws SQLException{
    	initializeTestId(setup().getUpdateCount());
    	PreparedStatement selectStatement = prepareStatement("select * from flatobject where fid=?");
    	PreparedStatement deleteStatement = prepareStatement("delete from flatobject where fid=?");
    	while(hasMoreTestIds()) {
			selectStatement.setInt(1, nextTestId());
    		ResultSet rs = selectStatement.executeQuery();
    		rs.next();
    		IndexedObject indexedObject = new IndexedObject(rs.getInt(INT), rs.getString(STRING));
    		deleteStatement.setInt(1, indexedObject._int);
    		deleteStatement.addBatch();
    		addToCheckSum(indexedObject);
    	}
    	deleteStatement.executeBatch();
    	closePreparedStatement(deleteStatement);
    	closePreparedStatement(selectStatement);
    	commit();
    }
    
    @Override
	public boolean supportsConcurrency() {
		return true;
	}
	
}

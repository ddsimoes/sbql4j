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





import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.polepos.circuits.bahrain.*;
import org.polepos.data.*;
import org.polepos.framework.*;
import org.polepos.teams.jdbc.drivers.melbourne.*;


public class BahrainJdbc extends JdbcDriver implements BahrainDriver
{
	/**
	 * Number of pilot to be written at once.
	 */
	private final static int BULKSIZE = 1000;
    
    public static final String TABLE = "bahrain";

	public void takeSeatIn(Car car, TurnSetup setup) throws CarMotorFailureException
	{	
		super.takeSeatIn(car, setup);

        openConnection();
        dropTable( TABLE);
        createTable( TABLE, new String[]{ "id", "Name", "FirstName", "Points", "LicenseID" }, 
					new Class[]{Integer.TYPE, String.class, String.class, Integer.TYPE, Integer.TYPE} );
        createIndex( TABLE, "Name" );
        createIndex( TABLE, "LicenseID" );

        close();
	}
	
	
    public void write()
	{
		Pilot[] pilots = new Pilot[ BULKSIZE ];
		int idx = 0;
		
		BulkWriteStrategy writer = new BulkWritePreparedStatement(this, TABLE);
	
        int commitctr = 0;
        int count = setup().getObjectCount();
        int commitInterval = setup().getCommitInterval();
        for ( int i = 1; i <= count; i++ ){
        
			pilots[ idx++ ] = new Pilot( "Pilot_" + i, "Jonny_" + i, i , i );
            
			if ( idx == BULKSIZE || i == count){
				writer.savePilots(TABLE, pilots, idx, i - idx + 1 );
				idx = 0;
			}
            
            if ( commitInterval > 0  &&  ++commitctr >= commitInterval ){
                commitctr = 0;
                commit();
            }
            
            addToCheckSum(i);
            
		}
		
        commit();
	}
    
    
    public void queryIndexedString(){
        int count = setup().getSelectCount();
    	PreparedStatement stat = prepareStatement("select * from bahrain where Name = ?");
        for (int i = 1; i <= count; i++) {
        	performPreparedQuery(stat, "Pilot_" + i);
        }
        closePreparedStatement(stat);
    }

	public void queryString(){
        int count = setup().getSelectCount();
    	PreparedStatement stat = prepareStatement("select * from bahrain where FirstName = ?");
        for (int i = 1; i <= count; i++) {
        	performPreparedQuery(stat, "Jonny_" + i);
        }
        closePreparedStatement(stat);
    }
    

    public void queryIndexedInt(){
        int count = setup().getSelectCount();
    	PreparedStatement stat = prepareStatement("select * from bahrain where LicenseID = ?");
        for (int i = 1; i <= count; i++) {
        	performPreparedQuery(stat, new Integer(i));
        }
        closePreparedStatement(stat);
    }

    
    public void queryInt(){
        int count = setup().getSelectCount();
    	PreparedStatement stat = prepareStatement("select * from bahrain where Points = ?");
        for (int i = 1; i <= count; i++) {
        	performPreparedQuery(stat, new Integer(i));
        }
        closePreparedStatement(stat);
    }
	
	/**
	 * Some JDBC implementations don't support ResultSet#updateRow(), so an alternative method can be used.
	 */
    public void update(){
        
        int updateCount = setup().getUpdateCount();
        
		updateIndexedStringStmt(updateCount);
	}

	/**
     * deleting one at a time, simulating deleting individual objects  
	 */
    public void delete(){
        
        int count = setup().getObjectCount();
        
        PreparedStatement statement = prepareStatement("delete from bahrain where id=?");
        
        try {
            for (int i = 1; i <= count; i++) {
                statement.setObject(1,i);
                statement.execute();
                addToCheckSum(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        commit();		
    }
	
	/**
	 * do the update using the ResultSet#updateRow() method
	 */
//	private void updateIndexedStringUpdateRow(int updateCount)
//	{
//        try{
//            ResultSet rs = jdbcCar().executeQueryForUpdate( "select ID, Name from bahrain" );
//
//            for (int i = 1; i <= updateCount ; i++) {
//                rs.next();
//				rs.updateString( 2, rs.getString( 2 ).toUpperCase() );
//				rs.updateRow();
//                addToCheckSum(1);
//            }
//        }
//        catch ( SQLException sqlex ){
//            sqlex.printStackTrace();
//        }
//        jdbcCar().commit();		
//	}
	
	
	/**
	 * do the update using an 'update' SQL statement
	 */
	private void updateIndexedStringStmt(int updateCount) {
		JdbcCar car = jdbcCar();
		ResultSetStatement resultSetStatement = null;
		try {
			PreparedStatement stmt = prepareStatement("update bahrain set Name=? where ID=?");
			try {
				resultSetStatement = executeQuery("select ID, Name from bahrain");
				ResultSet rs = resultSetStatement._resultSet;
				for (int i = 0; i < updateCount; i++) {
					rs.next();
					int id = rs.getInt(1);
					String name = rs.getString(2).toUpperCase();
					stmt.setString(1, name);
					stmt.setInt(2, id);
					stmt.addBatch();
					addToCheckSum(1);
				}
			} finally {
				closeQuery(resultSetStatement);
			}
			stmt.executeBatch();
			stmt.close();
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
		commit();
	}

	
	
}

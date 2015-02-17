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

package org.polepos.teams.jdbc.drivers.melbourne;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.polepos.data.*;
import org.polepos.teams.jdbc.*;

public class BulkWritePreparedStatement implements BulkWriteStrategy
{
	private final PreparedStatement mStmt;
	
	private final JdbcDriver _driver;

	/** 
	 * Creates a new instance of BulkWriteSingle.
	 */
	public BulkWritePreparedStatement( JdbcDriver driver, String tablename )
	{
		_driver = driver;
		mStmt = driver.prepareStatement( "insert into " + tablename + " (id,Name,FirstName,Points,LicenseID) values (?,?,?,?,?)" );
	}

	/**
	 * Dump an array of pilots to the DB by writing one-by-one.
	 */
	public void savePilots(String tablename, Pilot[] p, int count, int index )
	{
		try
		{
			for ( int i = 0; i < count; i++ )
			{
				savePilot(p[i], index++ );
			}		
			
			if ( count > 0 )
			{
				mStmt.executeBatch();
			}
		}
		catch ( SQLException sqlex )
		{
            sqlex.printStackTrace();			
		}
	}
	
	
    /**
	 * Helper:
     * Write a single pilot to the database.
     */
    private void savePilot( Pilot p, int index ) throws SQLException
    {
		mStmt.setInt(		1, index );
		mStmt.setString(	2, p.getName() );
		mStmt.setString(	3, p.getFirstName() );
		mStmt.setInt(		4, p.getPoints() );
		mStmt.setInt(		5, p.getPoints() );
		mStmt.addBatch();
    }
}

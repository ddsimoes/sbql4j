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
import java.util.*;

import org.polepos.*;
import org.polepos.data.*;
import org.polepos.framework.*;


public abstract class JdbcDriver extends org.polepos.framework.DriverBase {
	
	private Connection _connection;
	
	public void prepare() throws CarMotorFailureException{
		openConnection();
	}
	
	public void backToPit(){
        close();
	}
    
    public JdbcCar jdbcCar(){
        return (JdbcCar)car();
    }
    
	protected void performQuery(String sql) {
		Log.logger.fine("starting query"); // NOI18N
		ResultSetStatement resultSetStatement = null;
		try {
			resultSetStatement = executeQuery(sql);
			iteratePilotResult(resultSetStatement._resultSet);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		} finally {
			closeQuery(resultSetStatement);
		}
	}
	
	protected void performIndexedObjectQuery(PreparedStatement stat, Object arg) {
		ResultSet rs = null;
		try {
			stat.setObject(1, arg);
			rs = stat.executeQuery();
			iterateIndexedObjectResult(rs);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		} finally {
			closeResultSet(rs);
		}
	}

	protected void performPreparedQuery(PreparedStatement stat, Object arg) {
		Log.logger.fine("starting query"); // NOI18N
		ResultSet rs = null;
		try {
			stat.setObject(1, arg);
			rs = stat.executeQuery();
			iteratePilotResult(rs);
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		} finally {
			closeResultSet(rs);
		}
	}
	
	private void iterateIndexedObjectResult(ResultSet rs) throws SQLException {
		while (rs.next()) {
			IndexedObject indexedObject = new IndexedObject(rs.getInt(FlatObjectJdbc.INT), rs.getString(FlatObjectJdbc.STRING));
			addToCheckSum(indexedObject);
		}
	}

	private void iteratePilotResult(ResultSet rs) throws SQLException {
		while (rs.next()) {
			Pilot p = new Pilot(rs.getString(2), rs.getString(3), rs
					.getInt(4), rs.getInt(5));
			addToCheckSum(p.checkSum());
		}
	}
	
	protected <Value> void performSingleResultQuery(String sql,List<Value> values) {
	    Log.logger.fine( "starting query" ); //NOI18N
	    PreparedStatement stat=prepareStatement(sql);
		try {
			for(Value val : values) {
				stat.setObject(1,val);
				ResultSet rs=stat.executeQuery();
				if(!rs.next()) {
					System.err.println("Expected one result, received none: "+val);
				}
			    Pilot p = new Pilot( rs.getString( 2 ), rs.getString( 3 ), rs.getInt( 4 ), rs.getInt( 5 ) );
                addToCheckSum(p.checkSum());
				if(rs.next()) {
					System.err.println("Expected one result, received multiple: "+val);
				}
			}
		} catch (SQLException sqlexc) {
	        sqlexc.printStackTrace();
		}
		finally {
			try {
				stat.close();
			} catch (SQLException e) {
				handleException(e);
			}
		}
	}
	
    protected void closePreparedStatement(PreparedStatement stat) {
    	try {
			stat.close();
		} catch (SQLException e) {
			handleException(e);
		}
	}

	private void handleException(Exception e) {
		if(Settings.DEBUG){
			throw new RuntimeException(e);
		}
		e.printStackTrace();
	}

	
    @Override
	public boolean supportsConcurrency() {
    	
    	// Actually concurrency is not a problem in the JDBC driver
    	// it's just that the circuits were written in a way that
    	// primary keys are not generated multithreading-safe.
    	
		return false;
	}
    
	
	public void openConnection() throws CarMotorFailureException {

		try {
			assert null == _connection : "database has to be closed before opening";
			JdbcSettings jdbcSettings = Jdbc.settings();
			
			Properties props = new Properties();
			String username = jdbcSettings.getUsername(jdbcCar()._dbType);
			if(username != null){
				props.put("user", username);
			}
			String password = jdbcSettings.getPassword(jdbcCar()._dbType);
			if(password != null){
				props.put("password", password);
			}
			
			// If we don't use this setting, HSQLDB will hold all tables
			// in memory completely, which is not what other engines do.
			props.put("hsqldb.default_table_type", "cached");
			
			
			_connection = DriverManager.getConnection(jdbcSettings.getConnectUrl(jdbcCar()._dbType), props);
			_connection.setAutoCommit(false);
			
			if(isHsqlDb()){
				JdbcCar.hsqlDbWriteDelayToZero(_connection);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CarMotorFailureException();
		}
	}

	private boolean isHsqlDb() {
		return "hsqldb".equals(jdbcCar()._dbType);
	}
	
	private boolean isPostgres() {
		return "postgresql".equals(jdbcCar()._dbType);
	}
	
	public void close() {
		if(_connection == null) {
			return;
		}
		commit();
		closeConnection();
	}

	private void closeConnection() {
		try {
			_connection.close();
		} catch (SQLException sqlex) {
			handleException(sqlex);
		}
		_connection = null;
	}

	public void commit() {
		try {
			_connection.commit();
		} catch (SQLException ex) {
			handleException(ex);
		}
	}

	public void executeSQL(String sql) {
		Statement statement = null;
		try {
			statement = _connection.createStatement();
			statement.execute(sql);
		} catch (SQLException ex) {
			handleException(ex);
		} 
		closeStatement(statement);
	}
	public ResultSetStatement executeQuery(String sql) {
		Log.logger.fine(sql);
		try {
			Statement statement = _connection.createStatement();
			return new ResultSetStatement(statement, statement.executeQuery(sql)) ;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	public void closeQuery(ResultSetStatement resultSetStatement) {
		if(resultSetStatement != null){
			resultSetStatement.close();
		}
	}

	public void closeResultSet(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				handleException(e);
			}
		}
	}

	public void executeUpdate(String sql) {
		Statement statement = null;
		try {
			statement = _connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(statement);
		}
	}

	private void closeStatement(Statement statement) {
		if(statement == null){
			return;
		}
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void dropTable(String tablename) {
		
		// TODO: "drop table if exists" is nonstandard.
		// A better approach would be to look in the catalog and
		// to delete a table only if it can be found there.
		
		Statement statement = null;
		String sql = "drop table if exists " + tablename;
		
		try {
			statement = _connection.createStatement(); 
			statement.executeUpdate(sql);
			return;
		} catch (SQLException e) {
			System.out.println("SQL dialect not supported: 'drop table if exists'. Trying plain 'drop table'");
		} finally {
			closeStatement(statement);
		}
		
		sql = "drop table " + tablename;
		
		try {
			statement = _connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Table could not be dropped: " + tablename);
		} finally {
			closeStatement(statement);
		}
		
	}
	
	public void createTable(String tablename, String[] colnames, Class[] coltypes) {
		createTable(tablename, colnames, coltypes, colnames[0]);
	}
	
	public void createTable(String tablename, String[] colnames, Class[] coltypes, String primaryKey) {
		String sql = "create table " + tablename + " (" + colnames[0]
				+ " INTEGER NOT NULL";

		for (int i = 1; i < colnames.length; i++) {
			sql += ", " + colnames[i] + " " + JdbcCar.colTypesMap.get(coltypes[i]);
		}
		if(primaryKey != null){
			sql += ", PRIMARY KEY(" + primaryKey + ")";
		} 
		sql += ")";
		executeSQL(sql);
		commit();
	}

	
	public void dropIndex(String tablename, String colname){
		Statement statement = null;
		
		String sql = isPostgres() 
			?
			"DROP INDEX " + indexName(tablename, colname) + " CASCADE"
			:
			"DROP INDEX " + indexName(tablename, colname) + " ON " + tablename;
		
		try {
			statement = _connection.createStatement(); 
			statement.executeUpdate(sql);
			return;
		} catch (SQLException e) {
			System.out.println(e.getSQLState());
		} finally {
			closeStatement(statement);
		}
	}

	public void createIndex(String tablename, String colname) {
		// The maximum length for index names is 18 for Derby.
		String indexName = indexName(tablename, colname);
		indexName = indexName.replace(',', '_');
		String sql = "CREATE INDEX " + indexName + " ON "
				+ tablename + " (" + colname + ")";
		executeSQL(sql);
	}
	

	private String indexName(String tablename, String colname) {
		return "X" + tablename + "_" + colname;
	}

	public PreparedStatement prepareStatement(String sql) {
		PreparedStatement stmt = null;
		try {
			stmt = _connection.prepareStatement(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return stmt;
	}

}

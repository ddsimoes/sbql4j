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

import org.polepos.framework.*;

public class JdbcCar extends Car {

	public final String _dbType;
	
	private String _name;
	
	public static final Map<Class, String> colTypesMap = new HashMap<Class, String>();

	static {
		colTypesMap.put(String.class, "VARCHAR(100)");
		colTypesMap.put(Integer.TYPE, "INTEGER");
	}

	public JdbcCar(Team team, String dbtype, String color) throws CarMotorFailureException {
		super(team, color);
		_dbType = dbtype;
		JdbcSettings jdbcSettings = Jdbc.settings();
		_website = jdbcSettings.getWebsite(dbtype);
		_description = jdbcSettings.getDescription(dbtype);
		_name = jdbcSettings.getName(dbtype);

		try {
			Class.forName(jdbcSettings.getDriverClass(dbtype)).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new CarMotorFailureException();
		}
	}

	public String name() {
		if (_name != null) {
			return _name;
		}
		return _dbType;
	}


	public static void hsqlDbWriteDelayToZero(Connection connection) {
		
		// To be fair to other database engines, commits should
		// be ACID. Especially the "D" (durable) is not satisfied
		// if #sync() of the database file runs in a timer instead
		// of directly after a commit call.
		
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate("SET WRITE_DELAY 0");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

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

import org.polepos.circuits.inheritancehierarchy.*;
import org.polepos.data.*;
import org.polepos.framework.*;



public class InheritanceHierarchyJdbc extends JdbcDriver implements InheritanceHierarchy {
    
    private static final String[] TABLES = new String[]{
        "inheritanceHierarchyJdbc0",
        "inheritanceHierarchyJdbc1",
        "inheritanceHierarchyJdbc2",
        "inheritanceHierarchyJdbc3",
        "inheritanceHierarchyJdbc4",
    };
    
    public void takeSeatIn(Car car, TurnSetup setup) throws CarMotorFailureException{
        
        super.takeSeatIn(car, setup);
        openConnection();
        
        for (int i = TABLES.length - 1; i >= 0; i--) {
        	String table = TABLES[i];
        	dropTable( table);
        	createTable( table, new String[]{ "id", "parent", "i" + i}, 
        			new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE} );
        	createIndex( table, "parent" );
        	if(i == 2){
        		createIndex( table, "i2" );
        	}
		}
        
        close();

    }


    @Override
    public void write() {
        
        try{
            PreparedStatement[] statements = new PreparedStatement[5];
            for (int i = 0; i < 5; i++) {
                statements[i] = prepareStatement("insert into " + TABLES[i] + " (id, parent, i" + i + ") values (?,?,?)");
            }
            int count = setup().getObjectCount();
			for (int j = 0; j < 5; j++) {
				for (int i = 1; i <= count; i++) {
					
					InheritanceHierarchy4 inheritanceHierarchy4 = new InheritanceHierarchy4();
					inheritanceHierarchy4.setAll(i);
					statements[j].setInt(1, i);
					statements[j].setInt(2, i);
					statements[j].setInt(3, inheritanceHierarchy4.getIx(j));
					statements[j].addBatch();
				}
				statements[j].executeBatch();
				statements[j].close();
			}
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
		}
        
        commit();     
        
    }

    @Override
    public void read() {
        StringBuffer sql = select();
        sql.append(TABLES[0]);
        sql.append(".id=?");
        query(sql.toString(), setup().getObjectCount());
    }


    @Override
    public void query() {
        StringBuffer sql = select();
        sql.append(TABLES[2]);
        sql.append(".i2=?");
        query(sql.toString(), setup().getSelectCount());
    }

    /**
     * deleting one at a time, simulating deleting individual objects  
     */
    @Override
    public void delete(){
        
        int count = setup().getObjectCount();
        
        PreparedStatement[] statements = new PreparedStatement[5];
        for (int i = 0; i < 5; i++) {
            statements[i] = prepareStatement("delete from " + TABLES[i] + " where id=?");
        }
        
        try {
			for (int j = 0; j < 5; j++) {
				for (int i = 1; i <= count; i++) {
					statements[j].setInt(1, i);
					addToCheckSum(1);
					statements[j].addBatch();
				}
				statements[j].executeBatch();
				statements[j].close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        commit();     
    }
    
    private StringBuffer select(){
        StringBuffer sql = new StringBuffer("select * from ");
        sql.append(TABLES[0]);
        for (int i = 1; i < TABLES.length; i++) {
            sql.append(", ");
            sql.append(TABLES[i]);
        }
        sql.append(" where ");
        for (int i = 1; i < TABLES.length; i++) {
            sql.append(TABLES[i]);
            sql.append(".parent = ");
            sql.append(TABLES[i - 1]);
            sql.append(".id");
            sql.append(" and ");
        }
        return sql;
    }
    
    private void query(String sql, int count){
        PreparedStatement statement = prepareStatement(sql.toString());
        
        try {
            for(int i = 1 ; i <= count; i++) {
                statement.setInt(1,i);
                ResultSet rs=statement.executeQuery();
                if(!rs.next()) {
                    System.err.println("Expected one result, received none: "+ i);
                }
                InheritanceHierarchy4 ih4 = new InheritanceHierarchy4(rs.getInt(3), rs.getInt(6), rs.getInt(9), rs.getInt(12), rs.getInt(15));
                addToCheckSum(ih4.checkSum());
                if(rs.next()) {
                    System.err.println("Expected one result, received multiple: "+i);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
   

}

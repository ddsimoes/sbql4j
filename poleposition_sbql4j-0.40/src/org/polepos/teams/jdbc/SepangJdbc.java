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

import org.polepos.circuits.sepang.*;
import org.polepos.framework.*;

public class SepangJdbc extends JdbcDriver implements SepangDriver{
	
	private static final String TABLE = "malaysia";
    
	public void takeSeatIn(Car car, TurnSetup setup) throws CarMotorFailureException{
		super.takeSeatIn(car, setup);
		openConnection();
		dropTable( TABLE );
		createTable( TABLE, new String[]{ "id", "preceding", "subsequent", "name", "depth" }, new Class[]{Integer.TYPE,Integer.TYPE,Integer.TYPE,String.class, Integer.TYPE} );
		createIndex( TABLE, "id" );
		close();
	}
	
	public void write(){
		final PreparedStatement statement = prepareStatement("insert into " + TABLE + " (id, preceding, subsequent, name, depth ) values (?,?,?,?,?)");
        Tree tree = Tree.createTree(setup().getDepth());
        Tree.traverse(tree, new TreeVisitor() {
            public void visit(Tree tree) {
                try {
					statement.setInt(1, tree.id);
					statement.setInt(2, tree.preceding == null ? 0 : tree.preceding.id);
					statement.setInt(3, tree.subsequent == null ? 0 : tree.subsequent.id);
					statement.setString(4, tree.name);
					statement.setInt(5, tree.depth);
					statement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
        });
		commit();
	}

	
	public void read(){
        try {
        	PreparedStatement preparedStatement = prepareStatement("select * from " + TABLE + " where id = ?");
            Tree root = read(preparedStatement, 1);
            Tree.traverse(root, new TreeVisitor() {
                public void visit(Tree tree) {
                    addToCheckSum(tree.getDepth());
                }
            });
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
    
    private Tree read(PreparedStatement preparedStatement, int id) throws SQLException {
        ResultSet rs = null;
        
		int precedingID;
		int subsequentID;
		
		Tree tree = null;
		try {
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			rs.next();
			tree = new Tree(rs.getInt(1), rs.getString(4), rs.getInt(5));
			precedingID = rs.getInt(2);
			subsequentID = rs.getInt(3);
		} finally {
			closeResultSet(rs);
		}
		
        if(precedingID > 0){
            tree.preceding = read(preparedStatement, precedingID);
        }
        if(subsequentID > 0){
            tree.subsequent = read(preparedStatement, subsequentID);
        }
        return tree;
    }
    
	public void delete(){
		try{
			PreparedStatement selectStatement = prepareStatement("select * from " + TABLE + " where id=?");
			PreparedStatement deleteStatement = prepareStatement("delete from " + TABLE + " where id=?");
			try{
				delete(1, selectStatement, deleteStatement);
				commit();
			}
			finally{
				selectStatement.close();
				deleteStatement.close();
			}
		}catch ( SQLException sqlex ){ 
            sqlex.printStackTrace(); 
        }
	}
    
    private void delete(int id, PreparedStatement selectStatement, PreparedStatement deleteStatement) throws SQLException{
		ResultSet rs = null;
		int precedingID;
		int subsequentID;
		try {
			selectStatement.setInt(1, id);
			rs = selectStatement.executeQuery();
			rs.next();
			precedingID = rs.getInt(2);
			subsequentID = rs.getInt(3);
		} finally {
			closeResultSet(rs);
		}
        if(precedingID > 0){
            delete(precedingID, selectStatement, deleteStatement);
        }
        if(subsequentID > 0){
            delete(subsequentID, selectStatement, deleteStatement);
        }
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
    }

}

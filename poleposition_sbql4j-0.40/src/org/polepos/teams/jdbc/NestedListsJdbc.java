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

import org.polepos.circuits.nestedlists.*;
import org.polepos.data.*;
import org.polepos.framework.*;

public class NestedListsJdbc extends JdbcDriver implements NestedLists {
	
	private static final String LISTHOLDER_TABLE = "listholderJdbc";
	
	private static final String LIST_TABLE = "listJdbc";
	
    public static final int ID = 1;
    
    public static final int NAME = 2;
    
    public static final int ITEM = 2;
    
    public static final int ELEMENT = 3;
    
    
    private int _rootId;


	public void takeSeatIn(Car car, TurnSetup setup) throws CarMotorFailureException{
        
        super.takeSeatIn(car, setup);
        openConnection();
        
        dropTable(LISTHOLDER_TABLE);
        dropTable(LIST_TABLE);
        
        commit();
        
        createTable(LISTHOLDER_TABLE, 
        		new String[]{ "fid", "fname"}, 
                new Class[] {Integer.TYPE, String.class} );
        
        createTable(LIST_TABLE, 
        		new String[]{ "fid", "fitem", "felement"}, 
                new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE},
                null);

        createIndex(LIST_TABLE, "fid");
        createIndex(LIST_TABLE, "fid,felement");
        commit();
        
        close();

    }
	

	@Override
	public void create() throws Throwable {
		
		final PreparedStatement listHolderStatement = 
			prepareStatement("insert into " + LISTHOLDER_TABLE + " (fid, fname) values (?,?)");
		
		final PreparedStatement listStatement = 
			prepareStatement("insert into " + LIST_TABLE + " (fid, fitem, felement) values (?,?,?)");

		ListHolder root = ListHolder.generate(depth(), objectCount(), reuse());
		_rootId = (int) root.id();
		
		root.accept(new Visitor<ListHolder>() {
			@Override
			public void visit(ListHolder listHolder) {
				try {
					int listHolderId = (int) listHolder.id();
					listHolderStatement.setInt(ID, listHolderId);
					listHolderStatement.setString(NAME, listHolder.name());
					listHolderStatement.addBatch();
					List<ListHolder> list = listHolder.list();
					if(list != null && ! list.isEmpty()){
						int position = 0;
						for (ListHolder child : list) {
							listStatement.setInt(ID, listHolderId);
							listStatement.setInt(ITEM, (int) child.id());
							listStatement.setInt(ELEMENT, position++);
							listStatement.addBatch();
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
    	
    	listStatement.executeBatch();
    	listHolderStatement.executeBatch();
    	listStatement.close();
    	listHolderStatement.close();
    	commit();

	}
	
	@Override
	public void read() throws Throwable {
		ListHolder root = root();
		root.accept(new Visitor<ListHolder>(){
			public void visit(ListHolder listHolder){
				addToCheckSum(listHolder);
			}
		});
	}

	private ListHolder root() throws SQLException {
		PreparedStatement listHolderStatement = prepareStatement("select * from " + LISTHOLDER_TABLE + " where fid = ?");
		PreparedStatement listStatement = prepareStatement("select * from " + LIST_TABLE + " where fid = ? order by felement");
		Set<ListHolder> found = new HashSet<ListHolder>();
		ListHolder root = recurseRead(listHolderStatement, listStatement, _rootId, found);
		closePreparedStatement(listHolderStatement);
		closePreparedStatement(listStatement);
		return root;
	}

	private ListHolder recurseRead(PreparedStatement listHolderStatement,
			PreparedStatement listStatement, int id, Set<ListHolder> found) throws SQLException {
		listHolderStatement.setInt(ID, id);
		ResultSet listHolderResultSet = listHolderStatement.executeQuery();
		listHolderResultSet.next();
		ListHolder listHolder = new ListHolder();
		listHolder.id(id);
		if(found.contains(listHolder)){
			return listHolder;
		}
		found.add(listHolder);
		listHolder.name(listHolderResultSet.getString(NAME));
		listHolderResultSet.close();
		listStatement.setInt(ID, id);
		
		ResultSet listResultSet = listStatement.executeQuery();
		List<Integer> ids = new ArrayList<Integer>(); 
		if(listResultSet.next()){
			do{
				ids.add(listResultSet.getInt(ITEM));
			} while(listResultSet.next());
			listResultSet.close();
			List <ListHolder> list = new ArrayList<ListHolder>();
			for (Integer childId : ids) {
				list.add(recurseRead(listHolderStatement, listStatement, childId, found));
			}
			listHolder.list(list);
		}
		return listHolder;
	}

	@Override
	public void update() throws Throwable {
		ListHolder root = root();
		final PreparedStatement statement = prepareStatement("update " + LISTHOLDER_TABLE + " set fname=? where fid=?");
		addToCheckSum(root.update(depth(), 0,  new Procedure<ListHolder>() {
			@Override
			public void apply(ListHolder obj) {
				try {
					statement.setString(1, obj.name());
					statement.setInt(2, (int)obj.id());
					statement.addBatch();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}));
		statement.executeBatch();
		statement.close();
		commit();
	}

	@Override
	public void delete() throws Throwable {
		ListHolder root = root();
		final PreparedStatement statement = prepareStatement("delete from " + LISTHOLDER_TABLE + " where fid=?");
		addToCheckSum(root.delete(depth(), 0, new Procedure<ListHolder>() {
			@Override
			public void apply(ListHolder obj) {
				try {
					statement.setInt(1, (int)obj.id());
					statement.addBatch();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}));
		statement.executeBatch();
		statement.close();
		commit();
	}

}

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

import org.polepos.circuits.complex.*;
import org.polepos.data.*;
import org.polepos.framework.*;

import com.db4o.*;
import com.db4o.query.*;

public class ComplexJdbc extends JdbcDriver implements Complex {
	
	private static final int ROOT_ID = 1; 
	
	private static final String HOLDER_TABLE0 = "complexHolderJdbc0";
	
    private static final String[] HOLDER_TABLES = new String[]{
        "complexHolderJdbc1",
        "complexHolderJdbc2",
        "complexHolderJdbc3",
        "complexHolderJdbc4",
    };
    
    private static final String CHILDREN_TABLE = "children";
    
    private static final String ARRAY_TABLE = "tarray";
    
    private static final int ID = 1;
    
    private static final int NAME = 2;
    
    private static final int TYPE = 3;
    
    private static final int INT_FIELD = 2;
    
    private static final int CHILD = 2;
    
    private static final int POS = 3;
    
    private IdGenerator _idGenerator;
    
    public void takeSeatIn(Car car, TurnSetup setup) throws CarMotorFailureException{
        
        super.takeSeatIn(car, setup);
        openConnection();
        
        dropTable(HOLDER_TABLE0);
        dropTable(CHILDREN_TABLE);
        dropTable(ARRAY_TABLE);
        
        createTable( HOLDER_TABLE0, new String[]{ "id", "name", "type" }, 
                new Class[]{Integer.TYPE, String.class, Integer.TYPE} );
        
        createTable(CHILDREN_TABLE, 
        		new String[]{ "parent", "child", "pos"}, 
                new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE},
                null);
        createIndex( CHILDREN_TABLE, "parent" );
        createIndex( CHILDREN_TABLE, "child" );
        createIndex( CHILDREN_TABLE, "pos" );
        
        createTable(ARRAY_TABLE, 
        		new String[]{ "parent", "child", "pos"}, 
                new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE},
                null);
        createIndex( ARRAY_TABLE, "parent" );
        createIndex( ARRAY_TABLE, "child" );
        createIndex( ARRAY_TABLE, "pos" );
        
        int i = 1;
        for(String table : HOLDER_TABLES){
            dropTable( table);
            createTable( table, new String[]{ "id", "i" + i}, 
                        new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE} );
            if(i == 2){
                createIndex( table, "i2" );
            }
            i++;
        }
        close();

    }
    
	@Override
	public void write() {
		_idGenerator = new IdGenerator(ROOT_ID);
		final Stack<Integer> parentIds = new Stack<Integer>();
		
		final PreparedStatement complexHolder0Stat = prepareStatement("insert into " + HOLDER_TABLE0 + " (id, name, type) values (?,?,?)");
		final PreparedStatement[] complexHolderStats = new PreparedStatement[4];
		for (int i = 0; i < complexHolderStats.length; i++) {
			String table = HOLDER_TABLES[i];
			int idx = i + 1;
			complexHolderStats[i] = prepareStatement("insert into " + table + "(id, i" +  idx + ") values (?,?)"); 
		}
		final PreparedStatement arrayStat = prepareStatement("insert into tarray (parent, child, pos) values (?,?,?)");
		final PreparedStatement childrenStat = prepareStatement("insert into children (parent, child, pos) values (?,?,?)");
		
		final Map<ComplexHolder0,Integer> ids = new HashMap<ComplexHolder0, Integer>();
		ComplexHolder0 holder = ComplexHolder0.generate(depth(), objectCount());
		addToCheckSum(holder);
		holder.traverse(new Visitor<ComplexHolder0>() {
			@Override
			public void visit(ComplexHolder0 holder) {
				int id = (int) _idGenerator.nextId();
				ids.put(holder, id);
				try {
					int type = 0;
					
					if(holder instanceof ComplexHolder1){
						type=1;
						complexHolderStats[0].setInt(1, id);
						ComplexHolder1 complexHolder1 = (ComplexHolder1) holder;
						complexHolderStats[0].setInt(2, complexHolder1._i1);
						complexHolderStats[0].addBatch();
					}
					
					if(holder instanceof ComplexHolder2){
						type=2;
						complexHolderStats[1].setInt(1, id);
						ComplexHolder2 complexHolder2 = (ComplexHolder2) holder;
						complexHolderStats[1].setInt(2, complexHolder2._i2);
						complexHolderStats[1].addBatch();
					}
					
					if(holder instanceof ComplexHolder3){
						type=3;
						complexHolderStats[2].setInt(1, id);
						ComplexHolder3 complexHolder3 = (ComplexHolder3) holder;
						complexHolderStats[2].setInt(2, complexHolder3._i3);
						complexHolderStats[2].addBatch();
					}
					
					if(holder instanceof ComplexHolder4){
						type =4;
						complexHolderStats[3].setInt(1, id);
						ComplexHolder4 complexHolder4 = (ComplexHolder4) holder;
						complexHolderStats[3].setInt(2, complexHolder4._i4);
						complexHolderStats[3].addBatch();
					}
					
					complexHolder0Stat.setInt(ID, id);
					complexHolder0Stat.setString(NAME, holder.getName());
					complexHolder0Stat.setInt(TYPE, type);
					complexHolder0Stat.addBatch();


					
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				parentIds.push(id);
			}
		}, new Visitor<ComplexHolder0>() {
			@Override
			public void visit(ComplexHolder0 holder) {
				int parentId = parentIds.pop();
				List<ComplexHolder0> children = holder.getChildren();
				for (int i = 0; i < children.size(); i++) {
					ComplexHolder0 child = children.get(i);
					int childId = ids.get(child);
					try {
						childrenStat.setInt(1, parentId);
						childrenStat.setInt(2, childId);
						childrenStat.setInt(3, i);
						childrenStat.addBatch();
						
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				ComplexHolder0[] array = holder.getArray();
				if(array != null){
					for (int i = 0; i < array.length; i++) {
						ComplexHolder0 entry = array[i];
						int childId = ids.get(entry);
						try {
							arrayStat.setInt(1, parentId);
							arrayStat.setInt(2, childId);
							arrayStat.setInt(3, i);
							arrayStat.addBatch();
							
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				}
				
			}
		});
		
		try {
			complexHolder0Stat.executeBatch();
			complexHolder0Stat.close();
			for (int i = 0; i < complexHolderStats.length; i++) {
				complexHolderStats[i].executeBatch();
				complexHolderStats[i].close(); 
			}
			childrenStat.executeBatch();
			childrenStat.close();
			arrayStat.executeBatch();
			arrayStat.close();
			

		} catch (Exception e) {
			throw new RuntimeException(e);		
		}
		
		commit();
		
	}

	@Override
	public void read() {
		addToCheckSum(readRootInternal());
	}

	private ComplexHolder0 readRootInternal() {
		return readHolder(Integer.MAX_VALUE, ROOT_ID);
	}

	private ComplexHolder0 readHolder(int depth, int id) {
		ComplexHolder0 holder = null;
		try {
			final PreparedStatement complexHolder0Stat = prepareStatement("select * from " + HOLDER_TABLE0 + " where id=?");
			final PreparedStatement[] complexHolderStats = new PreparedStatement[4];
			for (int i = 0; i < complexHolderStats.length; i++) {
				int idx = i + 1;
				String table = HOLDER_TABLES[i];
				complexHolderStats[i] = prepareStatement("select * from " + table + " where id=?"); 
			}
			final PreparedStatement arrayStat = prepareStatement("select * from " + ARRAY_TABLE + " where parent=? order by pos");
			final PreparedStatement childrenStat = prepareStatement("select * from " + CHILDREN_TABLE + " where parent=? order by pos");

			Map<Integer, ComplexHolder0> idToInstance = new HashMap<Integer, ComplexHolder0>();
			holder = readHolder(
					depth,
					idToInstance, 
					complexHolder0Stat, 
					complexHolderStats,
					arrayStat,
					childrenStat,
					id);
			
			complexHolder0Stat.close();
			for (int i = 0; i < complexHolderStats.length; i++) {
				complexHolderStats[i].close(); 
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return holder;
	}

	private ComplexHolder0 readHolder(
			int depth,
			Map<Integer, ComplexHolder0> read, 
			final PreparedStatement complexHolder0Stat, 
			PreparedStatement[] complexHolderStats, 
			PreparedStatement arrayStat, 
			PreparedStatement childrenStat, 
			int id) throws SQLException {
		if(depth < 1){
			return null;
		}
		ComplexHolder0 existing = read.get(id);
		if(existing != null){
			return existing;
		}
		complexHolder0Stat.setInt(1, id); 
		ResultSet resultSet0 = executeQuery(complexHolder0Stat);
		int type = resultSet0.getInt(TYPE);
		ComplexHolder0 holder = holderFromType(type);
		holder.setId(id);
		read.put(id, holder);
		
		holder.setName(resultSet0.getString(NAME));
		close(resultSet0);
		
		if(holder instanceof ComplexHolder1){
			ComplexHolder1 complexHolder1 = (ComplexHolder1) holder; 
			complexHolderStats[0].setInt(1, id);
			ResultSet resultSet1 = executeQuery(complexHolderStats[0]);
			complexHolder1._i1 = resultSet1.getInt(INT_FIELD);
			close(resultSet1);
		}
		
		if(holder instanceof ComplexHolder2){
			ComplexHolder2 complexHolder2 = (ComplexHolder2) holder; 
			complexHolderStats[1].setInt(1, id);
			ResultSet resultSet2 = executeQuery(complexHolderStats[1]);
			complexHolder2._i2 = resultSet2.getInt(INT_FIELD);
			close(resultSet2);
		}

		if(holder instanceof ComplexHolder3){
			ComplexHolder3 complexHolder3 = (ComplexHolder3) holder; 
			complexHolderStats[2].setInt(1, id);
			ResultSet resultSet3 = executeQuery(complexHolderStats[2]);
			complexHolder3._i3 = resultSet3.getInt(INT_FIELD);
			close(resultSet3);
		}

		if(holder instanceof ComplexHolder4){
			ComplexHolder4 complexHolder4 = (ComplexHolder4) holder; 
			complexHolderStats[3].setInt(1, id);
			ResultSet resultSet4 = executeQuery(complexHolderStats[3]);
			complexHolder4._i4 = resultSet4.getInt(INT_FIELD);
			close(resultSet4);
		}
		arrayStat.setInt(1, id);
		ResultSet arrayResultSet = arrayStat.executeQuery();
		List<Integer> arrayIds = new ArrayList<Integer>();
		while(arrayResultSet.next()){
			arrayIds.add(arrayResultSet.getInt(CHILD));
		}
		if(! arrayIds.isEmpty()){
			ComplexHolder0[] array = new ComplexHolder0[arrayIds.size()];
			holder.setArray(array);
			int idx = 0;
			for (Integer childId : arrayIds) {
				array[idx++] = readHolder(
						depth -1,
						read, 
						complexHolder0Stat,
						complexHolderStats, 
						arrayStat, 
						childrenStat, 
						childId);
			}
		}
		arrayResultSet.close();
		
		childrenStat.setInt(1, id);
		ResultSet childrenResultSet = childrenStat.executeQuery();
		List<Integer> childrenIds = new ArrayList<Integer>();
		while(childrenResultSet.next()){
			childrenIds.add(childrenResultSet.getInt(CHILD));
		}
		if(! childrenIds.isEmpty()){
			List<ComplexHolder0> children = new ArrayList();
			holder.setChildren(children);
			for (Integer childId : childrenIds) {
				children.add(readHolder(
						depth -1,
						read, 
						complexHolder0Stat,
						complexHolderStats, 
						arrayStat, 
						childrenStat, 
						childId));
			}
		}
		childrenResultSet.close();
		return holder;
		
	}

	private ComplexHolder0 holderFromType(int type) {
		switch (type){
			case 0:
				return new ComplexHolder0();
			case 1:
				return new ComplexHolder1();
			case 2:
				return new ComplexHolder2();
			case 3:
				return new ComplexHolder3();
			case 4:
				return new ComplexHolder4();
			default:
				throw new IllegalStateException("Valid type int 0 to 4 expected. Found:" + type);
		
		}
	}

	private void close(ResultSet resultSet) throws SQLException {
		if(resultSet.next()){
			throw new IllegalStateException("More than one complexHolder0 found.");
		}
		resultSet.close();
	}

	private ResultSet executeQuery(final PreparedStatement statement)
			throws SQLException {
		ResultSet resultSet0 = statement.executeQuery();
		if(! resultSet0.next()){
			throw new IllegalStateException("No row found.");
		}
		return resultSet0;
	}

	@Override
	public void query() {
		int selectCount = selectCount();
		int firstInt = objectCount() * objectCount() + objectCount();
		int lastInt = firstInt + (objectCount() * objectCount() * objectCount()) - 1;
		int currentInt = firstInt;
		for (int run = 0; run < selectCount; run++) {
			StringBuilder sb = new StringBuilder();
			sb.append("select " + HOLDER_TABLE0 + ".id from " + HOLDER_TABLE0);
			sb.append(" INNER JOIN " + HOLDER_TABLES[0]);
			sb.append(" on " + HOLDER_TABLE0 + ".id = " + HOLDER_TABLES[0] + ".id ");
			sb.append(" INNER JOIN " + HOLDER_TABLES[1]);
			sb.append(" on " + HOLDER_TABLE0 + ".id = " + HOLDER_TABLES[1] + ".id ");
			sb.append(" LEFT OUTER JOIN " + HOLDER_TABLES[2]);
			sb.append(" on " + HOLDER_TABLE0 + ".id = " + HOLDER_TABLES[2] + ".id ");
			sb.append(" LEFT OUTER JOIN " + HOLDER_TABLES[3]);
			sb.append(" on " + HOLDER_TABLE0 + ".id = " + HOLDER_TABLES[3] + ".id ");
			sb.append(" where " + HOLDER_TABLES[1] + ".i2 = ?");
			PreparedStatement stat = prepareStatement(sb.toString());
			
			
			
			try {
				stat.setInt(1, currentInt);
				ResultSet resultSet = executeQuery(stat);
				int holderId = resultSet.getInt("id");
				ComplexHolder2 holder = (ComplexHolder2) readHolder(2, holderId);
				addToCheckSum(holder.ownCheckSum());
				List<ComplexHolder0> children = holder.getChildren();
				for (ComplexHolder0 child : children) {
					addToCheckSum(child.ownCheckSum());
				}
				ComplexHolder0[] array = holder.getArray();
				for (ComplexHolder0 arrayElement : array) {
					addToCheckSum(arrayElement.ownCheckSum());
				}
				close(resultSet);
				stat.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e); 
			}
			currentInt++;
			if(currentInt > lastInt){
				currentInt = firstInt;
			}
		}
	}

	@Override
	public void update() {
		final PreparedStatement nameStat = prepareStatement("update " + HOLDER_TABLE0 + " set name=? where id=?");
		final PreparedStatement arrayDeleteStat = prepareStatement("delete from " + ARRAY_TABLE + " where parent = ?");
		final PreparedStatement arrayInsertStat = prepareStatement("insert into tarray (parent, child, pos) values (?,?,?)");
		ComplexHolder0 holder = readRootInternal();
		holder.traverse(new NullVisitor(),
			new Visitor<ComplexHolder0>(){
				@Override
				public void visit(ComplexHolder0 holder) {
					addToCheckSum(holder.ownCheckSum());
					try {
						nameStat.setString(1, "updated");
						nameStat.setInt(2, holder.getId());
						nameStat.addBatch();
						arrayDeleteStat.setInt(1, holder.getId());
						arrayDeleteStat.addBatch();
						List<ComplexHolder0> children = holder.getChildren();
						for (int i = 0; i < children.size(); i++) {
							arrayInsertStat.setInt(1, holder.getId());
							arrayInsertStat.setInt(2, children.get(i).getId());
							arrayInsertStat.setInt(3, i);
							arrayInsertStat.addBatch();
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
		});
		try {
			nameStat.executeBatch();
			nameStat.close();
			arrayDeleteStat.executeBatch();
			arrayDeleteStat.close();
			arrayInsertStat.executeBatch();
			arrayInsertStat.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		commit();
		
	}

	@Override
	public void delete() {
		final PreparedStatement[] complexHolderStats = new PreparedStatement[5];
		complexHolderStats[0] = prepareStatement("delete from " + HOLDER_TABLE0 + " where id=?");
		for (int i = 1; i < complexHolderStats.length; i++) {
			String table = HOLDER_TABLES[i - 1];
			complexHolderStats[i] = prepareStatement("delete from " + table + " where id=?"); 
		}
		final PreparedStatement arrayDeleteStat = prepareStatement("delete from " + ARRAY_TABLE + " where parent=?");
		final PreparedStatement childrenStat = prepareStatement("delete from " + CHILDREN_TABLE + " where parent=?");

		ComplexHolder0 holder = readRootInternal();
		holder.traverse(new NullVisitor(),
			new Visitor<ComplexHolder0>(){
				@Override
				public void visit(ComplexHolder0 holder) {
					addToCheckSum(holder.ownCheckSum());
					try {
						int id = holder.getId();
						for (int i = 0; i < complexHolderStats.length; i++) {
							complexHolderStats[i].setInt(1, id);
							complexHolderStats[i].addBatch();
						}
						arrayDeleteStat.setInt(1, id);
						arrayDeleteStat.addBatch();
						childrenStat.setInt(1, id);
						childrenStat.addBatch();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
		});
		try {
			for (int i = 0; i < complexHolderStats.length; i++) {
				complexHolderStats[i].executeBatch();
				complexHolderStats[i].close(); 
			}
			arrayDeleteStat.executeBatch();
			arrayDeleteStat.close();
			childrenStat.executeBatch();
			childrenStat.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		commit();
		
	}


}

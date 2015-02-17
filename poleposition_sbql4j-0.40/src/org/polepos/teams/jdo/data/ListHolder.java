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


package org.polepos.teams.jdo.data;

import java.util.*;

import org.polepos.framework.*;

public class ListHolder implements CheckSummable {
	
	public static final String ROOT_NAME = "root";
	
	private static IdGenerator _idGenerator = new IdGenerator();
	
	private long _id;

	private String _name;
	
	private List<ListHolder> _list;
	
	public static ListHolder generate(int depth, int leafs, int reuse){
		ListHolder root = generate(new ArrayList<ListHolder>(), depth, leafs, reuse);
		root._name = ROOT_NAME;
		return root;
	}
	
	private static ListHolder generate(List<ListHolder> flatList, int depth, int leafs, int reuse){
		if(depth == 0){
			return null;
		}
		ListHolder listHolder = new ListHolder();
		listHolder.setId(_idGenerator.nextId());
		
		flatList.add(listHolder);
		if(depth == 1){
			return listHolder;
		}
		listHolder.setList(new ArrayList<ListHolder>());
		int childDepth = depth -1;
		for (int i = leafs -1; i >= 0; i--) {
			if(i < reuse){
				int indexInList = (flatList.size() - i) / 2;
				if(indexInList < 0){
					indexInList = 0;
				}
				listHolder.getList().add(flatList.get(indexInList) );
			} else {
				ListHolder child = generate(flatList, childDepth, leafs, reuse);
				child._name = "child:" + depth + ":" + i;
				listHolder.getList().add(child);
			}
		}
		return listHolder;
	}

	@Override
	public long checkSum() {
		return _name.hashCode();
	}

	public void accept(Visitor<ListHolder> visitor) {
		Set<ListHolder> visited = new HashSet<ListHolder>();
		acceptInternal(visited, visitor);
	}
	
	private void acceptInternal(Set<ListHolder> visited, Visitor<ListHolder> visitor){
		if(visited.contains(this)){
			return;
		}
		visitor.visit(this);
		visited.add(this);
		if(getList() == null){
			return;
		}
		Iterator<ListHolder> i = getList().iterator();
		while(i.hasNext()){
			ListHolder child = i.next();
			child.acceptInternal(visited, visitor);
		}
	}
	
	public int update(int maxDepth, Procedure<ListHolder> storeProcedure) {
		Set<ListHolder> visited = new HashSet<ListHolder>();
		return updateInternal(visited, maxDepth, 0, storeProcedure);
	}


	public int updateInternal(Set<ListHolder> visited, int maxDepth, int depth, Procedure<ListHolder> storeProcedure) {
		if(visited.contains(this)){
			return 0;
		}
		visited.add(this);
		int updatedCount = 1;
		if(depth > 0){
			_name = "updated " + _name;
		}
		
		if(_list != null){
			for (int i = 0; i < _list.size(); i++) {
				ListHolder child = _list.get(i);
				updatedCount += child.updateInternal(visited, maxDepth, depth +  1, storeProcedure);
			}
		}
		storeProcedure.apply(this);
		return updatedCount;
	}

	public int delete(int maxDepth, Procedure<ListHolder> deleteProcedure) {
		// We use an IdentityHashMap here so hashCode is not called on deleted items.
		Map<ListHolder, ListHolder> visited = new IdentityHashMap<ListHolder, ListHolder>();
		return deleteInternal(visited, maxDepth, 0, deleteProcedure);
	}

	public int deleteInternal(Map<ListHolder, ListHolder> visited, int maxDepth, int depth, Procedure<ListHolder> deleteProcedure) {
		if(visited.containsKey(this)){
			return 0;
		}
		visited.put(this, this);
		int deletedCount = 1;
		if(_list != null){
			for (int i = 0; i < _list.size(); i++) {
				ListHolder child = getList().get(i);
				deletedCount += child.deleteInternal(visited, maxDepth, depth +  1, deleteProcedure);
			}
		}
		deleteProcedure.apply(this);
		return deletedCount;
	}

	private void setId(long id) {
		_id = id;
	}


	public long getId() {
		return _id;
	}


	private void setList(List<ListHolder> list) {
		_list = list;
	}


	private List<ListHolder> getList() {
		return _list;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(obj.getClass() != this.getClass()){
			return false;
		}
		ListHolder other = (ListHolder) obj;
		return _id == other._id;
	}
	
	@Override
	public int hashCode() {
		return (int)_id;
	}

	@Override
	public String toString() {
		return "ListHolder [_id=" + _id + "]";
	}

}

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


package org.polepos.teams.db4o;

import org.polepos.circuits.nestedlists.*;
import org.polepos.data.*;
import org.polepos.framework.*;

import com.db4o.*;
import com.db4o.config.*;
import com.db4o.query.*;

public class NestedListsDb4o extends Db4oDriver implements NestedLists {

	@Override
	public void create() throws Throwable {
		store(ListHolder.generate(depth(), objectCount(), reuse()));
		commit();
	}
	
	@Override
	public void read() throws Throwable {
		ListHolder root = root();
		activate(root, Integer.MAX_VALUE);
		root.accept(new Visitor<ListHolder>(){
			public void visit(ListHolder listHolder){
				addToCheckSum(listHolder);
			}
		});
	}

	private ListHolder root() {
		Query query = db().query();
		query.constrain(ListHolder.class);
		query.descend("_name").constrain(ListHolder.ROOT_NAME);
		ObjectSet<Object> objectSet = query.execute();
		ListHolder root = (ListHolder) objectSet.next();
		return root;
	}
	
	@Override
	public void update() throws Throwable {
		ListHolder root = root();
		activate(root, Integer.MAX_VALUE);
		addToCheckSum(root.update(depth(), 0,  new Procedure<ListHolder>() {
			@Override
			public void apply(ListHolder obj) {
				store(obj);
			}
		}));
		commit();
	}

	@Override
	public void delete() throws Throwable {
		ListHolder root = root();
		activate(root, Integer.MAX_VALUE);
		addToCheckSum(root.delete(depth(), 0,  new Procedure<ListHolder>() {
			@Override
			public void apply(ListHolder obj) {
				delete(obj);
			}
		}));
		commit();
	}
	
	@Override
	public void configure(Configuration config) {
		config.objectClass(ListHolder.class).objectField("_name").indexed(true);
	}

}

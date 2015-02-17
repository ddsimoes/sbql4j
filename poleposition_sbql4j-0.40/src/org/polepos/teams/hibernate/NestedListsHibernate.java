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


package org.polepos.teams.hibernate;

import org.hibernate.*;
import org.polepos.circuits.nestedlists.*;
import org.polepos.framework.*;
import org.polepos.teams.hibernate.data.*;

public class NestedListsHibernate extends HibernateDriver implements NestedLists {
	
    private final String FROM = "from org.polepos.teams.hibernate.data.ListHolder";

	@Override
	public void create() throws Throwable {
		Transaction tx = begin();
		store(ListHolder.generate(depth(), objectCount(), reuse()));
		tx.commit();
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
	
	private ListHolder root() {
		return (ListHolder) queryForSingle(FROM + " where name='root'");
	}
	
	@Override
	public void update() throws Throwable {
		Transaction tx = begin();
		ListHolder root = root();
		addToCheckSum(root.update(depth(), new Procedure<ListHolder>() {
			@Override
			public void apply(ListHolder obj) {
				store(obj);
			}
		}));
		tx.commit();
	}

	@Override
	public void delete() throws Throwable {
		Transaction tx = begin();
		ListHolder root = root();
		addToCheckSum(root.delete(depth(), new Procedure<ListHolder>() {
			@Override
			public void apply(ListHolder listHolder) {
				delete(listHolder);
			}
		}));
		tx.commit();
	}

}

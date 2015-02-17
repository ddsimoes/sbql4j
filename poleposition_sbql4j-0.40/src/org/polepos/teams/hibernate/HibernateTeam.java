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

import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hsqldb.jdbc.*;
import org.polepos.framework.Car;
import org.polepos.framework.CarMotorFailureException;
import org.polepos.framework.DriverBase;
import org.polepos.framework.Team;
import org.polepos.teams.hibernate.data.*;
import org.polepos.teams.jdbc.Jdbc;

public class HibernateTeam extends Team {

	private final Car[] _cars;

	public HibernateTeam() {
		String[] dbs = Jdbc.settings().getHibernateTypes();
		_cars = new Car[dbs.length];
		String[] colors = new String[]{"0x2B439C", "0x44BDE0", "0x127ABF"};
		for (int i = 0; i < dbs.length; i++) {
			_cars[i] = new HibernateCar(this, dbs[i], i>=colors.length ? "0x000000" : colors[i]);
		}
	}

	public String name() {
		return "Hibernate";
	}

	@Override
	public String description() {
		return "relational persistence for idiomatic Java";
	}

	public String databaseFile() {
		// not needed
		return null;
	}

	public Car[] cars() {
		return _cars;
	}

	public DriverBase[] drivers() {
		return new DriverBase[] {
				new FlatObjectHibernate(),
				new NestedListsHibernate(),
				new InheritanceHierarchyHibernate(),
				new ComplexHibernate(),
				new MelbourneHibernate(),
				new SepangHibernate(), 
				new BahrainHibernate(),
				new ImolaHibernate(), 
				new BarcelonaHibernate(),
				new MonacoHibernate(), 
				new MontrealHibernate(),
				new NurburgringHibernate(), 
		};
	}

	@Override
	public String website() {
		return "http://www.hibernate.org";
	}

	@Override
	public void setUp() {
		for(int i = 0; i < _cars.length;i++){
			HibernateCar hibernateCar = (HibernateCar)_cars[i];
			
			hibernateCar.recreateSessionFactory();
			
			// The following fails with the Complex circuit enabled:
			
//			Session session = hibernateCar.openSession();
//			Class[] classes = persistentClasses();
//			for (int j = 0; j < classes.length; j++) {
//				deleteExtent(session, classes[j]);
//			}
//			session.close();
		}
	}

	private void deleteExtent(Session session, Class clazz) {
		Transaction txn = session.beginTransaction();
		session.delete("from " + clazz.getName());
		txn.commit();
	}

	public static final Class[] persistentClasses() {
		return new Class[] { 
			ComplexHolder0.class,
			ComplexRoot.class,
			HB0.class,
			HibernateIndexedPilot.class, 
			HibernateLightObject.class, 
			HibernateListHolder.class,
			HibernatePilot.class, 
			HibernateTree.class,
			HN1.class,
			IndexedObject.class,
			InheritanceHierarchy0.class,
			ListHolder.class,
		};
	}

}

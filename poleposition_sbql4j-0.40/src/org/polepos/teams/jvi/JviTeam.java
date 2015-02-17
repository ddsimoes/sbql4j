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

package org.polepos.teams.jvi;

import java.util.ArrayList;
import java.util.List;

import org.polepos.framework.Car;
import org.polepos.framework.DriverBase;
import org.polepos.framework.Team;
import org.polepos.teams.jvi.data.JVB0;
import org.polepos.teams.jvi.data.JVB1;
import org.polepos.teams.jvi.data.JVB2;
import org.polepos.teams.jvi.data.JVB3;
import org.polepos.teams.jvi.data.JVB4;
import org.polepos.teams.jvi.data.JVN1;
import org.polepos.teams.jvi.data.JviIndexedPilot;
import org.polepos.teams.jvi.data.JviLightObject;
import org.polepos.teams.jvi.data.JviListHolder;
import org.polepos.teams.jvi.data.JviPilot;
import org.polepos.teams.jvi.data.JviTree;

import com.versant.fund.FundQuery;
import com.versant.fund.VException;
import com.versant.trans.TransSession;
import com.versant.util.DBUtility;
import com.versant.util.FEProfile;

/**
 * @author Christian Ernst
 */
public class JviTeam extends Team {
	
	
	/**
	 * return true to enable the team
	 */
	public static boolean enabled(){
		return false;
	}

	private final Car[] mCars;

	public JviTeam() {
		
		if(! enabled()){
			String msg = "JVI team is not enabled, to omit having to run the enhancer.";
			msg += "\r\nSimply change by returning true from the static enabled method in JviTeam.";
			System.err.println(msg);
			throw new RuntimeException(msg);
		}

		String[] impls = Jvi.settings().getJviImplementations();

		if (impls == null) {
			System.out.println("No JVI engine configured.");
			mCars = new Car[0];
		} else {

			List<Car> cars = new ArrayList<Car>();

			for (String impl : impls) {

				String[] jdosqldbs = Jvi.settings().getJdbc(impl);

				if (jdosqldbs != null && jdosqldbs.length > 0) {
					for (String sqldb : jdosqldbs) {
						try {
							cars.add(new JviCar(this, impl, sqldb));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					try {
						cars.add(new JviCar(this, impl, null));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			mCars = new Car[cars.size()];
			cars.toArray(mCars);
		}

	}

	@Override
	public String name() {
		return "JVI";
	}

	@Override
	public String description() {
		return "the JVI team";
	}

	@Override
	public Car[] cars() {
		return mCars;
	}

	@Override
	public DriverBase[] drivers() {
		return new DriverBase[] { new MelbourneJvi(), new SepangJvi(),
				new BahrainJvi(), new ImolaJvi(), new BarcelonaJvi(),
				new MonacoJvi(), new MontrealJvi(), new NurburgringJvi() };
	}

	@Override
	public String website() {
		return null;
	}

	public String databaseFile() {
		// not supported yet
		return null;
	}

	@Override
	public void setUp() {

		for (int i = 0; i < mCars.length; i++) {
			// register schema only needed to be able to define indexes
			TransSession session = ((JviCar) mCars[i]).getTransSession();

			String url = session.getDefaultDatabase();

			session.setSession();
			session.defineClass(JVB0.class);
			session.defineClass(JVB1.class);
			session.defineClass(JVB2.class);
			session.defineClass(JVB3.class);
			session.defineClass(JVB4.class);
			session.defineClass(JviIndexedPilot.class);
			session.defineClass(JviTree.class);
			session.defineClass(JviPilot.class);
			session.defineClass(JviLightObject.class);
			session.defineClass(JviListHolder.class);
			session.defineClass(JVN1.class);

			// define Indexes
			try {
				session.locateClass(JVB2.class.getName()).createIndex("b2",
						session.BTREE_INDEX);
			} catch (VException e) {
				if (e.getErrno() != 6073) {
					throw new RuntimeException(e);
				}
			}
			try {
				session.locateClass(JVB3.class.getName()).createIndex("b2",
						session.BTREE_INDEX);
			} catch (VException e) {
				if (e.getErrno() != 6073) {
					throw new RuntimeException(e);
				}
			}
			try {
				session.locateClass(JVB4.class.getName()).createIndex("b2",
						session.BTREE_INDEX);
			} catch (VException e) {
				if (e.getErrno() != 6073) {
					throw new RuntimeException(e);
				}
			}
			try {
				session.locateClass(JviIndexedPilot.class.getName())
						.createIndex("mName", session.BTREE_INDEX);
			} catch (VException e) {
				if (e.getErrno() != 6073) {
					throw new RuntimeException(e);
				}
			}
			try {
				session.locateClass(JviIndexedPilot.class.getName())
						.createIndex("mLicenseID", session.BTREE_INDEX);
			} catch (VException e) {
				if (e.getErrno() != 6073) {
					throw new RuntimeException(e);
				}
			}

			session.commit();

			// delete all existing objects
			FundQuery q = new FundQuery(session, "SELECT selfoid FROM "
					+ JVB4.class.getName());
			session.newHandleVector(q.execute().nextAll()).groupDeleteObjects(
					url);
			q.close();
			session.commit();

			q = new FundQuery(session, "SELECT selfoid FROM "
					+ JviIndexedPilot.class.getName());
			session.newHandleVector(q.execute().nextAll()).groupDeleteObjects(
					url);
			q.close();
			session.commit();

			q = new FundQuery(session, "SELECT selfoid FROM "
					+ JviPilot.class.getName());
			session.newHandleVector(q.execute().nextAll()).groupDeleteObjects(
					url);
			q.close();
			session.commit();

			q = new FundQuery(session, "SELECT selfoid FROM "
					+ JviTree.class.getName());
			session.newHandleVector(q.execute().nextAll()).groupDeleteObjects(
					url);
			q.close();
			session.commit();

			q = new FundQuery(session, "SELECT selfoid FROM "
					+ JviLightObject.class.getName());
			session.newHandleVector(q.execute().nextAll()).groupDeleteObjects(
					url);
			q.close();
			session.commit();

			q = new FundQuery(session, "SELECT selfoid FROM "
					+ JviListHolder.class.getName());
			session.newHandleVector(q.execute().nextAll()).groupDeleteObjects(
					url);
			q.close();
			session.commit();

			q = new FundQuery(session, "SELECT selfoid FROM "
					+ JVN1.class.getName());
			session.newHandleVector(q.execute().nextAll()).groupDeleteObjects(
					url);
			q.close();
			session.commit();

			session.endSession();

			// adjust our frontend profile
			FEProfile prof = DBUtility.getFEProfile(url);
			prof.setEstimatedObjects("5M");
			prof.setHeapSize("1000M");
			prof.setHeapSizeIncrement("100M");
			prof.setSwapThreshold("1000M");
			DBUtility.writeFEProfile(prof);

		}

	}

}

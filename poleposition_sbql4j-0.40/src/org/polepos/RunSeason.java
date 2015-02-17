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

package org.polepos;

import java.io.*;

import org.polepos.circuits.bahrain.*;
import org.polepos.circuits.barcelona.*;
import org.polepos.circuits.complex.*;
import org.polepos.circuits.flatobject.*;
import org.polepos.circuits.imola.*;
import org.polepos.circuits.inheritancehierarchy.*;
import org.polepos.circuits.melbourne.*;
import org.polepos.circuits.monaco.*;
import org.polepos.circuits.montreal.*;
import org.polepos.circuits.nestedlists.*;
import org.polepos.circuits.nurburgring.*;
import org.polepos.circuits.sepang.*;
import org.polepos.framework.*;
import org.polepos.reporters.*;
import org.polepos.runner.*;
import org.polepos.teams.cobra.*;
import org.polepos.teams.db4o.*;
import org.polepos.teams.db4o_sbql4j.Db4oSbql4jTeam;
import org.polepos.teams.hibernate.*;
import org.polepos.teams.jdbc.*;
import org.polepos.teams.jdo.*;
import org.polepos.teams.jpa.*;
import org.polepos.teams.jvi.*;

/**
 * This is the Main class to run PolePosition. If JDO, JPA and JVI are
 * to be tested also, persistent classes have to be enhanced first.
 * 
 * For your convenience you can try {@link RunSeasonAfterEnhancing#main(String[])}
 * or you can use the Ant script to do all in one go.
 * 
 * 
 */
public class RunSeason extends AbstractRunner {
	
	public static void main(String[] args) {
		new RunSeason().run();
	}

	@Override
	public CircuitBase[] circuits() {
		return new CircuitBase[] {
				
				new ReflectiveCircuitBase(Complex.class),
				new ReflectiveCircuitBase(NestedLists.class),
				new ReflectiveCircuitBase(InheritanceHierarchy.class),
				new ReflectiveCircuitBase(FlatObject.class),
				
// Old Circuits
// Most usecases are covered by the 4 new circuits
				
//				new Melbourne(), 
//				new Sepang(), 
//				new Bahrain(),
//				new Imola(),
//				new Barcelona(),
//				new Monaco(),
//				new Montreal(),
//				new Nurburgring(),
		};
	}
	
	

	@Override
	public Team[] teams() {
		return new Team[] { 
				new Db4oTeam(),
				new Db4oSbql4jTeam()
//				new JdoTeam(),
				
//				new Db4oClientServerTeam(),
				
// 				new JdbcTeam(),
//				new HibernateTeam(),
//				new JpaTeam(),
				
				// new JviTeam(),
				// new CobraTeam(),
		};
	}

	@Override
	protected Reporter[] reporters() {
		return DefaultReporterFactory.defaultReporters();
	}

}

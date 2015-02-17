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

package org.polepos.runner;

import java.util.*;

import org.polepos.*;
import org.polepos.framework.*;
import org.polepos.reporters.*;

public abstract class AbstractRunner {

	public void run() {
		run(Settings.CIRCUIT);
	}
	
	public void run(String propertiesFileName){
		TurnSetupConfig turnSetupConfig = new TurnSetupConfig(propertiesFileName);
		List<CircuitBase> distinctCircuits = Arrays.asList(circuits());
		List<Circuit> circuits = new ArrayList<Circuit>();
		circuits.addAll(distinctCircuits);
        if(turnSetupConfig.runConcurrency()){
	        for(Circuit circuit : distinctCircuits){
	        	if(circuit.isConcurrency()){
	        		circuits.add(new ConcurrencyCircuit(circuit));
	        	}
	        }
        }
		for(Circuit circuit: circuits){
			TurnSetup[] turnSetups = turnSetupConfig.read(circuit);
			circuit.setTurnSetups(turnSetups);
		}
		new Racer( circuits, Arrays.asList(teams()), Arrays.asList(reporters())).run();
	}

	protected abstract CircuitBase[] circuits();

	protected abstract Team[] teams();

	protected abstract Reporter[] reporters();

}
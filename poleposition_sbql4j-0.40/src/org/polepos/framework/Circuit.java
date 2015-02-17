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

package org.polepos.framework;

import java.util.*;

public interface Circuit {

	public TurnResult[] race(Team team, Car car, Driver driver);

	public List<Lap> laps();

	public TurnSetup[] lapSetups();

	public Class<?> requiredDriver();

	public String description();

	public String internalName();
	
	public String fileNamePrefix();

	public String name();

	public void setTurnSetups(TurnSetup[] turnSetups);
	
	public TurnSetup[] getTurnSetups();

	public Driver[] nominate(Team team);
	
	public void reportTo(Circuit circuit);
	
	public boolean isConcurrency();

	public void runLapsBefore(Lap lap, TurnSetup turnSetup, DriverBase driver, Car car);

}
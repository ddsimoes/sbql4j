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

public class ConcurrencyCircuit implements Circuit {
	
	public static final String NAME_ADD_ON = "Concurrency";
	
	private final Circuit _delegate;
	
    private TurnSetup[] _turnSetups;
	
	public ConcurrencyCircuit(Circuit delegate){
		_delegate = delegate;
	}

	@Override
	public String description() {
		return _delegate.description();
	}

	@Override
	public String internalName() {
		return _delegate.internalName();
	}

	@Override
	public TurnSetup[] lapSetups() {
		return _delegate.lapSetups();
	}

	@Override
	public List<Lap> laps() {
		return _delegate.laps();
	}

	@Override
	public String name() {
		return NAME_ADD_ON + " " + _delegate.name();
	}

	@Override
	public TurnResult[] race(Team team, Car car, Driver driver) {
		
		TurnSetup[] turnSetups = _delegate.getTurnSetups();
		
		_delegate.setTurnSetups(_turnSetups);
		_delegate.reportTo(this);
		
		TurnResult[] results = _delegate.race(team, car, driver);
		
		
		_delegate.setTurnSetups(turnSetups);
		_delegate.reportTo(_delegate);
		
		return results;
	}

	@Override
	public Class<?> requiredDriver() {
		return _delegate.requiredDriver();
	}

	@Override
	public void setTurnSetups(TurnSetup[] turnSetups) {
		_turnSetups = turnSetups;
	}

	@Override
	public String fileNamePrefix() {
		return _delegate.fileNamePrefix() + "_" + NAME_ADD_ON;
	}
	
	@Override
	public Driver[] nominate(Team team){
		Driver[] drivers = team.nominate(this);
		Driver[] concurrentDrivers = new Driver[drivers.length];
		int i = 0;
		for (Driver driver : drivers) {
			concurrentDrivers[i++] = new ConcurrentDriver((DriverBase)driver);
		}
		return concurrentDrivers;
	}

	@Override
	public void reportTo(Circuit circuit) {
		
	}
	
	public boolean isConcurrency(){
		return true;
	}

	@Override
	public TurnSetup[] getTurnSetups() {
		return _turnSetups;
	}

	@Override
	public void runLapsBefore(Lap lap, TurnSetup turnSetup, DriverBase driver, Car car) {
		_delegate.runLapsBefore(lap, turnSetup, driver, car);
	}

}

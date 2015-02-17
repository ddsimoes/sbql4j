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

public class ConcurrentDriver extends Driver {
	
	
	private DriverBase _masterDriver;
	
	private DriverBase[] _drivers;
	
	public ConcurrentDriver(DriverBase driver){
		_masterDriver = driver;
	}
	
	@Override
	public void takeSeatIn(Car car, TurnSetup setup)
			throws CarMotorFailureException {
		int threadCount = setup.getThreadCount();
		cloneMasterDriver(threadCount);
		for(Driver driver : _drivers){
			driver.takeSeatIn(car, setup);
		}
	}

	private void cloneMasterDriver(int threadCount) {
		_drivers = new DriverBase[threadCount];
		_drivers[0] = _masterDriver;
		for (int i = 1; i < threadCount; i++) {
			_drivers[i] = _masterDriver.clone();
			_drivers[i].bulkId(i);
		}
	}

	@Override
	public void backToPit() {
		// We send the master driver back to pit last,
		// since it may have opened the server.
		for (int i = 1; i < _drivers.length; i++) {
			_drivers[i].backToPit();
		}
		_drivers[0].backToPit();
	}

	@Override
	public void prepare() throws CarMotorFailureException {
		for(Driver driver : _drivers){
			driver.prepare();
		}
	}

	public void circuitCompleted() {
		for(Driver driver : _drivers){
			driver.circuitCompleted();
		}
	}

	public long checkSum() {
		long expectedSum = _drivers[0].checkSum();
		for(Driver driver : _drivers){
			long actualSum = driver.checkSum();
			if(actualSum != expectedSum){
				System.err.println("Checksum not consistent in concurrency run: " + driver.getClass().toString());
			}
		}
		return expectedSum;
	}
	
	public Runnable prepareLap(final Lap lap) {
		
		lap.circuit().runLapsBefore(lap, lap.circuit().getTurnSetups()[0], _masterDriver, _masterDriver.car());
		
		for (int i = 1; i < _drivers.length; i++) {
			_drivers[i].copyStateFrom(_masterDriver);
		}
		
		
		final Thread[] threads = new Thread[_drivers.length];
		for (int i = 0; i < _drivers.length; i++) {
			threads[i] = new Thread(_drivers[i].prepareLap(lap));
		}
		
		return new Runnable(){

			@Override
			public void run() {
				
				for (Thread thread : threads) {
					thread.start();
				}
				
				for (Thread thread : threads) {
					try {
						thread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}

	@Override
	public boolean canRunLap(Lap lap) {
		if(! _masterDriver.supportsConcurrency()){
			return false;
		}
		return lap.concurrent();
	}
	
	
}

/* Copyright (C) 2009  Versant Inc.   http://www.db4o.com */
package org.polepos.reporters;

import org.polepos.framework.*;

public interface Reporter {

	void startSeason();

	void endSeason();

	void sendToCircuit(Circuit circuit);

	void noDriver(Team team, Circuit circuit);

	void report(Team team, Car car, TurnSetup[] setups, TurnResult[] results);

}
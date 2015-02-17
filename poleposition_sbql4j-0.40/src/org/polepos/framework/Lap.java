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

import java.io.*;

/**
 * a single (timed) test
 */
public class Lap {

	private Circuit _circuit;

	private final String mName;

	private boolean mHot;

	private boolean mReportResult;

	private boolean _concurrent = true;

	private String _code;

	public Lap(String name) {
		this.mName = name;
		mHot = false;
		mReportResult = true;
	}

	public Lap(String name, boolean hot, boolean reportResult) {
		this(name);
		this.mHot = hot;
		this.mReportResult = reportResult;
	}

	public Lap(String name, boolean hot, boolean reportResult,
			boolean concurrent) {
		this(name);
		this.mHot = hot;
		this.mReportResult = reportResult;
		_concurrent = concurrent;
	}

	public String name() {
		return mName;
	}

	public boolean hot() {
		return mHot;
	}

	public boolean reportResult() {
		return mReportResult;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != getClass()) {
			return false;
		}
		Lap key = (Lap) obj;
		return mName.equals(key.mName);
	}

	@Override
	public int hashCode() {
		return mName.hashCode();
	}

	public boolean concurrent() {
		return _concurrent;
	}

	public void code(String code) {
		_code = code;
	}

	public void circuit(CircuitBase circuitBase) {
		_circuit = circuitBase;
		String fileName = "pseudocode/" + circuitBase.internalName() + "."
				+ mName + ".txt";
		File file = new File(fileName);
		if (file.exists()) {
			try {
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				StringBuffer sb = new StringBuffer();
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
				_code = sb.toString();
				bufferedReader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public String code() {
		return _code;
	}
	
	public Circuit circuit(){
		return _circuit;
	}

}

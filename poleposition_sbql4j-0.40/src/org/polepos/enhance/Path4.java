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

package org.polepos.enhance;

import java.io.*;


public class Path4 { 
	
	private static final java.util.Random _random = new java.util.Random();
	
	public static String getDirectoryName(String targetPath) {
		return new File(targetPath).getParent();
	}

	public static String combine(String parent, String child) {		
		return parent.endsWith(java.io.File.separator)
        ? parent + child
        : parent + java.io.File.separator + child;
	}
	
	public static String getTempPath() {
		String path = System.getProperty("java.io.tmpdir"); 
		if(path == null || path.length() <= 1){
		    path = "/temp"; 
		}
		File4.mkdirs(path);
		return path;
	}

	public static String getTempFileName() {
		String tempPath = getTempPath();
		while (true) {
			String fname = combine(tempPath, "db4o-test-" + nextRandom() + ".tmp");
			if (!File4.exists(fname)) {
				try {
					new FileWriter(fname).close();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage());
				}
				return fname;
			}
		}
	}

	private static String nextRandom() {
		return Integer.toHexString(_random.nextInt());
	}	
}

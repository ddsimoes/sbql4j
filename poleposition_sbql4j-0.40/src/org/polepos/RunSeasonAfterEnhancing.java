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

import org.polepos.enhance.*;


/**
 * Runs all enhancers in one go and then starts RunSeason.
 */
public class RunSeasonAfterEnhancing{
	
	public static void main( String [] args ) throws Throwable{
		
		// We are spawning enhancing in a different Java process here,
		// otherwise our persistent classes are already loaded in the
		// current ClassLoader.
		
		System.out.println("Starting org.polepos.enhance.AllEnhance in a separate Java process.");
		
		String output = JavaServices.java("org.polepos.enhance.AllEnhance");
		System.out.println(output);
		
		
	    RunSeason.main(null);
	    
	}
    
}

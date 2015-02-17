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

public class TurnSetupConfig {
	
    private final PropertiesHandler mProperties;
    public final static String REUSE = "reuse";
	public final static String OBJECTCOUNT = "objects";
	public final static String SELECTCOUNT = "selects";
	public final static String UPDATECOUNT = "updates";
	public final static String COMMITCOUNT = "commits";
	public final static String TREEWIDTH = "width";
	public final static String DEPTH = "depth";
	public final static String COMMITINTERVAL = "commitinterval";
	
	public final static String OBJECTSIZE = "size";
	
	private final static String CONCURRENCY = "concurrency";
	public final static String THREADCOUNT = "threads";
	
	final static String[] AVAILABLE_SETTINGS = new String[]{
		REUSE,
	    OBJECTCOUNT,
	    SELECTCOUNT,
	    UPDATECOUNT,
	    COMMITCOUNT,
	    TREEWIDTH,
	    DEPTH,
	    COMMITINTERVAL,
	    OBJECTSIZE
	};
    
    public TurnSetupConfig(String propertiesFileName){
    	mProperties = new PropertiesHandler(propertiesFileName); 
    }
    
    public boolean runConcurrency(){
    	return mProperties.getBoolean(CONCURRENCY);
    }
	
    public TurnSetup[] read(Circuit circuit){
        
        Vector<TurnSetup> vec = new Vector<TurnSetup>();
        
        int[] threadCount = mProperties.getIntArray(THREADCOUNT);
        
        if(circuit.isConcurrency()){
        	
        	List<SetupProperty> setupProperties = new ArrayList<SetupProperty>();
        	
            for (int i = 0; i < AVAILABLE_SETTINGS.length; i++) {
                int[] values = mProperties.getIntArray(circuit.internalName() + "." + AVAILABLE_SETTINGS[i]);
                if(values!= null && values.length > 0){

                	// For concurrency, we only use the first setup parameters.
                	setupProperties.add(new SetupProperty(AVAILABLE_SETTINGS[i], values[0]));
                	
                }
            }
        	
        	TurnSetup[] turnSetups = new TurnSetup[threadCount.length];
        	for (int i = 0; i < threadCount.length; i++) {
        		turnSetups[i] = new TurnSetup();
        		turnSetups[i].addSetting(new SetupProperty(THREADCOUNT, threadCount[i]));
        		for (SetupProperty setupProperty : setupProperties) {
        			turnSetups[i].addSetting(setupProperty);
				}
			}
        	return turnSetups;
        }
        
        
        for (int i = 0; i < AVAILABLE_SETTINGS.length; i++) {
            
            int[] values = null;
            
            values = mProperties.getIntArray(circuit.internalName() + "." + AVAILABLE_SETTINGS[i]);
            
            if(values!= null && values.length > 0){
                int len = values.length;
                
                // make sure that we have enough LapSetup objects in our vector
                // and clone the last if we dont or create a first one
                while(vec.size() < len){
                    if(vec.size() > 0){
                        vec.add((vec.get(vec.size() - 1)).deepClone());
                    }else{
                        vec.add(new TurnSetup());
                    }
                }
                
                // pass values to all LapSetup objects and take the last value as
                // the default if there are more than we have values
                int j = 0;
                Iterator it = vec.iterator();
                while(it.hasNext()){
                    TurnSetup ls = (TurnSetup)it.next();
                    SetupProperty sp =new SetupProperty(AVAILABLE_SETTINGS[i], values[j]); 
                    ls.addSetting(sp);
                    if(j < values.length - 1){
                        j++;
                    }
                }
            }
        }
        
        TurnSetup[] res = new TurnSetup[vec.size()];
        vec.toArray(res);
        
        return res;
    }


}

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



public class TurnSetup implements Cloneable{
    
    private Map<String, SetupProperty> mSettings = new HashMap<String, SetupProperty>();
    
    public TurnSetup() {
    }

    public TurnSetup(SetupProperty... properties) {
    	for (SetupProperty property : properties) {
			mSettings.put(property.name(), property);
		}
    }

    TurnSetup deepClone(){
        TurnSetup res = null;
        try {
            res = (TurnSetup)this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        res.mSettings = new HashMap<String, SetupProperty>(mSettings);
        return res;
    }
    
    
    private int getSetting(String key){
        SetupProperty p = mSettings.get(key);
        if(p != null){
            return p.value();
        }
        return 0;
    }
    
    public int getCommitInterval(){
        return getSetting(TurnSetupConfig.COMMITINTERVAL);
    }

    public int getCommitCount(){
        return getSetting(TurnSetupConfig.COMMITCOUNT);
    }

    public int getObjectCount(){
        return getSetting(TurnSetupConfig.OBJECTCOUNT);
    }
    
    public int getReuse(){
        return getSetting(TurnSetupConfig.REUSE);
    }
    
    public int getSelectCount(){
        return getSetting(TurnSetupConfig.SELECTCOUNT);
    }
    
    public int getUpdateCount(){
        return getSetting(TurnSetupConfig.UPDATECOUNT);
    }
    
    public int getTreeWidth(){
        return getSetting(TurnSetupConfig.TREEWIDTH);
    }
    
    public int getDepth(){
        return getSetting(TurnSetupConfig.DEPTH);
    }
    
    public int getObjectSize(){
        return getSetting(TurnSetupConfig.OBJECTSIZE);
    }
    
    public int getMostImportantValueForGraph(){
    	
    	int threadCount = getThreadCount();
    	if(threadCount > 0){
    		return threadCount;
    	}
    	
        for (int i = 0; i < TurnSetupConfig.AVAILABLE_SETTINGS.length; i++) {
            int val = getSetting(TurnSetupConfig.AVAILABLE_SETTINGS[i]);
            if(val > 0){
                return val;
            }
        }
        return 0;
    }
    
    public String getMostImportantNameForGraph(){
    	int threadCount = getThreadCount();
    	if(threadCount > 0){
    		return TurnSetupConfig.THREADCOUNT;
    	}
    	
        for (int i = 0; i < TurnSetupConfig.AVAILABLE_SETTINGS.length; i++) {
            int val = getSetting(TurnSetupConfig.AVAILABLE_SETTINGS[i]);
            if(val > 0){
                return TurnSetupConfig.AVAILABLE_SETTINGS[i];
            }
        }
        return "";
    }
    
    public List<SetupProperty> properties() {
    	List<SetupProperty> list = new ArrayList<SetupProperty>(mSettings.values());
    	Collections.sort(list);
    	return list;
    }
    
    public void addSetting(SetupProperty setupProperty){
    	mSettings.put(setupProperty.name(), setupProperty);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mSettings == null) ? 0 : mSettings.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TurnSetup other = (TurnSetup) obj;
		if (mSettings == null) {
			if (other.mSettings != null) {
				return false;
			}
		} else if (!mSettings.equals(other.mSettings)) {
			return false;
		}
		return true;
	}

	public int getThreadCount() {
		return getSetting(TurnSetupConfig.THREADCOUNT);
	}
    
    
}

/* Copyright (C) 2004 - 2006  db4objects Inc.  http://www.db4o.com */

package org.polepos.teams.jdo.data;

import java.util.ArrayList;
import java.util.List;

import org.polepos.framework.CheckSummable;

/**
 * @author Christian Ernst
 */
public class JdoListHolder implements CheckSummable{
    
    public List list;
    
    public JdoListHolder(){
        
    }
    
    public static JdoListHolder generate(int index, int elements){
        JdoListHolder lh = new JdoListHolder();
        List list = new ArrayList(); 
        lh.setList(list);
        for (int i = 0; i < elements; i++) {
            list.add(new Integer(i));
        }
        return lh;
    }


    public long checkSum() {
        return list.size();
    }

    public List getList() {
        return list;
    }
    
    public void setList(List list) {
        this.list = list;
    }

}

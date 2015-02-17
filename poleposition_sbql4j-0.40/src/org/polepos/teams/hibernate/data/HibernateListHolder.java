/* Copyright (C) 2004 - 2006  db4objects Inc.  http://www.db4o.com */

package org.polepos.teams.hibernate.data;

import java.util.*;

import org.polepos.framework.*;


public class HibernateListHolder implements CheckSummable{
    
    public List list;
    
    public HibernateListHolder(){
        
    }
    
    public static HibernateListHolder generate(int index, int elements){
        HibernateListHolder lh = new HibernateListHolder();
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

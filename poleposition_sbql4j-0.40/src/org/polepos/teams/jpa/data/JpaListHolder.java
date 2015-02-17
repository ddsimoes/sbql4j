/* Copyright (C) 2004 - 2006  db4objects Inc.  http://www.db4o.com */

package org.polepos.teams.jpa.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.polepos.framework.CheckSummable;

/**
 * @author Christian Ernst
 */
@Entity
public class JpaListHolder implements CheckSummable{
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long oid;
    
    @Basic
    @Lob
    public List<Integer> list;
    
    public JpaListHolder(){
        
    }
    
    public static JpaListHolder generate(int index, int elements){
        JpaListHolder lh = new JpaListHolder();
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

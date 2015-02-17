package org;

import com.db4o.ObjectContainer;

public class Test {
    
    public Test() {
        super();
    }
    static ObjectContainer db;
    
    public static void main(String[] args) {
        Integer i = new Test_SbqlQuery0().executeQuery();
        Integer i2 = new Test_SbqlQuery1(db).executeQuery();
    }
}
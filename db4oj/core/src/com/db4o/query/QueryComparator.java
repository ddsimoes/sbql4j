/* Copyright (C) 2004 - 2006  Versant Inc.  http://www.db4o.com */

package com.db4o.query;

import java.io.*;

/**
 * Comparator for sorting queries on JDKs where 
 * java.util.Comparator is not available.
 */
public interface QueryComparator<Target> extends Serializable {
    
    /**
     * Implement to compare two arguments for sorting.  
     * Return a negative value, zero, or a positive value if
     * the first argument is smaller, equal or greater than 
     * the second.
     */
	int compare(Target first, Target second);
    
}

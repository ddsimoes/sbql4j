/* Copyright (C) 2004 - 2010  Versant Inc.  http://www.db4o.com */

package org.polepos.framework;

public interface Procedure<T> {
	
	void apply(T obj);

}

/* Copyright (C) 2008  Versant Inc.  http://www.db4o.com */

package com.db4o.internal;

import com.db4o.foundation.*;

/**
 * @exclude
 */
public class InCallback {
	
	private final static DynamicVariable<Boolean> _inCallback = new DynamicVariable<Boolean>(){
		@Override
		protected Boolean defaultValue() {
			return false;
		}
	};
	
	public static boolean value() {
		return _inCallback.value();
	}

	public static void run(Runnable runnable) {
		_inCallback.with(true, runnable);
    }
}

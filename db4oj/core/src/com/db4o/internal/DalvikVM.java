/* Copyright (C) 2004 - 2005  Versant Inc.  http://www.db4o.com */

package com.db4o.internal;

import java.io.*;
import java.lang.reflect.*;

import com.db4o.foundation.*;
import com.db4o.reflect.*;
import com.db4o.reflect.core.*;

/**
 * @sharpen.ignore
 */
@decaf.Remove(unlessCompatible=decaf.Platform.JDK15)
class DalvikVM extends JDK_5 {
	
	@decaf.Remove(unlessCompatible=decaf.Platform.JDK15)
	public final static class Factory implements JDKFactory {
		public JDK tryToCreate() {
			if (!"Dalvik".equals(System.getProperty("java.vm.name"))) {
				return null;
			}
			return new DalvikVM();
		}
	};
	
	@Override
	public ReflectConstructor serializableConstructor(Reflector reflector, final Class clazz) {
		
		return new ReflectConstructor() {
			
			public Object newInstance(Object[] parameters) {
				try {
					return DalvikVM.this.newInstanceSkippingConstructor(clazz);
				} catch (SecurityException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
			
			public ReflectClass[] getParameterTypes() {
				return new ReflectClass[0];
			}
		};
	}

	private Object newInstanceSkippingConstructor(final Class clazz) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			
		Method newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
		newInstance.setAccessible(true);
		return newInstance.invoke(null, clazz, Object.class);
	}
	
	public static class SkipConstructorCheck {
		public SkipConstructorCheck() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}
	}
	
	private TernaryBool supportSkipConstructorCall = TernaryBool.UNSPECIFIED;
	
	@Override
	boolean supportSkipConstructorCall() {
		if (supportSkipConstructorCall.isUnspecified()) {
			try {
				newInstanceSkippingConstructor(SkipConstructorCheck.class);
				supportSkipConstructorCall = TernaryBool.YES;
			} catch (Exception e) {
				supportSkipConstructorCall = TernaryBool.NO;
			}
		}
		return supportSkipConstructorCall.definiteYes();
	}

}

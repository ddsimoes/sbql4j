/* Copyright (C) 2004 - 2010  Versant Inc.  http://www.db4o.com */

package org.polepos.enhance;

import java.security.*;

public abstract class EvilSystemExitEnhancer {
	
	public void runWithoutSystemExit() throws Throwable{
		
		SecurityManager currentSecurityManager = System.getSecurityManager();
		System.setSecurityManager(new NoSystemExitSecurityManager());
		
		try{
			internalRunWithoutSystemExit();
		} catch(ExitTrappedException expectedException){
			// do nothing
		} finally {
			System.setSecurityManager( currentSecurityManager) ;
		}
	}
	
	protected abstract void internalRunWithoutSystemExit() throws Throwable;

	private static class NoSystemExitSecurityManager extends SecurityManager {
	      public void checkPermission( Permission permission ) {
		        if( permission.getName().startsWith("exitVM")) {
		        	throw new ExitTrappedException() ;
		        }
	      }
	}
	
	private static class ExitTrappedException extends SecurityException { }


}

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

package org.polepos.teams.db4o;

import java.lang.reflect.*;

import org.polepos.framework.*;

import com.db4o.*;
import com.db4o.config.*;
import com.db4o.ext.*;
import com.db4o.query.*;

public abstract class Db4oDriver extends DriverBase {
	
	private StoreAdapter _storeAdapter;

	private ExtObjectContainer _container;

	public void prepare() {
		Configuration objectContainerConfiguration = Db4o.newConfiguration();
		configure(objectContainerConfiguration);
		Configuration serverConfiguration = Db4o.newConfiguration();
		configure(serverConfiguration);
		_container = db4oCar().openObjectContainer(serverConfiguration, objectContainerConfiguration);
	}

	private Db4oCar db4oCar() {
		return ((Db4oCar) car());
	}
	
	public abstract void configure(Configuration config);
	
	protected void indexField(Configuration config, Class clazz, String fieldName) {
		ObjectClass objectClass = config.objectClass(clazz);
		objectClass.objectField(fieldName).indexed(true);
	}

	public void backToPit() {
		db4oCar().close(_container);
	}

	public ExtObjectContainer db() {
		return _container;
	}

	protected ObjectSet doQuery(Query q) {
		ObjectSet result = q.execute();
		while (result.hasNext()) {
			Object o = result.next();
			if (o instanceof CheckSummable) {
				addToCheckSum(((CheckSummable) o).checkSum());
			}
		}
		result.reset();
		return result;
	}

	protected void doQuery(Class clazz) {
		Query q = db().query();
		q.constrain(clazz);
		doQuery(q);
	}

	protected void begin() {
		// db4o always works in a transaction so a begin call
		// is not needed.
	}
	
	protected void activate(Object obj, int depth) {
		_container.activate(obj, depth);
	}

	protected void commit() {
		_container.commit();
	}

	protected void store(Object obj) {
		// using a storeAdapter to be compatible with old db4o versions
		// new syntax: ObjectContainer#store()
		// old syntax prior to 7.1: ObjectContainer#set()
		if(_storeAdapter == null){
			_storeAdapter = StoreAdapter.forObjectContainer(_container);
		}
		_storeAdapter.store(_container, obj);
	}
	
	protected void purge(Object obj) {
		_container.purge(obj);
	}
	
	protected void delete(Object obj) {
		_container.delete(obj);
	}
	
	private static abstract class StoreAdapter{
		
		abstract void store(ObjectContainer container, Object obj);

		public static StoreAdapter forObjectContainer(ObjectContainer container) {
			Class clazz = container.getClass();
			try {
				clazz.getMethod("store", new Class[]{Object.class});
			} catch (NoSuchMethodException nsme) {
				Method method = null;
				try {
					method = clazz.getMethod("set", new Class[]{Object.class});
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
				return new StoreAdapter70(method);
			}
			return new StoreAdapter80();
		}
	}
	
	private static class StoreAdapter80 extends StoreAdapter{
		void store(ObjectContainer container, Object obj){
			container.store(obj);
		}
	}
	
	private static class StoreAdapter70 extends StoreAdapter{
		
		private Method _method;
		
		StoreAdapter70(Method method){
			_method = method;
		}
		
		void store(ObjectContainer container, Object obj){
			try {
				_method.invoke(container, obj);
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		}
	}
	

}

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


package org.polepos.framework;

import java.lang.reflect.*;
import java.util.*;


public class ReflectiveCircuitBase extends CircuitBase {

	private final Class<?> _driverClass;

	public ReflectiveCircuitBase(Class<?> driverClass) {
		_driverClass = driverClass;
	}

	@Override
	protected void addLaps() {
		Method[] declaredMethods = _driverClass.getDeclaredMethods();
		Arrays.sort(declaredMethods,new Comparator<Method>() {
			@Override
			public int compare(Method m1, Method m2) {
				Order annotation1 = m1.getAnnotation(Order.class);
				int order1 = annotation1.value();
				Order annotation2 = m2.getAnnotation(Order.class);
				int order2 = annotation2.value();
				return order1 - order2;
			}
		});
		
		for (Method method : declaredMethods) {
			if (method.getAnnotation(Ignored.class) != null) {
				continue;
			}
			add(new Lap(method.getName()));
		}
		
	}
	
	@Override
	public boolean isConcurrency() {
		Concurrent concurrent = _driverClass.getAnnotation(Concurrent.class);
		return concurrent != null;
	}

	@Override
	public String description() {
		CircuitDescription description = _driverClass.getAnnotation(CircuitDescription.class);
		if(description == null){
			throw new IllegalStateException("Class " + _driverClass.getName()+ " has to be annotated with " + CircuitDescription.class.getName());
		}
		return description.value();
	}

	@Override
	public Class<?> requiredDriver() {
		return _driverClass;
	}
	
	@Override
	protected Class<?> circuitClass() {
		return _driverClass;
	}

}

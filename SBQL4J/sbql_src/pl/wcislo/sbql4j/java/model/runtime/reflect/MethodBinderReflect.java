package pl.wcislo.sbql4j.java.model.runtime.reflect;

import java.lang.reflect.Method;

import pl.wcislo.sbql4j.java.model.runtime.MethodBinder;
import pl.wcislo.sbql4j.java.utils.ReflectUtils;

public class MethodBinderReflect extends MethodBinder {
 
	public final Method method;	
	public MethodBinderReflect(Method method, Object owner) {
		super(owner);
		this.method = method;
//		this.methodGenericString = method.toGenericString();
	}

	@Override
	public String getName() {
		return method.getName();
	}
	 
	@Override
	public String toString() {
		return method.toString();
	}


	@Override
	public boolean isApplicableTo(String methodName, Object... params) {
		if(!methodName.equals(method.getName())) {
			return false;
		}
		Class[] pTypes = method.getParameterTypes();
		if(pTypes.length != params.length) {
			return false;
		}
		for(int j=0; j<params.length; j++) {		
			if(pTypes[j].isPrimitive()) {
				Class t =  ReflectUtils.getWrapperClassForPrimivite(pTypes[j]);
				Class pt = params[j].getClass();
				String s1 = t.getSimpleName().toLowerCase();
				String s2 = pt.getSimpleName().toLowerCase();
				
				if(!s1.equals(s2)) {
					return false;	
				}
			} else if(!pTypes[j].isInstance(params[j])) {
				return false;
			}
		}
		return true;
	}


	@Override
	public Object execute(Object... params) throws Exception {
		method.setAccessible(true);
		Object result = method.invoke(getOwner(), params);
		return result;
	}
}

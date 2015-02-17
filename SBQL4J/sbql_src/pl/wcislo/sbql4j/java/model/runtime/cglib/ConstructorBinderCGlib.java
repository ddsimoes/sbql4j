package pl.wcislo.sbql4j.java.model.runtime.cglib;

import net.sf.cglib.reflect.FastConstructor;
import pl.wcislo.sbql4j.java.model.runtime.ConstructorBinder;
import pl.wcislo.sbql4j.java.utils.ReflectUtils;

public class ConstructorBinderCGlib extends ConstructorBinder {
 
	public final FastConstructor constr;	
	public ConstructorBinderCGlib(FastConstructor constr, Class owner) {
		super(owner);
		this.constr = constr;
//		this.methodGenericString = method.toGenericString();
	}
 
	@Override
	public String toString() {
		return constr.toString();
	}
	
	@Override
	public String getName() {
		return constr.getName();
	}


	@Override
	public boolean isApplicableTo(Class owner, Object... params) {
		if(!this.owner.equals(owner)) {
			return false;
		}
		Class[] pTypes = constr.getParameterTypes();
		if(pTypes.length != params.length) {
			return false;
		}
		for(int j=0; j<params.length; j++) {		
			if(pTypes[j].isPrimitive()) {
				Class pType = ReflectUtils.getWrapperClassForPrimivite(pTypes[j]);
//				Class t = pTypes[j];
				Class pt = params[j].getClass();
				String s1 = pType.getSimpleName().toLowerCase();
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
		Object result = constr.newInstance(params);
		return result;
	}
}

package pl.wcislo.sbql4j.db4o.result;

import java.lang.reflect.Method;

import com.db4o.internal.LocalTransaction;

import pl.wcislo.sbql4j.java.model.runtime.reflect.MethodBinderReflect;

public class Db4oMethodBinder extends MethodBinderReflect {
	private LocalTransaction trans;
	
	public Db4oMethodBinder(Method method, Object owner, LocalTransaction trans) {
		super(method, owner);
		this.trans = trans;
	}

	@Override
	public Object execute(Object... params) throws Exception {
		trans.objectContainer().activate(super.getOwner(), 1);
		return super.execute(params);
	}
	
	public LocalTransaction getTransaction() {
		return trans;
	}
}

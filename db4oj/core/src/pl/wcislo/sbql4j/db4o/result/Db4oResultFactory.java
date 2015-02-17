package pl.wcislo.sbql4j.db4o.result;

import pl.wcislo.sbql4j.java.model.runtime.reflect.JavaComplexObjectReflect;

import com.db4o.internal.LocalTransaction;

public class Db4oResultFactory {
	public Db4oObject createDb4oPersistentObject(JavaComplexObjectReflect obj, LocalTransaction trans) {
//		String name = classMeta.getName();
		return new Db4oObject(trans, obj);
	}
}

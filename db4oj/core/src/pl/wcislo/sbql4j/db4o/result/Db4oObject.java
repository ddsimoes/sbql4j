package pl.wcislo.sbql4j.db4o.result;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.java.model.runtime.JavaComplexObject;
import pl.wcislo.sbql4j.java.model.runtime.MethodBinder;
import pl.wcislo.sbql4j.java.model.runtime.reflect.JavaComplexObjectReflect;
import pl.wcislo.sbql4j.java.model.runtime.reflect.MethodBinderReflect;
import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.lang.types.ENVSType;

import com.db4o.internal.LocalTransaction;

public class Db4oObject extends JavaComplexObject {
	
	private LocalTransaction trans;
	private JavaComplexObjectReflect obj;
//	private List<ENVSType> nestedObjects;

	public Db4oObject(LocalTransaction trans, JavaComplexObjectReflect obj) {
		super(obj.value);
		this.trans = trans;
		this.obj = obj;
	}
	
	public List getNestedObjects() {
		return nestedObjects;
	}

	public Object getValue() {
		return obj.getValue();
	}

	public String toString() {
		return obj.toString();
	}

	public int hashCode() {
		return obj.hashCode();
	}

	public boolean equals(Object obj) {
		return obj.equals(obj);
	}
	
	@Override
	public void initNestedObjects() {
		trans.objectContainer().activate(obj.value, 1);
		obj.initNestedObjects();
		Db4oResultFactory fac = new Db4oResultFactory();
		this.nestedMethods = new ArrayList<Db4oMethodBinder>();
		this.nestedFields = new ArrayList<Binder>();
		
		
		for(Object o : obj.getNestedFields()) {
			if(o instanceof Binder) {
				Binder b = (Binder) o;
				if(b.object instanceof JavaComplexObjectReflect) {
					JavaComplexObjectReflect jo = (JavaComplexObjectReflect) b.object;
					Db4oObject dbObj = fac.createDb4oPersistentObject(jo, trans);
					Binder newB = new Binder(b.name, dbObj);
					this.nestedFields.add(newB);
				}
			} 
		}
		for(Object o : obj.getNestedMethods()) {
			if(o instanceof MethodBinderReflect) {
				MethodBinderReflect mb = (MethodBinderReflect) o;
				Db4oMethodBinder dbMb = new Db4oMethodBinder(mb.method, mb.getOwner(), trans);
				nestedMethods.add(dbMb);
			}
		}
		int x =0;
	}


	
}

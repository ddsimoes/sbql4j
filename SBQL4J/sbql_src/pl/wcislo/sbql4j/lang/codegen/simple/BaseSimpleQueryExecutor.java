package pl.wcislo.sbql4j.lang.codegen.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import pl.wcislo.sbql4j.exception.SBQLException;
import pl.wcislo.sbql4j.java.model.runtime.ConstructorBinder;
import pl.wcislo.sbql4j.java.model.runtime.JavaClass;
import pl.wcislo.sbql4j.java.model.runtime.JavaComplexObject;
import pl.wcislo.sbql4j.java.model.runtime.JavaObject;
import pl.wcislo.sbql4j.java.model.runtime.JavaPackage;
import pl.wcislo.sbql4j.java.model.runtime.MethodBinder;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectFactory;
//import pl.wcislo.sbql4j.java.model.runtime.reflect.JavaPackageDEPRECIATED.PackageClassCompleter;
import pl.wcislo.sbql4j.java.utils.Pair;
import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.model.collections.CollectionResult;
import pl.wcislo.sbql4j.model.collections.Sequence;
import pl.wcislo.sbql4j.util.Utils;
import pl.wcislo.sbql4j.xml.model.XmlId;
import pl.wcislo.sbql4j.xml.parser.store.XMLObjectStore;

public class BaseSimpleQueryExecutor {
	protected XMLObjectStore store;
	protected Stack<List<? extends ENVSType>> envs = new Stack<List<? extends ENVSType>>();
//	protected Stack<QueryResult> qres = new Stack<QueryResult>();
	protected JavaObjectFactory javaObjectFactory = JavaObjectAbstractFactory.getJavaObjectFactory();
	protected List<String> usedPackages;
	
	public BaseSimpleQueryExecutor(
			List<Pair<Object>> varList,
			List<Pair<Class>> staticList,
			List<String> usedPackages) { 
		this.usedPackages = usedPackages;
		List<Binder> initClasses = new ArrayList<Binder>();
		for(Pair<Class> clazz : staticList) {
			JavaClass jObj = javaObjectFactory.createJavaClassObject(clazz.val);
//			for(QueryResult jo : jObj) {
				initClasses.add(new Binder(clazz.name, jObj));
//			}
		}
		envs.push(initClasses);
		List<Binder> initVar = new ArrayList<Binder>();
		for(Pair<Object> var : varList) {
			CollectionResult jObj = (CollectionResult)javaObjectFactory.createJavaObject(var.val);
			initVar.add(new Binder(var.name, jObj));
//			for(JavaObject jo : jObj) {
//				
//			}
		}
		envs.push(initVar);
	}
	
	protected CollectionResult bind(String name) {
		Sequence tmpRes = new Sequence<List<QueryResult>>();
		boolean found = false;
		boolean wasSequence = false;
//		for(List<? extends ENVSType> o : envs) {
		for(int i=envs.size()-1; i >= 0 && !found; i--) {
			List<? extends ENVSType> o = envs.get(i);
			List<? extends ENVSType> section = (List<? extends ENVSType>) o;
			for(ENVSType secEl : section) {
				if(secEl instanceof Binder) {
					Binder b = (Binder) secEl;
					if(name.equals(b.name)) {
						tmpRes.add(b.object);
						found = true;
						if(b.object instanceof Sequence) {
							wasSequence = true;
						}
					}
				}
			}
		}
		if(tmpRes.isEmpty() && usedPackages.contains(name)) {
			tmpRes.add(new JavaPackage(name));
		}
		CollectionResult res;
		if(wasSequence) {
			return tmpRes;
		} else {
			res = new Bag();
			res.addAll(tmpRes);
			return res;
		}
		
	}
	
	protected QueryResult bindOne(String name) {
		CollectionResult res = bind(name);
		Iterator<QueryResult> it = res.iterator();
		if(it.hasNext()) {
			return it.next();
		} else {
			return null;
		}
	}
	
	protected MethodBinder bindMethod(String methodName, Object[] params) {
		
		for(int i=envs.size()-1; i >= 0; i--) {
			List<? extends ENVSType> o = envs.get(i);		
			List<? extends ENVSType> section = (List<? extends ENVSType>) o;
			for(ENVSType secEl : section) {
				if(secEl instanceof MethodBinder) {
					MethodBinder methodBinder = (MethodBinder) secEl;
					if(methodBinder.isApplicableTo(methodName, params)) {	
						return methodBinder;
					}
					
				}
			}
		}
		return null;
	}
	
	protected ConstructorBinder bindConstructor(Class constrOwner, Object[] params) {
		for(int i=envs.size()-1; i >= 0; i--) {
			List<? extends ENVSType> o = envs.get(i);		
			List<? extends ENVSType> section = (List<? extends ENVSType>) o;
			for(ENVSType secEl : section) {
				if(secEl instanceof ConstructorBinder) {
					ConstructorBinder constrBinder = (ConstructorBinder) secEl;
					if(constrBinder.isApplicableTo(constrOwner, params)) {	
						return constrBinder;
					}
					
				}
			}
		}
		return null;
	}
}

package pl.wcislo.sbql4j.lang.db4o.codegen.nostacks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.wcislo.sbql4j.db4o.Db4oSbqlQuery;
import pl.wcislo.sbql4j.db4o.result.Db4oObject;
import pl.wcislo.sbql4j.db4o.result.Db4oResultFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectFactory;
import pl.wcislo.sbql4j.java.model.runtime.reflect.JavaComplexObjectReflect;
import pl.wcislo.sbql4j.java.utils.Pair;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.model.collections.AbstractCollectionResult;
import pl.wcislo.sbql4j.model.collections.CollectionResult;
import pl.wcislo.sbql4j.util.Utils;

import com.db4o.foundation.Visitor4;
import com.db4o.internal.ClassMetadata;
import com.db4o.internal.ClassMetadataRepository;
import com.db4o.internal.LazyObjectReference;
import com.db4o.internal.LocalTransaction;
import com.db4o.internal.ObjectContainerBase;
import com.db4o.internal.Transaction;

public abstract class Db4oSBQLNoStacksQuery<R> implements Db4oSbqlQuery<R> {
	private final Expression queryRootNode;
	private final List<Pair<Object>> javaVars; 
	private final List<String> topLevelClassNames;
	
	public Db4oSBQLNoStacksQuery(Expression queryRootNode,
			List<Pair<Object>> javaVars, List<String> topLevelClassNames) {
		super();
		this.queryRootNode = queryRootNode;
		this.javaVars = javaVars;
		this.topLevelClassNames = topLevelClassNames;
	}

	@Override
	public R executeQuery(ObjectContainerBase ocb, Transaction t) {
    	final LocalTransaction transLocal = (LocalTransaction) t;
    	final List<Binder> resVar = new ArrayList<Binder>();
    	final List<Binder> resClass = new ArrayList<Binder>();

    	ClassMetadataRepository clazzRep = ocb.classCollection();
    	final JavaObjectFactory fac = JavaObjectAbstractFactory.getJavaObjectFactory();
    	final Db4oResultFactory facDb4o = new Db4oResultFactory();
    	clazzRep.iterateTopLevelClasses(new Visitor4<ClassMetadata>() {
    		public void visit(ClassMetadata obj) {
    			String cName = obj.getName();
    			if(topLevelClassNames.contains(cName)) {
	    			long[] ids = obj.getIDs(transLocal);
	    			for(long id : ids) {
	    				LazyObjectReference ref = transLocal.lazyReferenceFor((int) id);
	    				CollectionResult resC = fac.createJavaObject(ref.getObject());
	    				for(pl.wcislo.sbql4j.model.QueryResult q : resC) {
	    					if(q instanceof JavaComplexObjectReflect) {
	    						JavaComplexObjectReflect jcor = (JavaComplexObjectReflect) q;
	    						Db4oObject db4oObj = facDb4o.createDb4oPersistentObject(jcor, transLocal);
	    						resVar.add(new Binder(obj.getName(), db4oObj));
	    					} else {
	    						System.err.println("cos nie halo: "+q);
	    					}
	    					
	    				}
	    			}
    			}
//    			Class clazz = JdkReflector.toNative(obj.classReflector());
//    			resClass.add(new Binder(clazz.getName(), fac.createJavaClassObject(clazz)));
    		}
    	});
    	for(Pair<Object> p : javaVars) {
    		resVar.add(new Binder(p.name, fac.createJavaObject(p.val)));
    	}
    	Interpreter i = new Interpreter(resVar, resClass);
    	Object queryRes = i.evaluateExpression(queryRootNode); 
		if(queryRes != null) {
			if(queryRes instanceof AbstractCollectionResult) {
				queryRes = Utils.sbqlType2Java((AbstractCollectionResult)queryRes);
			} else {
				queryRes = Utils.sbqlType2Java((pl.wcislo.sbql4j.model.QueryResult)queryRes);
			}
			if(queryRes instanceof Collection) {
				Collection c = (Collection) queryRes;
				for(Object o : c) {
					ocb.activate(o);		
				}
			} else {
				ocb.activate(queryRes);	
			}
			
			return (R)queryRes;
			
		} else {
			return null;
		}
	}
}

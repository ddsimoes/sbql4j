package pl.wcislo.sbql4j.java.model.runtime.factory;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import pl.wcislo.sbql4j.java.model.runtime.JavaClass;
import pl.wcislo.sbql4j.java.model.runtime.JavaComplexObject;
import pl.wcislo.sbql4j.java.model.runtime.reflect.JavaPackageDEPRECIATED;
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.model.collections.CollectionResult;
import pl.wcislo.sbql4j.model.collections.Sequence;


public abstract class JavaObjectFactory {
	public CollectionResult createJavaObject(Object value) {
		if(value == null) {
			return new Bag();
		}
		CollectionResult result;
		if(value.getClass().isArray() || value instanceof List) {
			result = new Sequence();
		} else {
			result = new Bag();
		}
		
		if (value instanceof Collection) {
			Collection<?> c = (Collection<?>) value;
			for (Iterator<?> it = c.iterator(); it.hasNext();) {
				result.addAll(createJavaObject(it.next()));
			}
		} else if (value != null && value.getClass().isArray()) {
//			Array.get(value, index)
			
//			Object[] l = (Object[]) value;
			// for (Iterator<?> it = l.iterator(); it.hasNext();) {
			for (int i=0; i<Array.getLength(value); i++) {
				Object o = Array.get(value, i); 
				result.addAll(createJavaObject(o));
			}
//		} else if (value instanceof Class) {
//			result.add(createJavaClassObject((Class) value));
		} else {
			result.add(createJavaComplexObject(value));
		}

		return result;

	}

	public abstract <T> JavaComplexObject<T> createJavaComplexObject(T value);
	public abstract <T> JavaClass<T> createJavaClassObject(Class<T> clazz); 
	public abstract List<JavaPackageDEPRECIATED> createJavaPackages(List<Package> reflectPckgs);

}
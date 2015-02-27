package pl.wcislo.sbql4j.db4o.utils;

import java.util.Collection;

import pl.wcislo.sbql4j.java.model.runtime.Struct;

import com.db4o.internal.ObjectContainerBase;

public class DerefUtils {
	public static void activateResult(Object result, ObjectContainerBase ocb) {
		if(result == null) {
			return;
		} else if(result instanceof Struct) {
			activateStructResult((Struct) result, ocb);
		} else if(result instanceof Collection) {
			activateCollectionResult((Collection) result, ocb);
		} else {
			ocb.activate(result);
		}
	}
	
	public static void activateStructResult(Struct result, ObjectContainerBase ocb) {
		if(result == null) {
			return;
		} else {
			activateCollectionResult(result.values(), ocb);
		}
	}
	
	public static void activateCollectionResult(Collection result, ObjectContainerBase ocb) {
		if(result == null) {
			return;
		} else {
			for(Object resEl : result) {
				activateResult(resEl, ocb);
			}
		}
	}
	
}

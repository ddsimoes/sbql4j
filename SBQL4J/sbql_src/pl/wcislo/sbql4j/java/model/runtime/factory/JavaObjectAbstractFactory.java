package pl.wcislo.sbql4j.java.model.runtime.factory;

import pl.wcislo.sbql4j.java.model.runtime.factory.cglib.JavaObjectCGlibFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.reflect.JavaObjectReflectFactory;

public class JavaObjectAbstractFactory {

	private static JavaObjectFactory javaObjectFactory;
	public static JavaObjectFactory getJavaObjectFactory() {
		if(javaObjectFactory == null) {
//			javaObjectFactory = JavaObjectCGlibFactory.getInstance();
			javaObjectFactory = JavaObjectReflectFactory.getInstance();
		}
		return javaObjectFactory;
	}
}

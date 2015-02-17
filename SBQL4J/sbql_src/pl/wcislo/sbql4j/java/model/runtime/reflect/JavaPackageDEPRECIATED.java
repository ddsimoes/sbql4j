package pl.wcislo.sbql4j.java.model.runtime.reflect;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import pl.wcislo.sbql4j.java.model.runtime.JavaObject;
import pl.wcislo.sbql4j.java.model.runtime.factory.reflect.JavaObjectReflectFactory;
import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.lang.types.ComplexType;
import pl.wcislo.sbql4j.lang.types.ENVSType;

public class JavaPackageDEPRECIATED extends ComplexType<JavaObject> implements JavaObject {
	public final String name;
//	public final String fullPath;

	private JavaPackageDEPRECIATED parent;
	private List<JavaPackageDEPRECIATED> subPackages = new ArrayList<JavaPackageDEPRECIATED>();
	//do bindowania klas z pakietu na envs
	private PackageClassCompleter classCompleter = new PackageClassCompleter();
	
	public List<JavaPackageDEPRECIATED> getSubPackages() {
		return subPackages;
	}

	private List<JavaClassReflect<?>> classes; ;

	private boolean nestedObjectsInitialized = false;
	
	public JavaPackageDEPRECIATED(String name, JavaPackageDEPRECIATED parent) {
		this.name = name;
		this.parent = parent;
	}
	
	public String getFullPath() {
		JavaPackageDEPRECIATED p = this;
		StringBuilder sb = new StringBuilder();
		while(p != null) {
			sb.insert(0, p.name);
			p = p.parent;
			if(p != null) {
				sb.insert(0, ".");
			}
		}
//		sb.append(this.name);
		return sb.toString();
	}
	
	
	public List<Class> getJavaClasses() throws ClassNotFoundException, IOException {
		String pckgname = getFullPath();
		ArrayList<Class> classes = new ArrayList<Class>();
		// Get a File object for the package
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			String path = pckgname.replace('.', '/');
			Enumeration<URL> ress = cld.getResources(path);
			while(ress.hasMoreElements()) {
				URL resource = ress.nextElement();
				if (resource == null) {
//					throw new ClassNotFoundException("No resource for " + path);
					continue;
				}
				directory = new File(resource.getFile());
				if (directory.exists()) {
					// Get the list of the files contained in the package
					String[] files = directory.list();
					for (int i = 0; i < files.length; i++) {
						// we are only interested in .class files
						if (files[i].endsWith(".class")) {
							// removes the .class extension
							classes.add(Class.forName(pckgname + '.'
									+ files[i].substring(0, files[i].length() - 6)));
						}
					}
				} else {
					continue;
//					throw new ClassNotFoundException(pckgname
//							+ " does not appear to be a valid package");
				}
			}
//			URL resource = cld.getResource(path);
			
			
		} catch (NullPointerException x) {
//			throw new ClassNotFoundException(pckgname + " (" + directory
//					+ ") does not appear to be a valid package");
		}
		
//		Class[] classesA = new Class[classes.size()];
//		classes.toArray(classesA);
		return classes;
	}
	
	private void initNestedObjects() {
		//lets assume that nested packages are initialized
		this.classes = new ArrayList<JavaClassReflect<?>>();
		try {
			List<Class> cs = getJavaClasses();
			for(Class c : cs) {
//				Collection<JavaObject> nestedList = JavaObjectReflectFactory.getInstance().createJavaObject(c);
				this.classes.addAll((Collection<? extends JavaClassReflect<?>>) nestedObjects); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<ENVSType> nested() {
		if(!nestedObjectsInitialized) {
			initNestedObjects();
			nestedObjectsInitialized = true;
		}
		List<ENVSType> result = new ArrayList<ENVSType>();
//		for (JavaObject o : getNestedObjects()) {
//			result.add(new Binder(o.getName(), o));
//		}
		for (JavaPackageDEPRECIATED pck : this.subPackages) {
			result.add(new Binder(pck.name, pck));
		}
		result.add(this.classCompleter);
		return result;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<JP ").append(getFullPath()).append("");
		
//		for(Iterator<String> it = valFields.keySet().iterator(); it.hasNext(); ) {
//			String fName = it.next();
//			String fClass = valFields.get(fName).getType().getName();
//			sb.append(fName).append(":").append(fClass);
//			if(it.hasNext()) {
//				sb.append(", ");
//			}
//		}
//		for(int i=0; i<valFields.keySet().size(); i++ ) {
//			sb.append(valFields.keySet().get(i)+":"+valFieldTypes.get(i).getName());
//			if(i<valFieldNames.size() - 1) {
//				sb.append(", ");
//			}
//		}
		sb.append(">");
		return sb.toString();
	}

	public class PackageClassCompleter implements ENVSType {
		public JavaClassReflect<?> getClassInPackage(String name) {
			JavaClassReflect<?> c = null;
			String className = getFullPath()+"."+name;
			try {
				Class clazz = Class.forName(className); 
				c = JavaObjectReflectFactory.getInstance().createJavaClassObject(clazz);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
			return c;
		}
		
		@Override
		public String getName() {
			return "#PackageClassCompleter";
		}
		
		@Override
		public List<? extends ENVSType> nested() {
			return Collections.EMPTY_LIST;
		}
	}	
	
	@Override
	public Object getValue() {
		return name;
	}
}
 
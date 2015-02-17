package pl.wcislo.sbql4j.lang.views.info;

import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.lang.views.annotatinos.View;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.Type.ClassType;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCAnnotation;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCClassDecl;

public class ViewAnalysisHelper {
    public static boolean isClassViewDeclaration(JCClassDecl decl) {
//		boolean view = false;
		for(JCAnnotation ann : decl.mods.annotations) {
			if(ann.annotationType.toString().equals(View.class.getName())) {
				return true;
			}
		}
    	return false;
//    	ClassTypes ct = ClassTypes.getInstance();
//    	Type rViewCompilerType =  ct.getCompilerType(View.class);
//		boolean readableView = isSubtype(rViewCompilerType, decl.type, ct);
//    	return readableView;
    }
    
    public static boolean isClassViewDeclaration(ClassType type) {
    	ClassTypes ct = ClassTypes.getInstance();
    	Type rViewCompilerType =  ct.getCompilerType(View.class);
		boolean readableView = isSubtype(rViewCompilerType, type, ct);
    	return readableView;
    }
    
//    public static boolean isClassReadableViewDeclaration(JCClassDecl decl) {
//    	ClassTypes ct = ClassTypes.getInstance();
//    	Type rViewCompilerType =  ct.getCompilerType(ReadableView.class);
//		boolean readableView = isSubtype(rViewCompilerType, decl.type, ct);
//    	return readableView;
//    }
    
//    public static boolean isClassReadableViewDeclaration(ClassType type) {
//    	ClassTypes ct = ClassTypes.getInstance();
//    	Type rViewCompilerType =  ct.getCompilerType(ReadableView.class);
//		boolean readableView = isSubtype(rViewCompilerType, type, ct);
//    	return readableView;
//    }
    
    private static boolean isSubtype(Type t, Type candidate, ClassTypes ct) {
    	return ct.isSubClass(candidate, t);
    }
}

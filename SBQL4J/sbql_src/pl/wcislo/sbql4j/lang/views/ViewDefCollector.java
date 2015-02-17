package pl.wcislo.sbql4j.lang.views;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCClassDecl;

/**
 * Used for collecting view definitions used in query.
 * One collector instance should be used for one top level query
 * 
 * @author Emil
 *
 */
public class ViewDefCollector {
	private List<JCClassDecl> viewClasses = new ArrayList<JCClassDecl>();
	private ViewDefCollector() {
		
	}
	private static ViewDefCollector instance;
	public static ViewDefCollector getInstance() {
		if(instance == null) {
			instance = newInstance();
		}
		return instance;
	}
	public static ViewDefCollector newInstance() {
		instance = new ViewDefCollector();
		return instance;
	}
	
	public void addViewDef(JCClassDecl view) {
		if(!viewClasses.contains(view)) {
			//TODO sprawdzenie czy def jest widokiem
			viewClasses.add(view);
		}
	}
	public List<JCClassDecl> getViewDefs() {
		return viewClasses;
	}
}
package pl.wcislo.sbql4j.java.preprocessor;

import java.util.ArrayList;

public class JavaStackHelper {
	private ArrayList<String> stack = new ArrayList<String>();
	
	public JavaStackHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public void addStackLevel(String levName) {
		stack.add(levName);
	}
	public void removeStackLevel() {
		stack.remove(stack.size()-1);
	}
	public void addVariable(String var) {
		String stackLev = stack.get(stack.size()-1);
		stackLev = stackLev + ", " +var;
		stack.set(stack.size()-1, stackLev);
	}
}

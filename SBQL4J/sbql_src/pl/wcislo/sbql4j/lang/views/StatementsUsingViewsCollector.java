package pl.wcislo.sbql4j.lang.views;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.java.utils.SBQL4JStatement;

public class StatementsUsingViewsCollector {
	private static StatementsUsingViewsCollector instance;
	
	private List<SBQL4JStatement> stmts = new ArrayList<SBQL4JStatement>();
	
	public static StatementsUsingViewsCollector getInstance() {
		if(instance == null) {
			instance = newInstance();
		}
		return instance;
	}
	public static StatementsUsingViewsCollector newInstance() {
		instance = new StatementsUsingViewsCollector();
		return instance;
	}
	
	public void addStatement(SBQL4JStatement stmt) {
		stmts.add(stmt);
	}
	public List<SBQL4JStatement> getStatements() {
		return stmts;
	}
}
package pl.wcislo.sbql4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.wcislo.sbql4j.lang.optimiser.coderewrite.deadquery.DeadQueryOptimiser;
import pl.wcislo.sbql4j.lang.optimiser.coderewrite.independentsubqueries.IndependentSubQueryOptimizer;
import pl.wcislo.sbql4j.lang.parser.ParserCup;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.JavaParamExpression;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;
import pl.wcislo.sbql4j.xml.parser.store.XMLObjectStore;

public class Statement {
	
	private static final Log log = LogFactory.getLog(Statement.class);
	
	private Expression rootExpr;
	private Map<String, List<JavaParamExpression>> javaParamExprMap;
	private List<JavaParamExpression> javaParamExprList;
	private XMLObjectStore store;
	
	public Statement(String query) throws Exception {
		ParserCup cup = new ParserCup(query);
		cup.user_init();
		cup.parse(); 
		
//		Interpreter i = new Interpreter();
		this.rootExpr = cup.RESULT;
		this.javaParamExprList = cup.javaParams;
		initJavaParamExprMap();
	}
	
//	
//	public Statement(String query, JPSObjectStore store) throws Exception {
//		this.store = store;
//		ParserCup cup = new ParserCup(query);
//		cup.user_init();
//		cup.parse();
//		
//		this.rootExpr = cup.RESULT;
//		this.javaParamExprList = cup.javaParams;
//		initJavaParamExprMap();
//	}
	
	
//	public Statement(Expression rootExpr, List<JavaParamExpression> paramsExpr) {
//		this.rootExpr = rootExpr;
//		this.javaParamExprList = paramsExpr;
//		initJavaParamExprMap();
//	}
	
	public Object execute(TreeVisitor visitor) {
		for(JavaParamExpression expr : javaParamExprList) {
			if(expr.getParamValue() == null) {
				throw new RuntimeException("param "+expr.getParamName()+" not set");
			}
		}
		return visitor.evaluateExpression(rootExpr);
	}
	
	private void initJavaParamExprMap() {
		javaParamExprMap = new HashMap<String, List<JavaParamExpression>>();
		for(JavaParamExpression expr : this.javaParamExprList) {
			List<JavaParamExpression> list = javaParamExprMap.get(expr.getParamName());
			if(list == null) {
				list = new ArrayList<JavaParamExpression>();
				javaParamExprMap.put(expr.getParamName(), list);
			}
			list.add(expr);
		}
	}
	
	public void setParam(String name, Object value) {
		List<JavaParamExpression> list = javaParamExprMap.get(name);
		if(list == null) {
			throw new RuntimeException("no such param: "+name);
		}
		log.debug("Statement.setParam() name="+name+" val="+value);
		for(JavaParamExpression expr : list) {
			expr.setParamValue(value);
		}
	}
	
	public String[] getParamNames() {
		String[] res = new String[javaParamExprMap.keySet().size()];
		int i=0;
		for(Iterator<String> it = javaParamExprMap.keySet().iterator(); it.hasNext(); i++) {
			res[i] = it.next();
		}
		
		return res;
	}
	
	public Expression getRootExpr() {
		return rootExpr;
	}
	
//	public void optimiseQuery() {
//		new DeadQueryOptimiser().optimise(rootExpr);
//		
//		new IndependentSubQueryOptimizer().optimise(rootExpr);
//		
//	}
}

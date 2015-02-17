package pl.wcislo.sbql4j.java.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.wcislo.sbql4j.exception.ParserException;
import pl.wcislo.sbql4j.java.model.compiletime.BinderSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.preprocessor.PreprocessorRun.Config;
import pl.wcislo.sbql4j.lang.db4o.indexes.IndexManager;
import pl.wcislo.sbql4j.lang.db4o.indexes.IndexMetadataStore;
import pl.wcislo.sbql4j.lang.db4o.indexes.IndexMetadataXMLStore;
import pl.wcislo.sbql4j.lang.optimiser.coderewrite.db4oindex.Db4oIndexOptimiser;
import pl.wcislo.sbql4j.lang.optimiser.coderewrite.deadquery.DeadQueryOptimiser;
import pl.wcislo.sbql4j.lang.optimiser.coderewrite.independentsubqueries.IndependentSubQueryOptimizer;
import pl.wcislo.sbql4j.lang.optimiser.view.DerefExpressionMarker;
import pl.wcislo.sbql4j.lang.parser.ParserCup;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.pretty.QueryPretty;
import pl.wcislo.sbql4j.lang.tree.visitors.ExpressionResultNameDecorator;
import pl.wcislo.sbql4j.lang.tree.visitors.Interpreter;
import pl.wcislo.sbql4j.lang.tree.visitors.TypeChecker;
import pl.wcislo.sbql4j.lang.views.StatementsUsingViewsCollector;
import pl.wcislo.sbql4j.lang.views.replace.ViewInvocationReplacer;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.model.collections.AbstractCollectionResult;
import pl.wcislo.sbql4j.tools.javac.comp.JavaNameResolver;
import pl.wcislo.sbql4j.util.Utils;

public class SBQL4JStatement {
	private static final Log log = LogFactory.getLog(SBQL4JStatement.class);
	
//	public JavaStatement(TreeNode parsedQuery, Map<String, Object> params) {
	private String originalQuery;
	private Expression queryRootNode;
//	private Statement stmt;
	private int startPos;
	
	private List<Pair<Object>> varList;
	private List<Pair<Class>> staticList;
	private List<String> usedPackageList;
	
	//analiza statyczna
	private JavaNameResolver jnr;
	private Signature resultType;
	private boolean reportTypeErrors = true;
	private List<BinderSignature> queryViewContext;
	private boolean viewUsed;
	private boolean xmlUsed;
	
	


	public static void main(String[] args) {
		List<pl.wcislo.sbql4j.java.utils.Pair<Object>> list = java.util.Arrays.asList(new pl.wcislo.sbql4j.java.utils.Pair<Object>("this",null), new pl.wcislo.sbql4j.java.utils.Pair<Object>("sc",null));
		List<pl.wcislo.sbql4j.java.utils.Pair<Class>> list2 = java.util.Arrays.asList(new pl.wcislo.sbql4j.java.utils.Pair<Class>("this",null), new pl.wcislo.sbql4j.java.utils.Pair<Class>("sc",null));
		
		new SBQL4JStatement("", java.util.Arrays.asList(new pl.wcislo.sbql4j.java.utils.Pair<Object>("this",null), new pl.wcislo.sbql4j.java.utils.Pair<Object>("sc",null))
				, java.util.Arrays.asList(new pl.wcislo.sbql4j.java.utils.Pair<Class>("this",null), new pl.wcislo.sbql4j.java.utils.Pair<Class>("sc",null))
				, java.util.Arrays.asList(new String[] {"java", "util"}));
//		new JavaStatement("", list, list2);
//				 Arrays.asList(java.util.Arrays.asList(new pl.wcislo.sbql4j.java.utils.Pair<Class>("this",Object.class), new pl.wcislo.sbql4j.java.utils.Pair<Class>("sc",Object.class))));
		
	}
	
	/**
	 * Konstruktor dla wykonania zapytania w runtime aplikacji
	 * 
	 * @param query
	 * @param varList
	 * @param staticList
	 */
	public SBQL4JStatement(String query, List<Pair<Object>> varList, List<Pair<Class>> staticList, List<String> usedPackagesList) {
		log.debug("query="+query+" varList="+varList+" staticList="+staticList);
		this.originalQuery = query;
		this.varList = varList;
		this.staticList = staticList;
		if(usedPackagesList == null) {
			this.usedPackageList = new ArrayList<String>();
		} else {
			this.usedPackageList = usedPackagesList;
		}
//		this.packages = new ArrayList( Arrays.asList(Package.getPackages()) );
		try {
			this.queryRootNode = parseQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Konstruktor dla wykonania statycznego przebiegu zapytania
	 * @param query
	 */
	public SBQL4JStatement(String query, JavaNameResolver jnr, int startPos, boolean reportTypeErrors,
			List<BinderSignature> viewContext) throws ParserException {
		log.debug("query="+query);
		this.originalQuery = query;
//		this.packages = new ArrayList( Arrays.asList(Package.getPackages()) );
		this.jnr = jnr;
		this.startPos = startPos;
		this.reportTypeErrors = reportTypeErrors;
		this.queryViewContext = viewContext;
		try {
			this.queryRootNode = parseQuery(query);
		} catch (ParserException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public SBQL4JStatement(String query, int startPos) throws ParserException {
		this.originalQuery = query;
		this.startPos = startPos;
		try {
			this.queryRootNode = parseQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Deprecated
//	public JavaStatement(String query, Object... params) {
//		this.query = query;
//		
//		try {
//			this.stmt = new Statement(query);
//			String[] paramNames = stmt.getParamNames();
//			Arrays.sort(paramNames);
//			int i=0;
//			for(String pName : paramNames) {
//				stmt.setParam(pName, params[i++]);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	private List<Package> createPackages() {
		Package[] pcks = Package.getPackages();
		
		return Arrays.asList(pcks);
	}

	public Object execute() {
		Interpreter i = new Interpreter(varList, staticList, usedPackageList);
		Object o = i.evaluateExpression(queryRootNode);
		if(o != null) {
			if(o instanceof AbstractCollectionResult) {
				o = Utils.sbqlType2Java((AbstractCollectionResult)o);
			} else {
				o = Utils.sbqlType2Java((QueryResult)o);
			}
			return o;
		} else {
			return null;
		}
	}
	
	public Signature staticCheck() {
		TypeChecker checker = new TypeChecker(jnr, queryViewContext, this);
		Signature sig = checker.evaluateExpression(queryRootNode);
//		Signature sig = null; 
//		if(o instanceof ValueSignature) {
//			sig = (ValueSignature)o;
//		} else if(o instanceof BinderSignature) {
//			BinderSignature bs = (BinderSignature)o;
//			sig = bs.getDerefSignatureWithCardinality();
//		} else if(o instanceof StructSignature) {
//			sig = (StructSignature)o;
//		}
//		ValueSignature sig = (ValueSignature) stmt.execute(checker);
		this.resultType = sig;	
		return sig;
	}
	
	public void optimise() {
		if(!Config.optimiseDeadQueries && !Config.optimiseIndependentQueries && !Config.optimiseDb4oIndexes) {
			return;
		}
		log.info("Query before optimization: ");
		log.info(QueryPretty.printQuery(queryRootNode));
		TypeChecker tc = new TypeChecker(this.jnr, queryViewContext, this);
		if(Config.optimiseDeadQueries) {
			DeadQueryOptimiser op1 = new DeadQueryOptimiser(queryRootNode, tc);
			this.queryRootNode = op1.optimise();
			this.resultType = tc.getResultType();
			log.info("Query after dead subqueries optimization: ");
			log.info(QueryPretty.printQuery(queryRootNode));
		}
		if(Config.optimiseIndependentQueries) {
			IndependentSubQueryOptimizer op2 = new IndependentSubQueryOptimizer(queryRootNode, tc);
			this.queryRootNode = op2.optimise();
			this.resultType = tc.getResultType();
			log.info("Query after independent subqueries optimization: ");
			log.info(QueryPretty.printQuery(queryRootNode));
		}
		if(Config.optimiseDb4oIndexes && Config.db4oMetabase != null) {
			IndexMetadataStore indexMetadataStore = new IndexMetadataXMLStore(Config.db4oMetabase);
			IndexManager im = new IndexManager(indexMetadataStore);
			Db4oIndexOptimiser db4oIndexOptimiser = new Db4oIndexOptimiser(queryRootNode, tc, im);
			this.queryRootNode = db4oIndexOptimiser.optimise();
			this.resultType = tc.getResultType();
			log.info("Query after db4o index optimization: ");
			log.info(QueryPretty.printQuery(queryRootNode));
		}
//		DerefExpressionMarker derefM = new DerefExpressionMarker();
//		derefM.evaluateExpression(queryRootNode);
//		this.queryRootNode = derefM.getRootExpression();
//		log.info("Query after deref marker: ");
//		log.info(QueryPretty.printQuery(queryRootNode));
	}
	
	public void decorateNames() {
		ExpressionResultNameDecorator decorator = new ExpressionResultNameDecorator();
		decorator.evaluateExpression(queryRootNode);
//		stmt.execute(decorator);
	}
	
	public Expression replaceViewDefinitions() {
		ViewInvocationReplacer repl = new ViewInvocationReplacer();
		this.queryRootNode = repl.evaluateExpression(queryRootNode);
		return queryRootNode;
	}
	
	public Expression getExpressionTree() {
		return queryRootNode;
	}
	private Expression parseQuery(String query) throws Exception {
		ParserCup cup = new ParserCup(query);
		cup.user_init();
		cup.parse(); 
		
		return cup.RESULT;
	}	
	public void markStatementAsUsingViews() {
		StatementsUsingViewsCollector col = StatementsUsingViewsCollector.getInstance();
		if(!col.getStatements().contains(this)) {
			col.addStatement(this);
		}
		this.viewUsed = true;
	}
	
	public boolean isViewUsed() {
		return viewUsed;
	}
	
	public boolean isXmlUsed() {
		return xmlUsed;
	}

	public void setXmlUsed(boolean xmlUsed) {
		this.xmlUsed = xmlUsed;
	}
}
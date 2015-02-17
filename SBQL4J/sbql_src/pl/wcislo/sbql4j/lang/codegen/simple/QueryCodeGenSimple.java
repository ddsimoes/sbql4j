package pl.wcislo.sbql4j.lang.codegen.simple;

import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.preprocessor.PreprocessorRun;
import pl.wcislo.sbql4j.lang.codegen.CodeGenerator;
import pl.wcislo.sbql4j.lang.parser.expression.AsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.BinarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.CloseByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ComaExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ConditionalExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ConstructorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.DerefExpression;
import pl.wcislo.sbql4j.lang.parser.expression.DotExpression;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.ForEachExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ForallExpression;
import pl.wcislo.sbql4j.lang.parser.expression.ForanyExpression;
import pl.wcislo.sbql4j.lang.parser.expression.GroupAsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.JoinExpression;
import pl.wcislo.sbql4j.lang.parser.expression.LiteralExpression;
import pl.wcislo.sbql4j.lang.parser.expression.MethodExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression;
import pl.wcislo.sbql4j.lang.parser.expression.RangeExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorComma;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorType;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class QueryCodeGenSimple implements TreeVisitor {
	protected final boolean printExprTrace = PreprocessorRun.Config.printExpressionTrace;
	public final StringBuilder sb = new StringBuilder();
	private OperatorTreeCodeGenSimple operatorGen = new OperatorTreeCodeGenSimple();
	private CodeGenerator codeGen;

	public QueryCodeGenSimple(CodeGenerator codeGen) {
		super();
		this.codeGen = codeGen;
	}

	public String generateIdentifier(String prefix) {
		return codeGen.genIdent(prefix);
	}
	
	@Override
	public String evaluateExpression(Expression expr) {
		sb.append("//evaluateExpression - start\n");
		sb.append("try {\n");
			expr.accept(this, null);
		sb.append("if(!qres.isEmpty()) {\n");
		String identO = generateIdentifier("o");
		sb.append("Object "+identO+" = qres.pop();\n");
		
		
		sb.append("if("+identO+" != null) {                                   \n");
		sb.append("	if("+identO+" instanceof AbstractCollectionResult) {              \n");
		sb.append("		"+identO+" = Utils.sbqlType2Java((AbstractCollectionResult)"+identO+");\n");
		sb.append("	} else {                                                 \n");
		sb.append("		"+identO+" = Utils.sbqlType2Java((QueryResult)"+identO+");             \n");
		sb.append("	}                                                        \n");
		sb.append("return ("+expr.getSignature().getJavaTypeString()+") "+identO+";\n");
		sb.append("} else {\n");
		sb.append("return null;\n");
		sb.append("}\n");
		sb.append("} else {\n");
		sb.append("return null;\n");
		sb.append("}\n");
		sb.append("} catch(RuntimeException e) {\n");
		sb.append("e.printStackTrace();\n");
		sb.append("throw new SBQLException(e.getMessage(), e);\n");
		sb.append("}\n");
		sb.append("//evaluateExpression - end\n");
		return sb.toString();
	}

	@Override
	public Object visitAsExpression(AsExpression expr, Object object) {
		expr.ex1.accept(this, object);
		sb.append("//visitAsExpression - start\n");
//		sb.append("{\n");
		String qrIdent = generateIdentifier("qr");
		String resultIdent = generateIdentifier("result");
		sb.append("QueryResult "+qrIdent+" = qres.pop(); \n");
		sb.append("CollectionResult "+resultIdent+"; \n");
		Signature s1 = expr.ex1.getSignature().getDerefSignatureWithCardinality();
		if(s1.getColType() == SCollectionType.SEQUENCE) {
			sb.append("	"+resultIdent+" = new Sequence(); \n");	
		} else {
			sb.append("	"+resultIdent+" = new Bag(); \n");
		}
		String bIdent = generateIdentifier("b");
		sb.append("CollectionResult "+bIdent+" = Utils.objectToCollection("+qrIdent+");	 \n");
		String oIdent = generateIdentifier("o");
		sb.append("for(QueryResult "+oIdent+" : "+bIdent+") {                    \n");
		sb.append("	"+resultIdent+".add(new Binder(\""+expr.identifier.val+"\", "+oIdent+"));      \n");
		sb.append("}                                                     		 \n");
		sb.append("qres.push(Utils.collectionToObject("+resultIdent+"));         \n");
//		sb.append("}\n");
		sb.append("//visitAsExpression - end\n");
		return null;
	}

	@Override
	public Object visitBinarySimpleOperatorExpression(BinarySimpleOperatorExpression expr, Object object) {
		sb.append("//visitBinarySimpleOperatorExpression - start \n");
		expr.ex1.accept(this, object);
		expr.ex2.accept(this, object);
		expr.op.accept(operatorGen, this, expr, expr.ex1, expr.ex2);
//		expr.op.generateSimpleCode(this, expr.ex1, expr.ex2, expr);
		sb.append("//visitBinarySimpleOperatorExpression - end \n");
		return null;
	}

	@Override
	public Object visitCloseByExpression(CloseByExpression expr, Object object) {
		sb.append("//visitCloseByExpression - start \n");
		expr.ex1.accept(this, object);
		String identBag = generateIdentifier("bag");
		String identQ = generateIdentifier("q");
		String identResult = generateIdentifier("result");
		String identI = generateIdentifier("i");
		String identType = generateIdentifier("type");
		String identRes = generateIdentifier("res");
		
		
		sb.append("List<QueryResult> "+identBag+" = new ArrayList<QueryResult>(); \n");
		sb.append("QueryResult "+identQ+" = qres.pop();                           \n");
		sb.append("if("+identQ+" instanceof Bag) {                                \n");
		sb.append("	"+identBag+".addAll((Bag) "+identQ+");                                 \n");
		sb.append("} else {                                              \n");
		sb.append("	"+identBag+".add("+identQ+");                                          \n");
		sb.append("}                                                     \n");
		sb.append("Bag "+identResult+" = new Bag<Collection<QueryResult>>();      \n");
	    sb.append("                                                      \n");
		sb.append(""+identResult+".addAll("+identBag+");                                   \n");
		sb.append("int "+identI+" = 0;                                              \n");
		sb.append("while("+identI+" < "+identBag+".size() ) {                              \n");
		sb.append("	QueryResult "+identType+" = "+identBag+".get("+identI+");                       \n");
		sb.append("	if("+identType+" instanceof XmlId) {                          \n");
		sb.append("		"+identType+" = store.get((XmlId) "+identType+");                  \n");
		sb.append("	}                                                    \n");
		sb.append("	                                                     \n");
		sb.append("	if("+identType+".nested() == null) {                          \n");
		sb.append("		envs.push(new ArrayList());                      \n");
		sb.append("	} else {                                             \n");
		sb.append("		envs.push(type.nested());	                     \n");
		sb.append("	}                                                    \n");
		expr.ex2.accept(this, object);
		sb.append("	QueryResult "+identRes+" = qres.pop();                        \n");
		sb.append("	if("+identRes+" instanceof CollectionResult) {                \n");
		sb.append("		"+identResult+".addAll((CollectionResult)"+identRes+"); \n");
		sb.append("		"+identBag+".addAll((CollectionResult)"+identRes+"); \n");
		sb.append("	} else {                                             \n");
		sb.append("		"+identResult+".add("+identRes+");                                 \n");
		sb.append("		"+identBag+".add("+identRes+");                                    \n");
		sb.append("	}                                                    \n");
		sb.append("	envs.pop();                                          \n");
		sb.append("	"+identI+"++;                                                 \n");
		sb.append("}                                                     \n");
		sb.append("qres.push("+identResult+");                                    \n");
		sb.append("//visitCloseByExpression - end \n");
		return object;
	}

	@Override
	public Object visitComaExpression(ComaExpression expr, Object object) {
		sb.append("//visitCommaExpression - start \n");
		expr.ex1.accept(this, object);
		expr.ex2.accept(this, object);
		
		OperatorComma op = new OperatorComma(OperatorType.COMA);
		op.accept(operatorGen, this, expr, new Expression[] {expr.ex1, expr.ex2});
//		op.generateSimpleCode(this, expr.ex1, expr.ex2, expr);
		sb.append("//visitCommaExpression - end \n");
		return object;
	}

	@Override
	public Object visitConditionalExpression(ConditionalExpression expr,
			Object object) {
		expr.conditionExpr.accept(this, object);
		String identCond = generateIdentifier("cond");
		String identB = generateIdentifier("b");
		String identRes = generateIdentifier("res");
		sb.append("//visitConditionalExpression - start \n");
		sb.append("QueryResult "+identCond+" = qres.pop(); \n");
		sb.append("Boolean "+identB+" = (Boolean) Utils.toSimpleValue("+identCond+", store); \n");
		sb.append("QueryResult "+identRes+"; \n");
		sb.append("if("+identB+") { \n");
		expr.trueExpr.accept(this, object);
		sb.append("	"+identRes+" = qres.pop(); \n");
		sb.append("} else { \n");
		expr.falseExpr.accept(this, object);
		sb.append("	"+identRes+" = qres.pop(); \n");
		sb.append("} \n");
		sb.append("qres.push("+identRes+"); \n");
		sb.append("//visitConditionalExpression - end \n");
		
		return object;
	}

	@Override
	public Object visitConstructorExpression(ConstructorExpression expr,
			Object object) {
		String identOwnerClass = generateIdentifier("ownerClass");
		String identParams = generateIdentifier("params");
		String identS = generateIdentifier("s");
		String identJavaParams = generateIdentifier("javaParams");
		String identCB = generateIdentifier("cb");
		String identResult = generateIdentifier("result");
		String identResultSbql = generateIdentifier("resultSbql");
		
		sb.append("//visitConstructorExpression - start \n");
		sb.append("JavaClass "+identOwnerClass+" = null; \n");
		if(expr.classNameExpr != null) {
			expr.classNameExpr.accept(this, object);
			sb.append(""+identOwnerClass+" = (JavaClass) Utils.collectionToObject(qres.pop()); \n");
		} else if(expr.classNameLiteral != null) {
			sb.append(""+identOwnerClass+" = (JavaClass) Utils.collectionToObject(bind("+expr.classNameLiteral+")); \n");
		}
		sb.append("Object[] "+identParams+" = new Object[0]; \n");
		if(expr.paramsExpression != null) {
			expr.paramsExpression.accept(this, object);
			sb.append("StructSBQL "+identS+" = Utils.objectToStruct(qres.pop()); \n");
			sb.append("Collection "+identJavaParams+" = Utils.sbqlStuct2JavaList("+identS+"); \n");
			sb.append(""+identParams+" = "+identJavaParams+".toArray(); \n");
		}
		sb.append("envs.push("+identOwnerClass+".nested()); \n");
		sb.append("ConstructorBinder "+identCB+" = bindConstructor("+identOwnerClass+".value, "+identParams+"); \n");
		sb.append("envs.pop(); \n");
		sb.append("if("+identCB+" == null) { \n");
		sb.append("	System.err.println(\"nie ma bindera do konstruktora \"+"+identOwnerClass+".value); \n");
		sb.append("} \n");
		
		sb.append("Object "+identResult+" = null; \n");
		sb.append("QueryResult "+identResultSbql+" = null; \n");
		sb.append("try { \n");
		sb.append("	"+identResult+" = "+identCB+".execute("+identParams+"); \n");
		sb.append("} catch (Exception e) { \n");
		sb.append("	throw new RuntimeException(e.getMessage(), e); \n");
		sb.append("} \n");
		sb.append("if("+identResult+" == null) { \n");
		sb.append("	"+identResultSbql+" = new Bag(); \n");
		sb.append("} else { \n");
		sb.append("	"+identResultSbql+" = javaObjectFactory.createJavaComplexObject("+identResult+"); \n");
		sb.append("} \n");
		sb.append("qres.push("+identResultSbql+"); \n");
		sb.append("//visitConstructorExpression - end \n");
		return object;
	}

	@Override
	public Object visitDerefExpression(DerefExpression derefExpression,
			Object object) {
		String identResult = generateIdentifier("result");
		String identO = generateIdentifier("o");
		String identOo = generateIdentifier("oo");
		sb.append("//visitDerefExpression - start \n");
		derefExpression.ex1.accept(this, object);
		sb.append("Bag "+identResult+" = new Bag(); \n");
		sb.append("QueryResult "+identO+" = qres.pop(); \n");
		sb.append("if("+identO+" instanceof CollectionResult) { \n");
		sb.append("	for (QueryResult "+identOo+" : (CollectionResult)"+identO+") { \n");
		sb.append("		if("+identOo+" instanceof XmlId) { \n");
		sb.append("			"+identResult+".add(store.get((XmlId) "+identOo+")); \n");
		sb.append("		} else { \n");
		sb.append("			"+identResult+".add("+identOo+"); \n");	
		sb.append("		} \n");
		sb.append("	} \n");
		sb.append("} else { \n");
		sb.append("	if("+identO+" instanceof XmlId) { \n");
		sb.append("		"+identResult+".add(store.get((XmlId) "+identO+")); \n");
		sb.append("	} else { \n");
		sb.append("		"+identResult+".add("+identO+"); \n");	
		sb.append("	} \n");
		sb.append("} \n");
		sb.append("qres.push("+identResult+"); \n");
		sb.append("//visitDerefExpression - end \n");
		return null;
	}

	@Override
	public Object visitDotExpression(DotExpression dotExpression, Object object) {
		String identDotLeftCol = generateIdentifier("dotLeftCol");
		String identQ = generateIdentifier("q");
		String identResult = generateIdentifier("result");
		String identO = generateIdentifier("o");
		String identType = generateIdentifier("type");
		String identEnvsStack = generateIdentifier("envsStack");
		String identLoopIndexBinder = generateIdentifier("loopIndexBinder");
		String identB = generateIdentifier("b");
		String identI = generateIdentifier("i");
		String identOo = generateIdentifier("oo");
		sb.append("//visitDotExpression - start \n");
		dotExpression.ex1.accept(this, object);
		
		sb.append("QueryResult "+identQ+" = qres.lastElement(); \n");
		sb.append("CollectionResult "+identDotLeftCol+" = Utils.objectToCollection("+identQ+"); \n");
		sb.append("CollectionResult "+identResult+" = "+identDotLeftCol+".createSameType(); \n");
		sb.append("int "+identI+"=0; \n");
		sb.append("for(Object "+identO+" : "+identDotLeftCol+") { \n");
		sb.append("	QueryResult "+identType+" = (QueryResult) "+identO+"; \n");
//		sb.append("	if("+identType+" instanceof XmlId) { \n");
//		sb.append("		"+identType+" = store.get((XmlId) "+identType+"); \n");
//		sb.append("	} \n");
		sb.append("	List "+identEnvsStack+"; \n");
//		sb.append("	if("+identType+".nested() == null) { \n");
//		sb.append("		"+identEnvsStack+" = new ArrayList(); \n");
//		sb.append("	} else { \n");
		sb.append("		"+identEnvsStack+" = "+identType+".nested(); \n");	
//		sb.append("	} \n");
		sb.append("	Binder "+identLoopIndexBinder+" = new Binder(\"$index\", javaObjectFactory.createJavaComplexObject(new Integer("+identI+"++))); \n");
		sb.append("	"+identEnvsStack+".add("+identLoopIndexBinder+"); \n");
		sb.append("	envs.push("+identEnvsStack+"); \n");
		dotExpression.ex2.accept(this, object);
		sb.append("	CollectionResult "+identB+" = Utils.objectToCollection(qres.pop()); \n");
		sb.append("	for(QueryResult "+identOo+" : "+identB+") { \n");
		sb.append("		"+identResult+".add("+identOo+"); \n");
		sb.append("	} \n");
		sb.append("	envs.pop(); \n");
		sb.append("} \n");
		sb.append("qres.pop(); \n");
//		sb.append("qres.push(Utils.collectionToObject("+identResult+")); \n");
		sb.append("qres.push("+identResult+"); \n");
		sb.append("//visitDotExpression - end \n");
		return null;
	}

	@Override
	public Object visitForEachExpression(ForEachExpression expr, Object object) {
		String identBag = generateIdentifier("bag");
		String identQ = generateIdentifier("q");
		String identT = generateIdentifier("t");
		sb.append("//visitForEachExpression - start \n");
		expr.toIterate.accept(this, object);
		sb.append("Bag "+identBag+"; \n"); 
		sb.append("QueryResult "+identQ+" = qres.pop(); \n"); 
		sb.append("if("+identQ+" instanceof Bag) { \n");
		sb.append("	"+identBag+" = (Bag) "+identQ+"; \n");
		sb.append("} else { \n");
		sb.append("	"+identBag+" = new Bag(); \n");
		sb.append("	"+identBag+".add("+identQ+"); \n");
		sb.append("} \n");
		sb.append("for(QueryResult "+identT+" : "+identBag+") { \n");
		sb.append("	if(t instanceof XmlId) { \n");
		sb.append("		"+identT+" = store.get((XmlId) "+identT+"); \n");
		sb.append("	} \n");
		sb.append("	for(Expression e : expr.exprs) { \n");
		sb.append("		if("+identT+".nested() == null) { \n");
		sb.append("			envs.push(new ArrayList()); \n");
		sb.append("		} else { \n");
		sb.append("			envs.push("+identT+".nested()); \n");	
		sb.append("		} \n");
		sb.append("		e.accept(this, object); \n");
		sb.append("		envs.pop(); \n");
		sb.append("		qres.pop(); \n");
		sb.append("	} \n");
		sb.append("} \n");
		sb.append("//visitForEachExpression - end \n");
		return null;
	}

	@Override
	public Object visitForallExpression(ForallExpression expr, Object object) {
		String identResult = generateIdentifier("result");
		String identC1 = generateIdentifier("c1");
		String identLeftObj = generateIdentifier("leftObj");
		String identEnvsStack = generateIdentifier("envsStack");
		String identLoopIndexBinder = generateIdentifier("loopIndexBinder");
		String identRightObj = generateIdentifier("rightObj");
		String identB = generateIdentifier("b");
		String identI = generateIdentifier("i");
		
		sb.append("//visitForallExpression - start \n");
		expr.ex1.accept(this, object);
		sb.append("CollectionResult "+identC1+" = Utils.objectToCollection(qres.lastElement()); \n");
		sb.append("Boolean "+identResult+" = true; \n");
		sb.append("int "+identI+"=0; \n");
		sb.append("for(QueryResult "+identLeftObj+" : "+identC1+") { \n");
		sb.append("	if("+identLeftObj+" instanceof XmlId) { \n");
		sb.append("		"+identLeftObj+" = store.get((XmlId) "+identLeftObj+"); \n");
		sb.append("	} \n");
		sb.append("	List "+identEnvsStack+"; \n");
		sb.append("	if("+identLeftObj+".nested() == null) { \n");
		sb.append("		"+identEnvsStack+" = new ArrayList(); \n");
		sb.append("	} else { \n");
		sb.append("		"+identEnvsStack+" = "+identLeftObj+".nested(); \n");	
		sb.append("	} \n");
		sb.append("	Binder "+identLoopIndexBinder+" = new Binder(\"$index\", javaObjectFactory.createJavaComplexObject(new Integer("+identI+"++))); \n");
		sb.append("	"+identEnvsStack+".add("+identLoopIndexBinder+"); \n");
		sb.append("	envs.push("+identEnvsStack+"); \n");	
		expr.ex2.accept(this, "+identLeftObj+");
		sb.append("	envs.pop(); \n");
		sb.append("	QueryResult "+identRightObj+" = qres.pop(); \n");
		sb.append("	boolean "+identB+" = (Boolean) Utils.toSimpleValue("+identRightObj+", store); \n");
		sb.append("	if(!"+identB+") { \n");
		sb.append("		"+identResult+" = false; \n");
		sb.append("		break; \n");
		sb.append("	} \n");
		sb.append("} \n");
		sb.append("qres.pop(); \n");
		sb.append("qres.push(javaObjectFactory.createJavaComplexObject("+identResult+")); \n");
		sb.append("//visitForallExpression - end \n");
		return null;
	}

	@Override
	public Object visitForanyExpression(ForanyExpression expr, Object object) {
		String identResult = generateIdentifier("result");
		String identC1 = generateIdentifier("c1");
		String identLeftObj = generateIdentifier("leftObj");
		String identRightObj = generateIdentifier("rightObj");
		String identB = generateIdentifier("b");
		sb.append("//visitForanyExpression - start \n");
		expr.ex1.accept(this, object);
		sb.append("CollectionResult "+identC1+" = Utils.objectToCollection(qres.lastElement()); \n");
		sb.append("Boolean "+identResult+" = false; \n");
		sb.append("for(QueryResult "+identLeftObj+" : "+identC1+") { \n");
		sb.append("	if("+identLeftObj+" instanceof XmlId) { \n");
		sb.append("		"+identLeftObj+" = store.get((XmlId) "+identLeftObj+"); \n");
		sb.append("	} \n");
		sb.append("	envs.push("+identLeftObj+".nested()); \n");	
		expr.ex2.accept(this, "+identLeftObj+");
		sb.append("	envs.pop(); \n");
		sb.append("	QueryResult "+identRightObj+" = qres.pop(); \n");
		sb.append("	boolean "+identB+" = (Boolean) Utils.toSimpleValue("+identRightObj+", store); \n");
		sb.append("	if("+identB+") { \n");
		sb.append("		"+identResult+" = true; \n");
		sb.append("		break; \n");
		sb.append("	} \n");
		sb.append("} \n");
		sb.append("qres.pop(); \n");
		sb.append("qres.push(javaObjectFactory.createJavaComplexObject("+identResult+")); \n");
		sb.append("//visitForanyExpression - end \n");
		return null;
	}

	@Override
	public Object visitGroupAsExpression(GroupAsExpression expr, Object object) {
		String identBag = generateIdentifier("bag");
		sb.append("//visitGroupAsExpression - start \n");
		expr.ex1.accept(this, object);
		sb.append("CollectionResult "+identBag+" = Utils.objectToCollection(qres.pop()); \n");
		sb.append("qres.push(new Binder(\""+expr.identifier.val+"\", "+identBag+")); \n");
		sb.append("//visitGroupAsExpression - end \n");
		return null;
	}

	@Override
	public Object visitNameExpression(NameExpression expr,
			Object object) {
		sb.append("//visitIdentifierExpression - start\n");
//		sb.append("{\n");
		String identB = generateIdentifier("b");
		if(expr.getSignature().getDerefSignatureWithCardinality().getColType() != SCollectionType.NO_COLLECTION) {
			sb.append("CollectionResult "+identB+" = bind(\""+expr.l.val+"\"); \n");
			sb.append("	qres.push("+identB+");	                 \n");
		} else {
			sb.append("QueryResult "+identB+" = bindOne(\""+expr.l.val+"\"); \n");
			sb.append("	qres.push("+identB+");	                 \n");
		}
//		sb.append("}                                 \n");
//		sb.append("}\n");
		sb.append("//visitIdentifierExpression - end\n");
		return null;
	}

	@Override
	public Object visitJoinExpression(JoinExpression joinExpression,
			Object object) {
		String identC1 = generateIdentifier("c1");
		String identResult = generateIdentifier("result");
		String identRightObj = generateIdentifier("rightObj");
		String identLeftObj = generateIdentifier("leftObj");
		String identRightObjId = generateIdentifier("rightObjId");
		String identB = generateIdentifier("b");
		String identS = generateIdentifier("s");
		
		sb.append("//visitJoinExpression - start \n");
		joinExpression.ex1.accept(this, object);
		sb.append("CollectionResult "+identC1+" = Utils.objectToCollection(qres.lastElement()); \n");
		sb.append("Bag "+identResult+" = new Bag(); \n");
		sb.append("for(QueryResult "+identRightObj+" : "+identC1+") { \n");
		sb.append("	QueryResult "+identRightObjId+" = "+identRightObj+"; \n"); 
		sb.append("	if("+identRightObj+" instanceof XmlId) { \n");
		sb.append("		"+identRightObj+" = store.get((XmlId) "+identRightObj+"); \n");
		sb.append("	} \n");
		sb.append("	envs.push("+identRightObj+".nested()); \n");	
		joinExpression.ex2.accept(this, "+identRightObj+");
		sb.append("	CollectionResult "+identB+" = Utils.objectToCollection(qres.pop()); \n");
		sb.append("	if("+identB+".size() > 0) { \n");
		sb.append("		for(QueryResult "+identLeftObj+" : "+identB+") { \n");
		sb.append("			"+identResult+".add(Utils.cartesianProduct("+identRightObjId+", "+identLeftObj+")); \n");
		sb.append("		} \n");
		sb.append("	} else { \n");
		sb.append("		StructSBQL "+identS+" = new StructSBQL(); \n");
		sb.append("		"+identS+".add("+identRightObjId+"); \n");
		sb.append("		"+identResult+".add("+identS+"); \n");
		sb.append("	} \n");
		sb.append("	envs.pop(); \n");
		sb.append("} \n");
		sb.append("qres.pop(); \n");
		sb.append("qres.push(Utils.collectionToObject("+identResult+")); \n");
		sb.append("//visitJoinExpression - end \n");
		return null;
	}

	@Override
	public Object visitLiteralExpression(LiteralExpression expr, Object object) {
		String identJo = generateIdentifier("jo");
		
		
		sb.append("//visitLiteralExpression - start \n");
		sb.append("JavaObject "+identJo+" = javaObjectFactory.createJavaComplexObject(\n");
		expr.generateSimpleCode(sb);
		sb.append(");\n");
		sb.append("qres.push("+identJo+");\n");
		sb.append("//visitLiteralExpression - end \n");
		return null;
	}

	@Override
	public Object visitMethodExpression(MethodExpression expr, Object object) {
		String identS = generateIdentifier("s");
		String identParams = generateIdentifier("params");
		String identJavaParams = generateIdentifier("javaParams");
		String identMethodBinder = generateIdentifier("methodBinder");
		String identResult = generateIdentifier("result");
		String identResultSbql = generateIdentifier("resultSbql");
		String identB = generateIdentifier("b");
		
		sb.append("//visitMethodExpression - start \n");

		sb.append("Object[] "+identParams+" = new Object[0]; \n");
		if(expr.paramsExpression != null) {
		expr.paramsExpression.accept(this, object);
		sb.append("	StructSBQL "+identS+" = Utils.objectToStruct(qres.pop()); \n");
//		sb.append("	Collection javaParams = Utils.sbqlType2Java((Collection<QueryResult>) s);
		sb.append("	Collection "+identJavaParams+" = Utils.sbqlStuct2JavaList("+identS+"); \n");
		sb.append("	"+identParams+" = "+identJavaParams+".toArray(); \n");
		}
		sb.append("MethodBinder "+identMethodBinder+" = bindMethod(\""+expr.methodName+"\", "+identParams+"); \n");
		sb.append("if("+identMethodBinder+" == null) { \n");
		sb.append("	System.err.println(\"nie ma bindera do metody +"+expr.methodName+"\"); \n");
		sb.append("} \n");
		sb.append(" \n");
		sb.append("Object "+identResult+" = null; \n");
		sb.append("QueryResult "+identResultSbql+" = null; \n");
		sb.append("try { \n");
		sb.append("	"+identResult+" = "+identMethodBinder+".execute("+identParams+"); \n");
		sb.append("} catch (Exception e) { \n");
		sb.append("	throw new RuntimeException(e.getMessage(), e); \n");
		sb.append("} \n");
		sb.append("if("+identResult+" == null) { \n");
		sb.append("	"+identResultSbql+" = new Bag(); \n");
		sb.append("} else { \n");
		sb.append("	Bag "+identB+" = new Bag(); \n");
		sb.append("	//zawsze traktujemy wynik jak obiekt \n");
		sb.append("	"+identB+".addAll(javaObjectFactory.createJavaObject("+identResult+")); \n");
//		sb.append("	} \n");
		sb.append("	"+identResultSbql+" = "+identB+"; \n");
		sb.append("} \n");
		sb.append("qres.push("+identResultSbql+"); \n");
		
		sb.append("//visitMethodExpression - end \n");
		return null;
	}

	@Override
	public Object visitOrderByExpression(OrderByExpression expr, Object object) {
		String identResult = generateIdentifier("result");
		String identC1 = generateIdentifier("c1");
		String identStList = generateIdentifier("stList");
		String identComparatorList = generateIdentifier("comparatorList");
		String identCompQR = generateIdentifier("compQR");
		String identCompObj = generateIdentifier("compObj");
		String identComp; // = generateIdentifier("comp");
		String identLeftObj = generateIdentifier("leftObj");
		String identS = generateIdentifier("s");
		String identParam; // = generateIdentifier("param");
		String identSortType; // = generateIdentifier("sortType");
		String identRes = generateIdentifier("res");
		String identI = generateIdentifier("i");
		
		sb.append("//visitOrderByExpression - start \n");
		expr.ex1.accept(this, object);
		sb.append("CollectionResult "+identC1+" = Utils.objectToCollection(qres.lastElement()); \n");
		sb.append("List<StructSortWrapper> "+identResult+" = new ArrayList<StructSortWrapper>(); \n");
		sb.append(" \n");
		sb.append("//prepare lists of sort types (ASC, DESC) and optional Comparators \n");
		sb.append("List<SortType> "+identStList+" = new ArrayList<SortType>(); \n");
		sb.append("List<Comparator> "+identComparatorList+" = new ArrayList<Comparator>(); \n");
		for(OrderByParamExpression paramExpr : expr.paramExprs) {
			sb.append("	"+identStList+".add(SortType.valueOf(\""+paramExpr.sortType.toString()+"\")); \n");
			Expression comparatorExpr = paramExpr.comparatorExpression;
			if(comparatorExpr != null) {
				identComp = generateIdentifier("comp");
				comparatorExpr.accept(this, object);
				sb.append("		QueryResult "+identCompQR+" = qres.pop(); \n");
				sb.append("		Object "+identCompObj+" = Utils.toSimpleValue("+identCompQR+", store); \n");
				sb.append("		Comparator "+identComp+" = (Comparator) "+identCompObj+"; \n");
				sb.append("		"+identComparatorList+".add("+identComp+"); \n");
			} else {
				sb.append("		"+identComparatorList+".add(null); \n");
			}
		}
		sb.append(" \n");
		sb.append("for(QueryResult "+identLeftObj+" : "+identC1+") { \n");
		sb.append("	"+identLeftObj+" = Utils.objectDeref("+identLeftObj+", store); \n");
		sb.append("	envs.push("+identLeftObj+".nested()); \n");
		sb.append("	StructSortWrapper "+identS+" = new StructSortWrapper(store); \n");
		sb.append("	"+identS+".add("+identLeftObj+"); \n"); 
		for(int i=0; i<expr.paramExprs.size(); i++) {
			identSortType = generateIdentifier("sortType");
			identComp = generateIdentifier("comp");
			identParam = generateIdentifier("param");
			OrderByParamExpression expr2 = expr.paramExprs.get(i);
			expr2.paramExpression.accept(this, object);
			sb.append("		QueryResult "+identParam+" = qres.pop(); \n");
			sb.append(" \n");		
			sb.append("		SortType "+identSortType+" = "+identStList+".get("+i+"); \n");
			sb.append("		Comparator "+identComp+" = "+identComparatorList+".get("+i+"); \n");
			sb.append("		"+identS+".add("+identParam+", "+identSortType+", "+identComp+"); \n");
		} 
		sb.append("	envs.pop(); \n");
		sb.append("	"+identResult+".add("+identS+"); \n");
		sb.append("} \n");
		sb.append("//++ sortowanie \n");
		sb.append("Collections.sort("+identResult+"); \n");
		sb.append("//-- sortowanie \n");
		sb.append("//zostawiamy tylko 1 element struktury \n");
		sb.append("Sequence "+identRes+" = new Sequence(); \n");
		sb.append("for(StructSortWrapper "+identS+" : "+identResult+") { \n");
		sb.append("	"+identRes+".add("+identS+".get(0)); \n");
		sb.append("} \n");
		sb.append("qres.pop(); \n");
		sb.append("qres.push("+identRes+"); \n");
		
		sb.append("//visitOrderByExpression - end \n");
		return object;
	}

	@Override
	public Object visitOrderByParamExpression(OrderByParamExpression expr,
			Object object) {
		String identParam = generateIdentifier("param");
		
		sb.append("//visitOrderByParamExpression - start \n");

		expr.paramExpression.accept(this, object);
		sb.append("QueryResult "+identParam+" = qres.pop(); \n");
		sb.append("qres.push("+identParam+"); \n");
		
		sb.append("//visitOrderByParamExpression - end \n");
		return null;
	}

	@Override
	public Object visitRangeExpression(RangeExpression expr, Object object) {
		String identRes = generateIdentifier("res");
		String identLowBound = generateIdentifier("lowBound");
		String identUpBound = generateIdentifier("upBound");
		
		sb.append("//visitRangeExpression - start \n");
		
		sb.append("StructSBQL "+identRes+" = new StructSBQL(); \n");
		expr.ex1.accept(this, object);
		sb.append("QueryResult "+identLowBound+" = qres.pop(); \n");
		sb.append(""+identRes+".add("+identLowBound+"); \n");
		if(expr.ex2 != null && !expr.isUpperUnbounded) {
			expr.ex2.accept(this, object);
			sb.append("	QueryResult "+identUpBound+" = qres.pop(); \n");
			sb.append("	"+identRes+".add("+identUpBound+"); \n");
		}
		sb.append("qres.push("+identRes+"); \n");
		
		sb.append("//visitRangeExpression - end \n");
		return null;
	}

	@Override
	public Object visitUnaryExpression(UnarySimpleOperatorExpression expr, Object object) {
		sb.append("//visitUnaryExpression - start \n");
		expr.ex1.accept(this, object);
		expr.op.accept(operatorGen, this, expr, expr.ex1);
//		expr.op.generateSimpleCode(this, expr, expr.ex1, expr.genericExpression);
		sb.append("//visitUnaryExpression - end \n");
		return object;
	}

	@Override
	public Object visitWhereExpression(WhereExpression whereExpression,
			Object object) {
		sb.append("//visitWhereExpression - start\n");
		whereExpression.ex1.accept(this, object);
		
//		sb.append("{\n");
		String identBag = generateIdentifier("bag");
		String identQ = generateIdentifier("q");
		String identWasCollection = generateIdentifier("wasCollection");
		String identResult = generateIdentifier("result");
		String identI = generateIdentifier("i");
		String identType = generateIdentifier("type");
		String identEnvsStack = generateIdentifier("envsStack");
		String identLoopIndexBinder = generateIdentifier("loopIndexBinder");
		String identT = generateIdentifier("t");
		String identRightVal = generateIdentifier("rightVal");
		sb.append("CollectionResult "+identBag+";   \n");
		sb.append("CollectionResult "+identResult+" = new Bag(); \n");
		sb.append("QueryResult "+identQ+" = qres.pop();                                                                                 \n");
//		sb.append("boolean "+identWasCollection+" = false; \n");
		boolean leftCollection = whereExpression.ex1.getSignature().getDerefSignatureWithCardinality().getColType() != SCollectionType.NO_COLLECTION;
//		sb.append("if("+identQ+" instanceof CollectionResult) {\n");
		if(leftCollection) {
			sb.append("	"+identBag+" = ((CollectionResult) "+identQ+").createSameType(); \n");
			sb.append("	"+identResult+" = ((CollectionResult) "+identQ+").createSameType(); \n");
		} else {
			sb.append("	"+identBag+" = new Bag(); \n");
			sb.append("	"+identResult+" = new Bag(); \n");
		}
		sb.append("	"+identBag+".add("+identQ+"); \n");
		sb.append("int "+identI+"=0;                                                                                                    \n");
		sb.append("for(QueryResult "+identType+" : "+identBag+") {                                                                               \n");
//		sb.append("	if("+identType+" instanceof XmlId) {                                                                                \n");
//		sb.append("		"+identType+" = store.get((XmlId) "+identType+");                                                                        \n");
//		sb.append("	}                                                                                                          \n");
		sb.append("	List "+identEnvsStack+";                                                                                            \n");
//		sb.append("	if("+identType+".nested() == null) {                                                                                \n");
//		sb.append("		"+identEnvsStack+" = new ArrayList();                                                                           \n");
//		sb.append("	} else {                                                                                                   \n");
		sb.append("		"+identEnvsStack+" = "+identType+".nested();	                                                                           \n");
//		sb.append("	}                                                                                                          \n");
		sb.append("	Binder "+identLoopIndexBinder+" = new Binder(\"$index\", javaObjectFactory.createJavaComplexObject(new Integer("+identI+"++)));\n");
		sb.append("	"+identEnvsStack+".add("+identLoopIndexBinder+");                                                                            \n");
		sb.append("	envs.push("+identEnvsStack+");                                                                                      \n");
		whereExpression.ex2.accept(this, object);                                                                               
		sb.append("	QueryResult "+identT+" = qres.pop();                                                                                \n");
		sb.append("	if("+identT+" instanceof CollectionResult) {                                                                        \n");
		sb.append("		"+identT+" = Utils.collectionToObject((CollectionResult)"+identT+");                                                     \n");
		sb.append("	}                                                                                                          \n");
		sb.append("	boolean "+identRightVal+" = (Boolean) Utils.toSimpleValue("+identT+", store);                                                \n");
		sb.append("	if("+identRightVal+") {                                                                                             \n");
		sb.append("		"+identResult+".add("+identType+");                                                                                      \n");
		sb.append("	}                                                                                                          \n");
		sb.append("	envs.pop();                                                                                                \n");
		sb.append("}                                                                                                           \n");
		if(!leftCollection) {
			sb.append("	if("+identResult+".isEmpty()) {                                                                                     \n");
			sb.append("		qres.push(null);                                                                                       \n");
			sb.append("	} else {                                                                                                   \n");
			sb.append("		qres.push("+identResult+".iterator().next());                                                                   \n");
			sb.append("	}                                                                                                          \n");
		} else {
			sb.append("	qres.push("+identResult+");	                                                                                       \n");
		}
		sb.append("//visitWhereExpression - end\n");
		return object;                                                                                                           
	}
	
	public void printExpressionTrace(String trace) {
		if(printExprTrace) {
			sb.append(trace);
		}
	}

}

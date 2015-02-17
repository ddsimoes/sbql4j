package pl.wcislo.sbql4j.lang.codegen.noqres;

import pl.wcislo.sbql4j.java.model.compiletime.Db4oConnectionSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.ResultSource;
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
import pl.wcislo.sbql4j.lang.tree.visitors.ExpressionCreateCodePrinter;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class QueryCodeGenNoQres implements TreeVisitor {
	protected final boolean printExprTrace = PreprocessorRun.Config.printExpressionTrace;
	public final StringBuilder sb = new StringBuilder();
	private OperatorTreeCodeGenNoQres opGen = new OperatorTreeCodeGenNoQres();
	private CodeGenerator parentCodeGen;
	
	public QueryCodeGenNoQres(CodeGenerator parentCodeGen) {
		super();
		this.parentCodeGen = parentCodeGen;
	}

	private int identCount = 0;
	public String generateIdentifier(String prefix) {
		return parentCodeGen.genIdent(prefix);
	}
	
	@Override
	public String evaluateExpression(Expression expr) {
		sb.append("//evaluateExpression - start \n");
		sb.append("try {\n");
//		sb.append(" "+expr.signature.genSBQLDeclarationCode()+"; \n");
		expr.accept(this, null);
		String identRes = expr.getSignature().getResultName();
		sb.append("if("+identRes+" != null) {\n");
		String identO = generateIdentifier("objRes");
		sb.append("Object "+identO+" = Utils.sbqlType2Java("+identRes+"); \n");
		
//		sb.append("Object "+identO+";\n");
//		sb.append("	if("+identRes+" instanceof AbstractCollectionResult) {              \n");
//		sb.append("		"+identO+" = Utils.sbqlType2Java((AbstractCollectionResult)"+identRes+");\n");
//		sb.append("	} else {                                                 \n");
//		sb.append("		"+identO+" = Utils.sbqlType2Java((QueryResult)"+identRes+");             \n");
//		sb.append("	}                                                        \n");
		sb.append("return ("+expr.getSignature().getJavaTypeString()+") "+identO+";\n");
		sb.append("} else {\n");
		if(expr.getSignature().getColType() == SCollectionType.NO_COLLECTION) {
			sb.append("return null;\n");
		} else {
			sb.append("return ("+expr.getSignature().getJavaTypeString()+") new ArrayList();\n");
		}
		sb.append("}\n");
//		sb.append("} else {\n");
//		sb.append("return null;\n");
//		sb.append("}\n");
		sb.append("} catch(RuntimeException e) {\n");
		sb.append("e.printStackTrace();\n");
		sb.append("throw new SBQLException(e.getMessage(), e);\n");
		sb.append("}\n");
		sb.append("//evaluateExpression - end\n");
		return sb.toString();
	}

	@Override
	public Object visitAsExpression(AsExpression expr, Object object) {
		sb.append("//visitAsExpression - start \n");
		
		expr.ex1.accept(this, object);
		Signature s1 = expr.ex1.getSignature().getDerefSignatureWithCardinality();
		String qrIdent = expr.ex1.getSignature().getResultName();
		String resultIdent = expr.getSignature().getResultName();
		
		if(s1.getColType() != SCollectionType.NO_COLLECTION) {
			sb.append(" "+expr.getSignature().genSBQLDeclarationCode()+" = new "+s1.getColType().genSBQLDeclCode()+"(); \n");
			String oIdent = generateIdentifier("o");
			sb.append("for(QueryResult "+oIdent+" : "+qrIdent+") { \n");
			sb.append("	"+resultIdent+".add(new Binder(\""+expr.identifier.val+"\", "+oIdent+")); \n");
			sb.append("} \n");
		} else {
			sb.append(" "+expr.getSignature().genSBQLDeclarationCode()+" = new Binder(\""+expr.identifier.val+"\", "+qrIdent+"); \n");
		}
		sb.append("//visitAsExpression - end\n");
		return null;
	}

	@Override
	public Object visitBinarySimpleOperatorExpression(BinarySimpleOperatorExpression expr, Object object) {
		sb.append("//visitBinaryAExpression - start \n");
		expr.ex1.accept(this, object);
		expr.ex2.accept(this, object);
		expr.op.accept(opGen, this, expr, expr.ex1, expr.ex2);
//		expr.op.generateSimpleCodeNoQres(this, expr.ex1, expr.ex2, expr);
		sb.append("//visitBinaryAExpression - end \n");
		return null;
	}

//	@Override
//	public Object visitBinaryNonAExpression(BinaryNonAExpression expr,
//			Object object) {
		//not used now
		
//		sb.append("//visitBinaryNonAExpression - start");
//		expr.ex1.accept(this, object);
//		sb.append("Bag bag = (Bag) qres.pop();                   ");
//		sb.append("for(Object o : bag) {                         ");
//		sb.append("	ENVSType type = (ENVSType) o;                ");
//		sb.append("	if(type instanceof XmlId) {                  ");
//		sb.append("		type = store.get((XmlId) type);          ");
//		sb.append("	}                                            ");
//		sb.append("	if(type.nested() == null) {                  ");
//		sb.append("		envs.push(new ArrayList());              ");
//		sb.append("	} else {                                     ");
//		sb.append("		envs.push(type.nested());	             ");
//		sb.append("	}                                            ");
//		sb.append("	                                             ");
//		expr.ex2.accept(this, object);
//		sb.append("	expr.op.eval(this, expr.ex1, expr.ex2);      ");
//		sb.append("	envs.pop();                                  ");
//		sb.append("}                                             ");
//		sb.append("qres.push(expr.op.getResult());               ");
//		sb.append("//visitBinaryNonAExpression - end");
//		
//		return null;
//	}

	@Override
	public Object visitCloseByExpression(CloseByExpression expr, Object object) {
		sb.append("//visitCloseByExpression - start \n");
		expr.ex1.accept(this, object);
		String identBag = generateIdentifier("bag");
//		String identQ = generateIdentifier("q");
		String identResult = expr.getSignature().getResultName();
		String identI = generateIdentifier("i");
		String identType = generateIdentifier("type");
//		String identRes = generateIdentifier("res");
		
		String identLeftRes = expr.ex1.getSignature().getResultName();
		String identRightRes = expr.ex2.getSignature().getResultName();
		
		sb.append("List<QueryResult> "+identBag+" = new ArrayList<QueryResult>(); \n");
//		sb.append("QueryResult "+identQ+" = qres.pop();                           \n");
		sb.append("if("+identLeftRes+" instanceof Bag) {                                \n");
		sb.append("	"+identBag+".addAll((Bag) "+identLeftRes+");                                 \n");
		sb.append("} else {                                              \n");
		sb.append("	"+identBag+".add("+identLeftRes+");                                          \n");
		sb.append("}                                                     \n");
		sb.append("Bag "+identResult+" = new Bag<Collection<QueryResult>>();      \n");
	    sb.append("                                                      \n");
		sb.append(""+identResult+".addAll("+identBag+");                                   \n");
		sb.append("int "+identI+" = 0;                                              \n");
		sb.append("while("+identI+" < "+identBag+".size() ) {                              \n");
		sb.append("	QueryResult "+identType+" = "+identBag+".get("+identI+");                       \n");
//		sb.append("	if("+identType+" instanceof XmlId) {                          \n");
//		sb.append("		"+identType+" = store.get((XmlId) "+identType+");                  \n");
//		sb.append("	}                                                    \n");
		sb.append("	                                                     \n");
		sb.append("	if("+identType+".nested() == null) {                          \n");
		sb.append("		envs.push(new ArrayList());                      \n");
		sb.append("	} else {                                             \n");
		sb.append("		envs.push(type.nested());	                     \n");
		sb.append("	}                                                    \n");
		expr.ex2.accept(this, object);
//		sb.append("	QueryResult "+identRes+" = qres.pop();                        \n");
		sb.append("	if("+identRightRes+" instanceof CollectionResult) {                \n");
		sb.append("		"+identResult+".addAll((CollectionResult)"+identRightRes+"); \n");
		sb.append("		"+identBag+".addAll((CollectionResult)"+identRightRes+"); \n");
		sb.append("	} else {                                             \n");
		sb.append("		"+identResult+".add("+identRightRes+");                                 \n");
		sb.append("		"+identBag+".add("+identRightRes+");                                    \n");
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
		op.accept(opGen, this, expr, new Expression[] {expr.ex1, expr.ex2});
//		op.generateSimpleCodeNoQres(this, expr.ex1, expr.ex2, expr);
		sb.append("//visitCommaExpression - end \n");
		return object;
	}

	@Override
	public Object visitConditionalExpression(ConditionalExpression expr,
			Object object) {
		sb.append("//visitConditionalExpression - start \n");
		expr.conditionExpr.accept(this, object);
		String identCond = expr.conditionExpr.getSignature().getResultName();
		String identB = generateIdentifier("b");
		String identRes = expr.getSignature().getResultName();
//		sb.append("QueryResult "+identCond+" = qres.pop(); \n");
		sb.append("Boolean "+identB+" = (Boolean) Utils.toSimpleValue("+identCond+", store); \n");
		sb.append(expr.getSignature().getColType().genSBQLDeclCode()+" "+identRes+"; \n");
		sb.append("if("+identB+") { \n");
		expr.trueExpr.accept(this, object);
		sb.append("	"+identRes+" = "+expr.trueExpr.getSignature().getResultName()+"; \n");
		sb.append("} else { \n");
		expr.falseExpr.accept(this, object);
		sb.append("	"+identRes+" = "+expr.falseExpr.getSignature().getResultName()+"; \n");
		sb.append("} \n");
//		sb.append("qres.push("+identRes+"); \n");
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
		String identResultSbql = expr.getSignature().getResultName();
		
		sb.append("//visitConstructorExpression - start \n");
		sb.append("JavaClass "+identOwnerClass+" = null; \n");
		if(expr.classNameExpr != null) {
			expr.classNameExpr.accept(this, object);
			sb.append(""+identOwnerClass+" = (JavaClass) Utils.collectionToObject("+expr.classNameExpr.getSignature().getResultName()+"); \n");
		} else if(expr.classNameLiteral != null) {
			sb.append(""+identOwnerClass+" = (JavaClass) bindOne("+expr.classNameLiteral+"); \n");
		}
		sb.append("Object[] "+identParams+" = new Object[0]; \n");
		if(expr.paramsExpression != null) {
			expr.paramsExpression.accept(this, object);
			sb.append("StructSBQL "+identS+" = Utils.objectToStruct("+expr.paramsExpression.getSignature().getResultName()+"); \n");
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
		sb.append(expr.getSignature().getColType().genSBQLDeclCode()+" "+identResultSbql+" = null; \n");
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
//		sb.append("qres.push("+identResultSbql+"); \n");
		sb.append("//visitConstructorExpression - end \n");
		return object;
	}

	@Override
	public Object visitDerefExpression(DerefExpression derefExpression,
			Object object) {
		String identResult = derefExpression.getSignature().getResultName();
		String identO = generateIdentifier("o");
		String identOo = generateIdentifier("oo");
		sb.append("//visitDerefExpression - start \n");
		derefExpression.ex1.accept(this, object);
		sb.append("Bag "+identResult+" = new Bag(); \n");
		sb.append("QueryResult "+identO+" = "+derefExpression.ex1.getSignature().getResultName()+"; \n");
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
//		sb.append("qres.push("+identResult+"); \n");
		sb.append("//visitDerefExpression - end \n");
		return null;
	}

	@Override
	public Object visitDotExpression(DotExpression dotExpression, Object object) {
//		String identDotLeftCol = generateIdentifier("dotLeftCol");
		
		if(dotExpression.ex1.getSignature() instanceof Db4oConnectionSignature) {
			ExpressionCreateCodePrinter ePrint = new ExpressionCreateCodePrinter();
			ePrint.evaluateExpression(dotExpression.ex2);
			String s = ePrint.toString();
			System.out.println(s);
		}
		
		String identLeftRes = dotExpression.ex1.getSignature().getResultName();
		String identRightRes = dotExpression.ex2.getSignature().getResultName();
		String identResult = dotExpression.getSignature().getResultName();
		String identO = generateIdentifier("o");
		String identType = generateIdentifier("type");
		String identEnvsStack = generateIdentifier("envsStack");
		String identLoopIndexBinder = generateIdentifier("loopIndexBinder");
		String identB = generateIdentifier("b");
		String identI = generateIdentifier("i");
		String identOo = generateIdentifier("oo");
		sb.append("//visitDotExpression - start \n");
		
		dotExpression.ex1.accept(this, object);
		
		Signature s1 = dotExpression.ex1.getSignature();
		Signature s2 = dotExpression.ex2.getSignature().getDerefSignatureWithCardinality();
		Signature sRes = dotExpression.getSignature();
		
//		if(s1.sColType == SCollectionType.NO_COLLECTION) {
//			sb.append("CollectionResult "+identDotLeftCol+" = Utils.objectToCollection("+identQ+"); \n");
//		} else {
//			sb.append("CollectionResult "+identDotLeftCol+" = "+identQ+"; \n");
//		}

		if(sRes.getColType() != SCollectionType.NO_COLLECTION) {
			sb.append(sRes.genSBQLDeclarationCode()+" = new "+sRes.getColType().genSBQLDeclCode()+"(); \n");
		} else {
			sb.append(sRes.genSBQLDeclarationCode()+"; \n");
		}
		if(s1.getColType() != SCollectionType.NO_COLLECTION) {
			sb.append("int "+identI+"=0; \n");
			sb.append("for(Object "+identO+" : "+identLeftRes+") { \n");
			sb.append("	QueryResult "+identType+" = (QueryResult) "+identO+"; \n");
			sb.append("	List "+identEnvsStack+" = "+identType+".nested(); \n");
			sb.append("	Binder "+identLoopIndexBinder+" = new Binder(\"$index\", javaObjectFactory.createJavaComplexObject(new Integer("+identI+"++))); \n");
			sb.append("	"+identEnvsStack+".add("+identLoopIndexBinder+"); \n");
		} else {
			sb.append("	List "+identEnvsStack+" = "+identLeftRes+".nested(); \n");
		}	
		sb.append("	envs.push("+identEnvsStack+"); \n");
		dotExpression.ex2.accept(this, object);
		if(s2.getColType() != SCollectionType.NO_COLLECTION) {
			sb.append("	CollectionResult "+identB+" = Utils.objectToCollection("+identRightRes+"); \n");
			sb.append("	for(QueryResult "+identOo+" : "+identB+") { \n");
			sb.append("		"+identResult+".add("+identOo+"); \n");
			sb.append("	} \n");
		} else {
			if(s1.getColType() != SCollectionType.NO_COLLECTION) {
				sb.append("		"+identResult+".add("+identRightRes+"); \n");	
			} else {
				sb.append("		"+identResult+" = "+identRightRes+"; \n");
			}
			
		}
		sb.append("	envs.pop(); \n");
		if(s1.getColType() != SCollectionType.NO_COLLECTION) {
			sb.append("} \n");
		}
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
		sb.append("QueryResult "+identQ+" = "+expr.toIterate.getSignature().getResultName()+"; \n"); 
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
//		sb.append("		qres.pop(); \n");
		sb.append("	} \n");
		sb.append("} \n");
		sb.append("//visitForEachExpression - end \n");
		return null;
	}

	@Override
	public Object visitForallExpression(ForallExpression expr, Object object) {
		String identResultJ = generateIdentifier("allRes");
		String identLeftObj = generateIdentifier("leftObj");
		String identB = generateIdentifier("b");
		
		
		Signature colSig = expr.ex1.getSignature();
		Signature condSig = expr.ex2.getSignature();
		
		String identColRes = colSig.getResultName();
		String identCondRes = condSig.getResultName();
		
		sb.append("//visitForallExpression - start \n");
		if(colSig.getColType() != SCollectionType.NO_COLLECTION) {
			expr.ex1.accept(this, object);
			String identEnvsStack = generateIdentifier("allEnvsStack");
			String identLoopIndexBinder = generateIdentifier("allLoopBinder");
			String identI = generateIdentifier("allI");
			sb.append("Boolean "+identResultJ+" = true; \n");
			sb.append("Integer "+identI+" = 0; \n");
			sb.append("for(QueryResult "+identLeftObj+" : "+identColRes+") { \n");
//			sb.append("	envs.push("+identLeftObj+".nested()); \n");	
			sb.append("	List "+identEnvsStack+" = "+identLeftObj+".nested(); \n");
			sb.append("	Binder "+identLoopIndexBinder+" = new Binder(\"$index\", javaObjectFactory.createJavaComplexObject(new Integer("+identI+"++))); \n");
			sb.append("	"+identEnvsStack+".add("+identLoopIndexBinder+"); \n");
			sb.append("	envs.push("+identEnvsStack+"); \n");
			expr.ex2.accept(this, object);
			sb.append("	envs.pop(); \n");
			sb.append("	boolean "+identB+" = (Boolean) Utils.toSimpleValue("+identCondRes+", store); \n");
			sb.append("	if(!"+identB+") { \n");
			sb.append("		"+identResultJ+" = false; \n");
			sb.append("		break; \n");
			sb.append("	} \n");
			sb.append("} \n");
			sb.append(expr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identResultJ+"); \n");
		} else {
			sb.append("	envs.push("+identColRes+".nested()); \n");
			expr.ex2.accept(this, object);
			sb.append("	envs.pop(); \n");
			sb.append("	boolean "+identB+" = (Boolean) Utils.toSimpleValue("+identCondRes+", store); \n");
			sb.append(expr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identB+"); \n");
		}
		sb.append("//visitForallExpression - end \n");
		return null;
	}

	@Override
	public Object visitForanyExpression(ForanyExpression expr, Object object) {
		String identResultJ = generateIdentifier("anyRes");
		String identLeftObj = generateIdentifier("leftObj");
		String identB = generateIdentifier("b");
		
		
		Signature colSig = expr.ex1.getSignature();
		Signature condSig = expr.ex2.getSignature();
		
		String identColRes = colSig.getResultName();
		String identCondRes = condSig.getResultName();
		
		sb.append("//visitForanyExpression - start \n");
		if(colSig.getColType() != SCollectionType.NO_COLLECTION) {
			expr.ex1.accept(this, object);
			String identEnvsStack = generateIdentifier("anyEnvsStack");
			String identLoopIndexBinder = generateIdentifier("anyLoopBinder");
			String identI = generateIdentifier("anyI");
			sb.append("Boolean "+identResultJ+" = false; \n");
			sb.append("Integer "+identI+" = 0; \n");
			sb.append("for(QueryResult "+identLeftObj+" : "+identColRes+") { \n");
//			sb.append("	envs.push("+identLeftObj+".nested()); \n");	
			sb.append("	List "+identEnvsStack+" = "+identLeftObj+".nested(); \n");
			sb.append("	Binder "+identLoopIndexBinder+" = new Binder(\"$index\", javaObjectFactory.createJavaComplexObject(new Integer("+identI+"++))); \n");
			sb.append("	"+identEnvsStack+".add("+identLoopIndexBinder+"); \n");
			sb.append("	envs.push("+identEnvsStack+"); \n");
			expr.ex2.accept(this, object);
			sb.append("	envs.pop(); \n");
			sb.append("	boolean "+identB+" = (Boolean) Utils.toSimpleValue("+identCondRes+", store); \n");
			sb.append("	if("+identB+") { \n");
			sb.append("		"+identResultJ+" = true; \n");
			sb.append("		break; \n");
			sb.append("	} \n");
			sb.append("} \n");
			sb.append(expr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identResultJ+"); \n");
		} else {
			sb.append("	envs.push("+identColRes+".nested()); \n");
			expr.ex2.accept(this, object);
			sb.append("	envs.pop(); \n");
			sb.append("	boolean "+identB+" = (Boolean) Utils.toSimpleValue("+identCondRes+", store); \n");
			sb.append(expr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identB+"); \n");
		}
		
		sb.append("//visitForanyExpression - end \n");
		return null;
	}

	@Override
	public Object visitGroupAsExpression(GroupAsExpression expr, Object object) {
		String identBag = generateIdentifier("bag");
		sb.append("//visitGroupAsExpression - start \n");
		expr.ex1.accept(this, object);
//		sb.append(""+expr.signature.genSBQLDeclarationCode()+" = new "+expr.signature.sColType.genSBQLDeclCode()+"; \n");
//		sb.append("CollectionResult "+identBag+" = Utils.objectToCollection(qres.pop()); \n");
		sb.append("Binder "+expr.getSignature().getResultName()+" = new Binder(\""+expr.identifier.val+"\", "+expr.ex1.getSignature().getResultName()+"); \n");
		sb.append("//visitGroupAsExpression - end \n");
		return null;
	}

	@Override
	public Object visitNameExpression(NameExpression expr,
			Object object) {
		sb.append("//visitIdentifierExpression - start\n");
		String identRes = expr.getSignature().getResultName();
//		String identResType = expr.signature.sColType.genSBQLDeclCode();
		String identResType = expr.getSignature().getColType().genCommonDeclCode();
		String resDecl = identResType + " " + identRes;
//		sb.append(expr.signature.genSBQLDeclarationCode() + "; \n");
		if(expr.getSignature().getDerefSignatureWithCardinality().getColType() != SCollectionType.NO_COLLECTION) {
			sb.append(""+resDecl+" = ("+identResType+") bind(\""+expr.l.val+"\"); \n");
		} else {
			sb.append(""+resDecl+" = bindOne(\""+expr.l.val+"\"); \n");
		}
		sb.append("//visitIdentifierExpression - end\n");
		return null;
	}

	@Override
	public Object visitJoinExpression(JoinExpression expr,
			Object object) {
		sb.append("//visitJoinExpression - start \n");
		
//		String identC1 = generateIdentifier("c1");
		
		String identLeftEl = generateIdentifier("joinLeftEl");
		String identRightEl = generateIdentifier("joinRightEl");
//		String identRightObjId = generateIdentifier("rightObjId");
//		String identB = generateIdentifier("b");
//		String identS = generateIdentifier("s");
		
		Signature s1 = expr.ex1.getSignature();
		Signature s2 = expr.ex2.getSignature();
		Signature sRes = expr.getSignature();
		
		String identLeftRes = s1.getResultName();
		String identRightRes = s2.getResultName();
		String identRightResDeref = generateIdentifier("joinRightResDeref");
		String identResult = sRes.getResultName();
		
		boolean isLeftCol = s1.getColType() != SCollectionType.NO_COLLECTION;
		boolean isRightCol = s2.getColType() != SCollectionType.NO_COLLECTION;
		boolean isResultCol = sRes.getColType() != SCollectionType.NO_COLLECTION;
		if(isResultCol) {
			sb.append(sRes.genSBQLDeclarationCode()+" = new "+sRes.getColType().genSBQLDeclCode()+"(); \n");
		} else {
			sb.append(sRes.genSBQLDeclarationCode()+"; \n");
		}
		
		expr.ex1.accept(this, object);
		if(isLeftCol) {
			sb.append("for(QueryResult "+identLeftEl+" : "+identLeftRes+") { \n"); 
		} else {
			sb.append("	QueryResult "+identLeftEl+" = "+identLeftRes+"; \n");
		}
		sb.append("	envs.push("+identLeftEl+".nested()); \n");
		expr.ex2.accept(this, "+identRightObj+");
		if(isRightCol) {
//			String rightElType = s2.getDerefSignature().sColType.genSBQLDeclCode();
//			if(s2 instanceof BinderSignature) {
//				sb.append(""+rightElType+" "+identRightResDeref+" = ("+rightElType+") ((Binder)"+identRightRes+").object; \n");
//			} else {
//				sb.append(""+rightElType+" "+identRightResDeref+" = "+identRightRes+ "; \n");
//			}
			sb.append("		for(QueryResult "+identRightEl+" : "+identRightRes+") { \n");
			sb.append("			"+identResult+".add(Utils.cartesianProduct("+identLeftEl+", "+identRightEl+")); \n");
			sb.append("		} \n");	
		} else if(isLeftCol) {
			sb.append("		"+identResult+".add(Utils.cartesianProduct("+identLeftEl+", "+identRightRes+")); \n");
		} else {
			sb.append("		"+identResult+" = Utils.cartesianProduct("+identLeftEl+", "+identRightRes+"); \n");
		}
//		sb.append("	CollectionResult "+identB+" = Utils.objectToCollection(qres.pop()); \n");
//		sb.append("	if("+identB+".size() > 0) { \n");
//		sb.append("	} else { \n");
//		sb.append("		Struct "+identS+" = new Struct(); \n");
//		sb.append("		"+identS+".add("+identRightObjId+"); \n");
//		sb.append("		"+identResult+".add("+identS+"); \n");
//		sb.append("	} \n");
		sb.append("	envs.pop(); \n");
		if(isLeftCol) {
			sb.append("} \n");
		}
//		sb.append("qres.pop(); \n");
//		sb.append("qres.push(Utils.collectionToObject("+identResult+")); \n");
		sb.append("//visitJoinExpression - end \n");
		return null;
	}

	@Override
	public Object visitLiteralExpression(LiteralExpression expr, Object object) {
		String identJo = generateIdentifier("jo");
		
		
		sb.append("//visitLiteralExpression - start \n");
		sb.append(" "+expr.getSignature().genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject(\n");
		expr.generateSimpleCode(sb);
		sb.append(");\n");
//		sb.append("qres.push("+identJo+");\n");
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
//		String identResultSbql = generateIdentifier("resultSbql");
		String identResultSbql = expr.getSignature().getResultName();
		String identB = generateIdentifier("b");
		
		sb.append("//visitMethodExpression - start \n");

		
		
		sb.append("Object[] "+identParams+" = new Object[0]; \n");
		if(expr.paramsExpression != null) {
		expr.paramsExpression.accept(this, object);
		String paramsName = expr.paramsExpression.getSignature().getResultName();
		sb.append("	StructSBQL "+identS+" = Utils.objectToStruct("+paramsName+"); \n");
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
//		sb.append("QueryResult "+identResultSbql+" = null; \n");
		sb.append(expr.getSignature().genSBQLDeclarationCode()+"; \n");
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
//		sb.append("qres.push("+identResultSbql+"); \n");
		
		sb.append("//visitMethodExpression - end \n");
		return null;
	}

	@Override
	public Object visitOrderByExpression(OrderByExpression expr, Object object) {
		String identResult = expr.getSignature().getResultName();
		String identResultWrapper = generateIdentifier("orderByResultWrapper");
		String identLeftResult = expr.ex1.getSignature().getResultName();
		String identStList = generateIdentifier("stList");
		String identComparatorList = generateIdentifier("comparatorList");
		String identCompQR = generateIdentifier("compQR");
		String identCompObj = generateIdentifier("compObj");
		String identComp; // = generateIdentifier("comp");
		String identLeftEl = generateIdentifier("orderByLeftEl");
		String identS = generateIdentifier("s");
//		String identParam; // = generateIdentifier("param");
		String identSortType; // = generateIdentifier("sortType");
//		String identRes = generateIdentifier("res");
		String identI = generateIdentifier("i");
		
		sb.append("//visitOrderByExpression - start \n");
		expr.ex1.accept(this, object);
		if(expr.ex1.getSignature().getColType() == SCollectionType.NO_COLLECTION) {
			//no collection to sort
			sb.append(""+expr.getSignature().genSBQLDeclarationCode()+" = "+identLeftResult+"; \n");
		} else {
			sb.append(""+expr.getSignature().genSBQLDeclarationCode()+" = new "+expr.getSignature().getColType().genSBQLDeclCode()+"(); \n");
//			sb.append("CollectionResult "+identC1+" = Utils.objectToCollection(qres.lastElement()); \n");
			sb.append("List<StructSortWrapper> "+identResultWrapper+" = new ArrayList<StructSortWrapper>(); \n");
			sb.append(" \n");
			sb.append("//prepare lists of sort types (ASC, DESC) and optional Comparators \n");
			sb.append("List<SortType> "+identStList+" = new ArrayList<SortType>(); \n");
			sb.append("List<Comparator> "+identComparatorList+" = new ArrayList<Comparator>(); \n");
			for(OrderByParamExpression paramExpr : expr.paramExprs) {
				sb.append("	"+identStList+".add(SortType.valueOf(\""+paramExpr.sortType.toString()+"\")); \n");
				Expression comparatorExpr = paramExpr.comparatorExpression;
				if(comparatorExpr != null) {
					comparatorExpr.accept(this, object);
					identComp = comparatorExpr.getSignature().getResultName();
//					sb.append("		QueryResult "+identCompQR+" = qres.pop(); \n");
//					sb.append("		Object "+identCompObj+" = Utils.toSimpleValue("+identCompQR+", store); \n");
//					sb.append("		Comparator "+identComp+" = (Comparator) "+identCompObj+"; \n");
					sb.append("		"+identComparatorList+".add((Comparator) Utils.toSimpleValue("+identComp+", store)); \n");
				} else {
					sb.append("		"+identComparatorList+".add(null); \n");
				}
			}
			sb.append(" \n");
			sb.append("for(QueryResult "+identLeftEl+" : "+identLeftResult+") { \n");
			sb.append("	"+identLeftEl+" = Utils.objectDeref("+identLeftEl+", store); \n");
			sb.append("	envs.push("+identLeftEl+".nested()); \n");
			sb.append("	StructSortWrapper "+identS+" = new StructSortWrapper(store); \n");
			sb.append("	"+identS+".add("+identLeftEl+"); \n"); 
			for(int i=0; i<expr.paramExprs.size(); i++) {
				identSortType = generateIdentifier("sortType");
				identComp = generateIdentifier("comp");
//				identParam = generateIdentifier("param");
				OrderByParamExpression expr2 = expr.paramExprs.get(i);
				expr2.paramExpression.accept(this, object);
//				sb.append("		QueryResult "+identParam+" = qres.pop(); \n");
				String identParamRes = expr2.paramExpression.getSignature().getResultName();
				sb.append(" \n");		
				sb.append("		SortType "+identSortType+" = "+identStList+".get("+i+"); \n");
				sb.append("		Comparator "+identComp+" = "+identComparatorList+".get("+i+"); \n");
				sb.append("		"+identS+".add("+identParamRes+", "+identSortType+", "+identComp+"); \n");
			} 
			sb.append("	envs.pop(); \n");
			sb.append("	"+identResultWrapper+".add("+identS+"); \n");
			sb.append("} \n");
//			}
			sb.append("//++ sortowanie \n");
			sb.append("Collections.sort("+identResultWrapper+"); \n");
			sb.append("//-- sortowanie \n");
			sb.append("//zostawiamy tylko 1 element struktury \n");
//			sb.append("Sequence "+identRes+" = new Sequence(); \n");
			sb.append("for(StructSortWrapper "+identS+" : "+identResultWrapper+") { \n");
			sb.append("	"+identResult+".add("+identS+".get(0)); \n");
			sb.append("} \n");
//			sb.append("qres.pop(); \n");
//			sb.append("qres.push("+identRes+"); \n");
		}
		
		sb.append("//visitOrderByExpression - end \n");
		return object;
	}

	@Override
	public Object visitOrderByParamExpression(OrderByParamExpression expr,
			Object object) {
//		String identParam = generateIdentifier("param");
		
		sb.append("//visitOrderByParamExpression - start \n");

		expr.paramExpression.accept(this, object);
//		sb.append("QueryResult "+identParam+" = qres.pop(); \n");
//		sb.append("qres.push("+identParam+"); \n");
		
		sb.append("//visitOrderByParamExpression - end \n");
		return null;
	}

	@Override
	public Object visitRangeExpression(RangeExpression expr, Object object) {
//		Expression leftExpr = expr.ex1;
//		Expression rightExpr = args[1];
//		Expression opExpr = args[2];
		
		
		String identRes = expr.getSignature().getResultName();
//		String identLowBound = generateIdentifier("lowBound");
//		String identUpBound = generateIdentifier("upBound");
		
		sb.append("//visitRangeExpression - start \n");
		sb.append("StructSBQL "+identRes+" = new StructSBQL(); \n");
		expr.ex1.accept(this, object);
//		sb.append("QueryResult "+identLowBound+" = qres.pop(); \n");
		sb.append(""+identRes+".add("+expr.ex1.getSignature().getResultName()+"); \n");
		if(expr.ex2 != null && !expr.isUpperUnbounded) {
			expr.ex2.accept(this, object);
//			sb.append("	QueryResult "+identUpBound+" = qres.pop(); \n");
			sb.append("	"+identRes+".add("+expr.ex2.getSignature().getResultName()+"); \n");
		}
//		sb.append("qres.push("+identRes+"); \n");
		
		sb.append("//visitRangeExpression - end \n");
		return null;
	}

	@Override
	public Object visitUnaryExpression(UnarySimpleOperatorExpression expr, Object object) {
		sb.append("//visitUnaryExpression - start \n");
		expr.ex1.accept(this, object);
		expr.op.accept(opGen, this, expr, expr.ex1);
//		expr.op.generateSimpleCodeNoQres(this, expr, expr.ex1, expr.genericExpression);
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
		String identLeftResult = whereExpression.ex1.getSignature().getResultName();
		String identRightResult = whereExpression.ex2.getSignature().getResultName();
		String identResult = whereExpression.getSignature().getResultName();
		String identResultType = whereExpression.getSignature().getColType().genSBQLDeclCode();
//		String identWasCollection = generateIdentifier("wasCollection");
		
		String identI = generateIdentifier("i");
		String identType = generateIdentifier("type");
		String identEnvsStack = generateIdentifier("envsStack");
		String identLoopIndexBinder = generateIdentifier("loopIndexBinder");
		
		String identRightVal = generateIdentifier("whereCondRes");
//		sb.append("CollectionResult "+identBag+";   \n");
		
//		sb.append("QueryResult "+identLeftResult+" = qres.pop();                                                                                 \n");
//		sb.append("boolean "+identWasCollection+" = false; \n");
		boolean leftCollection = whereExpression.ex1.getSignature().getDerefSignatureWithCardinality().getColType() != SCollectionType.NO_COLLECTION;
//		sb.append("if("+identQ+" instanceof CollectionResult) {\n");
		if(leftCollection) {
			sb.append(identResultType+" "+identResult+" = new "+identResultType+"(); \n");
			sb.append("int "+identI+"=0; \n");
			sb.append("for(QueryResult "+identType+" : "+identLeftResult+") { \n");
			sb.append("	List "+identEnvsStack+" = "+identType+".nested(); \n");
			sb.append("	Binder "+identLoopIndexBinder+" = new Binder(\"$index\", javaObjectFactory.createJavaComplexObject(new Integer("+identI+"++)));\n");
			sb.append("	"+identEnvsStack+".add("+identLoopIndexBinder+"); \n");
			sb.append("	envs.push("+identEnvsStack+"); \n");
		} else {
			sb.append(identResultType+"	"+identResult+"; \n");
			sb.append("	envs.push("+identLeftResult+".nested()); \n");
		}
		
		whereExpression.ex2.accept(this, object);                                                                               
//		sb.append("	QueryResult "+identRightResult+" = qres.pop(); \n");
//		sb.append("	if("+identRightResult+" instanceof CollectionResult) {                                                                        \n");
//		sb.append("		"+identRightResult+" = Utils.collectionToObject((CollectionResult)"+identRightResult+");                                                     \n");
//		sb.append("	}                                                                                                          \n");
		sb.append("	boolean "+identRightVal+" = (Boolean) Utils.toSimpleValue("+identRightResult+", store);                                                \n");
		sb.append("	if("+identRightVal+") { \n");
		if(leftCollection) {
			sb.append("		"+identResult+".add("+identType+"); \n");
		} else {
			sb.append("		"+identResult+" = "+identLeftResult+"; \n");
		}
		sb.append("	} \n");
		sb.append("	envs.pop(); \n");
		if(leftCollection) {
			sb.append("} \n");
		}
		if(!leftCollection) {
			sb.append("	if("+identResult+".isEmpty()) {                                                                                     \n");
//			sb.append("		qres.push(null); \n");
			sb.append("		"+identResult+" = null; \n");
			sb.append("	} else {                                                                                                   \n");
			sb.append("		"+identResult+" = "+identResult+".iterator().next());                                                                   \n");
			sb.append("	}                                                                                                          \n");
		} else {
//			sb.append("	qres.push("+identResult+");	                                                                                       \n");
		}
		sb.append("//visitWhereExpression - end\n");
		return object;                                                                                                           
	}
	
	public void printExpressionTrace(String trace) {
		if(printExprTrace) {
			sb.append(trace);
		}
	}
	
	private boolean checkDb4oExpression(Expression e) {
		return (e.getSignature().getResultSource() == ResultSource.DB4O);
	}

}

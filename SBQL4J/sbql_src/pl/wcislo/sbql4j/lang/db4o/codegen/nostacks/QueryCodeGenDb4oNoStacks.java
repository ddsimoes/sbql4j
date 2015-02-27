package pl.wcislo.sbql4j.lang.db4o.codegen.nostacks;

import pl.wcislo.sbql4j.java.model.compiletime.BindResult;
import pl.wcislo.sbql4j.java.model.compiletime.BinderSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ClassSignature;
import pl.wcislo.sbql4j.java.model.compiletime.MethodSignature;
import pl.wcislo.sbql4j.java.model.compiletime.PackageSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.ResultSource;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.compiletime.StructSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.preprocessor.PreprocessorRun;
import pl.wcislo.sbql4j.lang.codegen.CodeGenerator;
import pl.wcislo.sbql4j.lang.codegen.QueryCodeGenerator;
import pl.wcislo.sbql4j.lang.codegen.nostacks.OperatorTreeCodeGenNoStacks;
import pl.wcislo.sbql4j.lang.db4o.codegen.CodeGeneratorDb4o;
import pl.wcislo.sbql4j.lang.optimiser.coderewrite.db4oindex.Db4oIndexInvocationExpression;
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
import pl.wcislo.sbql4j.lang.parser.expression.JoinExpression;
import pl.wcislo.sbql4j.lang.parser.expression.LiteralExpression;
import pl.wcislo.sbql4j.lang.parser.expression.MethodExpression;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression.SortType;
import pl.wcislo.sbql4j.lang.parser.expression.RangeExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorComma;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorType;

public class QueryCodeGenDb4oNoStacks extends QueryCodeGenerator<Object, Object> {
	protected final boolean printExprTrace = PreprocessorRun.Config.printExpressionTrace;
	public final StringBuilder sb = new StringBuilder();
	private OperatorTreeCodeGenNoStacks opGen = new OperatorTreeCodeGenNoStacks();
	private CodeGeneratorDb4o genDb4o;
	private CodeGenerator parentCodeGen;
	
	public QueryCodeGenDb4oNoStacks(CodeGenerator parentCodeGen, CodeGeneratorDb4o parentGenDb4o) {
		super();
		this.parentCodeGen = parentCodeGen;
		this.genDb4o = parentGenDb4o;
	}

	public String generateIdentifier(String prefix) {
		return parentCodeGen.genIdent(prefix);
	}
	
	@Override
	public String evaluateExpression(Expression expr) {
		
		printExpressionTrace("//evaluateExpression - start", expr);
		sb.append("final LocalTransaction transLocal = (LocalTransaction) t;\n");
		expr.accept(this, null);
		String identResult = expr.getSignature().getResultName();
		//sb.append("if( "+identResult+" != null) { ocb.activate("+identResult+"); }\n");
		sb.append("pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(" + identResult + ", ocb);");
		sb.append("return "+identResult+"; \n");
		printExpressionTrace("//evaluateExpression - end", expr);
		return sb.toString();
	}

	@Override
	public Object visitAsExpression(AsExpression expr, Object object) {
		printExpressionTrace("//visitAsExpression - start", expr);
		expr.ex1.accept(this, object);
		String qrIdent = expr.ex1.getSignature().getResultName();
		String resIdent = expr.getSignature().getResultName();
		sb.append(" "+expr.ex1.getSignature().getJavaTypeString()+" "+resIdent+" = "+qrIdent+"; \n");
		printExpressionTrace("//visitAsExpression - end", expr);
		return null;
	}

	@Override
	public Object visitBinarySimpleOperatorExpression(BinarySimpleOperatorExpression expr, Object object) {
		printExpressionTrace("//visitBinaryAExpression - start", expr);
//		expr.ex1.accept(this, object);
//		expr.ex2.accept(this, object);
		expr.op.accept(opGen, this, expr, expr.ex1, expr.ex2);
		printExpressionTrace("//visitBinaryAExpression - end", expr);
		return null;
	}

	@Override
	public Object visitCloseByExpression(CloseByExpression expr, Object object) {
		printExpressionTrace("//visitCloseByExpression - start", expr);
		expr.ex1.accept(this, object);
		String identResult = expr.getSignature().getResultName();
		String resultType = expr.getSignature().getJavaTypeString();
		String leftType = expr.ex1.getSignature().getJavaTypeStringSingleResult();
		String identI = generateIdentifier("i");
		
		String identLoopVar = expr.getNestedVarName();
		String identLeftRes = expr.ex1.getSignature().getResultName();
		
		sb.append(expr.getSignature().genJavaDeclarationCode()+" = new ArrayList<"+expr.getSignature().getJavaTypeStringSingleResult()+">(); \n");
		if(expr.ex1.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
			sb.append("	"+identResult+".addAll("+identLeftRes+"); \n");	
		} else {
			sb.append("	"+identResult+".add("+identLeftRes+"); \n");
		}
		sb.append("int "+identI+" = 0; \n");
		sb.append("while("+identI+" < "+identResult+".size() ) { \n");
		sb.append("	"+leftType+ " "+identLoopVar+" = "+identResult+".get("+identI+"); \n");
		printObjectActivationCode(identLoopVar);
		expr.ex2.accept(this, object);
		String identRightRes = expr.ex2.getSignature().getResultName();
		if(expr.ex2.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
//			sb.append("ocb.activate("+identRightRes+", 1);\n");
			sb.append("	"+identResult+".addAll("+identRightRes+"); \n");	
		} else {
			sb.append("if("+identRightRes+" != null) { \n");
			sb.append("	"+identResult+".add("+identRightRes+"); \n");
			sb.append("} \n");
		}
		sb.append("	"+identI+"++; \n");
		sb.append("} \n");
		printExpressionTrace("//visitCloseByExpression - end", expr);
		return object;
	}

	@Override
	public Object visitComaExpression(ComaExpression expr, Object object) {
		printExpressionTrace("//visitCommaExpression - start", expr);
//		expr.ex1.accept(this, object);
//		expr.ex2.accept(this, object);
		
		OperatorComma op = new OperatorComma(OperatorType.COMA);
		op.accept(opGen, this, expr, new Expression[] {expr.ex1, expr.ex2});
//		op.generateCodeNoStacks(this, expr.ex1, expr.ex2, expr);
		printExpressionTrace("//visitCommaExpression - end", expr);
		return object;
	}

	@Override
	public Object visitConditionalExpression(ConditionalExpression expr,
			Object object) {
		printExpressionTrace("//visitConditionalExpression - start", expr);
		expr.conditionExpr.accept(this, object);
		String identCond = expr.conditionExpr.getSignature().getResultName();
		String identB = generateIdentifier("b");
		String identRes = expr.getSignature().getResultName();
//		sb.append("QueryResult "+identCond+" = qres.pop(); \n");
		sb.append("Boolean "+identB+" = "+identCond+"; \n");
		sb.append(expr.getSignature().genJavaDeclarationCode()+"; \n");
		sb.append("if("+identB+") { \n");
		expr.trueExpr.accept(this, object);
		sb.append("	"+identRes+" = "+expr.trueExpr.getSignature().getResultName()+"; \n");
		sb.append("} else { \n");
		expr.falseExpr.accept(this, object);
		sb.append("	"+identRes+" = "+expr.falseExpr.getSignature().getResultName()+"; \n");
		sb.append("} \n");
//		sb.append("qres.push("+identRes+"); \n");
		printExpressionTrace("//visitConditionalExpression - end", expr);
		
		return object;
	}

	@Override
	public Object visitConstructorExpression(ConstructorExpression expr,
			Object object) {
		boolean isArrayResType = false;
		if(expr.getSignature() instanceof ValueSignature) {
			isArrayResType = ((ValueSignature)expr.getSignature()).isArrayType();
		}
		printExpressionTrace("//visitConstructorExpression - start", expr);
		String className = null;
		if(expr.classNameLiteral != null) {
			className = expr.classNameLiteral;
		} else {
			if(expr.classNameExpr != null) {
				expr.classNameExpr.accept(this, object);
				className = expr.classNameExpr.getSignature().getResultName();
			}	
		}
		
		Expression paramsExpr = expr.paramsExpression;
		if(paramsExpr != null) {
			paramsExpr.accept(this, object);
			Signature paramSig = paramsExpr.getSignature();
			sb.append(expr.getSignature().genJavaDeclarationCode()+" = new "+className);
			if(paramSig instanceof StructSignature) {
				StructSignature sSig = (StructSignature) paramSig;
				sb.append("(");
				for(int i=0; i<sSig.fieldsNumber(); i++) {
					Signature field = sSig.getFields()[i];
//					sb.append(field.getResultName());
					sb.append("("+field.getJavaTypeString()+") "+ paramSig.getResultName()+".getValue("+i+")");
					if(i<sSig.fieldsNumber() - 1) {
						sb.append(", ");
					}
				}
				sb.append(")");
			} else {
				sb.append("("+paramSig.getResultName()+")");
			}
		} else {
			sb.append(expr.getSignature().genJavaDeclarationCode()+" = new "+className);
			sb.append("()");
		}
		sb.append("; \n");
//		sb.append("qres.push("+identResultSbql+"); \n");
		printExpressionTrace("//visitConstructorExpression - end", expr);
		return object;
	}

	@Override
	public Object visitDerefExpression(DerefExpression derefExpression,
			Object object) {
		String identResult = derefExpression.getSignature().getResultName();
		String identO = generateIdentifier("o");
		String identOo = generateIdentifier("oo");
		printExpressionTrace("//visitDerefExpression - start", derefExpression);
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
		printExpressionTrace("//visitDerefExpression - end", derefExpression);
		return null;
	}

	@Override
	public Object visitDotExpression(DotExpression dotExpression, Object object) {
		printExpressionTrace("//visitDotExpression - start", dotExpression);
//		String identDotLeftCol = generateIdentifier("dotLeftCol");
		
		dotExpression.ex1.accept(this, object);
		if(dotExpression.ex1.getSignature() instanceof ClassSignature) {
			dotExpression.getSignature().setResultName(dotExpression.ex2.getSignature().getResultName());
			dotExpression.setNestedVarName(dotExpression.ex1.toString());
			dotExpression.ex2.accept(this, object);
//			ClassSignature cSig = (ClassSignature) dotExpression.signature;
			printExpressionTrace("//visitDotExpression - end", dotExpression);
			return object;
		}
		String identLeftRes = dotExpression.ex1.getSignature().getResultName();
		
		String identResult = dotExpression.getSignature().getResultName();
		String identI = dotExpression.getNestedLoopVarName();
		Signature s1 = dotExpression.ex1.getSignature();
		Signature s2 = dotExpression.ex2.getSignature();
		Signature sRes = dotExpression.getSignature();
		String leftResType = s1.getJavaTypeStringSingleResult();
		if(s1.getColType() != SCollectionType.NO_COLLECTION) {
			sb.append(sRes.genJavaDeclarationCode()+" = new "+sRes.getJavaTypeStringAssigment()+"(); \n");
			sb.append("int "+identI+"=0; \n");
			//sprawdzenie, czy lewa wartość jest nullem - start
			sb.append("if("+identLeftRes+" != null) {\n");
			sb.append("for("+leftResType+" "+dotExpression.getNestedVarName()+" : "+identLeftRes+") { \n");
			sb.append("if("+dotExpression.getNestedVarName()+" == null) { continue; }");
			printObjectActivationCode(dotExpression.getNestedVarName(), dotExpression.getSignature(), true);
//			sb.append("if( "+dotExpression.getNestedVarName()+" != null) { ocb.activate("+dotExpression.getNestedVarName()+", 1); }\n");
//			sb.append("ocb.activate("++", 1);\n");
			dotExpression.ex2.accept(this, object);
			String identRightRes = dotExpression.ex2.getSignature().getResultName();
			printObjectActivationCode(identRightRes, dotExpression.ex2.getSignature(), false);
			if(s2.getColType() == SCollectionType.NO_COLLECTION) {
				sb.append(""+identResult+".add("+identRightRes+"); \n");
			} else {
				sb.append(""+identResult+".addAll("+identRightRes+"); \n");
			}
			sb.append(""+identI+"++; \n");
			sb.append("} \n");
			//sprawdzenie, czy lewa wartość jest nullem - end
			sb.append("} \n");
		} else {
			//trzeba obsluzyc sytuacje, gdy po lewej stronie jest nazwa klasy (wywolanie statyczne)
			if(!(dotExpression.ex1.getSignature() instanceof PackageSignature)) {
				sb.append(dotExpression.ex1.getSignature().getJavaTypeString()+" "+dotExpression.getNestedVarName()+" = "+identLeftRes+"; \n");
				//
//				printObjectActivationCode(dotExpression.getNestedVarName(), dotExpression.ex2.getSignature(), false);
			}
			if(s1 instanceof ValueSignature && s1.getResultSource() == ResultSource.DB4O) {
				printObjectActivationCode(s1.getResultName(), 2);
//				sb.append("if( "+s1.getResultName()+" != null) { ocb.activate("+s1.getResultName()+", 1); }\n");
//				sb.append("ocb.activate("+s1.getResultName()+", 1);\n");
			}
			dotExpression.ex2.accept(this, object);
//			sb.append(sRes.genJavaDeclarationCode()+" = "+s2.getResultName()+"; \n");
			dotExpression.getSignature().setResultName(s2.getResultName());
		}
		printExpressionTrace("//visitDotExpression - end", dotExpression);
		return null;
	}

	@Override
	public Object visitForEachExpression(ForEachExpression expr, Object object) {
		String identBag = generateIdentifier("bag");
		String identQ = generateIdentifier("q");
		String identT = generateIdentifier("t");
		printExpressionTrace("//visitForEachExpression - start", expr);
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
		printExpressionTrace("//visitForEachExpression - end", expr);
		
		return null;
	}

	@Override
	public Object visitForallExpression(ForallExpression expr, Object object) {
		String identResult = expr.getSignature().getResultName();
		String identLeftObj = expr.getNestedVarName();
		
		Signature colSig = expr.ex1.getSignature();
		String colElType = colSig.getJavaTypeStringSingleResult();
		Signature condSig = expr.ex2.getSignature();
		
//		String identColRes = colSig.getResultName();
//		String identCondRes = condSig.getResultName();
		
		printExpressionTrace("//visitForallExpression - start", expr);
		if(colSig.getColType() != SCollectionType.NO_COLLECTION) {
			expr.ex1.accept(this, object);
			String identI = expr.getNestedLoopVarName();
			sb.append(expr.getSignature().genJavaDeclarationCode()+" = true; \n");
//			sb.append("Boolean "+identResultJ+" = false; \n");
			sb.append("Integer "+identI+" = 0; \n");
			sb.append("for("+colElType+" "+identLeftObj+" : "+colSig.getResultName()+") { \n");
			printObjectActivationCode(identLeftObj);
//			sb.append("ocb.activate("+identLeftObj+", 1);\n");
//			sb.append("	envs.push("+identLeftObj+".nested()); \n");	
//			sb.append("	List "+identEnvsStack+" = "+identLeftObj+".nested(); \n");
//			sb.append("	Binder "+identLoopIndexBinder+" = new Binder(\"$index\", javaObjectFactory.createJavaComplexObject(new Integer("+identI+"++))); \n");
//			sb.append("	"+identEnvsStack+".add("+identLoopIndexBinder+"); \n");
//			sb.append("	envs.push("+identEnvsStack+"); \n");
			expr.ex2.accept(this, object);
//			sb.append("	envs.pop(); \n");
//			sb.append("	boolean "+identB+" = (Boolean) Utils.toSimpleValue("+identCondRes+", store); \n");
			sb.append("	if(!"+condSig.getResultName()+") { \n");
			sb.append("		"+identResult+" = false; \n");
			sb.append("		break; \n");
			sb.append("	} \n");
			sb.append("} \n");
//			sb.append(expr.signature.genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identResultJ+"); \n");
		} else {
//			sb.append("	envs.push("+identColRes+".nested()); \n");
			expr.ex1.accept(this, object);
			sb.append(""+colElType+" "+identLeftObj+" = "+colSig.getResultName()+"; \n");
			printObjectActivationCode(identLeftObj, 2);
			expr.ex2.accept(this, object);
//			sb.append("	envs.pop(); \n");
			sb.append(expr.getSignature().genJavaDeclarationCode()+" = "+condSig.getResultName()+"; \n");
//			sb.append("	boolean "+identB+" = (Boolean) Utils.toSimpleValue("+identCondRes+", store); \n");
//			sb.append(expr.signature.genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identB+"); \n");
		}
		printExpressionTrace("//visitForallExpression - end", expr);
		return null;
	}

	@Override
	public Object visitForanyExpression(ForanyExpression expr, Object object) {
		String identResult = expr.getSignature().getResultName();
		String identLeftObj = expr.getNestedVarName();
		
		Signature colSig = expr.ex1.getSignature();
		String colElType = colSig.getJavaTypeStringSingleResult();
		Signature condSig = expr.ex2.getSignature();
		
//		String identColRes = colSig.getResultName();
//		String identCondRes = condSig.getResultName();
		
		printExpressionTrace("//visitForanyExpression - start", expr);
		if(colSig.getColType() != SCollectionType.NO_COLLECTION) {
			expr.ex1.accept(this, object);
			String identI = expr.getNestedLoopVarName();
			sb.append(expr.getSignature().genJavaDeclarationCode()+" = false; \n");
//			sb.append("Boolean "+identResultJ+" = false; \n");
			sb.append("Integer "+identI+" = 0; \n");
			sb.append("for("+colElType+" "+identLeftObj+" : "+colSig.getResultName()+") { \n");
			printObjectActivationCode(identLeftObj);
//			sb.append("	envs.push("+identLeftObj+".nested()); \n");	
//			sb.append("	List "+identEnvsStack+" = "+identLeftObj+".nested(); \n");
//			sb.append("	Binder "+identLoopIndexBinder+" = new Binder(\"$index\", javaObjectFactory.createJavaComplexObject(new Integer("+identI+"++))); \n");
//			sb.append("	"+identEnvsStack+".add("+identLoopIndexBinder+"); \n");
//			sb.append("	envs.push("+identEnvsStack+"); \n");
			expr.ex2.accept(this, object);
//			sb.append("	envs.pop(); \n");
//			sb.append("	boolean "+identB+" = (Boolean) Utils.toSimpleValue("+identCondRes+", store); \n");
			sb.append("	if("+condSig.getResultName()+") { \n");
			sb.append("		"+identResult+" = true; \n");
			sb.append("		break; \n");
			sb.append("	} \n");
			sb.append("} \n");
//			sb.append(expr.signature.genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identResultJ+"); \n");
		} else {
//			sb.append("	envs.push("+identColRes+".nested()); \n");
			expr.ex1.accept(this, object);
			sb.append(""+colElType+" "+identLeftObj+" = "+colSig.getResultName()+"; \n");
			printObjectActivationCode(identLeftObj, 2);
			expr.ex2.accept(this, object);
//			sb.append("	envs.pop(); \n");
			sb.append(expr.getSignature().genJavaDeclarationCode()+" = "+condSig.getResultName()+"; \n");
//			sb.append("	boolean "+identB+" = (Boolean) Utils.toSimpleValue("+identCondRes+", store); \n");
//			sb.append(expr.signature.genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject("+identB+"); \n");
		}
		
		printExpressionTrace("//visitForanyExpression - end", expr);
		return null;
	}

	@Override
	public Object visitGroupAsExpression(GroupAsExpression expr, Object object) {
		String identBag = generateIdentifier("bag");
		printExpressionTrace("//visitGroupAsExpression - start", expr);
		expr.ex1.accept(this, object);
		String qrIdent = expr.ex1.getSignature().getResultName();
//		expr.signature.setResultName(qrIdent);
//		String resultIdent = expr.signature.getResultName();
		sb.append(" "+expr.getSignature().genJavaDeclarationCode()+" = "+qrIdent+"; \n");
		printExpressionTrace("//visitGroupAsExpression - end", expr);
		return null;
	}

	@Override
	public Object visitNameExpression(NameExpression expr,
			Object object) {
		printExpressionTrace("//visitIdentifierExpression - start", expr);
		//TODO fixed bug linq43 
//		String identResType = expr.getSignature().getJavaTypeStringNoDeref();
		String identResType = expr.getSignature().getJavaTypeString();
		String identResName = expr.getSignature().getResultName();
		Signature sig = expr.getSignature().getDerefSignatureWithCardinality();
		
		StringBuilder sb = new StringBuilder();
		BindResult boundResult = expr.getBindResults().get(0);
		String resAssociation = boundResult.genJavaCode(ResultSource.DB4O);
		boolean dontChangeResultName = false;
		if(expr instanceof Db4oIndexInvocationExpression) {
			Db4oIndexInvocationExpression indexExpr = (Db4oIndexInvocationExpression) expr;
			indexExpr.getParamExpression().accept(this, object);
			String paramExprRes = indexExpr.getParamExpression().getSignature().getResultName();
			
			String identClassMeta = generateIdentifier("classMeta");
			String identObjIds = generateIdentifier("ids");
			String identObjId = generateIdentifier("id");
			String identLazyRef = generateIdentifier("ref");
			String identObj = generateIdentifier("obj");
			
			String identFieldMeta = generateIdentifier("fieldMeta");
			String identPointersIterator = generateIdentifier("it");
			String identRange = generateIdentifier("range");
			String identPoint = generateIdentifier("point");
			String identPointKey = generateIdentifier("pointKey");
			String typeRes = expr.fullName;
			
//	        final java.util.Collection<org.polepos.data.IndexedObject> _ident_IndexedObject =
//	            new java.util.ArrayList<org.polepos.data.IndexedObject>();
			sb.append("final ").append(sig.genJavaDeclarationCode()).append(" = new ").append(sig.getJavaTypeStringAssigment()).append("();\n");
//	        ClassMetadata _classMeta0 = ocb.classCollection()
//	                                       .getClassMetadata("org.polepos.data.IndexedObject");
	        sb.append("ClassMetadata ").append(identClassMeta).append(" = ocb.classCollection().getClassMetadata(\"");
	        sb.append(expr.fullName);
			sb.append("\");\n");
//	        FieldMetadata fm = _classMeta0.fieldMetadataForName("_string");
	        sb.append("FieldMetadata ").append(identFieldMeta).append(" = ")
	        	.append(identClassMeta).append(".fieldMetadataForName(\"").append(indexExpr.getIndexedFieldName()).append("\");\n");
//	    	BTreeRange range = fm.search(t, stringId);
	        sb.append("BTreeRange ").append(identRange).append(" = ").append(identFieldMeta)
	        	.append(".search(t, ").append(paramExprRes).append(");\n");
//			Iterator4 it = range.pointers();
	        sb.append("Iterator4 ").append(identPointersIterator).append(" = ").append(identRange).append(".pointers();\n");
//			while(it.moveNext()) {
	        sb.append("while("+identPointersIterator+".moveNext()) {\n");
//				BTreePointer point = (BTreePointer) it.current();
	        sb.append("\tBTreePointer "+identPoint+" = (BTreePointer) "+identPointersIterator+".current();\n");
//				FieldIndexKeyImpl pointKey = (FieldIndexKeyImpl) point.key();
	        sb.append("FieldIndexKeyImpl "+identPointKey+" = (FieldIndexKeyImpl) "+identPoint+".key();\n");
//			  	int id = pointKey.parentID();
	        sb.append("int "+identObjId+" = "+identPointKey+".parentID();\n");
//			  	LazyObjectReference _ref0 = transLocal.lazyReferenceFor(id);
	        sb.append("LazyObjectReference "+identLazyRef+" = transLocal.lazyReferenceFor("+identObjId+");\n");
//			  	IndexedObject o = (IndexedObject)_ref0.getObject();
	        
	        sb.append(typeRes+" "+identObj+" = ("+typeRes+")"+identLazyRef+".getObject();\n");
//			  	ocb.activate(o);
	        sb.append("if("+identObj+" != null) { ocb.activate("+identObj+"); }\n");
//			  	_ident_IndexedObject.add(o);
	        sb.append(identResName+".add("+identObj+");\n");
//			}
	        sb.append("}\n");
	        this.sb.append(sb.toString());
		} else {
			if(sig instanceof ClassSignature) {
				ClassSignature cSig = (ClassSignature) sig;
				sb.append(cSig.getTypeName());
			} else if(sig instanceof PackageSignature) {
			} else if(sig instanceof ValueSignature) {
				ValueSignature vs = (ValueSignature) sig;
				boolean isResArrayType = vs.isArrayType();
	//			boolean isAuxilary = expr.getSignature() instanceof BinderSignature && ((BinderSignature)expr.getSignature()).auxiliary;
	//			if(isAuxilary) {
	//				BindResult bs = expr.getBindResults().get(0);
	//				resAssociation = bs.boundValueInfo.
	//			}
	//			if(expr.getSignature() instanceof ValueSignature) {
	//				isResArrayType = ((ValueSignature)expr.getSignature()).isArrayType;
	//			}
	//			sb.append(""+identResType+" "+identResName+" = ");
				if(expr.fullName != null) {
	//				String identClazzRep = generateIdentifier("clazzRep");
					String identClassMeta = generateIdentifier("classMeta");
					String identObjIds = generateIdentifier("ids");
					String identObjId = generateIdentifier("id");
					String identLazyRef = generateIdentifier("ref");
					sb.append("final ").append(vs.genJavaDeclarationCode()).append(" = new ").append(vs.getJavaTypeStringAssigment()).append("();\n");
					sb.append("ClassMetadata ").append(identClassMeta).append(" = ocb.classCollection().getClassMetadata(\"");
					sb.append(expr.fullName);
					sb.append("\");\n");
	//				sb.append(identClazzRep).append(".iterateTopLevelClasses(new Visitor4<ClassMetadata>() {\n");
	//				sb.append("    public void visit(ClassMetadata obj) {\n");
	//				sb.append("        String cName = obj.getName();\n");
	//				sb.append("        if(\"").append(expr.fullName).append("\".equals(cName)) {\n");
					sb.append("            long[] "+identObjIds+" = "+identClassMeta+".getIDs(transLocal);\n");
					sb.append("            for(long "+identObjId+" : "+identObjIds+") {\n");
					sb.append("                LazyObjectReference "+identLazyRef+" = transLocal.lazyReferenceFor((int) "+identObjId+");\n");
					sb.append("                ").append(vs.getResultName()).append(".add((").append(vs.getJavaTypeStringSingleResult()).append(")"+identLazyRef+".getObject());\n");
					sb.append("            }\n");
	//				sb.append("            return;\n");
	//				sb.append("        }\n");
	//				sb.append("    }\n");
	//				sb.append("});\n");
					this.sb.append(sb.toString());
					dontChangeResultName = true;
				} else {
					//should write result to new variable
					if(isResArrayType) {
						this.sb.append(identResType).append(identResName).append(" = ArrayUtils.toList(").append(resAssociation);
	//					if(vs instanceof MethodSignature) {
						if(expr.boundGetterMethodAsIdentifier) {
							this.sb.append("()");	
						}
						this.sb.append(")");
					} else {
						this.sb.append(identResType).append(" ").append(identResName).append(" = ").append(resAssociation);
	//					if(vs instanceof MethodSignature) {
						if(expr.boundGetterMethodAsIdentifier) {
							this.sb.append("()");	
						}
					}
					this.sb.append(";\n");
					if(vs.getResultSource() == ResultSource.DB4O) {
						//should activate object by one level
						printObjectActivationCode(identResName, sig, false);
//						this.sb.append("ocb.activate(").append(identResName).append(", 1);\n");
					}
					dontChangeResultName = true;
				}
			} else if(sig instanceof StructSignature) {
				this.sb.append(identResType).append(" ").append(identResName).append(" = ").append(resAssociation).append(";\n");
				if(sig.getResultSource() == ResultSource.DB4O) {
					//should activate object by one level
					printObjectActivationCode(identResName, sig, false);
//					this.sb.append("ocb.activate(").append(identResName).append(", 1);\n");
				}
				dontChangeResultName = true;
			}
	//		else if(sig instanceof BinderSignature) {
	//			BinderSignature bs = (BinderSignature) sig;
	//			Signature abs.getAssociatedExpression();
	//			System.out.println(bs);
	//		}
			if(!dontChangeResultName) {
				expr.getSignature().setResultName(sb.toString());
			}
		}
		printExpressionTrace("//visitIdentifierExpression - end", expr);
		return null;
	}

	@Override
	public Object visitJoinExpression(JoinExpression expr,
			Object object) {
		printExpressionTrace("//visitJoinExpression - start", expr);
		String identLeftEl = generateIdentifier("joinLeftEl");
		String identRightEl = generateIdentifier("joinRightEl");
		expr.ex1.accept(this, object);
		Signature s1 = expr.ex1.getSignature();
		Signature s2 = expr.ex2.getSignature();
		Signature sRes = expr.getSignature();
		
		String identLeftRes = s1.getResultName();
//		String identRightRes = s2.getResultName();
		
		String identI = expr.getNestedLoopVarName();
		String identRightResDeref = generateIdentifier("joinRightResDeref");
		String identResult = sRes.getResultName();
		
		boolean isLeftCol = s1.getColType() != SCollectionType.NO_COLLECTION;
		boolean isRightCol = s2.getColType() != SCollectionType.NO_COLLECTION;
		boolean isResultCol = sRes.getColType() != SCollectionType.NO_COLLECTION;
		
		String leftName = (s1 instanceof BinderSignature) ? ((BinderSignature)s1).name : "";
		String rightName = (s2 instanceof BinderSignature) ? ((BinderSignature)s2).name : "";
		
		
		String leftResType = s1.getJavaTypeStringSingleResult();
		String resType = sRes.getJavaTypeStringSingleResult();
		if(isLeftCol) {
			sb.append(sRes.genJavaDeclarationCode()+" = new "+sRes.getJavaTypeStringAssigment()+"(); \n");
			sb.append("int "+identI+"=0; \n");
			sb.append("for("+leftResType+" "+expr.getNestedVarName()+" : "+identLeftRes+") { \n");
			printObjectActivationCode(expr.getNestedVarName());
//			sb.append("ocb.activate("+expr.getNestedVarName()+", 1);\n");
			expr.ex2.accept(this, object);
//			sb.append(sRes.getResultName() + ".addAll(OperatorUtils.");
			sb.append(sRes.getResultName());
			if(isRightCol) {
				sb.append(".addAll(OperatorUtils.cartesianProductSC");
			} else {
				sb.append(".add(OperatorUtils.cartesianProductSS");
			}
			sb.append("("+expr.getNestedVarName()+", "+s2.getResultName()+", \""+leftName+"\", \""+rightName+"\")); \n");
			sb.append(""+identI+"++; \n");
			sb.append("} \n");
		} else {
			printObjectActivationCode(s1.getResultName(), 2);
//			sb.append("ocb.activate("+s1.getResultName()+", 1);\n");
			expr.ex2.accept(this, object);
//			sb.append(resType+" "+expr.getNestedVarName()+" = " + "OperatorUtils.");
			sb.append(sRes.genJavaDeclarationCode()+" = " + "OperatorUtils.");
			if(isRightCol) {
				sb.append("cartesianProductSC");
			} else {
				sb.append("cartesianProductSS");
			}
			sb.append("("+identLeftRes+", "+s2.getResultName()+", \""+leftName+"\", \""+rightName+"\"); \n");
//			sb.append(sRes.genJavaDeclarationCode()+" = "+expr.getNestedVarName()+"; \n");
		} 
		printExpressionTrace("//visitJoinExpression - end", expr);
		return null;
	}

	@Override
	public Object visitLiteralExpression(LiteralExpression expr, Object object) {
//		String identJo = generateIdentifier("jo");
		
		
		printExpressionTrace("//visitLiteralExpression - start", expr);
//		sb.append(expr.getSignature().genJavaDeclarationCode()+" = "+expr.generateJavaCode()+"; \n");
		expr.getSignature().setResultName(expr.generateJavaCode());
//		sb.append(" "+expr.signature.genSBQLDeclarationCode()+" = javaObjectFactory.createJavaComplexObject(\n");
//		expr.generateSimpleCode(sb);
//		sb.append(");\n");
////		sb.append("qres.push("+identJo+");\n");
		printExpressionTrace("//visitLiteralExpression - end", expr);
		return null;
	}

	@Override
	public Object visitMethodExpression(MethodExpression expr, Object object) {
		MethodSignature mthSig = (MethodSignature)expr.getSignature();
		boolean isArrayResType = mthSig.isArrayType();
		
		printExpressionTrace("//visitMethodExpression - start", expr);
		Expression paramsExpr = expr.paramsExpression;
		if(paramsExpr != null) {
			paramsExpr.accept(this, object);
		}
		sb.append(expr.getSignature().genJavaDeclarationCode()+" = ");
		if(isArrayResType) {
			sb.append("ArrayUtils.toList(");
		}
		sb.append(expr.bindResult.genJavaCode(ResultSource.DB4O));
		if(paramsExpr != null) {
			Signature paramSig = paramsExpr.getSignature();
			if(paramSig instanceof StructSignature) {
				sb.append("(");
				StructSignature sSig = (StructSignature) paramSig;
				for(int i=0; i<sSig.fieldsNumber(); i++) {
					Signature field = sSig.getFields()[i];
					sb.append("("+field.getJavaTypeString()+") "+ paramSig.getResultName()+".getValue("+i+")");
					if(i<sSig.fieldsNumber() - 1) {
						sb.append(", ");
					}
				}
				sb.append(")");
			} else {
				sb.append("("+paramSig.getResultName()+")");
			}
		} else {
			sb.append("()");
		}
		if(isArrayResType) {
			sb.append(")");
		}
		sb.append("; \n");
		printObjectActivationCode(mthSig.getResultName(), mthSig, false);
		printExpressionTrace("//visitMethodExpression - end", expr);
		return object;
	}

	@Override
	public Object visitOrderByExpression(OrderByExpression expr, Object object) {
		Signature sRes = expr.getSignature();
		Signature sLeft = expr.ex1.getSignature();
		
		String identResult = expr.getSignature().getResultName();
		String identComp = generateIdentifier("comparator");
		
		printExpressionTrace("//visitOrderByExpression - start", expr);
		expr.ex1.accept(this, object);
		String identLeftResult = expr.ex1.getSignature().getResultName();
		if(sLeft.getColType() == SCollectionType.NO_COLLECTION) {
			sb.append(sLeft.genJavaDeclarationCode()+" = "+identLeftResult+"; \n");
			return object;
		}
		sb.append(sRes.genJavaDeclarationCode()+" = new "+sRes.getJavaTypeStringAssigment()+"(); \n");
		printObjectActivationCode(identLeftResult, 2);
//		sb.append("ocb.activate("+identLeftResult+", 2);\n");
		sb.append(identResult+".addAll("+identLeftResult+"); \n");
		String compareObjectType = sRes.getJavaTypeStringSingleResult();
		String identCompObj = expr.getNestedVarName();
		sb.append("Comparator<").append(compareObjectType).append("> ").append(identComp);
		sb.append(" = new Comparator<").append(compareObjectType).append(">() {\n");
		
		String identCompLeft = "_leftObj";
		String identCompRight = "_rightObj";
		sb.append("  public int compare("+compareObjectType+" "+identCompLeft+", "+compareObjectType+" "+identCompRight+") { \n");
		sb.append("    if("+identCompLeft+" == null) {\n");
		SortType firstST = expr.paramExprs.get(0).sortType;
		sb.append("      return "+(firstST == SortType.ASC ? "-1" : "1")+"; \n");    
		sb.append("    } \n");
		printObjectActivationCode(identCompLeft, 1);
		printObjectActivationCode(identCompRight, 1);
		sb.append("int res = 0; \n");
		int i=0;
		for(OrderByParamExpression paramExpr : expr.paramExprs) {
			//now it needs to assign param value twice - first for 'identCompLeft' object, 
			//second for 'identCompRight' object
			//let's start some tricky combination...
			Signature paramSig = paramExpr.paramExpression.getSignature();
			String paramType = paramSig.getJavaTypeString();
			String identLeftObj = "_leftParam"+i;
			String identRightObj = "_rightParam"+i;
//			paramSig.setResultName(identLeftObj);
//			expr.nestedVarName = identCompLeft;
			//compute left result
			sb.append(""+paramType+" "+identLeftObj+";\n");
			sb.append("{ \n");
			expr.setNestedVarName(identCompLeft);
			paramExpr.accept(this, object);
			String paramResName = paramSig.getResultName();
			sb.append(""+identLeftObj+" = "+paramResName+";\n");
			sb.append("} \n");
			//compute right result			
			sb.append(""+paramType+" "+identRightObj+";\n");
			sb.append("{ \n");
			expr.setNestedVarName(identCompRight);
			paramExpr.accept(this, object);
			paramResName = paramSig.getResultName();
			sb.append(""+identRightObj+" = "+paramResName+";\n");
			sb.append("} \n");
			SortType st = paramExpr.sortType;
			if(paramExpr.comparatorExpression != null) {
				paramExpr.comparatorExpression.accept(this, object);
				String identComparator = paramExpr.comparatorExpression.getSignature().getResultName();
				sb.append("if ("+identComparator+" != null) { \n");
				sb.append("res = "+identComparator+".compare("+identLeftObj+", "+identRightObj+"); \n");
				sb.append("} else { \n");
				sb.append("res = 0; \n");
				sb.append("} \n");
				
			} else {
				sb.append("if("+identLeftObj+" != null) { \n");
				sb.append(" res = "+identLeftObj+".compareTo("+identRightObj+"); \n");
				sb.append("} else {\n");
				
				sb.append(" return "+(st == SortType.ASC ? "-1" : "1")+"; \n");		
				sb.append("} \n");
			}
			boolean lastParam = i == expr.paramExprs.size() - 1;
			if(!lastParam) {
				sb.append("if(res != 0) { \n");
			}
			String resSign = st == SortType.ASC ? "" : "-";
			sb.append(" return "+resSign+"res; \n");
			if(!lastParam) {
				sb.append("} \n");
			}
			//cleanup
			expr.setNestedVarName(identCompObj);
//			paramSig.setResultName(paramResName);
			i++;
		}
		
		sb.append("  } \n");
		sb.append(" }; ");
		sb.append("Collections.sort("+identResult+", "+identComp+"); \n");
		printExpressionTrace("//visitOrderByExpression - end", expr);
		return object;
	}

	@Override
	public Object visitOrderByParamExpression(OrderByParamExpression expr,
			Object object) {
//		String identParam = generateIdentifier("param");
		
		printExpressionTrace("//visitOrderByParamExpression - start", expr);

		expr.paramExpression.accept(this, object);
//		sb.append("QueryResult "+identParam+" = qres.pop(); \n");
//		sb.append("qres.push("+identParam+"); \n");
		
		printExpressionTrace("//visitOrderByParamExpression - end", expr);
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
		
		printExpressionTrace("//visitRangeExpression - start", expr);
//		sb.append("Struct "+identRes+" = new Struct(); \n");
		expr.ex1.accept(this, object);
//		sb.append("QueryResult "+identLowBound+" = qres.pop(); \n");
//		sb.append(""+identRes+".add("+expr.ex1.signature.getResultName()+"); \n");
		if(expr.ex2 != null && !expr.isUpperUnbounded) {
			expr.ex2.accept(this, object);
//			sb.append("	QueryResult "+identUpBound+" = qres.pop(); \n");
//			sb.append("	"+identRes+".add("+expr.ex2.signature.getResultName()+"); \n");
		}
//		sb.append("qres.push("+identRes+"); \n");
		
		printExpressionTrace("//visitRangeExpression - end", expr);
		return null;
	}

	@Override
	public Object visitUnaryExpression(UnarySimpleOperatorExpression expr, Object object) {
		printExpressionTrace("//visitUnaryExpression - start", expr);
//		expr.ex1.accept(this, object);
		expr.op.accept(opGen, this, expr, expr.ex1, expr.genericExpression);
//		expr.op.generateCodeNoStacks(this, expr, expr.ex1, expr.genericExpression);
		printExpressionTrace("//visitUnaryExpression - end", expr);
		return object;
	}

	@Override
	public Object visitWhereExpression(WhereExpression expr,
			Object object) {
		
		String identResult = expr.getSignature().getResultName();
		Signature s1 = expr.ex1.getSignature();
//		String identLeftRes = s1.getResultName();
		Signature s2 = expr.ex2.getSignature();
//		String identRightRes = s2.getResultName();
		Signature sRes = expr.getSignature();
		String leftResTypeSingle = s1.getJavaTypeStringSingleResult();
		String identI = expr.getNestedLoopVarName();
		
		printExpressionTrace("//visitWhereExpression - start", expr);
		if(s1.getColType() != SCollectionType.NO_COLLECTION) {
			expr.ex1.accept(this, object);
			sb.append(sRes.genJavaDeclarationCode()+" = new "+sRes.getJavaTypeStringAssigment()+"(); \n");
			sb.append("int "+identI+"=0; \n");
			sb.append("for("+leftResTypeSingle+" "+expr.getNestedVarName()+" : "+s1.getResultName()+") { \n");
			sb.append("if("+expr.getNestedVarName()+" == null) { continue; }");
			printObjectActivationCode(expr.getNestedVarName());
//			sb.append("ocb.activate("+expr.getNestedVarName()+", 1);\n");
			expr.ex2.accept(this, object);
			sb.append("if("+s2.getResultName()+") { \n");
			sb.append(""+identResult+".add("+expr.getNestedVarName()+"); \n");
			sb.append("} \n");
			sb.append(""+identI+"++; \n");
			sb.append("} \n");
		} else { 
			expr.ex1.accept(this, object);
			printObjectActivationCode(s1.getResultName(), 2);
//			sb.append("ocb.activate("+s1.getResultName()+", 1);\n");
			sb.append(""+sRes.getJavaTypeString()+" "+expr.getNestedVarName()+" = "+s1.getResultName()+"; \n");
//			sb.append(""+sRes.getJavaTypeString()+" "+expr.nestedVarName+" = null; \n");
			expr.ex2.accept(this, object);
			sb.append(sRes.genJavaDeclarationCode()+" = null; \n");
			sb.append("if("+s2.getResultName()+") { \n");
			sb.append(""+identResult+" = "+s1.getResultName()+"; \n");
			sb.append("} \n");
		}
		printExpressionTrace("//visitWhereExpression - end", expr);
		return object;                                                                                                           
	}

	public void printExpressionTrace(String trace, Expression e) {
		if(printExprTrace) {
			sb.append(trace);
			sb.append(" ");
			sb.append(e.toString());
			sb.append("\n");
		}
	}
	
	private void printObjectActivationCode(String ident, Signature sigToActivate, boolean asNestedVar) {
		int activateDepth = 1;
		SCollectionType colType = sigToActivate.getColType();
		if(colType != SCollectionType.NO_COLLECTION) {
			activateDepth++;
		}
		if(sigToActivate instanceof StructSignature) {
			activateDepth++;
		}
		if(asNestedVar) {
			activateDepth--;
		}
		printObjectActivationCode(ident, activateDepth);
	}
	
	private void printObjectActivationCode(String ident) {
		printObjectActivationCode(ident, 1);
	}
	private void printObjectActivationCode(String ident, int lev) {
		sb.append("if( "+ident+" != null) { ocb.activate("+ident+", "+lev+"); }\n");
	}
	
	@Override
	public StringBuilder getAppender() {
		return sb;
	}
}

package pl.wcislo.sbql4j.lang.tree.visitors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import pl.wcislo.sbql4j.exception.SBQLException;
import pl.wcislo.sbql4j.java.model.compiletime.BindExpression;
import pl.wcislo.sbql4j.java.model.compiletime.BindResult;
import pl.wcislo.sbql4j.java.model.compiletime.BinderSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ClassSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ClassTypes;
import pl.wcislo.sbql4j.java.model.compiletime.ConstructorSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Db4oConnectionSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Db4oNestedMarker;
import pl.wcislo.sbql4j.java.model.compiletime.MethodSignature;
import pl.wcislo.sbql4j.java.model.compiletime.NestedInfo;
import pl.wcislo.sbql4j.java.model.compiletime.PackageSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.ResultSource;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.compiletime.StaticENVS;
import pl.wcislo.sbql4j.java.model.compiletime.StaticEVNSType;
import pl.wcislo.sbql4j.java.model.compiletime.StructSignature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;
import pl.wcislo.sbql4j.java.model.compiletime.VariantSignature;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureAbstractFactory;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureCompilerFactory;
import pl.wcislo.sbql4j.java.model.compiletime.factory.JavaSignatureFactory;
import pl.wcislo.sbql4j.java.utils.SBQL4JStatement;
import pl.wcislo.sbql4j.lang.optimiser.coderewrite.db4oindex.Db4oIndexInvocationExpression;
import pl.wcislo.sbql4j.lang.parser.expression.AsExpression;
import pl.wcislo.sbql4j.lang.parser.expression.BinaryExpression;
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
import pl.wcislo.sbql4j.lang.parser.expression.RangeExpression;
import pl.wcislo.sbql4j.lang.parser.expression.UnarySimpleOperatorExpression;
import pl.wcislo.sbql4j.lang.parser.expression.WhereExpression;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorBag;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorComma;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorFactory;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorType;
import pl.wcislo.sbql4j.lang.xml.signature.XMLDataSourceSignature;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.TypeSymbol;
import pl.wcislo.sbql4j.tools.javac.code.Type;
import pl.wcislo.sbql4j.tools.javac.code.Type.ClassType;
import pl.wcislo.sbql4j.tools.javac.comp.JavaNameResolver;
import pl.wcislo.sbql4j.tools.javac.util.Context;
import pl.wcislo.sbql4j.tools.javac.util.Log;
import pl.wcislo.sbql4j.util.Utils;


public class TypeChecker implements TreeVisitor {
	
	private Log log = Log.instance(Context.getInstance());
	
	private static final String PROP_KEY_ALLOW_FIND_PACKAGE = "allowToFindPackage";
	
//	private Stack<List<? extends StaticEVNSType>> envs = new Stack<List<? extends StaticEVNSType>>();
	private StaticENVS envs;
//	private Stack<Signature> qres = new Stack<Signature>();
	private Signature resultType;
	
	private JavaNameResolver jnr;
	private ClassTypes cTypes; 
	private JavaSignatureFactory jSigFac;
	private boolean reportTypeErrors = true;
	private boolean viewUsed;
	private Expression rootExpr;
	private SBQL4JStatement parent;
	
//	private List<BindResult> varToIncludeToDb4oQuery = new ArrayList<BindResult>();
//	private List<String> varNamesToIncludeToDb4oQuery = new ArrayList<String>();
//	private boolean needTo
	
	public TypeChecker(JavaNameResolver jnr, SBQL4JStatement parent) {
		this.jnr = jnr;
		this.cTypes = ClassTypes.getInstance();
		this.jSigFac = JavaSignatureAbstractFactory.getJavaSignatureFactory();
		this.parent = parent;
		this.envs = new StaticENVS(jnr);
	}
	
	public TypeChecker(JavaNameResolver jnr, List<BinderSignature> viewContext, SBQL4JStatement parent) {
		this(jnr, parent);
		initQueryViewContext(viewContext);
	}

	private void initQueryViewContext(List<BinderSignature> viewContext) {
		for(BinderSignature bs : viewContext) {
			List<StaticEVNSType> stackFrame = new ArrayList<StaticEVNSType>();
			stackFrame.add(bs);
			envs.add(stackFrame);
		}
	}
	
//	public Stack<List<? extends StaticEVNSType>> getEnvs() {
//		return envs;
//	}
//	public Stack<Signature> getQres() {
//		return qres;
//	}

	@Override
	public Signature evaluateExpression(Expression expr) {
		this.jnr.clearResolvedNames();
		this.rootExpr = expr;
		try {
			expr.accept(this, null);
			Signature sig = expr.getSignature();
			Signature result = null;
//			if(expr.getSignature() instanceof BinderSignature) {
//				BinderSignature bs = (BinderSignature) sig;
//				result = bs.getDerefSignatureWithCardinality();
//			} else {
				result = sig;	
//			}
			return result;
		} catch(RuntimeException e) {
			System.err.println("expr="+expr);
			e.printStackTrace();
			throw new SBQLException(e.getMessage(), e);
		}
	}

	@Override
	public Object visitAsExpression(AsExpression expr, Object object) {
		expr.ex1.accept(this, object);
		Signature valSig = expr.ex1.getSignature().clone();
		valSig.setColType(SCollectionType.NO_COLLECTION);
		ResultSource rs = expr.ex1.getSignature().getResultSource();
		BinderSignature bs = new BinderSignature(expr.identifier.val, valSig, true, rs);
		bs.setAssociatedExpression(expr);
		bs.setColType(expr.ex1.getSignature().getColType());
		if(expr.ex1.getSignature().getColType() != SCollectionType.NO_COLLECTION) {
			bs.setAuixiliaryCollection(true);
		}
		expr.setSignature(bs);
		return object;
	}

	@Override
	public Object visitBinarySimpleOperatorExpression(BinarySimpleOperatorExpression expr, Object object) {
//		if(expr.op instanceof OperatorIn) {
//			System.out.println();;
//		}
		Object o1 = expr.ex1.accept(this, object);
		Object o2 = expr.ex2.accept(this, object);
		expr.op.evalStatic(this, expr.ex1, expr.ex2, expr);
		Signature s1 = expr.ex1.getSignature();
		Signature s2 = expr.ex2.getSignature();
		if(s1.getResultSource() == ResultSource.DB4O 
				|| s2.getResultSource() == ResultSource.DB4O) {
			expr.getSignature().setResultSource(ResultSource.DB4O);
		}
		return Utils.mergeExpressionParams(o1, o2);
	}

//	@Override
//	public Object visitBinaryNonAExpression(BinaryNonAExpression expr,
//			Object object) {
//		expr.ex1.accept(this, object);
//		Signature s = expr.ex1.signature;
////		envs.push(s.nested());
//		expr.ex2.accept(this, object);
//		return null;
//	}

	@Override
	public Object visitCloseByExpression(CloseByExpression expr, Object object) {
		Object o1 = expr.ex1.accept(this, object);
		envs.push(expr.ex1.getSignature().nested(expr));
		expr.setENVSOpeningLevel(envs.size() - 1);
		Object o2 = expr.ex2.accept(this, object);
		envs.pop();
		Signature leftSig = expr.ex1.getSignature();
		Signature sig = expr.ex2.getSignature().clone();
		sig.setColType(SCollectionType.SEQUENCE);
		expr.setSignature(sig);
		
		return object;
	}

	@Override
	public Object visitComaExpression(ComaExpression expr, Object object) {
		Object o1 = expr.ex1.accept(this, object);
		Object o2 = expr.ex2.accept(this, object);
		OperatorComma op = new OperatorComma(OperatorType.COMA);
		op.evalStatic(this, expr.ex1, expr.ex2, expr);
		return object;
	}

	@Override
	public Object visitDerefExpression(DerefExpression derefExpression,
			Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitDotExpression(DotExpression dotExpression, Object object) {
		//nalezy ustalic, kiedy moze byc bindowana nazwa pakietu (nieokreslona na stosie)
		//po lewej stronie - zawsze, po prawej - tylko jesli nadrzedym wyrazeniem jest dotexpression
//		Map<String, Boolean> leftPropMap = new HashMap<String, Boolean>();
//		leftPropMap.put(PROP_KEY_ALLOW_FIND_PACKAGE, true);
//		Map<String, Boolean> rightPropMap = new HashMap<String, Boolean>();
//		if(dotExpression.parentExpression instanceof DotExpression) {
//			rightPropMap.put(PROP_KEY_ALLOW_FIND_PACKAGE, true);
//		}
		dotExpression.ex1.accept(this, object);
//		boolean nestingDb4oConn = false;
		if(dotExpression.ex1.getSignature() instanceof Db4oConnectionSignature) {
			envs.clearVarToIncludeToDb4oQuery();
//			varToIncludeToDb4oQuery = new ArrayList<BindResult>();
//			varNamesToIncludeToDb4oQuery = new ArrayList<String>();
//			nestingDb4oConn = true;
		}
		if(dotExpression.ex1.getSignature() instanceof XMLDataSourceSignature) {
			parent.setXmlUsed(true);
		}
		
		List nestedItems = dotExpression.ex1.getSignature().nested(dotExpression);
		ResultSource rs = dotExpression.ex1.getSignature().getResultSource();
		BinderSignature loopIndexBinder = new BinderSignature("$index", jSigFac.createJavaSignature(cTypes.getCompilerType(Integer.class)), true, rs);
		loopIndexBinder.setAssociatedExpression(dotExpression);
		loopIndexBinder.setNestedInfo(new NestedInfo(dotExpression.getSignature(), null, dotExpression));
		nestedItems.add(loopIndexBinder);
		envs.push(nestedItems);
//		if(nestingDb4oConn) {
//			Stack envsClone = (Stack) envs.clone();
//			System.out.println(envsClone);
//		}
		
		dotExpression.setENVSOpeningLevel(envs.size() - 1);
		dotExpression.ex2.accept(this, object);
		Signature leftSig = dotExpression.ex1.getSignature();
		
//		sig.sColType = leftSig.sColType;
		SCollectionType lCType = leftSig.getColType();
		SCollectionType resCType = null;
		if(lCType == SCollectionType.NO_COLLECTION) {
			resCType = dotExpression.ex2.getSignature().getColType();
		} else {
			resCType = lCType;
		}
		Signature sig = dotExpression.ex2.getSignature().clone();
		sig.setColType(resCType);
		dotExpression.setSignature(sig);
		envs.pop();
		dotExpression.setVarToIncludeToDb4oQuery(envs.getVarToIncludeToDb4oQuery());
		return object;
	}

	@Override
	public Object visitForEachExpression(ForEachExpression expr, Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitForallExpression(ForallExpression expr, Object object) {
		expr.ex1.accept(this, object);
		Signature s1 = expr.ex1.getSignature();
		List nestedItems = s1.nested(expr);
		ResultSource rs = expr.ex1.getSignature().getResultSource();
		BinderSignature loopIndexBinder = new BinderSignature("$index", jSigFac.createJavaSignature(cTypes.getCompilerType(Integer.class)), true, rs);
		loopIndexBinder.setAssociatedExpression(expr);
		loopIndexBinder.setNestedInfo(new NestedInfo(expr.getSignature(), null, expr));
		nestedItems.add(loopIndexBinder);
		envs.push(nestedItems);
		expr.setENVSOpeningLevel(envs.size() - 1);
		expr.ex2.accept(this, object);
		Signature s2 = expr.ex2.getSignature().getDerefSignatureWithCardinality();
		if(!(s1.getDerefSignatureWithCardinality() instanceof ValueSignature)) {
			addError(expr.ex1, "expected value, got "+expr.ex1);
		}
		if(!(s2 instanceof ValueSignature)) {
			addError(expr.ex2, "expected value, got "+expr.ex1);
		} else {
			ValueSignature vSig2 = (ValueSignature) s2;
			if(!vSig2.isTypeCompatible(Boolean.class)) {
				addInvalidTypeError(expr.ex2, Boolean.class.toString(), vSig2.getTypeName());
			}
		}
		envs.pop();
		ValueSignature resSig = jSigFac.createJavaSignature(cTypes.getCompilerType(Boolean.class));
		expr.setSignature(resSig);
		return object;
	}

	@Override
	public Object visitForanyExpression(ForanyExpression expr, Object object) {
		expr.ex1.accept(this, object);
		Signature s1 = expr.ex1.getSignature();
		List nestedItems = s1.nested(expr);
		ResultSource rs = expr.ex1.getSignature().getResultSource();
		BinderSignature loopIndexBinder = new BinderSignature("$index", jSigFac.createJavaSignature(cTypes.getCompilerType(Integer.class)), true, rs);
		loopIndexBinder.setAssociatedExpression(expr);
		loopIndexBinder.setNestedInfo(new NestedInfo(expr.getSignature(), null, expr));
		nestedItems.add(loopIndexBinder);
		envs.push(nestedItems);
		expr.setENVSOpeningLevel(envs.size() - 1);
		expr.ex2.accept(this, object);
		Signature s2 = expr.ex2.getSignature().getDerefSignatureWithCardinality();
		if(!(s1.getDerefSignatureWithCardinality() instanceof ValueSignature)) {
			addError(expr.ex1, "expected value, got "+expr.ex1);
		}
		if(!(s2 instanceof ValueSignature)) {
			addError(expr.ex2, "expected value, got "+expr.ex1);
		} else {
			ValueSignature vSig2 = (ValueSignature) s2;
			if(!vSig2.isTypeCompatible(Boolean.class)) {
				addInvalidTypeError(expr.ex2, Boolean.class.toString(), vSig2.getTypeName());
			}
		}
		envs.pop();
		ValueSignature resSig = jSigFac.createJavaSignature(cTypes.getCompilerType(Boolean.class));
		expr.setSignature(resSig);
		return object;
	}

	@Override
	public Object visitGroupAsExpression(GroupAsExpression expr, Object object) {
		expr.ex1.accept(this, object);
		Signature s = expr.ex1.getSignature().clone();
		ResultSource rs = expr.ex1.getSignature().getResultSource();
		BinderSignature bs = new BinderSignature(expr.identifier.val, s, true, rs);
		bs.setAssociatedExpression(expr);
//		bs.resultSource = expr.ex1.getSignature().resultSource;
		expr.setSignature(bs);
		return object;
	}

	@Override
	public Object visitNameExpression(NameExpression expr,
			Object object) {
		if(expr instanceof Db4oIndexInvocationExpression) {
			Db4oIndexInvocationExpression idxExp = (Db4oIndexInvocationExpression) expr;
			if(idxExp.getParamExpression() != null) {
				idxExp.getParamExpression().accept(this, object);
			}
		}
		boolean allowToFindPackage;
		//tylko gdy: nadrzednym jest dotexpression i expr jest jego lewym
		//lub: nadrzednym jest dotexpression i expr jest jego prawym i nad-nadrzednym jest dotexpression
		boolean parentDot = expr.parentExpression != null && expr.parentExpression instanceof DotExpression;
		boolean grandparentDot = parentDot && expr.parentExpression.parentExpression != null && expr.parentExpression.parentExpression instanceof DotExpression; 
		boolean leftDotExpr = parentDot && expr == ((DotExpression)expr.parentExpression).ex1;
		boolean rightDotExpr = !leftDotExpr && parentDot && expr == ((DotExpression)expr.parentExpression).ex2;
		
		allowToFindPackage = leftDotExpr || (rightDotExpr && grandparentDot);

		String name = expr.l.val;
		List<BindResult> br = envs.bind(name, false, expr, true);
		
//		if(br.isEmpty()) {
//			String getterMethName = "get"+Character.toUpperCase(name.charAt(0))+name.substring(1);
//			StructSignature noArgSig = new StructSignature();
//			BindResult b = bindMethod(getterMethName, noArgSig);
//			if(b == null) {
//				br = bind(name, allowToFindPackage, expr);
//			} else {
//				br.add(b);
//				expr.boundGetterMethodAsIdentifier = true;
//			}
//		}
		if(br.isEmpty()) {
			//package info
			Expression ie = parentDot 
				? ((DotExpression)expr.parentExpression).getFirstLeftPackageExpr()
				: expr;
				
			Signature sig = jSigFac.createValueSignature(ClassTypes.getInstance().objectClassType, false);
			expr.setSignature(sig);
			addError(ie, "Name "+name+" cannot be resolved");
		} else {
			BindResult br0 = br.get(0);
			if(br0.binder instanceof MethodSignature && br0.boundValue instanceof MethodSignature) {
				//bound getter method as name
				//replace NameExpression with MethodSignature
				MethodSignature ms = (MethodSignature) br0.boundValue;
				MethodExpression mexpr = new MethodExpression(expr.position, ms.method.name.toString(), null);
				expr.parentExpression.replaceSubExpression(expr, mexpr);
				mexpr.accept(this, object);
				return object;
			}
			expr.getBindResults().clear();
			for(BindResult b : br) {
				expr.l.val = b.boundName;
				expr.addBindResult(b);
				//lets check if bound value is view definition
				Signature bSig = b.boundValue;
				expr.setSignature(bSig);
				//licznosc kolekcji wiazanego obiektu, gdy pochodzi ze struktury
				if(b.binder instanceof BinderSignature && b.boundValueInfo.nestedFrom instanceof StructSignature) {
					BinderSignature binderSig = (BinderSignature) b.binder;
					SCollectionType colType = SCollectionType.getStrongerSCType(binderSig.getColType(), bSig.getColType());
					bSig.setColType(colType);
				}
//				if(bSig.getResultName() == null) {
//					bSig.setResultName(resultName)
//				}
				if(bSig instanceof ValueSignature) {
					ValueSignature vSig = (ValueSignature) bSig;
					Type vType = vSig.getType();
					if(vType instanceof ClassType) {
						ClassType ct = (ClassType) vType;
//						if(ct.isViewDelcaration()) {
//							viewUsed = true;
//							reportTypeErrors = false;
//							parent.markStatementAsUsingViews();
//						}
						
//						if(ct.viewInfo != null) {
//							//now we are sure, that view is used in the query
//							this.viewUsed = true;
//							//replacement
//							Expression replacement = ct.viewInfo.getViewExpressionReadReplacement();
//							if(expr != rootExpr) {
//								expr.parentExpression.replaceSubExpression(expr, replacement);
//							} else {
//								this.rootExpr = replacement;
//							}
//							this.evaluateExpression(replacement);
//						}
					}
				}
			}
		}
		
		return object;
	}

//	@Override
//	public Object visitJavaParamExpression(JavaParamExpression expr,
//			Object object) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public Object visitJoinExpression(JoinExpression joinExpression,
			Object object) {
		joinExpression.ex1.accept(this, object);
		envs.push(joinExpression.ex1.getSignature().nested(joinExpression));
		joinExpression.setENVSOpeningLevel(envs.size() - 1);
		joinExpression.ex2.accept(this, object);
		Signature s1 = joinExpression.ex1.getSignature();
		Signature s2 = joinExpression.ex2.getSignature();
		Signature res = Utils.createCartesianSignature(s1, s2);
		ResultSource rs = calculateResultSource(joinExpression);
		res.setResultSource(rs);
		joinExpression.setSignature(res);
		return object;
	}
 
	@Override
	public Object visitLiteralExpression(LiteralExpression expr, Object object) {
		Class valueClass = expr.l.getClass();
		Type valueJavaCType = cTypes.getCompilerType(valueClass);
		ValueSignature vs = jSigFac.createJavaSignature(valueJavaCType);
		if(valueClass != String.class) {
			vs.setPrimitiveType(true);
		}
		expr.setSignature(vs);
		expr.getSignature().setResultName(expr.generateJavaCode());
		return vs;
//		return null;
	}

	@Override
	public Object visitMethodExpression(MethodExpression expr, Object object) {
		Expression paramsExpr = expr.paramsExpression;
		StructSignature paramsSig = new StructSignature();
		if(paramsExpr != null) {
			paramsExpr.accept(this, object);
			Signature pSig = paramsExpr.getSignature();
			if(pSig instanceof StructSignature) {
				paramsSig = (StructSignature)pSig;
			} else {
				paramsSig = new StructSignature();
				paramsSig.addField(pSig);
			}
		}
		BindResult br = envs.bindMethod(expr.methodName, paramsSig);
		if(br != null) {
			MethodSignature jms = ((MethodSignature)br.boundValue).clone();
			jms.paramsSig = paramsSig;
			expr.setSignature(jms); 
			expr.bindResult = br;
		} else {
			expr.setSignature(JavaSignatureCompilerFactory.getInstance().VOID_VAL_SIG);
			addError(expr, "no method found: "+expr);
		}
		
		return null;
	}
	
	@Override
	public Object visitConstructorExpression(ConstructorExpression expr,
			Object object) {
		Expression paramsExpr = expr.paramsExpression;
		StructSignature paramsSig = new StructSignature();
		Signature classNameSignature = null;
		if(expr.classNameExpr != null) {
			expr.classNameExpr.accept(this, object);
			classNameSignature = expr.classNameExpr.getSignature();
		} else if(expr.classNameLiteral != null) {
			List<BindResult> br = envs.bind(expr.classNameLiteral, false, expr, false);
			if(!br.isEmpty()) {
				classNameSignature = br.get(0).boundValue;	
			}
		}
		
		if(classNameSignature == null) {
			addError(expr, "");
		} else if(! (classNameSignature instanceof ClassSignature)) {
			addError(expr, "cannot be recognized as class name");
		} else if(paramsExpr != null) {
			paramsExpr.accept(this, object);
			Signature pSig = paramsExpr.getSignature();
			if(pSig instanceof StructSignature) {
				paramsSig = (StructSignature)pSig;
			} else {
				paramsSig = new StructSignature();
				paramsSig.addField(pSig);
			}
		}
		ClassSignature cs = (ClassSignature) classNameSignature;
		ValueSignature resSig = jSigFac.createValueSignature(cs.getType(), false);
		expr.setSignature(resSig);
		envs.push(classNameSignature.nested(expr));
		expr.setENVSOpeningLevel(envs.size() - 1);
		BindResult br = envs.bindConstructor(cs.getTypeName(), paramsSig);
		if(br != null) {
			classNameSignature = br.boundValue;
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("no constructor found ").append(cs.getTypeName()).append("(");
//			for(Iterator<Signature> it = paramsSig.getFields())
			for(int i=0; i<paramsSig.fieldsNumber(); i++) {
				Signature s = paramsSig.getFields()[i];
				sb.append(s.getTypeName());
				if(i<paramsSig.fieldsNumber() - 1) {
					sb.append(", ");
				}
			}
			sb.append(")");
			addError(expr, sb.toString());
		}
		envs.pop();
		return null;
	}

	@Override
	public Object visitOrderByExpression(OrderByExpression expr, Object object) {
		expr.ex1.accept(this, object);
		Signature sLeft = expr.ex1.getSignature();
		envs.push(sLeft.nested(expr));
		expr.setENVSOpeningLevel(envs.size() - 1);
		for(Expression e : expr.paramExprs) {
			e.accept(this, object);	
		}
		Signature resSig = sLeft.clone();
		resSig.setColType(SCollectionType.SEQUENCE);
		expr.setSignature(resSig);
		return object;
	}
	
	@Override
	public Object visitOrderByParamExpression(OrderByParamExpression expr,
			Object object) {
		//should has parent expession
//		boolean hasParent = expr.parentExpression != null;
//		Type returnType = cTypes.getCompilerType(Object.class);
//		if(!hasParent) {
//			addError(expr, "should be used only with ORDER BY");
//		} else {
//			//parent expression should be OrderByExpression
//			//or (when there are more than one OrderByParam) it should be CommaExpression and grandparent expression should be OrderByExpression 
//			boolean isParentOrderBy = expr.parentExpression instanceof OrderByExpression;
//			if(!isParentOrderBy) {
//				if(!(expr.parentExpression.parentExpression != null && expr.parentExpression.parentExpression instanceof OrderByExpression) ) {
//					addError(expr, "should be used only with ORDER BY");
//				}
//			}
//		}
		Type returnType = cTypes.getCompilerType(Object.class);
		expr.paramExpression.accept(this, object);
		Signature paramSig = expr.paramExpression.getSignature().getDerefSignatureWithCardinality();
		boolean isParamComparable = false;
		if(!(paramSig instanceof ValueSignature)) {
			addInvalidTypeError(expr.paramExpression, "value", paramSig.getTypeName());
		} else {
			ValueSignature paramValSig = (ValueSignature) paramSig;
			if(paramValSig.getColType() != SCollectionType.NO_COLLECTION) {
				addInvalidTypeError(expr.paramExpression, "NO_COLLECTION", paramSig.getColType().toString());
			}
			Type comparableType = cTypes.getCompilerType(Comparable.class);
			isParamComparable = cTypes.isSubClass(paramValSig.getType(), comparableType);
			returnType = paramValSig.getType();
		}
		if(expr.comparatorExpression != null) {
			expr.comparatorExpression.accept(this, object);
			Signature comparatorSig = expr.comparatorExpression.getSignature();
			if(!(comparatorSig instanceof ValueSignature)) {
				addInvalidTypeError(expr.comparatorExpression, "value", comparatorSig.getTypeName());
			} else {
				ValueSignature comparatorValSig = (ValueSignature) comparatorSig;
				if(comparatorValSig.getColType() != SCollectionType.NO_COLLECTION) {
					addInvalidTypeError(expr.comparatorExpression, "NO_COLLECTION", comparatorSig.getColType().toString());
				}
				Type compType = comparatorValSig.getType();
				Type expectedCompType = cTypes.getCompilerType(Comparator.class);
				boolean isCompType = cTypes.isSubClass(compType, expectedCompType);
				if(!isCompType) {
					addInvalidTypeError(expr.comparatorExpression, Comparator.class.toString(), comparatorSig.getTypeName());
				}
			}
		} else {
			//check if param type is comparable
			if(!isParamComparable) {
				addError(expr.paramExpression, "java.lang.Comparable expected or point java.util.Comparator by 'using [comparator_expression]'");
			}
		}
		Signature resSig = jSigFac.createValueSignature(returnType, false);
		expr.setSignature(resSig);
		
		return object;
	}

	@Override
	public Object visitUnaryExpression(UnarySimpleOperatorExpression expr, Object object) {

		if(expr.genericExpression != null) {
			expr.genericExpression.accept(this, object);
			//check generic first - it must be a JavaClass signature
			boolean check = checkValidGenericSignature(expr.genericExpression, expr.op.getAllowedGenericType());
			if(expr.genericExpression.getSignature() == null) {
				addError(expr.genericExpression, "");
			} else if(! (expr.genericExpression.getSignature() instanceof ClassSignature)) {
				addError(expr.genericExpression, "expected class identifier");
			}
			ClassSignature cs = (ClassSignature) expr.genericExpression.getSignature();
//			if(!Collection.class.isAssignableFrom(cs.type)) {
//				addError(expr.genericExpression, "expected class that implements interface java.util.Collection");
//			}
		}
		
		expr.ex1.accept(this, object);
		if(expr.op instanceof OperatorBag) {
			expr.ex1 = convertCommasToUnions(expr.ex1);
			expr.ex1.accept(this, object);
		}
		expr.op.evalStatic(this, expr.ex1, expr);
		if(expr.ex1.getSignature().getResultSource() == ResultSource.DB4O) {
			expr.getSignature().setResultSource(ResultSource.DB4O);
		}
		return object;
	}

	@Override
	public Object visitWhereExpression(WhereExpression whereExpression,
			Object object) {

		whereExpression.ex1.accept(this, object);
		Signature sLeft = whereExpression.ex1.getSignature();
		List nestedItems = sLeft.nested(whereExpression);
		ResultSource rs = whereExpression.ex1.getSignature().getResultSource();
		BinderSignature loopIndexBinder = new BinderSignature("$index", jSigFac.createJavaSignature(cTypes.getCompilerType(Integer.class)), true, rs);
		loopIndexBinder.setAssociatedExpression(whereExpression);
		loopIndexBinder.setNestedInfo(new NestedInfo(whereExpression.getSignature(), null, whereExpression));
		nestedItems.add(loopIndexBinder);
		envs.push(nestedItems);
		whereExpression.setENVSOpeningLevel(envs.size() - 1);
		whereExpression.ex2.accept(this, object);
		nestedItems.remove(loopIndexBinder);
		Signature sRight = whereExpression.ex2.getSignature();
		
		ValueSignature rightExpectedSig = jSigFac.createJavaSignature(cTypes.getCompilerType(Boolean.class));
		if(!sRight.isTypeCompatible(rightExpectedSig)) {
			addError(whereExpression.ex2, "expected Boolean value, got: "+sRight.getTypeName());
		}
		
//		if(!(sRight instanceof ValueSignature) || 
//				((ValueSignature)sRight).type != Boolean.class) {
//			addError(whereExpression.ex2, "expected Boolean value, got: "+sRight);
//		}
		whereExpression.setSignature(sLeft.clone());
		if(sLeft.getResultSource() == ResultSource.DB4O || sRight.getResultSource() == ResultSource.DB4O) {
			whereExpression.getSignature().setResultSource(ResultSource.DB4O);
		}
		envs.pop();
		return null;
	}
	
	@Override
	public Object visitRangeExpression(RangeExpression expr, Object object) {
		expr.ex1.accept(this, object);
		StructSignature result = new StructSignature();
		ValueSignature leftExpectedSig = jSigFac.createJavaSignature(cTypes.getCompilerType(Integer.class));
		leftExpectedSig.setColType(SCollectionType.NO_COLLECTION);
		Signature leftSig = expr.ex1.getSignature().getDerefSignatureWithCardinality();
		if(!leftSig.isTypeCompatible(leftExpectedSig)) {
			addError(expr.ex1, "expected int value, got: "+leftSig.getTypeName());
		} else {
			result.addField(leftSig);
		}
		
		if(expr.ex2 != null && !expr.isUpperUnbounded) {
			expr.ex2.accept(this, object);
			Signature rightSig = expr.ex2.getSignature().getDerefSignatureWithCardinality();
			if(!rightSig.isTypeCompatible(leftExpectedSig)) {
				addError(expr.ex2, "expected int value, got: "+rightSig.getTypeName());
			} else {
				result.addField(rightSig);
			}
		}
		expr.setSignature(result);
		return object;
	}
	
	@Override
	public Object visitConditionalExpression(ConditionalExpression expr,
			Object object) {
		expr.conditionExpr.accept(this, object);
		Signature condSig = expr.conditionExpr.getSignature();
		expr.trueExpr.accept(this, object);
		Signature trueSig = expr.trueExpr.getSignature();
		expr.falseExpr.accept(this, object);
		Signature falseSig = expr.falseExpr.getSignature();
		ValueSignature expectedCondSig = jSigFac.createValueSignature(cTypes.getCompilerType(Boolean.class), false);
		expectedCondSig.setColType(SCollectionType.NO_COLLECTION);
		boolean isCondTypeOK = condSig.isTypeCompatible(expectedCondSig);
		if(!isCondTypeOK) {
			addInvalidTypeError(expr.conditionExpr, "boolean", condSig.getTypeName());
		}
		Type returnType;
		SCollectionType returnSCType;
		Signature returnSig = null;
		if(trueSig instanceof ValueSignature && falseSig instanceof ValueSignature) {
			ValueSignature trueVSig = (ValueSignature) trueSig;
			ValueSignature falseVSig = (ValueSignature) falseSig;
			returnType = cTypes.getSharedAncestor(trueVSig.getType(), falseVSig.getType());
			returnSCType = SCollectionType.getStrongerSCType(trueVSig.getColType(), falseVSig.getColType());
			returnSig= jSigFac.createValueSignature(returnType, false);
		} else if(trueSig instanceof BinderSignature && falseSig instanceof BinderSignature) {
			BinderSignature trueBSig = (BinderSignature) trueSig;
			BinderSignature falseBSig = (BinderSignature) falseSig;
			if(!trueBSig.name.equals(falseBSig.name)) {
				addError(expr, "both binder must have same name, got true: "+trueBSig.name+", false: "+falseBSig.name);
			}
//			returnSig = trueBSig.clone();
			VariantSignature vsig = new VariantSignature();
			vsig.addField(trueBSig.clone());
			vsig.addField(falseBSig.clone());
			returnSig = vsig;
		} else if(trueSig instanceof StructSignature && falseSig instanceof StructSignature) {
//			returnSig = trueSig.clone();
			VariantSignature vsig = new VariantSignature();
			vsig.addField(trueSig.clone());
			vsig.addField(falseSig.clone());
			returnSig = vsig;
		} else {
			addError(expr, "expected both values, got true: "+trueSig+", false: "+falseSig);
			returnType = cTypes.getCompilerType(Object.class);
			returnSCType = SCollectionType.NO_COLLECTION;
			returnSig = jSigFac.createValueSignature(returnType, false);
		}
		returnSig.setAssociatedExpression(expr);
		expr.setSignature(returnSig);
		return object;
	}
	
	public void addError(Expression node, String error) {
//		System.err.println(node+" "+error);
//		System.out.println("position of "+node+" is "+node.position);
//		log.error(0, "proc.messager", "position of "+node+" is "+node.position);
		if(reportTypeErrors) {
			log.error(jnr.getQueryStartPos() + node.position + 2, "proc.messager", error);
		}
	}
	public void addInvalidTypeError(Expression node, String expectedType, String currentType) {
//		System.out.println("position of "+node+" is "+node.position);
		if(reportTypeErrors) {
			log.error(jnr.getQueryStartPos() + node.position + 2, "sbql.invalid_type", expectedType, currentType);
		}
	}
	

/*	private List<BindResult> bind(String name, boolean allowFindPackage, BindExpression bindExpression, boolean checkGetterMethods) {
//		for(List<? extends ENVSType> o : envs) {
		List<BindResult> res = new ArrayList<BindResult>();
		boolean db4oContext = false;
		//level of envs where found db4o nested marker (where binders to db4o objects should be)
		int db4oContextSection = -1;
		for(int i=envs.size()-1; i >= 0; i--) {
			List<? extends StaticEVNSType> o = envs.get(i);
			List<? extends StaticEVNSType> section = (List<? extends StaticEVNSType>) o;
			for(StaticEVNSType secEl : section) {
				if(secEl instanceof Db4oNestedMarker) {
					db4oContext = true;
					db4oContextSection = i;
				}
				if(secEl instanceof BinderSignature) {
					BinderSignature b = (BinderSignature) secEl;
					if(name.equals(b.name)) {
						BindResult r = new BindResult(name, b.value.clone(), b.getNestedInfo(), b, i);
						bindExpression.setBoundSignature(b);
						res.add(r);
					}
				} 
			}
			if(res.isEmpty() && checkGetterMethods) {
				String mName = "get"+Character.toUpperCase(name.charAt(0))+name.substring(1);;
				if(bindExpression instanceof NameExpression) {
					if(((NameExpression)bindExpression).boundGetterMethodAsIdentifier == true) {
						mName = name;
					}
				}
				
				StructSignature noArgSig = new StructSignature();
				for(StaticEVNSType secEl : section) {
					if(secEl instanceof MethodSignature) {
						MethodSignature b = (MethodSignature) secEl;
						if(b.isApplicableTo(mName, noArgSig)) {
							jnr.addResolvedMethod(b.method);
							BindResult r = new BindResult(mName, b.clone(), b.getNestedInfo(), b, i);
							bindExpression.setBoundSignature(b);
							if(bindExpression instanceof NameExpression) {
								((NameExpression)bindExpression).boundGetterMethodAsIdentifier = true;
							}
							res.add(r);
						}
					} 
				}
				mName  = "is"+Character.toUpperCase(name.charAt(0))+name.substring(1);
				for(StaticEVNSType secEl : section) {
					if(secEl instanceof MethodSignature) {
						MethodSignature b = (MethodSignature) secEl;
						String methResTypeString = b.returnTypeSig.typeName;
						String boolanClassName = Boolean.class.getName();
						if(b.isApplicableTo(mName, noArgSig) && methResTypeString.equals(boolanClassName)) {
							jnr.addResolvedMethod(b.method);
							BindResult r = new BindResult(mName, b.clone(), b.getNestedInfo(), b, i);
							bindExpression.setBoundSignature(b);
							if(bindExpression instanceof NameExpression) {
								((NameExpression)bindExpression).boundGetterMethodAsIdentifier = true;
							}
							res.add(r);
						}
					} 
				}
			}
		}
		// na koncu szukamy w zakresie nazw javy
		//sprawdzamy, czy na stosie nie ma informacji o pakiecie
		if(res.isEmpty()) {
			TypeSymbol pckSymbol = null;
			for(int i=envs.size()-1; i >= 0; i--) {
				List<? extends StaticEVNSType> o = envs.get(i);
				if(!o.isEmpty()) {
					StaticEVNSType s = o.get(0);
					if(s instanceof PackageSignature) {
						pckSymbol = ((PackageSignature)s).compilerSymbol;
						break;
					}
				}
			}
			Signature sig = jnr.findIdent(name, pckSymbol);
			int foundOnStackLevel = -1;
			boolean classNameAsDb4oObjects = false;
			if(sig instanceof ClassSignature && db4oContext) {
				ClassSignature cs = (ClassSignature)sig;
				sig = jSigFac.createValueSignature(cs.type, false);
				sig.sColType = SCollectionType.BAG;
				sig.setResultSource(ResultSource.DB4O);
//				name = cs.type.tsym.toString();
				NameExpression ns = (NameExpression)bindExpression;
				ns.fullName = cs.type.tsym.toString();
				//db4o connection - stack level -1, object in db4o - stack level 0
				foundOnStackLevel = db4oContextSection;
				classNameAsDb4oObjects = true;
				
			}
			if(sig instanceof PackageSignature && !allowFindPackage) {
			} else if(sig != null) {
				BindResult r = new BindResult(name, sig, null, null, foundOnStackLevel);
				res.add(r);
				
			}
		}
		if(db4oContext) {
			for(BindResult br : res) {
				Signature sig = br.boundValue;
				boolean originDb4o = sig.getResultSource() == ResultSource.DB4O;
				boolean isLiteral = sig.getAssociatedExpression() instanceof LiteralExpression;
				if(!originDb4o && !(sig instanceof Db4oConnectionSignature) && !isLiteral) {
					if(!varNamesToIncludeToDb4oQuery.contains(name)) {
						varToIncludeToDb4oQuery.add(br);
						varNamesToIncludeToDb4oQuery.add(name);
					}
				}
			}
		}
		
		return res;
	}
	
	private BindResult bindMethod(String mName, StructSignature paramsSig) {
//		ValueSignature res = null;
//		boolean found = false;
//		for(List<? extends ENVSType> o : envs) {
		for(int i=envs.size()-1; i >= 0; i--) {
			List<? extends StaticEVNSType> o = envs.get(i);
//			if(found) return res;
			List<? extends StaticEVNSType> section = (List<? extends StaticEVNSType>) o;
			for(StaticEVNSType secEl : section) {
				if(secEl instanceof MethodSignature) {
					MethodSignature b = (MethodSignature) secEl;
					if(b.isApplicableTo(mName, paramsSig)) {
						jnr.addResolvedMethod(b.method);
						BindResult r = new BindResult(mName, b.clone(), b.getNestedInfo(), b, i);
//						bindExpression.setBoundSignature(b);
						return r;
					}
				} 
			}
		}
		return null; //wyjatek
//		return res;
	}
	
	private BindResult bindConstructor(String className, StructSignature paramsSig) {
//		ValueSignature res = null;
//		boolean found = false;
//		for(List<? extends ENVSType> o : envs) {
		for(int i=envs.size()-1; i >= 0; i--) {
			List<? extends StaticEVNSType> o = envs.get(i);
//			if(found) return res;
			List<? extends Signature> section = (List<? extends Signature>) o;
			for(Signature secEl : section) {
				if(secEl instanceof ConstructorSignature) {
					ConstructorSignature b = (ConstructorSignature) secEl;
					String qName = b.ownerClass.getQualifiedName().toString();
					if(qName.equals(className) &&  b.isApplicableTo(paramsSig)) {
//						jnr.addResolvedMethod(b.method);
						return new BindResult(className, b.clone(), b.getNestedInfo(), b, i);
					}
				} 
			}
		}
		return null; //wyjatek
//		return res;
	}
	*/
	private boolean checkValidGenericSignature(Expression genExpr, Class awaitedGenericClass) {
//		if(genExpr != null) {
			if(awaitedGenericClass == null) {
				addError(genExpr, "no generics is allowed");
			}
			if(genExpr.getSignature() == null) {
				addError(genExpr, "");
				return false;
			} else if(! (genExpr.getSignature() instanceof ClassSignature)) {
				addError(genExpr, "expected class identifier");
				return false;
			}
			ClassSignature cs = (ClassSignature) genExpr.getSignature();
			ClassTypes ct = ClassTypes.getInstance();
			Type aGenJavaCType = ct.getCompilerType(awaitedGenericClass);
			if( !(aGenJavaCType instanceof ClassType) ) {
				addError(genExpr, "expected class that implements interface java.util.Collection");
				return false;
			}
			ClassType genCT = (ClassType) aGenJavaCType;
			ClassType ct2 = (ClassType) cs.getType();
			if(!ct.isSubClass(ct2, genCT)) {
				addError(genExpr, "expected class that implements interface java.util.Collection");
				return false;
			}
//			if(!awaitedGenericClass.isAssignableFrom(cs.type)) {
//				addError(genExpr, "expected class that implements interface java.util.Collection");
//				return false;
//			}
			return true;
//		}
	}

	public Signature getResultType() {
		return resultType;
	}
	
	public boolean isReportTypeErrors() {
		return reportTypeErrors;
	}
	public void setReportTypeErrors(boolean reportTypeErrors) {
		this.reportTypeErrors = reportTypeErrors;
	}
	
	public boolean isViewUsed() {
		return viewUsed;
	}
	
	private Expression convertCommasToUnions(Expression expr) {
		if (expr instanceof ComaExpression) {
			ComaExpression cExpr = (ComaExpression) expr;
			if(!cExpr.ex1.getSignature().isCollectionResult() && !cExpr.ex2.getSignature().isCollectionResult()) {
				Expression leftExpr = convertCommasToUnions(cExpr.ex1);
				BinarySimpleOperatorExpression unionExpr = new BinarySimpleOperatorExpression(cExpr.position, leftExpr, cExpr.ex2, OperatorFactory.getOperator(OperatorType.UNION));
				unionExpr.deadQData = cExpr.deadQData;
				expr.parentExpression.replaceSubExpression(expr, unionExpr);
				return unionExpr;
			}
		}
		return expr;
	}
	
	public static ResultSource calculateResultSource(BinaryExpression expr) {
		ResultSource rs = ResultSource.JAVA_HEAP;
		if(expr.ex1.getSignature().getResultSource() != ResultSource.JAVA_HEAP) {
			rs = expr.ex1.getSignature().getResultSource();
		}	
		if(expr.ex2.getSignature().getResultSource() != ResultSource.JAVA_HEAP) {
			expr.ex2.getSignature().getResultSource(); 
		}
		return rs;
	}
	
//	private void evalAgainstNestedEnvironment(Expression lExpr, Expression rExpr) {
//		lExpr.signature.nested();
//	}
	
}

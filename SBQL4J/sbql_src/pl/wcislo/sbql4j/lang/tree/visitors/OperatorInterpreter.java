package pl.wcislo.sbql4j.lang.tree.visitors;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import pl.wcislo.sbql4j.exception.SBQLException;
import pl.wcislo.sbql4j.java.model.runtime.JavaClass;
import pl.wcislo.sbql4j.java.model.runtime.JavaComplexObject;
import pl.wcislo.sbql4j.java.model.runtime.JavaObject;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectAbstractFactory;
import pl.wcislo.sbql4j.java.model.runtime.factory.JavaObjectFactory;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.MathUtils;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorAnd;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorAvg;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorBag;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorComma;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorCount;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorDivide;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorElementAt;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorEquals;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorExcept;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorExists;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorIn;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorInstanceof;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorIntersect;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorLess;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorLessOrEqual;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMax;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMin;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMinus;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorModulo;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMore;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMoreOrEqual;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorMultiply;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorNot;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorNotEquals;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorOr;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorPlus;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorSequence;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorStruct;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorSum;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorUnion;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.OperatorUnique;
import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.model.StructSBQL;
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.model.collections.CollectionResult;
import pl.wcislo.sbql4j.model.collections.Sequence;
import pl.wcislo.sbql4j.util.Utils;

public class OperatorInterpreter implements OperatorVisitor<Void, Interpreter> {

	protected static final JavaObjectFactory javaObjectFactory = JavaObjectAbstractFactory.getJavaObjectFactory();
	
	@Override
	public Void visitAnd(OperatorAnd op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Object rightArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());		
		Object leftArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		
		JavaObject result = null;
		if ((leftArg instanceof Boolean) && (rightArg instanceof Boolean)) {
			Boolean b1 = (Boolean) leftArg;
			Boolean b2 = (Boolean) rightArg;
			JavaObjectFactory fac = JavaObjectAbstractFactory.getJavaObjectFactory();
			result = fac.createJavaComplexObject(b1 && b2);
		} else {
			throw new RuntimeException(
					"OperatorAnd.eval() invalid type: left=" + leftArg
							+ " right=" + rightArg);
		} 
		treeVisitor.getQres().push(result);
		return null;
	}
	
	@Override
	public Void visitAvg(OperatorAvg op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		QueryResult result;
		QueryResult objects = treeVisitor.getQres().pop();
		double resultS =0;
		if(objects instanceof Collection) {
			Collection<QueryResult> section = (Collection<QueryResult>) objects;
			for(QueryResult secEl : section) {
				Number n = (Number) Utils.toSimpleValue(secEl, treeVisitor.getStore());
				resultS += n.doubleValue();   
			}
			resultS = resultS/(section.size());
		} else {
			resultS = ((Number) (Utils.toSimpleValue(objects, treeVisitor.getStore()))).doubleValue();
		}
		result = JavaObjectAbstractFactory.getJavaObjectFactory(). 
			createJavaComplexObject(resultS);
		
		treeVisitor.getQres().push(result);
		return null;
	}
	
	@Override
	public Void visitBag(OperatorBag op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Bag eres = null;
		if (subExprs.length < 2) {
			eres = new Bag();
		} else {
			// mamy generyczny bag - args[1] okresla jego typ
			Expression bagTypeExpr = subExprs[1];
			bagTypeExpr.accept(treeVisitor, null);
			QueryResult qr = treeVisitor.getQres().pop();
			QueryResult clazz = Utils.collectionToObject(qr);
			JavaClass<Collection> jc = (JavaClass<Collection>) clazz;
			try {
				Collection c = jc.value.newInstance();
				eres = new Bag<Collection<QueryResult>>(c);
			} catch (Exception e) {
				throw new SBQLException(e);
			}
		}
		QueryResult e1res = treeVisitor.getQres().pop();
		StructSBQL s;
		if (e1res instanceof StructSBQL) {
			s = (StructSBQL) e1res;
		} else {
			s = new StructSBQL();
			if (e1res instanceof Collection) {
				s.addAll((Collection) e1res);
			} else {
				s.add(e1res);
			}
		}
		eres.addAll(s);
		treeVisitor.getQres().push(eres);
		return null;
	}
	
	@Override
	public Void visitComma(OperatorComma op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Bag eres = new Bag();
		QueryResult e2res = treeVisitor.getQres().pop();
		QueryResult e1res = treeVisitor.getQres().pop();

		QueryResult res = Utils.cartesianProduct(e1res, e2res);
		treeVisitor.getQres().push(res);
		return null;
	}
	
	@Override
	public Void visitCount(OperatorCount op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		QueryResult result;
		int count = 0;
	
		Object objects = treeVisitor.getQres().pop();
		if(objects instanceof CollectionResult) {
			count = ((CollectionResult) objects).size();
		} else {
			count = 1;
		}
		result = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject(count);
		treeVisitor.getQres().push(result);
		return null;
	}
	
	@Override
	public Void visitDivide(OperatorDivide op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Number rightArg = (Number) Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		Number leftArg = (Number) Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		Number resultNum = MathUtils.divide(leftArg, rightArg);
		QueryResult result = javaObjectFactory.createJavaComplexObject(resultNum);
		treeVisitor.getQres().push(result);
		return null;
	}
	
	@Override
	public Void visitElementAt(OperatorElementAt op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		QueryResult rightVal = treeVisitor.getQres().pop();
		QueryResult leftArg = treeVisitor.getQres().pop();
		if(leftArg instanceof Binder) {
			leftArg = ((Binder)leftArg).object;
		}
		if(!(leftArg instanceof CollectionResult)) {
			throw new RuntimeException("execpted Sequence, got: "+leftArg.getClass());
		}
		CollectionResult leftCol = (CollectionResult) leftArg;
		if(!(rightVal instanceof StructSBQL)) {
			Object rightArg = Utils.toSimpleValue(rightVal, treeVisitor.getStore());
			if(!(rightArg instanceof Integer || rightArg instanceof Long || rightArg instanceof Short || rightArg instanceof Byte)) {
				throw new RuntimeException("execpted Number, got: "+rightArg.getClass());
			}
			Integer i = ((Number)rightArg).intValue();
			QueryResult result;
			if(leftCol.size() < i+1) {
				result = new Bag();
			} else {
				if(leftCol instanceof Sequence) {
					result = ((Sequence)leftCol).get(i);	
				} else {
					Iterator<QueryResult> it = leftCol.iterator();
					QueryResult qr = it.next();
					for(int j=0; j<i; j++) {
						qr = it.next();
					}
					result = qr;
				}
			}
			treeVisitor.getQres().push(result);
		} else {
			StructSBQL bounds = (StructSBQL) rightVal;
			Integer lowBound = (Integer) Utils.toSimpleValue(bounds.get(0), treeVisitor.getStore());
			Integer upBound;
			if(bounds.size() < 2) {
				upBound = Integer.MAX_VALUE;
			} else {
				upBound = (Integer) Utils.toSimpleValue(bounds.get(1), treeVisitor.getStore());
			}
			CollectionResult result;
			if(leftCol instanceof Sequence) {
				result = new Sequence();
			} else {
				result = new Bag();
			}
			Iterator<QueryResult> it = leftCol.iterator();
			int i=0;
			while(it.hasNext() && i <= upBound) {
				QueryResult el = it.next();
				if(i >= lowBound && i <= upBound) {
					result.add(el);
				}
				i++;
			}
			treeVisitor.getQres().push(result);
		}
		return null;
	}
	
	@Override
	public Void visitEquals(OperatorEquals op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Object rightArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());		
		Object leftArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		QueryResult result;
		result = JavaObjectAbstractFactory.getJavaObjectFactory().createJavaComplexObject((Utils.equalsNumeric(leftArg, rightArg)));
		treeVisitor.getQres().push(result);
		return null;
	}
	
	@Override
	public Void visitExcept(OperatorExcept op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Bag eres = new Bag();
		CollectionResult e2res = Utils.objectToCollection(treeVisitor.getQres().pop());
		CollectionResult e1res = Utils.objectToCollection(treeVisitor.getQres().pop());
		
		eres.addAll(e1res);
		for(Iterator<QueryResult> i1 = eres.iterator(); i1.hasNext(); ) {
			QueryResult t1 = i1.next();
			for(Iterator<QueryResult> i2 = e2res.iterator(); i2.hasNext(); ) {
				QueryResult t2 = i2.next();
				if(Utils.equalsWithDeref(t1, t2, treeVisitor.getStore())) {
					i1.remove();
					break;
				}
			}
		}
		treeVisitor.getQres().push(eres);
		return null;
	}
	
	@Override
	public Void visitExists(OperatorExists op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		QueryResult e1res = treeVisitor.getQres().pop();
		CollectionResult col = Utils.objectToCollection(e1res);
		boolean result = !col.isEmpty();
		treeVisitor.getQres().add(javaObjectFactory.createJavaComplexObject(result));
		return null;
	}
	
	@Override
	public Void visitIn(OperatorIn op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		CollectionResult e2res = Utils.objectToCollection(treeVisitor.getQres().pop());
		CollectionResult e1res = Utils.objectToCollection(treeVisitor.getQres().pop());
		boolean result = true;
		for(Iterator<QueryResult> i1 = e1res.iterator(); result && i1.hasNext(); ) {
			QueryResult t1 = i1.next();
			boolean r2 = false;
			for(Iterator<QueryResult> i2 = e2res.iterator(); !r2 && i2.hasNext(); ) {
				QueryResult t2 = i2.next();
				if(Utils.equalsWithDeref(t1, t2, treeVisitor.getStore())) {
					r2 = true;
				}
			}
			if(!r2) {
				result = false;
			}
		}		 		
		treeVisitor.getQres().push(javaObjectFactory.createJavaComplexObject(result));
		return null;
	}
	
	@Override
	public Void visitInstanceof(OperatorInstanceof op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Class clazz = (Class)Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());		
		Object leftArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		Boolean result = clazz.isInstance(leftArg);
		JavaObject res = javaObjectFactory.createJavaComplexObject(result);
		treeVisitor.getQres().push(res);
		return null;
	}
	
	@Override
	public Void visitIntersect(OperatorIntersect op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		CollectionResult e2res = Utils.objectToCollection(treeVisitor.getQres().pop());
		CollectionResult e1res = Utils.objectToCollection(treeVisitor.getQres().pop());
		Bag result = new Bag();
		result.addAll(CollectionUtils.intersection(e1res, e2res));
		treeVisitor.getQres().push(result);
		return null;
	}
	
	@Override
	public Void visitLess(OperatorLess op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Object rightArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());		
		Object leftArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		
		Boolean result;
		if(leftArg instanceof Number && rightArg instanceof Number) {
			Number n1 = (Number) leftArg;
			Number n2 = (Number) rightArg;
			result = n1.doubleValue() < n2.doubleValue();
		} else {
			Comparable c1 = (Comparable) leftArg;
			Comparable c2 = (Comparable) rightArg;
			int cRes = MathUtils.compareSafe(c1, c2);
			result = cRes < 0;
		}
		JavaComplexObject res = JavaObjectAbstractFactory.getJavaObjectFactory().
			createJavaComplexObject(result);
		treeVisitor.getQres().push(res);
		return null;
	}
	
	@Override
	public Void visitLessOrEqual(OperatorLessOrEqual op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Object rightArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());		
		Object leftArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		
		Boolean result;
		if(leftArg instanceof Number && rightArg instanceof Number) {
			Number n1 = (Number) leftArg;
			Number n2 = (Number) rightArg;
			result = n1.doubleValue() <= n2.doubleValue();
		} else {
			Comparable c1 = (Comparable) leftArg;
			Comparable c2 = (Comparable) rightArg;
			int cRes = MathUtils.compareSafe(c1, c2);
			result = cRes <= 0;
		}
		JavaComplexObject res = JavaObjectAbstractFactory.getJavaObjectFactory().
			createJavaComplexObject(result);
		 
		treeVisitor.getQres().push(res);
		return null;
	}
	
	@Override
	public Void visitMax(OperatorMax op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		QueryResult e1res = treeVisitor.getQres().pop();
		CollectionResult colRes = Utils.objectToCollection(e1res);
		Number max = null;
		try {
			for (QueryResult object : colRes) {
				Number n = (Number) Utils.toSimpleValue(object, treeVisitor.getStore());
				max = MathUtils.max(max, n);
			}
			treeVisitor.getQres().push(javaObjectFactory.createJavaComplexObject(max));
			
		}catch(ClassCastException e){
			throw new RuntimeException("OperatorMax.eval() invalid type: type should be a primitive value");
		}
		return null;
	}
	
	@Override
	public Void visitMin(OperatorMin op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		QueryResult e1res = treeVisitor.getQres().pop();
		CollectionResult colRes = Utils.objectToCollection(e1res);
		Number min = null;
		try {
			for (QueryResult object : colRes) {
				Number n = (Number) Utils.toSimpleValue(object, treeVisitor.getStore());
				min = MathUtils.min(min, n);
			}
			treeVisitor.getQres().push(javaObjectFactory.createJavaComplexObject(min));
		}catch(ClassCastException e){
			throw new RuntimeException("OperatorMin.eval() invalid type: type should be a primitive value");
		}
		return null;
	}
	
	@Override
	public Void visitMinus(OperatorMinus op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Number rightArg = (Number) Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		Number leftArg = (Number) Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		Number resultNum = MathUtils.subtract(leftArg, rightArg);
		QueryResult result = javaObjectFactory.createJavaComplexObject(resultNum);
		treeVisitor.getQres().push(result);
		return null;
	}
	
	@Override
	public Void visitModulo(OperatorModulo op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Number rightArg = (Number) Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		Number leftArg = (Number) Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		Number resultNum = MathUtils.modulo(leftArg, rightArg);
		QueryResult result = javaObjectFactory.createJavaComplexObject(resultNum);
		treeVisitor.getQres().push(result);
		return null;
	}
	
	@Override
	public Void visitMore(OperatorMore op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Object rightArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());		
		Object leftArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		
		Boolean result;
		if(leftArg instanceof Number && rightArg instanceof Number) {
			Number n1 = (Number) leftArg;
			Number n2 = (Number) rightArg;
			result = n1.doubleValue() > n2.doubleValue();
		} else {
			Comparable c1 = (Comparable) leftArg;
			Comparable c2 = (Comparable) rightArg;
			int cRes = MathUtils.compareSafe(c1, c2);
			result = cRes > 0;
		}
		JavaComplexObject res = javaObjectFactory.createJavaComplexObject(result);
		treeVisitor.getQres().push(res);
		return null;
	}
	
	@Override
	public Void visitMoreOrEqual(OperatorMoreOrEqual op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Object rightArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());		
		Object leftArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		
		Boolean result;
		if(leftArg instanceof Number && rightArg instanceof Number) {
			Number n1 = (Number) leftArg;
			Number n2 = (Number) rightArg;
			result = n1.doubleValue() >= n2.doubleValue();
		} else {
			Comparable c1 = (Comparable) leftArg;
			Comparable c2 = (Comparable) rightArg;
			int cRes = MathUtils.compareSafe(c1, c2);
			result = cRes >= 0;
		}
		JavaComplexObject res = javaObjectFactory.createJavaComplexObject(result);
		 
		treeVisitor.getQres().push(res);
		return null;
	}
	
	@Override
	public Void visitMultiply(OperatorMultiply op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Number rightArg = (Number) Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		Number leftArg = (Number) Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		Number resultNum = MathUtils.multiply(leftArg, rightArg);
		QueryResult result = javaObjectFactory.createJavaComplexObject(resultNum);
		treeVisitor.getQres().push(result);
		return null;
	}
	
	@Override
	public Void visitNot(OperatorNot op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Boolean b = (Boolean) Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		treeVisitor.getQres().add(javaObjectFactory.createJavaComplexObject(!b));
		return null;
	}
	
	@Override
	public Void visitNotEquals(OperatorNotEquals op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Object rightArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());		
		Object leftArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		QueryResult result;
		result = javaObjectFactory.createJavaComplexObject(!Utils.equalsNumeric(leftArg, rightArg));
		treeVisitor.getQres().push(result);
		return null;
	}
	
	@Override
	public Void visitOr(OperatorOr op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Boolean rightArg = (Boolean) Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());		
		Boolean leftArg = (Boolean) Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		QueryResult result = javaObjectFactory.createJavaComplexObject(leftArg || rightArg);
		treeVisitor.getQres().push(result);
		return null;
	}
	
	@Override
	public Void visitPlus(OperatorPlus op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Object rightArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		Object leftArg = Utils.toSimpleValue(treeVisitor.getQres().pop(), treeVisitor.getStore());
		QueryResult result;
		if(leftArg instanceof String || rightArg instanceof String) {
			result = javaObjectFactory.createJavaComplexObject(leftArg.toString().concat(rightArg.toString()));
		} else {
			Number resultNum = MathUtils.sum((Number)leftArg, (Number)rightArg);
			result = javaObjectFactory.createJavaComplexObject(resultNum);
		}
		treeVisitor.getQres().push(result);
		return null;
	}
	
	@Override
	public Void visitSequence(OperatorSequence op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Sequence eres = null;
		if(subExprs.length < 2) {
			eres = new Sequence();
		} else {
			//mamy generyczna sekwencje - args[1] okresla jej typ
			Expression bagTypeExpr = subExprs[1];
			bagTypeExpr.accept(treeVisitor, null);
			QueryResult qr = treeVisitor.getQres().pop();
			QueryResult clazz = Utils.collectionToObject(qr);
			JavaClass<List> jc = (JavaClass<List>)clazz;
			try {
				List c = jc.value.newInstance();
				eres = new Sequence<List<QueryResult>>(c);
			} catch (Exception e) {
				throw new SBQLException(e);
			}
		}
		QueryResult e1res = treeVisitor.getQres().pop();
		
		StructSBQL s;
		if(e1res instanceof StructSBQL) {
			s = (StructSBQL) e1res;
		} else {
			s = new StructSBQL();
			if(e1res instanceof Collection) {
				s.addAll((Collection) e1res);
			} else {
				s.add(e1res);
			}
		}
		eres.addAll(s);
		treeVisitor.getQres().push(eres);
		return null;
	}
	
	@Override
	public Void visitStruct(OperatorStruct op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		StructSBQL eres = new StructSBQL();
		QueryResult e1res = treeVisitor.getQres().pop();
		StructSBQL s;
		if(e1res instanceof StructSBQL) {
			s = (StructSBQL) e1res;
		} else {
			s = new StructSBQL();
			if(e1res instanceof CollectionResult) {
				s.addAll((CollectionResult) e1res);
			} else {
				s.add(e1res);
			}
			
		}
		eres.addAll(s);
		treeVisitor.getQres().push(eres);
		return null;
	}
	
	@Override
	public Void visitSum(OperatorSum op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		QueryResult result;
		QueryResult objects = treeVisitor.getQres().pop();
		CollectionResult colRes = Utils.objectToCollection(objects);
		Number sum = null;
		for(QueryResult qr : colRes) {				
			Number n = (Number)Utils.toSimpleValue(qr, treeVisitor.getStore());
			sum = MathUtils.sum(sum, n);
		}
		result = javaObjectFactory.createJavaComplexObject(sum);
		treeVisitor.getQres().push(result);
		return null;
	}
	
	@Override
	public Void visitUnion(OperatorUnion op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		Bag eres = new Bag();
		CollectionResult eRight = Utils.objectToCollection(treeVisitor.getQres().pop());
		CollectionResult eLeft = Utils.objectToCollection(treeVisitor.getQres().pop());
		eres.addAll(eLeft);
		eres.addAll(eRight);		
		treeVisitor.getQres().push(eres);
		return null;
	}
	
	@Override
	public Void visitUnique(OperatorUnique op, Interpreter treeVisitor, Expression opExpr, Expression... subExprs) {
		QueryResult objects = treeVisitor.getQres().pop();
		objects = Utils.objectToCollection(objects);
		QueryResult result = null;
		if (objects instanceof Bag) {
			result = new Bag();
		} else if (objects instanceof Sequence) {
			result = new Sequence();
		}
		Set<QueryResult> tmp = new LinkedHashSet<QueryResult>();
		tmp.addAll((CollectionResult) objects);
		((CollectionResult) result).addAll(tmp);
		treeVisitor.getQres().push(result);
		return null;
	}
}

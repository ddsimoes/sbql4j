package pl.wcislo.sbql4j.lang.parser.expression;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.java.model.compiletime.BindExpression;
import pl.wcislo.sbql4j.java.model.compiletime.BindResult;
import pl.wcislo.sbql4j.java.model.compiletime.BinderSignature;
import pl.wcislo.sbql4j.java.model.compiletime.StaticEVNSType;
import pl.wcislo.sbql4j.lang.parser.terminals.Name;
import pl.wcislo.sbql4j.lang.tree.visitors.TreeVisitor;

public class NameExpression extends Expression implements BindExpression {
	public final Name l;
	
	/**
	 * Used when name is class name used to query all instances from db4o  
	 */
	public String fullName;
	/**
	 * may be > 1 if bound inside conditional signature
	 */
	private List<BindResult> bindResults = new ArrayList<BindResult>();
	public boolean boundGetterMethodAsIdentifier = false;
	
	private StaticEVNSType boundSignature;
	
	public NameExpression(int pos, Name l) {
		super(l.pos, ExpressionType.NAME.priority);
		this.l = l;
	}
	
	public NameExpression(Name l) {
		this(-1, l);
	}
	
//	@Override
//	public String toString() {
//		return l.val+"";
////		return this.getClass().getName()+" val="+(l == null ? "null" : l.val);
//	}

	@Override
	public void replaceSubExpression(Expression oldExpr, Expression newExpr) {
		
	}
	
	@Override
	public Object accept(TreeVisitor visitor, Object object) {
		return visitor.visitNameExpression(this, object);
	}
	
	@Override
	public StaticEVNSType getBoundSignature() {
		return boundSignature;
	}
	@Override
	public void setBoundSignature(StaticEVNSType b) {
		this.boundSignature = b;			
	}
	
	public void addBindResult(BindResult res) {
		this.bindResults.add(res);
	}
	
	public List<BindResult> getBindResults() {
		return bindResults;
	}
	
	public boolean isNestedFrom(Expression candidate) {
		if(this.bindResults != null && !this.bindResults.isEmpty() && this.bindResults.get(0).boundValueInfo != null && this.bindResults.get(0).boundValueInfo.nestedFrom != null) {
			return this.bindResults.get(0).boundValueInfo.nestedFrom.equals(candidate.getSignature());	
		} else {
			return false;
		}
		
	}
}

package pl.wcislo.sbql4j.java.model.compiletime;

import java.util.ArrayList;

import pl.wcislo.sbql4j.lang.parser.expression.NestingExpression;


/**
 * This class implements binder signatures, i.e. abstract binders processed by the type checker.
 *
 * @author Emil Wcislo
 */

public class BinderSignature extends Signature implements StaticEVNSType {
	public final String name;
	public final Signature value;
	public final boolean auxiliary;
	
	private NestedInfo nestedInfo;
	
	
	/**
	 * Initializes a binder mSignature.
	 * @param name  is the name held by the binder mSignature.
	 * @param value is the argument mSignature of the binder mSignature. 
	 */
	public BinderSignature(String name, Signature value, boolean auxiliary, ResultSource rs) {
		super(null, SCollectionType.NO_COLLECTION);
		this.name = name;
		this.value = value;
		this.auxiliary = auxiliary;
		this.resultSource = rs;
	}

	@Override
	public NestedInfo getNestedInfo() {
		return nestedInfo;
	}
	
	@Override
	public void setNestedInfo(NestedInfo nestedInfo) {
		this.nestedInfo = nestedInfo;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public BinderSignature clone() {
		BinderSignature bs = new BinderSignature(this.name, this.value.clone(), this.auxiliary, this.getResultSource());
		bs.setColType(this.getColType());
		bs.setResultName(this.getResultName());
		bs.setAssociatedExpression(this.getAssociatedExpression());
		return bs;
	}
	
	@Override
	public String toString() {
		return "<BinderSIG name="+name+" collectionType="+getColType()+" value="+value+">";
	}

	@Override
	protected void initNestedSigs(NestingExpression nestingExpression) {
		nestedSigs = new ArrayList<StaticEVNSType>();
		BinderSignature bs = new BinderSignature(this.name, this.value, this.auxiliary, this.getResultSource());
		bs.setColType(this.getColType());
		NestedInfo ni = new NestedInfo(this, null, nestingExpression);
		bs.setNestedInfo(ni);
		bs.setAssociatedExpression(this.getAssociatedExpression());
		nestedSigs.add(bs);
	}
	
	@Override
	public boolean isTypeCompatible(Signature sig) {
		Signature compareSig = value.clone();
		compareSig.setColType(this.getColType());
		return compareSig.isTypeCompatible(sig);
	}
	
	@Override
	public Signature getDerefSignature() {
		Signature res = value.getDerefSignature();
		return res;
	}
	
	@Override
	public Signature getDerefSignatureWithCardinality() {
		Signature res = value.getDerefSignatureWithCardinality().clone();
		res.setColType(SCollectionType.getStrongerSCTypeRight(this.getColType(), res.getColType()));
		return res;
	}
}

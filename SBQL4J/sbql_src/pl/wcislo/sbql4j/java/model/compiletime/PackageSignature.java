package pl.wcislo.sbql4j.java.model.compiletime;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.lang.parser.expression.NestingExpression;
import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.PackageSymbol;

public class PackageSignature extends Signature implements StaticEVNSType {

	public final PackageSymbol compilerSymbol;
	private NestedInfo nestedInfo;
	
	public PackageSignature(String name, PackageSymbol compilerSymbol) {
		super(name, SCollectionType.NO_COLLECTION);
		this.compilerSymbol = compilerSymbol;
	}
	@Override
	public PackageSignature clone() {
		PackageSignature sig = new PackageSignature(this.getTypeName(), compilerSymbol);
		sig.setAssociatedExpression(this.getAssociatedExpression());
		sig.setResultSource(this.getResultSource());
		return sig;
	}
	
	@Override
	protected void initNestedSigs(NestingExpression ne) {
		nestedSigs = new ArrayList();
		PackageSignature sig = this.clone();
		NestedInfo ni = new NestedInfo(this, null, ne);
		sig.setNestedInfo(ni);
		nestedSigs.add(sig);
	}
		
	@Override
	public String getName() {
		return getTypeName();
	}
	
	@Override
	public Signature getDerefSignature() {
		return this.clone();
	}
	
	@Override
	public Signature getDerefSignatureWithCardinality() {
		return this.clone();
	}
	@Override
	public NestedInfo getNestedInfo() {
		return nestedInfo;
	}
	
	@Override
	public void setNestedInfo(NestedInfo nestedInfo) {
		this.nestedInfo = nestedInfo;
	}
}

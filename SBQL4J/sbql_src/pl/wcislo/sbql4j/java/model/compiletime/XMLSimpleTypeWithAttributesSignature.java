package pl.wcislo.sbql4j.java.model.compiletime;

import java.util.ArrayList;
import java.util.List;

import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.NestingExpression;
import pl.wcislo.sbql4j.tools.javac.code.Type;

public class XMLSimpleTypeWithAttributesSignature extends ValueSignature {
	private ValueSignature value;
	private List<BinderSignature> attributes;
	public List<BinderSignature> getAttributes() {
		return attributes;
	}
	public XMLSimpleTypeWithAttributesSignature(ValueSignature value, List<BinderSignature> attributes) {
		super(value);
		this.value = value;
		this.attributes = attributes;
	}
	public ValueSignature getValue() {
		return value;
	}
	public int hashCode() {
		return value.hashCode();
	}
	public Type getType() {
		return value.getType();
	}
	public String toString() {
		return value.toString();
	}
	public ValueSignature clone() {
		ValueSignature vs = value.clone();
		XMLSimpleTypeWithAttributesSignature res =  new XMLSimpleTypeWithAttributesSignature(vs, new ArrayList<BinderSignature>(attributes));
		res.setResultSource(this.resultSource);
		return res;
	}
//	public boolean equals(Object obj) {
//		return value.equals(obj);
//	}
//	public boolean isTypeCompatible(Signature sig) {
//		return value.isTypeCompatible(sig);
//	}
//	public boolean isTypeNameCompatible(Signature sig) {
//		return value.isTypeNameCompatible(sig);
//	}
//	public boolean isTypeCompatible(Class javaType) {
//		return value.isTypeCompatible(javaType);
//	}
//	public String getJavaTypeString() {
//		return value.getJavaTypeString();
//	}
//	public boolean isTypeCompatible(Type javaCType) {
//		return value.isTypeCompatible(javaCType);
//	}
//	public String getJavaTypeEmptyString() {
//		return value.getJavaTypeEmptyString();
//	}
//	public Signature getDerefSignature() {
//		return value.getDerefSignature();
//	}
//	public Signature getDerefSignatureWithCardinality() {
//		return value.getDerefSignatureWithCardinality();
//	}
//	public void setArrayType(boolean isArrayType) {
//		value.setArrayType(isArrayType);
//	}
//	public boolean isArrayType() {
//		return value.isArrayType();
//	}
//	public void setPrimitiveType(boolean primitiveType) {
//		value.setPrimitiveType(primitiveType);
//	}
//	public String getJavaTypeStringNoDeref() {
//		return value.getJavaTypeStringNoDeref();
//	}
//	public boolean isPrimitiveType() {
//		return value.isPrimitiveType();
//	}
//	public String getJavaTypeStringAssigment() {
//		return value.getJavaTypeStringAssigment();
//	}
//	public String getJavaTypeStringSingleResult() {
//		return value.getJavaTypeStringSingleResult();
//	}
//	public void setResultName(String resultName) {
//		value.setResultName(resultName);
//	}
//	public String getResultName() {
//		return value.getResultName();
//	}
//	public String genJavaDeclarationCode() {
//		return value.genJavaDeclarationCode();
//	}
//	public String genSBQLDeclarationCode() {
//		return value.genSBQLDeclarationCode();
//	}
//	public boolean isAuixiliaryCollection() {
//		return value.isAuixiliaryCollection();
//	}
//	public void setAuixiliaryCollection(boolean auixiliaryCollection) {
//		value.setAuixiliaryCollection(auixiliaryCollection);
//	}
//	public boolean isCollectionResult() {
//		return value.isCollectionResult();
//	}
//	public Expression getAssociatedExpression() {
//		return value.getAssociatedExpression();
//	}
//	public void setAssociatedExpression(Expression associatedExpression) {
//		value.setAssociatedExpression(associatedExpression);
//	}
//	public void setResultSource(ResultSource resultSource) {
//		value.setResultSource(resultSource);
//	}
//	public ResultSource getResultSource() {
//		return value.getResultSource();
//	}
//	public boolean isPassedAsParameterToDiffContext() {
//		return value.isPassedAsParameterToDiffContext();
//	}
//	public void setPassedAsParameterToDiffContext(
//			boolean isPassedAsParameterToDiffContext) {
//		value.setPassedAsParameterToDiffContext(isPassedAsParameterToDiffContext);
//	}

	@Override
	protected void initNestedSigs(NestingExpression nestingExpression) {
		value.initNestedSigs(nestingExpression);
		for(int i=0; i<attributes.size(); i++) {
			BinderSignature bs = attributes.get(i);
			BinderSignature bsClone = bs.clone();
			bsClone.setAssociatedExpression(value.getAssociatedExpression());
			NestedInfo ni = new NestedInfo(this, i, nestingExpression);
			bsClone.setNestedInfo(ni);
			value.nestedSigs.add(bsClone);
		}
		this.nestedSigs = value.nestedSigs;
	}
	public String getTypeName() {
		return value.getTypeName();
	}
	public void setColType(SCollectionType sColType) {
		value.setColType(sColType);
	}
	public SCollectionType getColType() {
		return value.getColType();
	}

//	@Override
//	public List<StaticEVNSType> nested(NestingExpression ne) {
//		List<StaticEVNSType> res = value.nested(ne);
//		return res;
//	}
	
	
}

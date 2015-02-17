/**
 * 
 */
package pl.wcislo.sbql4j.java.model.compiletime;

import pl.wcislo.sbql4j.java.model.compiletime.Signature.ResultSource;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.ExpressionType;
import pl.wcislo.sbql4j.lang.parser.expression.NameExpression;
import pl.wcislo.sbql4j.lang.xml.signature.XMLDataSourceSignature;

public class BindResult {
	public final String boundName;
	public final Signature boundValue;
	public final NestedInfo boundValueInfo;
	public final StaticEVNSType binder;
	public final int envsStackLevel;
	
	public BindResult(String boundName, Signature boundValue, NestedInfo boundValueInfo, 
			StaticEVNSType binder, int envsStackLevel) {
		super();
		this.boundName = boundName;
		this.boundValue = boundValue;
		this.boundValueInfo = boundValueInfo;
		this.binder = binder;
		this.envsStackLevel = envsStackLevel;
	}
	
	public String genJavaCode(ResultSource dist) {
		if(boundValueInfo != null) {
			if(binder instanceof BinderSignature 
					&& (((BinderSignature) binder).auxiliary || ((BinderSignature) binder).resultSource == ResultSource.XML)
//					&& !( boundValue instanceof MethodSignature)
				) {
				BinderSignature bs = (BinderSignature) binder;
				if("$index".equals(boundName)) {
					String res = boundValueInfo.nestingExpression.getNestedLoopVarName();
					return res;
				} else if(bs.getResultSource() != dist && bs.isPassedAsParameterToDiffContext()) {
					String res = bs.getAssociatedExpression().getSignature().getResultName();
					return res;
				} else if(boundValueInfo.nestedFrom instanceof StructSignature || boundValueInfo.nestedFrom instanceof XMLSimpleTypeWithAttributesSignature) {
//					return "("+bs.getJavaTypeString()+")"+boundValueInfo.nestingExpression.getNestedVarName()+".getValue("+boundValueInfo.structSignatureIndex+")";
					return "("+bs.getJavaTypeString()+")"+boundValueInfo.nestingExpression.getNestedVarName()+".get(\""+boundName+"\")";
//			 	} else if(boundValue.sColType != bs.sColType) {
//			 		return boundValueInfo.nestingExpression.getNestedVarName();
//			 	} else if(boundValue.sColType == bs.sColType) {
			 		
			 	} else {
			 		return boundValueInfo.nestingExpression.getNestedVarName();
//			 		throw new RuntimeException("cannot generate java code for bound name: "+bs.name);
			 	}
//				 		
//				 		
////				 		Expression e = boundValueInfo.nestingExpression.getExpression();
////				 		if(boundValue.resultSource == e.getSignature().resultSource) {
////				 			//we use name of origin variable (not new auxiliary name)
////				 			return boundValueInfo.nestingExpression.getNestedVarName();
////				 		} else {
////				 			//we use auxiliary name
////				 			return boundValueInfo.nestedFrom.getAssociatedExpression().getSignature().getResultName();
////				 		}
//				 		
//				 		
//				 		return bs.getAssociatedExpression().getSignature().getResultName();
//				 	}
//					
//				}
				
				
//			} else if(binder instanceof MethodSignature) {
//				MethodSignature mSig = (MethodSignature) binder;
//				
			} else if(boundValue instanceof MethodSignature) {
				MethodSignature mSig = (MethodSignature) boundValue;
				StringBuilder sb = new StringBuilder();
				if(boundValueInfo.nestedFrom instanceof StructSignature || boundValueInfo.nestedFrom instanceof XMLSimpleTypeWithAttributesSignature) {
					sb.append("("+mSig.getJavaTypeString()+")"+boundValueInfo.nestingExpression.getNestedVarName()+".getValue("+boundValueInfo.structSignatureIndex+")");
			 	} else {
			 		sb.append(boundValueInfo.nestingExpression.getNestedVarName());
			 	}
//				sb.append(boundValueInfo.nestingExpression.nestedVarName);
				sb.append(".");
				sb.append(mSig.method.name.toString());
//				sb.append("(");
//				if(mSig.paramsSig != null) {
//					for(int i=0; i<mSig.paramsSig.fieldsNumber(); i++) { 
//						sb.append(mSig.paramsSig.getFields()[i].getResultName());
//						if(i < mSig.paramsSig.fieldsNumber()-1) {
//							sb.append(", ");
//						}
//					}
//				}
//				sb.append(")");
				return sb.toString();
//			} else if(boundValue instanceof XMLDataSourceSignature){
//				return 
			} else {
				//value is nested from other value
				String leftDot;
				String leftDotEmptyValue = boundValue.getJavaTypeEmptyString();
				if(boundValueInfo.nestedFrom instanceof StructSignature) {
					StructSignature nestedFrom = (StructSignature) boundValueInfo.nestedFrom;
					Signature structSNestedFrom = nestedFrom.getFields()[boundValueInfo.structSignatureIndex];
					leftDot = "(("+structSNestedFrom.getJavaTypeString()+")"+boundValueInfo.nestingExpression.getNestedVarName()+".getValue("+boundValueInfo.structSignatureIndex+"))";
//					String res = "("+boundValueInfo.nestingExpression.getNestedVarName()+" == null ? "+leftDotEmptyValue+" : "+leftDot+")";
//					return res;
				} else if(boundValueInfo.nestedFrom instanceof XMLSimpleTypeWithAttributesSignature){
					XMLSimpleTypeWithAttributesSignature sig = (XMLSimpleTypeWithAttributesSignature) boundValueInfo.nestedFrom;
					Signature structSNestedFrom = sig.getAttributes().get(boundValueInfo.structSignatureIndex);
					leftDot = "(("+structSNestedFrom.getJavaTypeString()+")"+boundValueInfo.nestingExpression.getNestedVarName()+".getValue("+boundValueInfo.structSignatureIndex+"))";
//					String res = "("+boundValueInfo.nestingExpression.getNestedVarName()+" == null ? "+leftDotEmptyValue+" : "+leftDot+")";
				} else {
					leftDot = boundValueInfo.nestingExpression.getNestedVarName();
				}
//				return leftDot+"."+boundName;
				String res = "("+leftDot+" == null ? "+leftDotEmptyValue+" : "+leftDot+"."+boundName+")";
				return res;
			}
		} else if(boundValue instanceof XMLDataSourceSignature) {
			return boundName+".getParsedData()";
		} else {
			//value is on bottom on ENVS stack, so it must be generated class member (query param)
//			return "this."+boundName;
			return boundName;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((boundName == null) ? 0 : boundName.hashCode());
		result = prime * result + envsStackLevel;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BindResult other = (BindResult) obj;
		if (boundName == null) {
			if (other.boundName != null)
				return false;
		} else if (!boundName.equals(other.boundName))
			return false;
		if (envsStackLevel != other.envsStackLevel)
			return false;
		return true;
	}
	
	
}

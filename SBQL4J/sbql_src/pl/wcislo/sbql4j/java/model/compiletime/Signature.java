package pl.wcislo.sbql4j.java.model.compiletime;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.wcislo.sbql4j.lang.parser.expression.Expression;
import pl.wcislo.sbql4j.lang.parser.expression.NestingExpression;

public abstract class Signature implements Cloneable { 
//	public enum SType {
//		VAR,
//		CLASS,
//		METHOD,
//		PACKAGE;
//	}
	public enum ResultSource {
		JAVA_HEAP,
		DB4O,
		XML;
	}
	
	public enum SCollectionType {
		
		NO_COLLECTION,
		BAG,
		SEQUENCE;
		
		private static final Log log = LogFactory.getLog(SCollectionType.class);
		public static SCollectionType getStrongerSCType(SCollectionType t1, SCollectionType t2) {
			if(t1 == null || t2 == null) {
				log.warn("got null as value: t1="+t1+", t2="+t2);
			}
			if(t1 == SEQUENCE || t2 == SEQUENCE) {
				return SEQUENCE;
			} else if(t1 == BAG || t2 == BAG) {
				return BAG;
			} else {
				return NO_COLLECTION;
			}
		}
		/**
		 * Only right type can be stronger
		 * @param t1
		 * @param t2
		 * @return
		 */
		public static SCollectionType getStrongerSCTypeRight(SCollectionType t1, SCollectionType t2) {
			if(t1 == null || t2 == null) {
				log.warn("got null as value: t1="+t1+", t2="+t2);
			}
			if(t1 == SEQUENCE || t2 == SEQUENCE) {
				return SEQUENCE;
			} else if(t2 == BAG) {
				return BAG;
			} else {
				return t1;
			}
		}
		public static SCollectionType getWorseSCType(SCollectionType t1, SCollectionType t2) {
			if(t1 == null || t2 == null) {
				log.warn("got null as value: t1="+t1+", t2="+t2);
			}
			if(t1 == NO_COLLECTION || t2 == NO_COLLECTION) {
				return NO_COLLECTION;
			} else if(t1 == BAG || t2 == BAG) {
				return BAG;
			} else {
				return SEQUENCE;
			}
		}
		public static SCollectionType getUnionSCType(SCollectionType t1, SCollectionType t2) {
			if(t1 == null || t2 == null) {
				log.warn("got null as value: t1="+t1+", t2="+t2);
			}
			if(t1 == NO_COLLECTION || t2 == NO_COLLECTION) {
				return BAG;
			} else if(t1 == BAG || t2 == BAG) {
				return BAG;
			} else {
				return SEQUENCE;
			}
		}
		
		public String genSBQLDeclCode() {
			if(this == NO_COLLECTION) {
				return "QueryResult";
			} else if(this == BAG) {
				return "Bag";
			} else if(this == SEQUENCE) {
				return "Sequence<List<QueryResult>>";
			}
			else return null;
		}
		
		public String genCommonDeclCode() {
			if(this == NO_COLLECTION) {
				return "QueryResult";
			} else {
				return "CollectionResult";
			}
		}
	}
	
//	public final String name;
	private final String typeName;
//	public final SType stype;
	private SCollectionType sColType;
	protected ResultSource resultSource = ResultSource.JAVA_HEAP;
	private Expression associatedExpression;
	
//	public Signature nestedFrom;
//	public BinderSignature boundFrom;
	
	/**
	 * given name of variable
	 */
	private String resultName;
	
	public Signature(String typeName, SCollectionType sColType) {
		super();
//		this.name = name;
		this.typeName = typeName;
//		this.stype = stype;
		this.sColType = sColType;
//		this.resultSource = ResultSource.JAVA_HEAP;
	}
	
	@Override
	public abstract Signature clone();
	
	public abstract Signature getDerefSignature();
	public abstract Signature getDerefSignatureWithCardinality();
	
	
	private boolean isNestedSigInit = false;
	private boolean auixiliaryCollection;
	//true if eg. is parameter for db4o from java heap
	private boolean isPassedAsParameterToDiffContext;
	protected abstract void initNestedSigs(NestingExpression ne);
	
	protected List<StaticEVNSType> nestedSigs;
	
	/**
	 * 
	 * @param ne Expression in which nesting is performed
	 * @return
	 */
	public List<StaticEVNSType> nested(NestingExpression ne) {
		if(!isNestedSigInit) {
			initNestedSigs(ne);
			isNestedSigInit = true;
		}
		for(StaticEVNSType s : nestedSigs) {
			if(s instanceof Signature) {
				((Signature) s).setResultSource(this.getResultSource());
			}
		}
		return nestedSigs;
	}
	
	public boolean isTypeCompatible(Signature sig) {
		return isTypeNameCompatible(sig);
	}
	public boolean isTypeNameCompatible(Signature sig) {
		if (getTypeName() != null && sig.getTypeName() != null )
			return getTypeName().equals(sig.getTypeName());
		if (getTypeName() == null && sig.getTypeName() == null )
			return true;

		return false;
	}

	public static ValueSignature castToValueSignature(Signature s) {
		if(s instanceof ValueSignature) {
			return (ValueSignature)s;
		} else if(s instanceof BinderSignature) {
			BinderSignature bs = (BinderSignature)s;
			return (castToValueSignature(bs.value));
		} else {
			throw new RuntimeException("cannot get ValueSignature from "+s);
		}
	}
	
	
	public String getJavaTypeString() {
		Signature sig = getDerefSignatureWithCardinality();
    	StringBuilder sb = new StringBuilder();
    	if(sig.getColType() == SCollectionType.BAG) {
    		sb.append("java.util.Collection");
    	} else if(sig.getColType() == SCollectionType.SEQUENCE) {
    		sb.append("java.util.List");
    	}
    	if(sig.getColType() != SCollectionType.NO_COLLECTION) {
    		sb.append("<");
    	}
    	sb.append(getJavaTypeStringSingleResult());
    	if(sig.getColType() != SCollectionType.NO_COLLECTION) {
    		sb.append(">");
    	}
    	return sb.toString();
	}
	
	public String getJavaTypeString(boolean derefXML) { 
		if(!derefXML) {
			return getJavaTypeString();
		} else {
			Signature sig = getDerefSignatureWithCardinality();
	    	StringBuilder sb = new StringBuilder();
	    	if(sig.getColType() == SCollectionType.BAG) {
	    		sb.append("java.util.Collection");
	    	} else if(sig.getColType() == SCollectionType.SEQUENCE) {
	    		sb.append("java.util.List");
	    	}
	    	if(sig.getColType() != SCollectionType.NO_COLLECTION) {
	    		sb.append("<");
	    	}
	    	sb.append(getJavaTypeStringSingleResult(derefXML));
	    	if(sig.getColType() != SCollectionType.NO_COLLECTION) {
	    		sb.append(">");
	    	}
	    	return sb.toString();	
		}
	}
	
	/**
	 * for null or empty collection value
	 * @return
	 */
	public String getJavaTypeEmptyString() {
		Signature sig = getDerefSignatureWithCardinality();
    	StringBuilder sb = new StringBuilder();
//    	sb.append("(");
    	if(sig.getColType() != SCollectionType.NO_COLLECTION) {
    		sb.append("new java.util.ArrayList");
    		sb.append("<");
    		sb.append(getJavaTypeStringSingleResult());
    		sb.append(">()");
    	} else {
    		sb.append("null");
    	}
    	return sb.toString();
	}
	
	public String getJavaTypeStringNoDeref() {
		Signature sig = this;
    	StringBuilder sb = new StringBuilder();
//    	sb.append("(");
    	if(sig.getColType() == SCollectionType.BAG) {
    		sb.append("java.util.Collection");
    	} else if(sig.getColType() == SCollectionType.SEQUENCE) {
    		sb.append("java.util.List");
    	}
    	if(sig.getColType() != SCollectionType.NO_COLLECTION) {
    		sb.append("<");
    	}
    	sb.append(getJavaTypeStringSingleResult());
    	if(sig.getColType() != SCollectionType.NO_COLLECTION) {
    		sb.append(">");
    	}
//    	sb.append(")");
    	return sb.toString();
	}
	
	
	public String getJavaTypeStringAssigment() {
		Signature sig = getDerefSignatureWithCardinality();
    	StringBuilder sb = new StringBuilder();
    	if(isCollectionResult()) {
    		sb.append("java.util.ArrayList");
    		sb.append("<");
    		sb.append(getJavaTypeStringSingleResult());
    		sb.append(">");
    	}
    	return sb.toString();
	}
	
	public String getJavaTypeStringSingleResult() {
		StringBuilder sb = new StringBuilder();
		Signature sig = getDerefSignature();
		
		if(sig instanceof StructSignature || sig instanceof XMLSimpleTypeWithAttributesSignature) {
//			StructSignature st = (StructSignature) sig;
			sb.append("pl.wcislo.sbql4j.java.model.runtime.Struct");
		} else if(sig instanceof ValueSignature) {
//			sb.append(((ValueSignature)sig).type.tsym.name.toString());
    		sb.append(((ValueSignature)sig).getType().tsym.toString());
    	} else if(sig instanceof VariantSignature) {
    		Signature s = ((VariantSignature)sig).getFields()[0];
    		sb.append(s.getJavaTypeStringSingleResult());
    	}
		return sb.toString();
	}
	
	public String getJavaTypeStringSingleResult(boolean derefXML) {
		StringBuilder sb = new StringBuilder();
		Signature sig = getDerefSignature();
		
		if(sig instanceof StructSignature) {
//			StructSignature st = (StructSignature) sig;
			sb.append("pl.wcislo.sbql4j.java.model.runtime.Struct");
		} else if(sig instanceof ValueSignature) {
//			sb.append(((ValueSignature)sig).type.tsym.name.toString());
    		sb.append(((ValueSignature)sig).getType().tsym.toString());
    	} else if(sig instanceof VariantSignature) {
    		Signature s = ((VariantSignature)sig).getFields()[0];
    		sb.append(s.getJavaTypeStringSingleResult());
    	}
		return sb.toString();
	}
	
	public void setResultName(String resultName) {
		this.resultName = resultName;
	}
	public String getResultName() {
		return resultName;
	}
	
	public String genJavaDeclarationCode() {
		return getJavaTypeString()+" "+resultName;
	}
	
	public String genSBQLDeclarationCode() {
		
//		return sColType.genCommonDeclCode()+" "+resultName;
		return getDerefSignatureWithCardinality().getColType().genCommonDeclCode()+" "+resultName;
	}

	public boolean isAuixiliaryCollection() {
		return auixiliaryCollection;
	}

	public void setAuixiliaryCollection(boolean auixiliaryCollection) {
		this.auixiliaryCollection = auixiliaryCollection;
	}
	public boolean isCollectionResult() {
		return this.getColType() != SCollectionType.NO_COLLECTION || isAuixiliaryCollection();
	}
	public Expression getAssociatedExpression() {
		return associatedExpression;
	}
	public void setAssociatedExpression(Expression associatedExpression) {
		this.associatedExpression = associatedExpression;
	}

	public void setResultSource(ResultSource resultSource) {
		this.resultSource = resultSource;
	}

	public ResultSource getResultSource() {
		return resultSource;
	}
	
	public boolean isPassedAsParameterToDiffContext() {
		return isPassedAsParameterToDiffContext;
	}
	public void setPassedAsParameterToDiffContext(
			boolean isPassedAsParameterToDiffContext) {
		this.isPassedAsParameterToDiffContext = isPassedAsParameterToDiffContext;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setColType(SCollectionType sColType) {
		this.sColType = sColType;
	}

	public SCollectionType getColType() {
		return sColType;
	}
}

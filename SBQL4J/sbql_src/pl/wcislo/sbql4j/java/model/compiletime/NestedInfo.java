package pl.wcislo.sbql4j.java.model.compiletime;

import pl.wcislo.sbql4j.lang.parser.expression.NestingExpression;

/**
 * @author Emil Wcislo
 *
 *	Contains informations about nesting operation
 *
 */
public class NestedInfo {
	/**
	 * A signature, that provides nested objects
	 */
	public final Signature nestedFrom;
	
	/**
	 * If object was nested from StructSignature this field points 
	 * which field of it provided nested objects
	 */
	public final Integer structSignatureIndex;
	
	/**
	 * An Expression in which nested operation is invoked
	 */
	public final NestingExpression nestingExpression;

	public NestedInfo(Signature nestedFrom, 
					Integer structSignatureIndex, NestingExpression nestingExpression) {
		super();
		this.nestedFrom = nestedFrom;
		this.structSignatureIndex = structSignatureIndex;
		this.nestingExpression = nestingExpression;
	}
}

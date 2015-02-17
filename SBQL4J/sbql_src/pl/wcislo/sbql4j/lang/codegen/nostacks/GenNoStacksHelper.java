package pl.wcislo.sbql4j.lang.codegen.nostacks;

import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.ValueSignature;

public class GenNoStacksHelper {
	public static String genEmptyValueCheckBinaryOperator(Signature leftSig, Signature rightSig,
			String resEmptyLeft, String resEmptyRight, String resEmptyBoth) {
		StringBuilder sb = new StringBuilder();
		boolean isLeftPrimitiveType =  leftSig instanceof ValueSignature && ((ValueSignature)leftSig).isPrimitiveType();
		boolean isRightPrimitiveType =  rightSig instanceof ValueSignature && ((ValueSignature)rightSig).isPrimitiveType();
		String identLeftRes = leftSig.getResultName();
		String identRightRes = leftSig.getResultName();
		if(isLeftPrimitiveType && isRightPrimitiveType) {
			return "";
		}
		if(!isLeftPrimitiveType && isRightPrimitiveType) {
			//sprawdzamy tylko lewy
			sb.append(identLeftRes+"==null ? "+resEmptyLeft+" : ");
		}
		if(isLeftPrimitiveType && !isRightPrimitiveType) {
			//sprawdzamy tylko prawy
			sb.append(identRightRes+"==null ? "+resEmptyRight+" : ");
		}
		if(!isLeftPrimitiveType && !isRightPrimitiveType) {
			sb.append(identLeftRes+"==null ? (" 
					+ identRightRes + "==null ? "+resEmptyBoth+" : "+resEmptyLeft+") : "
					+ identRightRes + "==null ? "+resEmptyRight+" : ");
		}
		return sb.toString();
	}
}

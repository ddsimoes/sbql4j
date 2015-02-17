package pl.wcislo.sbql4j.lang.parser.terminals.operators;

public class MathUtils {
	public static Number min(Number n1, Number n2) {
		Number res = null;
		if(n1 == null) {
			return n2;
		}
		if(n2 == null) {
			return n1;
		}
		Double min = Math.min(n1.doubleValue(), n2.doubleValue());
		res = castToProperNumberType(min, n1, n2);
		return res;
	}
	
	public static Number max(Number n1, Number n2) {
		Number res = null;
		if(n1 == null) {
			return n2;
		}
		if(n2 == null) {
			return n1;
		}
		Double max = Math.max(n1.doubleValue(), n2.doubleValue());
		res = castToProperNumberType(max, n1, n2);
		return res;
	}
	
	public static Number sum(Number n1, Number n2) {
		Number res = null;
		if(n1 == null) {
			return n2;
		}
		if(n2 == null) {
			return n1;
		}
		Double sum = n1.doubleValue() + n2.doubleValue();
		res = castToProperNumberType(sum, n1, n2);
		return res;
	}
	
	public static Number subtract(Number n1, Number n2) {
		Number res = null;
		if(n1 == null) {
			return n2;
		}
		if(n2 == null) {
			return n1;
		}
		Double sum = n1.doubleValue() - n2.doubleValue();
		res = castToProperNumberType(sum, n1, n2);
		return res;
	}
	
	public static Number divide(Number n1, Number n2) {
		Number res = null;
		if(n1 == null) {
			return n2;
		}
		if(n2 == null) {
			return n1;
		}
		Double sum = n1.doubleValue() / n2.doubleValue();
		res = castToProperNumberType(sum, n1, n2);
		return res;
	}
	
	public static Number multiply(Number n1, Number n2) {
		Number res = null;
		if(n1 == null) {
			return n2;
		}
		if(n2 == null) {
			return n1;
		}
		Double sum = n1.doubleValue() * n2.doubleValue();
		res = castToProperNumberType(sum, n1, n2);
		return res;
	}
	
	public static Number modulo(Number n1, Number n2) {
		Number res = null;
		if(n1 == null) {
			return n2;
		}
		if(n2 == null) {
			return n1;
		}
		Double sum = n1.doubleValue() % n2.doubleValue();
		res = castToProperNumberType(sum, n1, n2);
		return res;
	}
	
	public static int compareSafe(Comparable c1, Comparable c2) {
		boolean c1Null = c1 == null;
		boolean c2Null = c2 == null;
		if(c1Null && c2Null) {
			return 0;
		} else if(c1Null && !c2Null) {
			return -1;
		} else if(!c1Null && c2Null) {
			return 1;
		} else {
			return c1.compareTo(c2);
		}
	}
	
	public static Number castToProperNumberType(Double val, Number n1, Number n2) {
		Number res = null;
		if(n1 instanceof Double || n2 instanceof Double) {
			res = val;
		} else if(n1 instanceof Float || n2 instanceof Float) {
			res = val.floatValue();
		} else if(n1 instanceof Long || n2 instanceof Long) {
			res = val.longValue();
		} else if(n1 instanceof Integer || n2 instanceof Integer) {
			res = val.intValue();
		} else if(n1 instanceof Short || n2 instanceof Short) {
			res = val.intValue();
		} else if(n1 instanceof Byte || n2 instanceof Byte) {
			res = val.byteValue();
		}
		return res;
	}
	
}

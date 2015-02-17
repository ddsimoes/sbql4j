package pl.wcislo.sbql4j.lang.xml.typeparser;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.DecimalDV;
import org.apache.xerces.xs.datatypes.XSDecimal;

public class LongParser extends DecimalDV {
	@Override
	public Long getActualValue(String content, ValidationContext context)
			throws InvalidDatatypeValueException {
		Object val = super.getActualValue(content, context);
		XSDecimal valDec = (XSDecimal)val;
		return valDec.getLong();
	}
}

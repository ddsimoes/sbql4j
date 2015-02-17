package pl.wcislo.sbql4j.lang.xml.typeparser;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.FloatDV;
import org.apache.xerces.xs.datatypes.XSFloat;

public class FloatParser extends FloatDV {
	@Override
	public Float getActualValue(String content, ValidationContext context)
			throws InvalidDatatypeValueException {
		Object val = super.getActualValue(content, context);
		XSFloat valDec = (XSFloat) val;
		return valDec.getValue();
	}
}

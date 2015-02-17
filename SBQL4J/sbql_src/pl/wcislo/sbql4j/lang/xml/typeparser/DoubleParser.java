package pl.wcislo.sbql4j.lang.xml.typeparser;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.impl.dv.xs.DoubleDV;
import org.apache.xerces.xs.datatypes.XSDouble;

public class DoubleParser extends DoubleDV {
	@Override
	public Double getActualValue(String content, ValidationContext context)
			throws InvalidDatatypeValueException {
		Object val = super.getActualValue(content, context);
		XSDouble valDec = (XSDouble) val;
		return valDec.getValue();
	}
}

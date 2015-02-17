package pl.wcislo.sbql4j.lang.xml;

import java.util.TreeMap;

import org.apache.xerces.impl.dv.xs.AnySimpleDV;
import org.apache.xerces.impl.dv.xs.AnyURIDV;
import org.apache.xerces.impl.dv.xs.Base64BinaryDV;
import org.apache.xerces.impl.dv.xs.BooleanDV;
import org.apache.xerces.impl.dv.xs.DateDV;
import org.apache.xerces.impl.dv.xs.DateTimeDV;
import org.apache.xerces.impl.dv.xs.DayDV;
import org.apache.xerces.impl.dv.xs.DecimalDV;
import org.apache.xerces.impl.dv.xs.DoubleDV;
import org.apache.xerces.impl.dv.xs.DurationDV;
import org.apache.xerces.impl.dv.xs.EntityDV;
import org.apache.xerces.impl.dv.xs.FloatDV;
import org.apache.xerces.impl.dv.xs.HexBinaryDV;
import org.apache.xerces.impl.dv.xs.IDDV;
import org.apache.xerces.impl.dv.xs.IDREFDV;
import org.apache.xerces.impl.dv.xs.IntegerDV;
import org.apache.xerces.impl.dv.xs.ListDV;
import org.apache.xerces.impl.dv.xs.MonthDV;
import org.apache.xerces.impl.dv.xs.MonthDayDV;
import org.apache.xerces.impl.dv.xs.QNameDV;
import org.apache.xerces.impl.dv.xs.StringDV;
import org.apache.xerces.impl.dv.xs.TimeDV;
import org.apache.xerces.impl.dv.xs.TypeValidator;
import org.apache.xerces.impl.dv.xs.UnionDV;
import org.apache.xerces.impl.dv.xs.YearDV;
import org.apache.xerces.impl.dv.xs.YearMonthDV;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.datatypes.XSDateTime;

import pl.wcislo.sbql4j.lang.xml.typeparser.DoubleParser;
import pl.wcislo.sbql4j.lang.xml.typeparser.FloatParser;
import pl.wcislo.sbql4j.lang.xml.typeparser.IntegerParser;

import com.sun.org.apache.xerces.internal.impl.dv.util.ByteListImpl;

public class XMLTypes {
	
	/** "any" type */
	public static final short ANYSIMPLETYPE = 0;
    /** "string" type */
    public static final short PRIMITIVE_STRING        = 1;
    /** "boolean" type */
    public static final short PRIMITIVE_BOOLEAN       = 2;
    /** "decimal" type */
    public static final short PRIMITIVE_DECIMAL       = 3;
    /** "float" type */
    public static final short PRIMITIVE_FLOAT         = 4;
    /** "double" type */
    public static final short PRIMITIVE_DOUBLE        = 5;
    /** "duration" type */
    public static final short PRIMITIVE_DURATION      = 6;
    /** "dataTime" type */
    public static final short PRIMITIVE_DATETIME      = 7;
    /** "time" type */
    public static final short PRIMITIVE_TIME          = 8;
    /** "date" type */
    public static final short PRIMITIVE_DATE          = 9;
    /** "gYearMonth" type */
    public static final short PRIMITIVE_GYEARMONTH    = 10;
    /** "gYear" type */
    public static final short PRIMITIVE_GYEAR         = 11;
    /** "gMonthDay" type */
    public static final short PRIMITIVE_GMONTHDAY     = 12;
    /** "gDay" type */
    public static final short PRIMITIVE_GDAY          = 13;
    /** "gMonth" type */
    public static final short PRIMITIVE_GMONTH        = 14;
    /** "hexBinary" type */
    public static final short PRIMITIVE_HEXBINARY     = 15;
    /** "base64Binary" type */
    public static final short PRIMITIVE_BASE64BINARY  = 16;
    /** "anyURI" type */
    public static final short PRIMITIVE_ANYURI        = 17;
    /** "QName" type */
    public static final short PRIMITIVE_QNAME         = 18;
    /** "precisionDecimal" type */
    public static final short PRIMITIVE_PRECISIONDECIMAL = 19;
    /** "NOTATION" type */
    public static final short PRIMITIVE_NOTATION      = 20;
    /** "ID" type */
    public static final short ID = 21;
    /** "IDREF" type */
    public static final short IDREF = 22;
    /** "entity" type */
    public static final short ENTITY = 23;
    /** "integer" type */
    public static final short INTEGER = 24;
    /** "list" type */
    public static final short LIST = 25;
    /** "union" type */
    public static final short UNION = 26;
    /** "yearMonthDuration" type */
    public static final short YEARMONTHDURATION = 27;
    /** "dayTimeDuration" type */
    public static final short DAYTIMEDURATION = 28;
    /** "anyAtomicType" type */
    public static final short ANYATOMICTYPE = 29;
    
    public static short getTypeCodeForTypeName(String typeName) {
    	if("string".equals(typeName)) {
//        	return PRIMITIVE_STRING;
    		return -1; //string is default
    	} else if("boolean".equals(typeName)) {
    		return PRIMITIVE_BOOLEAN;
    	} else if("decimal".equals(typeName)) {
    		return PRIMITIVE_DECIMAL;
    	} else if("float".equals(typeName)) {
    		return PRIMITIVE_FLOAT;
    	} else if("double".equals(typeName)) {
    		return PRIMITIVE_DOUBLE;
    	} else if("duration".equals(typeName)) {
    		return PRIMITIVE_DURATION;
    	} else if("dataTime".equals(typeName)) {
    		return PRIMITIVE_DATETIME;
    	} else if("time".equals(typeName)) {
    		return PRIMITIVE_TIME;
    	} else if("date".equals(typeName)) {
    		return PRIMITIVE_DATE;
    	} else if("gYearMonth".equals(typeName)) {
    		return PRIMITIVE_GYEARMONTH;
    	} else if("gYear".equals(typeName)) {
    		return PRIMITIVE_GYEAR;
    	} else if("gMonthDay".equals(typeName)) {
    		return PRIMITIVE_GMONTHDAY;
    	} else if("gDay".equals(typeName)) {
    		return PRIMITIVE_GDAY;
    	} else if("gMonth".equals(typeName)) {
    		return PRIMITIVE_GMONTH;
    	} else if("hexBinary".equals(typeName)) {
    		return PRIMITIVE_HEXBINARY;
    	} else if("base64Binary".equals(typeName)) {
    		return PRIMITIVE_BASE64BINARY;
    	} else if("anyURI".equals(typeName)) {
    		return PRIMITIVE_ANYURI;
    	} else if("QName".equals(typeName)) {
    		return PRIMITIVE_QNAME;
    	} else if("precisionDecimal".equals(typeName)) {
    		return PRIMITIVE_PRECISIONDECIMAL;
    	} else if("NOTATION".equals(typeName)) {
    		return PRIMITIVE_NOTATION;
    	} else if("ID".equals(typeName)) {
    		return ID;
    	} else if("IDREF".equals(typeName)) {
    		return IDREF;
    	} else if("entity".equals(typeName)) {
    		return ENTITY;
    	} else if("integer".equals(typeName)) {
    		return INTEGER;
    	} else if("list".equals(typeName)) {
    		return LIST;
    	} else if("union".equals(typeName)) {
    		return UNION;
    	} else if("yearMonthDuration".equals(typeName)) {
    		return YEARMONTHDURATION;
    	} else if("dayTimeDuration".equals(typeName)) {
    		return DAYTIMEDURATION;
    	} else if("yearMonthDuration".equals(typeName)) {
    		return YEARMONTHDURATION;
    	} else if("dayTimeDuration".equals(typeName)) {
    		return DAYTIMEDURATION;
    	} else if("anyAtomicType".equals(typeName)) {
    		return ANYATOMICTYPE;
    	} else {
    		throw new RuntimeException("type '"+typeName+"' not supported");
    	}
    }
    
    private static final TypeValidator[] typeValidators = {
        new AnySimpleDV(),
        new StringDV(),
//        null,
        new BooleanDV(),
        new DoubleParser(),
        new FloatParser(),
        new DoubleParser(),
        new DurationDV(),
        new DateTimeDV(),
        new TimeDV(),
        new DateDV(),
        new YearMonthDV(),
        new YearDV(),
        new MonthDayDV(),
        new DayDV(),
        new MonthDV(),
        new HexBinaryDV(),
        new Base64BinaryDV(),
        new AnyURIDV(),
        new QNameDV(),
//        new PrecisionDecimalDV(), // XML Schema 1.1 type
        null,
        new QNameDV(),   // notation use the same one as qname
        new IDDV(),
        new IDREFDV(),
        new EntityDV(),
//        new IntegerDV(),
        new IntegerParser(),
        new ListDV(),
        new UnionDV(),
//        new YearMonthDurationDV(), // XML Schema 1.1 type
        null,
//        new DayTimeDurationDV(), // XML Schema 1.1 type
        null,
//        new AnyAtomicDV() // XML Schema 1.1 type
        null,
    };
    private static final Class[] typeResultClasses = {
    	String.class,
    	String.class,
    	Boolean.class,
    	Double.class,
    	Float.class,
    	Double.class,
    	XSDateTime.class,
    	XSDateTime.class,
    	XSDateTime.class,
    	XSDateTime.class,
    	XSDateTime.class,
    	XSDateTime.class,
    	XSDateTime.class,
    	XSDateTime.class,
    	XSDateTime.class,
    	ByteListImpl.class,
    	ByteListImpl.class,
    	String.class,
    	QName.class,
    	Object.class,
    	QName.class,
    	String.class,
    	String.class,
    	String.class,
    	Integer.class,
    	Object.class,
    	Object.class,
    	Object.class,
    	Object.class,
    	Object.class,
    };
   
   public static Class getClassType(short typeCode) {
	   if(typeCode > -1 && typeCode < typeResultClasses.length) {
		   return typeResultClasses[typeCode];
	   }
	   return String.class;
   }
    
    public static TypeValidator getTypeValidator(String typeName) {
    	short typeIdx = getTypeCodeForTypeName(typeName);
    	return typeValidators[typeIdx];
    }
    public static TypeValidator getTypeValidator(short typeIdx) {
    	return typeValidators[typeIdx];
    }
}

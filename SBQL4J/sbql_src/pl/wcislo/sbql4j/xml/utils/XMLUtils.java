package pl.wcislo.sbql4j.xml.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.compiletime.XMLSimpleTypeWithAttributesSignature;
import pl.wcislo.sbql4j.java.model.runtime.Struct;
import pl.wcislo.sbql4j.lang.codegen.QueryCodeGenerator;

public class XMLUtils {
	public static Object derefIfNecessary(Object result) {
		Object newVal = derefResult(result);
		if(newVal != null) {
			return newVal;
		} else {
			return result;
		}
	}
	
	public static Object derefResult(Object result) {
		if(result == null) {
			return null;
		}
		if(result instanceof Struct) {
			Struct s = (Struct) result;
			if(s.isXmlSimpleTypeWithAttr()) {
				Object val = s.get("_value");
				return val;
			} else {
				Struct newVal = new Struct();
				for(Object key : s.keySet()) {
					Object oldVal = s.get(key);
					Object deref = derefResult(oldVal);
					String keyString = (String) key;
					if(deref != null) {
						newVal.put(keyString, deref);
					} else {
						newVal.put(keyString, oldVal);
					}
				}
				return newVal;
			}
		} else if(result instanceof List) {
			List list = (List)result;
			List newList = new ArrayList();
			for(int i=0; i<list.size(); i++) {
				Object oldVal = list.get(i);
				Object newVal = derefResult(oldVal);
				if(newVal != null) {
//					list.remove(i);
//					list.add(i, newVal);
					newList.add(newVal);
				} else {
					newList.add(oldVal);
				}
			}
			return newList;
		} else if(result instanceof Collection) {
			Collection col = (Collection) result;
			Collection newCol = new ArrayList();
			for(Iterator it = col.iterator(); it.hasNext(); ) {
				Object oldVal = it.next();
				Object newVal = derefResult(oldVal);
				if(newVal != null) {
					newCol.add(newVal);
				} else {
					newCol.add(oldVal);
				}
			}
			return newCol;
		}
		return null;
	}
	
	public static String genDerefCodeIfNecessary(String ident, Signature identSig, QueryCodeGenerator gen, StringBuilder appender) {
		if(!(identSig instanceof XMLSimpleTypeWithAttributesSignature)) {
			return ident;
		}
		XMLSimpleTypeWithAttributesSignature xmlSig = (XMLSimpleTypeWithAttributesSignature) identSig;
		String newIdent = gen.generateIdentifier(ident+"_deref");
		SCollectionType colType = identSig.getColType();
		StringBuilder sb = appender;
		String newTypeSingle = xmlSig.getJavaTypeStringSingleResult(true);
		if(colType == SCollectionType.NO_COLLECTION) {
			sb.append(newTypeSingle+" "+newIdent+" = ("+newTypeSingle+") "+ident+".get(\"_value\");\n");
		} else if(colType == SCollectionType.SEQUENCE || colType == SCollectionType.BAG) {
			sb.append("List<"+newTypeSingle+"> "+newIdent+" = new ArrayList<"+newTypeSingle+">();\n");
			String identLoopOldVal = gen.generateIdentifier("s");
			sb.append("for(Struct "+identLoopOldVal+" : "+ident+") {\n");
			sb.append(newIdent+".add(("+newTypeSingle+") "+identLoopOldVal+".get(\"_value\"));\n");
			sb.append("}\n");
		}
		return newIdent;
	}
}

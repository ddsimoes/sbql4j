package pl.wcislo.sbql4j.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Content;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import pl.wcislo.sbql4j.exception.SBQLException;
import pl.wcislo.sbql4j.java.model.compiletime.Signature;
import pl.wcislo.sbql4j.java.model.compiletime.StructSignature;
import pl.wcislo.sbql4j.java.model.compiletime.Signature.SCollectionType;
import pl.wcislo.sbql4j.java.model.runtime.JavaClass;
import pl.wcislo.sbql4j.java.model.runtime.JavaComplexObject;
import pl.wcislo.sbql4j.java.model.runtime.JavaObject;
import pl.wcislo.sbql4j.java.model.runtime.Struct;
import pl.wcislo.sbql4j.lang.types.Binder;
import pl.wcislo.sbql4j.lang.types.ComplexType;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.model.StructSBQL;
import pl.wcislo.sbql4j.model.collections.Bag;
import pl.wcislo.sbql4j.model.collections.CollectionResult;
import pl.wcislo.sbql4j.model.collections.Sequence;
import pl.wcislo.sbql4j.xml.model.ComplexDBObject;
import pl.wcislo.sbql4j.xml.model.DBObject;
import pl.wcislo.sbql4j.xml.model.ReferenceDBObject;
import pl.wcislo.sbql4j.xml.model.XmlId;
import pl.wcislo.sbql4j.xml.parser.store.XMLObjectStore;

/**
 * @author emil.wcislo
 */
public class Utils {

	private static final Logger log = Logger.getLogger(Utils.class);
	
	public static String resultToString(QueryResult obj) throws IOException {
		return resultToString(obj, true);
	}

	public static String resultToString(QueryResult obj, boolean recurrent)
			throws IOException {
		Element elRoot = new Element("RESULT");
		elRoot.addContent(getXMLElements(obj, recurrent));

		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		StringWriter sw = new StringWriter();
		out.output(elRoot, sw);
		return sw.toString();
	}

	// public static String objectToString(BaseObject obj) throws IOException {
	// Element elRoot = getXMLElement(obj);
	// XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
	// StringWriter sw = new StringWriter();
	// out.output(elRoot, sw);
	// return sw.toString();
	// }

	public static List<Content> getXMLElements(QueryResult o, boolean recurrent) {
		List<Content> res = new ArrayList<Content>();
		if (o instanceof CollectionResult) {
			CollectionResult col = (CollectionResult) o;
			for (QueryResult t : col) {
				res.addAll(getXMLElements(t, recurrent));
			}
		} else if (o instanceof StructSBQL) {
			StructSBQL s = (StructSBQL) o;
			for (QueryResult t : s) {
				res.addAll(getXMLElements(t, recurrent));
			}
		} else if (o instanceof ReferenceDBObject) {
			ReferenceDBObject ref = (ReferenceDBObject) o;
			Element el = new Element(ref.getName());

			// System.out.println(ref.getName()+" "+ref.getId()+"
			// "+ref.getRefObject());
			el.setAttribute("IDREF", ref.getRefObject().getId() + "");
			res.add(el);
		} else if (o instanceof XmlId) {
			XmlId id = (XmlId) o;
			Content e = new Text(id.toString() + "\n");
			res.add(e);
		} else if (o instanceof ComplexType<?>) {
			ComplexType<?> ct = (ComplexType<?>) o;
			Element e;
			if (o instanceof ComplexDBObject) {
				ComplexDBObject co = (ComplexDBObject) o;
				e = new Element(co.getName());
				e.setAttribute("XmlId", co.getId().toString());
			} else if (o instanceof JavaComplexObject) {
				JavaComplexObject jco = (JavaComplexObject) o;
				// TODO JavaComplexObjectReflect
				jco.nested();
				e = new Element(jco.getClass().getName());
			} else {
				e = null;
			}
			for (QueryResult child : ct.getNestedObjects()) {
				if (!recurrent && child instanceof ComplexType<?>) {
					continue;
				}
				if (child instanceof Binder) {
					e.addContent(getXMLElements((Binder)child, recurrent));
				}
			}
			res.add(e);
		} else if (o instanceof Binder) {
			Binder st = (Binder) o;
			Element e = new Element(st.name);
			e.addContent(getXMLElements(st.object, recurrent));
			// e.setText(st.object.toString());
			res.add(e);
		}
		return res;
	}

	//	
	// public static ENVSType derefRec(ENVSType t, JPSObjectStore s) {
	// if(t instanceof JPSIdentifier) {
	// BaseObject o = s.get((JPSIdentifier) t);
	// if(o instanceof ComplexObject) {
	// ComplexObject co = (ComplexObject) o;
	// for(BaseObject child : co.getNestedObjects()) {
	// res.addContent(getXMLElement(child));
	// }
	// }
	// }
	// }

	public static Object toSimpleValue(QueryResult cValue, XMLObjectStore store) {
		if (cValue instanceof CollectionResult) {
			CollectionResult cRes = (CollectionResult) cValue;
			if(cRes.isEmpty()) {
				return null;
			} else {
				cValue = cRes.iterator().next();
			}
		}
		if (cValue instanceof XmlId) {
			cValue = store.get((XmlId) cValue);
		}
		Object result;
		if (cValue instanceof JavaClass) {
			JavaClass jc = (JavaClass) cValue;
			result = jc.value;
		} else if (cValue instanceof JavaComplexObject) {
			result = ((JavaComplexObject) cValue).value;
		} else if (cValue instanceof Binder) {
			Binder b = (Binder)cValue;
			return toSimpleValue(b.object, store);
		} else {
			result = cValue;
		}
		return result;
	}
	
	public static Collection toSimpleValues(CollectionResult cValue, XMLObjectStore store) {
		try {
			Collection res = cValue.getInnerCollectionType().newInstance();
			for(QueryResult qr : cValue) {
				res.add(toSimpleValue(qr, store));
			}
			return res;
		} catch (InstantiationException e) {
			throw new SBQLException(e);
		} catch (IllegalAccessException e) {
			throw new SBQLException(e);
		}
	}

	public static boolean isEmptyBag(Object o) {
		if (o instanceof Bag) {
			if (((Bag) o).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public static CollectionResult objectToCollection(QueryResult o) {
		if(o == null) {
			return new Bag();
		} else if (o instanceof CollectionResult) {
			return (CollectionResult) o;
		} else {
			Bag b = new Bag();
			b.add(o);
			return b;
		}
	}

	public static StructSBQL objectToStruct(QueryResult o) {
		if (o instanceof StructSBQL) {
			return (StructSBQL) o;
		} else {
			StructSBQL s = new StructSBQL();
			s.add(o);
			return s;
		}
	}

	public static QueryResult collectionToObject(QueryResult b) {
		if(b instanceof CollectionResult) {
			CollectionResult c = (CollectionResult)b;
			if(c.isEmpty()) return null;
			return c.iterator().next();
		} else {
			return b;
		}
	}
	
	public static <E extends CollectionResult> QueryResult collectionToObject(E b) {
		if (b.size() == 1) {
			return b.iterator().next();
		} else {
			return b;
		}
	}

	public static CollectionResult collectionDeref(CollectionResult c,
			XMLObjectStore store) {
		CollectionResult result;
		// CollectionResult tmp;
		if (c instanceof Bag) {
			result = new Bag();
			// tmp = new Bag();
		} else if (c instanceof Sequence) {
			result = new Sequence();
			// tmp = new Sequence();
		} else {
			return null;
		}
		for (QueryResult o : c) {
			if (o instanceof XmlId) {
				result.add(store.get((XmlId) o));
			} else {
				result.add(o);
			}
		}
		// HashSet<ENVSType> res = new HashSet<ENVSType>(tmp);
		// result.addAll(res);
		return result;
	}

	public static CollectionResult collectionRef(CollectionResult c) {
		CollectionResult result;
		if (c instanceof Bag) {
			result = new Bag();
		} else if (c instanceof Sequence) {
			result = new Sequence();
		} else {
			return null;
		}
		for (QueryResult o : c) {
			if (o instanceof DBObject) {
				result.add(((DBObject) o).getId());
			} else {
				result.add(o);
			}
		}
		return result;
	}

	public static CollectionResult replaceSO2ST(CollectionResult c) {
		CollectionResult result;
		if (c instanceof Bag) {
			result = new Bag();
		} else if (c instanceof Sequence) {
			result = new Sequence();
		} else {
			return null;
		}
		for (QueryResult o : c) {
			result.add(o);
		}
		return result;
	}

	public static boolean equalsWithDeref(QueryResult o1, QueryResult o2,
			XMLObjectStore s) {
		if (o1 == o2) {
			return true;
		}
		if (o1 == null) {
			if (o2 == null) {
				return true;
			} else {
				return false;
			}
		}
		if (o2 == null) {
			return false;
		}
		// if(o1.getClass() == o2.getClass()) {
		// return o1.equals(o2);
		// }
		if (o1 instanceof XmlId) {
			DBObject b1 = s.get((XmlId) o1);
			if (o2 instanceof XmlId) {
				DBObject b2 = s.get((XmlId) o2);
				return b1.equals(b2);
			} else {
				return b1.equals(o2);
			}
		} else if (o2 instanceof XmlId) {
			DBObject b2 = s.get((XmlId) o2);
			return b2.equals(o1);
		}
		return o1.equals(o2);
	}

	public static CollectionResult cartesianProduct(QueryResult obj1, QueryResult obj2) {
		CollectionResult c1 = objectToCollection(obj1);
		CollectionResult c2 = objectToCollection(obj2);

		Bag res = new Bag();

		for (Iterator<QueryResult> i1 = c1.iterator(); i1.hasNext();) {
			QueryResult o1 = i1.next();
			for (Iterator<QueryResult> i2 = c2.iterator(); i2.hasNext();) {
				QueryResult o2 = i2.next();
				StructSBQL item = new StructSBQL();
				if (o1 instanceof StructSBQL) {
					item.addAll((StructSBQL) o1);
				} else {
					item.add(o1);
				}
				if (o2 instanceof StructSBQL) {
					item.addAll((StructSBQL) o2);
				} else {
					item.add(o2);
				}
				res.add(item);
			}

		}
		return res;
	}
	
	public static QueryResult cartesianProductSingle(QueryResult obj1, QueryResult obj2) {
		CollectionResult res = cartesianProduct(obj1, obj2);
		return collectionToObject(res);
	}
	
	public static Signature createCartesianSignature(Signature sig1, Signature sig2) {
		StructSignature ssig = new StructSignature();
		if(sig1.getColType() == SCollectionType.SEQUENCE || sig2.getColType() == SCollectionType.SEQUENCE) {
			ssig.setColType(SCollectionType.SEQUENCE);
		} else if(sig1.getColType() == SCollectionType.BAG || sig2.getColType() == SCollectionType.BAG){
			ssig.setColType(SCollectionType.BAG);
		}

		if (!(sig1 instanceof StructSignature)) {
			ssig.addField(sig1.clone());
//			ssig.addField(sig1);
		} else {
			Signature[] fields = ((StructSignature) sig1).getFields();
			for (Signature i : fields) {
				ssig.addField(i.clone());
//				ssig.addField(i);
			}
		}

		if (!(sig2 instanceof StructSignature)) {
			ssig.addField(sig2.clone());
//			ssig.addField(sig2);
		} else {
			Signature[] fields = ((StructSignature) sig2).getFields();
			for (Signature i : fields) {
				ssig.addField(i.clone());
//				ssig.addField(i);
			}
		}
		//wszystkie pola sa pojednyncze - nie ma kolekcji
		for(Signature s : ssig.getFields()) {
			s.setColType(SCollectionType.NO_COLLECTION);
		}
		
		
		return ssig;
	}

	public static boolean equalsNumeric(Object t1, Object t2) {
		if (t1 instanceof Number && t2 instanceof Number) {
			return ((Number) t1).doubleValue() == ((Number) t2).doubleValue();
		} else {
			if(t1 == null || t2 == null) {
				return false;
			}
			return t1.equals(t2);
		}
	}
	
//	public static Collection sbqlType2Java(AbstractCollectionResult s) {
//		Collection result;
//		try {
//			result = s.getInnerCollectionType().newInstance();
//		} catch (Exception e) {
//			log.error(e);
//			return null;
//		}	
//		for (QueryResult t : s) {
//			result.add(sbqlType2Java(t));
//		}
//		return result;
//	}

//	public static Collection sbqlType2Java(Collection<QueryResult> s) {
//		Collection result = new ArrayList();
//		
//		for (QueryResult t : s) {
//			result.add(sbqlType2Java(t));
//		}
//		return result;
//	}
	
	public static List sbqlStuct2JavaList(StructSBQL s) {
		List res = new ArrayList();
		for(QueryResult qr : s) {
			res.add(sbqlType2Java(qr));
		}
		return res;
	}
	
	public static Object sbqlType2Java(QueryResult t) {
		Object jObject = null;
		if (t instanceof JavaObject) {
			return ((JavaObject)t).getValue();
		}
		if(t instanceof CollectionResult) {
			CollectionResult cr = (CollectionResult)t;
			if(cr.isEmpty()) {
//				return null;
				return new ArrayList();
			}
			
			Collection result;
			try {
				result = cr.getInnerCollectionType().newInstance();
			} catch (Exception e) {
				log.error(e);
				return null;
			}	
			for (QueryResult qr : cr) {
				result.add(sbqlType2Java(qr));
			}
			return result;
		} else if (t instanceof JavaComplexObject) {
			jObject = ((JavaComplexObject) t).value;
		} else if(t instanceof JavaClass) {
			jObject = ((JavaClass)t).value;
		} else if(t instanceof Binder) {
			jObject = sbqlType2Java(((Binder)t).object);
		} else if(t instanceof StructSBQL) {
			StructSBQL st = (StructSBQL) t;
			Struct res = new Struct();
//			Map<String, Object> res = new HashMap<String, Object>(); 
			for(int i=0; i<st.size(); i++) {
				QueryResult qr = st.get(i);
				if(qr instanceof Binder) {
					Binder b = (Binder)qr;
					res.put(b.name, sbqlType2Java(b.object));
				} else {
					res.put(String.valueOf(i), sbqlType2Java(qr));
				}
			}
			jObject = res;
		}
		return jObject;
	}

	public static boolean isSimpleType(Object javaObject) {
		return (javaObject instanceof Number || javaObject instanceof String || javaObject instanceof Boolean);
	}

	public static Object mergeExpressionParams(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return null;
		} else {
			List l = new ArrayList();
			if(o1 instanceof Collection) {
				l.addAll((Collection)o1);
			} else {
				l.add(o1);
			}
			if(o2 instanceof Collection) {
				l.addAll((Collection)o2);
			} else {
				l.add(o2);
			}
			return l;
		}
	}
	
	public static QueryResult objectDeref(QueryResult t, XMLObjectStore store) {
		if(t instanceof XmlId) {
			return store.get((XmlId)t);
		}
		return t;
	}
}

package pl.wcislo.sbql4j.java.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import pl.wcislo.sbql4j.java.model.runtime.Struct;
import pl.wcislo.sbql4j.model.QueryResult;
import pl.wcislo.sbql4j.model.StructSBQL;

public class OperatorUtils {
	public static boolean equalsSafe(Object o1, Object o2) {
		if (o1 == null) {
			return false;
		}
		return o1.equals(o2);
	}

	/**
	 * both left and right params are threated as collections
	 * 
	 * @param obj1
	 * @param obj2
	 * @param name1
	 * @param name2
	 * @return
	 */
	public static List<Struct> cartesianProductCC(Object obj1, Object obj2, String name1, String name2) {
		Collection c1, c2;
		if (obj1 instanceof Collection && !(obj1 instanceof Struct)) {
			c1 = (Collection) obj1;
		} else {
			c1 = new ArrayList();
			c1.add(obj1);
		}
		if (obj2 instanceof Collection && !(obj2 instanceof Struct)) {
			c2 = (Collection) obj2;
		} else {
			c2 = new ArrayList();
			c2.add(obj2);
		}
		if(c1.isEmpty()) {
			c1 = new ArrayList();
			c1.add(new ArrayList());
		}
		if(c2.isEmpty()) {
			c2 = new ArrayList();
			c2.add(new ArrayList());
		}
		
		List<Struct> result = new ArrayList<Struct>();
		for (Iterator i1 = c1.iterator(); i1.hasNext();) {
			Object o1 = i1.next();
			for (Iterator i2 = c2.iterator(); i2.hasNext();) {
				Object o2 = i2.next();
				Struct item = new Struct();
				if (o1 instanceof Struct) {
					item.putAll((Struct) o1);
				} else {
					if (name1 != null && !"".equals(name1.trim())) {
						item.put(name1, o1);
					} else {
						item.add(o1);
					}
				}
				if (o2 instanceof Struct) {
					item.putAll((Struct) o2);
				} else {
					if (name2 != null && !"".equals(name2.trim())) {
						item.put(name2, o2);
					} else {
						item.add(o2);
					}
				}
				result.add(item);
			}

		}
		return result;
	}

	/**
	 * left param is single object, right param may be collection
	 * 
	 * @param obj1
	 * @param obj2
	 * @param name1
	 * @param name2
	 * @return
	 */
	public static List<Struct> cartesianProductSC(Object obj1, Object obj2, String name1, String name2) {
		Collection c2;
		// if(obj1 instanceof Collection && !(obj1 instanceof Struct)) {
		// c1 = (Collection) obj1;
		// } else {
		// c1 = new ArrayList();
		// c1.add(obj1);
		// }
		if (obj2 instanceof Collection && !(obj2 instanceof Struct)) {
			c2 = (Collection) obj2;
		} else {
			c2 = new ArrayList();
			c2.add(obj2);
		}
		if(c2.isEmpty()) {
			c2 = new ArrayList();
//			c2.add(new ArrayList());
			c2.add(null);
		}
		List<Struct> result = new ArrayList<Struct>();
		// for (Iterator i1 = c1.iterator(); i1.hasNext();) {
		// Object o1 = i1.next();
		
		for (Iterator i2 = c2.iterator(); i2.hasNext();) {
			Object o2 = i2.next();
			Struct item = new Struct();
			if (obj1 instanceof Struct) {
				item.putAll((Struct) obj1);
			} else {
				if (name1 != null && !"".equals(name1.trim())) {
					item.put(name1, obj1);
				} else {
					item.add(obj1);
				}
			}
			if (o2 instanceof Struct) {
				item.putAll((Struct) o2);
			} else {
				if (name2 != null && !"".equals(name2.trim())) {
					item.put(name2, o2);
				} else {
					item.add(o2);
				}
			}
			result.add(item);
			// }
		}
		return result;
	}

	/**
	 * left param may be collection right param is single object
	 * 
	 * @param obj1
	 * @param obj2
	 * @param name1
	 * @param name2
	 * @return
	 */
	public static List<Struct> cartesianProductCS(Object obj1, Object obj2, String name1, String name2) {
		Collection c1;
		if (obj1 instanceof Collection && !(obj1 instanceof Struct)) {
			c1 = (Collection) obj1;
		} else {
			c1 = new ArrayList();
			c1.add(obj1);
		}
		if(c1.isEmpty()) {
			c1 = new ArrayList();
			c1.add(new ArrayList());
		}
		// if(obj2 instanceof Collection && !(obj2 instanceof Struct)) {
		// c2 = (Collection) obj2;
		// } else {
		// c2 = new ArrayList();
		// c2.add(obj2);
		// }
		List<Struct> result = new ArrayList<Struct>();
		for (Iterator i1 = c1.iterator(); i1.hasNext();) {
			Object o1 = i1.next();
			// for (Iterator i2 = c2.iterator(); i2.hasNext();) {
			// Object o2 = i2.next();
			Struct item = new Struct();
			if (o1 instanceof Struct) {
				item.putAll((Struct) o1);
			} else {
				if (name1 != null && !"".equals(name1.trim())) {
					item.put(name1, o1);
				} else {
					item.add(o1);
				}
			}
			if (obj2 instanceof Struct) {
				item.putAll((Struct) obj2);
			} else {
				if (name2 != null && !"".equals(name2.trim())) {
					item.put(name2, obj2);
				} else {
					item.add(obj2);
				}
			}
			result.add(item);
			// }

		}
		return result;
	}

	public static Struct cartesianProductSS(Object o1, Object o2, String name1, String name2) {
		Struct item = new Struct();
		if (o1 instanceof Struct) {
			item.putAll((Struct) o1);
		} else {
			if (name1 != null && !"".equals(name1.trim())) {
				item.put(name1, o1);
			} else {
				item.add(o1);
			}
		}
		if (o2 instanceof Struct) {
			item.putAll((Struct) o2);
		} else {
			if (name2 != null && !"".equals(name2.trim())) {
				item.put(name2, o2);
			} else {
				item.add(o2);
			}
		}
		return item;
	}
}

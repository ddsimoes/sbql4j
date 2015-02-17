package org.polepos.teams.db4o_sbql4j;


import com.db4o.*;

import com.db4o.config.*;

import com.db4o.foundation.*;

import com.db4o.internal.*;
import com.db4o.internal.btree.BTree;
import com.db4o.internal.btree.BTreeNodeSearchResult;
import com.db4o.internal.btree.BTreePointer;
import com.db4o.internal.btree.BTreeRange;
import com.db4o.internal.btree.FieldIndexKey;
import com.db4o.internal.btree.FieldIndexKeyImpl;
import com.db4o.internal.btree.SearchTarget;
import com.db4o.internal.slots.Slot;

import com.db4o.query.*;

import org.apache.commons.collections.CollectionUtils;

import org.polepos.circuits.flatobject.*;

import org.polepos.data.*;

import pl.wcislo.sbql4j.exception.*;
import pl.wcislo.sbql4j.java.model.runtime.*;
import pl.wcislo.sbql4j.java.model.runtime.factory.*;
import pl.wcislo.sbql4j.java.utils.ArrayUtils;
import pl.wcislo.sbql4j.java.utils.OperatorUtils;
import pl.wcislo.sbql4j.java.utils.Pair;
import pl.wcislo.sbql4j.javac.test.linq_comp.model.Product;
import pl.wcislo.sbql4j.lang.codegen.nostacks.*;
import pl.wcislo.sbql4j.lang.codegen.simple.*;
import pl.wcislo.sbql4j.lang.db4o.*;
import pl.wcislo.sbql4j.lang.db4o.codegen.*;
import pl.wcislo.sbql4j.lang.db4o.codegen.interpreter.*;
import pl.wcislo.sbql4j.lang.db4o.codegen.nostacks.*;
import pl.wcislo.sbql4j.lang.parser.expression.*;
import pl.wcislo.sbql4j.lang.parser.expression.OrderByParamExpression.SortType;
import pl.wcislo.sbql4j.lang.parser.terminals.*;
import pl.wcislo.sbql4j.lang.parser.terminals.operators.*;
import pl.wcislo.sbql4j.lang.types.*;
import pl.wcislo.sbql4j.model.*;
import pl.wcislo.sbql4j.model.collections.*;
import pl.wcislo.sbql4j.util.*;
import pl.wcislo.sbql4j.xml.model.*;
import pl.wcislo.sbql4j.xml.parser.store.*;

import java.util.*;


public class QueryIndexedObjectByString implements Db4oSBQLQuery {
    private java.lang.String stringId;

    public QueryIndexedObjectByString(
        java.lang.String stringId) {
        this.stringId = stringId;
    }

    public java.util.Collection<org.polepos.data.IndexedObject> executeQuery(
        ObjectContainerBase ocb, Transaction t) {
        //evaluateExpression - start IndexedObject where _string in stringIds
        final LocalTransaction transLocal = (LocalTransaction) t;

        //visitWhereExpression - start IndexedObject where _string in stringIds
        //visitIdentifierExpression - start IndexedObject
        final java.util.Collection<org.polepos.data.IndexedObject> _ident_IndexedObject =
            new java.util.ArrayList<org.polepos.data.IndexedObject>();
        ClassMetadata _classMeta0 = ocb.classCollection()
                                       .getClassMetadata("org.polepos.data.IndexedObject");
        
        FieldMetadata fm = _classMeta0.fieldMetadataForName("_string");
        
        BTree idx = fm.getIndex(transLocal);
        Iterator4 it4 = idx.iterator(transLocal);
        while(it4.moveNext()) {
        	Object o = it4.current();
        	System.out.println(o);
        }
        
    	BTreeRange range = fm.search(t, stringId);
		Iterator4 it = range.pointers();
		while(it.moveNext()) {
			BTreePointer point = (BTreePointer) it.current();
			FieldIndexKeyImpl pointKey = (FieldIndexKeyImpl) point.key();
		  	int id = pointKey.parentID();
		  	LazyObjectReference _ref0 = transLocal.lazyReferenceFor(id);
		  	IndexedObject o = (IndexedObject)_ref0.getObject();
		  	ocb.activate(o);
		  	_ident_IndexedObject.add(o);
		}
//        }
//        PreparedComparison<FieldIndexKeyImpl> pc = new PreparedComparison<FieldIndexKeyImpl>() {
//			@Override
//			public int compareTo(FieldIndexKeyImpl obj) {
//				String s = obj.toString();
//				Object val = obj.value();
//				
//				if(stringIds.contains(obj)) {
//					return 0;
//				} else {
//					return -1;
//				}
//			}
//		};
		
//		BTreeNodeSearchResult res = fm.getIndex(t).searchLeaf(t, pc, SearchTarget.ANY);
//		System.out.println(res);
//        while(it.moveNext()) {
//        	Object itObj = it.current();
//        	FieldIndexKeyImpl key = (FieldIndexKeyImpl) itObj;
//        	Slot keyVal = (Slot) key.value();
//        	System.out.println(itObj);
//        }
        
//        BTreeRange range = fm.search(t, "Ikura");
//        int rangeSize = range.size();
//        Iterator4 it = range.pointers();
//        while(it.moveNext()) {
//        	
//        	BTreePointer point = (BTreePointer) it.current();
//        	FieldIndexKeyImpl pointKey = (FieldIndexKeyImpl) point.key();
//        	int id = pointKey.parentID();
////        	point.node().read(t);
//        	LazyObjectReference _ref0 = transLocal.lazyReferenceFor(id);
//        	Product o = (Product)_ref0.getObject();
//        	ocb.activate(o);
//            _ident_Product.add(o);
//        	System.out.println(point);
//        }
//        
        
//        long[] _ids0 = _classMeta0.getIDs(transLocal);
//
//        for (long _id0 : _ids0) {
//            LazyObjectReference _ref0 = transLocal.lazyReferenceFor((int) _id0);
//            _ident_IndexedObject.add((org.polepos.data.IndexedObject) _ref0.getObject());
//        }
//
//        //visitIdentifierExpression - end IndexedObject
//        java.util.Collection<org.polepos.data.IndexedObject> _whereResult = new java.util.ArrayList<org.polepos.data.IndexedObject>();
//        int _whereLoopIndex = 0;
//
//        for (org.polepos.data.IndexedObject _whereEl : _ident_IndexedObject) {
//            if (_whereEl == null) {
//                continue;
//            }
//
//            ocb.activate(_whereEl, 1);
//
//            //visitBinaryAExpression - start _string IN stringIds
//            //visitIdentifierExpression - start _string
//            //visitIdentifierExpression - end _string
//            //visitIdentifierExpression - start stringIds
//            //visitIdentifierExpression - end stringIds
//            //OperatorIn - start _string IN stringIds
//            Collection _inLeftCol0 = new ArrayList();
//            _inLeftCol0.add(((_whereEl == null) ? null : _whereEl._string));
//
//            Collection _inRightCol0 = new ArrayList(stringIds);
//            java.lang.Boolean _inResult = _inRightCol0.containsAll(_inLeftCol0);
//
//            //OperatorIn - end _string IN stringIds
//            //visitBinaryAExpression - end _string IN stringIds
//            if (_inResult) {
//                _whereResult.add(_whereEl);
//            }
//
//            _whereLoopIndex++;
//        }
//
//        //visitWhereExpression - end IndexedObject where _string in stringIds
//        ocb.activate(_whereResult);

        return _ident_IndexedObject;

        //evaluateExpression - end IndexedObject where _string in stringIds
    }
}

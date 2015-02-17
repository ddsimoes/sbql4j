package org.polepos.teams.db4o_sbql4j;

import com.db4o.*;

import com.db4o.config.*;

import com.db4o.foundation.*;

import com.db4o.internal.*;
import com.db4o.internal.btree.*;

import com.db4o.query.*;

import org.apache.commons.collections.CollectionUtils;

import org.polepos.circuits.flatobject.*;

import org.polepos.data.*;

import pl.wcislo.sbql4j.exception.*;
import pl.wcislo.sbql4j.java.model.runtime.*;
import pl.wcislo.sbql4j.java.model.runtime.Struct;
import pl.wcislo.sbql4j.java.model.runtime.factory.*;
import pl.wcislo.sbql4j.java.utils.ArrayUtils;
import pl.wcislo.sbql4j.java.utils.OperatorUtils;
import pl.wcislo.sbql4j.java.utils.Pair;
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
import pl.wcislo.sbql4j.util.Utils;
import pl.wcislo.sbql4j.xml.model.*;
import pl.wcislo.sbql4j.xml.parser.store.*;

import java.util.*;


public class Sbql4jQueries_SbqlQuery1Db4o0 implements Db4oSBQLQuery {
    private java.lang.Integer intId;

    public Sbql4jQueries_SbqlQuery1Db4o0(java.lang.Integer intId) {
        this.intId = intId;
    }

    /**
     * query='db.IndexedObject_ByIndex[_int](intId)'
    '
     **/
    public java.util.Collection<org.polepos.data.IndexedObject> executeQuery(
        ObjectContainerBase ocb, Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        java.lang.Integer _ident_intId = intId;
        final java.util.Collection<org.polepos.data.IndexedObject> _ident_IndexedObject =
            new java.util.ArrayList<org.polepos.data.IndexedObject>();
        ClassMetadata _classMeta1 = ocb.classCollection()
                                       .getClassMetadata("org.polepos.data.IndexedObject");
        FieldMetadata _fieldMeta1 = _classMeta1.fieldMetadataForName("_int");
        BTreeRange _range1 = _fieldMeta1.search(t, _ident_intId);
        Iterator4 _it1 = _range1.pointers();

        while (_it1.moveNext()) {
            BTreePointer _point1 = (BTreePointer) _it1.current();
            FieldIndexKeyImpl _pointKey1 = (FieldIndexKeyImpl) _point1.key();
            int _id1 = _pointKey1.parentID();
            LazyObjectReference _ref1 = transLocal.lazyReferenceFor(_id1);
            org.polepos.data.IndexedObject _obj1 = (org.polepos.data.IndexedObject) _ref1.getObject();

            if (_obj1 != null) {
                ocb.activate(_obj1);
            }

            _ident_IndexedObject.add(_obj1);
        }

        if (_ident_IndexedObject != null) {
            ocb.activate(_ident_IndexedObject);
        }

        return _ident_IndexedObject;
    }
}

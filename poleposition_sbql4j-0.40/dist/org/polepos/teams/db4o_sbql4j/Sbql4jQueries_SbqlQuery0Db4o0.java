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


public class Sbql4jQueries_SbqlQuery0Db4o0 implements Db4oSBQLQuery {
    private java.lang.String stringId;

    public Sbql4jQueries_SbqlQuery0Db4o0(java.lang.String stringId) {
        this.stringId = stringId;
    }

    /**
     * query='db.IndexedObject_ByIndex[_string](stringId)'
    '
     **/
    public java.util.Collection<org.polepos.data.IndexedObject> executeQuery(
        ObjectContainerBase ocb, Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        java.lang.String _ident_stringId = stringId;
        final java.util.Collection<org.polepos.data.IndexedObject> _ident_IndexedObject =
            new java.util.ArrayList<org.polepos.data.IndexedObject>();
        ClassMetadata _classMeta0 = ocb.classCollection()
                                       .getClassMetadata("org.polepos.data.IndexedObject");
        FieldMetadata _fieldMeta0 = _classMeta0.fieldMetadataForName("_string");
        BTreeRange _range0 = _fieldMeta0.search(t, _ident_stringId);
        Iterator4 _it0 = _range0.pointers();

        while (_it0.moveNext()) {
            BTreePointer _point0 = (BTreePointer) _it0.current();
            FieldIndexKeyImpl _pointKey0 = (FieldIndexKeyImpl) _point0.key();
            int _id0 = _pointKey0.parentID();
            LazyObjectReference _ref0 = transLocal.lazyReferenceFor(_id0);
            org.polepos.data.IndexedObject _obj0 = (org.polepos.data.IndexedObject) _ref0.getObject();

            if (_obj0 != null) {
                ocb.activate(_obj0);
            }

            _ident_IndexedObject.add(_obj0);
        }

        if (_ident_IndexedObject != null) {
            ocb.activate(_ident_IndexedObject);
        }

        return _ident_IndexedObject;
    }
}

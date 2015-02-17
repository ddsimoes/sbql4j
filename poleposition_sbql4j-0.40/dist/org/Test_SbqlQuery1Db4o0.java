package org;

import com.db4o.*;
import com.db4o.ObjectContainer;

import com.db4o.foundation.*;

import com.db4o.internal.*;
import com.db4o.internal.btree.*;

import org.apache.commons.collections.CollectionUtils;

import pl.wcislo.sbql4j.exception.*;
import pl.wcislo.sbql4j.java.model.runtime.*;
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


public class Test_SbqlQuery1Db4o0 implements Db4oSBQLQuery {
    public Test_SbqlQuery1Db4o0() {
    }

    /**
     * query='db.(Integer[0])'
    '
     **/
    public java.lang.Integer executeQuery(ObjectContainerBase ocb, Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<java.lang.Integer> _ident_Integer = new java.util.ArrayList<java.lang.Integer>();
        ClassMetadata _classMeta0 = ocb.classCollection()
                                       .getClassMetadata("java.lang.Integer");
        long[] _ids0 = _classMeta0.getIDs(transLocal);

        for (long _id0 : _ids0) {
            LazyObjectReference _ref0 = transLocal.lazyReferenceFor((int) _id0);
            _ident_Integer.add((java.lang.Integer) _ref0.getObject());
        }

        java.lang.Integer _element_atResult;
        _element_atResult = null;

        if (!_ident_Integer.isEmpty()) {
            Iterator<java.lang.Integer> _elementAtIteraotr0 = _ident_Integer.iterator();

            for (int _j0 = 0; _j0 < (0 + 1); _j0++) {
                _element_atResult = _elementAtIteraotr0.next();
            }
        }

        if (_element_atResult != null) {
            ocb.activate(_element_atResult);
        }

        return _element_atResult;
    }
}

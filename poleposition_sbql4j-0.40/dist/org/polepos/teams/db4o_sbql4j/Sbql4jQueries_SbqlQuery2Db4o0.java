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


public class Sbql4jQueries_SbqlQuery2Db4o0 implements Db4oSBQLQuery {
    private java.lang.Integer i2Constrain;

    public Sbql4jQueries_SbqlQuery2Db4o0(java.lang.Integer i2Constrain) {
        this.i2Constrain = i2Constrain;
    }

    /**
     * query='db.(InheritanceHierarchy4 where getI2() == i2Constrain)'
    '
     **/
    public java.util.Collection<org.polepos.data.InheritanceHierarchy4> executeQuery(
        ObjectContainerBase ocb, Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<org.polepos.data.InheritanceHierarchy4> _ident_InheritanceHierarchy4 =
            new java.util.ArrayList<org.polepos.data.InheritanceHierarchy4>();
        ClassMetadata _classMeta2 = ocb.classCollection()
                                       .getClassMetadata("org.polepos.data.InheritanceHierarchy4");
        long[] _ids2 = _classMeta2.getIDs(transLocal);

        for (long _id2 : _ids2) {
            LazyObjectReference _ref2 = transLocal.lazyReferenceFor((int) _id2);
            _ident_InheritanceHierarchy4.add((org.polepos.data.InheritanceHierarchy4) _ref2.getObject());
        }

        java.util.Collection<org.polepos.data.InheritanceHierarchy4> _whereResult =
            new java.util.ArrayList<org.polepos.data.InheritanceHierarchy4>();
        int _whereLoopIndex = 0;

        for (org.polepos.data.InheritanceHierarchy4 _whereEl : _ident_InheritanceHierarchy4) {
            if (_whereEl == null) {
                continue;
            }

            if (_whereEl != null) {
                ocb.activate(_whereEl, 1);
            }

            java.lang.Integer _mth_getI2Result = _whereEl.getI2();
            java.lang.Integer _ident_i2Constrain = i2Constrain;
            java.lang.Boolean _equalsResult = OperatorUtils.equalsSafe(_mth_getI2Result,
                    _ident_i2Constrain);

            if (_equalsResult) {
                _whereResult.add(_whereEl);
            }

            _whereLoopIndex++;
        }

        if (_whereResult != null) {
            ocb.activate(_whereResult);
        }

        return _whereResult;
    }
}

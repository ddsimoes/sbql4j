package pl.wcislo.sbql4j.javac.test.football.query;

import com.db4o.*;
import com.db4o.ObjectContainer;

import com.db4o.foundation.*;

import com.db4o.internal.*;
import com.db4o.internal.btree.*;

import org.apache.commons.collections.CollectionUtils;

import pl.wcislo.sbql4j.db4o.*;
import pl.wcislo.sbql4j.exception.*;
import pl.wcislo.sbql4j.java.model.runtime.*;
import pl.wcislo.sbql4j.java.model.runtime.Struct;
import pl.wcislo.sbql4j.java.model.runtime.factory.*;
import pl.wcislo.sbql4j.java.utils.ArrayUtils;
import pl.wcislo.sbql4j.java.utils.OperatorUtils;
import pl.wcislo.sbql4j.java.utils.Pair;
import pl.wcislo.sbql4j.javac.test.football.data.*;
import pl.wcislo.sbql4j.javac.test.football.model.*;
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
import pl.wcislo.sbql4j.lang.xml.*;
import pl.wcislo.sbql4j.model.*;
import pl.wcislo.sbql4j.model.collections.*;
import pl.wcislo.sbql4j.util.*;
import pl.wcislo.sbql4j.util.Utils;
import pl.wcislo.sbql4j.xml.model.*;
import pl.wcislo.sbql4j.xml.parser.store.*;

import java.lang.Integer;
import java.lang.String;

import java.text.ParseException;

import java.util.*;


public class KORQueries_SbqlQuery34Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery34Db4o0() {
    }

    /**
     * query='dbConn.(PilkarzMecz where getIlosc_goli() == 5)'
    '
     **/
    public java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> _ident_PilkarzMecz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz>();
        ClassMetadata _classMeta47 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz");
        long[] _ids47 = _classMeta47.getIDs(transLocal);

        for (long _id47 : _ids47) {
            LazyObjectReference _ref47 = transLocal.lazyReferenceFor((int) _id47);
            _ident_PilkarzMecz.add((pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz) _ref47.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> _whereResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz>();
        int _whereLoopIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz _whereEl : _ident_PilkarzMecz) {
            if (_whereEl == null) {
                continue;
            }

            if (_whereEl != null) {
                ocb.activate(_whereEl, 1);
            }

            java.lang.Integer _mth_getIlosc_goliResult = _whereEl.getIlosc_goli();

            if (_mth_getIlosc_goliResult != null) {
                ocb.activate(_mth_getIlosc_goliResult, 1);
            }

            java.lang.Boolean _equalsResult = OperatorUtils.equalsSafe(_mth_getIlosc_goliResult,
                    5);

            if (_equalsResult) {
                _whereResult.add(_whereEl);
            }

            _whereLoopIndex++;
        }

        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_whereResult, ocb);

        return _whereResult;
    }
}

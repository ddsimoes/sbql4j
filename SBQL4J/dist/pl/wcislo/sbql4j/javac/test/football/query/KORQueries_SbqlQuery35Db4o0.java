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


public class KORQueries_SbqlQuery35Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery35Db4o0() {
    }

    /**
     * query='dbConn.( avg(Lekarz.getPremia()) group as _aux0).(Lekarz where getPremia() > _aux0)'
    '
     **/
    public java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Lekarz> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Lekarz> _ident_Lekarz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Lekarz>();
        ClassMetadata _classMeta48 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Lekarz");
        long[] _ids48 = _classMeta48.getIDs(transLocal);

        for (long _id48 : _ids48) {
            LazyObjectReference _ref48 = transLocal.lazyReferenceFor((int) _id48);
            _ident_Lekarz.add((pl.wcislo.sbql4j.javac.test.football.model.Lekarz) _ref48.getObject());
        }

        java.util.Collection<java.lang.Integer> _dotResult = new java.util.ArrayList<java.lang.Integer>();
        int _dotIndex = 0;

        if (_ident_Lekarz != null) {
            for (pl.wcislo.sbql4j.javac.test.football.model.Lekarz _dotEl : _ident_Lekarz) {
                if (_dotEl == null) {
                    continue;
                }

                if (_dotEl != null) {
                    ocb.activate(_dotEl, 1);
                }

                java.lang.Integer _mth_getPremiaResult = _dotEl.getPremia();

                if (_mth_getPremiaResult != null) {
                    ocb.activate(_mth_getPremiaResult, 1);
                }

                if (_mth_getPremiaResult != null) {
                    ocb.activate(_mth_getPremiaResult, 1);
                }

                _dotResult.add(_mth_getPremiaResult);
                _dotIndex++;
            }
        }

        java.lang.Double _avgResult = 0d;

        if ((_dotResult != null) && !_dotResult.isEmpty()) {
            Number _avgSum6 = null;

            for (Number _avgEl6 : _dotResult) {
                _avgSum6 = MathUtils.sum(_avgSum6, _avgEl6);
            }

            _avgResult = _avgSum6.doubleValue() / _dotResult.size();
        }

        java.lang.Double _groupAsResult_aux0 = _avgResult;
        java.lang.Double _dotEl1 = _groupAsResult_aux0;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Lekarz> _ident_Lekarz1 =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Lekarz>();
        ClassMetadata _classMeta49 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Lekarz");
        long[] _ids49 = _classMeta49.getIDs(transLocal);

        for (long _id49 : _ids49) {
            LazyObjectReference _ref49 = transLocal.lazyReferenceFor((int) _id49);
            _ident_Lekarz1.add((pl.wcislo.sbql4j.javac.test.football.model.Lekarz) _ref49.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Lekarz> _whereResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Lekarz>();
        int _whereLoopIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Lekarz _whereEl : _ident_Lekarz1) {
            if (_whereEl == null) {
                continue;
            }

            if (_whereEl != null) {
                ocb.activate(_whereEl, 1);
            }

            java.lang.Integer _mth_getPremiaResult1 = _whereEl.getPremia();

            if (_mth_getPremiaResult1 != null) {
                ocb.activate(_mth_getPremiaResult1, 1);
            }

            java.lang.Double _ident__aux0 = _dotEl1;

            if (_ident__aux0 != null) {
                ocb.activate(_ident__aux0, 1);
            }

            Boolean _moreResult = (_mth_getPremiaResult1 == null)
                ? ((_mth_getPremiaResult1 == null) ? false : false)
                : ((_mth_getPremiaResult1 == null) ? true
                                                   : (_mth_getPremiaResult1 > _ident__aux0));

            if (_moreResult) {
                _whereResult.add(_whereEl);
            }

            _whereLoopIndex++;
        }

        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_whereResult, ocb);

        return _whereResult;
    }
}

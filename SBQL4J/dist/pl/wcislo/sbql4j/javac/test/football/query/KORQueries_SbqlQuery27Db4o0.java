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


public class KORQueries_SbqlQuery27Db4o0 implements Db4oSbqlQuery {
    private java.lang.String nazwadruzyny;

    public KORQueries_SbqlQuery27Db4o0(java.lang.String nazwadruzyny) {
        this.nazwadruzyny = nazwadruzyny;
    }

    /**
     * query='dbConn.((Druzyna as d where d.getNazwa() == nazwadruzyny) group as _aux0).(Stroj as s join _aux0)'
    '
     **/
    public java.util.Collection<pl.wcislo.sbql4j.java.model.runtime.Struct> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _ident_Druzyna =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Druzyna>();
        ClassMetadata _classMeta37 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Druzyna");
        long[] _ids37 = _classMeta37.getIDs(transLocal);

        for (long _id37 : _ids37) {
            LazyObjectReference _ref37 = transLocal.lazyReferenceFor((int) _id37);
            _ident_Druzyna.add((pl.wcislo.sbql4j.javac.test.football.model.Druzyna) _ref37.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _asResult_d =
            _ident_Druzyna;
        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _whereResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Druzyna>();
        int _whereLoopIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Druzyna _whereEl : _asResult_d) {
            if (_whereEl == null) {
                continue;
            }

            if (_whereEl != null) {
                ocb.activate(_whereEl, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _ident_d = _whereEl;

            if (_ident_d != null) {
                ocb.activate(_ident_d, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl = _ident_d;

            if (_ident_d != null) {
                ocb.activate(_ident_d, 2);
            }

            java.lang.String _mth_getNazwaResult = _dotEl.getNazwa();

            if (_mth_getNazwaResult != null) {
                ocb.activate(_mth_getNazwaResult, 1);
            }

            java.lang.String _ident_nazwadruzyny = nazwadruzyny;
            java.lang.Boolean _equalsResult = OperatorUtils.equalsSafe(_mth_getNazwaResult,
                    _ident_nazwadruzyny);

            if (_equalsResult) {
                _whereResult.add(_whereEl);
            }

            _whereLoopIndex++;
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _groupAsResult_aux0 =
            _whereResult;
        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _dotEl1 =
            _groupAsResult_aux0;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Stroj> _ident_Stroj =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Stroj>();
        ClassMetadata _classMeta38 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Stroj");
        long[] _ids38 = _classMeta38.getIDs(transLocal);

        for (long _id38 : _ids38) {
            LazyObjectReference _ref38 = transLocal.lazyReferenceFor((int) _id38);
            _ident_Stroj.add((pl.wcislo.sbql4j.javac.test.football.model.Stroj) _ref38.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Stroj> _asResult_s =
            _ident_Stroj;
        java.util.Collection<pl.wcislo.sbql4j.java.model.runtime.Struct> _joinResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.java.model.runtime.Struct>();
        int _joinIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Stroj _joinEl : _asResult_s) {
            if (_joinEl != null) {
                ocb.activate(_joinEl, 1);
            }

            java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _ident__aux0 =
                _dotEl1;

            if (_ident__aux0 != null) {
                ocb.activate(_ident__aux0, 2);
            }

            _joinResult.addAll(OperatorUtils.cartesianProductSC(_joinEl,
                    _ident__aux0, "s", "d"));
            _joinIndex++;
        }

        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_joinResult, ocb);

        return _joinResult;
    }
}

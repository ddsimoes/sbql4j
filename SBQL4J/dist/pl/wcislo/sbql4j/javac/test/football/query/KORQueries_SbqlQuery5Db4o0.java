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


public class KORQueries_SbqlQuery5Db4o0 implements Db4oSbqlQuery {
    private java.lang.String druzyna1;

    public KORQueries_SbqlQuery5Db4o0(java.lang.String druzyna1) {
        this.druzyna1 = druzyna1;
    }

    /**
     * query='dbConn.( count((Mecz where getDruzyna().getNazwa() == getDruzyna2())) as d2 group as _aux0).( count((Mecz where getDruzyna().getNazwa() == druzyna1)) as d1 join _aux0).(d1 == d2)'
    '
     **/
    public java.lang.Boolean executeQuery(final ObjectContainerBase ocb,
        final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Mecz> _ident_Mecz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Mecz>();
        ClassMetadata _classMeta5 = ocb.classCollection()
                                       .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Mecz");
        long[] _ids5 = _classMeta5.getIDs(transLocal);

        for (long _id5 : _ids5) {
            LazyObjectReference _ref5 = transLocal.lazyReferenceFor((int) _id5);
            _ident_Mecz.add((pl.wcislo.sbql4j.javac.test.football.model.Mecz) _ref5.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Mecz> _whereResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Mecz>();
        int _whereLoopIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Mecz _whereEl : _ident_Mecz) {
            if (_whereEl == null) {
                continue;
            }

            if (_whereEl != null) {
                ocb.activate(_whereEl, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _mth_getDruzynaResult =
                _whereEl.getDruzyna();

            if (_mth_getDruzynaResult != null) {
                ocb.activate(_mth_getDruzynaResult, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl = _mth_getDruzynaResult;

            if (_mth_getDruzynaResult != null) {
                ocb.activate(_mth_getDruzynaResult, 2);
            }

            java.lang.String _mth_getNazwaResult = _dotEl.getNazwa();

            if (_mth_getNazwaResult != null) {
                ocb.activate(_mth_getNazwaResult, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _mth_getDruzyna2Result =
                _whereEl.getDruzyna2();

            if (_mth_getDruzyna2Result != null) {
                ocb.activate(_mth_getDruzyna2Result, 1);
            }

            java.lang.Boolean _equalsResult = OperatorUtils.equalsSafe(_mth_getNazwaResult,
                    _mth_getDruzyna2Result);

            if (_equalsResult) {
                _whereResult.add(_whereEl);
            }

            _whereLoopIndex++;
        }

        java.lang.Integer _countResult = _whereResult.size();
        java.lang.Integer _asResult_d2 = _countResult;
        java.lang.Integer _groupAsResult_aux0 = _asResult_d2;
        java.lang.Integer _dotEl2 = _groupAsResult_aux0;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Mecz> _ident_Mecz1 =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Mecz>();
        ClassMetadata _classMeta6 = ocb.classCollection()
                                       .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Mecz");
        long[] _ids6 = _classMeta6.getIDs(transLocal);

        for (long _id6 : _ids6) {
            LazyObjectReference _ref6 = transLocal.lazyReferenceFor((int) _id6);
            _ident_Mecz1.add((pl.wcislo.sbql4j.javac.test.football.model.Mecz) _ref6.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Mecz> _whereResult1 =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Mecz>();
        int _whereLoopIndex1 = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Mecz _whereEl1 : _ident_Mecz1) {
            if (_whereEl1 == null) {
                continue;
            }

            if (_whereEl1 != null) {
                ocb.activate(_whereEl1, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _mth_getDruzynaResult1 =
                _whereEl1.getDruzyna();

            if (_mth_getDruzynaResult1 != null) {
                ocb.activate(_mth_getDruzynaResult1, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl1 = _mth_getDruzynaResult1;

            if (_mth_getDruzynaResult1 != null) {
                ocb.activate(_mth_getDruzynaResult1, 2);
            }

            java.lang.String _mth_getNazwaResult1 = _dotEl1.getNazwa();

            if (_mth_getNazwaResult1 != null) {
                ocb.activate(_mth_getNazwaResult1, 1);
            }

            java.lang.String _ident_druzyna1 = druzyna1;
            java.lang.Boolean _equalsResult1 = OperatorUtils.equalsSafe(_mth_getNazwaResult1,
                    _ident_druzyna1);

            if (_equalsResult1) {
                _whereResult1.add(_whereEl1);
            }

            _whereLoopIndex1++;
        }

        java.lang.Integer _countResult1 = _whereResult1.size();
        java.lang.Integer _asResult_d1 = _countResult1;

        if (_asResult_d1 != null) {
            ocb.activate(_asResult_d1, 2);
        }

        java.lang.Integer _ident__aux0 = _dotEl2;

        if (_ident__aux0 != null) {
            ocb.activate(_ident__aux0, 1);
        }

        pl.wcislo.sbql4j.java.model.runtime.Struct _joinResult = OperatorUtils.cartesianProductSS(_asResult_d1,
                _ident__aux0, "d1", "d2");
        pl.wcislo.sbql4j.java.model.runtime.Struct _dotEl3 = _joinResult;
        java.lang.Integer _ident_d1 = (java.lang.Integer) _dotEl3.get("d1");

        if (_ident_d1 != null) {
            ocb.activate(_ident_d1, 1);
        }

        java.lang.Integer _ident_d2 = (java.lang.Integer) _dotEl3.get("d2");

        if (_ident_d2 != null) {
            ocb.activate(_ident_d2, 1);
        }

        java.lang.Boolean _equalsResult2 = OperatorUtils.equalsSafe(_ident_d1,
                _ident_d2);
        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_equalsResult2,
            ocb);

        return _equalsResult2;
    }
}

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


public class KORQueries_SbqlQuery9Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery9Db4o0() {
    }

    /**
     * query='dbConn.( sum PilkarzMecz.getLiczba_czerwonych_kartek() as lss group as _aux0).(( sum PilkarzMecz.getLiczba_zoltych_kartek() as ls group as _aux1).( count(Mecz) as m join _aux1) join _aux0).((ls/ m) as liczbazoltychkartek, (lss/ m) as liczbaczerwonychkartek)'
    '
     **/
    public pl.wcislo.sbql4j.java.model.runtime.Struct executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> _ident_PilkarzMecz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz>();
        ClassMetadata _classMeta11 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz");
        long[] _ids11 = _classMeta11.getIDs(transLocal);

        for (long _id11 : _ids11) {
            LazyObjectReference _ref11 = transLocal.lazyReferenceFor((int) _id11);
            _ident_PilkarzMecz.add((pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz) _ref11.getObject());
        }

        java.util.Collection<java.lang.Integer> _dotResult = new java.util.ArrayList<java.lang.Integer>();
        int _dotIndex = 0;

        if (_ident_PilkarzMecz != null) {
            for (pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz _dotEl : _ident_PilkarzMecz) {
                if (_dotEl == null) {
                    continue;
                }

                if (_dotEl != null) {
                    ocb.activate(_dotEl, 1);
                }

                java.lang.Integer _mth_getLiczba_czerwonych_kartekResult = _dotEl.getLiczba_czerwonych_kartek();

                if (_mth_getLiczba_czerwonych_kartekResult != null) {
                    ocb.activate(_mth_getLiczba_czerwonych_kartekResult, 1);
                }

                if (_mth_getLiczba_czerwonych_kartekResult != null) {
                    ocb.activate(_mth_getLiczba_czerwonych_kartekResult, 1);
                }

                _dotResult.add(_mth_getLiczba_czerwonych_kartekResult);
                _dotIndex++;
            }
        }

        Number _sum0 = null;

        for (Number _sumEl0 : _dotResult) {
            _sum0 = MathUtils.sum(_sum0, _sumEl0);
        }

        java.lang.Integer _sumResult = (java.lang.Integer) _sum0;
        java.lang.Integer _asResult_lss = _sumResult;
        java.lang.Integer _groupAsResult_aux0 = _asResult_lss;
        java.lang.Integer _dotEl3 = _groupAsResult_aux0;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> _ident_PilkarzMecz1 =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz>();
        ClassMetadata _classMeta12 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz");
        long[] _ids12 = _classMeta12.getIDs(transLocal);

        for (long _id12 : _ids12) {
            LazyObjectReference _ref12 = transLocal.lazyReferenceFor((int) _id12);
            _ident_PilkarzMecz1.add((pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz) _ref12.getObject());
        }

        java.util.Collection<java.lang.Integer> _dotResult1 = new java.util.ArrayList<java.lang.Integer>();
        int _dotIndex1 = 0;

        if (_ident_PilkarzMecz1 != null) {
            for (pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz _dotEl1 : _ident_PilkarzMecz1) {
                if (_dotEl1 == null) {
                    continue;
                }

                if (_dotEl1 != null) {
                    ocb.activate(_dotEl1, 1);
                }

                java.lang.Integer _mth_getLiczba_zoltych_kartekResult = _dotEl1.getLiczba_zoltych_kartek();

                if (_mth_getLiczba_zoltych_kartekResult != null) {
                    ocb.activate(_mth_getLiczba_zoltych_kartekResult, 1);
                }

                if (_mth_getLiczba_zoltych_kartekResult != null) {
                    ocb.activate(_mth_getLiczba_zoltych_kartekResult, 1);
                }

                _dotResult1.add(_mth_getLiczba_zoltych_kartekResult);
                _dotIndex1++;
            }
        }

        Number _sum1 = null;

        for (Number _sumEl1 : _dotResult1) {
            _sum1 = MathUtils.sum(_sum1, _sumEl1);
        }

        java.lang.Integer _sumResult1 = (java.lang.Integer) _sum1;
        java.lang.Integer _asResult_ls = _sumResult1;
        java.lang.Integer _groupAsResult_aux1 = _asResult_ls;
        java.lang.Integer _dotEl2 = _groupAsResult_aux1;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Mecz> _ident_Mecz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Mecz>();
        ClassMetadata _classMeta13 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Mecz");
        long[] _ids13 = _classMeta13.getIDs(transLocal);

        for (long _id13 : _ids13) {
            LazyObjectReference _ref13 = transLocal.lazyReferenceFor((int) _id13);
            _ident_Mecz.add((pl.wcislo.sbql4j.javac.test.football.model.Mecz) _ref13.getObject());
        }

        java.lang.Integer _countResult = _ident_Mecz.size();
        java.lang.Integer _asResult_m = _countResult;

        if (_asResult_m != null) {
            ocb.activate(_asResult_m, 2);
        }

        java.lang.Integer _ident__aux1 = _dotEl2;

        if (_ident__aux1 != null) {
            ocb.activate(_ident__aux1, 1);
        }

        pl.wcislo.sbql4j.java.model.runtime.Struct _joinResult = OperatorUtils.cartesianProductSS(_asResult_m,
                _ident__aux1, "m", "ls");

        if (_joinResult != null) {
            ocb.activate(_joinResult, 2);
        }

        java.lang.Integer _ident__aux0 = _dotEl3;

        if (_ident__aux0 != null) {
            ocb.activate(_ident__aux0, 1);
        }

        pl.wcislo.sbql4j.java.model.runtime.Struct _joinResult1 = OperatorUtils.cartesianProductSS(_joinResult,
                _ident__aux0, "", "lss");
        pl.wcislo.sbql4j.java.model.runtime.Struct _dotEl4 = _joinResult1;
        java.lang.Integer _ident_ls = (java.lang.Integer) _dotEl4.get("ls");

        if (_ident_ls != null) {
            ocb.activate(_ident_ls, 1);
        }

        java.lang.Integer _ident_m = (java.lang.Integer) _dotEl4.get("m");

        if (_ident_m != null) {
            ocb.activate(_ident_m, 1);
        }

        java.lang.Integer _divideResult = _ident_ls / _ident_m;
        java.lang.Integer _asResult_liczbazoltychkartek = _divideResult;
        java.lang.Integer _ident_lss = (java.lang.Integer) _dotEl4.get("lss");

        if (_ident_lss != null) {
            ocb.activate(_ident_lss, 1);
        }

        java.lang.Integer _ident_m1 = (java.lang.Integer) _dotEl4.get("m");

        if (_ident_m1 != null) {
            ocb.activate(_ident_m1, 1);
        }

        java.lang.Integer _divideResult1 = _ident_lss / _ident_m1;
        java.lang.Integer _asResult_liczbaczerwonychkartek = _divideResult1;
        pl.wcislo.sbql4j.java.model.runtime.Struct _commaResult = OperatorUtils.cartesianProductSS(_asResult_liczbazoltychkartek,
                _asResult_liczbaczerwonychkartek, "liczbazoltychkartek",
                "liczbaczerwonychkartek");
        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_commaResult, ocb);

        return _commaResult;
    }
}

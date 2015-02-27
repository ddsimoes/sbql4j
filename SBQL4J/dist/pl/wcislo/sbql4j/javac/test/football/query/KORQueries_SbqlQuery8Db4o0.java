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


public class KORQueries_SbqlQuery8Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery8Db4o0() {
    }

    /**
     * query='dbConn.( max(Pilkarz.getPremia()) as premia group as _aux0).( max(Pilkarz.getPensja()) as pensja join _aux0).(pensja + premia)'
    '
     **/
    public java.lang.Integer executeQuery(final ObjectContainerBase ocb,
        final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _ident_Pilkarz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz>();
        ClassMetadata _classMeta9 = ocb.classCollection()
                                       .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Pilkarz");
        long[] _ids9 = _classMeta9.getIDs(transLocal);

        for (long _id9 : _ids9) {
            LazyObjectReference _ref9 = transLocal.lazyReferenceFor((int) _id9);
            _ident_Pilkarz.add((pl.wcislo.sbql4j.javac.test.football.model.Pilkarz) _ref9.getObject());
        }

        java.util.Collection<java.lang.Integer> _dotResult = new java.util.ArrayList<java.lang.Integer>();
        int _dotIndex = 0;

        if (_ident_Pilkarz != null) {
            for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl : _ident_Pilkarz) {
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

        Number _max2 = null;

        for (Number _maxEl2 : _dotResult) {
            _max2 = MathUtils.max(_max2, _maxEl2);
        }

        java.lang.Integer _maxResult = (java.lang.Integer) _max2;
        java.lang.Integer _asResult_premia = _maxResult;
        java.lang.Integer _groupAsResult_aux0 = _asResult_premia;
        java.lang.Integer _dotEl2 = _groupAsResult_aux0;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _ident_Pilkarz1 =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz>();
        ClassMetadata _classMeta10 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Pilkarz");
        long[] _ids10 = _classMeta10.getIDs(transLocal);

        for (long _id10 : _ids10) {
            LazyObjectReference _ref10 = transLocal.lazyReferenceFor((int) _id10);
            _ident_Pilkarz1.add((pl.wcislo.sbql4j.javac.test.football.model.Pilkarz) _ref10.getObject());
        }

        java.util.Collection<java.lang.Integer> _dotResult1 = new java.util.ArrayList<java.lang.Integer>();
        int _dotIndex1 = 0;

        if (_ident_Pilkarz1 != null) {
            for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl1 : _ident_Pilkarz1) {
                if (_dotEl1 == null) {
                    continue;
                }

                if (_dotEl1 != null) {
                    ocb.activate(_dotEl1, 1);
                }

                java.lang.Integer _mth_getPensjaResult = _dotEl1.getPensja();

                if (_mth_getPensjaResult != null) {
                    ocb.activate(_mth_getPensjaResult, 1);
                }

                if (_mth_getPensjaResult != null) {
                    ocb.activate(_mth_getPensjaResult, 1);
                }

                _dotResult1.add(_mth_getPensjaResult);
                _dotIndex1++;
            }
        }

        Number _max3 = null;

        for (Number _maxEl3 : _dotResult1) {
            _max3 = MathUtils.max(_max3, _maxEl3);
        }

        java.lang.Integer _maxResult1 = (java.lang.Integer) _max3;
        java.lang.Integer _asResult_pensja = _maxResult1;

        if (_asResult_pensja != null) {
            ocb.activate(_asResult_pensja, 2);
        }

        java.lang.Integer _ident__aux0 = _dotEl2;

        if (_ident__aux0 != null) {
            ocb.activate(_ident__aux0, 1);
        }

        pl.wcislo.sbql4j.java.model.runtime.Struct _joinResult = OperatorUtils.cartesianProductSS(_asResult_pensja,
                _ident__aux0, "pensja", "premia");
        pl.wcislo.sbql4j.java.model.runtime.Struct _dotEl3 = _joinResult;
        java.lang.Integer _ident_pensja = (java.lang.Integer) _dotEl3.get(
                "pensja");

        if (_ident_pensja != null) {
            ocb.activate(_ident_pensja, 1);
        }

        java.lang.Integer _ident_premia = (java.lang.Integer) _dotEl3.get(
                "premia");

        if (_ident_premia != null) {
            ocb.activate(_ident_premia, 1);
        }

        java.lang.Integer _plusResult = _ident_pensja + _ident_premia;
        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_plusResult, ocb);

        return _plusResult;
    }
}

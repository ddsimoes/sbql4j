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


public class KORQueries_SbqlQuery22Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery22Db4o0() {
    }

    /**
     * query='dbConn.((0 as i close by (i + 1 where i <=  max(PilkarzMecz.getLiczba_czerwonych_kartek())) as i join  count((PilkarzMecz where getLiczba_czerwonych_kartek() >= i and getLiczba_czerwonych_kartek() < i + 1)) as c).(c + " Pilkarzy zdobylo czerwonych kartek  pomiedzy  " + i + " and " + i + 1) as message)'
    '
     **/
    public java.util.List<java.lang.String> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        java.lang.Integer _asResult_i = 0;
        java.util.List<java.lang.Integer> _closeByResult = new ArrayList<java.lang.Integer>();
        _closeByResult.add(_asResult_i);

        int _i2 = 0;

        while (_i2 < _closeByResult.size()) {
            java.lang.Integer _closeByEl = _closeByResult.get(_i2);

            if (_closeByEl != null) {
                ocb.activate(_closeByEl, 1);
            }

            java.lang.Integer _ident_i = _closeByEl;
            java.lang.Integer _plusResult = _ident_i + 1;

            if (_plusResult != null) {
                ocb.activate(_plusResult, 2);
            }

            java.lang.Integer _whereEl = _plusResult;
            java.lang.Integer _ident_i1 = _closeByEl;
            final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> _ident_PilkarzMecz =
                new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz>();
            ClassMetadata _classMeta29 = ocb.classCollection()
                                            .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz");
            long[] _ids29 = _classMeta29.getIDs(transLocal);

            for (long _id29 : _ids29) {
                LazyObjectReference _ref29 = transLocal.lazyReferenceFor((int) _id29);
                _ident_PilkarzMecz.add((pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz) _ref29.getObject());
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

            Number _max6 = null;

            for (Number _maxEl6 : _dotResult) {
                _max6 = MathUtils.max(_max6, _maxEl6);
            }

            java.lang.Integer _maxResult = (java.lang.Integer) _max6;

            Boolean _less_or_equalResult = _ident_i1 <= _maxResult;
            java.lang.Integer _whereResult = null;

            if (_less_or_equalResult) {
                _whereResult = _plusResult;
            }

            java.lang.Integer _asResult_i1 = _whereResult;

            if (_asResult_i1 != null) {
                _closeByResult.add(_asResult_i1);
            }

            _i2++;
        }

        java.util.List<pl.wcislo.sbql4j.java.model.runtime.Struct> _joinResult = new java.util.ArrayList<pl.wcislo.sbql4j.java.model.runtime.Struct>();
        int _joinIndex = 0;

        for (java.lang.Integer _joinEl : _closeByResult) {
            if (_joinEl != null) {
                ocb.activate(_joinEl, 1);
            }

            final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> _ident_PilkarzMecz1 =
                new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz>();
            ClassMetadata _classMeta30 = ocb.classCollection()
                                            .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz");
            long[] _ids30 = _classMeta30.getIDs(transLocal);

            for (long _id30 : _ids30) {
                LazyObjectReference _ref30 = transLocal.lazyReferenceFor((int) _id30);
                _ident_PilkarzMecz1.add((pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz) _ref30.getObject());
            }

            java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> _whereResult1 =
                new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz>();
            int _whereLoopIndex1 = 0;

            for (pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz _whereEl1 : _ident_PilkarzMecz1) {
                if (_whereEl1 == null) {
                    continue;
                }

                if (_whereEl1 != null) {
                    ocb.activate(_whereEl1, 1);
                }

                java.lang.Integer _mth_getLiczba_czerwonych_kartekResult1 = _whereEl1.getLiczba_czerwonych_kartek();

                if (_mth_getLiczba_czerwonych_kartekResult1 != null) {
                    ocb.activate(_mth_getLiczba_czerwonych_kartekResult1, 1);
                }

                java.lang.Integer _ident_i2 = _joinEl;

                if (_ident_i2 != null) {
                    ocb.activate(_ident_i2, 1);
                }

                Boolean _more_or_equalResult = _mth_getLiczba_czerwonych_kartekResult1 >= _ident_i2;
                java.lang.Boolean _andResult;

                if (!_more_or_equalResult) {
                    _andResult = false;
                } else {
                    java.lang.Integer _mth_getLiczba_czerwonych_kartekResult2 = _whereEl1.getLiczba_czerwonych_kartek();

                    if (_mth_getLiczba_czerwonych_kartekResult2 != null) {
                        ocb.activate(_mth_getLiczba_czerwonych_kartekResult2, 1);
                    }

                    java.lang.Integer _ident_i3 = _joinEl;

                    if (_ident_i3 != null) {
                        ocb.activate(_ident_i3, 1);
                    }

                    java.lang.Integer _plusResult1 = _ident_i3 + 1;

                    Boolean _lessResult = _mth_getLiczba_czerwonych_kartekResult2 < _plusResult1;
                    _andResult = _lessResult;
                }

                if (_andResult) {
                    _whereResult1.add(_whereEl1);
                }

                _whereLoopIndex1++;
            }

            java.lang.Integer _countResult = _whereResult1.size();
            java.lang.Integer _asResult_c = _countResult;
            _joinResult.add(OperatorUtils.cartesianProductSS(_joinEl,
                    _asResult_c, "i", "c"));
            _joinIndex++;
        }

        java.util.List<java.lang.String> _dotResult1 = new java.util.ArrayList<java.lang.String>();
        int _dotIndex1 = 0;

        if (_joinResult != null) {
            for (pl.wcislo.sbql4j.java.model.runtime.Struct _dotEl1 : _joinResult) {
                if (_dotEl1 == null) {
                    continue;
                }

                if (_dotEl1 != null) {
                    ocb.activate(_dotEl1, 1);
                }

                java.lang.Integer _ident_c = (java.lang.Integer) _dotEl1.get(
                        "c");

                if (_ident_c != null) {
                    ocb.activate(_ident_c, 1);
                }

                java.lang.String _plusResult2 = _ident_c +
                    " Pilkarzy zdobylo czerwonych kartek  pomiedzy  ";
                java.lang.Integer _ident_i4 = (java.lang.Integer) _dotEl1.get(
                        "i");

                if (_ident_i4 != null) {
                    ocb.activate(_ident_i4, 1);
                }

                java.lang.String _plusResult3 = _plusResult2 + _ident_i4;
                java.lang.String _plusResult4 = _plusResult3 + " and ";
                java.lang.Integer _ident_i5 = (java.lang.Integer) _dotEl1.get(
                        "i");

                if (_ident_i5 != null) {
                    ocb.activate(_ident_i5, 1);
                }

                java.lang.Integer _plusResult5 = _ident_i5 + 1;
                java.lang.String _plusResult6 = _plusResult4 + _plusResult5;

                if (_plusResult6 != null) {
                    ocb.activate(_plusResult6, 1);
                }

                _dotResult1.add(_plusResult6);
                _dotIndex1++;
            }
        }

        java.util.List<java.lang.String> _asResult_message = _dotResult1;
        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_asResult_message,
            ocb);

        return _asResult_message;
    }
}

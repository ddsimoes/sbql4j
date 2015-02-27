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


public class KORQueries_SbqlQuery23Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery23Db4o0() {
    }

    /**
     * query='dbConn.((0 as i close by (i + 100 where i <=  max(Trener.getPremia())) as i join  count((Trener where getPremia() >= i and getPremia() < i + 100)) as c).(c + " Trenerow uzyskuje premie pomiedzy  " + i + " i " + i + 100) as message)'
    '
     **/
    public java.util.List<java.lang.String> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        java.lang.Integer _asResult_i = 0;
        java.util.List<java.lang.Integer> _closeByResult = new ArrayList<java.lang.Integer>();
        _closeByResult.add(_asResult_i);

        int _i3 = 0;

        while (_i3 < _closeByResult.size()) {
            java.lang.Integer _closeByEl = _closeByResult.get(_i3);

            if (_closeByEl != null) {
                ocb.activate(_closeByEl, 1);
            }

            java.lang.Integer _ident_i = _closeByEl;
            java.lang.Integer _plusResult = _ident_i + 100;

            if (_plusResult != null) {
                ocb.activate(_plusResult, 2);
            }

            java.lang.Integer _whereEl = _plusResult;
            java.lang.Integer _ident_i1 = _closeByEl;
            final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Trener> _ident_Trener =
                new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Trener>();
            ClassMetadata _classMeta31 = ocb.classCollection()
                                            .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Trener");
            long[] _ids31 = _classMeta31.getIDs(transLocal);

            for (long _id31 : _ids31) {
                LazyObjectReference _ref31 = transLocal.lazyReferenceFor((int) _id31);
                _ident_Trener.add((pl.wcislo.sbql4j.javac.test.football.model.Trener) _ref31.getObject());
            }

            java.util.Collection<java.lang.Integer> _dotResult = new java.util.ArrayList<java.lang.Integer>();
            int _dotIndex = 0;

            if (_ident_Trener != null) {
                for (pl.wcislo.sbql4j.javac.test.football.model.Trener _dotEl : _ident_Trener) {
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

            Number _max7 = null;

            for (Number _maxEl7 : _dotResult) {
                _max7 = MathUtils.max(_max7, _maxEl7);
            }

            java.lang.Integer _maxResult = (java.lang.Integer) _max7;

            Boolean _less_or_equalResult = _ident_i1 <= _maxResult;
            java.lang.Integer _whereResult = null;

            if (_less_or_equalResult) {
                _whereResult = _plusResult;
            }

            java.lang.Integer _asResult_i1 = _whereResult;

            if (_asResult_i1 != null) {
                _closeByResult.add(_asResult_i1);
            }

            _i3++;
        }

        java.util.List<pl.wcislo.sbql4j.java.model.runtime.Struct> _joinResult = new java.util.ArrayList<pl.wcislo.sbql4j.java.model.runtime.Struct>();
        int _joinIndex = 0;

        for (java.lang.Integer _joinEl : _closeByResult) {
            if (_joinEl != null) {
                ocb.activate(_joinEl, 1);
            }

            final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Trener> _ident_Trener1 =
                new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Trener>();
            ClassMetadata _classMeta32 = ocb.classCollection()
                                            .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Trener");
            long[] _ids32 = _classMeta32.getIDs(transLocal);

            for (long _id32 : _ids32) {
                LazyObjectReference _ref32 = transLocal.lazyReferenceFor((int) _id32);
                _ident_Trener1.add((pl.wcislo.sbql4j.javac.test.football.model.Trener) _ref32.getObject());
            }

            java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Trener> _whereResult1 =
                new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Trener>();
            int _whereLoopIndex1 = 0;

            for (pl.wcislo.sbql4j.javac.test.football.model.Trener _whereEl1 : _ident_Trener1) {
                if (_whereEl1 == null) {
                    continue;
                }

                if (_whereEl1 != null) {
                    ocb.activate(_whereEl1, 1);
                }

                java.lang.Integer _mth_getPremiaResult1 = _whereEl1.getPremia();

                if (_mth_getPremiaResult1 != null) {
                    ocb.activate(_mth_getPremiaResult1, 1);
                }

                java.lang.Integer _ident_i2 = _joinEl;

                if (_ident_i2 != null) {
                    ocb.activate(_ident_i2, 1);
                }

                Boolean _more_or_equalResult = _mth_getPremiaResult1 >= _ident_i2;
                java.lang.Boolean _andResult;

                if (!_more_or_equalResult) {
                    _andResult = false;
                } else {
                    java.lang.Integer _mth_getPremiaResult2 = _whereEl1.getPremia();

                    if (_mth_getPremiaResult2 != null) {
                        ocb.activate(_mth_getPremiaResult2, 1);
                    }

                    java.lang.Integer _ident_i3 = _joinEl;

                    if (_ident_i3 != null) {
                        ocb.activate(_ident_i3, 1);
                    }

                    java.lang.Integer _plusResult1 = _ident_i3 + 100;

                    Boolean _lessResult = _mth_getPremiaResult2 < _plusResult1;
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
                    " Trenerow uzyskuje premie pomiedzy  ";
                java.lang.Integer _ident_i4 = (java.lang.Integer) _dotEl1.get(
                        "i");

                if (_ident_i4 != null) {
                    ocb.activate(_ident_i4, 1);
                }

                java.lang.String _plusResult3 = _plusResult2 + _ident_i4;
                java.lang.String _plusResult4 = _plusResult3 + " i ";
                java.lang.Integer _ident_i5 = (java.lang.Integer) _dotEl1.get(
                        "i");

                if (_ident_i5 != null) {
                    ocb.activate(_ident_i5, 1);
                }

                java.lang.Integer _plusResult5 = _ident_i5 + 100;
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

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


public class KORQueries_SbqlQuery0Db4o0 implements Db4oSbqlQuery {
    private java.lang.String u;
    private java.lang.String ut;

    public KORQueries_SbqlQuery0Db4o0(java.lang.String u, java.lang.String ut) {
        this.u = u;
        this.ut = ut;
    }

    /**
     * query='dbConn.((Pilkarz as d where d.getImie() == u and d.getNazwisko() == ut) order by (d.getDruzyna().getNazwa()))'
    '
     **/
    public java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _ident_Pilkarz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz>();
        ClassMetadata _classMeta0 = ocb.classCollection()
                                       .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Pilkarz");
        long[] _ids0 = _classMeta0.getIDs(transLocal);

        for (long _id0 : _ids0) {
            LazyObjectReference _ref0 = transLocal.lazyReferenceFor((int) _id0);
            _ident_Pilkarz.add((pl.wcislo.sbql4j.javac.test.football.model.Pilkarz) _ref0.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _asResult_d =
            _ident_Pilkarz;
        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _whereResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz>();
        int _whereLoopIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _whereEl : _asResult_d) {
            if (_whereEl == null) {
                continue;
            }

            if (_whereEl != null) {
                ocb.activate(_whereEl, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _ident_d = _whereEl;

            if (_ident_d != null) {
                ocb.activate(_ident_d, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl = _ident_d;

            if (_ident_d != null) {
                ocb.activate(_ident_d, 2);
            }

            java.lang.String _mth_getImieResult = _dotEl.getImie();

            if (_mth_getImieResult != null) {
                ocb.activate(_mth_getImieResult, 1);
            }

            java.lang.String _ident_u = u;
            java.lang.Boolean _equalsResult = OperatorUtils.equalsSafe(_mth_getImieResult,
                    _ident_u);
            java.lang.Boolean _andResult;

            if (!_equalsResult) {
                _andResult = false;
            } else {
                pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _ident_d1 = _whereEl;

                if (_ident_d1 != null) {
                    ocb.activate(_ident_d1, 1);
                }

                pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl1 = _ident_d1;

                if (_ident_d1 != null) {
                    ocb.activate(_ident_d1, 2);
                }

                java.lang.String _mth_getNazwiskoResult = _dotEl1.getNazwisko();

                if (_mth_getNazwiskoResult != null) {
                    ocb.activate(_mth_getNazwiskoResult, 1);
                }

                java.lang.String _ident_ut = ut;
                java.lang.Boolean _equalsResult1 = OperatorUtils.equalsSafe(_mth_getNazwiskoResult,
                        _ident_ut);
                _andResult = _equalsResult1;
            }

            if (_andResult) {
                _whereResult.add(_whereEl);
            }

            _whereLoopIndex++;
        }

        java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _orderByResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz>();

        if (_whereResult != null) {
            ocb.activate(_whereResult, 2);
        }

        _orderByResult.addAll(_whereResult);

        Comparator<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _comparator0 =
            new Comparator<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz>() {
                public int compare(
                    pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _leftObj,
                    pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _rightObj) {
                    if (_leftObj == null) {
                        return -1;
                    }

                    if (_leftObj != null) {
                        ocb.activate(_leftObj, 1);
                    }

                    if (_rightObj != null) {
                        ocb.activate(_rightObj, 1);
                    }

                    int res = 0;
                    java.lang.String _leftParam0;

                    {
                        pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _ident_d2 =
                            _leftObj;

                        if (_ident_d2 != null) {
                            ocb.activate(_ident_d2, 1);
                        }

                        pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl2 =
                            _ident_d2;

                        if (_ident_d2 != null) {
                            ocb.activate(_ident_d2, 2);
                        }

                        pl.wcislo.sbql4j.javac.test.football.model.Druzyna _mth_getDruzynaResult =
                            _dotEl2.getDruzyna();

                        if (_mth_getDruzynaResult != null) {
                            ocb.activate(_mth_getDruzynaResult, 1);
                        }

                        pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl3 =
                            _mth_getDruzynaResult;

                        if (_mth_getDruzynaResult != null) {
                            ocb.activate(_mth_getDruzynaResult, 2);
                        }

                        java.lang.String _mth_getNazwaResult = _dotEl3.getNazwa();

                        if (_mth_getNazwaResult != null) {
                            ocb.activate(_mth_getNazwaResult, 1);
                        }

                        _leftParam0 = _mth_getNazwaResult;
                    }

                    java.lang.String _rightParam0;

                    {
                        pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _ident_d2 =
                            _rightObj;

                        if (_ident_d2 != null) {
                            ocb.activate(_ident_d2, 1);
                        }

                        pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl2 =
                            _ident_d2;

                        if (_ident_d2 != null) {
                            ocb.activate(_ident_d2, 2);
                        }

                        pl.wcislo.sbql4j.javac.test.football.model.Druzyna _mth_getDruzynaResult =
                            _dotEl2.getDruzyna();

                        if (_mth_getDruzynaResult != null) {
                            ocb.activate(_mth_getDruzynaResult, 1);
                        }

                        pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl3 =
                            _mth_getDruzynaResult;

                        if (_mth_getDruzynaResult != null) {
                            ocb.activate(_mth_getDruzynaResult, 2);
                        }

                        java.lang.String _mth_getNazwaResult = _dotEl3.getNazwa();

                        if (_mth_getNazwaResult != null) {
                            ocb.activate(_mth_getNazwaResult, 1);
                        }

                        _rightParam0 = _mth_getNazwaResult;
                    }

                    if (_leftParam0 != null) {
                        res = _leftParam0.compareTo(_rightParam0);
                    } else {
                        return -1;
                    }

                    return res;
                }
            };

        Collections.sort(_orderByResult, _comparator0);
        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_orderByResult,
            ocb);

        return _orderByResult;
    }
}

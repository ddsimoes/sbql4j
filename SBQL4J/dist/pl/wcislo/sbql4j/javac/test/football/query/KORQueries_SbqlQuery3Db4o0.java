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


public class KORQueries_SbqlQuery3Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery3Db4o0() {
    }

    /**
     * query='dbConn.(Pilkarz as d order by (d.getDruzyna().getNazwa()))'
    '
     **/
    public java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _ident_Pilkarz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz>();
        ClassMetadata _classMeta3 = ocb.classCollection()
                                       .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Pilkarz");
        long[] _ids3 = _classMeta3.getIDs(transLocal);

        for (long _id3 : _ids3) {
            LazyObjectReference _ref3 = transLocal.lazyReferenceFor((int) _id3);
            _ident_Pilkarz.add((pl.wcislo.sbql4j.javac.test.football.model.Pilkarz) _ref3.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _asResult_d =
            _ident_Pilkarz;
        java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _orderByResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz>();

        if (_asResult_d != null) {
            ocb.activate(_asResult_d, 2);
        }

        _orderByResult.addAll(_asResult_d);

        Comparator<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _comparator3 =
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
                        pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _ident_d =
                            _leftObj;

                        if (_ident_d != null) {
                            ocb.activate(_ident_d, 1);
                        }

                        pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl =
                            _ident_d;

                        if (_ident_d != null) {
                            ocb.activate(_ident_d, 2);
                        }

                        pl.wcislo.sbql4j.javac.test.football.model.Druzyna _mth_getDruzynaResult =
                            _dotEl.getDruzyna();

                        if (_mth_getDruzynaResult != null) {
                            ocb.activate(_mth_getDruzynaResult, 1);
                        }

                        pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl1 =
                            _mth_getDruzynaResult;

                        if (_mth_getDruzynaResult != null) {
                            ocb.activate(_mth_getDruzynaResult, 2);
                        }

                        java.lang.String _mth_getNazwaResult = _dotEl1.getNazwa();

                        if (_mth_getNazwaResult != null) {
                            ocb.activate(_mth_getNazwaResult, 1);
                        }

                        _leftParam0 = _mth_getNazwaResult;
                    }

                    java.lang.String _rightParam0;

                    {
                        pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _ident_d =
                            _rightObj;

                        if (_ident_d != null) {
                            ocb.activate(_ident_d, 1);
                        }

                        pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl =
                            _ident_d;

                        if (_ident_d != null) {
                            ocb.activate(_ident_d, 2);
                        }

                        pl.wcislo.sbql4j.javac.test.football.model.Druzyna _mth_getDruzynaResult =
                            _dotEl.getDruzyna();

                        if (_mth_getDruzynaResult != null) {
                            ocb.activate(_mth_getDruzynaResult, 1);
                        }

                        pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl1 =
                            _mth_getDruzynaResult;

                        if (_mth_getDruzynaResult != null) {
                            ocb.activate(_mth_getDruzynaResult, 2);
                        }

                        java.lang.String _mth_getNazwaResult = _dotEl1.getNazwa();

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

        Collections.sort(_orderByResult, _comparator3);
        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_orderByResult,
            ocb);

        return _orderByResult;
    }
}

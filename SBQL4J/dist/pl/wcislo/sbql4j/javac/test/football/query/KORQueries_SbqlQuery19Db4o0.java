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


public class KORQueries_SbqlQuery19Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery19Db4o0() {
    }

    /**
     * query='dbConn.(PilkarzMecz as pm group as _aux0).(Druzyna as druzyna join  sum (_aux0 where pm.getPilkarz() in druzyna.getLista_zawodnikow()).(pm.getLiczba_zoltych_kartek() + pm.getLiczba_czerwonych_kartek()) as liczbakartek)'
    '
     **/
    public java.util.Collection<pl.wcislo.sbql4j.java.model.runtime.Struct> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> _ident_PilkarzMecz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz>();
        ClassMetadata _classMeta23 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz");
        long[] _ids23 = _classMeta23.getIDs(transLocal);

        for (long _id23 : _ids23) {
            LazyObjectReference _ref23 = transLocal.lazyReferenceFor((int) _id23);
            _ident_PilkarzMecz.add((pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz) _ref23.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> _asResult_pm =
            _ident_PilkarzMecz;
        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> _groupAsResult_aux0 =
            _asResult_pm;
        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> _dotEl5 =
            _groupAsResult_aux0;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _ident_Druzyna =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Druzyna>();
        ClassMetadata _classMeta24 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Druzyna");
        long[] _ids24 = _classMeta24.getIDs(transLocal);

        for (long _id24 : _ids24) {
            LazyObjectReference _ref24 = transLocal.lazyReferenceFor((int) _id24);
            _ident_Druzyna.add((pl.wcislo.sbql4j.javac.test.football.model.Druzyna) _ref24.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _asResult_druzyna =
            _ident_Druzyna;
        java.util.Collection<pl.wcislo.sbql4j.java.model.runtime.Struct> _joinResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.java.model.runtime.Struct>();
        int _joinIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Druzyna _joinEl : _asResult_druzyna) {
            if (_joinEl != null) {
                ocb.activate(_joinEl, 1);
            }

            java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> _ident__aux0 =
                _dotEl5;

            if (_ident__aux0 != null) {
                ocb.activate(_ident__aux0, 2);
            }

            java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz> _whereResult =
                new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz>();
            int _whereLoopIndex = 0;

            for (pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz _whereEl : _ident__aux0) {
                if (_whereEl == null) {
                    continue;
                }

                if (_whereEl != null) {
                    ocb.activate(_whereEl, 1);
                }

                pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz _ident_pm =
                    _whereEl;

                if (_ident_pm != null) {
                    ocb.activate(_ident_pm, 1);
                }

                pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz _dotEl = _ident_pm;

                if (_ident_pm != null) {
                    ocb.activate(_ident_pm, 2);
                }

                pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _mth_getPilkarzResult =
                    _dotEl.getPilkarz();

                if (_mth_getPilkarzResult != null) {
                    ocb.activate(_mth_getPilkarzResult, 1);
                }

                pl.wcislo.sbql4j.javac.test.football.model.Druzyna _ident_druzyna =
                    _joinEl;

                if (_ident_druzyna != null) {
                    ocb.activate(_ident_druzyna, 1);
                }

                pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl1 = _ident_druzyna;

                if (_ident_druzyna != null) {
                    ocb.activate(_ident_druzyna, 2);
                }

                java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _mth_getLista_zawodnikowResult =
                    _dotEl1.getLista_zawodnikow();

                if (_mth_getLista_zawodnikowResult != null) {
                    ocb.activate(_mth_getLista_zawodnikowResult, 2);
                }

                Collection _inLeftCol0 = new ArrayList();
                _inLeftCol0.add(_mth_getPilkarzResult);

                Collection _inRightCol0 = new ArrayList(_mth_getLista_zawodnikowResult);
                java.lang.Boolean _inResult = _inRightCol0.containsAll(_inLeftCol0);

                if (_inResult) {
                    _whereResult.add(_whereEl);
                }

                _whereLoopIndex++;
            }

            java.util.Collection<java.lang.Integer> _dotResult4 = new java.util.ArrayList<java.lang.Integer>();
            int _dotIndex4 = 0;

            if (_whereResult != null) {
                for (pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz _dotEl4 : _whereResult) {
                    if (_dotEl4 == null) {
                        continue;
                    }

                    if (_dotEl4 != null) {
                        ocb.activate(_dotEl4, 1);
                    }

                    pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz _ident_pm1 =
                        _dotEl4;

                    if (_ident_pm1 != null) {
                        ocb.activate(_ident_pm1, 1);
                    }

                    pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz _dotEl2 =
                        _ident_pm1;

                    if (_ident_pm1 != null) {
                        ocb.activate(_ident_pm1, 2);
                    }

                    java.lang.Integer _mth_getLiczba_zoltych_kartekResult = _dotEl2.getLiczba_zoltych_kartek();

                    if (_mth_getLiczba_zoltych_kartekResult != null) {
                        ocb.activate(_mth_getLiczba_zoltych_kartekResult, 1);
                    }

                    pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz _ident_pm2 =
                        _dotEl4;

                    if (_ident_pm2 != null) {
                        ocb.activate(_ident_pm2, 1);
                    }

                    pl.wcislo.sbql4j.javac.test.football.model.PilkarzMecz _dotEl3 =
                        _ident_pm2;

                    if (_ident_pm2 != null) {
                        ocb.activate(_ident_pm2, 2);
                    }

                    java.lang.Integer _mth_getLiczba_czerwonych_kartekResult = _dotEl3.getLiczba_czerwonych_kartek();

                    if (_mth_getLiczba_czerwonych_kartekResult != null) {
                        ocb.activate(_mth_getLiczba_czerwonych_kartekResult, 1);
                    }

                    java.lang.Integer _plusResult = _mth_getLiczba_zoltych_kartekResult +
                        _mth_getLiczba_czerwonych_kartekResult;

                    if (_plusResult != null) {
                        ocb.activate(_plusResult, 1);
                    }

                    _dotResult4.add(_plusResult);
                    _dotIndex4++;
                }
            }

            Number _sum2 = null;

            for (Number _sumEl2 : _dotResult4) {
                _sum2 = MathUtils.sum(_sum2, _sumEl2);
            }

            java.lang.Integer _sumResult = (java.lang.Integer) _sum2;
            java.lang.Integer _asResult_liczbakartek = _sumResult;
            _joinResult.add(OperatorUtils.cartesianProductSS(_joinEl,
                    _asResult_liczbakartek, "druzyna", "liczbakartek"));
            _joinIndex++;
        }

        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_joinResult, ocb);

        return _joinResult;
    }
}

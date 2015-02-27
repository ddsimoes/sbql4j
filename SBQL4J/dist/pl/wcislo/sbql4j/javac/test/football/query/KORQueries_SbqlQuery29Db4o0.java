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


public class KORQueries_SbqlQuery29Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery29Db4o0() {
    }

    /**
     * query='dbConn.( all Druzyna as d  any d.getLista_zawodnikow() as e e.getPremia() == 0)'
    '
     **/
    public java.lang.Boolean executeQuery(final ObjectContainerBase ocb,
        final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _ident_Druzyna =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Druzyna>();
        ClassMetadata _classMeta41 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Druzyna");
        long[] _ids41 = _classMeta41.getIDs(transLocal);

        for (long _id41 : _ids41) {
            LazyObjectReference _ref41 = transLocal.lazyReferenceFor((int) _id41);
            _ident_Druzyna.add((pl.wcislo.sbql4j.javac.test.football.model.Druzyna) _ref41.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Druzyna> _asResult_d =
            _ident_Druzyna;
        java.lang.Boolean _allResult = true;
        Integer _allIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Druzyna _allEl : _asResult_d) {
            if (_allEl != null) {
                ocb.activate(_allEl, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _ident_d = _allEl;

            if (_ident_d != null) {
                ocb.activate(_ident_d, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Druzyna _dotEl = _ident_d;

            if (_ident_d != null) {
                ocb.activate(_ident_d, 2);
            }

            java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _mth_getLista_zawodnikowResult =
                _dotEl.getLista_zawodnikow();

            if (_mth_getLista_zawodnikowResult != null) {
                ocb.activate(_mth_getLista_zawodnikowResult, 2);
            }

            java.util.List<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _asResult_e =
                _mth_getLista_zawodnikowResult;
            java.lang.Boolean _anyResult = false;
            Integer _anyIndex = 0;

            for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _anyEl : _asResult_e) {
                if (_anyEl != null) {
                    ocb.activate(_anyEl, 1);
                }

                pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _ident_e = _anyEl;

                if (_ident_e != null) {
                    ocb.activate(_ident_e, 1);
                }

                pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl1 = _ident_e;

                if (_ident_e != null) {
                    ocb.activate(_ident_e, 2);
                }

                java.lang.Integer _mth_getPremiaResult = _dotEl1.getPremia();

                if (_mth_getPremiaResult != null) {
                    ocb.activate(_mth_getPremiaResult, 1);
                }

                java.lang.Boolean _equalsResult = OperatorUtils.equalsSafe(_mth_getPremiaResult,
                        0);

                if (_equalsResult) {
                    _anyResult = true;

                    break;
                }
            }

            if (!_anyResult) {
                _allResult = false;

                break;
            }
        }

        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_allResult, ocb);

        return _allResult;
    }
}

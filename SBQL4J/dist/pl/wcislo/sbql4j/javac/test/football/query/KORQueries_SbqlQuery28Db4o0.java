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


public class KORQueries_SbqlQuery28Db4o0 implements Db4oSbqlQuery {
    public KORQueries_SbqlQuery28Db4o0() {
    }

    /**
     * query='dbConn.(Pilkarz as pilkarz group as _aux0).(Zabieg as zabiegi join (zabiegi.getPilkarz() group as _aux1).(_aux0 where _aux1 == pilkarz))'
    '
     **/
    public java.util.Collection<pl.wcislo.sbql4j.java.model.runtime.Struct> executeQuery(
        final ObjectContainerBase ocb, final Transaction t) {
        final LocalTransaction transLocal = (LocalTransaction) t;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _ident_Pilkarz =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz>();
        ClassMetadata _classMeta39 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Pilkarz");
        long[] _ids39 = _classMeta39.getIDs(transLocal);

        for (long _id39 : _ids39) {
            LazyObjectReference _ref39 = transLocal.lazyReferenceFor((int) _id39);
            _ident_Pilkarz.add((pl.wcislo.sbql4j.javac.test.football.model.Pilkarz) _ref39.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _asResult_pilkarz =
            _ident_Pilkarz;
        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _groupAsResult_aux0 =
            _asResult_pilkarz;
        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _dotEl2 =
            _groupAsResult_aux0;
        final java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Zabieg> _ident_Zabieg =
            new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Zabieg>();
        ClassMetadata _classMeta40 = ocb.classCollection()
                                        .getClassMetadata("pl.wcislo.sbql4j.javac.test.football.model.Zabieg");
        long[] _ids40 = _classMeta40.getIDs(transLocal);

        for (long _id40 : _ids40) {
            LazyObjectReference _ref40 = transLocal.lazyReferenceFor((int) _id40);
            _ident_Zabieg.add((pl.wcislo.sbql4j.javac.test.football.model.Zabieg) _ref40.getObject());
        }

        java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Zabieg> _asResult_zabiegi =
            _ident_Zabieg;
        java.util.Collection<pl.wcislo.sbql4j.java.model.runtime.Struct> _joinResult =
            new java.util.ArrayList<pl.wcislo.sbql4j.java.model.runtime.Struct>();
        int _joinIndex = 0;

        for (pl.wcislo.sbql4j.javac.test.football.model.Zabieg _joinEl : _asResult_zabiegi) {
            if (_joinEl != null) {
                ocb.activate(_joinEl, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Zabieg _ident_zabiegi = _joinEl;

            if (_ident_zabiegi != null) {
                ocb.activate(_ident_zabiegi, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Zabieg _dotEl = _ident_zabiegi;

            if (_ident_zabiegi != null) {
                ocb.activate(_ident_zabiegi, 2);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _mth_getPilkarzResult =
                _dotEl.getPilkarz();

            if (_mth_getPilkarzResult != null) {
                ocb.activate(_mth_getPilkarzResult, 1);
            }

            pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _groupAsResult_aux1 =
                _mth_getPilkarzResult;
            pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _dotEl1 = _groupAsResult_aux1;
            java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _ident__aux0 =
                _dotEl2;

            if (_ident__aux0 != null) {
                ocb.activate(_ident__aux0, 2);
            }

            java.util.Collection<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz> _whereResult =
                new java.util.ArrayList<pl.wcislo.sbql4j.javac.test.football.model.Pilkarz>();
            int _whereLoopIndex = 0;

            for (pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _whereEl : _ident__aux0) {
                if (_whereEl == null) {
                    continue;
                }

                if (_whereEl != null) {
                    ocb.activate(_whereEl, 1);
                }

                pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _ident__aux1 = _dotEl1;

                if (_ident__aux1 != null) {
                    ocb.activate(_ident__aux1, 1);
                }

                pl.wcislo.sbql4j.javac.test.football.model.Pilkarz _ident_pilkarz =
                    _whereEl;

                if (_ident_pilkarz != null) {
                    ocb.activate(_ident_pilkarz, 1);
                }

                java.lang.Boolean _equalsResult = OperatorUtils.equalsSafe(_ident__aux1,
                        _ident_pilkarz);

                if (_equalsResult) {
                    _whereResult.add(_whereEl);
                }

                _whereLoopIndex++;
            }

            _joinResult.addAll(OperatorUtils.cartesianProductSC(_joinEl,
                    _whereResult, "zabiegi", "pilkarz"));
            _joinIndex++;
        }

        pl.wcislo.sbql4j.db4o.utils.DerefUtils.activateResult(_joinResult, ocb);

        return _joinResult;
    }
}
